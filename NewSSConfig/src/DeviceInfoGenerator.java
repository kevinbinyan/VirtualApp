import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class DeviceInfoGenerator {

    private static final int NUMBER = 100;
    private static String modulus;
    private static String public_exponent;
    private static String private_exponent;
    
    public static void main(String[] args) {
        
        modulus = "101139253338155537122681263551391401692066665916613487436275955722010199471415841485729163754132286657951275782618854770472010908407158470741951949410587800589127059181738617385251968563652490730289519152085655065302311553563299905910600441758613944432476284758060061258064772215795815169533468766442967476449";
        System.out.println(modulus);
        // 公钥指数
//        public_exponent = publicKey.getPublicExponent().toString();
        public_exponent = "65537";
        System.out.println(public_exponent);
        // 私钥指数
//        private_exponent = privateKey.getPrivateExponent().toString();
        private_exponent = "77040033353587478351181338141034990369862215683099041858893937555861134440278777222165884672323082873057748117004376901547725049339972199183804313083082114860116154901276523598153162839702785813272951961243156651418620364910731144201588093748132726391031044890152993376853663320094215905479322137162494227093";
        System.out.println(private_exponent);
        
        StringBuffer sb = new StringBuffer();
        StringBuffer getIMEI = new StringBuffer();
        StringBuffer getImsi = new StringBuffer();
        StringBuffer getMac = new StringBuffer();
        for (int i = 0; i < NUMBER; i++) {
            getIMEI.append("\"" + getIMEI() + "\",\n");
            getImsi.append("\"" + getImsi() + "\",\n");
            getMac.append("\"" + getMac() + "\",\n");

        }
        sb.append("{\r\n");
        sb.append("\"imeis\": [");
        sb.append(getIMEI.deleteCharAt(getIMEI.lastIndexOf(",")));
        sb.append("],\r\n");
        sb.append("\"macs\": [");
        sb.append(getMac.deleteCharAt(getMac.lastIndexOf(",")));
        sb.append("],\r\n");
        sb.append("\"imsies\": [");
        sb.append(getImsi.deleteCharAt(getImsi.lastIndexOf(",")));
        sb.append("]\r\n");
        sb.append("}");
        
        try {
            writeToFile(sb,"MINGCHENG", "D:\\");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        System.out.println(sb.toString());
    }

    private static String getIMEI() {// calculator IMEI
        int r1 = 1000000 + new java.util.Random().nextInt(9000000);
        int r2 = 1000000 + new java.util.Random().nextInt(9000000);
        String input = r1 + "" + r2;
        char[] ch = input.toCharArray();
        int a = 0, b = 0;
        for (int i = 0; i < ch.length; i++) {
            int tt = Integer.parseInt(ch[i] + "");
            if (i % 2 == 0) {
                a = a + tt;
            } else {
                int temp = tt * 2;
                b = b + temp / 10 + temp % 10;
            }
        }
        int last = (a + b) % 10;
        if (last == 0) {
            last = 0;
        } else {
            last = 10 - last;
        }
        return input + last;
    }

    private static String getImsi() {
        // 460022535025034
        String title = "4600";
        int second = 0;
        do {
            second = new java.util.Random().nextInt(8);
        } while (second == 4);
        int r1 = 10000 + new java.util.Random().nextInt(90000);
        int r2 = 10000 + new java.util.Random().nextInt(90000);
        return title + "" + second + "" + r1 + "" + r2;
    }

    private static String getMac() {
        char[] char1 = "ABCDEF".toCharArray();
        char[] char2 = "0123456789".toCharArray();
        StringBuffer mBuffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            int t = new java.util.Random().nextInt(char1.length);
            int y = new java.util.Random().nextInt(char2.length);
            int key = new java.util.Random().nextInt(2);
            if (key == 0) {
                mBuffer.append(char2[y]).append(char1[t]);
            } else {
                mBuffer.append(char1[t]).append(char2[y]);
            }

            if (i != 5) {
                mBuffer.append(":");
            }
        }
        return mBuffer.toString();
    }
    
    public static void writeToFile(StringBuffer sb, String name, String string) throws IOException {
        // for (int i = 0; i < sbs.length; i++) {
        FileWriter fw = new FileWriter((string.endsWith("\\") ? string : string + File.separator)
                + encrypt(name));
        // DESUtil des = new DESUtil(KEY);
        try {
            // 明文
            // 使用模和指数生成公钥和私钥
            RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
            RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);

            fw.write(RSAUtils.encryptByPublicKey(sb.toString(), pubKey));
            fw.close();
//            System.out
//                    .println(RSAUtils.decryptByPrivateKey(readAssetsTxt(new FileInputStream((string.endsWith("\\") ? string : string + File.separator)
//                            + encrypt(name.substring(0, name.indexOf("."))))), priKey));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println(des.encryptStr(sb.toString()));

    }

    public static String readAssetsTxt(InputStream is) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while ((n = is.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            String text = new String(out.toByteArray());
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
            // throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }

    public final static String encrypt(String plaintext) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
