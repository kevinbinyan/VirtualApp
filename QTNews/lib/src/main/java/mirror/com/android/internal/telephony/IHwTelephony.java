package mirror.com.android.internal.telephony;

import android.os.IBinder;
import android.os.IInterface;

import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

/**
 * Created by sunb on 1/24/2018.
 */

public class IHwTelephony {

    public static Class<?> TYPE = RefClass.load(IHwTelephony.class, "com.android.internal.telephony.IHwTelephony");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(IHwTelephony.Stub.class, "com.android.internal.telephony.IHwTelephony$Stub");
        @MethodParams({IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}
