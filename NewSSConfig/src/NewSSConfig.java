import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

public class NewSSConfig {

	public static void main(String[] args) {
		String ip = args[0];
		String output = args[1];
		File file = new File(output);
		try {
			if (file.exists()) {
				file.delete();
			}
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println("{");
			ps.println("\"server\": \"" + ip + "\",");
			ps.println("\"server_ipv6\": \"::\",");
			ps.println("\"local_address\": \"127.0.0.1\",");
			ps.println("\"local_port\": 1081,");
			ps.println("\"port_password\": {");
			ps.println("\"8388\": \"sunshine\",");
			for (int i = 8389; i < 8388 + 500; i++) {
				if(i == 8388 + 499){
					ps.println("\"" + i + "\": \"" + genRandomNum(8) + "\"");
					break;
				}
				ps.println("\"" + i + "\": \"" + genRandomNum(8) + "\",");
			}
			ps.println("},");
			ps.println("\"timeout\": 120,");
			ps.println("\"udp_timeout\": 60,");
			ps.println("\"method\": \"aes-256-cfb\",");
			ps.println("\"protocol\": \"auth_sha1_v2\",");
			ps.println("\"protocol_param\": \"\",");
			ps.println("\"obfs\": \"tls1.2_ticket_auth\",");
			ps.println("\"obfs_param\": \"\",");
			ps.println("\"dns_ipv6\": false,");
			ps.println("\"connect_verbose_info\": 0,");
			ps.println("\"redirect\": \"\",");
			ps.println("\"fast_open\": false,");
			ps.println("\"workers\": 1");
			ps.println("}");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 
	  * 生成随即密码 
	  * @param pwd_len 生成的密码的总长度 
	  * @return  密码的字符串 
	  */  
	 public static String genRandomNum(int pwd_len){  
	  //35是因为数组是从0开始的，26个字母+10个数字  
	  int i;  //生成的随机数  
	  int count = 0; //生成的密码的长度  
	  char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',  
	    'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
	    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',  
	    'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
	    'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};  
	    
	  StringBuffer pwd = new StringBuffer("");  
	  Random r = new Random();  
	  while(count < pwd_len){  
	   //生成随机数，取绝对值，防止生成负数，  
	     
	   i = Math.abs(r.nextInt(str.length));  //生成的数最大为36-1  
	     
	   if (i >= 0 && i < str.length) {  
	    pwd.append(str[i]);  
	    count ++;  
	   }  
	  }  
	    
	  return pwd.toString();  
	 }  

}
