package com.skoz.reviewapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import org.json.JSONException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity; 
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.RatingBar;

import android.util.Log;

public class ResultActivity extends Activity {
    /** Called when the activity is first created. */
    
    public  class mReviewAddOnClickListener implements OnClickListener { 
	private String rating;
	private String bc;
	public mReviewAddOnClickListener(String  rating, String bc){
	    this.rating=rating;
	    this.bc=bc;
	     
	}
	public void onClick (View v)
	{   
	    try {
		Intent tagIntent = new Intent(ResultActivity.this,com.skoz.reviewapp.AddReviewActivity.class);
	    	tagIntent.putExtra("bc",this.bc);
		tagIntent.putExtra("rating",this.rating);
		setContentView(R.layout.tagview);
		startActivity(tagIntent);
		
		
		
	    }catch (Exception e) {
		e.printStackTrace();
	    }
		
	    
	}
    }
    private OnItemClickListener  mItemOnClickListener = new OnItemClickListener(){
	    public void onItemClick (AdapterView<?> parent, View v,int position, long id){
		String listTag = parent.getItemAtPosition(position).toString();
		Intent tagSearchIntent = new Intent(ResultActivity.this,com.skoz.reviewapp.SearchActivity.class);
		tagSearchIntent.putExtra("terms","review__tag=" +listTag);
		startActivity(tagSearchIntent);
	    }
	};
   @Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	setContentView(R.layout.resultview);
	Intent resultIntent=getIntent();
	String bc = resultIntent.getStringExtra("contents");
	String urlstring=this.getString(R.string.serverurl)+"/items/"+bc;
	TextView tv=(TextView)findViewById(R.id.textView2);
	try {
	    URL url= new URL(urlstring);
	    HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();
	    BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    ImageView imview=(ImageView)findViewById(R.id.imageView1);
	    
	    while ((line = reader.readLine())!=null){
		sb.append(line + "\n");
	    }
	    in.close();
	    String webresult = sb.toString();
	    JSONArray jsonwebresult;
	    jsonwebresult = new JSONArray(webresult);
	    JSONObject jswebresult = jsonwebresult.getJSONObject(0);
	    JSONObject fields = jswebresult.getJSONObject("fields");
	    String name = fields.getString("name");
	    String imgurl =fields.getString("imageurl");
	    String rating = fields.getString("rating");
	    
	    JSONArray JSONTags=fields.getJSONArray("review");
	    ArrayList<String> Server_Tags = new ArrayList<String>();
	    for (int i=0; i < JSONTags.length(); i++){
		JSONArray tagtuple=JSONTags.getJSONArray(i);
		String tag = tagtuple.getString(0);
		Server_Tags.add(tag);
	    }
	    GridView lv = (GridView) findViewById(R.id.GridView1);
	    ListAdapter la = new ArrayAdapter<String>(this,R.layout.tagview,R.id.textView3,Server_Tags);
	    lv.setAdapter(la);
	    lv.setOnItemClickListener(mItemOnClickListener);
	    urlConnection.disconnect();
	    URL imgURL=new URL(imgurl);
	    InputStream imagestream = (InputStream)imgURL.getContent();
	    Bitmap img= BitmapFactory.decodeStream(imagestream);
	    imview.setImageBitmap(img);
	    tv.setText(name);
	    Button reviewbutton = (Button) findViewById(R.id.button1);
	    reviewbutton.setOnClickListener( new mReviewAddOnClickListener(rating,bc));
					     
	}catch (IOException e){
	    e.printStackTrace();
	    tv.setText("404 @" + urlstring);
	}catch ( JSONException e2){
	    e2.printStackTrace();
	    tv.setText("Google Shopper Doesn't Have your Product");
	} 
    }
}
