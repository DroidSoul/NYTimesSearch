package com.droidsoul.nytimessearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.droidsoul.nytimessearch.adapters.ArticleAdapter;
import com.droidsoul.nytimessearch.adapters.ArticleArrayAdapter;
import com.droidsoul.nytimessearch.fragments.FilterFragment;
import com.droidsoul.nytimessearch.models.Article;
import com.droidsoul.nytimessearch.models.Query;
import com.droidsoul.nytimessearch.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.droidsoul.nytimessearch.R.id.rvResults;

public class SearchActivity extends AppCompatActivity implements FilterFragment.onFragmentResult{

//    EditText etQuery;
//    Button btnSearch;
    RecyclerView rvResults;
    ArrayList<Article> articles;
//    ArticleArrayAdapter articleAdapter;
    ArticleAdapter articleAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    Query preQuery;
    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        parentLayout = findViewById(R.id.toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupView();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        parentLayout = findViewById(R.id.activity_search);
        if (!isNetworkAvailable()) {
            final Snackbar snackBar = Snackbar.make(parentLayout, "no internet", Snackbar.LENGTH_INDEFINITE);;
            snackBar.setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackBar.dismiss();
                }
            });
            snackBar.show();
        }
        else {
            if (articles.size() == 0) {
                Snackbar.make(parentLayout, "", Snackbar.LENGTH_LONG)
                        .setAction("Welcome, Here are your top stories today!", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .show();
                showTopStories();
            }
        }
    }
    public void showTopStories() {
        String topStoriesURL = "https://api.nytimes.com/svc/topstories/v2/home.json";
        RequestParams params = preQuery.getParams();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(topStoriesURL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONArray("results");
                    articles.clear();
                    articles.addAll(Article.fromJSONArray(articleJsonResults, true));
                    articleAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void setupView() {
//        etQuery = (EditText) findViewById(R.id.etQuery);
 //       btnSearch = (Button) findViewById(R.id.btnSearch);
        rvResults = (RecyclerView) findViewById(R.id.rvResults);
        articles = new ArrayList<>();
        preQuery = new Query();
        articleAdapter = new ArticleAdapter(this, articles);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(4,1);
        rvResults.setLayoutManager(staggeredGridLayoutManager);
        articleAdapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Article article = articles.get(position);
                Intent i = new Intent(SearchActivity.this, ArticleActivity.class);
                i.putExtra("url", article.getWebUrl());
                startActivity(i);
            }
        });
//        articleAdapter = new ArticleArrayAdapter(this, articles);
        rvResults.setAdapter(articleAdapter);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,1);
        rvResults.setLayoutManager(staggeredGridLayoutManager);
//        setupGridViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchView.clearFocus();
                searchArticle(query, 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FilterFragment filterFragment = FilterFragment.newInstance(preQuery);
            FragmentManager fm = getSupportFragmentManager();
            filterFragment.show(fm, "fragment_filter");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void searchArticle(final String queryStr, int page) {
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = preQuery.getParams();
        params.put("page", page);
        params.put("q", queryStr);
        if (page == 0) {
            rvResults.clearOnScrollListeners();
            rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    searchArticle(queryStr, page);
                }
            });
            client.get(url, params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray articleJSONResults = null;
                    try {
                        articleJSONResults = response.getJSONObject("response").getJSONArray("docs");
                        articles.clear();
                        articles.addAll(Article.fromJSONArray(articleJSONResults, false));
                        articleAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
 //           etQuery.setText("");
        }
        else if (page > 99) {
            return;
        }
        else {
            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray articleJsonResults = null;
                    try {
                        articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                        articles.addAll(Article.fromJSONArray(articleJsonResults, false));
                        int curSize = articleAdapter.getItemCount();
                        articleAdapter.notifyItemRangeInserted(curSize, articles.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }

    }

    @Override
    public void returnData(Query query) {
        preQuery = query;
    }
}
