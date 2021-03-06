package com.paulshantanu.bputapp;

/*
 * This activity display those notices that are in HTML format. The notice is displayed in a textview
 * "main_tv". The HTML body of the notice is downloaded and parsed using XMLParser.java with
 *  the help of SaxParserHandler.java handler class.
 *  
 *  The tablelayout is used to display the table present in the notice.
 *  
 *  TODO: UI Enhancements, Code Optimizations
*/


import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TableRow.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class NoticeAcitivity extends ActionBarActivity implements AsyncTaskListener{

	SaxParserHandler notice_handler;
	StringBuffer str = new StringBuffer();
	String url;
	TextView main_tv;
	TableLayout tl;
    ProgressBar mProgressBar;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setSubtitle("View Notice");

		main_tv = (TextView) findViewById(R.id.notice_text);
		tl = (TableLayout) findViewById(R.id.notice_table);

        mProgressBar = (ProgressBar)findViewById(R.id.progressBar3);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);

		notice_handler = new SaxParserHandler();

		url = getIntent().getExtras().getString("link").trim();

		new XMLParser(this, notice_handler,url).execute("http://paul-shantanu-bputapp.appspot.com/notice.php");
	}

	public void onTaskComplete(String result) {

		 if(result.equals("OK")){
                mProgressBar.setVisibility(View.INVISIBLE);
				main_tv.setText(notice_handler.getNotice().getNotice_body());
				if(notice_handler.getNotice().getHas_table()==true)
					showTable();
			}
	 }
		
		
		// Method to display table if one exists.
	public void showTable() {	
		int columns = notice_handler.getNotice().getTable_head().size();
		int rows = (notice_handler.getNotice().getTable_body().size())/columns;
		int counter =0;
		TableRow head_tr = new TableRow(this);
		head_tr.setId(0);
        head_tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
            for(int i=0;i<columns;i++)
            {
            	TextView tv = new TextView(this);
            	tv.setId(200+i);
                tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                tv.setSingleLine(false);
            	tv.setText(notice_handler.getNotice().getTable_head().get(i));
            	head_tr.addView(tv);
            	
            }
            tl.addView(head_tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));


    		for(int i=0;i<rows-1;i++){
    			// Create table rows
    			TableRow tr = new TableRow(this);
    			tr.setId(100+i);
                
                for(int j=0;j<columns;j++){
                	TextView tv = new TextView(this);
                	tv.setId(200+i);
                    tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
                    tv.setSingleLine(false);
                	tv.setText(notice_handler.getNotice().getTable_body().get(counter));
                	tr.addView(tv);
                	counter++;
                }
                tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
    		}		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	switch (item.getItemId()) {
		case R.id.action_settings:		
				
			return true;
			
		case R.id.about:	
             AlertDialog.Builder b = new AlertDialog.Builder(this);
             b.setTitle("About");
             
             WebView about_view = new WebView(this);
             about_view.loadUrl("file:///android_asset/about.htm");
             
             b.setView(about_view);
             b.setPositiveButton("OK", null);
             b.create().show();
			return true;
			
		case android.R.id.home:
			 NavUtils.navigateUpFromSameTask(this);
		        return true;

		default:
	    	return super.onOptionsItemSelected(item);
		}
    }
}




