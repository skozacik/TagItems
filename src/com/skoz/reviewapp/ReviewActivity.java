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
import java.util.Collection;
import android.util.Log;

public class ReviewActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button ScanButton =(Button)findViewById(R.id.button1);
        ScanButton.setOnClickListener(mAddOnClickListener);
	Button SearchButton = (Button)findViewById(R.id.searchButton);
	SearchButton.setOnClickListener(mSearchOnClickListener);
    }
    ArrayList<String> Tags=new ArrayList<String>();
    String bc;
    private OnClickListener mAddOnClickListener =new OnClickListener() {
    	public void onClick (View v)  	{
	    IntentIntegrator integrator = new IntentIntegrator(ReviewActivity.this);
	    ArrayList<String> scanModes = new ArrayList<String>();
	    scanModes.add("PRODUCT");
	    integrator.initiateScan(scanModes);
	   }
    
    };
    private OnClickListener mSearchOnClickListener =new OnClickListener() {
    	public void onClick (View v)  	{
    		    
	    EditText SearchBar = (EditText)findViewById(R.id.editText1);
	    Intent searchIntent =new Intent(ReviewActivity.this,com.skoz.reviewapp.SearchActivity.class);
	    String searchTerms = "name="+SearchBar.getText().toString();
	    SearchBar.setText("") ;
	    SearchBar.setHint("search another item");
	    Log.e("searchTerms",searchTerms);
	    searchIntent.putExtra("terms",searchTerms);
	    startActivity(searchIntent) ;
	    
    		
	}
    
    };
   
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent )//make sure to have lowercase O
    { 	
    	IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
	
    
	if (result != null) {
	    String contents=result.getContents();
        
	    if (contents !=null) {
		showDialog(R.string.result_suceeded,contents); 	
		Context context = getApplicationContext();
		
		Intent resultIntent=new Intent(this,com.skoz.reviewapp.ResultActivity.class);
		resultIntent.putExtra("contents",contents);
		startActivity(resultIntent);
		
	    }else {
		
		showDialog(R.string.result_failed, "clownshoes");
	    }

	}
    }
    private void showDialog(int title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }    
}
