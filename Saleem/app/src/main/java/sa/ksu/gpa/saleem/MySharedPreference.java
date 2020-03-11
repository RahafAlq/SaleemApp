package sa.ksu.gpa.saleem;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

public class MySharedPreference {

    private static SharedPreferences prf;

    private MySharedPreference() {
    }

    public static SharedPreferences getInstance(Context context) {
        if (prf == null) {
            prf = context.getSharedPreferences("user_details", Context.MODE_PRIVATE);
        }

        return prf;
    }//end getInstance

    public static void clearData(Context context) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.clear();
        editor.commit();
    }

    public static void clearValue(Context context, String key) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.remove(key);
        editor.commit();
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putString(key, value);
        editor.commit();
    }



    public static void putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putFloat(Context context, String key, float value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String valueDefault){
        return getInstance(context).getString(key, valueDefault);
    }

    public static int getInt(Context context, String key, int valueDefault){
        return getInstance(context).getInt(key, valueDefault);
    }

    public static boolean getBoolean(Context context, String key, boolean valueDefault){
        return getInstance(context).getBoolean(key, valueDefault);
    }

    public static float getFloat(Context context, String key, float valueDefault){
        return getInstance(context).getFloat(key, valueDefault);
    }

}

