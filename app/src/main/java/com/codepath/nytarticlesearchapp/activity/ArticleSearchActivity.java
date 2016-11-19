package com.codepath.nytarticlesearchapp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.codepath.nytarticlesearchapp.R;
import com.codepath.nytarticlesearchapp.adapter.RecyclerViewAdapter;
import com.codepath.nytarticlesearchapp.connection.ArticleConnection;
import com.codepath.nytarticlesearchapp.databinding.ActivityArticleSearchBinding;
import com.codepath.nytarticlesearchapp.decorator.SpacesItemDecorator;
import com.codepath.nytarticlesearchapp.fragment.FilterSettingsFragment;
import com.codepath.nytarticlesearchapp.helper.EndlessRecyclerViewScrollListener;
import com.codepath.nytarticlesearchapp.model.Article;
import com.codepath.nytarticlesearchapp.model.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ArticleSearchActivity extends AppCompatActivity implements FilterSettingsFragment.OnItemSelectedListener {
    List<Article> articles;
    SearchView searchView;
    RecyclerViewAdapter articleAdapter;
    RecyclerView rvItems;
    private int REQUEST_CODE = 20;
    String currQuery = "";
    private final int numItemsInRow = 2;
    private ActivityArticleSearchBinding binding;
    Settings settings;
    private EndlessRecyclerViewScrollListener endlessScrollListener;
    StaggeredGridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_search);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvItems = binding.rvArticles;
        articles = new ArrayList<>();
        articleAdapter = new RecyclerViewAdapter(ArticleSearchActivity.this, articles);
        rvItems.setAdapter(articleAdapter);
        layoutManager = new StaggeredGridLayoutManager(numItemsInRow, StaggeredGridLayoutManager.VERTICAL);
        rvItems.setLayoutManager(layoutManager);
        SpacesItemDecorator decoration = new SpacesItemDecorator(8);
        rvItems.addItemDecoration(decoration);
//        RecyclerView.ItemDecoration itemDecorationHor = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
//        RecyclerView.ItemDecoration itemDecorationVer = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        rvItems.addItemDecoration(itemDecorationHor);
//        rvItems.addItemDecoration(itemDecorationVer);
        rvItems.addOnScrollListener(getEndlessScrollListenerInstance());
        loadNextDataFromApi(0, true);
    }

    private EndlessRecyclerViewScrollListener getEndlessScrollListenerInstance() {
        endlessScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page, false);
            }
        };
        return endlessScrollListener;
    }

    private void loadNextDataFromApi(int page, boolean clearList) {
        final String pageQueryParam = "&page=";
        String pageQuery = pageQueryParam + page;
        String finalQuery = pageQuery + currQuery;
        getAPIResponse(finalQuery, clearList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_search, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        if (searchMenuItem != null) {
            searchView = (android.widget.SearchView) searchMenuItem.getActionView();
            if (searchView != null) {
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        listArticlesByCategory(query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                });
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getFilterData(MenuItem item) {
//        Intent intent = new Intent(ArticleSearchActivity.this, FilterActivity.class);
//        startActivityForResult(intent, REQUEST_CODE);
        Bundle bundle = new Bundle();
        if (settings == null) {
            settings = Settings.getSettingsInstance();
        }
        bundle.putParcelable("settings", Parcels.wrap(settings));
        FragmentManager fm = getSupportFragmentManager();
        FilterSettingsFragment filterSettingsFragment = FilterSettingsFragment.newInstance();
        filterSettingsFragment.setArguments(bundle);
        filterSettingsFragment.show(fm, "activity_filter");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            currQuery = data.getExtras().getString("query");
            loadNextDataFromApi(0, true);
        }
    }

    private void getAPIResponse(String query, final boolean clearList) {
        ArticleConnection connection = new ArticleConnection();
        connection.getArticles(query, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d("DEBUG", "Could not get response");
                }
                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonResponse.getJSONObject("response").getJSONArray("docs");
                    if (clearList) {
                        articles.clear();
                    }
                    articles.addAll(Article.getArticles(jsonArray));
                    runOnUiThread(() -> articleAdapter.notifyDataSetChanged());
                } catch (JSONException jsone) {
                    Log.d("DEBUG", "Could not convert response string to JSON");
                }
            }
        });
    }

    private void listArticlesByCategory(String query) {
        articles.clear();
        final String queryParam = "&q=";
        currQuery = queryParam + query;
        loadNextDataFromApi(0, false);
    }

    @Override
    public void onRssItemSelected(String query) {
        articles.clear();
        currQuery = query;
        rvItems.addOnScrollListener(getEndlessScrollListenerInstance());
        loadNextDataFromApi(0, false);
    }



}
