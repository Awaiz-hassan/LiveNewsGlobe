package com.cc.globenewslive.Fragments;

import android.content.Context;
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
import com.cc.globenewslive.Adapters.ListViewNews;
import com.cc.globenewslive.Adapters.SearchAdapter;
import com.cc.globenewslive.ApiClient.ApiClient;
import com.cc.globenewslive.ApiClient.FavClient;
import com.cc.globenewslive.ApiClient.RetrofitClient;
import com.cc.globenewslive.MainActivity;
import com.cc.globenewslive.Model.FavOrNot;
import com.cc.globenewslive.Model.GetFav;
import com.cc.globenewslive.Model.NewsPost;
import com.cc.globenewslive.Model.Search;
import com.cc.globenewslive.R;
import com.cc.globenewslive.Utils.ExtractUrl;
import com.cc.globenewslive.Utils.SharedPreference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResult extends Fragment {

    private static final String TAG ="HOME FRAGMENT" ;
    private RecyclerView recyclerView;
    private List<Search> searchResultList =new ArrayList<>();
    private NewsPost newsPost;
    private RecyclerView.LayoutManager layoutManager;
    private SearchAdapter adapter;
    private LottieAnimationView lottieAnimationView;
    private GridLayoutManager gridLayoutManager;
    private boolean[] listType={true,false};
    private  String searchQuery;
    private SharedPreference sharedPreference;
    private FavOrNot favOrNot=new FavOrNot();
    private FavOrNot addFav;
    private FavOrNot delfav;
    private List<GetFav> getFavList=new ArrayList<>();
    private boolean[] favlistbool;
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        sharedPreference=new SharedPreference(getActivity());
        context=getActivity();
        Bundle bundle=getArguments();
        if(bundle!=null){
            listType=bundle.getBooleanArray("keyList");
            searchQuery=bundle.getString("key");
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

        LoadJson();
        return view;
    }

    private void LoadJson() {
        ApiClient apiClient= RetrofitClient.getRetrofitClient(getActivity()).create(ApiClient.class);

        Call<List<Search>> call;
        call=apiClient.getResultts(searchQuery);
        call.enqueue(new Callback<List<Search>>() {
            @Override
            public void onResponse(Call<List<Search>> call, Response<List<Search>> response) {
                Log.d(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    if(searchResultList.isEmpty()){
                        searchResultList.clear();
                    }
                    searchResultList=response.body();

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
                                if(listType[0]){
                                    adapter=new SearchAdapter(searchResultList,getActivity(),"list",getFavList);
                                }
                                if(listType[1]) {
                                    adapter=new SearchAdapter(searchResultList,getActivity(),"grid",getFavList);
                                }
                                recyclerView.setAdapter(adapter);
                                lottieAnimationView.setVisibility(View.GONE);
                                Log.d(TAG, "onResponse: "+getFavList.size());

                            }
                        }

                        @Override
                        public void onFailure(Call<List<GetFav>> call, Throwable t) { }
                    });

                }
                else{
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning")
                            .setContentText("Found Nothing")
                            .show();
                    lottieAnimationView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Search>> call, Throwable t) {

            }
        });

    }
}