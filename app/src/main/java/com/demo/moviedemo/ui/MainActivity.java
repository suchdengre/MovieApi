package com.demo.moviedemo.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.moviedemo.R;
import com.demo.moviedemo.adapter.MoviesAdapter;
import com.demo.moviedemo.network.ApiModels.MoviesResponse;
import com.demo.moviedemo.network.ApiModels.Search;
import com.demo.moviedemo.network.NetworkManager;
import com.demo.moviedemo.network.UiUpdateCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements UiUpdateCallback {

    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private int totalResults = 0;
    private int page = 1;
    private String filterText = "default";
    private boolean isLoading;
    private ArrayList<Search> moviesList = new ArrayList<>();
    private MoviesAdapter moviesAdapter;
    private LinearLayout layBookmarks;
    private TextView tvLabelBookmark;
    private TextView tvLabelMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        iniData();
    }

    private void iniData() {
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        moviesAdapter = new MoviesAdapter(MainActivity.this, moviesList, this);
        recyclerView.setAdapter(moviesAdapter); // set the Adapter to RecyclerView
        callSearchApi(filterText, false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                if (searchText.length() > 2) {
                    page = 1;
                    filterText = searchText;
                    callSearchApi(searchText, true);
                } else if (searchText.length() == 0) {
                    filterText = "default";
                    callSearchApi(filterText, true);
                }
                return false;
            }
        });
    }

    private void initViews() {
        searchView = findViewById(R.id.searchView);
        tvLabelMovies = findViewById(R.id.tvLabelMovies);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void callSearchApi(String searchText, final boolean isFromTextChanged) {
        Map<String, Object> query = new HashMap<>();
        isLoading = true;
        query.put("s", searchText);
        query.put("apikey", "10a99111");
        query.put("page", page);
        NetworkManager.getInstance().request(query, NetworkManager.REQUEST.FETCH_MOVIES,
                new NetworkManager.RequestCallback() {
                    @Override
                    public void onSuccess(NetworkManager.REQUEST type, Response response) {
                        isLoading = false;
                        MoviesResponse moviesResponse = (MoviesResponse) response.body();
                        if (moviesResponse.getResponse().equalsIgnoreCase("True")) {
                            totalResults = Integer.parseInt(moviesResponse.getTotalResults());
                            ArrayList<Search> moviesSearchList = moviesResponse.getSearch();
                            if (isFromTextChanged)
                                moviesList.clear();
                            moviesList.addAll(moviesSearchList);
                            moviesAdapter.notifyDataSetChanged();
                        } else {
                            moviesList.clear();
                            moviesAdapter.notifyDataSetChanged();
                        }
                    }


                    @Override
                    public void onFailure(NetworkManager.REQUEST type) {
                        Log.e("Data Send result", "Failed");
                        isLoading = false;
                    }

                });
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = gridLayoutManager.getChildCount();
            int totalItemCount = gridLayoutManager.getItemCount();
            int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

            if (!isLoading) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount < totalResults) {
                    page++;
                    callSearchApi(filterText, false);
                }
            }
        }
    };





    @Override
    public void updateUI() {

    }
}
