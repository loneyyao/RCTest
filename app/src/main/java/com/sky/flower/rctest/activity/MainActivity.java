package com.sky.flower.rctest.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sky.flower.rctest.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String mUserId;
    private TextView userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initData();
    }

    private void initUI() {
        Button conversationList = findViewById(R.id.conversationList);
        conversationList.setOnClickListener(this);
        Button conversation = findViewById(R.id.conversation);
        conversation.setOnClickListener(this);
        Button updateUserInfo = findViewById(R.id.updateUserInfo);
        updateUserInfo.setOnClickListener(this);
        Button groupConversation = findViewById(R.id.groupConversation);
        groupConversation.setOnClickListener(this);
        Button groupConversation1 = findViewById(R.id.groupConversation1);
        groupConversation1.setOnClickListener(this);
        userInfo = findViewById(R.id.userInfo);
    }

    private void initData() {
        this.mUserId = getIntent().getStringExtra("userId");
        userInfo.setText(mUserId.equals("95588") ? "我是工商银行" : "我是中国银行");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.conversationList:
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startConversationList(MainActivity.this);
                }
                break;
            case R.id.conversation:
                if (RongIM.getInstance() != null) {
                    //指定聊天
                    RongIM.getInstance().startPrivateChat(MainActivity.this, mUserId.equals("95588") ? "95566" : "95588", mUserId.equals("95588") ? "中国银行" : "工商银行");
                }
                break;
            case R.id.updateUserInfo:
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo("95588", "新新新农业银行", Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528791070&di=c39a0e893a462ab64b48aacd30bdb432&imgtype=jpg&er=1&src=http%3A%2F%2Fupload.gezila.com%2Fdata%2F20150314%2F18821426309234.png")));
                }
                break;
            case R.id.groupConversation:
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startGroupChat(MainActivity.this, "999999", "军情观察处");
                }
                break;
            case R.id.groupConversation1:
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startGroupChat(MainActivity.this, "888888", "五角大楼");
                }
                break;
            default:
                break;
        }
    }
}
