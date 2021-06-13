package com.cc.globenewslive.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cc.globenewslive.Adapters.ListViewNews;
import com.cc.globenewslive.Adapters.StatesAdapter;
import com.cc.globenewslive.ApiClient.ApiClient;
import com.cc.globenewslive.ApiClient.NetworkConnectionInterceptor;
import com.cc.globenewslive.ApiClient.RetrofitClient;
import com.cc.globenewslive.Model.States;
import com.cc.globenewslive.R;
import com.cc.globenewslive.Utils.ExtractUrl;
import com.cc.globenewslive.Utils.SharedPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class State extends Fragment {

    private static final String TAG ="State" ;
    private RecyclerView recyclerView;
    private List<States> stateList =new ArrayList<>();
    private List<States> filterstate=new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;
    private StatesAdapter adapter;
    States states;
    private LottieAnimationView lottieAnimationView;
    boolean[] listType;
    SharedPreference sharedPreference;
    boolean internet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_states, container, false);
        sharedPreference=new SharedPreference(getActivity());
        Bundle bundle=getArguments();

        if(bundle!=null){
            listType=bundle.getBooleanArray("keyList");
            Log.d(TAG, "onCreateView: "+listType[0]+" "+listType[1]);
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
        if(sharedPreference.getStates("STATES_KEY")!=null){
            if(!sharedPreference.getStates("STATES_KEY").isEmpty()){
                if(listType[0]){
                    adapter=new StatesAdapter(sharedPreference.getStates("STATES_KEY"),getActivity(),"list");

                }
                if(listType[1]) {
                    adapter=new StatesAdapter(sharedPreference.getStates("STATES_KEY"),getActivity(),"grid");
                }
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lottieAnimationView.setVisibility(View.GONE);
            }
            else LoadJson();
        }
        else LoadJson();
        return view;
    }

    private void LoadJson() {
        ExtractUrl.CheckConnection(getActivity(),lottieAnimationView);
        ApiClient apiClient= RetrofitClient.getRetrofitClient(getActivity()).create(ApiClient.class);
        Call<List<States>> call;
        call=apiClient.getStates();

        call.enqueue(new Callback<List<States>>() {
            @Override
            public void onResponse(Call<List<States>> call, Response<List<States>> response) {

                Log.d(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    if(stateList.isEmpty()){
                        stateList.clear();
                    }
                    stateList =response.body();
                    List<States> remduplicates=new ArrayList<>();
                    for (States state:stateList){
                        if(state.getName().equals("Featured")||state.getName()==null){
                            remduplicates.add(state);
                        }
                    }
                    for (States state:remduplicates){
                        if(state.getName().equals("Featured")||state.getName()==null){
                            stateList.remove(state);
                        }
                    }

                    sharedPreference.setList("STATES_KEY",stateList);

                    if(listType[0]){
                        adapter=new StatesAdapter(stateList,getActivity(),"list");
                    }
                    if(listType[1]) {
                        adapter=new StatesAdapter(stateList,getActivity(),"grid");
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
            public void onFailure(Call<List<States>> call, Throwable t) {
            }
        });
    }


}