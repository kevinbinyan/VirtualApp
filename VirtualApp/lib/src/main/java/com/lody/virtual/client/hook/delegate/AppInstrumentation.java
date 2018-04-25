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
//import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
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
import com.lody.virtual.helper.utils.CallbackEvent;
import com.lody.virtual.helper.utils.ConfigureLog4J;
import com.lody.virtual.helper.utils.CrashHandler;
import com.lody.virtual.helper.utils.MessageEvent;
import com.lody.virtual.helper.utils.SimilarPicture;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.interfaces.IUiCallback;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import mirror.android.app.ActivityThread;
import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * @author Lody
 */
public final class AppInstrumentation extends InstrumentationDelegate implements IInjector {

    private static final String TAG = AppInstrumentation.class.getSimpleName();

    private static AppInstrumentation gDefault;
    private TextView popText;
    private Handler handler;
    private View currentView;
    private Logger log;
    private boolean isEmulator;
    private long delay = 2000;
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
//        if (!isEmulator) {
//            WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
//            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//            popText = getTextView(activity, params);
//            windowManager.removeView(popText);
//            windowManager.addView(popText, params);
//            windowManager.updateViewLayout(popText, params);
//        }

        View rootView = activity.getWindow().getDecorView();
        traversalView(activity, rootView);
        HermesEventBus.getDefault().register(this);
    }

    public void traversalView(final Activity activity, final View view) {
        if (null == view) {
            return;
        }
        currentView = view;
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
        HermesEventBus.getDefault().unregister(this);
    }


    @Override
    public void callApplicationOnCreate(Application app) {
        super.callApplicationOnCreate(app);
        ConfigureLog4J configureLog4J = new ConfigureLog4J();
        configureLog4J.configure();
        HermesEventBus.getDefault().connectApp(app, app.getPackageName());
        log = Logger.getLogger("VirtualLives");
        CrashHandler.getInstance().init(app, log);

        isEmulator = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.EMULATOR, true);
        boolean loginNow = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.LOGIN_NOW, false);
        if (loginNow) {
            handler = new LoginHandler();
            handler.sendEmptyMessageDelayed(HOME_PAGE, 10000);
        }
        boolean autoOp = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.AUTO_OP, false);
        if (autoOp) {
            handler = new AutoOpHandler();
            handler.sendEmptyMessageDelayed(HOME_PAGE, 8000);
        }
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

    private boolean compareKeyword(Bitmap bitmap, String keyword) {
        return doOcr(bitmap, "chi_sim").contains(keyword);
    }

    private boolean equalKeyword(Bitmap bitmap, String keyword) {
        return doOcr(bitmap, "chi_sim").equals(keyword);
    }

    private void sendMessageAfterClear(int what) {
        handler.removeMessages(what);
        handler.sendEmptyMessageDelayed(what, delay);
    }


    public static final int HOME_PAGE = 0x01;
    public static final int HOME_MINING_PAGE = 0x02;
    private static final int HOME_MINING_LOGIN_WARNING_PAGE = 0x03;
    private static final int HOME_LOGIN_ACCOUNT = 0x04;
    private static final int HOME_LOGIN_ACCOUNT_CHECK = 0x07;
    private static final int HOME_LOGIN_PWD_CHECK = 0x05;
    private static final int HOME_TIP = 0x06;

    class LoginHandler extends Handler {

        private int indexError = 0;

        private int indexWhitePage = 0;//空白页

        @Override
        public void handleMessage(Message msg) {
            if (currentView == null) {
                handler.sendEmptyMessageDelayed(HOME_PAGE, delay);
                return;
            }
            currentView.invalidate();
            switch (msg.what) {
                case HOME_PAGE: {
                    final ArrayList<Bitmap> arrayList = getBitmaps(HOME_PAGE);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            if (compareKeyword(arrayList.get(0), "网址")) {//搜索或输入网址
                                postHermesEvent(MessageEvent.CLICK_MINING, HOME_PAGE);
                            } else if (compareKeyword(arrayList.get(1), "安装")) {
                                postHermesEvent(MessageEvent.CLICK_INSTALL_PLUGIN, HOME_PAGE);
                            } else if (isEmulator ? compareKeyword(arrayList.get(2), "宣宗") : compareKeyword(arrayList.get(2), "登录")) {
                                postHermesEvent(MessageEvent.CLICK_LOGIN, HOME_PAGE);
                            } else if (compareKeyword(arrayList.get(3), "返回")) {
                                postHermesEvent(MessageEvent.HOME_RETURN, HOME_PAGE);
                            } else if (!equalKeyword(arrayList.get(4), "")) {//非空页面
                                postHermesEvent(MessageEvent.CLICK_HOME, HOME_PAGE);
                            } else if (equalKeyword(arrayList.get(4), "")) {//空白页面
                                sendMessageAfterClear(HOME_PAGE);
                                handleWhitePage();
                            } else {
                                sendMessageAfterClear(HOME_PAGE);
                            }
                        }
                    }).start();
                }
                break;
                case HOME_MINING_PAGE: {
                    final ArrayList<Bitmap> arrayList = getBitmaps(HOME_MINING_PAGE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (compareKeyword(arrayList.get(0), "安装")) {
                                postHermesEvent(MessageEvent.CLICK_INSTALL_PLUGIN, HOME_MINING_PAGE);
                            } else if (isEmulator ? compareKeyword(arrayList.get(1), "宣宗") : compareKeyword(arrayList.get(1), "登录")) {
                                postHermesEvent(MessageEvent.CLICK_LOGIN, HOME_MINING_PAGE);
                            } else if (compareKeyword(arrayList.get(2), "入账号")) {//喻入账号
//                                HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.NEXT_ACCOUNT));
                                log.info("账号 " + (VUserHandle.myUserId() + 1) + " 登录失败：未绑定共生账号");
                            } else if (compareKeyword(arrayList.get(3), "矿工") || (currentView.getWidth() == 720 ? compareKeyword(arrayList.get(5), "矿工") : false)) {//挖矿收入
                                HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.NEXT_ACCOUNT));
                                log.info("账号 " + (VUserHandle.myUserId() + 1) + " 登录成功");
                            } else if (equalKeyword(arrayList.get(4), "")) {//空包页面
                                sendMessageAfterClear(HOME_MINING_PAGE);
                                handleWhitePage();
                            } else {
                                sendMessageAfterClear(HOME_MINING_PAGE);
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
                            if (isEmulator ? compareKeyword(arrayList.get(0), "宣宗") : compareKeyword(arrayList.get(0), "登录")) {
                                postHermesEvent(MessageEvent.CLICK_LOGIN, HOME_MINING_LOGIN_WARNING_PAGE);
                            } else if (equalKeyword(arrayList.get(1), "")) {//空包页面
                                sendMessageAfterClear(HOME_MINING_LOGIN_WARNING_PAGE);
                                handleWhitePage();
                            } else {
                                sendMessageAfterClear(HOME_MINING_LOGIN_WARNING_PAGE);
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
                            if (isEmulator ? compareKeyword(arrayList.get(0), "今 切橡黜瓤氟") : compareKeyword(arrayList.get(0), "切换到邮箱")) {
                                postHermesEvent(MessageEvent.SWITCH_EMAIL, HOME_LOGIN_ACCOUNT);
                            } else if (isEmulator ? compareKeyword(arrayList.get(1), "宣宗") : compareKeyword(arrayList.get(1), "登录")) {
                                postHermesEvent(MessageEvent.CLICK_LOGIN, HOME_LOGIN_ACCOUNT);
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
                            if (compareKeyword(arrayList.get(1), "网络")) {//网络蹈误
                                postHermesEvent(MessageEvent.CLICK_LOGIN_ACCOUNT, HOME_LOGIN_ACCOUNT_CHECK);
                            } else if (isEmulator ? compareKeyword(arrayList.get(0), "展记鹭码") : compareKeyword(arrayList.get(0), "忘记密码")) {
                                postHermesEvent(MessageEvent.INPUT_PWD, HOME_LOGIN_ACCOUNT_CHECK);
                            } else if (!equalKeyword(arrayList.get(1), "")) {
//                                handleUniError();
                                indexError++;
                                if (indexError < 4) {
                                    postHermesEvent(MessageEvent.RETURN_ONCE, HOME_LOGIN_ACCOUNT_CHECK);
                                }
                            }
//                            else if (equalKeyword(arrayList.get(1), "")) {
//                                postHermesEvent(MessageEvent.CLICK_LOGIN_ACCOUNT, HOME_LOGIN_PWD_CHECK);
//                            }
                            else {
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
                            if (isEmulator ? compareKeyword(arrayList.get(0), "萱录") : compareKeyword(arrayList.get(0), "登录")) {
                                postHermesEvent(MessageEvent.CLICK_CANCEL, HOME_LOGIN_PWD_CHECK);
                            } else if (compareKeyword(arrayList.get(1), "网络")) {//网络蹈误
                                postHermesEvent(MessageEvent.CLICK_PWD_ACCOUNT, HOME_LOGIN_PWD_CHECK);
                            } else if (!equalKeyword(arrayList.get(2), "")) {
                                indexError++;
                                if (indexError < 4) {
                                    postHermesEvent(MessageEvent.RETURN_TWICE, HOME_LOGIN_PWD_CHECK);
                                }
                            } else if (equalKeyword(arrayList.get(2), "")) {
                                postHermesEvent(MessageEvent.CLICK_PWD_ACCOUNT, HOME_LOGIN_PWD_CHECK);
                            } else {
                                sendMessageAfterClear(HOME_LOGIN_PWD_CHECK);
                            }
                        }
                    }).start();
                }
                break;
                case HOME_TIP: {
                    final ArrayList<Bitmap> arrayList = getBitmaps(HOME_TIP);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (isEmulator ? compareKeyword(arrayList.get(0), "萱录") : compareKeyword(arrayList.get(0), "登录")) {
                                postHermesEvent(MessageEvent.CLICK_HOME, HOME_TIP);
                            } else if (equalKeyword(arrayList.get(1), "")) {//空包页面
                                sendMessageAfterClear(HOME_TIP);
                                handleWhitePage();
                            }
//                            else {
//                                handleUniError();
//                            }
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

        private void handleWhitePage() {
            indexWhitePage++;
            if (indexWhitePage > 10) {
                HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.CLICK_HOME));
                indexWhitePage = 0;
            }
        }

        private ArrayList<Bitmap> getBitmaps(int type) {

            int width = currentView.getWidth();
//            int height = currentView.getHeight();
            ArrayList<Bitmap> arrayList = new ArrayList<>();
            switch (type) {
                case HOME_PAGE:
                    if (isEmulator) {
                        arrayList.add(getImageFromScreenShot(currentView, 62, 152, 127, 29));
                        arrayList.add(getImageFromScreenShot(currentView, 177, 656, 134, 33));
                        arrayList.add(getImageFromScreenShot(currentView, 215, 338, 47, 28));
                        arrayList.add(getImageFromScreenShot(currentView, 18, 37, 37, 19));
//                        arrayList.add(getImageFromScreenShot(currentView, 52, 234, 60, 23));
//                        arrayList.add(getImageFromScreenShot(currentView, 0, 235, 85, 35));
                        arrayList.add(getImageFromScreenShot(currentView, 130, 245, 235, 250));
                    } else {
                        if (width == 720) {
                            arrayList.add(getImageFromScreenShot(currentView, 117, 305, 270, 58));
                            arrayList.add(getImageFromScreenShot(currentView, 105, 387, 302, 61));
                            arrayList.add(getImageFromScreenShot(currentView, 313, 661, 92, 59));
                            arrayList.add(getImageFromScreenShot(currentView, 35, 64, 77, 52));
                            arrayList.add(getImageFromScreenShot(currentView, 165, 446, 381, 455));
                            break;
                        }
                        arrayList.add(getImageFromScreenShot(currentView, 170, 461, 383, 81));
                        arrayList.add(getImageFromScreenShot(currentView, 150, 620, 447, 95));
                        arrayList.add(getImageFromScreenShot(currentView, 475, 1023, 126, 78));
                        arrayList.add(getImageFromScreenShot(currentView, 52, 96, 103, 68));
//                        arrayList.add(getImageFromScreenShot(currentView, 154, 687, 190, 82));
//                        arrayList.add(getImageFromScreenShot(currentView, 50, 702, 187, 82));
                        arrayList.add(getImageFromScreenShot(currentView, 228, 664, 560, 613));
                    }
                    break;
                case HOME_MINING_PAGE:
                    if (isEmulator) {
                        arrayList.add(getImageFromScreenShot(currentView, 177, 656, 134, 33));
//                        arrayList.add(getImageFromScreenShot(currentView, 188, 650, 106, 33));
                        arrayList.add(getImageFromScreenShot(currentView, 215, 338, 47, 28));
                        arrayList.add(getImageFromScreenShot(currentView, 52, 234, 60, 23));
                        arrayList.add(getImageFromScreenShot(currentView, 0, 235, 85, 35));  //需要改
                        arrayList.add(getImageFromScreenShot(currentView, 130, 245, 235, 250));
                    } else {
                        if (width == 720) {
                            arrayList.add(getImageFromScreenShot(currentView, 105, 387, 302, 61));
                            arrayList.add(getImageFromScreenShot(currentView, 313, 661, 92, 59));
                            arrayList.add(getImageFromScreenShot(currentView, 102, 452, 126, 61));
                            arrayList.add(getImageFromScreenShot(currentView, 213, 179, 149, 49));
                            arrayList.add(getImageFromScreenShot(currentView, 165, 446, 381, 455));

                            arrayList.add(getImageFromScreenShot(currentView, 243, 190, 157, 49));//小板终极页面
                            break;
                        }
                        arrayList.add(getImageFromScreenShot(currentView, 150, 620, 447, 95));
//                        arrayList.add(getImageFromScreenShot(currentView, 188, 650, 106, 33));
                        arrayList.add(getImageFromScreenShot(currentView, 475, 1023, 126, 78));
                        arrayList.add(getImageFromScreenShot(currentView, 154, 687, 190, 82));
                        arrayList.add(getImageFromScreenShot(currentView, 314, 262, 231, 86));
                        arrayList.add(getImageFromScreenShot(currentView, 228, 664, 560, 613));
                    }
                    break;
                case HOME_MINING_LOGIN_WARNING_PAGE:
                    if (isEmulator) {
                        arrayList.add(getImageFromScreenShot(currentView, 215, 338, 47, 28));
//                        arrayList.add(getImageFromScreenShot(currentView, 367, 333, 100, 30));
                        arrayList.add(getImageFromScreenShot(currentView, 130, 245, 235, 250));
                    } else {
                        if (width == 720) {
                            arrayList.add(getImageFromScreenShot(currentView, 313, 661, 92, 59));
                            arrayList.add(getImageFromScreenShot(currentView, 165, 446, 381, 455));
                            break;
                        }
                        arrayList.add(getImageFromScreenShot(currentView, 475, 1023, 126, 78));
//                        arrayList.add(getImageFromScreenShot(currentView, 801, 873, 226, 69));
                        arrayList.add(getImageFromScreenShot(currentView, 228, 664, 560, 613));
                    }
                    break;
                case HOME_LOGIN_ACCOUNT:
                    if (isEmulator) {
                        arrayList.add(getImageFromScreenShot(currentView, 367, 333, 100, 30));
                        arrayList.add(getImageFromScreenShot(currentView, 215, 338, 47, 28));
//                        arrayList.add(getImageFromScreenShot(currentView, 186, 293, 110, 28));
//                        arrayList.add(getImageFromScreenShot(currentView, 189, 363, 108, 22));
//                        arrayList.add(getImageFromScreenShot(currentView, 398, 342, 65, 20));
//                        arrayList.add(getImageFromScreenShot(currentView, 220, 528, 35, 23));
//                        arrayList.add(getImageFromScreenShot(currentView, 130, 245, 235, 250));
//                        arrayList.add(getImageFromScreenShot(currentView, 215, 338, 47, 28));
                    } else {
                        if (width == 720) {
                            arrayList.add(getImageFromScreenShot(currentView, 533, 577, 162, 55));
                            arrayList.add(getImageFromScreenShot(currentView, 313, 661, 92, 59));
                            break;
                        }
                        arrayList.add(getImageFromScreenShot(currentView, 801, 873, 226, 69));
                        arrayList.add(getImageFromScreenShot(currentView, 475, 1023, 126, 78));
//                        arrayList.add(getImageFromScreenShot(currentView, 361, 736, 354, 86));
//                        arrayList.add(getImageFromScreenShot(currentView, 367, 945, 333, 75));
//                        arrayList.add(getImageFromScreenShot(currentView, 831, 879, 200, 73));
//                        arrayList.add(getImageFromScreenShot(currentView, 475, 1306, 131, 81));
//                        arrayList.add(getImageFromScreenShot(currentView, 228, 664, 560, 613));
//                        arrayList.add(getImageFromScreenShot(currentView, 475, 1023, 126, 78));
                    }
                    break;
                case HOME_LOGIN_ACCOUNT_CHECK:
                    if (isEmulator) {
                        arrayList.add(getImageFromScreenShot(currentView, 398, 342, 65, 20));
                        arrayList.add(getImageFromScreenShot(currentView, 189, 363, 108, 22));
//                        arrayList.add(getImageFromScreenShot(currentView, 220, 528, 35, 23));
                    } else {
                        if (width == 720) {
                            arrayList.add(getImageFromScreenShot(currentView, 549, 585, 145, 53));
                            arrayList.add(getImageFromScreenShot(currentView, 240, 635, 236, 47));
                            break;
                        }
                        arrayList.add(getImageFromScreenShot(currentView, 831, 879, 200, 73));
                        arrayList.add(getImageFromScreenShot(currentView, 367, 945, 333, 75));
//                        arrayList.add(getImageFromScreenShot(currentView, 475, 1306, 131, 81));
                    }
                    break;
                case HOME_LOGIN_PWD_CHECK:
                    if (isEmulator) {
                        arrayList.add(getImageFromScreenShot(currentView, 215, 338, 47, 28));
//                        arrayList.add(getImageFromScreenShot(currentView, 195, 300, 89, 24));
                        arrayList.add(getImageFromScreenShot(currentView, 189, 363, 108, 22));
                        arrayList.add(getImageFromScreenShot(currentView, 143, 367, 188, 24));
//                        arrayList.add(getImageFromScreenShot(currentView, 220, 528, 35, 23));
//                        arrayList.add(getImageFromScreenShot(currentView, 215, 338, 47, 28));
                        arrayList.add(getImageFromScreenShot(currentView, 130, 245, 235, 250));
                    } else {
                        if (width == 720) {
                            arrayList.add(getImageFromScreenShot(currentView, 313, 661, 92, 59));
                            arrayList.add(getImageFromScreenShot(currentView, 240, 635, 236, 47));
                            arrayList.add(getImageFromScreenShot(currentView, 172, 635, 371, 47));
                            break;
                        }
                        arrayList.add(getImageFromScreenShot(currentView, 475, 1023, 126, 78));
//                        arrayList.add(getImageFromScreenShot(currentView, 410, 759, 251, 82));
                        arrayList.add(getImageFromScreenShot(currentView, 367, 945, 333, 75));
                        arrayList.add(getImageFromScreenShot(currentView, 267, 964, 538, 75));
//                        arrayList.add(getImageFromScreenShot(currentView, 475, 1306, 131, 81));
//                        arrayList.add(getImageFromScreenShot(currentView, 475, 1023, 126, 78));
                        arrayList.add(getImageFromScreenShot(currentView, 228, 664, 560, 613));
                    }
                    break;
                case HOME_TIP:
                    if (isEmulator) {
                        arrayList.add(getImageFromScreenShot(currentView, 215, 338, 47, 28));
//                        arrayList.add(getImageFromScreenShot(currentView, 62, 152, 127, 29));
                        arrayList.add(getImageFromScreenShot(currentView, 130, 245, 235, 250));
                    } else {
                        if (width == 720) {
                            arrayList.add(getImageFromScreenShot(currentView, 313, 661, 92, 59));
                            arrayList.add(getImageFromScreenShot(currentView, 165, 446, 381, 455));
                            break;
                        }
                        arrayList.add(getImageFromScreenShot(currentView, 475, 1023, 126, 78));
//                        arrayList.add(getImageFromScreenShot(currentView, 170, 461, 383, 81));
                        arrayList.add(getImageFromScreenShot(currentView, 228, 664, 560, 613));
                    }
                    break;
            }
            return arrayList;
        }
    }

    class AutoOpHandler extends Handler {

        private static final int AUTO_CHECK = 0x01;
        private Bitmap lastArea;
        private int countNoMove;

        @Override
        public void handleMessage(Message msg) {
            if (currentView == null) {
                handler.sendEmptyMessageDelayed(AUTO_CHECK, delay);
                return;
            }
            currentView.invalidate();
            switch (msg.what) {
                case AUTO_CHECK:
                    final Bitmap homePage = getImageFromScreenShot(currentView, 62, 152, 127, 29);
                    final Bitmap returnWord = getImageFromScreenShot(currentView, 18, 37, 37, 19);
                    final Bitmap area = getImageFromScreenShot(currentView, 200, 200, 200, 400);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (compareKeyword(returnWord, "返回")) {
                                HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.HOME_RETURN_BY_AUTO));
                                sendMessageAfterClear(AUTO_CHECK);
                            } else if (compareBitmap(area, lastArea)) {
                                countNoMove++;
                                if (countNoMove > 10) {
                                    HermesEventBus.getDefault().post(new MessageEvent(MessageEvent.SCROLLDOWN_TO_AUTO));
                                    countNoMove = 0;
                                }
                                sendMessageAfterClear(AUTO_CHECK);
                            }
                        }
                    }).start();
                    break;
            }


        }

        private boolean compareBitmap(Bitmap area, Bitmap lastArea) {
            if (lastArea == null) {
                lastArea = area;
                return false;
            }
            int index = 0;
            while (index < 20) {
                index++;
                int x = new Random().nextInt(area.getWidth());
                int y = new Random().nextInt(area.getHeight());
                if (area.getPixel(x, y) != lastArea.getPixel(x, y)) {
                    lastArea = area;
                    return false;
                }
            }
            lastArea = area;
            return true;
        }
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
        }
    }
}