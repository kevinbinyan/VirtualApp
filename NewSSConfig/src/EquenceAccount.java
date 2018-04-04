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
            inputStream = new FileInputStream("D:\\kevin001.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            ArrayList<String> array = new ArrayList();

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                array.add(str.trim());
            }

            array.sort(new Comparator() {

                @Override
                public int compare(Object arg0, Object arg1) {
                    String args0 = (String) arg0;
                    String args1 = (String) arg1;
//                    String[] args0_p = args0.split("\\|");
//                    String[] args1_p = args1.split("\\|");
//                    String[] args0_p = args0.split("----");
//                    String[] args1_p = args1.split("----");
                    String[] args0_p = args0.split(",");
                    String[] args1_p = args1.split(",");
                    if (args0_p.length > 1) {
                        return (args0_p[2].compareTo(args1_p[2]));
                    } else {
                        return (args0.compareTo(args1_p[2]));
                    }
                }

            });
            inputStream.close();
            bufferedReader.close();

             writeToFile(array);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static void writeToFile(ArrayList<String> array) throws IOException {
        FileWriter fw = new FileWriter("D:\\newAccount\\sequence.txt");        for(String str: array){
            fw.write(("login;" + str + "\r\n").replace("----", ","));
        }
        fw.close();

    }
}
