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
		// 鍏挜鎸囨暟
		// public_exponent = publicKey.getPublicExponent().toString();
		public_exponent = "65537";
		System.out.println(public_exponent);
		RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
		
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(new File("D:\\VL-2.8.apk"));
			// InputStream is =
			// context.getAssets().open(MD5Utils.encrypt("origin.data"));
			File temp = new File("D:\\" + encrypt("aoyou.data"));
			FileOutputStream fos = new FileOutputStream(temp);
			byte[] buffer = new byte[1024];
			int byteCount = inputStream.read(buffer);
			String head = RSAUtils.encryptByPublicKey(Base64.encode(buffer), pubKey);
			System.out.println(head); //放在androidstudio中  AppRepository partdata
			while ((byteCount = inputStream.read(buffer)) != -1) {// 循环从输入流读取
																	// buffer字节
				fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
			}
			fos.flush();// 刷新缓冲区
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
			// 鑾峰緱MD5鎽樿绠楁硶鐨� MessageDigest 瀵硅薄
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 浣跨敤鎸囧畾鐨勫瓧鑺傛洿鏂版憳瑕�
			mdInst.update(btInput);
			// 鑾峰緱瀵嗘枃
			byte[] md = mdInst.digest();
			// 鎶婂瘑鏂囪浆鎹㈡垚鍗佸叚杩涘埗鐨勫瓧绗︿覆褰㈠紡
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
