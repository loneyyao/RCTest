package com.sky.flower.rctest.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.sky.flower.rctest.R;

import io.rong.imlib.model.Conversation;

import static io.rong.imlib.model.Conversation.*;

public class SubConversationListActivtiy extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subconversationlist);
        Intent intent = getIntent();
        String type = intent.getData().getQueryParameter("type");
        ActionBar actionBar = getActionBar();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        switch (type) {
            case "private":
                actionBar.setTitle("单聊");
                break;
            case "group":
                actionBar.setTitle("群聊");
                break;
            default:
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
