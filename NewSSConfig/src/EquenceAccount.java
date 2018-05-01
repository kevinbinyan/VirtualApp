import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

public class EquenceAccount {

	private static final int NUMBER = 100;

	public static void main(String[] args) {

		FileInputStream inputStream;
		try {
			// inputStream = new FileInputStream("E:\\新建文本文档 (7).txt");
			System.out.println("输入文件：" + args[0]);
			System.out.println("输出文件：" + args[1]);
			inputStream = new FileInputStream(args[0]);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			ArrayList<String> array = new ArrayList();

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				if (!array.contains(str.trim())) {
					array.add(str.trim());
				}
			}

			array.sort(new Comparator() {

				@Override
				public int compare(Object arg0, Object arg1) {
					String args0 = (String) arg0;
					String args1 = (String) arg1;
					// System.out.println("args0：" + args0);
					// System.out.println("args1：" + args1);
					// String[] args0_p = args0.split("\\|");
					// String[] args1_p = args1.split("\\|");
					String[] args0_p = args0.split("----");
					String[] args1_p = args1.split("----");
					// String[] args0_p = args0.split(",");
					// String[] args1_p = args1.split(",");
					if (args0_p.length > 1) {
						return (args0_p[2].compareTo(args1_p[2]));
					} else {
						return (args0.compareTo(args1_p[2]));
					}
				}

			});
			inputStream.close();
			bufferedReader.close();
			System.out.println("-------------------------------");
			writeToFile(array, args[1]);
			System.out.println("导出成功！");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void writeToFile(ArrayList<String> array, String target) throws IOException {
		FileWriter fw = new FileWriter(target);
		for (String str : array) {
			// if(str.startsWith("login;")){
			// fw.write( str + "\r\n");
			// }else{
			// fw.write(("login;" + str + "\r\n").replace("----", ","));
			// }
			fw.write(str + "\r\n");
		}
		fw.close();

	}
}
