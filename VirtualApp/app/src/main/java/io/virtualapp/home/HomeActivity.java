package io.virtualapp.home;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lody.virtual.GmsSupport;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.ParamSettings;
import com.lody.virtual.helper.SharedPreferencesUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
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
import io.virtualapp.home.models.MultiplePackageAppData;
import io.virtualapp.home.models.PackageAppData;
import io.virtualapp.home.repo.AppRepository;
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
    private static final int LAUNCH = 0x03;
    private static final int AUTO_OP = 0x04;
    private static final int EXE = 0x05;
    private static final int ACCOUNT_OP = 0x06;
    private static final int ACCOUNT_AUTO_OP = 0x07;
    private static final int ACCOUNT_EXE = 0x08;
    private static final int CHECK_VALIDATION = 0x09;
    private static final String KEY = "KEY";
    private static final long CHECK_DELAY = 60000 * 10;
//    private static final String HOOK_APK = "com.mx.browser";

    private static final int REQUEST_BATCH_LOGIN = 1000;
    private static final int REQUEST_BIND_ID = 1001;
    private static final String HOOK_APK = "com.example.kevin.deviceinfo";


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
    private String deviceInfo;

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
        deviceInfo = (String)SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.DEVICE, "");
        currentLaunchIndex = (int) SharedPreferencesUtils.getParam(this, SharedPreferencesUtils.AUTO_LAUNCH_INDEX, 0);
        setContentView(R.layout.activity_home);
        mUiHandler = new Handler(Looper.getMainLooper());
        bindViews();
        initLaunchpad();
        initMenu();
        mRepository = new AppRepository(this);
        handler = new Myhandler();
        handler.sendEmptyMessageDelayed(CHECK_VALIDATION, CHECK_DELAY);
        new HomePresenterImpl(this).start();
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
        menu.add("导入设备号").setIcon(R.drawable.ic_account).setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1001);
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
            if(TextUtils.isEmpty(deviceInfo)){
                Toast.makeText(this, "请先导入设备号", Toast.LENGTH_SHORT).show();
                return false;
            }
            startActivityForResult(new Intent(HomeActivity.this, AccountActivity.class), REQUEST_BATCH_LOGIN);
            return false;
        });
        menu.add("批量模拟操作").setIcon(R.drawable.ic_notification).setOnMenuItemClickListener(item -> {
//            Toast.makeText(this, "The coming", Toast.LENGTH_SHORT).show();
            if(TextUtils.isEmpty(deviceInfo)){
                Toast.makeText(this, "请先导入设备号", Toast.LENGTH_SHORT).show();
                return false;
            }
            handler.sendEmptyMessage(LAUNCH);
            return false;
        });
//        menu.add("虚拟定位").setIcon(R.drawable.ic_notification).setOnMenuItemClickListener(item -> {
//            startActivity(new Intent(this, VirtualLocationSettings.class));
//            return true;
//        });
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
                }

            }
            new ProcessBuilder(order).start();
        } catch (IOException e) {
            Log.i("GK", e.getMessage());
            e.printStackTrace();
        }
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
            if(TextUtils.isEmpty(deviceInfo)){
                Toast.makeText(this, "请先导入设备号", Toast.LENGTH_SHORT).show();
                return;
            }
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
        if (requestCode == REQUEST_BIND_ID) {
            Uri uri = data.getData();
            SharedPreferencesUtils.setParam(this, SharedPreferencesUtils.DEVICE,readDeviceTxt(uri));
            Toast.makeText(HomeActivity.this, "綁定设备号成功！", Toast.LENGTH_LONG).show();
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
            InputStream inputStream  = getContentResolver().openInputStream(uri);
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
                case LAUNCH:
                    launchApp(currentLaunchIndex);

                    SharedPreferencesUtils.setParam(HomeActivity.this, SharedPreferencesUtils.AUTO_LAUNCH_INDEX, currentLaunchIndex);
                    currentLaunchIndex++;
                    if (currentLaunchIndex >= mLaunchpadAdapter.getList().size()) {
                        currentLaunchIndex = 0;
                    }
                    currnentOp = getOpByTimeType();//ParamSettings.batchOps[2];
                    Message message = new Message();
                    message.what = AUTO_OP;
                    message.arg1 = currentLaunchIndex;
                    sendMessage(message);
                    currentOpIndex = 0;
                    sendEmptyMessageDelayed(LAUNCH, TIME_BEGIN * 60 * 1000 + (TIME_RANDOM != 0 ? new Random().nextInt(TIME_RANDOM * 60 * 1000) : 0));
                    break;
                case AUTO_OP:
                    if (currentOpIndex < currnentOp.length && msg.arg1 == currentLaunchIndex) {
                        String currentCommand = currnentOp[currentOpIndex];
                        int delay = Integer.parseInt(currentCommand.substring(0, currentCommand.indexOf(",")));
                        String[] opParam = currentCommand.substring(currentCommand.indexOf(",") + 1, currentCommand.length()).split(",");
                        message = new Message();
                        message.what = EXE;
                        message.arg1 = msg.arg1;
                        message.obj = opParam;
                        sendMessageDelayed(message, delay);
                    } else {
                        currentOpIndex = 0;
                    }
                    break;
                case EXE:
                    if (currentLaunchIndex == msg.arg1) {
                        String[] param = (String[]) msg.obj;
                        exeCommand(param);
                        currentOpIndex++;

                        message = new Message();
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
                        message = new Message();
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
                    if (accountLaunchIndex <= mLaunchpadAdapter.getList().size() && accountLaunchIndex <= mAccountLines.length) {
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
                                VirtualCore.get().killAllApps();
                                finish();
                            }
                        }
                    });
                    break;
            }
        }
    }

    private void launchApp(int currentLaunchIndex) {
        mLaunchpadAdapter.notifyItemChanged(currentLaunchIndex);
        mPresenter.launchApp(mLaunchpadAdapter.getList().get(currentLaunchIndex));
        AppData appData = mLaunchpadAdapter.getList().get(currentLaunchIndex);
        if (appData instanceof PackageAppData) {
            Toast.makeText(HomeActivity.this, "当前启动 " + 1 + " 号程序", Toast.LENGTH_SHORT).show();
//            startPopupWindow(1);
        } else {
            MultiplePackageAppData multipleData = (MultiplePackageAppData) appData;
            Toast.makeText(HomeActivity.this, "当前启动 " + (multipleData.userId + 1) + " 号程序", Toast.LENGTH_SHORT).show();
        }

        //杀死上面第5个应用
        int last5Index = (currentLaunchIndex - 5 + mLaunchpadAdapter.getList().size()) % mLaunchpadAdapter.getList().size();
        appData = mLaunchpadAdapter.getList().get(last5Index);
        if (appData instanceof PackageAppData) {
            VirtualCore.get().killApp(HOOK_APK,0);
        } else {
            MultiplePackageAppData multipleData = (MultiplePackageAppData)appData;
            VirtualCore.get().killApp(HOOK_APK,multipleData.userId);
        }
    }

//    public void startPopupWindow(int number) {
//        Intent intent = new Intent(this, PopupWindowService.class);
//        intent.putExtra(PopupWindowService.ALERT_TEXT, "程序号：" + number);
//        //启动FxService
//        startService(intent);
//    }
//
//    public void stopPopupWindow(){
//        Intent intent = new Intent(this, PopupWindowService.class);
//        //终止FxService
//        stopService(intent);
//    }

    private String[] getOpByAccountOp(String type) {
        switch (type) {
            case "login":
                currnentOp = ParamSettings.batchOps[5];
                return currnentOp;
            case "signup":
                break;
            case "signupB":
                break;
        }
        return null;
    }

    private String[] getOpByTimeType() {
//        switch (TIME_BEGIN) {
//            case 5:
////                return ParamSettings.batchOps[new Random().nextInt(3)];
//                return ParamSettings.batchOps[2];
//            case 10:
//                return ParamSettings.batchOps[3];
//            case 15:
//                return ParamSettings.batchOps[4];
//            case 20:
//                return ParamSettings.batchOps[7];
//            case 30:
        return ParamSettings.batchOps[6];
//            default:
//                return ParamSettings.batchOps[2];
//    }
    }
}
