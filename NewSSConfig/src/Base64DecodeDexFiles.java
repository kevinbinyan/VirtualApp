import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Base64DecodeDexFiles {

    public static void main(String[] args) {

        File file = new File("D:\\output\\mx");
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getName());
            if (files[i].isFile()) {
                FileInputStream inputStream;
                try {
                    inputStream = new FileInputStream(files[i]);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String str = null;
                    StringBuffer sb = new StringBuffer();
                    while ((str = bufferedReader.readLine()) != null) {
                        sb.append(str);
                    }
                    inputStream.close();
                    bufferedReader.close();
                    byte[] data = Base64.decode(sb.toString());
                    FileOutputStream fos = new FileOutputStream(new File("D:\\output", files[i].getName()));
                    fos.write(data);
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

        }
    }
}
