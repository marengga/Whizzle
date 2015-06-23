package com.marengga.whizzle.fragments;

import static org.quaere.DSL.from;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.quaere.Group;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.marengga.whizzle.R;
import com.marengga.whizzle.utils.Constant;
import com.marengga.whizzle.utils.Menus;
import com.marengga.whizzle.volley.LruBitmapCache;
import com.marengga.whizzle.data.DatabaseHelper;
import com.marengga.whizzle.data.LibraryModel;
import com.marengga.whizzle.adapter.LibraryHorizontalListViewAdapter;
import com.marengga.whizzle.app.AppController;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LibraryFragment extends Fragment {
	private boolean mSearchCheck;
	private VerticalAdapter verListAdapter;
	DatabaseHelper db;
	private String URL_LIBRARY = Constant.BASE_API_URL+"library/";
	private String URL_LIBRARY_DOWNLOAD = Constant.BASE_LIBRARY_URL;
	private String LIBRARY_FOLDER = Environment.getExternalStorageDirectory().getPath() + "/Whizzle/Library/";
	private ProgressDialog pDialog;
	private LruBitmapCache mLruBitmapCache;
	
	public LibraryFragment newInstance(String text){
		LibraryFragment mFragment = new LibraryFragment();		
		Bundle mBundle = new Bundle();
		mBundle.putString(Constant.TEXT_FRAGMENT, text);
		mFragment.setArguments(mBundle);
		return mFragment;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		View rootView = inflater.inflate(R.layout.library_fragment, container, false);
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		db = DatabaseHelper.getInstance(getActivity());
		ArrayList<LibraryModel> allLibrary = db.getAllLibrary();
		ArrayList<ArrayList<LibraryModel>> groupList = groupLibray(allLibrary);
		verListAdapter = new VerticalAdapter(getActivity(), R.layout.library_row, groupList);
		ListView list = (ListView)getView().findViewById(R.id.librarylist);
		list.setAdapter(verListAdapter);				
		verListAdapter.notifyDataSetChanged();
		
		mLruBitmapCache = new LruBitmapCache();
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
	    
	    menu.findItem(Menus.ADD).setVisible(false);
		menu.findItem(Menus.UPDATE).setVisible(true);		
		menu.findItem(Menus.SEARCH).setVisible(false);
  	    
		mSearchCheck = false;	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case Menus.ADD:	    	
			break;
		
		case Menus.UPDATE:
			getBookList();
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
	
	/**
	 * This class add a list of ArrayList to ListView that it include multi
	 * items as bookItem.
	 */
	private class VerticalAdapter extends ArrayAdapter<ArrayList<LibraryModel>> {

		private int resource;

		public VerticalAdapter(Context _context, int _ResourceId,
				ArrayList<ArrayList<LibraryModel>> _items) {
			super(_context, _ResourceId, _items);
			this.resource = _ResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final View rowView;

			if (convertView == null) {
				rowView = LayoutInflater.from(getContext()).inflate(resource,null);
			} else {
				rowView = convertView;
			}

			LibraryHorizontalListViewAdapter hListView = (LibraryHorizontalListViewAdapter) rowView
					.findViewById(R.id.subListview);
			HorizontalAdapter horListAdapter = new HorizontalAdapter(
					getContext(), R.layout.library_item, getItem(position));
			hListView.setAdapter(horListAdapter);
			
			hListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					LinearLayout x = (LinearLayout) view;
				    ImageView t = (ImageView) x.findViewById(R.id.bookCover);
				    LibraryModel l =  (LibraryModel) t.getTag();
				    final String downloadUrl = URL_LIBRARY_DOWNLOAD + l.getLibraryId() + ".pdf";
				    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
						.setTitle(l.getTitle())
						.setMessage(l.getDescription()+"\n\nAuthor : "+l.getAuthor())
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
							// Do nothing.
							}}
						);
				    final File file = new File(LIBRARY_FOLDER+ "/" + l.getLibraryId() + ".pdf");
				    if(!file.exists())
				    	dialog.setPositiveButton("Download", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								new DownloadFileFromURL().execute(downloadUrl);
							}}
						);
				    else
				    	dialog.setPositiveButton("Read", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								Intent target = new Intent(Intent.ACTION_VIEW);
								target.setDataAndType(Uri.fromFile(file),"application/pdf");
								target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

								Intent intent = Intent.createChooser(target, "Open File");
								try {
									startActivity(intent);
								} catch (ActivityNotFoundException e) {
									Toast.makeText(getActivity(), "No PDF reader found. Please download a PDF reader from PlayStore", Toast.LENGTH_LONG).show();
								}   
							}}
						);
				    dialog.show();
				}
			});

			return rowView;
		}
	}
	
	/**
	 * This class add some items to Horizontal ListView this ListView include
	 * several bookItem.
	 */
	@SuppressLint("ViewHolder")
	private class HorizontalAdapter extends ArrayAdapter<LibraryModel> {

		private int resource;

		public HorizontalAdapter(Context _context, int _textViewResourceId,
				ArrayList<LibraryModel> _items) {
			super(_context, _textViewResourceId, _items);
			this.resource = _textViewResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(getContext()).inflate(this.resource, null);
			ImageView bookCover = (ImageView) retval.findViewById(R.id.bookCover);
			try{
				String imgKey = getItem(position).getLibraryId().toString();
				if(mLruBitmapCache.getBitmap(imgKey) == null){
					byte[] imageBytes = getItem(position).getCover();
					Bitmap pic = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
					mLruBitmapCache.putBitmap(imgKey, pic);
					//bookCover.setImageBitmap(pic);
					bookCover.setImageBitmap(mLruBitmapCache.getBitmap(imgKey));
				}
				else{
					bookCover.setImageBitmap(mLruBitmapCache.get(imgKey));
				}
			}catch(Exception e){
				Log.e("LibraryFragment", "Error : " + e.getMessage());
			}
			LibraryModel l = getItem(position);
			l.setCover(null); // biar lebih enteng
			bookCover.setTag(l);
			
			return retval;
		}
	}
	
	private ArrayList<ArrayList<LibraryModel>> groupLibray(ArrayList<LibraryModel> allLibrary){
		ArrayList<ArrayList<LibraryModel>> groupList = new ArrayList<ArrayList<LibraryModel>>();
		
		Iterable<Group> groups =
			from("library").in(allLibrary).
			group("library").by("library.getCategory()").into("g").
			select("g");
		
		for (Group group : groups) {		    
		    ArrayList<LibraryModel> obj = new ArrayList<LibraryModel>();
			for (Object Item : group.getGroup()) {
				obj.add((LibraryModel) Item);
			}
			groupList.add(obj);
		}
		return groupList;
	}
	
	private void getBookList(){
		// Tag used to cancel the request
		String tag_json_arry = "json_array_req";
		         
		final ProgressDialog pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading...");
		pDialog.show();

		JsonArrayRequest req = new JsonArrayRequest(
			URL_LIBRARY,
			new Response.Listener<JSONArray>() {
	            @Override
	            public void onResponse(JSONArray response) {
	                Log.d("VOLLEY", "Udah dapet respon dari server library : " + response.toString());
	                pDialog.hide();
	                new HandleResponseAsyncTask().execute(response);
	            }
	        },
	        new Response.ErrorListener() {
	            @Override
	            public void onErrorResponse(VolleyError error) {
	                Log.e("VOLLEY", "Error waktu ngrefresh library : " + error.getMessage());
	                pDialog.hide();
	                Toast.makeText(getActivity(), "An error occured : " + error.getMessage(), Toast.LENGTH_LONG).show();
	                
	                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
	                    Toast.makeText(getActivity(),"Timeout / Connection Error",Toast.LENGTH_LONG).show();
	                } else if (error instanceof AuthFailureError) {
	                	Toast.makeText(getActivity(),"Authentication Error",Toast.LENGTH_LONG).show();
	                } else if (error instanceof ServerError) {
	                	Toast.makeText(getActivity(),"Server Error",Toast.LENGTH_LONG).show();
	                } else if (error instanceof NetworkError) {
	                	Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_LONG).show();
	                } else if (error instanceof ParseError) {
	                	Toast.makeText(getActivity(),"Parse Error",Toast.LENGTH_LONG).show();
	                }
	            }
	        });
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(req, tag_json_arry);
	}
	
	private class HandleResponseAsyncTask extends AsyncTask < JSONArray, String, Void > {
		@Override
		protected void onPreExecute() {
			//mDataSource.open();
		}

		@Override
		protected Void doInBackground(JSONArray...params) {
			publishProgress("Saving data...");
			
			DefaultHttpClient mHttpClient = new DefaultHttpClient();
			HttpGet mHttpGet;
			HttpResponse mHttpResponse;
			JSONArray objects = params[0];
			
			for (int i = 0; i < objects.length(); i++) {
				JSONObject o = objects.optJSONObject(i);
				LibraryModel lib = new LibraryModel();
				try {
					lib.setAuthor(o.getString("Author"));
					lib.setCategory(o.getInt("Category"));
					lib.setDescription(o.getString("Description"));
					lib.setLibraryId(o.getString("LibraryId"));
					lib.setTitle(o.getString("Title"));
					
					mHttpGet = new HttpGet(o.getString("ImageUrl"));
					mHttpResponse = mHttpClient.execute(mHttpGet);
					if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						HttpEntity entity = mHttpResponse.getEntity();  
					    if ( entity != null) {
					    	lib.setCover(EntityUtils.toByteArray(entity));
					    }
					}
				} catch (JSONException e) {
					Toast.makeText(getActivity(), "A problem has occured while refreshing library. " +
						e.getMessage(), Toast.LENGTH_SHORT).show();
				} catch (ClientProtocolException e) {
					Toast.makeText(getActivity(), "A problem has occured while refreshing library. " +
							e.getMessage(), Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					Toast.makeText(getActivity(), "A problem has occured while refreshing library. " +
							e.getMessage(), Toast.LENGTH_SHORT).show();
				}
				DatabaseHelper.getInstance(getActivity()).createLibrary(lib);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String...messages) {
			//mTextViewLoading.setText(messages[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			DatabaseHelper.getInstance(getActivity()).closeDB();
			onActivityCreated(null);
		}
	}
	
	private class DownloadFileFromURL extends AsyncTask<String, String, String> {
		
/**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Downloading file. Please wait...");
			pDialog.setIndeterminate(false);
			//pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
         * Downloading file in background thread
         * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(f_url[0]);
				//url = new URL("http://www.worldbank.org/content/dam/Worldbank/document/EAP/Indonesia/Indonesia-Beyond-2015.pdf");
				HttpURLConnection conection = (HttpURLConnection) url.openConnection();
				conection.connect();
				int lenghtOfFile = conection.getContentLength() / 1024;
				pDialog.setMax(lenghtOfFile);
				pDialog.setProgressNumberFormat("%1d/%2d KB");
				
				File folder = new File(LIBRARY_FOLDER);
				folder.mkdirs();
				String [] urls = url.toString().split("/");
				String filename = urls[urls.length-1];
				
				Log.d("Download Library", "Downloading file from " + url.openStream() + ". Will be saved to " + folder +"/"+ filename);
				
				BufferedInputStream input = new BufferedInputStream(url.openStream(), 8192);
				OutputStream output = new FileOutputStream(folder +"/"+ filename);
				byte data[] = new byte[1024];
				long total = 0;
				
				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + total/1024);
					output.write(data, 0, count);
				}
				// flushing output
				output.flush();
				// closing streams
				output.close();
				input.close();
			}catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}				
			return null;
		}
		/**
		* Updating progress bar
		* */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			pDialog.setProgress(Integer.parseInt(progress[0]));
		}
		
		/**
		* After completing background task
		* Dismiss the progress dialog
		* **/
		@Override
		protected void onPostExecute(String file_url) {
			SystemClock.sleep(3000);
			pDialog.dismiss();
		}
	}
}
