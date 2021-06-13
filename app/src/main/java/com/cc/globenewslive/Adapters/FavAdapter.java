package com.cc.globenewslive.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cc.globenewslive.ApiClient.ApiClient;
import com.cc.globenewslive.Fragments.WebViewNews;
import com.cc.globenewslive.MainActivity;
import com.cc.globenewslive.Model.FavOrNot;
import com.cc.globenewslive.Model.GetFav;
import com.cc.globenewslive.R;
import com.cc.globenewslive.Utils.ExtractUrl;
import com.cc.globenewslive.Utils.SharedPreference;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ListViewHolder> {

    private List<GetFav> getFavList;
    private Context context;
    private int lastPosition=-1;
    private String listType;
    private SharedPreference sharedPreference;

    public FavAdapter(List<GetFav> getFavList, Context context, String listType) {
        this.getFavList = getFavList;
        this.context = context;
        this.listType=listType;
        sharedPreference=new SharedPreference(context);
    }
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(listType.equals("grid")){
            View view= LayoutInflater.from(context).inflate(R.layout.grid_home,parent,false);
            ListViewHolder listViewHolder= new ListViewHolder(view);
            return listViewHolder;
        }
        View view= LayoutInflater.from(context).inflate(R.layout.fav_list_item,parent,false);
        ListViewHolder listViewHolder= new ListViewHolder(view);
        return listViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        String title=getFavList.get(position).getPostTitle();
        holder.textView.setText(title);
        String thumbnail=getFavList.get(position).getPostContent();


        if(getFavList.get(position).getPostContent()!=null){
           String getThumbnail=ExtractUrl.extractURL(getFavList.get(position).getPostContent()).get(0);

            if (getThumbnail != null) {
                Picasso.get().load(getThumbnail).into(holder.Thumbnail);
            }
        }
        if(holder.getAdapterPosition()>lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down);

            holder.itemView.startAnimation(animation);
        }
        lastPosition=holder.getAdapterPosition();
        if(listType.equals("grid")){
        holder.favourite.setImageResource(R.drawable.favtrue);
        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreference.LoggedIN()){
                    sharedPreference.removeChannelsAndFav();
                    deletefromfav(sharedPreference.getuserid(),Integer.parseInt(getFavList.get(position).getId()));
                    holder.favourite.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
                else {
                    ExtractUrl.LoginDialog(context, MainActivity.navigationView);
                }

            }
        });}
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.currentFragment="webview";
                Fragment fragment1= WebViewNews.newInstance(getFavList.get(position).getGuid());
                MainActivity.detachfrag=fragment1;
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.WebvieFragment, fragment1).commit();
                MainActivity.mainRelativelayout.setVisibility(View.GONE);
                MainActivity.webviewFragment.setVisibility(View.VISIBLE);
            }

        });
    }

    @Override
    public int getItemCount() {
        return getFavList.size();
    }


    public static class ListViewHolder extends RecyclerView.ViewHolder {
        private ImageView Thumbnail,favourite;
        private TextView textView;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            Thumbnail = itemView.findViewById(R.id.thumbnail1);
            textView = itemView.findViewById(R.id.title);
            favourite=itemView.findViewById(R.id.fav);
        }
    }
    public void deletefromfav(int userid,int postid){
        ExtractUrl.CheckConnection((FragmentActivity)context);
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
                    if(sharedPreference.LoggedIN())
                        sharedPreference.removeChannelsAndFav();

                }
            }

            @Override
            public void onFailure(Call<FavOrNot> call, Throwable t) {

            }
        });

    }
}