package com.lody.virtual.client.hook.proxies.telephony;

import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.Inject;

import mirror.com.android.internal.telephony.IHwTelephony;

/**
 * Created by sunb on 1/24/2018.
 */
@Inject(HwMethodProxies.class)
public class HwTelephonyStub extends BinderInvocationProxy {

    public HwTelephonyStub() {
        super(IHwTelephony.Stub.asInterface, "phone_huawei");
    }
}
