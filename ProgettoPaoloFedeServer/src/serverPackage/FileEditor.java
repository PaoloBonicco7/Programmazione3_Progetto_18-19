package serverPackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import comunication.Email;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FileEditor {

    /**
     * Crea o controlla l'esistenza di un file json
     */
    public static void newFile() {
        String path = "file.json";
        try {
            File file = new File(path);

            if (file.exists()) {
                System.out.println("Il file" + path + " esiste");
            } else if (file.createNewFile()) {
                System.out.println("Il file" + path + " è stato creato");
            } else {
                System.out.println("Il file" + path + " non puo' essere creato");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     public static void saveToJson(Email email){
        Gson gson;
        newFile();
        try (Writer writer = new FileWriter("file.json",true)) {
            gson = new GsonBuilder().create();
            gson.toJson(email, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return un arraylist che estrae i dati da un file.json
     * @throws FileNotFoundException
     */
    public static Email loadFromJson() throws FileNotFoundException {
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader("file.json"));
        Type type = new TypeToken<ArrayList<Email>>(){}.getType();

        Email l = gson.fromJson(br, type);

        return l;
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println(loadFromJson().toString());
    }

}
