package com.cc.globenewslive.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.cc.globenewslive.Adapters.FavAdapter;
import com.cc.globenewslive.ApiClient.ApiClient;
import com.cc.globenewslive.ApiClient.FavClient;
import com.cc.globenewslive.MainActivity;
import com.cc.globenewslive.Model.FavOrNot;
import com.cc.globenewslive.Model.GetFav;
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

public class Favourites extends Fragment {

    List<GetFav> getFavList=new ArrayList<>();
    List<GetFav> updatedfav=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private FavAdapter adapter;
    private LottieAnimationView lottieAnimationView;
    private  boolean [] listType={false,false};

    SharedPreference sharedPreference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_favourites, container, false);
        lottieAnimationView=view.findViewById(R.id.animationView);
        lottieAnimationView.setVisibility(View.GONE);
        sharedPreference=new SharedPreference(getActivity());
        if(sharedPreference.LoggedIN()){
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
            if(sharedPreference.getFavList("FAV_KEY")!=null){
                if(!sharedPreference.getFavList("FAV_KEY").isEmpty()){
                    updatedfav=sharedPreference.getFavList("FAV_KEY");
                    if(listType[0]){
                        adapter=new FavAdapter(updatedfav,getActivity(),"list");
                    }
                    if(listType[1]) {
                        adapter=new FavAdapter(updatedfav,getActivity(),"grid");
                    }
                    recyclerView.setAdapter(adapter);
                    ItemTouchHelper helper=new ItemTouchHelper(callback);
                    helper.attachToRecyclerView(recyclerView);
                    adapter.notifyDataSetChanged();
                    lottieAnimationView.setVisibility(View.GONE);
                }
                else getFav(sharedPreference.getuserid());
            }
            else
            getFav(sharedPreference.getuserid());

        }
        else {
            ExtractUrl.LoginDialog(getActivity(), MainActivity.navigationView);
        }
        return view;
    }

    public void getFav(int user_id){
        ExtractUrl.CheckConnection(getActivity(),lottieAnimationView);
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
                    updatedfav=getFavList;
                    sharedPreference.setList("FAV_KEY",getFavList);
                    if(listType[0]){
                        adapter=new FavAdapter(getFavList,getActivity(),"list");
                    }
                    if(listType[1]) {
                        adapter=new FavAdapter(getFavList,getActivity(),"grid");
                    }
                    recyclerView.setAdapter(adapter);
                    ItemTouchHelper helper=new ItemTouchHelper(callback);
                    helper.attachToRecyclerView(recyclerView);
                    adapter.notifyDataSetChanged();
                    lottieAnimationView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<GetFav>> call, Throwable t) { }
        });
    }
    public void deletefromfav(int userid,int postid){
        ExtractUrl.CheckConnection(getActivity(),lottieAnimationView);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://livenewsglobe.com/wp-json/newspaper/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient= retrofit.create(ApiClient.class);
        Call<FavOrNot> delfromfav;
        delfromfav=apiClient.delfav(userid,postid);
        delfromfav.enqueue(new Callback<FavOrNot>() {
            @Override
            public void onResponse(Call<FavOrNot> call, Response<FavOrNot> response) {
                if(response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<FavOrNot> call, Throwable t) {

            }
        });

    }
    ItemTouchHelper.SimpleCallback callback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(sharedPreference.getFavList("FAV_KEY")!=null){
                if(!sharedPreference.getFavList("FAV_KEY").isEmpty()){
                    deletefromfav(sharedPreference.getuserid(),Integer.parseInt(updatedfav.get(viewHolder.getAdapterPosition()).getId()));
                    updatedfav.remove(viewHolder.getAdapterPosition());
                    adapter.notifyDataSetChanged();
                    sharedPreference.removeChannelsAndFav();
                }
                else {
                    deletefromfav(sharedPreference.getuserid(),Integer.parseInt(getFavList.get(viewHolder.getAdapterPosition()).getId()));
                    getFavList.remove(viewHolder.getAdapterPosition());
                    adapter.notifyDataSetChanged();
                    sharedPreference.removeChannelsAndFav();
                }
            }
            else {

                deletefromfav(sharedPreference.getuserid(),Integer.parseInt(getFavList.get(viewHolder.getAdapterPosition()).getId()));
                getFavList.remove(viewHolder.getAdapterPosition());
                adapter.notifyDataSetChanged();
            }
        }
    };
}