package io.virtualapp.home;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lody.virtual.GmsSupport;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.client.stub.DaemonService;
import com.lody.virtual.helper.SharedPreferencesUtils;
import com.lody.virtual.helper.utils.CallbackEvent;
import com.lody.virtual.helper.utils.ConfigureLog4J;
import com.lody.virtual.helper.utils.CrashHandler;
import com.lody.virtual.helper.utils.MD5Utils;
import com.lody.virtual.helper.utils.MessageEvent;
import com.lody.virtual.helper.utils.Tools;
import com.lody.virtual.remote.InstalledAppInfo;
import com.show.api.ShowApiRequest;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.virtualapp.R;
import io.virtualapp.VCommends;
import io.virtualapp.abs.nestedadapter.SmartRecyclerAdapter;
import io.virtualapp.abs.ui.VActivity;
import io.virtualapp.abs.ui.VUiKit;
import io.virtualapp.home.adapters.LaunchpadAdapter;
import io.virtualapp.home.adapters.decorations.ItemOffsetDecoration;
import io.virtualapp.home.models.AddAppButton;
import io.virtualapp.home.models.AppData;
import io.virtualapp.home.models.AppInfo;
import io.virtualapp.home.models.AppInfoLite;
import io.virtualapp.home.models.EmptyAppData;
import io.virtualapp.home.models.LocationData;
import io.virtualapp.home.models.MultiplePackageAppData;
import io.virtualapp.home.models.PackageAppData;
import io.virtualapp.home.repo.AppRepository;
import io.virtualapp.utils.ContactUtil;
import io.virtualapp.utils.HttpUtils;
import io.virtualapp.utils.ParamSettings;
import io.virtualapp.widgets.TwoGearsView;
import mirror.android.util.RootCmd;
import xiaofei.library.hermeseventbus.HermesEventBus;

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
    //    private static final int ACCOUNT_OP = 0x06;
//    private static final int ACCOUNT_AUTO_OP = 0x07;
//    private static final int ACCOUNT_EXE = 0x08;
    private static final int CHECK_VALIDATION = 0x09;
    //    private static final String KEY = "KEY";
    private static final long CHECK_DELAY = 60000 * 10;
    private static final String HOOK_APK = "com.mx.browser";

    private static final int REQUEST_BATCH_LOGIN = 1000;
    private static final int REQUEST_BIND_ID = 1001;
    //        private static final String HOOK_APK = "com.example.kevin.deviceinfo";
    private static final int V_CONTACTS = 0x10;
    private static final int EXE_COMMAND = 0x11;
    private static final int EXE_SEQUENCE = 0x12;
    private static final int REQUEST_NET_SCRIPT = 1002;


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
    private String token;
    //    private String deviceInfo;
    private int readMode;
    private boolean onlyOnePro;
    private Logger log;
    private String[] wapnets;
    private boolean virtualContacts;
    private boolean autoRestart;
    private boolean isEmulator;
    private boolean autoOp;
    private int indexWap;//从0开始循环
    private ArrayList<String> mainWapnets;
    private String[] sequenceCommands;
    private int indexSequence;
    private int sequenceId;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;

    public static void goHome(Context context) {
        SharedPreferencesUtils.setParam(context, SharedPreferencesUtils.AUTO_OP, false);
        SharedPreferencesUtils.setParam(context, SharedPreferencesUtils.LOGIN_NOW, false);
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        key = (String) SharedPreferencesUtils.getParam(HomeActivity.this, SharedPreferencesUtils.KEY, "");
        token = (String) SharedPreferencesUtils.getParam(HomeActivity.this, SharedPreferencesUtils.TOKEN, "");
        MAX_EMULATOR = (int) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.MAX_EMULATOR, SettingsDialog.DEFAULT_MAX_EMULATOR);
        TIME_BEGIN = (int) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.TIME_BEGIN, SettingsDialog.DEFAULT_TIME);
        TIME_RANDOM = (int) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.TIME_RANDOM, SettingsDialog.DEFAULT_RANDOM);
//        deviceInfo = (String) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.DEVICE, "");
        currentLaunchIndex = (int) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.AUTO_LAUNCH_INDEX, 0);
        readMode = (int) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.SCRIPT_ANI, 0);
        onlyOnePro = (boolean) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.ONLY_ONE_PRO, true);
        virtualContacts = (boolean) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.V_CONTACTS, false);
        autoRestart = (boolean) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.AUTO_RESTART, false);
        isEmulator = (boolean) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.EMULATOR, true);
        autoOp = (boolean) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.AUTO_OP, false);
        setContentView(R.layout.activity_home);
        mUiHandler = new Handler(Looper.getMainLooper());
        bindViews();
        initLaunchpad();
        initMenu();
        mRepository = new AppRepository(this);
        handler = new Myhandler();
        handler.sendEmptyMessageDelayed(CHECK_VALIDATION, CHECK_DELAY);
        ConfigureLog4J configureLog4J = new ConfigureLog4J();
        configureLog4J.configure("vl.log");
        //初始化 log
        log = Logger.getLogger("VirtualLives");
        CrashHandler.getInstance().init(this, log);
        loadWapNets();
        loadMainWapNets();
        new HomePresenterImpl(this).start();
        if (getIntent().getBooleanExtra(DaemonService.AUTO_MONI, false) && autoRestart && autoOp) {
            handler.sendEmptyMessageDelayed(LAUNCH_INIT, 3000);
            log.info("虚幻共生重新启动！！！！！！！！！！");
        }
        HermesEventBus.getDefault().register(this);

        if (!isEmulator) {
            windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            params = new WindowManager.LayoutParams();
            popText = getTextView(this, params);
            windowManager.addView(popText, params);
            windowManager.updateViewLayout(popText, params);
        }
    }

    private TextView getTextView(Activity activity, WindowManager.LayoutParams params) {
        TextView popText = new TextView(activity);
        popText.setBackgroundColor(Color.parseColor("#000000"));
        popText.setText("程序:");
        popText.setTextSize(12);
        popText.setTextColor(Color.parseColor("#FFFFFF"));
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        // 设置window type
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.alpha = 1f;  //0为全透明，1为不透明
        boolean emulator = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.EMULATOR, true);
        if (emulator) {
            params.width = 75;
            params.height = 20;
        } else {
            params.width = 150;
            params.height = 45;
        }
        return popText;
    }

    private void loadWapNets() {
        String content = (String) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.NET_SCRIPT_TXT, "");
        if (!TextUtils.isEmpty(content)) {
            wapnets = content.split("\n");
            return;
        }
        try {
            ArrayList<String> temp = new ArrayList<>();
            InputStream inputStream = getAssets().open("lines.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while (!TextUtils.isEmpty(line = bufferedReader.readLine())) {
                temp.add(line);
            }
            bufferedReader.close();
            wapnets = new String[temp.size()];
            for (int i = 0; i < temp.size(); i++) {
                wapnets[i] = temp.get(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMainWapNets() {
        try {
            mainWapnets = new ArrayList<>();
            InputStream inputStream = getAssets().open("auto.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while (!TextUtils.isEmpty(line = bufferedReader.readLine())) {
                mainWapnets.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HermesEventBus.getDefault().unregister(this);
    }

    private void initMenu() {
        mPopupMenu = new PopupMenu(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light), mMenuView);
        Menu menu = mPopupMenu.getMenu();
        setIconEnable(menu, true);
        menu.add("安装遨游").setIcon(R.drawable.ic_notification).setOnMenuItemClickListener(item -> {
            mRepository.installMX(this);
            copyOCRToSDK();
            return true;
        });
        menu.add("批量克隆遨游").setIcon(R.drawable.ic_vs).setOnMenuItemClickListener(item -> {
//            SharedPreferencesUtils.setParam(this, SharedPreferencesUtils.LOGIN_NOW, false);
            List<AppInfo> appInfos = null;
            appInfos = mRepository.convertPackageInfoToAppData(this, getPackageManager().getInstalledPackages(0), true, HOOK_APK);
            if (appInfos.size() > 0) {
                appBatchInfo = appInfos.get(0);
                int installedApp = mLaunchpadAdapter.getList().size();
                if (installedApp < MAX_EMULATOR) {
                    batchInstall = true;
                    handler.sendEmptyMessage(INSTALL);
                }
                if (installedApp > MAX_EMULATOR) {
                    final ProgressDialog proDialog = android.app.ProgressDialog.show(HomeActivity.this, "正在删除遨游....", "请等待....");
                    proDialog.setCancelable(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = mLaunchpadAdapter.getList().size() - 1; i >= MAX_EMULATOR; i--) {
                                mPresenter.deleteApp(mLaunchpadAdapter.getList().get(i));
                            }
                            mLaunchpadAdapter.notifyDataSetChanged();
                            proDialog.dismiss();
                        }
                    }).start();
                }
            } else {
                Toast.makeText(this, "请在手机中安装遨游挖矿浏览器", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        menu.add("批量登录遨游").setIcon(R.drawable.ic_notification).setOnMenuItemClickListener(item -> {

            startActivityForResult(new Intent(HomeActivity.this, AccountActivity.class), REQUEST_BATCH_LOGIN);
            return false;
        });
        menu.add("批量模拟挂机").setIcon(R.drawable.ic_notification).setOnMenuItemClickListener(item -> {

            SharedPreferencesUtils.setParam(this, SharedPreferencesUtils.AUTO_OP, true);
            SharedPreferencesUtils.setParam(this, SharedPreferencesUtils.LOGIN_NOW, false);
            if (virtualContacts) {
                handler.sendEmptyMessage(V_CONTACTS);
            } else {
                handler.sendEmptyMessage(LAUNCH_INIT);
            }
            return false;
        });
//        menu.add("虚拟定位").setIcon(R.drawable.ic_notification).setOnMenuItemClickListener(item -> {
//            startActivity(new Intent(this, VirtualLocationSettings.class));
//            return true;
//        });
        menu.add("模拟脚本类型").setIcon(R.drawable.ic_notification).setOnMenuItemClickListener(item -> {
            selectScript();
            return true;
        });
        menu.add("脚本网站加载").setIcon(R.drawable.ic_notification).setOnMenuItemClickListener(item -> {
            startActivityForResult(new Intent(HomeActivity.this, NetScriptActivity.class), REQUEST_NET_SCRIPT);
            return true;
        });
        menu.add("设置").setIcon(R.drawable.ic_settings).setOnMenuItemClickListener(item -> {
            SettingsDialog settingsDialog = new SettingsDialog(this);
            settingsDialog.setPositiveButton("确定", new View.OnClickListener() {
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
                    virtualContacts = settingsDialog.isVContacts();
                    SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.V_CONTACTS, virtualContacts);
                    autoRestart = settingsDialog.isAutoRestart();
                    SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.AUTO_RESTART, autoRestart);
                    isEmulator = settingsDialog.isEmulator();
                    SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.EMULATOR, isEmulator);
                    settingsDialog.dismiss();
//                    SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.PWD_WAIT_TIME, settingsDialog.getPwdWaitTime());
//                    SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.MINE_WAIN_TIME, settingsDialog.getMimeWaitTime());
                }
            });
            settingsDialog.setNegativeButton("取消", new View.OnClickListener() {
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

    private void copyOCRToSDK() {
        File parent_path = Environment.getExternalStorageDirectory();
        File dir = new File(parent_path.getAbsoluteFile(), "tessdata");
        dir.mkdir();
        InputStream myInput;
        OutputStream myOutput = null;
        try {
            myOutput = new FileOutputStream(new File(dir, "chi_sim.traineddata"));
            myInput = this.getAssets().open("tessdata/chi_sim.traineddata");
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                        order[2] = mAccountLines[accountLaunchIndex].split("----")[0];
                        break;
                    case "<password>":
                        order[2] = mAccountLines[accountLaunchIndex].split("----")[1];
                        break;
                    case "<net>":
                        order[2] = wapnets[indexWap];
                        indexWap++;
                        if (indexWap >= wapnets.length) {
                            indexWap = 0;
                        }
                        break;
                    case "<main_net>":
                        order[2] = mainWapnets.get(new Random().nextInt(mainWapnets.size()));
                        break;
                    case "<mining>":
                        order[2] = "https://app.lives.one/?p=3&uid=0&ln=zh-cn&mxver=5.2.1.3217&mxpn=mx5";
                        break;
                }

            }
            if (isEmulator) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < order.length; i++) {
                    sb.append(order[i] + " ");
                }
                RootCmd.execRootCmdSilent(sb.toString());
            } else {
                new ProcessBuilder(order).start();
            }

        } catch (IOException e) {
            Log.i("GK", e.getMessage());
            e.printStackTrace();
        }
    }

    private void selectScript() {
        final String[] items = {"百度浏览", "脚本网站"};
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(HomeActivity.this);
        singleChoiceDialog.setTitle("浏览模式");
        singleChoiceDialog.setSingleChoiceItems(items, (int) SharedPreferencesUtils.getParam(HomeActivity.this, SharedPreferencesUtils.SCRIPT_ANI, 0),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        readMode = which;

                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeActivity.this,
                                "你选择了" + items[readMode],
                                Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.SCRIPT_ANI, readMode);
                    }
                });
        singleChoiceDialog.show();
    }

    private String getRamdomSearchText() {
        String[] text = {"Music", "Hero", "AI", "mining", "america", "jack chen", "china", "movie", "love", "japan", "english", "ebay", "free", "hotels", "cheap", "flights", "online", "school", "software", "insurance", "insurance", "deals", "google", "shoes", "baby", "vacations", "furniture", "real", "estate"};
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

            if (!Tools.javaValidateSign(this)) {
                return;
            }
            if (!data.isLoading()) {
                if (data instanceof AddAppButton) {
//                    onAddAppButtonClick();
                }
                mLaunchpadAdapter.notifyItemChanged(pos);
                mPresenter.launchApp(data);
            }
        });
    }

    private void onAddAppButtonClick() {
        if (Tools.javaValidateSign(this)) {
            ListAppActivity.gotoListApp(this);
        }
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
                int index = data.getIntExtra(AccountActivity.CONTENT_INDEX, 1);
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                accountLaunchIndex = index - 1;
                mAccountLines = content.split("\n");
                launchApp(accountLaunchIndex);
                SharedPreferencesUtils.setParam(this, SharedPreferencesUtils.LOGIN_NOW, true);
                SharedPreferencesUtils.setParam(this, SharedPreferencesUtils.AUTO_OP, false);
            }
            return;
        }
        if (requestCode == REQUEST_NET_SCRIPT) {
            if (resultCode == RESULT_OK && data != null) {
                String content = data.getStringExtra(AccountActivity.CONTENT);
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                wapnets = content.split("\n");
            }
            return;
        }
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
            if (isDestroyed() || isFinishing()) {
                return;
            }
            switch (msg.what) {
                case INSTALL_OVER:
                    if (batchInstall) {
                        int installedApp = mLaunchpadAdapter.getList().size();
                        if (installedApp < MAX_EMULATOR) {
                            sendEmptyMessage(INSTALL);
                        } else {
                            batchInstall = false;
                            deleteTempData();
                        }
                    }
                    break;
                case INSTALL:
                    mPresenter.addApp(new AppInfoLite(appBatchInfo.packageName, appBatchInfo.path, appBatchInfo.fastOpen));
                    break;
                case V_CONTACTS:
                    final ProgressDialog proDialog = android.app.ProgressDialog.show(HomeActivity.this, "添加虚拟联系人", "请等待....");
                    proDialog.setCancelable(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContactUtil.clearAll(HomeActivity.this);
                            int userId = getUserId(currentLaunchIndex);
                            String contacts = (String) SharedPreferencesUtils.getParam(HomeActivity.this, SharedPreferencesUtils.USER_CONTACTS + userId, "");
                            if (TextUtils.isEmpty(contacts)) {
                                contacts = ContactUtil.generateContacts();
                                SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.USER_CONTACTS + userId, contacts);
                            }
                            ContactUtil.insertContacts(HomeActivity.this, contacts);
                            proDialog.dismiss();

                            handler.sendEmptyMessage(LAUNCH_INIT);
                        }
                    }).start();
                    break;
                case LAUNCH_INIT:
                    launchApp(currentLaunchIndex);
                    SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.AUTO_LAUNCH_INDEX, currentLaunchIndex);
                    currentLaunchIndex++;
                    if (currentLaunchIndex >= mLaunchpadAdapter.getList().size()) {
                        currentLaunchIndex = 0;
                    }
                    currnentOp = ParamSettings.getOpScriptByReadMode(HomeActivity.this, readMode);
                    startAniScript();
                    int target = LAUNCH_INIT;
                    if (virtualContacts) {
                        target = V_CONTACTS;
                    }
                    sendEmptyMessageDelayed(target, TIME_BEGIN * 60 * 1000 + (TIME_RANDOM != 0 ? new Random().nextInt(TIME_RANDOM * 60 * 1000) : 0));
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
                            switchScript();
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
                case CHECK_VALIDATION:
                    HttpUtils.verifyKey(key, MD5Utils.encrypt(token), new HttpUtils.HttpCallBack() {

                        @Override
                        public void callback(boolean value) {
                            if (value) {
                                sendEmptyMessageDelayed(CHECK_VALIDATION, CHECK_DELAY);
                            } else {
                                log.info("后台验证失效，退出程序！");
                                VirtualCore.get().killAllApps();
                                SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.AUTO_RESTART, false);
                                finish();
                            }
                        }
                    });
                    break;
                case EXE_SEQUENCE: {
                    if (indexSequence < sequenceCommands.length) {
                        int delay = Integer.parseInt(sequenceCommands[indexSequence].substring(0, sequenceCommands[indexSequence].indexOf(",")));
                        String[] opParam = sequenceCommands[indexSequence].substring(sequenceCommands[indexSequence].indexOf(",") + 1, sequenceCommands[indexSequence].length()).split(",");
                        Message message = new Message();
                        message.what = EXE_COMMAND;
                        message.obj = opParam;
                        sendMessageDelayed(message, delay);
                    } else {
                        HermesEventBus.getDefault().post(new CallbackEvent(sequenceId));
                    }
                }
                break;
                case EXE_COMMAND: {
                    String[] param = (String[]) msg.obj;
                    exeCommand(param);
                    indexSequence++;
                    sendEmptyMessage(EXE_SEQUENCE);
                }
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

    private void deleteTempData() {
        File parent_path = Environment.getExternalStorageDirectory();
        File dir = new File(parent_path.getAbsoluteFile(), "nox/temp/user/_template");
        new File(dir, "targetData").delete();
    }

    private long getNextAccountTime() {
        int passWaitTime = (int) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.PWD_WAIT_TIME, SettingsDialog.PWD_WAIT_TIME);
        int mineWaitTime = (int) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.MINE_WAIN_TIME, SettingsDialog.MINE_WAIT_TIME);
        return 95000 + passWaitTime + mineWaitTime;
    }

    private void switchScript() {
        switch (readMode) {
            case 0:
                currnentOp = ParamSettings.getOpScript(1);
                break;
            case 1:
                currnentOp = ParamSettings.getOpScript(3);
                break;
        }

    }

    private int getUserId(int index) {
        int userId = 0;
        AppData appData = mLaunchpadAdapter.getList().get(index);
        if (appData instanceof MultiplePackageAppData) {
            MultiplePackageAppData multipleData = (MultiplePackageAppData) appData;
            userId = multipleData.userId;
        }
        return userId;
    }

    private void randowLocation() {
        int currentUserId = getUserId(currentLaunchIndex);
        List<InstalledAppInfo> infos = VirtualCore.get().getInstalledApps(0);
        for (InstalledAppInfo info : infos) {
            if (!VirtualCore.get().isPackageLaunchable(info.packageName)) {
                continue;
            }
            int[] userIds = info.getInstalledUsers();
            for (int userId : userIds) {
                if (currentUserId == userId) {
                    LocationData locationData = new LocationData(this, info, userId);
                    locationData.mode = VirtualLocationManager.get().getMode(locationData.userId, locationData.packageName);
                    locationData.location = VirtualLocationManager.get().getLocation(locationData.userId, locationData.packageName);
                    if (locationData.location != null) {
                        VirtualLocationManager.get().setMode(locationData.userId, locationData.packageName, 2);
                        locationData.location.latitude_randow = (float) (new Random().nextInt(5000) / 1000000.0);
                        locationData.location.longitude_randow = (float) (new Random().nextInt(5000) / 1000000.0);
                        VirtualLocationManager.get().setLocation(locationData.userId, locationData.packageName, locationData.location);
                    }
                }
            }
        }
    }

    private void launchApp(int currentLaunchIndex) {
        if (!Tools.javaValidateSign(this)) {
            return;
        }
        mLaunchpadAdapter.notifyItemChanged(currentLaunchIndex);
        mPresenter.launchApp(mLaunchpadAdapter.getList().get(currentLaunchIndex));
        AppData appData = mLaunchpadAdapter.getList().get(currentLaunchIndex);
//        int lastIndex = (currentLaunchIndex - 1 + mLaunchpadAdapter.getList().size()) % mLaunchpadAdapter.getList().size();
        if (appData instanceof PackageAppData) {
            Toast.makeText(HomeActivity.this, "当前启动 1 号程序", Toast.LENGTH_SHORT).show();
            if (!isEmulator) {
                updatePopupWindow(1);
            }
            log.info("当前启动 1 号程序");
        } else {
            MultiplePackageAppData multipleData = (MultiplePackageAppData) appData;
            Toast.makeText(HomeActivity.this, "当前启动 " + (multipleData.userId + 1) + " 号程序", Toast.LENGTH_SHORT).show();
            updatePopupWindow((multipleData.userId + 1));
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
            //杀死之前的应用
            int lastIndex = (currentLaunchIndex - 1 + mLaunchpadAdapter.getList().size()) % mLaunchpadAdapter.getList().size();
            appData = mLaunchpadAdapter.getList().get(lastIndex);
            if (appData instanceof PackageAppData) {
                VirtualCore.get().killApp(HOOK_APK, 0);
                log.info("后台杀死 1 号程序");
            } else {
                MultiplePackageAppData multipleData = (MultiplePackageAppData) appData;
                VirtualCore.get().killApp(HOOK_APK, multipleData.userId);
                log.info("后台杀死 " + (multipleData.userId + 1) + " 号程序");
            }
        }
    }

    private void updatePopupWindow(int i) {
        popText.setText("程序: " + i);
        windowManager.updateViewLayout(popText, params);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        sequenceId = event.getCurrent();
        Message message = new Message();
        message.what = EXE_SEQUENCE;
        switch (event.getCurrent()) {
            case MessageEvent.CLICK_MINING:
                if (isEmulator) {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.5,0.392"
                    };
                } else {
                    sequenceCommands = new String[]{
//                            "0,input,tap,0.5,0.485"
                            "0,input,tap,0.5,0.3055",
                            "1000,input,tap,0.5,0.0972",
                            "1000,input,text,<mining>",
                            "3000,input,tap,0.913,0.0835"
                    };
                }
                break;
            case MessageEvent.CLICK_HOME:
                if (isEmulator) {
                    sequenceCommands = new String[]{
//                            "1000,input,tap,0.904,0.971",
//                            "2000,input,tap,0.176,0.971"
                    };
                } else {
                    sequenceCommands = new String[]{
//                            "1000,input,tap,0.896,0.965",
//                            "2000,input,tap,0.176,0.965"
//                            "0,input,tap,0.5,0.3055",
//                            "1000,input,tap,0.5,0.0972",
//                            "1000,input,text,<mining>",
//                            "3000,input,tap,0.913,0.0835"
                    };
                }
                break;
            case MessageEvent.HOME_RETURN:
                if (isEmulator) {
                    sequenceCommands = new String[]{
                            "0,input,keyevent,4"
                    };
                } else {
                    sequenceCommands = new String[]{
                            "0,input,keyevent,4"
                    };
                }
                break;
            case MessageEvent.CLICK_INSTALL_PLUGIN:
                if (isEmulator) {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.5,0.84"
                    };
                } else {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.5,0.893"
                    };
                }
                break;
            case MessageEvent.CLICK_LOGIN:
                if (isEmulator) {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.5,0.427"
                    };
                } else {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.5,0.537"
                    };
                }
                break;
            case MessageEvent.SWITCH_EMAIL:
                if (isEmulator) {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.820,0.433",
                            "1000,input,tap,0.5,0.39",
                            "1000,input,text,<account>",
                            "1000,input,tap,0.6,0.672"
                    };
                } else {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.820,0.461",
                            "1000,input,text,<account>",
                            "500,input,tap,0.188,0.188",
                            "500,input,tap,0.188,0.188",
                            "1000,input,tap,0.6,0.704"
                    };
                }
                break;
            case MessageEvent.INPUT_EMAIL:
                if (isEmulator) {
                    sequenceCommands = new String[]{

                    };
                } else {
                    sequenceCommands = new String[]{
//                            "1000,input,tap,0.5,0.42",

                    };
                }
                break;
            case MessageEvent.CLICK_LOGIN_ACCOUNT:
                if (isEmulator) {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.6,0.672"
                    };
                } else {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.188,0.188",
                            "0,input,tap,0.188,0.188",
                            "1000,input,tap,0.6,0.704"
                    };
                }
                break;
            case MessageEvent.CLICK_PWD_ACCOUNT:
                if (isEmulator) {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.6,0.672"
                    };
                } else {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.188,0.188",
                            "0,input,tap,0.188,0.188",
                            "1000,input,tap,0.6,0.704"
                    };
                }
                break;
            case MessageEvent.INPUT_PWD:
                if (isEmulator) {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.5,0.39",
                            "0,input,text,<password>",
                            "1000,input,tap,0.6,0.704"
                    };
                } else {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.5,0.42",
                            "1000,input,text,<password>",
                            "500,input,tap,0.188,0.188",
                            "500,input,tap,0.188,0.188",
                            "1000,input,tap,0.6,0.704"
                    };
                }
                break;
            case MessageEvent.CLICK_CANCEL:
                if (isEmulator) {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.291,0.577",
                            "2000,input,tap,0.291,0.577",
                            "2000,input,tap,0.291,0.577"
                    };
                } else {
                    sequenceCommands = new String[]{
                            "0,input,tap,0.291,0.596",
                            "1500,input,tap,0.291,0.577",
                            "1500,input,tap,0.291,0.577"
                    };
                }
                break;
            case MessageEvent.RETURN_ONCE:
                sequenceCommands = new String[]{
                        "0,input,keyevent,4"
                };
                break;
            case MessageEvent.RETURN_TWICE:
                sequenceCommands = new String[]{
                        "0,input,keyevent,4",
                        "2000,input,keyevent,4"
                };
                break;
            case MessageEvent.NEXT_ACCOUNT:
                accountLaunchIndex++;
                if (accountLaunchIndex < mLaunchpadAdapter.getList().size() && accountLaunchIndex < mAccountLines.length) {
                    launchApp(accountLaunchIndex);
                }
                return;
            case MessageEvent.HOME_RETURN_BY_AUTO:
                sequenceCommands = new String[]{
                        "0,input,keyevent,4",
                        "1000,input,tap,0.5,0.965",
                };
                resetAutoLauncher();
                break;
            case MessageEvent.SCROLLDOWN_TO_AUTO:
                sequenceCommands = new String[]{
                        "0,input,swipe,0.5,0.3,0.5,0.6",
                        "1000,input,tap,0.5,0.965",
                };
                resetAutoLauncher();
                break;
            case MessageEvent.CLICK_CLEAR:
                sequenceCommands = new String[]{
                        "0,input,swipe,0.5,0.3,0.5,0.6",
                        "1500,input,tap,0.904,0.971",
                        "2000,input,tap,0.176,0.971",
                        "1000,input,tap,0.5,0.256",
                        "1000,input,tap,0.5,0.0859",
                        "1000,input,text,<mining>",
                        "3000,input,tap,0.913,0.0835"
                };
                break;
        }
        indexSequence = 0;
        handler.sendMessage(message);
    }

    private void resetAutoLauncher() {
        currentOpIndex = 0;
        int target = LAUNCH_INIT;
        if (virtualContacts) {
            target = V_CONTACTS;
        }
        handler.removeMessages(target);
        handler.removeMessages(LAUNCH_INIT);
        handler.removeMessages(AUTO_OP);
        handler.removeMessages(EXE);
        handler.sendEmptyMessage(LAUNCH_INIT);
    }

}
