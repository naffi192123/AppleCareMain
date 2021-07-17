package com.naffi.applecare;


import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String  SESSION_KEY = "session_user";

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void saveSession(User user){
        // save session of the user whenever user is loggedin
        int id = user.getId();
        editor.putInt(SESSION_KEY,id).commit();
    }
    public int getSession(){
        // return id of the user id whose session is saved
        return sharedPreferences.getInt(SESSION_KEY,-1);
    }
    public void removeSession(){
        // it will remove the session for a user
        editor.putInt(SESSION_KEY,-1).commit();
    }
}
