import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MergeAccount {

	private static final int NUMBER = 100;

	public static void main(String[] args) {

		FileInputStream inputStream1;
		FileInputStream inputStream2;
		try {
			inputStream1 = new FileInputStream("D:\\7.1 3组分出110老牛.txt");// 共生
			inputStream2 = new FileInputStream("D:\\550傲游.txt");// 遨游
			BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));
			BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream2));

			ArrayList<String> list1 = new ArrayList();
			ArrayList<String> list2 = new ArrayList();

			String str = null;
			while ((str = bufferedReader1.readLine()) != null) {
				list1.add(str.trim());
			}
			while ((str = bufferedReader2.readLine()) != null) {
				list2.add(str.trim());
			}
			bufferedReader1.close();
			bufferedReader2.close();

			ArrayList<String> list = new ArrayList();

			int index1 = 0, index2 = 0;
			for (int i = index1; i < list1.size(); i++) {
				for (int j = index2; j < index2 + 5 && j < list2.size(); j++) {
					String temp = list2.get(j) + "----" + list1.get(i);
					list.add(temp);
					System.out.println(temp);
				}
				index2 += 5;
				index1++;
			}

			// writeToFile(sbs, args[1]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void writeToFile(StringBuffer[] sbs, String string) throws IOException {
		for (int i = 0; i < sbs.length; i++) {
			FileWriter fw = new FileWriter(
					(string.endsWith("\\") ? string : string + File.separator) + "account_" + (i + 1) + ".txt");
			fw.write(sbs[i].toString());
			fw.close();
		}

	}
}
