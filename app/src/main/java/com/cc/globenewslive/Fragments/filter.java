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
import com.cc.globenewslive.Adapters.Filter;
import com.cc.globenewslive.Adapters.ListViewNews;
import com.cc.globenewslive.Adapters.filterChannelAdapter;
import com.cc.globenewslive.ApiClient.ApiClient;
import com.cc.globenewslive.ApiClient.FavClient;
import com.cc.globenewslive.ApiClient.RetrofitClient;
import com.cc.globenewslive.MainActivity;
import com.cc.globenewslive.Model.City;
import com.cc.globenewslive.Model.GetFav;
import com.cc.globenewslive.Model.NewsPost;
import com.cc.globenewslive.Model.States;
import com.cc.globenewslive.R;
import com.cc.globenewslive.Utils.ExtractUrl;
import com.cc.globenewslive.Utils.SharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class filter extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<City> listCity = new ArrayList<>();
    private NewsPost newsPost;
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private Filter adapter;
    private filterChannelAdapter channelAdapter;
    private ArrayList<NewsPost> channelslist = new ArrayList<>();
    private String adaptertype = "cities";
    private List<GetFav> getFavList=new ArrayList<>();
    private SharedPreference sharedPreference;
    public static LottieAnimationView lottieAnimationView;
    private boolean[] listType = {false, false};

    public static filter newInstance(ArrayList<City> listCity, boolean[] listType, String adaptertype) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("listCity", listCity);
        args.putBooleanArray("keyList", listType);
        args.putString("adaptertype", adaptertype);
        filter fragment = new filter();
        fragment.setArguments(args);
        return fragment;
    }

    public static filter newInstanceChannel(ArrayList<NewsPost> list, boolean[] listType, String adaptertype) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("listChannels", list);
        args.putBooleanArray("keyList", listType);
        args.putString("adaptertype", adaptertype);
        filter fragment = new filter();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        sharedPreference=new SharedPreference(getActivity());
        lottieAnimationView=view.findViewById(R.id.animationView);
        Bundle bundle = getArguments();
        if (bundle != null) {
            listType = bundle.getBooleanArray("keyList");
            listCity = bundle.getParcelableArrayList("listCity");
            channelslist = bundle.getParcelableArrayList("listChannels");
            adaptertype = bundle.getString("adaptertype");


        }
        recyclerView = view.findViewById(R.id.recyclerview);
        if(sharedPreference.LoggedIN()){
        getFav(sharedPreference.getuserid());}
        else{

            if (listType[0]) {
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
            }
            if (listType[1]) {
                gridLayoutManager = new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(gridLayoutManager);
            }
            if (adaptertype == "city") {
                if (listType[0]) {
                    adapter = new Filter(listCity, getActivity(), "list");
                }
                if (listType[1]) {
                    adapter = new Filter(listCity, getActivity(), "grid");
                }
                lottieAnimationView.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else if (adaptertype == "channel"){
                if (listType[0]) {
                    channelAdapter=new filterChannelAdapter(channelslist,getActivity(),"list",getFavList);
                }
                if (listType[1]) {
                    channelAdapter=new filterChannelAdapter(channelslist,getActivity(),"grid",getFavList);
                }
                lottieAnimationView.setVisibility(View.GONE);

                recyclerView.setAdapter(channelAdapter);
                channelAdapter.notifyDataSetChanged();
            }
        }

        return view;
    }
    public void getFav(int user_id){
        ExtractUrl.CheckConnection(getActivity());


        ApiClient apiClient= FavClient.getRetrofitClient().create(ApiClient.class);
        Call<List<GetFav>> getFavchannel;
        getFavchannel=apiClient.getFavUser(user_id);
        getFavchannel.enqueue(new Callback<List<GetFav>>() {
            @Override
            public void onResponse(Call<List<GetFav>> call, Response<List<GetFav>> response) {
                if(response.isSuccessful()){
                    if(!getFavList.isEmpty()){
                        getFavList.clear();
                    }
                    getFavList=response.body();


                    if (listType[0]) {
                        layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                    }
                    if (listType[1]) {
                        gridLayoutManager = new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(gridLayoutManager);
                    }
                    if (adaptertype == "city") {
                        if (listType[0]) {
                            adapter = new Filter(listCity, getActivity(), "list");
                        }
                        if (listType[1]) {
                            adapter = new Filter(listCity, getActivity(), "grid");
                        }
                        lottieAnimationView.setVisibility(View.GONE);

                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    else if (adaptertype == "channel"){
                        if (listType[0]) {
                            channelAdapter=new filterChannelAdapter(channelslist,getActivity(),"list",getFavList);
                        }
                        if (listType[1]) {
                            channelAdapter=new filterChannelAdapter(channelslist,getActivity(),"grid",getFavList);
                        }
                        lottieAnimationView.setVisibility(View.GONE);

                        recyclerView.setAdapter(channelAdapter);
                        channelAdapter.notifyDataSetChanged();

                    }

                }
            }

            @Override
            public void onFailure(Call<List<GetFav>> call, Throwable t) { }
        });
    }
}

