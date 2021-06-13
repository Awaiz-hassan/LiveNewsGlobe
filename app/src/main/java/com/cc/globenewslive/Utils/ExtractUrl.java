package com.cc.globenewslive.Utils;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.cc.globenewslive.Adapters.CityAdapter;
import com.cc.globenewslive.Adapters.Filter;
import com.cc.globenewslive.Adapters.StatesAdapter;
import com.cc.globenewslive.ApiClient.ApiClient;
import com.cc.globenewslive.Fragments.Cities;
import com.cc.globenewslive.Fragments.Favourites;
import com.cc.globenewslive.Fragments.Home;
import com.cc.globenewslive.Fragments.OnClickGO;
import com.cc.globenewslive.Fragments.SearchResult;
import com.cc.globenewslive.Fragments.State;
import com.cc.globenewslive.Fragments.filter;
import com.cc.globenewslive.MainActivity;
import com.cc.globenewslive.Model.LoginUser;
import com.cc.globenewslive.Model.RegisterUser;
import com.cc.globenewslive.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExtractUrl {
    private SharedPreference sharedPreference;

    public static List<String> extractURL(
            String str)
    {

        List<String> list
                = new ArrayList<>();


        String regex
                = "\\b((?:https?|ftp|file):"
                + "//[-a-zA-Z0-9+&@#/%?="
                + "~_|!:, .;]*[-a-zA-Z0-9+"
                + "&@#/%=~_|])";


        Pattern p = Pattern.compile(
                regex,
                Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(str);


        while (m.find()) {


            list.add(str.substring(
                    m.start(0), m.end(0)));
        }

        if (list.size() == 0) {
            return null;
        }

        return list;

    }


    public static void LoginDialog(Context context, NavigationView navigationView){
        LinearLayout signupLayout,loginLayout;
        TextView loginNav,SignUpnav;
        Button SIGNUP,LOGIN;
        TextInputEditText username,emaillogin,passwordlogin,emailSignup,passwordSignup;
        final Boolean[] Signupvalidated = new Boolean[1];
        ImageView topbar1,topbar2;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.login_signup_alert_label, null);
        loginNav=dialogView.findViewById(R.id.loginNavutton);
        SignUpnav=dialogView.findViewById(R.id.SignUpNavButton);
        signupLayout=dialogView.findViewById(R.id.SignupLayout);
        loginLayout=dialogView.findViewById(R.id.LoginLayout);
        topbar1=dialogView.findViewById(R.id.topbarLogin);
        topbar2=dialogView.findViewById(R.id.topbarSignUp);
        SIGNUP=dialogView.findViewById(R.id.signupbutton);
        LOGIN=dialogView.findViewById(R.id.loginbutton);
        username=dialogView.findViewById(R.id.suername);
        emailSignup=dialogView.findViewById(R.id.signupemail);
        passwordSignup=dialogView.findViewById(R.id.passwordsignup);
        emaillogin=dialogView.findViewById(R.id.usernameforlogin);
        passwordlogin=dialogView.findViewById(R.id.passwordlogin1);
        dialogBuilder.setView(dialogView);
        AlertDialog alert11 = dialogBuilder.create();
        alert11.show();
        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreference sharedPreference=new SharedPreference(context);

                String getEmail=emaillogin.getText().toString();
                String getPassword=passwordlogin.getText().toString();

                if(getEmail.isEmpty())
                {
                    emaillogin.setError("Email cannot be Empty");
                    return;}
                if(getPassword.isEmpty()){
                    passwordlogin.setError("Password cannot be Empty");
                    return;}
                if(!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()){emaillogin.setError("Invalid Email");
                return;
                }
                SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();

                Login(getEmail,getPassword,context,alert11,sharedPreference,navigationView,pDialog);}

        });

        loginNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topbar2.setVisibility(View.GONE);
                signupLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                topbar1.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_left);
                topbar1.startAnimation(animation);

            }
        });
        SignUpnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topbar1.setVisibility(View.GONE);
                loginLayout.setVisibility(View.GONE);
                signupLayout.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.slideright);
                topbar2.setVisibility(View.VISIBLE);
                topbar2.startAnimation(animation);


            }
        });

        SIGNUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getusername=username.getText().toString();
                if(getusername.isEmpty()){
                    username.setError("Username cannot be empty");
                    username.requestFocus();
                    return;}
                String email=emailSignup.getText().toString();
                if(email.isEmpty()){emailSignup.setError("Email cannot be empty");
                emailSignup.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailSignup.setError("Invalid Email");
                    return;}

                String getpassword=passwordSignup.getText().toString();
                if(getpassword.isEmpty()){passwordSignup.setError("Password Cannot be empty");
                    return;
                }
                 if(getpassword.length()<5){passwordSignup.setError("Password length should be greater than 5");
                    return;}


                    SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Loading");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    Register(getusername,email,getpassword,context,alert11,signupLayout,loginLayout,topbar1,topbar2,pDialog);


            }
        });

    }
    private static void Login(String Email, String Password, Context context, AlertDialog alertDialog,SharedPreference sharedPreference,NavigationView navigationView,SweetAlertDialog p){
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://livenewsglobe.com/wp-json/custom-plugin/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiClient apiClient=retrofit.create(ApiClient.class);
        Call<LoginUser> loginUserCall;
        loginUserCall=apiClient.loginUSer(Email,Password);
        loginUserCall.enqueue(new Callback<LoginUser>() {
            @Override
            public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                if(response.isSuccessful()){
                    LoginUser loginUser=response.body();
                    alertDialog.dismiss();
                    assert response.body() != null;
                    sharedPreference.SaveUser(response.body());
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.signoutuser).setVisible(true);
                    nav_Menu.findItem(R.id.LoginMenu).setVisible(false);
                    sharedPreference.removeAllLists();
                    currentfrag(context);
                    p.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Welcome!")
                            .setContentText(loginUser.getData().getDisplayName())
                            .show();
                    MainActivity.navUsername.setText(sharedPreference.getUserName());
                }
                else {
                    p.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("Invalid Username/Password")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {
                p.dismiss();
                if (t instanceof IOException) {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("Network Error")
                            .show();
                }
            }
        });
    }

    private static void Register(String username, String email, String password, Context context, AlertDialog alertDialog,LinearLayout signupLayout,LinearLayout loginLayout,ImageView topbar1,ImageView topbar2,SweetAlertDialog sweetAlertDialog) {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://livenewsglobe.com/wp-json/wp/v2/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient=retrofit.create(ApiClient.class);
        Call<RegisterUser> regUser;
        regUser=apiClient.registerUser(username,email,password);
        regUser.enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                if(response.isSuccessful()){
                    if(response.code()==200){
                        topbar2.setVisibility(View.GONE);
                        signupLayout.setVisibility(View.GONE);
                        loginLayout.setVisibility(View.VISIBLE);
                        topbar1.setVisibility(View.VISIBLE);
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_left);
                        topbar1.startAnimation(animation);
                        sweetAlertDialog.dismiss();
                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Registration Successful")
                                .setContentText("Please Login to continue")
                                .show();
                    }

                }
                else {
                    if(response.code()==400){
                        sweetAlertDialog.dismiss();
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText("User Already Exists")
                                .show();
                    }
                }

            }

            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {
                if (t instanceof IOException) {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("Network Error")
                            .show();
                }

            }
        });
    }


    static void currentfrag(Context context){
        if(MainActivity.currentFragment!=null){
            if(MainActivity.currentFragment.equals("home")){ loadfragment(new Home(),context); }
            else if(MainActivity.currentFragment.equals("State")){loadfragment(new State(),context);}
            else if(MainActivity.currentFragment.equals("Cities")){loadfragment(new Cities(),context);}
            else if(MainActivity.currentFragment.equals("fav")){loadfragment(new Favourites(),context);}
            else if(MainActivity.currentFragment.equals("Search")){loadfragment(new SearchResult(),context);}
            else if(MainActivity.currentFragment.equals("Filter")){loadfragment(filter.newInstance(MainActivity.citiesList,MainActivity.layoutType,"city"),context);}
            else if(MainActivity.currentFragment.equals("FilterChannel")){
                Fragment fragment1=
                        filter.newInstanceChannel(MainActivity.channellistArray,MainActivity.layoutType,"channel");
                ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();
            }
            else if(MainActivity.currentFragment.equals("onclikfiltercity")){
                if(MainActivity.layoutType[0]){
                Fragment fragment1= OnClickGO.CityInstance("list","channel", CityAdapter.currentcityid);
                ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();}
                else if(MainActivity.layoutType[1]){
                    Fragment fragment1= OnClickGO.CityInstance("grid","channel", CityAdapter.currentcityid);
                    ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();}
            }
            else if(MainActivity.currentFragment.equals("clickfilterstates")){
                if(MainActivity.layoutType[0]){
                Fragment fragment1= OnClickGO.StateInstance("list","city", StatesAdapter.currentcityname);
                ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();}
                if(MainActivity.layoutType[1]){
                    Fragment fragment1= OnClickGO.StateInstance("grid","city", StatesAdapter.currentcityname);
                    ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();}
            }
            else if(MainActivity.currentFragment.equals("onclikfilterfiltercity")){
                if(MainActivity.layoutType[0]){
                Fragment fragment1= OnClickGO.CityInstance("list","channel", Filter.currentcityid);
                ((MainActivity)context). getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();}
                else if(MainActivity.layoutType[0]){
                    Fragment fragment1= OnClickGO.CityInstance("grid","channel", Filter.currentcityid);
                    ((MainActivity)context). getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();}
            }
            else if(MainActivity.currentFragment.equals("webview")){
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.home);
                loadfragment(new Home(),context);
            }

        }
    }

    static void loadfragment(Fragment fragment, Context context){
        MainActivity.mainRelativelayout.setVisibility(View.VISIBLE);
        MainActivity.webviewFragment.setVisibility(View.GONE);

        if(MainActivity.currentFragment!="Filter"&&MainActivity.currentFragment!="FilterChannel") {
            Bundle bundle = new Bundle();
            bundle.putBooleanArray("keyList", MainActivity.layoutType);
            fragment.setArguments(bundle);
            if (MainActivity.currentFragment.equals("Search")) bundle.putString("key", MainActivity.search);
        }
        ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment).commit();
    }
    public static boolean neworkAvailibility(Context context) {
        boolean isConnected = false;
        try{
            ConnectivityManager connService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo network = connService.getActiveNetworkInfo();
            if(network != null) {
                NetworkInfo.State state = network.getState();

                if(network.isConnected()){
                    isConnected = true;
                }
            }else{
                isConnected = false;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return isConnected;

    }

    public static boolean hasInternetConnected() {

        try {
            java.net.URL url = new URL("https://livenewsglobe.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Test");
            connection.setRequestProperty("Connection", "close");
            connection.setConnectTimeout(5000);
            connection.connect();
            Log.d("classTag", "hasServerConnected: ${(connection.responseCode == 200)}"+connection.getResponseCode());
            return (connection.getResponseCode()==200);
        } catch (IOException e) {
            Log.d("sada", "hasInternetConnected: false");
        }
        return false;
    }
    public static void CheckConnection(Context context, LottieAnimationView lottieAnimationView){
        if(neworkAvailibility(context)){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!ExtractUrl.hasInternetConnected()){
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lottieAnimationView.setVisibility(View.GONE);
                            try {
                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error")
                                        .setContentText("Error Connecting:https://livenewsglobe.com/")
                                        .show();
                            }
                            catch (Exception e){}
                        }
                    },1);
                }
            }
        }).start();}
        else {
            lottieAnimationView.setVisibility(View.GONE);
            try {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Network Error")
                        .show();}
            catch (Exception e){}
        }
    }
    public static void CheckConnection(Context context){
        if(neworkAvailibility(context)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(!ExtractUrl.hasInternetConnected()){
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error")
                                        .setContentText("Error Connecting:https://livenewsglobe.com/")
                                        .show();
                            }
                             catch (Exception e){}
                            }
                        },1);
                    }
                }
            }).start();}
        else {
            try {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Error")
                    .setContentText("Network Error")
                    .show();}
            catch (Exception e){}
        }
    }


}
