package pubgm.loader.Component;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class SharedPreferencesManager {
    private Context context;
    private static final String PREFS_NAME = "MyPrefs";

    public SharedPreferencesManager(Context context) {
        this.context = context;
    }

    public void exportToJSON(File directory, String fileName) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        JSONObject jsonObject = new JSONObject(allEntries);

        File file = new File(directory, fileName);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonObject.toString());
        }
    }

    public void importFromJSON(File directory, String fileName) throws IOException, JSONException {
        File file = new File(directory, fileName);
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try (FileReader fileReader = new FileReader(file)) {
            StringBuilder json = new StringBuilder();
            int c;
            while ((c = fileReader.read()) != -1) {
                json.append((char) c);
            }
            JSONObject jsonObject = new JSONObject(json.toString());

            for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                String key = it.next();
                Object value = jsonObject.get(key);
                if (value instanceof String) {
                    editor.putString(key, (String) value);
                } else if (value instanceof Integer) {
                    editor.putInt(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    editor.putBoolean(key, (Boolean) value);
                } else if (value instanceof Float) {
                    editor.putFloat(key, (Float) value);
                } else if (value instanceof Long) {
                    editor.putLong(key, (Long) value);
                }
            }
            editor.apply();
        }
    }

    public void reset() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
