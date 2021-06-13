package com.cc.globenewslive.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cc.globenewslive.Adapters.ListViewNews;
import com.cc.globenewslive.ApiClient.ApiClient;
import com.cc.globenewslive.ApiClient.FavClient;
import com.cc.globenewslive.ApiClient.NetworkConnectionInterceptor;
import com.cc.globenewslive.ApiClient.RetrofitClient;
import com.cc.globenewslive.Model.FavOrNot;
import com.cc.globenewslive.Model.GetFav;
import com.cc.globenewslive.Model.NewsPost;
import com.cc.globenewslive.R;
import com.cc.globenewslive.Utils.ExtractUrl;
import com.cc.globenewslive.Utils.SharedPreference;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends Fragment {

    private static final String TAG ="HOME FRAGMENT" ;
    private RecyclerView recyclerView;
    private List<NewsPost> listNewspost=new ArrayList<>();
    private NewsPost newsPost;
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private ListViewNews adapter;
    private LottieAnimationView lottieAnimationView;
    private  boolean [] listType={false,false};
    SharedPreference sharedPreference;
    private FavOrNot favOrNot=new FavOrNot();
    private FavOrNot addFav;
    private FavOrNot delfav;
    private List<GetFav> getFavList=new ArrayList<>();
    private boolean[] favlistbool;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
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
        if(sharedPreference.getNews("CHANNELS_KEY")!=null){
            if (sharedPreference.getNews("CHANNELS_KEY").size() > 0) {
                if(listType[0]){
                    adapter=new ListViewNews(sharedPreference.getNews("CHANNELS_KEY"),getActivity(),"list",sharedPreference.getFavList("FAV_KEY"));
                }
                if(listType[1]) {
                    adapter=new ListViewNews(sharedPreference.getNews("CHANNELS_KEY"),getActivity(),"grid",sharedPreference.getFavList("FAV_KEY"));
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
        Call<List<NewsPost>> call;
        call=apiClient.getNews();
        call.enqueue(new Callback<List<NewsPost>>() {

            @Override
            public void onResponse(Call<List<NewsPost>> call, Response<List<NewsPost>> response) {

                if(response.isSuccessful()){
                    if(!listNewspost.isEmpty()){
                        listNewspost.clear();
                    }
                    listNewspost=response.body();

                    for(NewsPost newsPost: listNewspost){
                        if(newsPost.getTitle()==null||newsPost.getTitle().equals("Featured")){
                            listNewspost.remove(newsPost);}
                    }
                    sharedPreference.setList("CHANNELS_KEY",listNewspost);

                    ApiClient apiClient= FavClient.getRetrofitClient().create(ApiClient.class);
                    Call<List<GetFav>> getFavchannel;
                    getFavchannel=apiClient.getFavUser(sharedPreference.getuserid());
                    ExtractUrl.CheckConnection(getActivity(),lottieAnimationView);
                    getFavchannel.enqueue(new Callback<List<GetFav>>() {
                        @Override
                        public void onResponse(Call<List<GetFav>> call, Response<List<GetFav>> response) {
                            if(response.isSuccessful()){
                                if(!getFavList.isEmpty()){
                                    getFavList.clear();
                                }
                                getFavList=response.body();
                                sharedPreference.setList("FAV_KEY",getFavList);
                                if(listType[0]){
                                    adapter=new ListViewNews(listNewspost,getActivity(),"list",getFavList);
                                }
                                if(listType[1]) {
                                    adapter=new ListViewNews(listNewspost,getActivity(),"grid",getFavList);
                                }
                                recyclerView.setAdapter(adapter);
                                lottieAnimationView.setVisibility(View.GONE);

                            }
                        }

                        @Override
                        public void onFailure(Call<List<GetFav>> call, Throwable t) {
                        }
                    });


                }
                else {
                    Toast.makeText(getActivity(),"responseEmpty",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NewsPost>> call, Throwable t) {
            }
        });
    }

}