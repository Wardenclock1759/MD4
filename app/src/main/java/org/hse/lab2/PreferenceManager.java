package org.hse.lab2;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private final static String PREFERENCE_FILE = "org.hse.lab2.file";
    private final static String PICTURE_KEY = "picture";
    private final static String PREFERENCE_NAME = "name";
    private final SharedPreferences sharedPref;

    public PreferenceManager(Context context) {
        sharedPref = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
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
        saveValue(PREFERENCE_NAME, name);
    }
    public String getName(){
        return getValue(PREFERENCE_NAME, "");
    }
    public void savePicture(String uri){
        saveValue(PICTURE_KEY, uri);
    }
    public String getPicture(){
        return getValue(PICTURE_KEY, null);
    }
}
