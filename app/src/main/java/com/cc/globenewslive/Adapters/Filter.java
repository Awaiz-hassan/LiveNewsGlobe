package com.cc.globenewslive.Adapters;

import android.content.Context;
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

import com.cc.globenewslive.Fragments.OnClickGO;
import com.cc.globenewslive.MainActivity;
import com.cc.globenewslive.Model.City;
import com.cc.globenewslive.R;
import com.cc.globenewslive.Utils.ExtractUrl;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Filter extends RecyclerView.Adapter<Filter.CityViewholder> {
    ArrayList<City> cityList;
    Context context;
    private int lastPosition=-1;
    private String listType;
    public static int currentcityid;

    public Filter(ArrayList<City> cityList, Context context, String listType) {
        this.cityList = cityList;
        this.context = context;
        this.listType=listType;
    }

    @NonNull
    @Override
    public CityViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(listType.equals("grid")){
            View view= LayoutInflater.from(context).inflate(R.layout.grid_cities,parent,false);
            CityViewholder cityViewholder=new CityViewholder(view);
            return cityViewholder;
        }
        View view= LayoutInflater.from(context).inflate(R.layout.city_list,parent,false);
        CityViewholder cityViewholder=new CityViewholder(view);
        return cityViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewholder holder, int position) {
        String title=cityList.get(position).getName();
        String thumbnailUrl=cityList.get(position).getDescription();
        if(title!=null)
            holder.cityName.setText(title);
        if (thumbnailUrl != null) {
            List<String> url= ExtractUrl.extractURL(thumbnailUrl);
            if(url!=null)
                Picasso.get().load(url.get(0)).into(holder.thumbnail);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentcityid=cityList.get(position).getTermId();
                MainActivity.currentFragment="onclikfilterfiltercity";
                Fragment fragment1= OnClickGO.CityInstance(listType,"channel",cityList.get(position).getTermId());
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();
            }
        });
        if(holder.getAdapterPosition()>lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down);

            holder.itemView.startAnimation(animation);
        }
        lastPosition=holder.getAdapterPosition();


    }

    @Override
    public int getItemCount() {
        if(cityList==null)return 0;
        return cityList.size();
    }
    public class CityViewholder extends RecyclerView.ViewHolder{
        private ImageView thumbnail;
        private TextView cityName;

        public CityViewholder(@NonNull View itemView) {
            super(itemView);
            thumbnail=itemView.findViewById(R.id.citythumbnail1);
            cityName=itemView.findViewById(R.id.citytitle);
        }
    }

}
