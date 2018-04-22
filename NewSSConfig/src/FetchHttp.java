import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class FetchHttp {

	private static final int NUMBER = 100;

	public static void main(String[] args) {

		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream("E:\\text.txt");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			Set<String> set = new HashSet();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				int index = str.indexOf("<a href=\"http");
				if (index >= 0) {
					index = index + "<a href=\"http".length();
					set.add(str.substring(index-4, str.indexOf("\"", index)));
				}
			}

			for (String string : set) {
				System.out.println(string);
			}
			// close
			inputStream.close();
			bufferedReader.close();

			// writeToFile(sbs, args[1]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
