package com.demo.moviedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.moviedemo.R;
import com.demo.moviedemo.constant.Utils;
import com.demo.moviedemo.network.ApiModels.Search;
import com.demo.moviedemo.network.UiUpdateCallback;

import java.util.ArrayList;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private UiUpdateCallback callback;
    private ArrayList<Search> bookmarkList;
    private Context mContext;
    private ArrayList<Search> moviesList;

    // RecyclerView recyclerView;
    public MoviesAdapter(Context context, ArrayList<Search> moviesList, UiUpdateCallback callback) {
        mContext = context;
        this.moviesList = moviesList;
        this.callback = callback;
        bookmarkList = Utils.getBookMarkedList(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.movie_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Search search = moviesList.get(position);
        holder.tvName.setText(search.getTitle());
        holder.tvYear.setText(search.getYear());
        Utils.loadImage(mContext,holder.ivPoster, search.getPoster());


    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster, ivBookmark;
        TextView tvName, tvYear;
        RelativeLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            this.ivPoster = itemView.findViewById(R.id.ivPoster);
            this.tvName = itemView.findViewById(R.id.tvName);
            this.tvYear = itemView.findViewById(R.id.tvYear);
        }
    }
}  