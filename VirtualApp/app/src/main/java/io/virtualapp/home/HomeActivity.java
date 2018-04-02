package io.virtualapp.home;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lody.virtual.GmsSupport;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.ParamSettings;
import com.lody.virtual.helper.SharedPreferencesUtils;
import com.show.api.ShowApiRequest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import io.virtualapp.R;
import io.virtualapp.VCommends;
import io.virtualapp.abs.nestedadapter.SmartRecyclerAdapter;
import io.virtualapp.abs.ui.VActivity;
import io.virtualapp.abs.ui.VUiKit;
import io.virtualapp.home.adapters.LaunchpadAdapter;
import io.virtualapp.home.adapters.decorations.ItemOffsetDecoration;
import io.virtualapp.home.location.VirtualLocationSettings;
import io.virtualapp.home.models.AddAppButton;
import io.virtualapp.home.models.AppData;
import io.virtualapp.home.models.AppInfo;
import io.virtualapp.home.models.AppInfoLite;
import io.virtualapp.home.models.EmptyAppData;
import io.virtualapp.home.models.MultiplePackageAppData;
import io.virtualapp.home.models.PackageAppData;
import io.virtualapp.home.repo.AppRepository;
import io.virtualapp.utils.ConfigureLog4J;
import io.virtualapp.utils.CrashHandler;
import io.virtualapp.utils.HttpUtils;
import io.virtualapp.widgets.TwoGearsView;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;
import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.END;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;
import static android.support.v7.widget.helper.ItemTouchHelper.START;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

/**
 * @author Lody
 */
public class HomeActivity extends VActivity implements HomeContract.HomeView {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int INSTALL_OVER = 0x01;
    private static final int INSTALL = 0x02;
    private static final int LAUNCH_INIT = 0x03;
    private static final int AUTO_OP = 0x04;
    private static final int EXE = 0x05;
    private static final int ACCOUNT_OP = 0x06;
    private static final int ACCOUNT_AUTO_OP = 0x07;
    private static final int ACCOUNT_EXE = 0x08;
    private static final int CHECK_VALIDATION = 0x09;
    private static final String KEY = "KEY";
    private static final long CHECK_DELAY = 60000 * 10;
    private static final String HOOK_APK = "com.mx.browser";

    private static final int REQUEST_BATCH_LOGIN = 1000;
    private static final int REQUEST_BIND_ID = 1001;
//    private static final String HOOK_APK = "com.example.kevin.deviceinfo";


    private HomeContract.HomePresenter mPresenter;
    private TwoGearsView mLoadingView;
    private RecyclerView mLauncherView;
    private View mMenuView;
    private PopupMenu mPopupMenu;
    private View mBottomArea;
    private View mCreateShortcutBox;
    private TextView mCreateShortcutTextView;
    private View mDeleteAppBox;
    private TextView mDeleteAppTextView;
    private LaunchpadAdapter mLaunchpadAdapter;
    private Handler mUiHandler;
    private AppRepository mRepository;
    private Handler handler;
    private boolean batchInstall;
    private AppInfo appBatchInfo;
    private int currentLaunchIndex;
    private int currentOpIndex;
    private String[] currnentOp;
    private int MAX_EMULATOR = SettingsDialog.DEFAULT_MAX_EMULATOR;
    private int TIME_BEGIN;
    private int TIME_RANDOM;
    private int accountLaunchIndex;//自动添加账号的位置
    private String[] mAccountLines;
    private TextView popText;
    private WindowService.MyBinder myBinder;
    private String key;
    //    private String deviceInfo;
    private int readMode;
    private boolean onlyOnePro;
    private Logger log;
    private ArrayList<String> wapnets;

    public static void goHome(Context context, String encrypt) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(KEY, encrypt);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        key = getIntent().getStringExtra(KEY);
        MAX_EMULATOR = (int) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.MAX_EMULATOR, SettingsDialog.DEFAULT_MAX_EMULATOR);
        TIME_BEGIN = (int) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.TIME_BEGIN, SettingsDialog.DEFAULT_TIME);
        TIME_RANDOM = (int) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.TIME_RANDOM, SettingsDialog.DEFAULT_RANDOM);
//        deviceInfo = (String) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.DEVICE, "");
        currentLaunchIndex = (int) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.AUTO_LAUNCH_INDEX, 0);
        readMode = (int) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.SCRIPT_ANI, 0);
        onlyOnePro = (boolean) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.ONLY_ONE_PRO, true);
        setContentView(R.layout.activity_home);
        mUiHandler = new Handler(Looper.getMainLooper());
        bindViews();
        initLaunchpad();
        initMenu();
        mRepository = new AppRepository(this);
        handler = new Myhandler();
        handler.sendEmptyMessageDelayed(CHECK_VALIDATION, CHECK_DELAY);
        ConfigureLog4J configureLog4J = new ConfigureLog4J();
        configureLog4J.configure();
        //初始化 log
        log = Logger.getLogger("VirtualLives");
        CrashHandler.getInstance().init(this, log);
        loadWapNets();
        new HomePresenterImpl(this).start();
    }

    private void loadWapNets() {
        try {
            wapnets = new ArrayList<>();
            InputStream inputStream = getAssets().open("lines.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while (!TextUtils.isEmpty(line = bufferedReader.readLine())) {
                wapnets.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initMenu() {
        mPopupMenu = new PopupMenu(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light), mMenuView);
        Menu menu = mPopupMenu.getMenu();
        setIconEnable(menu, true);
//        menu.add("账号").setIcon(R.drawable.ic_account).setOnMenuItemClickListener(item -> {
//            List<VUserInfo> users = VUserManager.get().getUsers();
//            List<String> names = new ArrayList<>(users.size());
//            for (VUserInfo info : users) {
//                names.add(info.name);
//            }
//            CharSequence[] items = new CharSequence[names.size()];
//            for (int i = 0; i < names.size(); i++) {
//                items[i] = names.get(i);
//            }
//            new AlertDialog.Builder(this)
//                    .setTitle("Please select an user")
//                    .setItems(items, (dialog, which) -> {
//                        VUserInfo info = users.get(which);
//                        Intent intent = new Intent(this, ChooseTypeAndAccountActivity.class);
//                        intent.putExtra(ChooseTypeAndAccountActivity.KEY_USER_ID, info.id);
//                        startActivity(intent);
//                    }).show();
//            return false;
//        });
        menu.add("添加通讯录(30/次)").setIcon(R.drawable.ic_account).setOnMenuItemClickListener(item -> {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            startActivityForResult(intent, 1001);
            int index = 0;
            while (index < 30) {
                addContact(getRandomChineseName(), getTel());
                index++;
            }
            return false;
        });
        menu.add("批量增减遨游").setIcon(R.drawable.ic_vs).setOnMenuItemClickListener(item -> {
            List<AppInfo> appInfos = mRepository.convertPackageInfoToAppData(this, getPackageManager().getInstalledPackages(0), true, HOOK_APK);
//            List<AppInfo> appInfos = mRepository.convertPackageInfoToAppData(this, getPackageManager().getInstalledPackages(0), true, "com.example.kevin.deviceinfo");
            if (appInfos.size() > 0) {
                appBatchInfo = appInfos.get(0);
                int installedApp = mLaunchpadAdapter.getList().size();
                if (installedApp < MAX_EMULATOR) {
                    batchInstall = true;
                    handler.sendEmptyMessage(INSTALL);
                }
                if (installedApp > MAX_EMULATOR) {
                    for (int i = mLaunchpadAdapter.getList().size() - 1; i >= MAX_EMULATOR; i--) {
                        mPresenter.deleteApp(mLaunchpadAdapter.getList().get(i));
                    }
                }
            }
            return false;
        });
        menu.add("批量登录遨游").setIcon(R.drawable.ic_notification).setOnMenuItemClickListener(item -> {
//            if (TextUtils.isEmpty(deviceInfo)) {
//                Toast.makeText(this, "请先导入设备号", Toast.LENGTH_SHORT).show();
//                return false;
//            }
            startActivityForResult(new Intent(HomeActivity.this, AccountActivity.class), REQUEST_BATCH_LOGIN);
            return false;
        });
        menu.add("批量模拟操作").setIcon(R.drawable.ic_notification).setOnMenuItemClickListener(item -> {
//            Toast.makeText(this, "The coming", Toast.LENGTH_SHORT).show();
//            if (TextUtils.isEmpty(deviceInfo)) {
//                Toast.makeText(this, "请先导入设备号", Toast.LENGTH_SHORT).show();
//                return false;
//            }
            handler.sendEmptyMessage(LAUNCH_INIT);
            return false;
        });
        menu.add("虚拟定位").setIcon(R.drawable.ic_notification).setOnMenuItemClickListener(item -> {
            startActivity(new Intent(this, VirtualLocationSettings.class));
            return true;
        });
        menu.add("模拟脚本").setIcon(R.drawable.ic_notification).setOnMenuItemClickListener(item -> {
            selectScript();
            return true;
        });
        menu.add("设置").setIcon(R.drawable.ic_settings).setOnMenuItemClickListener(item -> {
            SettingsDialog settingsDialog = new SettingsDialog(this);
            settingsDialog.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MAX_EMULATOR = settingsDialog.getMaxNumber();
                    SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.MAX_EMULATOR, MAX_EMULATOR);
                    TIME_BEGIN = settingsDialog.getTimeBegin();
                    SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.TIME_BEGIN, TIME_BEGIN);
                    TIME_RANDOM = settingsDialog.getTimeRandom();
                    SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.TIME_RANDOM, TIME_RANDOM);
                    currentLaunchIndex = settingsDialog.getPosition() - 1;
                    SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.AUTO_LAUNCH_INDEX, currentLaunchIndex);
                    onlyOnePro = settingsDialog.isOnly5Pro();
                    SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.ONLY_ONE_PRO, onlyOnePro);
                    settingsDialog.dismiss();
                }
            });
            settingsDialog.setNegativeButton("Cancel", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    settingsDialog.dismiss();
                }
            });
            settingsDialog.show();
            return false;
        });
        mMenuView.setOnClickListener(v -> mPopupMenu.show());
    }

    private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

    private static String getTel() {
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + third;
    }

    public static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    private String getRandomChineseName() {
        int index = new Random().nextInt(Surname.length - 1);
        String name = Surname[index]; //获得一个随机的姓氏

        /* 从常用字中选取一个或两个字作为名 */
        if (new Random().nextBoolean()) {
            name += getChinese() + getChinese();
        } else {
            name += getChinese();
        }
        return name;
    }

    /* 598 百家姓 */
    String[] Surname = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
            "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
            "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷",
            "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和",
            "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒",
            "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危", "江", "童", "颜", "郭", "梅", "盛", "林", "刁", "钟",
            "徐", "邱", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍", "虞", "万", "支", "柯", "昝", "管", "卢", "莫", "经", "房", "裘", "缪", "干", "解", "应",
            "宗", "丁", "宣", "贲", "邓", "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉", "钮", "龚", "程", "嵇", "邢", "滑", "裴", "陆", "荣", "翁", "荀",
            "羊", "于", "惠", "甄", "曲", "家", "封", "芮", "羿", "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫", "乌", "焦", "巴", "弓", "牧", "隗", "山",
            "谷", "车", "侯", "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫", "宁", "仇", "栾", "暴", "甘", "钭", "厉", "戎", "祖", "武", "符", "刘", "景",
            "詹", "束", "龙", "叶", "幸", "司", "韶", "郜", "黎", "蓟", "溥", "印", "宿", "白", "怀", "蒲", "邰", "从", "鄂", "索", "咸", "籍", "赖", "卓", "蔺", "屠",
            "蒙", "池", "乔", "阴", "郁", "胥", "能", "苍", "双", "闻", "莘", "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵", "冉", "宰", "郦", "雍", "却",
            "璩", "桑", "桂", "濮", "牛", "寿", "通", "边", "扈", "燕", "冀", "浦", "尚", "农", "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连", "茹", "习",
            "宦", "艾", "鱼", "容", "向", "古", "易", "慎", "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿", "满", "弘", "匡", "国", "文", "寇", "广", "禄",
            "阙", "东", "欧", "殳", "沃", "利", "蔚", "越", "夔", "隆", "师", "巩", "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚", "那", "简", "饶", "空",
            "曾", "毋", "沙", "乜", "养", "鞠", "须", "丰", "巢", "关", "蒯", "相", "查", "后", "荆", "红", "游", "郏", "竺", "权", "逯", "盖", "益", "桓", "公", "仉",
            "督", "岳", "帅", "缑", "亢", "况", "郈", "有", "琴", "归", "海", "晋", "楚", "闫", "法", "汝", "鄢", "涂", "钦", "商", "牟", "佘", "佴", "伯", "赏", "墨",
            "哈", "谯", "篁", "年", "爱", "阳", "佟", "言", "福", "南", "火", "铁", "迟", "漆", "官", "冼", "真", "展", "繁", "檀", "祭", "密", "敬", "揭", "舜", "楼",
            "疏", "冒", "浑", "挚", "胶", "随", "高", "皋", "原", "种", "练", "弥", "仓", "眭", "蹇", "覃", "阿", "门", "恽", "来", "綦", "召", "仪", "风", "介", "巨",
            "木", "京", "狐", "郇", "虎", "枚", "抗", "达", "杞", "苌", "折", "麦", "庆", "过", "竹", "端", "鲜", "皇", "亓", "老", "是", "秘", "畅", "邝", "还", "宾",
            "闾", "辜", "纵", "侴", "万俟", "司马", "上官", "欧阳", "夏侯", "诸葛", "闻人", "东方", "赫连", "皇甫", "羊舌", "尉迟", "公羊", "澹台", "公冶", "宗正",
            "濮阳", "淳于", "单于", "太叔", "申屠", "公孙", "仲孙", "轩辕", "令狐", "钟离", "宇文", "长孙", "慕容", "鲜于", "闾丘", "司徒", "司空", "兀官", "司寇",
            "南门", "呼延", "子车", "颛孙", "端木", "巫马", "公西", "漆雕", "车正", "壤驷", "公良", "拓跋", "夹谷", "宰父", "谷梁", "段干", "百里", "东郭", "微生",
            "梁丘", "左丘", "东门", "西门", "南宫", "第五", "公仪", "公乘", "太史", "仲长", "叔孙", "屈突", "尔朱", "东乡", "相里", "胡母", "司城", "张廖", "雍门",
            "毋丘", "贺兰", "綦毋", "屋庐", "独孤", "南郭", "北宫", "王孙"};

    public static String getChinese() {
        String str = null;
        int highPos, lowPos;
        Random random = new Random();
        highPos = (176 + Math.abs(random.nextInt(71)));//区码，0xA0打头，从第16区开始，即0xB0=11*16=176,16~55一级汉字，56~87二级汉字
        random = new Random();
        lowPos = 161 + Math.abs(random.nextInt(94));//位码，0xA0打头，范围第1~94列

        byte[] bArr = new byte[2];
        bArr[0] = (new Integer(highPos)).byteValue();
        bArr[1] = (new Integer(lowPos)).byteValue();
        try {
            str = new String(bArr, "GB2312");   //区位码组合成汉字
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void addContact(String name, String phoneNumber) {
        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();

        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // 内容类型
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // 联系人名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        // 向联系人URI添加联系人名字
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        // 联系人的电话号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        // 电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        // 向联系人电话号码URI添加电话号码
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);

//        Toast.makeText(this, "联系人数据添加成功", Toast.LENGTH_SHORT).show();
    }

    public void exeCommand(String[] order) {
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        try {
            if (order[1].equals("swipe")) {
                order[2] = Float.parseFloat(order[2]) * screenWidth + "";
                order[3] = Float.parseFloat(order[3]) * screenHeight + "";
                order[4] = Float.parseFloat(order[4]) * screenWidth + "";
                order[5] = Float.parseFloat(order[5]) * screenHeight + "";
            } else if (order[1].equals("tap")) {
                order[2] = Float.parseFloat(order[2]) * screenWidth + "";
                order[3] = Float.parseFloat(order[3]) * screenHeight + "";
            } else if (order[1].equals("text")) {
                switch (order[2]) {
                    case "<account>":
                        order[2] = mAccountLines[accountLaunchIndex - 1].substring(mAccountLines[accountLaunchIndex - 1].indexOf(";") + 1).split(",")[0];
                        break;
                    case "<password>":
                        order[2] = mAccountLines[accountLaunchIndex - 1].substring(mAccountLines[accountLaunchIndex - 1].indexOf(";") + 1).split(",")[1];
                        break;
                    case "<net>":
                        order[2] = wapnets.get(new Random().nextInt(wapnets.size()));
                        break;
                }

            }
            new ProcessBuilder(order).start();
        } catch (IOException e) {
            Log.i("GK", e.getMessage());
            e.printStackTrace();
        }
    }

    private void selectScript() {
        final String[] items = {"随机浏览", "混乱浏览"};
        readMode = -1;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(HomeActivity.this);
        singleChoiceDialog.setTitle("浏览模式");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, (int) SharedPreferencesUtils.getParam(HomeActivity.this, SharedPreferencesUtils.SCRIPT_ANI, 0),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        readMode = which;
                        SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.SCRIPT_ANI, readMode);
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (readMode != -1) {
                            Toast.makeText(HomeActivity.this,
                                    "你选择了" + items[readMode],
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    private String getRamdomSearchText() {
        String[] text = {"Music", "android dev", "AI", "tech", "heroes", "jack chen", "shenhua", "movie", "love", "qinghua", "guodegang"};
//        String[] text = {"喜乐街", "开心麻花", "开发工具", "美妆", "笑话", "军事", "娱乐节目", "电影", "Android学习资料", "MAC学习", "电视剧", "郭德纲", "高圆圆", "政协大会", "天天向上", "AI"};
        return text[new Random().nextInt(text.length)];
    }

    private static void setIconEnable(Menu menu, boolean enable) {
        try {
            @SuppressLint("PrivateApi")
            Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            m.invoke(menu, enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews() {
        mLoadingView = (TwoGearsView) findViewById(R.id.pb_loading_app);
        mLauncherView = (RecyclerView) findViewById(R.id.home_launcher);
        mMenuView = findViewById(R.id.home_menu);
        mBottomArea = findViewById(R.id.bottom_area);
        mCreateShortcutBox = findViewById(R.id.create_shortcut_area);
        mCreateShortcutTextView = (TextView) findViewById(R.id.create_shortcut_text);
        mDeleteAppBox = findViewById(R.id.delete_app_area);
        mDeleteAppTextView = (TextView) findViewById(R.id.delete_app_text);
    }

    private void initLaunchpad() {
        mLauncherView.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(5, OrientationHelper.VERTICAL);
        mLauncherView.setLayoutManager(layoutManager);
        mLaunchpadAdapter = new LaunchpadAdapter(this);
        SmartRecyclerAdapter wrap = new SmartRecyclerAdapter(mLaunchpadAdapter);
        View footer = new View(this);
        footer.setLayoutParams(new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VUiKit.dpToPx(this, 60)));
        wrap.setFooterView(footer);
        mLauncherView.setAdapter(wrap);
        mLauncherView.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.desktop_divider));
        ItemTouchHelper touchHelper = new ItemTouchHelper(new LauncherTouchCallback());
        touchHelper.attachToRecyclerView(mLauncherView);
        mLaunchpadAdapter.setAppClickListener((pos, data) -> {
//            if (TextUtils.isEmpty(deviceInfo)) {
//                Toast.makeText(this, "请先导入设备号", Toast.LENGTH_SHORT).show();
//                return;
//            }
            if (!data.isLoading()) {
                if (data instanceof AddAppButton) {
                    onAddAppButtonClick();
                }
                mLaunchpadAdapter.notifyItemChanged(pos);
                mPresenter.launchApp(data);
            }
        });
    }

    private void onAddAppButtonClick() {
        ListAppActivity.gotoListApp(this);
    }

    private void deleteApp(int position) {
        AppData data = mLaunchpadAdapter.getList().get(position);
        new AlertDialog.Builder(this)
                .setTitle("Delete app")
                .setMessage("Do you want to delete " + data.getName() + "?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    mPresenter.deleteApp(data);
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void createShortcut(int position) {
        AppData model = mLaunchpadAdapter.getList().get(position);
        if (model instanceof PackageAppData || model instanceof MultiplePackageAppData) {
            mPresenter.createShortcut(model);
        }
    }

    @Override
    public void setPresenter(HomeContract.HomePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showBottomAction() {
        mBottomArea.setTranslationY(mBottomArea.getHeight());
        mBottomArea.setVisibility(View.VISIBLE);
        mBottomArea.animate().translationY(0).setDuration(500L).start();
    }

    @Override
    public void hideBottomAction() {
        mBottomArea.setTranslationY(0);
        ObjectAnimator transAnim = ObjectAnimator.ofFloat(mBottomArea, "translationY", 0, mBottomArea.getHeight());
        transAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mBottomArea.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                mBottomArea.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        transAnim.setDuration(500L);
        transAnim.start();
    }

    @Override
    public void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingView.startAnim();
    }

    @Override
    public void hideLoading() {
        mLoadingView.setVisibility(View.GONE);
        mLoadingView.stopAnim();
    }

    @Override
    public void loadFinish(List<AppData> list) {
//        list.add(new AddAppButton(this));
        mLaunchpadAdapter.setList(list);
        hideLoading();
    }

    @Override
    public void loadError(Throwable err) {
        err.printStackTrace();
        hideLoading();
    }

    @Override
    public void showGuide() {

    }

    @Override
    public void addAppToLauncher(AppData model) {
        List<AppData> dataList = mLaunchpadAdapter.getList();
        boolean replaced = false;
        for (int i = 0; i < dataList.size(); i++) {
            AppData data = dataList.get(i);
            if (data instanceof EmptyAppData) {
                mLaunchpadAdapter.replace(i, model);
                replaced = true;
                handler.sendEmptyMessage(INSTALL_OVER);
                break;
            }
        }
        if (!replaced) {
            handler.sendEmptyMessage(INSTALL_OVER);
            mLaunchpadAdapter.add(model);
            mLauncherView.smoothScrollToPosition(mLaunchpadAdapter.getItemCount() - 1);
        }

    }


    @Override
    public void removeAppToLauncher(AppData model) {
        mLaunchpadAdapter.remove(model);
    }

    @Override
    public void refreshLauncherItem(AppData model) {
        mLaunchpadAdapter.refresh(model);
    }

    @Override
    public void askInstallGms() {
        new AlertDialog.Builder(this)
                .setTitle("Hi")
                .setMessage("We found that your device has been installed the Google service, whether you need to install them?")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    defer().when(() -> {
                        GmsSupport.installGApps(0);
                    }).done((res) -> {
                        mPresenter.dataChanged();
                    });
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) ->
                        Toast.makeText(HomeActivity.this, "You can also find it in the Settings~", Toast.LENGTH_LONG).show())
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BATCH_LOGIN) {
            if (resultCode == RESULT_OK && data != null) {
                String content = data.getStringExtra(AccountActivity.CONTENT);
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                accountLaunchIndex = Integer.parseInt(content.substring(0, content.indexOf("\n"))) - 1;
                mAccountLines = content.substring(content.indexOf("\n") + 1).split("\n");
                handler.sendEmptyMessage(ACCOUNT_OP);
            }
            return;
        }
//        if (requestCode == REQUEST_BIND_ID && resultCode == RESULT_OK) {
//            Uri uri = data.getData();
//            deviceInfo = readDeviceTxt(uri);
//            SharedPreferencesUtils.setParam(this, SharedPreferencesUtils.DEVICE, deviceInfo);
//            Toast.makeText(HomeActivity.this, "綁定设备号成功！", Toast.LENGTH_LONG).show();
//            return;
//        }
        if (resultCode == RESULT_OK && data != null) {
            List<AppInfoLite> appList = data.getParcelableArrayListExtra(VCommends.EXTRA_APP_INFO_LIST);
            if (appList != null) {
                for (AppInfoLite info : appList) {
                    mPresenter.addApp(info);
                }
            }
        }
    }

    /**
     * 读取assets下的txt文件，返回utf-8 String
     *
     * @return
     */
    public String readDeviceTxt(Uri uri) {

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while ((n = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            String text = new String(out.toByteArray());
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }

    private class LauncherTouchCallback extends ItemTouchHelper.SimpleCallback {

        int[] location = new int[2];
        boolean upAtDeleteAppArea;
        boolean upAtCreateShortcutArea;
        RecyclerView.ViewHolder dragHolder;

        LauncherTouchCallback() {
            super(UP | DOWN | LEFT | RIGHT | START | END, 0);
        }

        @Override
        public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int viewSize, int viewSizeOutOfBounds, int totalSize, long msSinceStartScroll) {
            return 0;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            try {
                AppData data = mLaunchpadAdapter.getList().get(viewHolder.getAdapterPosition());
                if (!data.canReorder()) {
                    return makeMovementFlags(0, 0);
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return super.getMovementFlags(recyclerView, viewHolder);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int pos = viewHolder.getAdapterPosition();
            int targetPos = target.getAdapterPosition();
            mLaunchpadAdapter.moveItem(pos, targetPos);
            return true;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (viewHolder instanceof LaunchpadAdapter.ViewHolder) {
                if (actionState == ACTION_STATE_DRAG) {
                    if (dragHolder != viewHolder) {
                        dragHolder = viewHolder;
                        viewHolder.itemView.setScaleX(1.2f);
                        viewHolder.itemView.setScaleY(1.2f);
                        if (mBottomArea.getVisibility() == View.GONE) {
                            showBottomAction();
                        }
                    }
                }
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
            if (upAtCreateShortcutArea || upAtDeleteAppArea) {
                return false;
            }
            try {
                AppData data = mLaunchpadAdapter.getList().get(target.getAdapterPosition());
                return data.canReorder();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof LaunchpadAdapter.ViewHolder) {
                LaunchpadAdapter.ViewHolder holder = (LaunchpadAdapter.ViewHolder) viewHolder;
                viewHolder.itemView.setScaleX(1f);
                viewHolder.itemView.setScaleY(1f);
                viewHolder.itemView.setBackgroundColor(holder.color);
            }
            super.clearView(recyclerView, viewHolder);
            if (dragHolder == viewHolder) {
                if (mBottomArea.getVisibility() == View.VISIBLE) {
                    mUiHandler.postDelayed(HomeActivity.this::hideBottomAction, 200L);
                    if (upAtCreateShortcutArea) {
                        createShortcut(viewHolder.getAdapterPosition());
                    } else if (upAtDeleteAppArea) {
                        deleteApp(viewHolder.getAdapterPosition());
                    }
                }
                dragHolder = null;
            }
        }


        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            if (actionState != ACTION_STATE_DRAG || !isCurrentlyActive) {
                return;
            }
            View itemView = viewHolder.itemView;
            itemView.getLocationInWindow(location);
            int x = (int) (location[0] + dX);
            int y = (int) (location[1] + dY);

            mBottomArea.getLocationInWindow(location);
            int baseLine = location[1] - mBottomArea.getHeight();
            if (y >= baseLine) {
                mDeleteAppBox.getLocationInWindow(location);
                int deleteAppAreaStartX = location[0];
                if (x < deleteAppAreaStartX) {
                    upAtCreateShortcutArea = true;
                    upAtDeleteAppArea = false;
                    mCreateShortcutTextView.setTextColor(Color.parseColor("#0099cc"));
                    mDeleteAppTextView.setTextColor(Color.WHITE);
                } else {
                    upAtDeleteAppArea = true;
                    upAtCreateShortcutArea = false;
                    mDeleteAppTextView.setTextColor(Color.parseColor("#0099cc"));
                    mCreateShortcutTextView.setTextColor(Color.WHITE);
                }
            } else {
                upAtCreateShortcutArea = false;
                upAtDeleteAppArea = false;
                mDeleteAppTextView.setTextColor(Color.WHITE);
                mCreateShortcutTextView.setTextColor(Color.WHITE);
            }
        }
    }

    private class Myhandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INSTALL_OVER:
                    if (batchInstall) {
                        int installedApp = mLaunchpadAdapter.getList().size();
                        if (installedApp < MAX_EMULATOR) {
                            sendEmptyMessage(INSTALL);
                        } else {
                            batchInstall = false;
                        }
                    }
                    break;
                case INSTALL:
                    mPresenter.addApp(new AppInfoLite(appBatchInfo.packageName, appBatchInfo.path, appBatchInfo.fastOpen));
                    break;
                case LAUNCH_INIT:
                    launchApp(currentLaunchIndex);

                    SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.AUTO_LAUNCH_INDEX, currentLaunchIndex);
                    currentLaunchIndex++;
                    if (currentLaunchIndex >= mLaunchpadAdapter.getList().size()) {
                        currentLaunchIndex = 0;
                    }
//                    currnentOp = getOpByScriptType();//ParamSettings.batchOps[2];
                    currnentOp = ParamSettings.batchOps[1];
                    startAniScript();
                    sendEmptyMessageDelayed(LAUNCH_INIT, TIME_BEGIN * 60 * 1000 + (TIME_RANDOM != 0 ? new Random().nextInt(TIME_RANDOM * 60 * 1000) : 0));
                    break;
                case AUTO_OP:
                    if (currentOpIndex < currnentOp.length && msg.arg1 == currentLaunchIndex) {
                        String currentCommand = currnentOp[currentOpIndex];
                        int delay = Integer.parseInt(currentCommand.substring(0, currentCommand.indexOf(",")));
                        String[] opParam = currentCommand.substring(currentCommand.indexOf(",") + 1, currentCommand.length()).split(",");
                        Message message = new Message();
                        message.what = EXE;
                        message.arg1 = msg.arg1;
                        message.obj = opParam;
                        sendMessageDelayed(message, delay);
                    } else {
                        currentOpIndex = 0;
                        if (msg.arg1 == currentLaunchIndex) {
                            if (currnentOp == ParamSettings.batchOps[1]) {
                                currnentOp = ParamSettings.batchOps[3];
                            } else {
                                if (currnentOp == ParamSettings.batchOps[3]) {
                                    if (readMode != 0) {
//                                        currnentOp = ParamSettings.batchOps[3];
//                                    } else {
                                        currnentOp = ParamSettings.batchOps[2];
                                    }
                                } else {
                                    currnentOp = ParamSettings.batchOps[3];
                                }
                            }
                            startAniScript();
                        }
                    }
                    break;
                case EXE:
                    if (currentLaunchIndex == msg.arg1) {
                        String[] param = (String[]) msg.obj;
                        exeCommand(param);
                        currentOpIndex++;
                        Message message = new Message();
                        message.what = AUTO_OP;
                        message.arg1 = msg.arg1;
                        sendMessage(message);
                    } else {
                        currentOpIndex = 0;
                    }
                    break;
                case ACCOUNT_AUTO_OP:
                    if (currentOpIndex < currnentOp.length) {
                        String currentCommand = currnentOp[currentOpIndex];
                        int delay = Integer.parseInt(currentCommand.substring(0, currentCommand.indexOf(",")));
                        String[] opParam = currentCommand.substring(currentCommand.indexOf(",") + 1, currentCommand.length()).split(",");
                        Message message = new Message();
                        message.what = ACCOUNT_EXE;
                        message.obj = opParam;
                        sendMessageDelayed(message, delay);
                    } else {
                        currentOpIndex = 0;
                    }
                    break;
                case ACCOUNT_EXE:
                    String[] param = (String[]) msg.obj;
                    exeCommand(param);
                    currentOpIndex++;
                    sendEmptyMessage(ACCOUNT_AUTO_OP);
                    break;
                case ACCOUNT_OP:
                    launchApp(accountLaunchIndex);
                    String line = mAccountLines[accountLaunchIndex];
                    String type = line.substring(0, line.indexOf(";"));
                    accountLaunchIndex++;
                    if (accountLaunchIndex < mLaunchpadAdapter.getList().size() && accountLaunchIndex < mAccountLines.length) {
                        sendEmptyMessageDelayed(ACCOUNT_OP, 90 * 1000);
                    } else {
                        break;
                    }
                    currnentOp = getOpByAccountOp(type);
                    sendEmptyMessage(ACCOUNT_AUTO_OP);
                    break;
                case CHECK_VALIDATION:
                    HttpUtils.requestNetForGetLogin(key, new HttpUtils.HttpCallBack() {

                        @Override
                        public void callback(boolean value) {
                            if (value) {
                                sendEmptyMessageDelayed(CHECK_VALIDATION, CHECK_DELAY);
                            } else {
                                log.info("后台验证失效，退出程序！");
                                VirtualCore.get().killAllApps();
                                finish();
                            }
                        }
                    }, false);
                    break;
            }
        }

        private void startAniScript() {
            Message message = new Message();
            message.what = AUTO_OP;
            message.arg1 = currentLaunchIndex;
            sendMessage(message);
            currentOpIndex = 0;
        }
    }

    private void launchApp(int currentLaunchIndex) {
        mLaunchpadAdapter.notifyItemChanged(currentLaunchIndex);
        mPresenter.launchApp(mLaunchpadAdapter.getList().get(currentLaunchIndex));
        AppData appData = mLaunchpadAdapter.getList().get(currentLaunchIndex);
        int lastIndex = (currentLaunchIndex - 1 + mLaunchpadAdapter.getList().size()) % mLaunchpadAdapter.getList().size();
        if (appData instanceof PackageAppData) {
            Toast.makeText(HomeActivity.this, "当前启动 1 号程序", Toast.LENGTH_SHORT).show();

//            MultiplePackageAppData multipleData = (MultiplePackageAppData) mLaunchpadAdapter.getList().get(lastIndex);
//            if (VirtualCore.get().isPackageLaunched(multipleData.userId, HOOK_APK)) {
//                log.info("当前 " + (multipleData.userId + 1) + " 号程序退到后台");
//            }

            log.info("当前启动 1 号程序");
        } else {
            MultiplePackageAppData multipleData = (MultiplePackageAppData) appData;
            Toast.makeText(HomeActivity.this, "当前启动 " + (multipleData.userId + 1) + " 号程序", Toast.LENGTH_SHORT).show();

//            AppData lastAppData = mLaunchpadAdapter.getList().get(lastIndex);
//            int userId = lastAppData instanceof MultiplePackageAppData ? ((MultiplePackageAppData) appData).userId : 0;
//            if (VirtualCore.get().isPackageLaunched(userId, HOOK_APK)) {
//                log.info("当前 " + (userId + 1) + " 号程序退到后台");
//            }

            log.info("当前启动 " + (multipleData.userId + 1) + " 号程序");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String res = new ShowApiRequest("http://route.showapi.com/632-1", "60687", "8b5f6cc7fb264367b8e1eca18e994813")
                        .post();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONObject info = jsonObject.getJSONObject("showapi_res_body");
                    log.info("当前网络: " + info.getString("region") + " - " + info.getString("city") + " - " + info.getString("isp") + " - " + info.getString("ip"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();


        if (onlyOnePro) {
            //杀死上面第5个应用
            int last5Index = (currentLaunchIndex - 1 + mLaunchpadAdapter.getList().size()) % mLaunchpadAdapter.getList().size();
            appData = mLaunchpadAdapter.getList().get(last5Index);
            if (appData instanceof PackageAppData) {
                VirtualCore.get().killApp(HOOK_APK, 0);
                log.info("后台杀死 1 号程序");
            } else {
                MultiplePackageAppData multipleData = (MultiplePackageAppData) appData;
                VirtualCore.get().killApp(HOOK_APK, multipleData.userId);
                log.info("后台杀死 " + multipleData.userId + " 号程序");
            }
        }
    }

    private String[] getOpByAccountOp(String type) {
        switch (type) {
            case "login":
                currnentOp = ParamSettings.batchOps[0];
                return currnentOp;
            case "signup":
                break;
            case "signupB":
                break;
        }
        return null;
    }

    private String[] getOpByScriptType() {
        SensorManager sm = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensoGyros = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        switch (readMode) {
            case 0://随机阅读
                return ParamSettings.batchOps[2];
            case 1://混乱模式
                return ParamSettings.batchOps[1];
            default:
                return ParamSettings.batchOps[1];
        }
    }
}
