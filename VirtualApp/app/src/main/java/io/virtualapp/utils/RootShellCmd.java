package io.virtualapp.utils;

import java.io.OutputStream;

/**
 * Created by sunb on 2018/4/12.
 */

public class RootShellCmd {
    private OutputStream os;

    /**
     * 执行shell指令
     *
     * @param cmd 指令
     */
    public final void exec(String cmd) {
        try {
            if (os == null) {
                os = Runtime.getRuntime().exec("su").getOutputStream();
            }
            os.write(cmd.getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 后台模拟全局按键
     *
     * @param keyCode 键值
     */
    public final void simulateKey(int keyCode) {
        exec("input keyevent " + keyCode + "\n");
    }
}
