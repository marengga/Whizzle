package com.marengga.whizzle.fragments;

import com.marengga.whizzle.R;
import com.marengga.whizzle.adapter.NewsfeedAdapter;
import com.marengga.whizzle.app.AppController;
import com.marengga.whizzle.data.NewsfeedModel;
 
import com.marengga.whizzle.utils.Constant;
import com.marengga.whizzle.utils.Menus;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class NewsfeedFragment extends Fragment {
	private boolean mSearchCheck;
	
	public NewsfeedFragment newInstance(String text){
		NewsfeedFragment mFragment = new NewsfeedFragment();		
		Bundle mBundle = new Bundle();
		mBundle.putString(Constant.TEXT_FRAGMENT, text);
		mFragment.setArguments(mBundle);
		return mFragment;
	}
	
    private static final String TAG = NewsfeedFragment.class.getSimpleName();
    private ListView listView;
    private NewsfeedAdapter listAdapter;
    private List<NewsfeedModel> feedItems;
    private String URL_FEED = Constant.URL_FEED;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.newsfeed_fragment, container, false);
 
        listView = (ListView) rootView.findViewById(R.id.list);
 
        feedItems = new ArrayList<NewsfeedModel>();
 
        listAdapter = new NewsfeedAdapter(getActivity(), feedItems);
        listView.setAdapter(listAdapter);
        
        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONArray(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
 
        } else {
            // making fresh volley request and getting json
            JsonArrayRequest jsonReq = new JsonArrayRequest(
                    URL_FEED, new Response.Listener<JSONArray>() {
 
                        @Override
                        public void onResponse(JSONArray response) {
                            VolleyLog.d(TAG, "Response: " + response.toString());
                            if (response != null) {
                                parseJsonFeed(response);
                            }
                        }
                    }, new Response.ErrorListener() {
 
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    });
 
            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
        return rootView;
 
    }
 
    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONArray feedArray) {
        try {
            //JSONArray feedArray = response.getJSONArray("feed");
 
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
 
                NewsfeedModel item = new NewsfeedModel();
                item.setId(feedObj.getString("NewsId"));
                item.setTitle(feedObj.getString("Title"));
 
                // Image might be null sometimes
                String image = feedObj.isNull("ImageUrl") ? null : feedObj
                        .getString("ImageUrl");
                item.setImage(image);
                item.setNewsContent(feedObj.getString("NewsContent"));
                item.setProfilePic(feedObj.getString("ImageUrl"));
                item.setTimeStamp(feedObj.getString("PublishedOn"));
                item.setCategory(feedObj.getInt("Category"));
 
                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);
 
                feedItems.add(item);
            }
 
            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
 
    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);										
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);		
		inflater.inflate(R.menu.menu, menu);
		 	    
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(Menus.SEARCH));
	    searchView.setQueryHint(this.getString(R.string.search));
	    
	    ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
        .setHintTextColor(getResources().getColor(R.color.white));	    
	    searchView.setOnQueryTextListener(OnQuerySearchView);
					    	   	    
		menu.findItem(Menus.ADD).setVisible(true);
		menu.findItem(Menus.UPDATE).setVisible(false);		
		menu.findItem(Menus.SEARCH).setVisible(true);
  	    
		mSearchCheck = false;	
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		switch (item.getItemId()) {

		case Menus.ADD:	    	
			break;				
		
		case Menus.UPDATE:	    	
			break;				
			
		case Menus.SEARCH:
			mSearchCheck = true;
			break;
		}		
		return true;
	}	
	
	private OnQueryTextListener OnQuerySearchView = new OnQueryTextListener() {
		
		@Override
		public boolean onQueryTextSubmit(String arg0) {
			return false;
		}
		
		@Override
		public boolean onQueryTextChange(String arg0) {
			if (mSearchCheck){
				// implement your search here
			}
			return false;
		}
	};
 
}