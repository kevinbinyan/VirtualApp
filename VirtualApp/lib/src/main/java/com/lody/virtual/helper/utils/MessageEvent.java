package com.lody.virtual.helper.utils;

/**
 * Created by sunb on 2018/4/17.
 */

public class MessageEvent {

    public static final int CLICK_MINING = 0x03;//点击挖矿
    public static final int CLICK_INSTALL_PLUGIN = 0x04;//安装插件
    public static final int CLICK_HOME = 0x05;//点击HOME
    public static final int HOME_RETURN = 0x06;//点击手机返回键
    public static final int CLICK_LOGIN = 0x07;//点击挖矿页面中的登录
    public static final int SWITCH_EMAIL = 0x08;//切换邮箱
    public static final int INPUT_EMAIL = 0x09;//输入邮箱
    public static final int CLICK_LOGIN_ACCOUNT = 0x10;//登录页面的登录按钮

    private int current;

    public MessageEvent(int current) {
        this.current = current;
    }

    public int getCurrent() {
        return current;
    }
}
