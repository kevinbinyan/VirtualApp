package mirror.android.os;

import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

/**
 * Created by Kevin on 2018/4/5.
 */

public class SystemProperties {
    public static Class<?> TYPE = RefClass.load(SystemProperties.class, "android.os.SystemProperties");
    @MethodParams({String.class, String.class})
    public static RefStaticMethod<String> get;
    @MethodParams({String.class, String.class})
    public static RefStaticMethod<Void> set;
}
