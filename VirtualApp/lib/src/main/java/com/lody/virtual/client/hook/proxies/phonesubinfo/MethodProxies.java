package com.lody.virtual.client.hook.proxies.phonesubinfo;

import android.os.UserHandle;

import com.lody.virtual.client.VClientImpl;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.helper.ParamSettings;
import com.lody.virtual.helper.utils.marks.FakeDeviceMark;
import com.lody.virtual.os.VUserHandle;

import java.lang.reflect.Method;

/**
 * @author Lody
 */
@SuppressWarnings("ALL")
class MethodProxies {

    @FakeDeviceMark("fake device id")
    static class GetDeviceId extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getDeviceId";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            return getDeviceInfo().deviceId;
//            return ParamSettings.getDeviceIds()[VUserHandle.myUserId()];
        }
    }

    static class GetDeviceIdForSubscriber extends GetDeviceId {

        @Override
        public String getMethodName() {
            return "getDeviceIdForSubscriber";
        }

    }

    @FakeDeviceMark("fake iccid")
    static class GetIccSerialNumber extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getIccSerialNumber";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            return getDeviceInfo().iccId;
//            return "d3333333333333333";
        }
    }


    static class getIccSerialNumberForSubscriber extends GetIccSerialNumber {
        @Override
        public String getMethodName() {
            return "getIccSerialNumberForSubscriber";
        }
    }

    static class GetImei extends GetDeviceId {

        @Override
        public String getMethodName() {
            return "getImei";
        }

    }

    static class GetSubscriberId extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getSubscriberId";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            return getDeviceInfo().imsi;
//            return ParamSettings.getImsis()[VUserHandle.myUserId()];
        }
    }

    static class GetSubscriberIdForSubscriber extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getSubscriberIdForSubscriber";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            return getDeviceInfo().imsi;
//            return ParamSettings.getImsis()[VUserHandle.myUserId()];
        }
    }

    @FakeDeviceMark("fake line number")
    static class GetLine1Number extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getLine1Number";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            return getDeviceInfo().lineNumber;
        }
    }



}
