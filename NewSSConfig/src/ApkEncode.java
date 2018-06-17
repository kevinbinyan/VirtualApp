import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.interfaces.RSAPublicKey;

public class ApkEncode {

	private static String modulus;
	private static String public_exponent;

	public static void main(String[] args) {

		// DESUtil des = new DESUtil();
		// des.setKey("ks20180414bingyanXX");
		// try {
		// des.encryptFile("E:\\aoyou_tuoke_v_signed_Aligned.apk", "E:\\" +
		// encrypt("origin.data"));
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		modulus = "101139253338155537122681263551391401692066665916613487436275955722010199471415841485729163754132286657951275782618854770472010908407158470741951949410587800589127059181738617385251968563652490730289519152085655065302311553563299905910600441758613944432476284758060061258064772215795815169533468766442967476449";
		System.out.println(modulus);
		// 閸忣剟鎸滈幐鍥ㄦ殶
		// public_exponent = publicKey.getPublicExponent().toString();
		public_exponent = "65537";
		System.out.println(public_exponent);
		RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
		
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(new File("E:\\mx5.2.1.3224cn_rm_virtual.apk"));
			// InputStream is =
			// context.getAssets().open(MD5Utils.encrypt("origin.data"));
			File temp = new File("E:\\" + encrypt("aoyou.data"));
			FileOutputStream fos = new FileOutputStream(temp);
			byte[] buffer = new byte[1024];
			int byteCount = inputStream.read(buffer);
			String head = RSAUtils.encryptByPublicKey(Base64.encode(buffer), pubKey);
			System.out.println(head); //鏀惧湪androidstudio涓�  AppRepository partdata
			while ((byteCount = inputStream.read(buffer)) != -1) {// 寰幆浠庤緭鍏ユ祦璇诲彇
																	// buffer瀛楄妭
				fos.write(buffer, 0, byteCount);// 灏嗚鍙栫殑杈撳叆娴佸啓鍏ュ埌杈撳嚭娴�
			}
			fos.flush();// 鍒锋柊缂撳啿鍖�
			inputStream.close();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final static String encrypt(String plaintext) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = plaintext.getBytes();
			// 閼惧嘲绶盡D5閹芥顩︾粻妤佺《閻拷 MessageDigest 鐎电钖�
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 娴ｈ法鏁ら幐鍥х暰閻ㄥ嫬鐡ч懞鍌涙纯閺傜増鎲崇憰锟�
			mdInst.update(btInput);
			// 閼惧嘲绶辩�靛棙鏋�
			byte[] md = mdInst.digest();
			// 閹跺﹤鐦戦弬鍥祮閹广垺鍨氶崡浣稿彋鏉╂稑鍩楅惃鍕摟缁楋缚瑕嗚ぐ銏犵础
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
