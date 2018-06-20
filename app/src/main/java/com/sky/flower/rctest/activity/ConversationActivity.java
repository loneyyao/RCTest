package com.sky.flower.rctest.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;

import com.sky.flower.rctest.R;
import com.sky.flower.rctest.RongEvent;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

public class ConversationActivity extends FragmentActivity {

    private String mTargetId;
    private String mTargetIds;
    private Conversation.ConversationType mConversationType;
    private boolean isFromPush;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        getIntentDate(getIntent());
    }

    /**
     * 重连
     */
    private void reconnect() {
        SharedPreferences login = getSharedPreferences("login", MODE_PRIVATE);
        String token = login.getString("token", "dcUj49GLeHHrf5EVAKp5gMLdi3SClttIVU+oMsLEGpPcE4YonTP7ZCLmXnF+5cKOTUXgLSKLEFiygpP/2K22oq6XnmWsCWap");
        RongEvent.getInstance().connect(token, new RongEvent.ConnectCallBack() {
            @Override
            public void onsuccess(String s) {

                userId = s;
                enterFragment(mConversationType, mTargetId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }

            @Override
            public void onTokenIncorrect() {

            }
        });
    }

    public String getTargetId() {
        return mTargetId;
    }

    public String getTargetIds() {
        return mTargetIds;
    }

    public Conversation.ConversationType getConversationType() {
        return mConversationType;
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        isFromPush(intent);

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        final String title = intent.getData().getQueryParameter("title");
        getActionBar().setTitle(title);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        RongIMClient.ConnectionStatusListener.ConnectionStatus currentConnectionStatus = RongIM.getInstance().getCurrentConnectionStatus();
        if (!currentConnectionStatus.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
            reconnect();
        } else {
            enterFragment(mConversationType, mTargetId);
        }

        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (type.equals(mConversationType) && targetId.equals(mTargetId)) {
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    int count = typingStatusSet.size();
                    if (count > 0) {
                        Iterator iterator = typingStatusSet.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();

                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        if (objectName.equals(textTag.value())) {
                            //显示“对方正在输入”
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    getActionBar().setTitle(title+"   正在打字...");
                                }
                            });
                        } else if (objectName.equals(voiceTag.value())) {
                            //显示"对方正在讲话"
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getActionBar().setTitle(title+"   正在语音...");
                                }
                            });
                        }
                    } else {
                        //当前会话没有用户正在输入，标题栏仍显示原来标题
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getActionBar().setTitle(title);
                            }
                        });
                    }
                }
            }
        });

    }

    private void isFromPush(Intent intent) {
        if(intent == null || intent.getData() == null){
            return;
        }

        if(intent.getData().getScheme().equals("rong")&&intent.getData().getQueryParameter("isFromPush")!=null){
            if(intent.getData().getQueryParameter("isFromPush").equals("true")){
                isFromPush =true;
            }
        }
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
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

    @Override
    public void onBackPressed() {
        if(isFromPush){
            finish();
            if(userId==null){
                super.onBackPressed();
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("userId",userId);
            intent.setClass(this,MainActivity.class);
            startActivity(intent);
        }else{
            super.onBackPressed();
        }
    }
}
