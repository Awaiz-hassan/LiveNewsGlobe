package com.cc.globenewslive.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;

import com.cc.globenewslive.MainActivity;
import com.cc.globenewslive.Model.City;
import com.cc.globenewslive.Model.GetFav;
import com.cc.globenewslive.Model.LoginUser;
import com.cc.globenewslive.Model.NewsPost;
import com.cc.globenewslive.Model.States;
import com.cc.globenewslive.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SharedPreference {
    public static final String SHARED_PREF_NAME="LiveNewsGlobe";
    private SharedPreferences sharedPreferences;
    Context context;
    private SharedPreferences.Editor editor;


    public SharedPreference(Context context) {
        this.context = context;
    }
    public void SaveUser(LoginUser user){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putInt("UserId",user.getId());
        editor.putString("Email",user.getData().getUserEmail());
        editor.putString("UserName",user.getData().getDisplayName());
        editor.putBoolean("Logged",true);
        editor.apply();
    }
    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key, json);
    }

    private void set(String key, String value) {
        if (sharedPreferences != null) {
            sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
            editor=sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }
    public List<NewsPost> getNews(String key) {
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            List<NewsPost> companyList;
            String string = sharedPreferences.getString(key, null);
            Type type = new TypeToken<List<NewsPost>>() {
            }.getType();
            companyList = gson.fromJson(string, type);
            return companyList;
        }
        return null;
    }


    // FAV save

    public List<GetFav> getFavList(String key) {
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            List<GetFav> companyList;
            String string = sharedPreferences.getString(key, null);
            Type type = new TypeToken<List<GetFav>>() {
            }.getType();
            companyList = gson.fromJson(string, type);
            return companyList;
        }
        return null;
    }
    public List<City> getCities(String key) {
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            List<City> companyList;

            String string = sharedPreferences.getString(key, null);
            Type type = new TypeToken<List<City>>() {
            }.getType();
            companyList = gson.fromJson(string, type);
            return companyList;
        }
        return null;
    }

    public List<States> getStates(String key) {
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            List<States> companyList;

            String string = sharedPreferences.getString(key, null);
            Type type = new TypeToken<List<States>>() {
            }.getType();
            companyList = gson.fromJson(string, type);
            return companyList;
        }
        return null;
    }

    public boolean LoggedIN(){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean("Logged",false);
    }
    public int getuserid(){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt("UserId",-1);
    }
    public String getUserEmail(){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("Email",null);
    }
    public String getUserName(){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("UserName",null);
    }
    public void LOGOUT(NavigationView navigationView){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.signoutuser).setVisible(false);
        navigationView.getCheckedItem();
        ExtractUrl.currentfrag(context);
        MainActivity.navUsername.setText("Username");
        editor.clear();
        editor.apply();
    }
    public void removeChannelsAndFav(){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if(sharedPreferences!=null){
        editor=sharedPreferences.edit();
        editor.remove("CHANNELS_KEY");
        editor.remove("FAV_KEY");
        editor.apply();
        }
    }
    public void removeAllLists(){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if(sharedPreferences!=null){
            editor=sharedPreferences.edit();
            editor.remove("CHANNELS_KEY");
            editor.remove("FAV_KEY");
            editor.remove("CITIES_KEY");
            editor.remove("STATES_KEY");
            editor.apply();
        }
    }
}
