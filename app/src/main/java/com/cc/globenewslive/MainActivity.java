package com.cc.globenewslive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cc.globenewslive.Adapters.CityAdapter;
import com.cc.globenewslive.Adapters.Filter;
import com.cc.globenewslive.Adapters.StatesAdapter;
import com.cc.globenewslive.ApiClient.ApiClient;
import com.cc.globenewslive.ApiClient.RetrofitClient;
import com.cc.globenewslive.Fragments.AboutUs;
import com.cc.globenewslive.Fragments.Cities;
import com.cc.globenewslive.Fragments.Favourites;
import com.cc.globenewslive.Fragments.Home;
import com.cc.globenewslive.Fragments.OnClickGO;
import com.cc.globenewslive.Fragments.Profile;
import com.cc.globenewslive.Fragments.SearchResult;
import com.cc.globenewslive.Fragments.State;
import com.cc.globenewslive.Fragments.WebViewNews;
import com.cc.globenewslive.Fragments.filter;
import com.cc.globenewslive.Model.City;
import com.cc.globenewslive.Model.NewsPost;
import com.cc.globenewslive.Model.States;
import com.cc.globenewslive.Utils.ExtractUrl;
import com.cc.globenewslive.Utils.SharedPreference;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    private static Retrofit retrofit1;
    public static String currentFragment="home";
    public static NavigationView navigationView;
    public static Fragment detachfrag;
    public static DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    public static BottomNavigationView bottomNavigationView;
    ImageButton searchButton,listButton,gridButton,buttonFilter,animate,clearText;
    public static Retrofit retrofit;
    TextInputEditText input;
    Spinner state,city,channel;
    public static RelativeLayout mainRelativelayout,webviewFragment;
    public static String search;
    int cityid=0;
    String [] StateAdapterList;
    String[] CityAdapterList;
    LinearLayout searchBar,dropdownList,statel,cityl,channell;
    RelativeLayout fragmentlist;
    ArrayAdapter cityAdapter;
    private List<City> remove_Channel_city=new ArrayList<>();
    private List<NewsPost> channelsList=new ArrayList<>();
    private String[] channelname;
    public static ArrayList<NewsPost> channellistArray;
    private String[] majorChannels;
    private boolean[] clickedprev={true,false,false};
    public static boolean [] layoutType={true,false};
    private static boolean [] fromClicked=new boolean[3];
    boolean filterClicked=false,filternotempty=false;
    public List<States> stateList =new ArrayList<>();
    public List<City> cityList=new ArrayList<>();
    public static ArrayList<City> citiesList=new ArrayList<>();
    private List<NewsPost> listChannels=new ArrayList<>();
    private ArrayAdapter channelAdapters;
    private boolean selectedfromlist,selectedfromlistChannel;
    public static View headerView;
    public static TextView navUsername;
    SharedPreference sharedPreference=new SharedPreference(MainActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton toggleButton;
        mainRelativelayout=findViewById(R.id.mainLayout);
        webviewFragment=findViewById(R.id.WebvieFragment);
        drawerLayout = findViewById(R.id.drawerLayout1);
        navigationView=findViewById(R.id.navmenu);
        Menu nav_Menu = navigationView.getMenu();
        toggleButton = findViewById(R.id.opendrawer);
        bottomNavigationView=findViewById(R.id.bottomnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(allItems);
        searchButton=findViewById(R.id.SearchBtn);
        input=findViewById(R.id.Searchtext);
        listButton=findViewById(R.id.list_layout);
        gridButton=findViewById(R.id.grid_layout);
        searchBar=findViewById(R.id.linearLayout2);
        dropdownList=findViewById(R.id.filterlaytout);
        buttonFilter=findViewById(R.id.filterButton);
        fragmentlist=findViewById(R.id.relativeLayout);
        state=findViewById(R.id.stateSpinner);
        city=findViewById(R.id.citySpinner);
        channel=findViewById(R.id.channelSpinner);
        animate=findViewById(R.id.animatebuton);
        statel=findViewById(R.id.Lstate);
        cityl=findViewById(R.id.Lcity);
        channell=findViewById(R.id.LChannel);
        state.setPrompt("Select State");
        city.setPrompt("Select Cities");
        channel.setPrompt("Select Channel");
        clearText=findViewById(R.id.cleartext);
         headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.headerUsername);

        SharedPreference sharedPreference=new SharedPreference(MainActivity.this);
        loadfragment(new Home());


        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(sharedPreference.LoggedIN()){
            nav_Menu.findItem(R.id.signoutuser).setVisible(true);
            nav_Menu.findItem(R.id.LoginMenu).setVisible(false);
            navUsername.setText(sharedPreference.getUserName());


        }
        else {
            navUsername.setText("UserName");
            nav_Menu.findItem(R.id.signoutuser).setVisible(false);
            nav_Menu.findItem(R.id.LoginMenu).setVisible(true);
            invalidateOptionsMenu();

        }


        channel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ExtractUrl.CheckConnection(MainActivity.this);
                String majChannelName=channel.getItemAtPosition(position).toString();
                int majChannelId=getNetworkId(majChannelName);
                if(majChannelName!="Channels"){
                    channell.setBackground(getResources().getDrawable(R.drawable.spinnerbackground));
                ApiClient apiClient = getRetrofit().create(ApiClient.class);
                Call<List<NewsPost>> callMajChannels;
                callMajChannels = apiClient.getFilterChannels(cityid,majChannelId);
                callMajChannels.enqueue(new Callback<List<NewsPost>>() {
                    @Override
                    public void onResponse(Call<List<NewsPost>> call, Response<List<NewsPost>> response) {
                        if (response.isSuccessful()) {
                            if (listChannels.isEmpty()) {
                                listChannels.clear();
                            }
                            listChannels = response.body();
                            channellistArray = (ArrayList<NewsPost>) listChannels;
                            currentFragment = "FilterChannel";

                            Fragment fragment1= filter.newInstanceChannel(channellistArray,layoutType,"channel");
                            getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();
                        }
                        else {
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Warning")
                                    .setContentText("No Channels Found")
                                    .show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<NewsPost>> call, Throwable t) {

                    }
                });}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ExtractUrl.CheckConnection(MainActivity.this);
                String cityname=city.getItemAtPosition(position).toString();
                Log.d("CITY", "onItemSelected: "+cityname);
                if(cityname!="States"&& selectedfromlist) {
                    cityl.setBackground(getResources().getDrawable(R.drawable.spinnerbackground));
                    cityid=cityList.get(position-1).getTermId();
                    ApiClient apiClient = getRetrofit().create(ApiClient.class);
                    Call<List<NewsPost>> callChannels;
                    callChannels = apiClient.getChannels(cityid);
                    callChannels.enqueue(new Callback<List<NewsPost>>() {
                        @Override
                        public void onResponse(Call<List<NewsPost>> call, Response<List<NewsPost>> response) {
                            if (response.isSuccessful()) {
                                if (listChannels.isEmpty()) {
                                    listChannels.clear();
                                }
                                listChannels = response.body();
                                channelname = new String[listChannels.size()+1];
                                channelname[0]="Channel";
                                int channelno = 1;
                                for (NewsPost newsPost : listChannels) {
                                    channelname[channelno] = newsPost.getTitle();
                                    channelno++;
                                }
                                channellistArray = (ArrayList<NewsPost>) listChannels;
                                currentFragment = "FilterChannel";

                                Fragment fragment1= filter.newInstanceChannel(channellistArray,layoutType,"channel");
                                getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();
                                majorChannels= new String[]{"Channels", "ABC", "CBS", "FOX", "Independent", "NBC"};
                                channelAdapters = new ArrayAdapter(MainActivity.this, R.layout.selectable_list_item, majorChannels);
                                channelAdapters.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                                channel.setAdapter(channelAdapters);


                            }
                        }

                        @Override
                        public void onFailure(Call<List<NewsPost>> call, Throwable t) {

                        }
                    });
                }
                selectedfromlist=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ExtractUrl.CheckConnection(MainActivity.this);
               String StateName=state.getItemAtPosition(position).toString();
               if(StateName!="States") {
                   buttonFilter.setBackground(getResources().getDrawable(R.drawable.filternotnull));
                   filternotempty=true;
                   Log.d("sda", "onItemSelected: "+buttonFilter.getBackground());
                   statel.setBackground(getResources().getDrawable(R.drawable.spinnerbackground));
                   ApiClient apiClient = getRetrofitClient().create(ApiClient.class);
                   Call<List<City>> callCity;
                   callCity = apiClient.getFilterCity(StateName);
                   callCity.enqueue(new Callback<List<City>>() {
                       @Override
                       public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                           if (response.isSuccessful()) {
                               if (cityList.isEmpty()) {
                                   cityList.clear();
                               }
                               cityList = response.body();
                               for(City city1: cityList){
                                   if(city1.getName().equals("Fox") ||city1.getName().equals("ABC")||city1.getName().equals("NBC")|| city1.getName().equals("CBS")||city1.getName().equals("Featured")||city1.getName().equals(null))
                                       remove_Channel_city.add(city1);
                               }

                               for(int i=0;i<remove_Channel_city.size();i++){
                                   if(cityList.contains(remove_Channel_city.get(i))){
                                       cityList.remove(remove_Channel_city.get(i));
                                   }
                               }
                               Set<City> s= new HashSet<City>();
                               s.addAll(cityList);
                               cityList = new ArrayList<City>();
                               cityList.addAll(s);


                               citiesList= (ArrayList<City>) cityList;
                               currentFragment="Filter";

                               loadfragment(filter.newInstance(citiesList,layoutType,"city"));
                               CityAdapterList = new String[cityList.size()+1];
                               CityAdapterList[0]="Cities";
                               selectedfromlist=false;

                               for (int i = 0; i < cityList.size(); i++) {
                                   CityAdapterList[i+1] = cityList.get(i).getName();
                               }
                           }
                           cityAdapter = new ArrayAdapter(MainActivity.this, R.layout.selectable_list_item, CityAdapterList);
                           cityAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                           city.setAdapter(cityAdapter);
                       }

                       @Override
                       public void onFailure(Call<List<City>> call, Throwable t) {

                       }
                   });
               }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!filternotempty){
                if(!filterClicked) {
                    if(layoutType[1]){
                        Animation fadeIn = new TranslateAnimation(-160,-190,0,0);
                        fadeIn.setInterpolator(new DecelerateInterpolator());
                        fadeIn.setDuration(200);
                        animate.startAnimation(fadeIn);
                    }
                    if(layoutType[0]){
                        Animation fadeIn = new TranslateAnimation(0,-190,0,0);
                        fadeIn.setInterpolator(new DecelerateInterpolator());
                        fadeIn.setDuration(200);
                        animate.startAnimation(fadeIn);
                    }
                    dropdownList.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down);
                    Animation fadeIn = new AlphaAnimation(0, 1);
                    fadeIn.setInterpolator(new DecelerateInterpolator());
                    fadeIn.setDuration(500);
                    dropdownList.startAnimation(fadeIn);
                    state.startAnimation(animation);
                    city.startAnimation(animation);
                    channel.startAnimation(animation);

/*                    currentFragment="Filter";
                    loadfragment(filter.newInstance(citiesList,layoutType,"city"));*/
                    buttonFilter.setBackground(getResources().getDrawable(R.drawable.backon));
                    buttonFilter.setImageResource(R.drawable.filtericonblank);
                    filterClicked = true;
                    LoadJson();

                }
                else if(filterClicked) {
                    clearFilter();
                    currentFragment="home";
                    MainActivity.mainRelativelayout.setVisibility(View.VISIBLE);
                    MainActivity.webviewFragment.setVisibility(View.GONE);
                    loadfragment(new Home());
                    bottomNavigationView.setSelectedItemId(R.id.home);
                }

            }
                else if(filternotempty){

                    filterClicked = true;
                    cityl.setBackground(getResources().getDrawable(R.drawable.list_item_background));
                    statel.setBackground(getResources().getDrawable(R.drawable.list_item_background));
                    channell.setBackground(getResources().getDrawable(R.drawable.list_item_background));
                    filternotempty=false;
                    buttonFilter.setBackground(getResources().getDrawable(R.drawable.backon));
                    buttonFilter.setImageResource(R.drawable.filtericonblank);
                    city.setAdapter(null);
                    channel.setAdapter(null);
                    state.setSelection(0);

                }
            }

        });
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listButton.setBackground(getResources().getDrawable(R.drawable.backon));
                listButton.setImageResource(R.drawable.listiconblank);
                gridButton.setBackground(getResources().getDrawable(R.drawable.grid));
                if(layoutType[1]){
                Animation fadeIn = new TranslateAnimation(-90,0,0,0);
                fadeIn.setInterpolator(new DecelerateInterpolator());
                fadeIn.setDuration(200);
                animate.startAnimation(fadeIn);}

                layoutType= new boolean[]{true, false};
                if(currentFragment!=null){
                    if(currentFragment.equals("home")){
                        loadfragment(new Home());
                    }
                    else if(currentFragment.equals("State")){loadfragment(new State());}
                    else if(currentFragment.equals("Cities")){loadfragment(new Cities());}
                    else if(currentFragment.equals("fav")){loadfragment(new Favourites());}
                    else if(currentFragment.equals("Search")){loadfragment(new SearchResult());}
                    else if(currentFragment.equals("Filter")){loadfragment(filter.newInstance(citiesList,layoutType,"city"));}
                    else if(currentFragment.equals("FilterChannel")){
                        Fragment fragment1=
                                filter.newInstanceChannel(channellistArray,layoutType,"channel");
                        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();
                    }
                    else if(currentFragment.equals("onclikfiltercity")){

                        Fragment fragment1= OnClickGO.CityInstance("list","channel", CityAdapter.currentcityid);
                        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();
                    }
                    else if(currentFragment.equals("clickfilterstates")){
                        Fragment fragment1= OnClickGO.StateInstance("list","city", StatesAdapter.currentcityname);
                        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();
                    }
                    else if(currentFragment.equals("onclikfilterfiltercity")){
                        Fragment fragment1= OnClickGO.CityInstance("list","channel", Filter.currentcityid);
                        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();
                    }
                }

            }
        });
        gridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutType[0]){
                Animation fadeIn = new TranslateAnimation(0,-80,0,0);
                fadeIn.setInterpolator(new DecelerateInterpolator());
                fadeIn.setDuration(200);
                animate.startAnimation(fadeIn);}
                layoutType= new boolean[]{false, true};
                listButton.setBackground(getResources().getDrawable(R.drawable.on));
                gridButton.setBackground(getResources().getDrawable(R.drawable.backon));
                gridButton.setImageResource(R.drawable.gridiconblank);
                if(currentFragment!=null){
                    if(currentFragment.equals("home")){ loadfragment(new Home()); }
                    else if(currentFragment.equals("State")){loadfragment(new State());}
                    else if(currentFragment.equals("Cities")){loadfragment(new Cities());}
                    else if(currentFragment.equals("fav")){loadfragment(new Favourites());}
                    else if(currentFragment.equals("Search")){loadfragment(new SearchResult());}
                    else if(currentFragment.equals("Filter")){loadfragment(filter.newInstance(citiesList,layoutType,"city"));}
                    else if(currentFragment.equals("FilterChannel")){
                        Fragment fragment1=
                                filter.newInstanceChannel(channellistArray,layoutType,"channel");
                        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();
                    }
                    else if(currentFragment.equals("onclikfiltercity")){

                        Fragment fragment1= OnClickGO.CityInstance("grid","channel", CityAdapter.currentcityid);
                        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();
                    }
                    else if(currentFragment.equals("clickfilterstates")){
                        Fragment fragment1= OnClickGO.StateInstance("grid","city", StatesAdapter.currentcityname);
                        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();
                    }
                    else if(currentFragment.equals("onclikfilterfiltercity")){
                        Fragment fragment1= OnClickGO.CityInstance("grid","channel", Filter.currentcityid);
                        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();
                    }

                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                search=input.getText().toString();
                SearchResult searchResult=new SearchResult();
                Bundle bundle=new Bundle();
                if(!search.isEmpty()){
                    bundle.putString("key",search);
                    bundle.putBooleanArray("keyList",layoutType);
                    searchResult.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, searchResult).commit();
                    currentFragment="Search";
                }
                if(search.isEmpty()){
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning")
                            .setContentText("Search box is empty!")
                            .show();
                }
            }
        });
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText("");
            }
        });
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    clearText.setVisibility(View.VISIBLE);

                }
                else {
                    clearText.setVisibility(View.GONE);
                }
                Log.d("TAG", "onTextChanged: "+s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                Log.d("TAG", "onKey: "+event.getKeyCode());
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    search=input.getText().toString();
                    SearchResult searchResult=new SearchResult();
                    Bundle bundle=new Bundle();
                    if(!search.isEmpty()){
                        bundle.putString("key",search);
                        bundle.putBooleanArray("keyList",layoutType);
                        searchResult.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, searchResult).commit();
                        currentFragment="Search";

                    }
                    if(search.isEmpty()){
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Warning")
                                .setContentText("Search box is empty!")
                                .show();
                    }


                    return true;
                }
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        clearFilter();
                        currentFragment="home";
                        MainActivity.mainRelativelayout.setVisibility(View.VISIBLE);
                        MainActivity.webviewFragment.setVisibility(View.GONE);
                        loadfragment(new Home());
                        drawerLayout.closeDrawer(GravityCompat.END);
                        bottomNavigationView.setSelectedItemId(R.id.home);

                        break;
                    case R.id.LoginMenu:
                        clearFilter();
                        ExtractUrl.LoginDialog(MainActivity.this,MainActivity.navigationView);
                        drawerLayout.closeDrawer(GravityCompat.END);
                        break;
                    case R.id.About:
                        clearFilter();
                        MainActivity.currentFragment="webview";
                        Fragment fragment2= new AboutUs();
                        getSupportFragmentManager().beginTransaction().replace(R.id.WebvieFragment, fragment2).commit();
                        MainActivity.mainRelativelayout.setVisibility(View.GONE);
                        MainActivity.webviewFragment.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawer(GravityCompat.END);
                        break;
                    case R.id.Profile:
                        clearFilter();
                        if(sharedPreference.LoggedIN()){
                        MainActivity.currentFragment="webview";
                        Fragment fragment1= new Profile();
                        getSupportFragmentManager().beginTransaction().replace(R.id.WebvieFragment, fragment1).commit();
                        MainActivity.mainRelativelayout.setVisibility(View.GONE);
                        MainActivity.webviewFragment.setVisibility(View.VISIBLE);}
                        else {
                            ExtractUrl.LoginDialog(MainActivity.this,navigationView);
                        }
                        drawerLayout.closeDrawer(GravityCompat.END);
                        break;
                    case R.id.Fav:
                        clearFilter();
                        bottomNavigationView.setSelectedItemId(R.id.fav);
                        MainActivity.mainRelativelayout.setVisibility(View.VISIBLE);
                        MainActivity.webviewFragment.setVisibility(View.GONE);
                        Fragment fragment= new Favourites();
                        Bundle bundle=new Bundle();
                        bundle.putBooleanArray("keyList",layoutType);
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout,fragment).commit();
                        currentFragment="fav";
                        drawerLayout.closeDrawer(GravityCompat.END);
                        break;

                    case R.id.signoutuser:
                        clearFilter();
                        onPrepareOptionsMenu(nav_Menu);
                        sharedPreference.LOGOUT(navigationView);
                        drawerLayout.closeDrawer(GravityCompat.END);
                        break;
                }
                return true;
            }
        });
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPrepareOptionsMenu(nav_Menu);
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

    }

    public List<States> getStateList() {
        return stateList;
    }

    public List<City> getCityList() {
        return cityList;
    }
    BottomNavigationView.OnNavigationItemSelectedListener allItems = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            Bundle bundle=null;
            switch (item.getItemId()) {

                case R.id.home:
                    clearFilter();
                    MainActivity.mainRelativelayout.setVisibility(View.VISIBLE);
                    MainActivity.webviewFragment.setVisibility(View.GONE);
                    fragment = new Home();
                    bundle=new Bundle();
                    bundle.putBooleanArray("keyList",layoutType);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment).commit();
                    currentFragment="home";
                    break;
                case R.id.states:
                    clearFilter();
                    MainActivity.mainRelativelayout.setVisibility(View.VISIBLE);
                    MainActivity.webviewFragment.setVisibility(View.GONE);
                    fragment = new State();
                    bundle=new Bundle();
                    bundle.putBooleanArray("keyList",layoutType);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout,fragment).commit();
                    currentFragment="State";
                    break;

                case R.id.cities:
                    clearFilter();
                    MainActivity.mainRelativelayout.setVisibility(View.VISIBLE);
                    MainActivity.webviewFragment.setVisibility(View.GONE);
                    fragment = new Cities();
                    bundle=new Bundle();
                    bundle.putBooleanArray("keyList",layoutType);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout,fragment).commit();
                    currentFragment="Cities";

                    break;
                case R.id.fav:
                    clearFilter();
                    MainActivity.mainRelativelayout.setVisibility(View.VISIBLE);
                    MainActivity.webviewFragment.setVisibility(View.GONE);
                     fragment= new Favourites();
                    bundle=new Bundle();
                    bundle.putBooleanArray("keyList",layoutType);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout,fragment).commit();
                    currentFragment="fav";

                    break;
            }
            return true;
        }
    };



    void loadfragment (Fragment fragment){
        MainActivity.mainRelativelayout.setVisibility(View.VISIBLE);
        MainActivity.webviewFragment.setVisibility(View.GONE);

        if(currentFragment!="Filter"&&currentFragment!="FilterChannel") {
            Bundle bundle = new Bundle();
            bundle.putBooleanArray("keyList", layoutType);
            fragment.setArguments(bundle);
            if (currentFragment.equals("Search")) bundle.putString("key", search);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment).commit();
    }
    public static Retrofit getRetrofit(){
        if(retrofit1==null){
            retrofit1=new Retrofit.Builder()
                    .baseUrl("https://www.livenewsglobe.com/wp-json/wp/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit1;
    }
    private void LoadJson() {
        ExtractUrl.CheckConnection(MainActivity.this);

        ApiClient apiClient = RetrofitClient.getRetrofitClient(MainActivity.this).create(ApiClient.class);

        //get States
        Call<List<States>> callStates;
        callStates=apiClient.getStates();

        callStates.enqueue(new Callback<List<States>>() {
            @Override
            public void onResponse(Call<List<States>> call, Response<List<States>> response) {

                if (response.isSuccessful()) {

                    if (stateList.isEmpty()) {
                        stateList.clear();
                    }
                    stateList = response.body();
                    StateAdapterList=new String[stateList.size()+1];
                    StateAdapterList[0]="States";
                    for(int i=0;i<stateList.size();i++){
                        StateAdapterList[i+1]=stateList.get(i).getName().trim();

                    }
                    ArrayAdapter aa = new ArrayAdapter(MainActivity.this, R.layout.selectable_list_item, StateAdapterList);
                    aa.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    state.setAdapter(aa);
                }

            }

            @Override
            public void onFailure(Call<List<States>> call, Throwable t) {


            }
        });
    }
    public static Retrofit getRetrofitClient(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl("https://livenewsglobe.com/wp-json/salman/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public int getNetworkId(String ChannelName){
        if(ChannelName.equals("ABC")) return 58;
        else if(ChannelName.equals("CBS")) return 62;
        else if(ChannelName.equals("FOX")) return 60;
        else if(ChannelName.equals("Independent")) return 70;
        else if(ChannelName.equals("NBC")) return 59;
        return 0;
    }
    public void clearFilter(){
        cityl.setBackground(getResources().getDrawable(R.drawable.list_item_background));
        statel.setBackground(getResources().getDrawable(R.drawable.list_item_background));
        channell.setBackground(getResources().getDrawable(R.drawable.list_item_background));
        dropdownList.setVisibility(View.GONE);
        filterClicked = false;
        buttonFilter.setBackground(getResources().getDrawable(R.drawable.filter));
        state.setAdapter(null);
        city.setAdapter(null);
        channel.setAdapter(null);
        cityList.clear();
        stateList.clear();
    }


    @Override
    public  boolean onPrepareOptionsMenu(Menu menu) {
        if(sharedPreference.LoggedIN()){
         menu.findItem(R.id.LoginMenu).setVisible(false);

            return super.onPrepareOptionsMenu(menu);
        }
        else{
            menu.findItem(R.id.LoginMenu).setVisible(true);
            return super.onPrepareOptionsMenu(menu);
        }

    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if(currentFragment.equals("webview")){
            MainActivity.mainRelativelayout.setVisibility(View.VISIBLE);
            MainActivity.webviewFragment.setVisibility(View.GONE);
        }
        else {
        super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        sharedPreference.removeAllLists();
        super.onStop();
    }
}