package org.hse.lab2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private final static String filePath = "org.hse.lab2.file";
    private final static String imageKey = "picture";
    private final static String nameKey = "name";
    private final SharedPreferences sharedPref;

    public PreferenceManager(Context context) {
        sharedPref = context.getSharedPreferences(filePath, Context.MODE_PRIVATE);
    }

    private void saveValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getValue(String key, String defaultValue) {
        return sharedPref.getString(key, defaultValue);
    }

    public void saveName(String name){
        saveValue(nameKey, name);
    }
    public String getName(){
        return getValue(nameKey, "");
    }
    public void savePicture(String uri){
        saveValue(imageKey, uri);
    }
    public String getPicture(){
        return getValue(imageKey, null);
    }
}
