package com.sky.flower.rctest.custom.message;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.flower.rctest.R;
import com.sky.flower.rctest.application.App;

import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;


@ProviderTag(messageContent = MyContactMessage.class)
public class MyContactMessageProvider extends IContainerItemProvider.MessageProvider<MyContactMessage> {

    class ViewHolder {
        TextView name;
        TextView call;
        View view;
    }


    @Override
    public void bindView(View view, int i, MyContactMessage myContactMessage, UIMessage message) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.view.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            holder.view.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        holder.name.setText(myContactMessage.getName());
        holder.call.setText(myContactMessage.getCallNum());
    }

    @Override
    public Spannable getContentSummary(MyContactMessage myContactMessage) {
        return new SpannableString("这是一条自定义消息CustomizeMessage");
    }

    @Override
    public void onItemLongClick(View view, int position, MyContactMessage content, UIMessage message) {
        super.onItemLongClick(view, position, content, message);
        Toast.makeText(App.getApplication(),content.getName()+":"+content.getCallNum(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View view, int i, MyContactMessage myContactMessage, UIMessage uiMessage) {
        Uri uri = Uri.parse("tel:" + myContactMessage.getCallNum());
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        view.getContext().startActivity(intent);
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_contact_message_layout, null);
        ViewHolder holder = new ViewHolder();
        holder.name = view.findViewById(R.id.text_name);
        holder.call = view.findViewById(R.id.text_call);
        holder.view = view.findViewById(R.id.view_root);
        view.setTag(holder);
        return view;
    }
}
