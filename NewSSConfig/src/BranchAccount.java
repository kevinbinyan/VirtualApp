import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BranchAccount {

    public static void main(String[] args) {
        // BufferedReader是可以按行读取文件
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(args[0]);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            ArrayList<String>[] arrays = new ArrayList[5];
            for (int i = 0; i < arrays.length; i++) {
                arrays[i] = new ArrayList();
            }

            StringBuffer[] sbs = new StringBuffer[5];
            for (int i = 0; i < arrays.length; i++) {
                sbs[i] = new StringBuffer();
            }

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                String[] params = str.trim().split("----");
                if (params != null && params.length == 4) {

                    if (!arrays[0].contains(params[2])) {
                        arrays[0].add(params[2]);
                        sbs[0].append(str + "\r\n");
                        continue;
                    }

                    if (!arrays[1].contains(params[2])) {
                        arrays[1].add(params[2]);
                        sbs[1].append(str + "\r\n");
                        continue;
                    }

                    if (!arrays[2].contains(params[2])) {
                        arrays[2].add(params[2]);
                        sbs[2].append(str + "\r\n");
                        continue;
                    }

                    if (!arrays[3].contains(params[2])) {
                        arrays[3].add(params[2]);
                        sbs[3].append(str + "\r\n");
                        continue;
                    }

                    if (!arrays[4].contains(params[2])) {
                        arrays[4].add(params[2]);
                        sbs[4].append(str + "\r\n");
                        continue;
                    }

                }
            }

            // close
            inputStream.close();
            bufferedReader.close();

            writeToFile(sbs, args[1]);
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
            FileWriter fw = new FileWriter((string.endsWith("\\") ? string : string +File.separator) + "account_" + (i + 1) + ".txt");
            fw.write(sbs[i].toString());
            fw.close();
        }

    }

}
