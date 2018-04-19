package com.lody.virtual.client.hook.delegate;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.lody.virtual.client.VClientImpl;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.fixer.ActivityFixer;
import com.lody.virtual.client.fixer.ContextFixer;
import com.lody.virtual.client.interfaces.IInjector;
import com.lody.virtual.client.ipc.ActivityClientRecord;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.SharedPreferencesUtils;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.helper.utils.ConfigureLog4J;
import com.lody.virtual.helper.utils.CrashHandler;
import com.lody.virtual.helper.utils.MessageEvent;
import com.lody.virtual.helper.utils.SimilarPicture;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.interfaces.IUiCallback;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import mirror.android.app.ActivityThread;
import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * @author Lody
 */
public final class AppInstrumentation extends InstrumentationDelegate implements IInjector {

    private static final String TAG = AppInstrumentation.class.getSimpleName();

    private static AppInstrumentation gDefault;
    private TextView popText;
    private LoginHandler handler;
    private View currentView;

    private AppInstrumentation(Instrumentation base) {
        super(base);
    }

    public static AppInstrumentation getDefault() {
        if (gDefault == null) {
            synchronized (AppInstrumentation.class) {
                if (gDefault == null) {
                    gDefault = create();
                }
            }
        }
        return gDefault;
    }

    private static AppInstrumentation create() {
        Instrumentation instrumentation = ActivityThread.mInstrumentation.get(VirtualCore.mainThread());
        if (instrumentation instanceof AppInstrumentation) {
            return (AppInstrumentation) instrumentation;
        }
        return new AppInstrumentation(instrumentation);
    }


    @Override
    public void inject() throws Throwable {
        base = ActivityThread.mInstrumentation.get(VirtualCore.mainThread());
        ActivityThread.mInstrumentation.set(VirtualCore.mainThread(), this);
    }

    @Override
    public boolean isEnvBad() {
        return !(ActivityThread.mInstrumentation.get(VirtualCore.mainThread()) instanceof AppInstrumentation);
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        if (icicle != null) {
            BundleCompat.clearParcelledData(icicle);
        }
        VirtualCore.get().getComponentDelegate().beforeActivityCreate(activity);
        IBinder token = mirror.android.app.Activity.mToken.get(activity);
        ActivityClientRecord r = VActivityManager.get().getActivityRecord(token);
        if (r != null) {
            r.activity = activity;
        }
        ContextFixer.fixContext(activity);
        ActivityFixer.fixActivity(activity);
        ActivityInfo info = null;
        if (r != null) {
            info = r.info;
        }
        if (info != null) {
            if (info.theme != 0) {
                activity.setTheme(info.theme);
            }
            if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    && info.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
                activity.setRequestedOrientation(info.screenOrientation);
            }
        }
        super.callActivityOnCreate(activity, icicle);
        VirtualCore.get().getComponentDelegate().afterActivityCreate(activity);
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        if (icicle != null) {
            BundleCompat.clearParcelledData(icicle);
        }
        super.callActivityOnCreate(activity, icicle, persistentState);
    }

    @Override
    public void callActivityOnResume(Activity activity) {
        VirtualCore.get().getComponentDelegate().beforeActivityResume(activity);
        VActivityManager.get().onActivityResumed(activity);
        super.callActivityOnResume(activity);
        VirtualCore.get().getComponentDelegate().afterActivityResume(activity);
        Intent intent = activity.getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("_VA_|_sender_");
            if (bundle != null) {
                IBinder callbackToken = BundleCompat.getBinder(bundle, "_VA_|_ui_callback_");
                IUiCallback callback = IUiCallback.Stub.asInterface(callbackToken);
                if (callback != null) {
                    try {
                        callback.onAppOpened(VClientImpl.get().getCurrentPackage(), VUserHandle.myUserId());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        popText = getTextView(activity, params);
        windowManager.addView(popText, params);
        windowManager.updateViewLayout(popText, params);

        View rootView = activity.getWindow().getDecorView();
        traversalView(activity, rootView);

        boolean loginNow = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.LOGIN_NOW, false);
        if (loginNow) {
            handler.sendEmptyMessageDelayed(LoginHandler.HOME_PAGE, 10000);
        }
    }

    public void traversalView(final Activity activity, final View view) {
        if (null == view) {
            return;
        }
        currentView = view;
    }

    private void handleLogin(final Activity activity, final View view) {
        new Thread(new Runnable() {
            long time = System.currentTimeMillis();
            int step = 0;//0-确保首页，1-点击挖矿
            long timestamp = 10000;//初始加载页面

            @Override
            public void run() {
                while (true) {
                    if (System.currentTimeMillis() - time > timestamp && !activity.isFinishing()) {
                        time = System.currentTimeMillis();
                        switch (step) {
                            case -1:
                                break;
                            case 0://确认首页
                                if (isCurrentPage(view, "white.png", 107, 272)) {
                                    Log.e("LLLL", "step" + step + "white");
                                } else if (isCurrentPage(view, "mining.png", 216, 286)) {
                                    HermesEventBus.getDefault().post(MessageEvent.CLICK_MINING);
                                    step = 1;
                                    timestamp = 10000;
                                    Log.e("LLLL", "step" + step + "CLICK_MINING");
                                } else {
                                    Log.e("LLLL", "step" + step + "else");
//                                SharedPreferencesUtils.setParam(VirtualCore.get().getContext(), SharedPreferencesUtils.LOGIN_NOW, false);
                                }
                                break;
                            case 1://判断当前页面
                                //"一键安装,181,652,126,33",
                                if (isCurrentPage(view, "white.png", 107, 272)) {
                                    Log.e("LLLL", "step" + step + "white");
                                } else if (isCurrentPage(view, "install_mining.png", 177, 84)) {
                                    HermesEventBus.getDefault().post(MessageEvent.CLICK_INSTALL_PLUGIN);
                                    timestamp = 10000;
                                    step = 2;
                                    Log.e("LLLL", "step" + step + "install_mining");
                                } else if (needToLogin(view)) {

                                } else if (needToBindLvtAccount(view)) {

                                } else if (isLvtInstalled(view)) {

                                } else {
                                    //其他错误退出，有可能账号被禁止等原因,记下log
                                }
                                break;
                            case 2://安装插件
                                if (isCurrentPage(view, "white.png", 107, 272)) {

                                } else if (isCurrentPage(view, "login.png", 172, 319)) {
                                    timestamp = 4000;
                                    step = 2;
                                } else if (isLvtInstalling(view)) {

                                } else if (needToBindLvtAccount(view)) {

                                } else if (isLvtInstalled(view)) {

                                } else {
                                    //其他错误退出，有可能账号被禁止等原因,记下log
                                }
                                break;
                        }
                    }
                }
            }
        }).start();

    }


    private boolean needToLogin(View view) {
        return false;
    }

    private boolean isLvtInstalling(View view) {
        return false;
    }

    private boolean isLvtInstalled(View view) {
        return false;
    }

    private boolean needToBindLvtAccount(View view) {
        return false;
    }

    private Bitmap getImageFromScreenShot(View view, int x, int y, Bitmap bitmapt) {
        Bitmap newbmp;
        try {
            view.setDrawingCacheEnabled(true);
            newbmp = Bitmap.createBitmap(view.getDrawingCache(), (int) x, (int) y, bitmapt.getWidth(), bitmapt.getHeight());// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newbmp;
    }

    private Bitmap getImageFromScreenShot(View view, int x, int y, int width, int height) {
        Bitmap newbmp;
        try {
            view.setDrawingCacheEnabled(true);
            newbmp = Bitmap.createBitmap(view.getDrawingCache(), (int) x, (int) y, width, height);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newbmp;
    }

    private boolean isCurrentPage(View view, String path, int x, int y) {
        //165,355
        int width = view.getWidth();
        Bitmap bitmap1 = null;
        Bitmap bitmap2 = null;
        switch (width) {
            case 480:
                bitmap1 = getImageFromAssetsFile(VirtualCore.get().getContext(), "480_800/" + path);
                bitmap2 = getImageFromScreenShot(view, x, y, bitmap1);
                if (bitmap2 == null) {
                    return false;
                }
                break;
        }
        return SimilarPicture.compare(bitmap1, bitmap2);
    }

    private TextView getTextView(Activity activity, WindowManager.LayoutParams params) {
        TextView popText = new TextView(activity);
        popText.setBackgroundColor(Color.parseColor("#000000"));
        popText.setText("程序:" + (VUserHandle.myUserId() + 1));
        popText.setTextSize(12);
        popText.setTextColor(Color.parseColor("#FFFFFF"));
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 设置window type
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.alpha = 1f;  //0为全透明，1为不透明
        boolean emulator = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.EMULATOR, false);
        if (emulator) {
            params.width = 75;
            params.height = 20;
        } else {
            params.width = 150;
            params.height = 45;
        }
        return popText;
    }


    @Override
    public void callActivityOnDestroy(Activity activity) {
        VirtualCore.get().getComponentDelegate().beforeActivityDestroy(activity);
        super.callActivityOnDestroy(activity);
        VirtualCore.get().getComponentDelegate().afterActivityDestroy(activity);
    }

    @Override
    public void callActivityOnPause(Activity activity) {
        VirtualCore.get().getComponentDelegate().beforeActivityPause(activity);
        super.callActivityOnPause(activity);
        VirtualCore.get().getComponentDelegate().afterActivityPause(activity);
    }


    @Override
    public void callApplicationOnCreate(Application app) {
        super.callApplicationOnCreate(app);
        ConfigureLog4J configureLog4J = new ConfigureLog4J();
        configureLog4J.configure();
        HermesEventBus.getDefault().connectApp(app, app.getPackageName());
        Logger log = Logger.getLogger("VirtualLives");
        CrashHandler.getInstance().init(app, log);
        handler = new LoginHandler();
    }

    private Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    /**
     * 进行图片识别
     *
     * @param bitmap   待识别图片
     * @param language 识别语言
     * @return 识别结果字符串
     */
//    public String doOcr(Bitmap bitmap, String language) {
//        TessBaseAPI baseApi = new TessBaseAPI();
//
//        baseApi.init(getSDPath(), language);
//
//        // 必须加此行，tess-two要求BMP必须为此配置
//        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//
//        baseApi.setImage(bitmap);
//
//        String text = baseApi.getUTF8Text();
//
//        baseApi.clear();
//        baseApi.end();
//
//        return text;
//    }
//
//    public static String getSDPath() {
//        File sdDir = null;
//        boolean sdCardExist = Environment.getExternalStorageState().equals(
//                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
//        if (sdCardExist) {
//            sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
//        }
//        return sdDir.toString();
//    }
    public String doOcr(Bitmap bitmap, String language) {
        TessBaseAPI baseApi = new TessBaseAPI();

        File sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
        baseApi.init(sdDir.toString(), language);
        // 必须加此行，tess-two要求BMP必须为此配置
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        baseApi.setImage(bitmap);
        String text = baseApi.getUTF8Text();
        baseApi.clear();
        baseApi.end();
        return text;
    }

    class LoginHandler extends Handler {

        public static final int HOME_PAGE = 0x01;
        public static final int HOME_MINING_PAGE = 0x02;
        private static final int HOME_MINING_LOGIN_WARNING_PAGE = 0x03;
        private static final int HOME_LOGIN_ACCOUNT = 0x04;
        private static final int HOME_LOGIN_PWD = 0x05;

        @Override
        public void handleMessage(Message msg) {
            currentView.invalidate();
//            currentView.postInvalidate();
//            currentView.requestLayout();
            switch (msg.what) {
                case HOME_PAGE:
//                    Bitmap bitmap = getImageFromScreenShot(currentView, 62, 152, 127, 29);
                    if (doOcr(getImageFromScreenShot(currentView, 62, 152, 127, 29), "chi_sim").contains("网址")) {
                        HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.CLICK_MINING));
                        sendEmptyMessageDelayed(HOME_PAGE, 5000);
                        Log.e("LLLL", "搜索或输入网址");
//                    } else if (isCurrentPage(currentView, "install_mining.png", 177, 656)) {
                    } else if (doOcr(getImageFromScreenShot(currentView, 177, 656, 134, 33), "chi_sim").contains("安装")) {
                        sendEmptyMessage(HOME_MINING_PAGE);
                        Log.e("LLLL", "一键安装");
//                    } else if (isCurrentPage(currentView, "home_return.png", 11, 31)) {
                    } else if (doOcr(getImageFromScreenShot(currentView, 18, 37, 37, 19), "chi_sim").contains("返回")) {
                        HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.HOME_RETURN));
                        sendEmptyMessageDelayed(HOME_PAGE, 5000);
                        Log.e("LLLL", "返回");
                    } else {
                        HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.CLICK_HOME));
                        sendEmptyMessageDelayed(HOME_PAGE, 5000);
                        Log.e("LLLL", "HOME_PAGE——按照HOME处理了");
                    }
                    break;
                case HOME_MINING_PAGE:
                    if (doOcr(getImageFromScreenShot(currentView, 177, 656, 134, 33), "chi_sim").contains("安装")) {
                        HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.CLICK_INSTALL_PLUGIN));
                        sendEmptyMessageDelayed(HOME_MINING_PAGE, 10000);
                        Log.e("LLLL", "一键安装");
//                    } else if (isCurrentPage(currentView, "installing_mining.png", 188, 650)) {
                    } else if (doOcr(getImageFromScreenShot(currentView, 188, 650, 106, 33), "chi_sim").contains("安装中")) {
                        sendEmptyMessageDelayed(HOME_MINING_PAGE, 5000);
                        Log.e("LLLL", "安装中");
//                    } else if (isCurrentPage(currentView, "login.png", 172, 319)) {
                    } else if (doOcr(getImageFromScreenShot(currentView, 215, 338, 47, 28), "chi_sim").contains("宣宗")) {
                        sendEmptyMessage(HOME_MINING_LOGIN_WARNING_PAGE);
                        Log.e("LLLL", "登录");
                    } else {
                        sendEmptyMessageDelayed(HOME_MINING_PAGE, 10000);
                        Log.e("LLLL", "HOME_MINING_PAGE——其它");
                    }
                    break;
                case HOME_MINING_LOGIN_WARNING_PAGE:
                    if (doOcr(getImageFromScreenShot(currentView, 215, 338, 47, 28), "chi_sim").contains("宣宗")) {
                        HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.CLICK_LOGIN));
                        Log.e("LLLL", "登录");
                        sendEmptyMessageDelayed(HOME_MINING_LOGIN_WARNING_PAGE, 5000);
//                    } else if (isCurrentPage(currentView, "login_account.png", 367, 333)) {
                    } else if (doOcr(getImageFromScreenShot(currentView, 367, 333, 100, 30), "chi_sim").contains("今 切橡黜瓤氟")) {
                        Log.e("LLLL", "切换到邮箱");
                        sendEmptyMessage(HOME_LOGIN_ACCOUNT);
                    } else {
                        sendEmptyMessageDelayed(HOME_MINING_LOGIN_WARNING_PAGE, 5000);
                        Log.e("LLLL", "HOME_MINING_LOGIN_WARNING_PAGE——othoer");
                    }
                    break;
                case HOME_LOGIN_ACCOUNT:
                    if (doOcr(getImageFromScreenShot(currentView, 367, 333, 100, 30), "chi_sim").contains("切换到邮箱")) {
                        HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.SWITCH_EMAIL));
                        sendEmptyMessageDelayed(HOME_LOGIN_ACCOUNT, 5000);
                        Log.e("LLLL", "切换到邮箱");
                    } else if (doOcr(getImageFromScreenShot(currentView, 186, 293, 110, 28), "chi_sim").contains("请输入邮箱地址")) {
                        HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.INPUT_EMAIL));
                        sendEmptyMessageDelayed(HOME_LOGIN_ACCOUNT, 5000);
                        Log.e("LLLL", "请输入邮箱地址");
//                    } else if (isCurrentPage(currentView, "login_input_email_network_error.png", 208, 364)) {
                    } else if (doOcr(getImageFromScreenShot(currentView, 208, 364, 65, 23), "chi_sim").contains("网络错误")) {
                        HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.CLICK_LOGIN_ACCOUNT));
                        sendEmptyMessageDelayed(HOME_LOGIN_ACCOUNT, 5000);
                        Log.e("LLLL", "网络错误");
//                    } else if (!isCurrentPage(currentView, "login_input_email_format_error.png", 189, 363)) {
                    } else if (doOcr(getImageFromScreenShot(currentView, 189, 363, 108, 22), "chi_sim").contains("邮箱格式不正确")) {
                        HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.HOME_RETURN));
                        sendEmptyMessageDelayed(HOME_LOGIN_ACCOUNT, 5000);
                        Log.e("LLLL", "邮箱格式不正确");
//                    } else if (!isCurrentPage(currentView, "login_email_button.png", 220, 528,35,23)) {
                    } else if (doOcr(getImageFromScreenShot(currentView, 220, 528,35,23), "chi_sim").contains("登录")) {
                        HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.CLICK_LOGIN_ACCOUNT));
                        sendEmptyMessageDelayed(HOME_LOGIN_ACCOUNT, 5000);
                        Log.e("LLLL", "登录");
                    } else if (doOcr(getImageFromScreenShot(currentView, 215, 338, 47, 28), "chi_sim").contains("宣宗")) {
                        sendEmptyMessageDelayed(HOME_LOGIN_ACCOUNT, 5000);
                        Log.e("LLLL", "警告登录");
                    }else if (doOcr(getImageFromScreenShot(currentView, 398, 342, 65, 20), "chi_sim").contains("忘记密码")) {
                        sendEmptyMessage(HOME_LOGIN_PWD);
                        Log.e("LLLL", "忘记密码");
                    } else {
                        sendEmptyMessageDelayed(HOME_LOGIN_ACCOUNT, 5000);
                    }
                    break;
                case HOME_LOGIN_PWD:
                    break;
            }
        }
    }
}
