package com.sky.flower.rctest.custom.extension;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.sky.flower.rctest.R;
import com.sky.flower.rctest.activity.ConversationActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;

public class MyEmotionTab implements IEmoticonTab {
    @Override
    public Drawable obtainTabDrawable(Context context) {
        return context.getDrawable(R.drawable.my_emotion);
    }

    @Override
    public View obtainTabPager(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_emotion_tab, null);
        final ImageView myEmotion = view.findViewById(R.id.my_emotion);
        myEmotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConversationActivity cat =  (ConversationActivity)context;
                InputStream is = context.getResources().openRawResource(R.drawable.my_emotion);
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                String defaultPath = context.getFilesDir()
                        .getAbsolutePath() + "/defaultGoodInfo";
                File file = new File(defaultPath);
                if (!file.exists()) {
                    file.mkdirs();
                    String defaultImgPath = defaultPath + "/my_emotion.png";
                    file = new File(defaultImgPath);
                    try {
                        file.createNewFile();
                        FileOutputStream fOut = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 20, fOut);
                        is.close();
                        fOut.flush();
                        fOut.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    String defaultImgPath = defaultPath + "/my_emotion.png";
                    file = new File(defaultImgPath);
                }



                // 7.0以上未做fileprovider适配
                Uri uri = Uri.fromFile(file);

                ImageMessage imageMessage = ImageMessage.obtain(uri, uri);

                RongIM.getInstance().sendImageMessage(cat.getConversationType(),cat.getTargetId(), imageMessage, null, null, new RongIMClient.SendImageMessageCallback() {

                    @Override
                    public void onAttached(Message message) {
                        //保存数据库成功
                        Log.d("hahaha", "onAttached");
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode code) {
                        //发送失败
                        Log.d("hahaha", "onError");

                    }

                    @Override
                    public void onSuccess(Message message) {
                        //发送成功
                        Log.d("hahaha", "onSuccess");

                    }

                    @Override
                    public void onProgress(Message message, int progress) {
                        //发送进度
                        Log.d("hahaha", "onProgress");

                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onTableSelected(int i) {

    }
}
