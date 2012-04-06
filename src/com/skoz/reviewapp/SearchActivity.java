package com.skoz.reviewapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.ListView;
import android.util.Log;
public class SearchActivity extends ListActivity{
    private ArrayList<String> bcs = new ArrayList<String>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	String urlstring=this.getString(R.string.serverurl)+"/items/search/";
	ArrayList<String> Items = new ArrayList<String>();
	Intent search = getIntent();
	String pdata = search.getStringExtra("terms");
	ListView lv = this.getListView();
	lv.setOnItemClickListener(searchListOnClickListener);
	
	try { 
	    URL url = new URL(urlstring);
	    HttpURLConnection httpCnx = (HttpURLConnection) url.openConnection();
	    httpCnx.setDoOutput(true) ;
	    httpCnx.setFixedLengthStreamingMode(pdata.getBytes().length);
	    OutputStreamWriter outWriter  = new OutputStreamWriter(httpCnx.getOutputStream());
	    outWriter.write(pdata);
	    outWriter.close();
	    BufferedInputStream in = new BufferedInputStream(httpCnx.getInputStream());
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in) ) ;
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	  
	    while (( line = reader.readLine()) != null){
		sb.append(line + "\n") ;
	    }
	    in.close();
	    httpCnx.disconnect();
	    
	    String webresult = sb.toString();
	    
	    JSONArray JSONwebResult  = new JSONArray(webresult);
	    JSONObject JSwebResult = JSONwebResult.getJSONObject(0);
	    JSONObject fields = JSwebResult.getJSONObject("fields") ;

	    
	    String Item;
	    String bc; 
	    for (int i = 0 ;i < JSONwebResult.length(); i++){
		JSwebResult = JSONwebResult.getJSONObject(i);
		fields = JSwebResult.getJSONObject("fields");
		Item = fields.getString("name") ;
		bc = fields.getString("bc") ;
		Log.d("bc",bc);
		Items.add(Item);
		bcs.add(bc);
	    } 
	} catch (JSONException e) {
	    Items.add("No Matches Found");
	    Log.e("post data",pdata);
	    e.printStackTrace();
	}
	catch (Exception e2) {
	    e2.printStackTrace();
	    finish();
	}
	this.setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1,Items));

	
    }
    private OnItemClickListener searchListOnClickListener = new OnItemClickListener(){
	    public void onItemClick (AdapterView<?> parent, View v, int position, long id) {
		Intent resultIntent = new Intent(SearchActivity.this, com.skoz.reviewapp.ResultActivity.class);
		resultIntent.putExtra("contents",SearchActivity.this.bcs.get(position));
		Log.d("barcodes",SearchActivity.this.bcs.get(0) );
		Log.d("position", Integer.toString(position)) ;
		startActivity(resultIntent);
	    }
	};
}
			     	    
