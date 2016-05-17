package com.helpfooter.steve.petcure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.helpfooter.steve.petcure.common.StaticVar;
import com.helpfooter.steve.petcure.mgr.ActivityMgr;

public class GeneralTextActivity extends AppCompatActivity {

    String code;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        this.code = intent.getStringExtra("code");
        this.title = intent.getStringExtra("title");

        this.setTitle(title);

        ((WebView) findViewById(R.id.txtContext)).loadUrl(StaticVar.GeneralTextUrl + code);
        ((WebView) findViewById(R.id.txtContext)).setBackgroundColor(0);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
