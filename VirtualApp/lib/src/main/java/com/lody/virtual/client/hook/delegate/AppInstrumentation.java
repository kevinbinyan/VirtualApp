package com.lody.virtual.client.hook.delegate;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

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
import com.lody.virtual.helper.utils.Base64;
import com.lody.virtual.helper.utils.CallbackEvent;
import com.lody.virtual.helper.utils.ConfigureLog4J;
import com.lody.virtual.helper.utils.CrashHandler;
import com.lody.virtual.helper.utils.MessageEvent;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.interfaces.IUiCallback;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import mirror.android.app.ActivityThread;
import xiaofei.library.hermeseventbus.HermesEventBus;

//import android.os.PersistableBundle;

/**
 * @author Lody
 */
public final class AppInstrumentation extends InstrumentationDelegate implements IInjector {

    private static final String TAG = AppInstrumentation.class.getSimpleName();

    private static AppInstrumentation gDefault;
    //    private TextView popText;
    private LoginHandler handler;
    private View currentView;
    private Logger log;
    private boolean isEmulator;
    private long delay = 1000;
    //    private boolean loginNow;
    private boolean boundNow;
    private String capture;
    private String imgId;
    private int boundMode;
//    private boolean currentStatus;

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
        saveSign(activity);
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
        saveSign(activity);
        super.callActivityOnCreate(activity, icicle);
        VirtualCore.get().getComponentDelegate().afterActivityCreate(activity);
        saveSign(activity);
    }

//    @Override
//    public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
//        if (icicle != null) {
//            BundleCompat.clearParcelledData(icicle);
//        }
//        super.callActivityOnCreate(activity, icicle, persistentState);
//    }

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

        View rootView = activity.getWindow().getDecorView();
        traversalView(activity, rootView);
        if (boundNow) {
            HermesEventBus.getDefault().register(this);
        }
    }

    public void traversalView(final Activity activity, final View view) {
        if (null == view) {
            return;
        }
        currentView = view;
    }

//    private Bitmap getImageFromScreenShot(View view, int x, int y, Bitmap bitmapt) {
//        Bitmap newbmp;
//        try {
//            view.setDrawingCacheEnabled(true);
//            newbmp = Bitmap.createBitmap(view.getDrawingCache(), (int) x, (int) y, bitmapt.getWidth(), bitmapt.getHeight());// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return newbmp;
//    }

    private Bitmap getImageFromScreenShot(View view, float x, float y, float width, float height) {
        Bitmap newbmp;
        try {
            view.setDrawingCacheEnabled(true);
            newbmp = Bitmap.createBitmap(view.getDrawingCache(), (int) x, (int) y, (int) width, (int) height);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newbmp;
    }

    private Bitmap getImageFromScreenShotHalf(View view, float x, float y, float width, float height) {
        Bitmap newbmp;
        try {
            view.setDrawingCacheEnabled(true);
            Matrix matrix = new Matrix();
            matrix.postScale(0.5f, 0.5f); //长和宽放大缩小的比例
            newbmp = Bitmap.createBitmap(view.getDrawingCache(), (int) x, (int) y, (int) width, (int) height, matrix, true);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newbmp;
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
        if (boundNow) {
            HermesEventBus.getDefault().unregister(this);
        }
    }


    @Override
    public void callApplicationOnCreate(Application app) {
        super.callApplicationOnCreate(app);
        ConfigureLog4J configureLog4J = new ConfigureLog4J();
        configureLog4J.configure("aoyou.log");
        saveSign(app);
        log = Logger.getLogger("VirtualLives");
        CrashHandler.getInstance().init(app, log);

        isEmulator = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.EMULATOR, false);
//        loginNow = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.LOGIN_NOW, false);
        boundNow = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.BOUND_NOW, false);
        boundMode = (int) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.BOUND_MODE, 1);
        if (boundNow) {
            handler = new LoginHandler();
            handler.sendEmptyMessageDelayed(HOME_INIT, 10000);
            HermesEventBus.getDefault().connectApp(app, app.getPackageName());
        }

    }

    private void saveSign(Context app) {
        SharedPreferences sp = app.getSharedPreferences("sce", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("inside", true);
        editor.commit();
    }

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

    private boolean compareKeyword(Bitmap bitmap, String[] keyword) {
        String str = doOcr(bitmap, "chi_sim");
        for (String key : keyword) {
            if (str.contains(key)) {
                return true;
            }
        }
        return false;
    }

    private boolean equalKeyword(Bitmap bitmap, String keyword) {
        return doOcr(bitmap, "chi_sim").equals(keyword);
    }

    private void sendMessageAfterClear(int what) {
        handler.removeMessages(what);
        handler.sendEmptyMessageDelayed(what, delay);
    }

    private void sendMessageAfterClearDelay(int what) {
        handler.removeMessages(what);
        handler.sendEmptyMessageDelayed(what, 10 * delay);
    }

    public static final int HOME_INIT = 0x00;
    public static final int HOME_PAGE = 0x01;
    public static final int HOME_MINING_PAGE = 0x02;
    private static final int HOME_MINING_LOGIN_WARNING_PAGE = 0x03;
    private static final int HOME_LOGIN_ACCOUNT = 0x04;
    private static final int HOME_LOGIN_ACCOUNT_CHECK = 0x07;
    private static final int HOME_LOGIN_PWD_CHECK = 0x05;
    private static final int HOME_TIP = 0x06;
    private static final int CHECK_LIVES_STATUS = 0x08;

    class LoginHandler extends Handler {

        private int indexError = 0;

        private int indexWhitePage = 0;//空白页

        @Override
        public void handleMessage(Message msg) {
            if (currentView == null) {
                handler.sendEmptyMessageDelayed(HOME_INIT, delay);
                return;
            }
            currentView.invalidate();
//            currentView.postInvalidate();
//            saveBitmap(currentView.getDrawingCache());
            switch (msg.what) {
                case HOME_INIT: {
                    postHermesEvent(MessageEvent.CLICK_CLEAR, HOME_INIT);
                }
                break;
                case HOME_MINING_PAGE: {
                    final ArrayList<Bitmap> arrayList = getBitmaps(HOME_MINING_PAGE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (compareKeyword(arrayList.get(0), new String[]{"键", "安", "装", "挖", "矿", "插", "件"})) {
                                postHermesEvent(MessageEvent.CLICK_INSTALL_PLUGIN, HOME_MINING_PAGE);
                            } else if (compareKeyword(arrayList.get(1), new String[]{"今", "日", "矿", "工"}) || compareKeyword(arrayList.get(3), new String[]{"今", "日", "矿", "工"})) {//挖矿收入
                                HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.NEXT_ACCOUNT));
                                log.info("账号 **********" + (VUserHandle.myUserId() + 1) + "  **********登录成功");
                            } else if (compareKeyword(arrayList.get(2), new String[]{"绑", "定", "L", "O", "账", "号"})) {
                                if (boundMode == 2) {
                                    postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            sendCapture();
                                        }
                                    }, 5000);
                                } else {
                                    HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.MANUAL_ACCOUNT));
                                }
//                                HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.NEXT_ACCOUNT));
                            } else if (compareKeyword(arrayList.get(4), new String[]{"萱", "宣", "宗", "登", "录", "未", "傲", "游", "账", "号", "时", "无", "法", "使", "用", "插", "件"})) {
                                postHermesEvent(MessageEvent.CLICK_LOGIN, HOME_MINING_LOGIN_WARNING_PAGE);
                            } else {
                                handleMiningWhitePage(HOME_MINING_PAGE);
                            }
                        }
                    }).start();
                }
                break;
                case HOME_MINING_LOGIN_WARNING_PAGE: {
                    final ArrayList<Bitmap> arrayList = getBitmaps(HOME_MINING_LOGIN_WARNING_PAGE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (compareKeyword(arrayList.get(0), new String[]{"萱", "宣", "宗", "登", "录", "未", "傲", "游", "账", "号", "时", "无", "法", "使", "用", "插", "件"})) {
                                postHermesEvent(MessageEvent.CLICK_LOGIN, HOME_MINING_LOGIN_WARNING_PAGE);
                            } else {
                                handleWhitePage(HOME_MINING_LOGIN_WARNING_PAGE);
                            }
                        }
                    }).start();

                }
                break;
                case HOME_LOGIN_ACCOUNT: {
                    final ArrayList<Bitmap> arrayList = getBitmaps(HOME_LOGIN_ACCOUNT);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (compareKeyword(arrayList.get(0), new String[]{"今", "切", "换", "到", "邮", "箱"})) {
                                postHermesEvent(MessageEvent.SWITCH_EMAIL, HOME_LOGIN_ACCOUNT);
                            } else {
                                sendMessageAfterClear(HOME_LOGIN_ACCOUNT);
                            }
                        }
                    }).start();

                }
                break;
                case HOME_LOGIN_ACCOUNT_CHECK: {
                    final ArrayList<Bitmap> arrayList = getBitmaps(HOME_LOGIN_ACCOUNT_CHECK);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (compareKeyword(arrayList.get(1), new String[]{"网", "络", "错", "误"})) {//网络蹈误
                                postHermesEvent(MessageEvent.CLICK_LOGIN_ACCOUNT, HOME_LOGIN_ACCOUNT_CHECK);
                            } else if (compareKeyword(arrayList.get(0), new String[]{"展", "记", "忘", "密", "码"})) {
                                postHermesEvent(MessageEvent.INPUT_PWD, HOME_LOGIN_ACCOUNT_CHECK);
                            } else if (!equalKeyword(arrayList.get(1), "")) {
                                indexError++;
                                if (indexError < 4) {
                                    postHermesEvent(MessageEvent.RETURN_ONCE, HOME_LOGIN_ACCOUNT_CHECK);
                                }
                            } else {
                                sendMessageAfterClear(HOME_LOGIN_ACCOUNT_CHECK);
                            }
                        }
                    }).start();

                }
                break;
                case HOME_LOGIN_PWD_CHECK: {
                    final ArrayList<Bitmap> arrayList = getBitmaps(HOME_LOGIN_PWD_CHECK);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (compareKeyword(arrayList.get(0), new String[]{"萱", "宣", "宗", "登", "录", "未", "傲", "游", "账", "号", "时", "无", "法", "使", "用", "插", "件"})) {
                                postHermesEvent(MessageEvent.CLICK_CANCEL, HOME_LOGIN_PWD_CHECK);
                            } else if (compareKeyword(arrayList.get(1), new String[]{"网", "络", "错", "误"})) {//网络蹈误
                                postHermesEvent(MessageEvent.CLICK_PWD_ACCOUNT, HOME_LOGIN_PWD_CHECK);
                            } else if (!equalKeyword(arrayList.get(2), "")) {
                                indexError++;
                                if (indexError < 4) {
                                    postHermesEvent(MessageEvent.RETURN_TWICE, HOME_LOGIN_PWD_CHECK);
                                }
                            } else {
                                sendMessageAfterClear(HOME_LOGIN_PWD_CHECK);
                            }
                        }
                    }).start();
                }
                break;
                case HOME_TIP: {
                    postHermesEvent(MessageEvent.CLICK_CLEAR, HOME_TIP);
                }
                break;
                case CHECK_LIVES_STATUS: {
                    final ArrayList<Bitmap> arrayList = getBitmaps(CHECK_LIVES_STATUS);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (compareKeyword(arrayList.get(0), new String[]{"今", "日", "矿", "工"}) || compareKeyword(arrayList.get(2), new String[]{"今", "日", "矿", "工"})) {//挖矿收入
                                HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.NEXT_ACCOUNT));
                                log.info("账号 **********" + (VUserHandle.myUserId() + 1) + "  **********登录成功");
                            } else if (compareKeyword(arrayList.get(1), new String[]{"绑", "定", "L", "O", "账", "号"})) {
                                HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.CLICK_REFRESH_CAPTURE));
                            } else {
                                handleMiningWhitePage(CHECK_LIVES_STATUS);
                            }
                        }
                    }).start();
                }
                break;
            }
        }

        private void postHermesEvent(int event, int page) {
            indexWhitePage = 0;
            removeMessages(page);
            HermesEventBus.getDefault().post(new MessageEvent(event));
        }

        private void handleUniError() {
            HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.NEXT_ACCOUNT));
            log.info("账号 " + (VUserHandle.myUserId() + 1) + " 登录异常：未知异常");
            indexWhitePage = 0;
        }

        private void handleWhitePage(int what) {
            indexWhitePage++;
            if (indexWhitePage > 10) {
                HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.CLICK_CLEAR));
                indexWhitePage = 0;
            } else {
                sendMessageAfterClear(what);
            }
        }

        private void handleMiningWhitePage(int what) {
            indexWhitePage++;
            if (indexWhitePage > 10) {
                HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.CLICK_CLEAR));
                indexWhitePage = 0;
            } else {
                sendMessageAfterClear(what);
            }
        }


        private ArrayList<Bitmap> getBitmaps(int type) {

            int width = currentView.getWidth();
//            int height = currentView.getHeight();
            ArrayList<Bitmap> arrayList = new ArrayList<>();
            switch (type) {
                case HOME_PAGE:
                    break;
                case HOME_MINING_PAGE:
                    if (isEmulator) {
                        arrayList.add(getImageFromScreenShot(currentView, 177, 656, 134, 33));
                        arrayList.add(getImageFromScreenShot(currentView, 164, 91, 76, 25));
//                        arrayList.add(getImageFromScreenShot(currentView, 52, 234, 60, 23));
                        arrayList.add(getImageFromScreenShot(currentView, 164, 135, 76, 25));//瞎写了
                        arrayList.add(getImageFromScreenShot(currentView, 110, 259, 256, 28));
                    } else {
                        if (width == 720) {
                            arrayList.add(getImageFromScreenShot(currentView, 105, 387, 302, 61));
                            arrayList.add(getImageFromScreenShot(currentView, 213, 179, 180, 65));
                            arrayList.add(getImageFromScreenShot(currentView, 222, 355, 280, 60));
                            arrayList.add(getImageFromScreenShot(currentView, 213, 262, 180, 65));
                            arrayList.add(getImageFromScreenShot(currentView, 313, 661, 92, 59));
                            break;
                        }
                        arrayList.add(getImageFromScreenShot(currentView, 150, 560, 447, 135));
                        arrayList.add(getImageFromScreenShot(currentView, 314, 262, 231, 86));
                        arrayList.add(getImageFromScreenShot(currentView, 320, 525, 440, 200));
                        arrayList.add(getImageFromScreenShot(currentView, 314, 360, 231, 86));
                        arrayList.add(getImageFromScreenShot(currentView, 152, 770, 750, 120));
                    }
                    break;
                case HOME_MINING_LOGIN_WARNING_PAGE:
                    if (isEmulator) {
                        arrayList.add(getImageFromScreenShot(currentView, 110, 259, 256, 28));
                    } else {
                        if (width == 720) {
                            arrayList.add(getImageFromScreenShot(currentView, 313, 661, 92, 59));
                            break;
                        } else if (width == 540) {
                            arrayList.add(getImageFromScreenShot(currentView, 87, 401, 366, 41));
                            break;
                        }
                        arrayList.add(getImageFromScreenShot(currentView, 152, 770, 750, 120));
                    }
                    break;
                case HOME_LOGIN_ACCOUNT:
                    if (isEmulator) {
                        arrayList.add(getImageFromScreenShot(currentView, 367, 333, 100, 30));
                    } else {
                        if (width == 720) {
                            arrayList.add(getImageFromScreenShot(currentView, 533, 577, 162, 55));
                            break;
                        } else if (width == 540) {
                            arrayList.add(getImageFromScreenShot(currentView, 401, 435, 119, 41));
                            break;
                        }
                        arrayList.add(getImageFromScreenShot(currentView, 801, 873, 226, 69));
                    }
                    break;
                case HOME_LOGIN_ACCOUNT_CHECK:
                    if (isEmulator) {
                        arrayList.add(getImageFromScreenShot(currentView, 398, 342, 65, 20));
                        arrayList.add(getImageFromScreenShot(currentView, 189, 363, 108, 22));
                    } else {
                        if (width == 720) {
                            arrayList.add(getImageFromScreenShot(currentView, 549, 585, 145, 53));
                            arrayList.add(getImageFromScreenShot(currentView, 240, 635, 236, 47));
                            break;
                        } else if (width == 540) {
                            arrayList.add(getImageFromScreenShot(currentView, 416, 437, 101, 43));
                            arrayList.add(getImageFromScreenShot(currentView, 190, 439, 193, 56));
                            break;
                        }
                        arrayList.add(getImageFromScreenShot(currentView, 831, 879, 200, 73));
                        arrayList.add(getImageFromScreenShot(currentView, 367, 945, 333, 75));
                    }
                    break;
                case HOME_LOGIN_PWD_CHECK:
                    if (isEmulator) {
                        arrayList.add(getImageFromScreenShot(currentView, 110, 259, 256, 28));
                        arrayList.add(getImageFromScreenShot(currentView, 189, 363, 108, 22));
                        arrayList.add(getImageFromScreenShot(currentView, 143, 367, 188, 24));
                    } else {
                        if (width == 720) {
                            arrayList.add(getImageFromScreenShot(currentView, 313, 661, 92, 59));
                            arrayList.add(getImageFromScreenShot(currentView, 240, 635, 236, 47));
                            arrayList.add(getImageFromScreenShot(currentView, 172, 635, 371, 47));
                            break;
                        } else if (width == 540) {
                            arrayList.add(getImageFromScreenShot(currentView, 87, 401, 366, 41));
                            arrayList.add(getImageFromScreenShot(currentView, 190, 439, 193, 56));
                            arrayList.add(getImageFromScreenShot(currentView, 190, 439, 193, 56));
                            break;
                        }
                        arrayList.add(getImageFromScreenShot(currentView, 152, 770, 750, 120));
                        arrayList.add(getImageFromScreenShot(currentView, 367, 945, 333, 75));
                        arrayList.add(getImageFromScreenShot(currentView, 267, 964, 538, 75));
                    }
                    break;
                case HOME_TIP:
                    break;
                case CHECK_LIVES_STATUS:
                    if (isEmulator) {//瞎写的
                        arrayList.add(getImageFromScreenShot(currentView, 110, 259, 256, 28));
                        arrayList.add(getImageFromScreenShot(currentView, 189, 363, 108, 22));
                        arrayList.add(getImageFromScreenShot(currentView, 143, 367, 188, 24));
                    } else {
                        if (width == 720) {
                            arrayList.add(getImageFromScreenShot(currentView, 213, 179, 180, 65));
                            arrayList.add(getImageFromScreenShot(currentView, 222, 355, 280, 60));
                            arrayList.add(getImageFromScreenShot(currentView, 213, 262, 180, 65));
                            break;
                        }
                        arrayList.add(getImageFromScreenShot(currentView, 314, 262, 231, 86));
                        arrayList.add(getImageFromScreenShot(currentView, 320, 525, 440, 200));
                        arrayList.add(getImageFromScreenShot(currentView, 314, 360, 231, 86));
                    }
                    break;
            }
            return arrayList;
        }
    }

    public void saveBitmap(Bitmap bitmap) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "zxing_image");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "zxing_image" + System.currentTimeMillis() + ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCapture() {
        currentView.invalidate();
        int width = currentView.getWidth();
        Bitmap bitmap = null;
        if (width == 720) {
            bitmap = getImageFromScreenShot(currentView, 456, 727, 190, 70);
        } else {
            bitmap = getImageFromScreenShotHalf(currentView, 664, 1100, 300, 100);
        }

        saveBitmap(bitmap);
//        final String bitmapStr = binaryToHexString(bitmap2Bytes(bitmap));
        final String bitmapStr = Base64.encode(bitmap2Bytes(bitmap));
        HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.INPUT_LIVES_ACCOUNT, bitmapStr));

    }

    byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private static String hexStr = "0123456789ABCDEF";

    public static String binaryToHexString(byte[] bytes) {

        String result = "";
        String hex = "";
        for (int i = 0; i < bytes.length; i++) {
            //字节高4位
            hex = String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4));
            //字节低4位
            hex += String.valueOf(hexStr.charAt(bytes[i] & 0x0F));
            result += hex;
        }
        return result;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CallbackEvent event) {
        switch (event.getCallbackId()) {
            case MessageEvent.HOME_RETURN:
                sendMessageAfterClear(HOME_PAGE);
                break;
            case MessageEvent.CLICK_MINING:
                sendMessageAfterClear(HOME_MINING_PAGE);
                break;
            case MessageEvent.CLICK_INSTALL_PLUGIN:
                sendMessageAfterClear(HOME_MINING_LOGIN_WARNING_PAGE);
                break;
            case MessageEvent.CLICK_LOGIN:
                sendMessageAfterClear(HOME_LOGIN_ACCOUNT);
                break;
            case MessageEvent.SWITCH_EMAIL:
            case MessageEvent.CLICK_LOGIN_ACCOUNT:
                sendMessageAfterClear(HOME_LOGIN_ACCOUNT_CHECK);
                break;
            case MessageEvent.INPUT_PWD:
            case MessageEvent.CLICK_PWD_ACCOUNT:
                sendMessageAfterClear(HOME_LOGIN_PWD_CHECK);
                break;
            case MessageEvent.CLICK_CANCEL:
                sendMessageAfterClear(HOME_TIP);
                break;
            case MessageEvent.CLICK_HOME:
                sendMessageAfterClear(HOME_PAGE);
                break;
            case MessageEvent.RETURN_ONCE:
            case MessageEvent.RETURN_TWICE:
                sendMessageAfterClear(HOME_MINING_LOGIN_WARNING_PAGE);
                break;
            case MessageEvent.CLICK_CLEAR:
            case MessageEvent.CLICK_MINING_CLEAR:
                sendMessageAfterClear(HOME_MINING_PAGE);
                break;
            case MessageEvent.CLICK_REFRESH_CAPTURE:
                sendMessageAfterClear(HOME_MINING_PAGE);
                break;
            case MessageEvent.INPUT_LIVES_ACCOUNT:
                sendMessageAfterClearDelay(CHECK_LIVES_STATUS);
                break;
            case MessageEvent.MANUAL_ACCOUNT:
                sendMessageAfterClear(CHECK_LIVES_STATUS);
                break;
        }
    }

}