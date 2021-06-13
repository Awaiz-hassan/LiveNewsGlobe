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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cc.globenewslive.Adapters.CityAdapter;
import com.cc.globenewslive.Adapters.ListViewNews;
import com.cc.globenewslive.Adapters.StatesAdapter;
import com.cc.globenewslive.ApiClient.ApiClient;
import com.cc.globenewslive.ApiClient.RetrofitClient;
import com.cc.globenewslive.MainActivity;
import com.cc.globenewslive.Model.City;
import com.cc.globenewslive.Model.States;
import com.cc.globenewslive.R;
import com.cc.globenewslive.Utils.ExtractUrl;
import com.cc.globenewslive.Utils.SharedPreference;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Cities extends Fragment {
    private static final String TAG ="State" ;
    private RecyclerView recyclerView;
    private List<City> cityList =new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private CityAdapter adapter;
    private LottieAnimationView lottieAnimationView;
    private boolean[] listType;
    SharedPreference sharedPreference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cities, container, false);
        sharedPreference=new SharedPreference(getActivity());
        Bundle bundle=getArguments();
        if(bundle!=null){
            listType=bundle.getBooleanArray("keyList");
        }
        recyclerView=view.findViewById(R.id.recyclerview);
        if(listType[0]){
            layoutManager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);}
        if(listType[1]){
            gridLayoutManager=new GridLayoutManager(getActivity(),3,RecyclerView.VERTICAL,false);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        lottieAnimationView=view.findViewById(R.id.animationView);
        lottieAnimationView.setVisibility(View.VISIBLE);
        if(sharedPreference.getCities("CITIES_KEY")!=null){
            if(!sharedPreference.getCities("CITIES_KEY").isEmpty()){
                if(listType[0]){
                    adapter=new CityAdapter(sharedPreference.getCities("CITIES_KEY"),getActivity(),"list");
                }
                if(listType[1]) {
                    adapter=new CityAdapter(sharedPreference.getCities("CITIES_KEY"),getActivity(),"grid");
                }
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lottieAnimationView.setVisibility(View.GONE);
            }
            else
                LoadJson();
        }
        else
        LoadJson();
        return view;
    }
    private void LoadJson() {
        ExtractUrl.CheckConnection(getActivity(),lottieAnimationView);
        ApiClient apiClient= RetrofitClient.getRetrofitClient(getActivity()).create(ApiClient.class);
        Call<List<City>> call;
        call=apiClient.getCity();

        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {


                if(response.isSuccessful()){

                    if(cityList.isEmpty()){
                        cityList.clear();
                    }
                    cityList =response.body();
                    Set<City> setOfBlogs = new LinkedHashSet<>(cityList);

                    cityList.clear();
                    cityList.addAll(setOfBlogs);
                    List<City>remove_Channel_city=new ArrayList<>();
                    for(City city1: cityList){
                        if(city1.getName().equals("Fox") ||city1.getName().equals("ABC")||city1.getName().equals("NBC")|| city1.getName().equals("CBS")||city1.getName().equals("Featured")||city1.getName().equals(null))
                            remove_Channel_city.add(city1);
                    }

                    for(int i=0;i<remove_Channel_city.size();i++){
                        if(cityList.contains(remove_Channel_city.get(i))){
                            cityList.remove(remove_Channel_city.get(i));
                        }
                    }
                    sharedPreference.setList("CITIES_KEY",cityList);
                    if(listType[0]){
                        adapter=new CityAdapter(cityList,getActivity(),"list");
                    }
                    if(listType[1]) {
                        adapter=new CityAdapter(cityList,getActivity(),"grid");
                    }
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                lottieAnimationView.setVisibility(View.GONE);
            }
                else {
                    Toast.makeText(getActivity(),"responseEmpty",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                lottieAnimationView.setVisibility(View.GONE);


            }
        });
    }
}