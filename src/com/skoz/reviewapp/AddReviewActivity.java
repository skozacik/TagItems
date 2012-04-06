package com.skoz.reviewapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.widget.GridView;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
public class AddReviewActivity extends Activity implements OnClickListener{
   /** Called when the activity is first created. */
    String rating;
    String bc;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.reviewview);	
	Intent AddReviewIntent = getIntent();
	
	this.rating = AddReviewIntent.getStringExtra("rating");
	this.bc  = AddReviewIntent.getStringExtra("bc");

	
	Button TagButton=(Button)findViewById(R.id.tagbutton);
	Button SubmitButton=(Button)findViewById(R.id.button2);
	EditText tags=(EditText)findViewById(R.id.editText1);
	if (TagButton==null){
	    Log.d("log_tag","Tag Button pointer is null");
	}
	else if (SubmitButton==null) {
	    Log.d("log_tag","Submit Button is Null");
	}
	else if (tags == null){
	    Log.d("log_tag","tags is NULL");
	}
	TagButton.setOnClickListener(this);
	Log.e("log_tag","rating value"+rating);
	Log.e("log_tag","this rating value"+rating);
	float prev_rating = new Float(this.rating); 
	RatingBar rb =(RatingBar)findViewById(R.id.ratingBar1);
	
	if (rb == null){
	    Log.d("ratingnull","rating is null");
	}
	
	rb.setRating(prev_rating);
	SubmitButton.setOnClickListener(mSubmitOnClickListener);
    }
    public void onClick (View v)
    {               
	EditText tags=(EditText)findViewById(R.id.editText1);
	String tag=tags.getText().toString();
	tags.setText("");
	tags.setHint("Add Another Tag");
	tag="tag="+tag;
        try{
	    URL url =new URL(this.getString(R.string.serverurl)+"/items/"+bc+"/tagpost/");
	    HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
	    urlConnection.setDoOutput(true);
	    urlConnection.setFixedLengthStreamingMode(tag.getBytes().length); 
	    OutputStreamWriter outwriter=new OutputStreamWriter(urlConnection.getOutputStream());
	    outwriter.write(tag);
	    outwriter.close();
	    urlConnection.disconnect();
	}
	catch( IOException e)  {
	    tags.setText("No Dice");

	}

    }
    private OnClickListener mSubmitOnClickListener =new OnClickListener() {
	    public void onClick (View v)    {
		RatingBar rb =(RatingBar)findViewById(R.id.ratingBar1);



		float rating=rb.getRating();
		setContentView(R.layout.outputview);
		TextView tv=(TextView)findViewById(R.id.textView2);
		try{
		    URL url =new URL(R.string.serverurl+"/itemss/"+bc+"/rating/"+rating);
		    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		    //TODO 
		    //rather than passing rating in url, use put or post

		    BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
		    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		    StringBuilder sb = new StringBuilder();
		    String line = null;
		    while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		    }
		    in.close();

		    String webresult=sb.toString();
		    urlConnection.disconnect();
		    tv.setText(webresult);
		}catch (MalformedURLException e) {
		    
		    e.printStackTrace();

		    tv.setText("Malformed URL");
		}catch (IOException e2){
		    e2.printStackTrace();

		    tv.setText("IO exception");
		}

	    }

        };
}
