package io.virtualapp.home.repo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.lody.virtual.GmsSupport;
import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.Base64;
import com.lody.virtual.helper.utils.MD5Utils;
import com.lody.virtual.helper.utils.RSAUtils;
import com.lody.virtual.remote.InstallResult;
import com.lody.virtual.remote.InstalledAppInfo;

import org.jdeferred.Promise;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.virtualapp.abs.ui.VUiKit;
import io.virtualapp.home.models.AppData;
import io.virtualapp.home.models.AppInfo;
import io.virtualapp.home.models.AppInfoLite;
import io.virtualapp.home.models.MultiplePackageAppData;
import io.virtualapp.home.models.PackageAppData;

/**
 * @author Lody
 */
public class AppRepository implements AppDataSource {

    //原生脱壳的APK
//    private String partData = "6B9F90685E573273041DA37708D59A59EFCCCF29BF370ED1677C2A41144138AB38B864BFB852819E7B6F5E0AA59C4403084598921B0B6DB0CEDE80C7CEE0BDBDF497828F7D2D25A662FD08386A0051489AD2C31F0135E765D077B377184FDCBC0184FCDBE0869658CA90F2FD9708A2DC08BE8B3FFDB8C1F8699819610D8E987C6F6A5C1890297DFF4A908D56FE9348F02735F69151673690FDD34C0A3CB169A861BD1CD9B8ABB771B53B1F68EE8AF85DA4EE8B1B43B517E1FEE2B650A8EBE04A54A09EAD9E1EFEE6153DAE763D2CAEC53A251DBC3F7C0737D4B318F348C92F9488634B0BE039A51EECA6AF2AAB5ACF29D3C9C7311938B20CFB788240D7869DA784FFC18C513A5195A61D4D8EC473EE49F2F32D79185B07D4F8A85F4CF4BE28BB42DE7EE2DB54C3798249C1F56814DD22003A1301419BECB664DB35517D86AF7552374ABE7975B3EBCB1678E2EC489D1FBCDC38BB557408D719EBFE0F02D7C329F441314295F2505AC105A3983EBDC2836C4BB2B0AC3D7D60924BB8A13F01A9C080CAE1A628822A7377698A51B68C5845B15072B8CF8D7349EC27E32DC12C39E7DF9135458DF6ADAAFAE1E77EE0F65167552CAD73E63F1FBE4F0E778E117A0C6948EEFD52056D1D9045CE59C94444482D0CCD91750324ED5F9C24D2C486EA57DFC8E8559E8241409A8EB0C0199EF3CB6339CBE781420A6F896C28F3D86EA744BA5700D5C0ED96DFB5304441ED4D1CD2C06D08171DBBBBE228C6445284C6C9F581ACB40243F86E5A815E6FDBDE1D5A22F9BD2A969FC9415F9DAF4520707DBD11B5CF95B9FFBF3D6813D5D2B32E09B07C2ECFC7D4F834DA87B4F67104E8D60A7A25EB0765FE459F4390F04E6309B3EDA2A9B107F0F95107602973A4548440E583EC8CAB2D881F9EA05884247E0FD20F7D6FB9C738C82A9D03CEE36ECC536E69CB17E57D31D4EE2388F628D9D33113E819F4C7FF9172A13B289A91C9CD53EE12997C7F4F887863911C26EDEF559D166622767923A3C84431D630CBAC82921C191BE03621AFD91C222F493C88A08B0B86E78CF78721A1E0C58CC0F7F74C8F7D2D61CB0AE25A57C09F806B7B9E3E33958C264F660DEA9222A8856A83FB5D7433998A01729C8890FD303C73A9EAA47D68E6468F381D6A05A516E6F4510CE33EF25BDA5273EC2610E299E46A33B16FDB1101EA131A6590E9CA8ECB6EB6FA3F469AC7EEE9D5BC3545F812F71225236D21E6FA66E57369AA0749C9BE776CA2555F58AC0F1E02EEF2189921ACD1232475E2AFF29FDA189011570971B20204EAFB5D4DE7552D84F5AC84E17D2B6394A5CEF268CE4CE77FA4D5C2ED87D7F67B592C6F52DF54ADBFEE48594C356A72D737A4FF367DA5396DAB23947AA4A4384DA9D7AA0FB80573986FA29440FEC9EDE137D61881CBFE652E9F78450658E3DF52459EDFC51E5AA72BC85248B81C71D9DB1713F8F64BC5FF4C52993A9B7F73913617F6CDAD8EDBD1AEBE21E00C0E55EA8B209AEF74D89C377C87CD6AFD5B10C7125FCA2DD4D2BC60AA030C9910938F232A9B6EDAAB0367F750A669C1547A12497D0AAC2C9F1BBA1AB4F89717860DB12C80701911EA1CC6E8DB04378C64702493B53A95096689AC9714D36E7B568FF719565210279C92374E0FBAF680AE9120DB4CAAE0AE85C4E97A9EFFA03C8DC9876FDB570B4A95DFB920C1CD2A036F29323013939E4D5A2636C6AEB2460AD9471B84AC84DF76F9FA7C55881D1BC9300983DF9E45E33240B23F78F4B4A9451ED929447561B8836FB29D0C045D007A11133BCD8D08D930DF1792008E9F3207BEACA2B655220E2ED2CBDD311729BB728B1D07D537ABF8980A08C7B70C54833A6BE0EDA5922C1A3C9D18CC60BC24511973CF0AE35510FAAFCDF2F948DBEDFFE8CFDC859FA47259F2EF7007A087CD4A02462D03E6495702B5BD22D1C9FFB00AB8B74629C0ACC119FB32C62D192D56A2BDB81F2A6831441FB0CF1D952E7ED8A685AC2B95BD7ACEF9974A98321398C7A332338DF203619B6E3284E5F5338489C55D6DEBBB6DD414F3101A4F91EBE0970A15A837A049E66600755F01B17509F215DA2B771086888859925CF4D820F0D878205407F3E7A3D1E2E931C87F22E834C313B8D515CAB76753F981E9EB9EEBF52C1A634CE2B24173BBDE443B0A28";
    //阿里加固的APK
    private String partData = "3EE56734F2C3D38AEBBF60896619C277D315718B26FDE1E678456AC1F716C8031E11FE82D08B7E6E3F84C75611920364D3DCA3EADEFC60FC46FA2F7B1056DADD41050EB6C7E140912CD27D33899F9B4F2F55F01206006A6444795E7CE638E6366744AC435F24AD80A9559605130A204C25BECDC3971DAB3BC9662C573488F3C3226B80BC277183607116C296671087CFAE6C184B6FFEC28D91012C77BD91892D17DDFE4F7233D866D49B4FDBDC911260706BA9AE5D3045661ADBEE91A84C4C30B5A837C8A2E91E92D0389276CE3301CAAD8952216E3F1C080B37164BEAFB3A1902645BEFA700DCDC7F4309152B5A59FA975E0F3B45F16EB743B6B5A57827313D297FFA9C741EE09BE02A223C1A0BE9D5CDDB727C738BDDDCB532AFE55C6B7853203218E5A277C701E189510198D45CEA3E8311E54917DD57EED365115A1B827F28BDDD3FA6B6290869F10DAB4804E3F643975B7458DF0B8B80BD5DA2CA88360079E4AA668E5425FE6C59EAD67CBC0CFCAC9C5B1A2B73F576FBF46D1226B2C50327C1F941A6E95F9EAB38F87AB222EE46CE76E44DC2A228210403A378A8C87DE3BA179435A3F1A5F0E9CF2AF38734D368CD0B60F19A25F22A17CCF9F3A5A07D6D0CEAE9953765CF72A51BE67F282B0E6BE7784BE68FA94643CC35F7B1E9A76A4BB4F970E6FFC24467978ABA7C9CED4D28811083FA914C9AFFD5DD9A80B24BB1471A01F7B0DDE366005F45C4571A3914374838B177D3B704C8CA71306049F9AA18BECB00B4CFA3182830B058CCF08A5F5EDA1E0FFFBA35C00BAE22DF7A3185D0D97441ACF3EEB717147CF1C8928E71EA460F6B9195E256D912BF996E80C230226BFFCBDD1B3B5E6C7A9ECB2C359F1B1E57B34AA0B43B4613DFEADF35B07987DF2304F460F84EFC77962E5FE184BAFBF310C3E271AD8EE30BED78541980D9748D7CE33597573A91928F65F4D68C2BCD9CAC2584980540844E12B4EB2EDF4EA4040E7BB3F7DF856E1428DBAB1CD16463685B4370BF021446B86CDF74EA409685B22BE0903FE260A3FE494C5379B4328BE0E4E03257C995A575FED6F6C5A019ECC70A415994538B4A3F0CEB444BDB967F8B93B83AD92DD5CF1CD82303B7D7BCFFAEA8ECED6828BCE99772ABEEE95255FF035D890FDA7EFC75F20761F0C04FC2BD67929AA93AC6DC0E7227E8D3EA9B6521CBB1BC4166079D3BB142811908FEC979E04A41D5E498D2FAFA11911B50E6C9228D133E9E2100DCC7B05A001C80F9C92AD1003E160CA2CA552B51DCFD20A30905642C0582FCC2C5B8ABE2200ACCC192698878D7F0826901BA64DD743C655B7960EE8C940EEB71935C58F1759FA5488221874FF81DDE1FEB5343DAB3F30E7959193B87D5E83F1CEBC43B34810A64186F692531115E6E84D5BA1A1810BD1A7987D201EB096FC4789524130B44A8CA06018202D1296E5443D13754EBD4E0256215E4DEA628A02DCDC812D5DA99670FE24136B17B2F4E32153743E288607CD551A20477E9F89C9D43681778DBE8193996B61AB983A005731747559E493B13D5203525DA611E5B39D1585C07B8132C9B9631781F8E494CF962F951BEE6B3466734C7D5CB818DF6370C88848B25A73DC688F4E31F93459196601A8679358568D93BA646CB663E61781E47A95A475215D0D550FA810A431FF5F64572CF998E546DA0463C12B58D31AC00E9000FE57D66F0B9C5180F3AE45F3BE47C5C827635ED8ED5F99F3557F10B489FFBA62AABBBFE2CA179FC3FF29142DF3045995E9AC93F23EE36B6946F31A51C1BE693074578959A62288B95534339CD8B348789D2982A23F18B282196CBAC3F2299AC80B73DB3FEC77C67B0414DBA02C62BE24F9A86B224C843AD6F2C42195F2E0056BC1E45CB474D6B352E45404FC7FF5C2BA8FFCFDF26843D3B6D8B45925BE12A992167E20C76F4EA4113E3CE8021551EB3964DBB52EE9898F68CA88D27D42C7B3A10526494CE9946CEFFCB312FFF5895AD3A4BF61F1B079DED7477E213E83D8259EEE1E0743DE9D1870198B1C7C8FD4B109A71F022FB7EAF6355AF175549B808C8F09C1AFC15FD36D11C83A418FFA0DF23944D6AE184D117DCE8F69757A16C8EE3C9322C67580430EB637DE53F47EDBD7B6F35AE59D95003151D13AF0F798FDB761658BBC7BA4A4F058761";
    private static final Collator COLLATOR = Collator.getInstance(Locale.CHINA);
    private static final List<String> SCAN_PATH_LIST = Arrays.asList(
            ".",
            "wandoujia/app",
            "tencent/tassistant/apk",
            "BaiduAsa9103056",
            "360Download",
            "pp/downloader",
            "pp/downloader/apk",
            "pp/downloader/silent/apk");

    private Context mContext;

    public AppRepository(Context context) {
        mContext = context;
    }

    private static boolean isSystemApplication(PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0
                && !GmsSupport.isGmsFamilyPackage(packageInfo.packageName);
    }

    @Override
    public Promise<List<AppData>, Throwable, Void> getVirtualApps() {
        return VUiKit.defer().when(() -> {
            List<InstalledAppInfo> infos = VirtualCore.get().getInstalledApps(0);
            List<AppData> models = new ArrayList<>();
            for (InstalledAppInfo info : infos) {
                if (!VirtualCore.get().isPackageLaunchable(info.packageName)) {
                    continue;
                }
                PackageAppData data = new PackageAppData(mContext, info);
                if (VirtualCore.get().isAppInstalledAsUser(0, info.packageName)) {
                    models.add(data);
                }
                int[] userIds = info.getInstalledUsers();
                for (int userId : userIds) {
                    if (userId != 0) {
                        models.add(new MultiplePackageAppData(data, userId));
                    }
                }
            }
            return models;
        });
    }

    @Override
    public Promise<List<AppInfo>, Throwable, Void> getInstalledApps(Context context) {
        return VUiKit.defer().when(() -> convertPackageInfoToAppData(context, context.getPackageManager().getInstalledPackages(0), true));
    }

    @Override
    public Promise<List<AppInfo>, Throwable, Void> getStorageApps(Context context, File rootDir) {
        return VUiKit.defer().when(() -> convertPackageInfoToAppData(context, installMX(context, rootDir, SCAN_PATH_LIST), false));
    }

    public List<PackageInfo> installMX(Context context, File rootDir, List<String> paths) {
        List<PackageInfo> packageList = new ArrayList<>();
        if (paths == null)
            return packageList;
        for (String path : paths) {
            File[] dirFiles = new File(rootDir, path).listFiles();
            if (dirFiles == null)
                continue;
            for (File f : dirFiles) {
                if (!f.getName().toLowerCase().endsWith(".apk"))
                    continue;
                PackageInfo pkgInfo = null;
                try {
                    pkgInfo = context.getPackageManager().getPackageArchiveInfo(f.getAbsolutePath(), 0);
                    pkgInfo.applicationInfo.sourceDir = f.getAbsolutePath();
                    pkgInfo.applicationInfo.publicSourceDir = f.getAbsolutePath();
                } catch (Exception e) {
                    // Ignore
                }
                if (pkgInfo != null)
                    packageList.add(pkgInfo);
            }
        }
        return packageList;
    }

    public void installMX(Context context) {
        File f = redirctFile(context);
        installApk(context, f);
    }

    public static void installApk(Context context, File file) {
        if (context == null || file == null) {
            return;
        }


        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(context, "com.bin.youchain.fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    private File redirctFile(Context context) {
        File target = null;
        try {
            InputStream is = context.getAssets().open("origin");
            File parent_path = Environment.getExternalStorageDirectory();
            // 可以建立一个子目录专门存放自己专属文件
            File dir = new File(parent_path.getAbsoluteFile(), "sys/temp/user/yc_template");
            dir.mkdirs();

            byte[] buffer = new byte[1024];
            int byteCount = 0;
//            String modulus = "101139253338155537122681263551391401692066665916613487436275955722010199471415841485729163754132286657951275782618854770472010908407158470741951949410587800589127059181738617385251968563652490730289519152085655065302311553563299905910600441758613944432476284758060061258064772215795815169533468766442967476449";
//            //私钥指数
//            String private_exponent = "77040033353587478351181338141034990369862215683099041858893937555861134440278777222165884672323082873057748117004376901547725049339972199183804313083082114860116154901276523598153162839702785813272951961243156651418620364910731144201588093748132726391031044890152993376853663320094215905479322137162494227093";
//            RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);
////            //解密后的明文
//            buffer = Base64.decode(RSAUtils.decryptByPrivateKey(partData, priKey));
            target = new File(dir, "targetData");
            FileOutputStream fos = new FileOutputStream(target);
//            fos.write(buffer, 0, buffer.length);
            while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
            }
            fos.flush();//刷新缓冲区
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    public List<AppInfo> convertPackageInfoToAppData(Context context, List<PackageInfo> pkgList, boolean fastOpen) {
        PackageManager pm = context.getPackageManager();
        List<AppInfo> list = new ArrayList<>(pkgList.size());
        String hostPkg = VirtualCore.get().getHostPkg();
        for (PackageInfo pkg : pkgList) {
            // ignore the host package
            if (hostPkg.equals(pkg.packageName)) {
                continue;
            }
            // ignore the System package
            if (isSystemApplication(pkg)) {
                continue;
            }

            ApplicationInfo ai = pkg.applicationInfo;
            String path = ai.publicSourceDir != null ? ai.publicSourceDir : ai.sourceDir;
            if (path == null) {
                continue;
            }
            AppInfo info = new AppInfo();
            info.packageName = pkg.packageName;
            info.fastOpen = fastOpen;
            info.path = path;
            info.icon = ai.loadIcon(pm);
            info.name = ai.loadLabel(pm);
            InstalledAppInfo installedAppInfo = VirtualCore.get().getInstalledAppInfo(pkg.packageName, 0);
            if (installedAppInfo != null) {
                info.cloneCount = installedAppInfo.getInstalledUsers().length;
            }
            list.add(info);
        }
        return list;
    }

    private boolean isOrderedApp(String packageName, String pkg) {
        return packageName.equalsIgnoreCase(pkg);// || packageName.equalsIgnoreCase("com.example.kevin.deviceinfo");
    }

    @Override
    public InstallResult addVirtualApp(AppInfoLite info) {
        int flags = InstallStrategy.COMPARE_VERSION | InstallStrategy.SKIP_DEX_OPT;
        if (info.fastOpen) {
            flags |= InstallStrategy.DEPEND_SYSTEM_IF_EXIST;
        }
        return VirtualCore.get().installPackage(info.path, flags);
    }

    @Override
    public boolean removeVirtualApp(String packageName, int userId) {
        return VirtualCore.get().uninstallPackageAsUser(packageName, userId);
    }

    public List<AppInfo> convertPackageInfoToAppData(Context context, List<PackageInfo> pkgList, boolean fastOpen, String orderedPkg) {
        PackageManager pm = context.getPackageManager();
        List<AppInfo> list = new ArrayList<>(pkgList.size());
        String hostPkg = VirtualCore.get().getHostPkg();
        for (PackageInfo pkg : pkgList) {
            // ignore the host package
            if (hostPkg.equals(pkg.packageName)) {
                continue;
            }
            // ignore the System package
            if (isSystemApplication(pkg)) {
                continue;
            }
            if (!isOrderedApp(pkg.packageName, orderedPkg)) {
                continue;
            }
            ApplicationInfo ai = pkg.applicationInfo;
            String path = ai.publicSourceDir != null ? ai.publicSourceDir : ai.sourceDir;
            if (path == null) {
                continue;
            }
            AppInfo info = new AppInfo();
            info.packageName = pkg.packageName;
            info.fastOpen = fastOpen;
            info.path = path;
            info.icon = ai.loadIcon(pm);
            info.name = ai.loadLabel(pm);
            InstalledAppInfo installedAppInfo = VirtualCore.get().getInstalledAppInfo(pkg.packageName, 0);
            if (installedAppInfo != null) {
                info.cloneCount = installedAppInfo.getInstalledUsers().length;
            }
            list.add(info);
        }
        return list;
    }
}
