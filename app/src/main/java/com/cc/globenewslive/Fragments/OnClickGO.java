package com.cc.globenewslive.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.cc.globenewslive.Adapters.CityAdapter;
import com.cc.globenewslive.Adapters.ListViewNews;
import com.cc.globenewslive.ApiClient.ApiClient;
import com.cc.globenewslive.ApiClient.FavClient;
import com.cc.globenewslive.Model.City;
import com.cc.globenewslive.Model.GetFav;
import com.cc.globenewslive.Model.NewsPost;
import com.cc.globenewslive.R;
import com.cc.globenewslive.Utils.ExtractUrl;
import com.cc.globenewslive.Utils.SharedPreference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OnClickGO extends Fragment {


    private RecyclerView recyclerView;
    private List<City> cityList =new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private CityAdapter adapter;

    private List<NewsPost> newsPostList=new ArrayList<>();
    private ListViewNews newsAdapter;
    private LottieAnimationView lottieAnimationView;
    private boolean[] listType;
    String adaterType,StateName;
    String listorgrid;
    int Cityid=0;
    private List<GetFav> getFavList=new ArrayList<>();
    private SharedPreference sharedPreference;
    TextView textView;
    public OnClickGO() {
        // Required empty public constructor
    }

    public static OnClickGO StateInstance(String listType, String adaptertype,String State) {
        OnClickGO fragment = new OnClickGO();
        Bundle args = new Bundle();
        args.putString("keyList", listType);
        args.putString("adaptertype", adaptertype);
        args.putString("State", State);
        fragment.setArguments(args);
        return fragment;
    }
    public static OnClickGO CityInstance(String listType, String adaptertype,int cityid) {
        OnClickGO fragment = new OnClickGO();
        Bundle args = new Bundle();
        args.putString("keyList", listType);
        args.putString("adaptertype", adaptertype);
        args.putInt("city", cityid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view=inflater.inflate(R.layout.fragment_on_click_g_o, container, false);
        sharedPreference=new SharedPreference(getActivity());
        recyclerView=view.findViewById(R.id.recyclerview);
        lottieAnimationView=view.findViewById(R.id.animationView);
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.GONE);

        Bundle bundle=getArguments();
        if(bundle!=null){
            listorgrid=bundle.getString("keyList");
            adaterType=bundle.getString("adaptertype");
            StateName=bundle.getString("State");
            Cityid=bundle.getInt("city");

        }


        if(listorgrid.equals("list")){
            layoutManager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);}
        if(listorgrid.equals("grid")){
            gridLayoutManager=new GridLayoutManager(getActivity(),3,RecyclerView.VERTICAL,false);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        lottieAnimationView=view.findViewById(R.id.animationView);
        lottieAnimationView.setVisibility(View.VISIBLE);
        if(adaterType.equals("city")){
            getCityfromstates(StateName);
        }
        else if (adaterType.equals("channel")){
            getchannelsfromcity(Cityid);
        }
      return view;
    }
    public void getCityfromstates(String StateName) {
        ExtractUrl.CheckConnection(getActivity(),lottieAnimationView);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://livenewsglobe.com/wp-json/salman/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<City>> callCity;
        callCity = apiClient.getFilterCity(StateName);
        callCity.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if (response.isSuccessful()) {
                    if (cityList.isEmpty()) {
                        cityList.clear();
                    }
                    List<City> remove_Channel_city=new ArrayList<>();
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
                    if(listorgrid.equals("list")){
                        adapter=new CityAdapter(cityList,getActivity(),"list");
                    }
                    if(listorgrid.equals("grid")) {
                        adapter=new CityAdapter(cityList,getActivity(),"grid");
                    }
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    lottieAnimationView.setVisibility(View.GONE);

                }
                else {lottieAnimationView.setVisibility(View.GONE);}
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {

            }
        });
    }
    public void getchannelsfromcity(int city){
        ExtractUrl.CheckConnection(getActivity(),lottieAnimationView);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://www.livenewsglobe.com/wp-json/wp/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<NewsPost>> callChannels;
        callChannels = apiClient.getChannels(city);
        callChannels.enqueue(new Callback<List<NewsPost>>() {
            @Override
            public void onResponse(Call<List<NewsPost>> call, Response<List<NewsPost>> response) {
                if (response.isSuccessful()) {
                    if (newsPostList.isEmpty()) {
                        newsPostList.clear();
                    }
                    newsPostList = response.body();

                    ApiClient apiClient= FavClient.getRetrofitClient().create(ApiClient.class);
                    Call<List<GetFav>> getFavchannel;
                    getFavchannel=apiClient.getFavUser(sharedPreference.getuserid());
                    getFavchannel.enqueue(new Callback<List<GetFav>>() {
                        @Override
                        public void onResponse(Call<List<GetFav>> call, Response<List<GetFav>> response) {
                            if(response.isSuccessful()){
                                if(!getFavList.isEmpty()){
                                    getFavList.clear();
                                }
                                getFavList=response.body();
                                if(listorgrid.equals("list")){
                                    newsAdapter=new ListViewNews(newsPostList,getActivity(),"list",getFavList);
                                }
                                if(listorgrid.equals("grid")) {
                                    newsAdapter=new ListViewNews(newsPostList,getActivity(),"grid",getFavList);
                                }
                                recyclerView.setAdapter(newsAdapter);
                                newsAdapter.notifyDataSetChanged();
                                lottieAnimationView.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onFailure(Call<List<GetFav>> call, Throwable t) { }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<NewsPost>> call, Throwable t) {

            }
        });
    }
}