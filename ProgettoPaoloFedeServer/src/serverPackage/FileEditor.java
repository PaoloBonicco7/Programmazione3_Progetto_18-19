package serverPackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
<<<<<<< HEAD
=======
import comunication.Email;
>>>>>>> refs/remotes/origin/master

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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

    public static void saveToJson(Map<Integer, Email> list) throws IOException {
        Gson gson;
        newFile();
        try (Writer writer = new FileWriter("file.json")) {
            gson = new GsonBuilder().create();
            gson.toJson(list, writer);
        }
    }

    /**
     * @return un arraylist che estrae i dati da un file.json
     * @throws FileNotFoundException
     */
    public static HashMap<Integer, Email> loadFromJson() throws FileNotFoundException {
        Gson gson = new Gson();

        BufferedReader br = new BufferedReader(new FileReader("file.json"));

        Type type = new TypeToken<HashMap<Integer, Email>>() {
        }.getType();

        return gson.fromJson(br, type);
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println(loadFromJson().toString());
    }

}
