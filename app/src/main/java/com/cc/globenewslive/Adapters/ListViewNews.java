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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cc.globenewslive.ApiClient.ApiClient;
import com.cc.globenewslive.ApiClient.FavClient;
import com.cc.globenewslive.Fragments.OnClickGO;
import com.cc.globenewslive.Fragments.WebViewNews;
import com.cc.globenewslive.MainActivity;
import com.cc.globenewslive.Model.FavOrNot;
import com.cc.globenewslive.Model.GetFav;
import com.cc.globenewslive.Model.NewsPost;
import com.cc.globenewslive.R;
import com.cc.globenewslive.Utils.ExtractUrl;
import com.cc.globenewslive.Utils.SharedPreference;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListViewNews extends RecyclerView.Adapter<ListViewNews.ListViewHolder> {

    private List<NewsPost> newsPostList;
    private Context context;
    private int lastPosition=-1;
    private String listType;
    private FavOrNot favOrNot=new FavOrNot();
    private FavOrNot addFav=new FavOrNot();
    private List<GetFav> getFavList=new ArrayList<>();
    private FavOrNot delfav;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    SharedPreference sharedPreference;

    public ListViewNews(List<NewsPost> newsPostList, Context context, String listType,List<GetFav> getFavList) {
        this.newsPostList = newsPostList;
        this.context = context;
        this.listType=listType;
        sharedPreference=new SharedPreference(context);
        this.getFavList=getFavList;
        viewBinderHelper.setOpenOnlyOne(true);
    }
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(listType.equals("grid")){
         View view= LayoutInflater.from(context).inflate(R.layout.grid_home,parent,false);
            ListViewHolder listViewHolder= new ListViewHolder(view);
            return listViewHolder;
        }
        View view= LayoutInflater.from(context).inflate(R.layout.list_item_home,parent,false);
        ListViewHolder listViewHolder= new ListViewHolder(view);
        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        if(!listType.equals("grid")){
            viewBinderHelper.bind(holder.swipeRevealLayout, newsPostList.get(position).getId().toString());
            viewBinderHelper.setOpenOnlyOne(true);
        }
         String title=newsPostList.get(position).getTitle();
         String thumbnailUrl=newsPostList.get(position).getThumbnailUrl().get(0);
        holder.textView.setText(title);
        if (thumbnailUrl != null) {
            Picasso.get().load(thumbnailUrl).into(holder.Thumbnail);
        }
         if(holder.getAdapterPosition()>lastPosition) {
             Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down);

             holder.itemView.startAnimation(animation);
         }
        if(getFavList!=null){
            for(int i=0;i<getFavList.size();i++){

                if(newsPostList.get(position).getTitle().equals(getFavList.get(i).getPostTitle())){
                    holder.favourite.setImageResource(R.drawable.favtrue);
                }
            }
        }
        if(!listType.equals("grid")){

             holder.cardView.setOnClickListener(new View.OnClickListener() {

                 @Override
                 public void onClick(View v) {
                     MainActivity.currentFragment="webview";
                     Fragment fragment1= WebViewNews.newInstance(newsPostList.get(position).getGuid());
                     MainActivity.detachfrag=fragment1;
                     ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.WebvieFragment, fragment1).commit();
                     MainActivity.mainRelativelayout.setVisibility(View.GONE);
                     MainActivity.webviewFragment.setVisibility(View.VISIBLE);
                 }
             });
        }
         lastPosition=holder.getAdapterPosition();
         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 MainActivity.currentFragment="webview";
                 Fragment fragment1= WebViewNews.newInstance(newsPostList.get(position).getGuid());
                 MainActivity.detachfrag=fragment1;
                 ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.WebvieFragment, fragment1).commit();
                 MainActivity.mainRelativelayout.setVisibility(View.GONE);
                 MainActivity.webviewFragment.setVisibility(View.VISIBLE);
             }
         });

         if(!listType.equals("grid")){
         holder.favourite.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 holder.swipeRevealLayout.close(true);
                 addOrDelfav(holder,position);
                 if(sharedPreference.LoggedIN())
                 sharedPreference.removeChannelsAndFav();

             }
         });}
         else if(listType.equals("grid")){
             holder.favourite.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     addOrDelfav(holder,position);
                     if(sharedPreference.LoggedIN())
                         sharedPreference.removeChannelsAndFav();
                 }
             });

         }

    }

    @Override
    public int getItemCount() {
        return newsPostList.size();
    }
    public static class ListViewHolder extends RecyclerView.ViewHolder {
        private ImageView Thumbnail,favourite;
        private TextView textView;
        private SwipeRevealLayout swipeRevealLayout;
        private CardView cardView;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            Thumbnail = itemView.findViewById(R.id.thumbnail1);
            textView = itemView.findViewById(R.id.title);
            swipeRevealLayout=itemView.findViewById(R.id.sw);
            favourite=itemView.findViewById(R.id.fav);
            cardView=itemView.findViewById(R.id.clickablecardview);


        }
    }

    public void addtofav(int userid,String email,int postid){
        ExtractUrl.CheckConnection(context);

        ApiClient apiClient= FavClient.getRetrofitClient().create(ApiClient.class);
        Call<FavOrNot> addtofav;
        addtofav=apiClient.addfav(userid,email,postid);
        addtofav.enqueue(new Callback<FavOrNot>() {
            @Override
            public void onResponse(Call<FavOrNot> call, Response<FavOrNot> response) {
                if(response.isSuccessful()){
                    addFav=response.body(); }
            }

            @Override
            public void onFailure(Call<FavOrNot> call, Throwable t) {

            }
        });
    }
    public void deletefromfav(int userid,int postid){
        ExtractUrl.CheckConnection(context);
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
                    delfav=response.body();
                }
            }

            @Override
            public void onFailure(Call<FavOrNot> call, Throwable t) {

            }
        });

    }
    public void addOrDelfav(ListViewHolder holder, int position){
        if(sharedPreference.LoggedIN()) {
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl("https://livenewsglobe.com/wp-json/newspaper/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiClient apiClient=retrofit.create(ApiClient.class);
            Call<FavOrNot> favCheck;
            favCheck=apiClient.favcheck(sharedPreference.getuserid(),newsPostList.get(position).getId());
            favCheck.enqueue(new Callback<FavOrNot>() {
                @Override
                public void onResponse(Call<FavOrNot> call, Response<FavOrNot> response) {
                    if(response.isSuccessful()){
                        if(favOrNot!=null){favOrNot=null;}
                        favOrNot=response.body();
                        if (favOrNot.getStatus() == 0) {
                            addtofav(sharedPreference.getuserid(), sharedPreference.getUserEmail(), newsPostList.get(position).getId());
                            holder.favourite.setImageResource(R.drawable.favtrue);
                            holder.favourite.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fav_beat));

                        } else if (favOrNot.getStatus() == 1) {
                            deletefromfav(sharedPreference.getuserid(), newsPostList.get(position).getId());
                            holder.favourite.setImageResource(R.drawable.ic_baseline_favorite_24);


                        }
                    }
                }

                @Override
                public void onFailure(Call<FavOrNot> call, Throwable t) {

                }
            });
        }
        else{
            ExtractUrl.LoginDialog(context, MainActivity.navigationView);
        }
    }
}
