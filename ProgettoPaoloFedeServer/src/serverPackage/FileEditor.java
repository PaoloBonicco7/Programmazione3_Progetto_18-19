package serverPackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import comunication.Email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
    public static ArrayList<Email> loadFromJson() throws FileNotFoundException {
        Gson gson = new Gson();

        BufferedReader br = new BufferedReader(new FileReader("file.json"));

        Type type = new TypeToken<ArrayList<Email>>(){}.getType();

        ArrayList<Email> list = gson.fromJson(br, type);

        return list;
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println(loadFromJson().toString());
    }

}
