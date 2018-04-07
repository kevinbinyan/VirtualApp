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

public class CheckMultipleAccount {

	private static final int NUMBER = 100;

	public static void main(String[] args) {

		File file = new File("D:\\allaccount");
		File[] files = file.listFiles();
		ArrayList<Set<String>> sets = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i].getName());
			FileInputStream inputStream;
			try {
				inputStream = new FileInputStream(files[i]);
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				Set<String> set = new HashSet();

				String str = null;
				while ((str = bufferedReader.readLine()) != null) {
					String[] params = str.trim().split(",");
					set.add(params[2]);
				}
				sets.add(set);
				inputStream.close();
				bufferedReader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < sets.size(); i++) {
			for (int j = i + 1; j < sets.size(); j++) {
//				System.out.println("ASet " + i + ":" + sets.get(i).size());
//				System.out.println("BSet " + j + ":" + sets.get(j).size());
				Set<String> c = new HashSet<>();
				c.addAll(sets.get(i));
				c.addAll(sets.get(j));
				if(sets.get(i).size() + sets.get(j).size() > c.size()){
					System.out.println("CSet:" + c.size());
				}
//				System.out.println("CSet:" + c.size());
//				System.out.println();
			}
		}
	}

}
