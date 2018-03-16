package mirror.com.android.internal.telephony;

import android.os.IBinder;
import android.os.IInterface;

import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

/**
 * Created by sunb on 2018/3/16.
 */

public class IPhoneSubInfoMSim {
    public static Class<?> TYPE = RefClass.load(IPhoneSubInfoMSim.class, "com.android.internal.telephony.IPhoneSubInfoMSim");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(IPhoneSubInfoMSim.Stub.class, "com.android.internal.telephony.IPhoneSubInfoMSim$Stub");
        @MethodParams({IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}
