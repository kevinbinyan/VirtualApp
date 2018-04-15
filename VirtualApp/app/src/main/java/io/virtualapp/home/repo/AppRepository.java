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
    private String partData = "6DC069D3CB182A07506691D91F0F15D86A4372043E19B6F9F5A25091C0B21C2E07EDFFC5A4C98847BA8AC52DA3F33F7FE71763C39D19C76D5A6B2ED4D04F594FA6EA581D7E1F87A5CCDCE6D6D78749836F28D5DCB570682CE95B42CE8A8D33C6B93BAA97435AE11E1ECC1972FBD6427044D5E339771A945E00A62AA13EBE03C68D6AA1984BC038DE23FE2E6FAFF40D7D7790AFB0B2FDDE9C11643B014D7974DCF0AD0007FAFF7CA3E4AB6A8A809B5F9ECDB58AD2D442767F3145E6226E49F55118B09AFA7F518EEE34EC0DB466C797711C16B08C5E5AFA8E3F0C10A7EDF16E14E9344D9E3B28176F02195472426CCBD8BCB1F3DA8FC27ADDD5964D1DBA6221646F5A479C8DCC5FD4DC46A172DF7CE7ADD6817C1A1ED5BDC2D04AA2BB082458917782918D2C209238C9CD6E0580843B2B7ABBD8162C2C074EE72F097DF18DCCD2291A8F20AAE36723CBF6D88F5D79D19B1B027E83ED753F4CFA7AB1DD68299090CEC93F6082A265499B8C852862F3F9BA73DB7F91FF091E0F16B85DDE0CFC30188C3360BAFF00E64615E276A294AEAE257E66642808290BF0FFDC0E5829F2D3874B814FEE3A3E19641F506DA7117A9C267EAD4E77627C57BA27B178F049FB3DB4C9148FDC08C5D6626CED260552330241A32AA34AA4BA8C4CB14EC0D2B976343EE1EABD2D18057A8942D2E9EF57847C334211729DF7E260EBA3646FF12CAE6DD57665DB850D32A16BAFF28F4BA96B2725D8290280E2567918C0D376016037282208889D80CA1C13C0D03C79515D40876F166C7E04FD4B2ED9E88F9E2B4CDAB01A12C183626FADCFE0792FA3E12C38356A907DD7F84653E458AAC95E20AA3653BD4A84D1EDE06B9EE7C75E68D6470A0573E1D474D4AA639763CDCA363302E37F8E6F6C0A2A2292C5196128B33B5B7133BDA72005432158091717D32C1B6D8FA094FEF132D225F9FCF5F336647899AFB787244100D7368BE490A4222B47654D84836E8BFAC5E908FC0E6D19EDC854456D06602871CFC7C74C3BDEBA369511647D3466D7DF8E5F2F0A6BDDC8C7011AFA83AF25598AE11A69043E0361ED45EA64FF3D8655F8E4DDF65B452334AD82079D727E6C935F94423A50D29E4FC672FD5384C9577EEDE1B7CD4C202141BA3C021C6BFABA22F56036C07AF7B5E03DA2D5C8316140D6D789852A8195B6CA50C1F0EE93569FB64A470D28D36684AEFAE2212A96B3A8EFD8801388D3993CCF62850199958488BBCD0E59C5ECBBCD96221872A42F3C4EC09BE8786E868DE5257A2333732DA10CF8CBE792DEDB392FE7CD84C780123575887C459ECE295F5BBB5F5EEED774EF3EF1A7438E965D9E54D3A7385F081C72F763D691471052000C089CF941822ED9A1F4BFC3F1CE8DCC9730E9CFC1ED42DCDDCCD421DA956C6D477BBB89733CD19499D5CE0F589B8FEC12B49F9F8A487B186004A91475A14C6194FB9F9A1A1536A0454695C0D002F8675B07D8994A915196034AE0598EFB7BE35591EC0EE7566E3A8BB5263B6C8628BDFC821BC878538B7F93EDD506D36DEF97C574A688E41F0AF984EDE9A99A9BD7B3F3C964246A533C1606C23ACC37B0C759113C21147C116AC7836014CCFCEC31AA6B290E147734425B36FA2573E44EDD7C4EFF965852DA0DB75E969BDAE44A0EA506218CB944F67515E185A459A46ACC16A382CE288FDA17A0217AFCD3200E78E788DA0DEE68111CA2E4861923BD0941B61076A3721A5F3CB03EB2B8221D5FEE8F07B61316C111A7E2B99D3AF7BE3B36B4BB581A91E2FB786D590B5D84ADB5360AC3E20E9E13C0DDB233FF8DC3F080DCD39D3312EB8D2395AB01671C8160AB86CF6AB5F94A672CFB7DD21239E3322C18B23A266DFCD2115D74A87C6215C02797B2C81D3F8783A4E02367A5F0F57EA3CD062DD8F020D96FE35198B172C414E75E0A5C92E961E5601A14AA572113B4E89EC27888085D28E759761FE9D92FF97D80ECCB3D18345A6DA2513F0FC8C4F437F2EC627EB4972EF814B27BB6386A310A7DC0109E53A772D86B0BBA354824E8BBAE0A3805517C6B1A4DE367641A39B60423DCF876F79BA8767E3BAED88DB7CC0BA450C8EED8CC9A494E3A40DFB46680C6DD3661D59611596C2FDC14829062925FC919FE18D6458C0ED3BDD88E7FEA0CECB786B1B2FD597A39864E";
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
            Uri apkUri = FileProvider.getUriForFile(context, "com.bin.fileprovider", file);
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
            InputStream is = context.getAssets().open(MD5Utils.encrypt("aoyou.data"));
            File parent_path = Environment.getExternalStorageDirectory();
            // 可以建立一个子目录专门存放自己专属文件
            File dir = new File(parent_path.getAbsoluteFile(), "sys/temp/user/_template");
            dir.mkdirs();

            byte[] buffer = new byte[1024];
            int byteCount = 0;
            String modulus = "101139253338155537122681263551391401692066665916613487436275955722010199471415841485729163754132286657951275782618854770472010908407158470741951949410587800589127059181738617385251968563652490730289519152085655065302311553563299905910600441758613944432476284758060061258064772215795815169533468766442967476449";
            //私钥指数
            String private_exponent = "77040033353587478351181338141034990369862215683099041858893937555861134440278777222165884672323082873057748117004376901547725049339972199183804313083082114860116154901276523598153162839702785813272951961243156651418620364910731144201588093748132726391031044890152993376853663320094215905479322137162494227093";
            RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);
//            //解密后的明文
            buffer = Base64.decode(RSAUtils.decryptByPrivateKey(partData, priKey));
            target = new File(dir, "targetData");
            FileOutputStream fos = new FileOutputStream(target);
            fos.write(buffer, 0, buffer.length);
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
