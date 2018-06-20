package com.sky.flower.rctest.custom.extension;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.sky.flower.rctest.R;
import com.sky.flower.rctest.application.App;
import com.sky.flower.rctest.custom.message.MyContactMessage;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

public class MyPlugin implements IPluginModule {


    private Context mContext;
    private String targetId;
    private Conversation.ConversationType conversationType;

    @Override
    public Drawable obtainDrawable(Context context) {
        this.mContext = context;
        return context.getDrawable(R.drawable.phonebook_selector);
    }

    @Override
    public String obtainTitle(Context context) {
        return "通讯录";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        conversationType = rongExtension.getConversationType();
        targetId = rongExtension.getTargetId();
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        rongExtension.startActivityForPluginResult(intent, 132, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 132:
                if (data == null) {
                    return;
                }
                //处理返回的data,获取选择的联系人信息
                Uri uri = data.getData();
                String[] contacts = getPhoneContacts(uri);
                String name = contacts[0];
                String phone = contacts[1];
                Message message = Message.obtain(targetId, conversationType,new MyContactMessage(name,phone));


                RongIM.getInstance().sendMessage(message, name+":::"+phone, "", new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                        Log.d("LIZEJUN:message...","onAttached");
                    }

                    @Override
                    public void onSuccess(Message message) {
                        Log.d("LIZEJUN:message...","onSuccess");
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        Log.d("LIZEJUN:message...","onError");
                    }
                });

                break;
        }
    }

    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = mContext.getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }
}
