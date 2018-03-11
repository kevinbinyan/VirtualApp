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
//            return getDeviceInfo().deviceId;
            return ParamSettings.deviceIds[VUserHandle.myUserId()];
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
            return "3455566";
        }
    }


}
