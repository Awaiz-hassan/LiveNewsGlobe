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
import com.cc.globenewslive.Model.States;
import com.cc.globenewslive.R;
import com.cc.globenewslive.Utils.ExtractUrl;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StatesAdapter extends RecyclerView.Adapter<StatesAdapter.ListViewHolder> {
    private static final String TAG = "States Adapter";
    private List<States> statesList;
    private Context context;
    private String listType;
    private List<City> cityList;
    public static String currentcityname;
    private int lastPosition = -1;

    public StatesAdapter(List<States> statesList, Context context, String listType) {
        this.statesList = statesList;
        this.context = context;
        this.listType = listType;
    }

    @NonNull
    @Override
    public StatesAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (listType.equals("grid")) {
            View view = LayoutInflater.from(context).inflate(R.layout.grid_states, parent, false);
            ListViewHolder listViewHolder = new ListViewHolder(view);
            return listViewHolder;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_states, parent, false);
        ListViewHolder listViewHolder = new ListViewHolder(view);
        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatesAdapter.ListViewHolder holder, int position) {
        String title = statesList.get(position).getName();
        String thumbnailUrl = statesList.get(position).getDescription();

        holder.textView.setText(title);
        if (thumbnailUrl != null && thumbnailUrl.contains("<img")) {
            List<String> url = ExtractUrl.extractURL(thumbnailUrl);
            if (url != null)
                Picasso.get().load(url.get(0)).into(holder.Thumbnail);

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentcityname=statesList.get(position).getName();
                MainActivity.currentFragment="clickfilterstates";
                Fragment fragment1= OnClickGO.StateInstance(listType,"city",statesList.get(position).getName());

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment1).commit();
            }
        });
        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down);

            holder.itemView.startAnimation(animation);
        }
        lastPosition = holder.getAdapterPosition();


    }

    @Override
    public int getItemCount() {
        return statesList.size();
    }


    public static class ListViewHolder extends RecyclerView.ViewHolder {
        private ImageView Thumbnail;
        private TextView textView;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            Thumbnail = itemView.findViewById(R.id.thumbnail1);
            textView = itemView.findViewById(R.id.title);

        }
    }
}
