package com.lody.virtual.client.hook.delegate;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

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
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.interfaces.IUiCallback;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import mirror.android.app.ActivityThread;
import xiaofei.library.hermeseventbus.HermesEventBus;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * @author Lody
 */
public final class AppInstrumentation extends InstrumentationDelegate implements IInjector {

    private static final String TAG = AppInstrumentation.class.getSimpleName();

    private static AppInstrumentation gDefault;
    private TextView popText;

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
        Logger log = Logger.getLogger("VirtualLives");
        CrashHandler.getInstance().init(activity, log);

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

        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        traversalView(activity, rootView);
        HermesEventBus.getDefault().post(new MessageEvent());
    }

    public void traversalView(final Activity activity, final View view) {
        if (null == view) {
            return;
        }

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            long time = 0;

            @Override
            public boolean onPreDraw() {
                boolean loginNow = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.LOGIN_NOW, false);
                if (!loginNow) {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    LinkedList<ViewGroup> queue = new LinkedList<ViewGroup>();
                    queue.add(viewGroup);
                    while (!queue.isEmpty()) {
                        final ViewGroup current = queue.removeFirst();
                        for (int i = 0; i < current.getChildCount(); i++) {
                            if (current.getChildAt(i) instanceof ViewGroup) {
                                queue.addLast((ViewGroup) current.getChildAt(i));
                            } else {
                                if (current.getChildAt(i) instanceof TextView) {
                                    final TextView textView = (TextView) current.getChildAt(i);
                                    if (textView.getText().toString().equalsIgnoreCase("挖矿Go")) {
                                        if (System.currentTimeMillis() - time > 5000 && !activity.isFinishing() && !activity.isDestroyed()) {
                                            time = System.currentTimeMillis();
                                            screenshot(view, (VUserHandle.myUserId() + 1));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
            }
        });
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
//        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
//        screenshot(VUserHandle.myUserId(), rootView);
    }


    @Override
    public void callApplicationOnCreate(Application app) {
        super.callApplicationOnCreate(app);
        ConfigureLog4J configureLog4J = new ConfigureLog4J();
        configureLog4J.configure();
        Log.e("LLLL", app.getPackageName());
        HermesEventBus.getDefault().connectApp(app, app.getPackageName());
    }

    private void screenshot(View dView, int userId) {
        // 获取屏幕
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        dView.setDrawingCacheEnabled(true);
        Bitmap bmp = dView.getDrawingCache();
        dView.buildDrawingCache();
        if (bmp != null) {
            try {
                // 获取内置SD卡路径
                String sdCardPath = getExternalStorageDirectory().getPath() + "/VirtualLives/" + time.format(new Date());
                File path = new File(sdCardPath);
                path.mkdirs();
                // 图片文件路径
                final String fileName = "screenshot_" + userId + ".jpg";
                final String filePath = sdCardPath + File.separator + fileName;
                File file = new File(filePath);
                if (!file.exists())
                    file.createNewFile();
                FileOutputStream os = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
                Log.e("Check", "Runnable");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        uploadFile(fileName, filePath);
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
