package com.sky.flower.rctest.custom.message;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;


@MessageTag(value = "app:custom", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class MyContactMessage extends MessageContent {


    private String name;//姓名
    private String callNum;//电话

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCallNum() {
        return callNum;
    }

    public void setCallNum(String callNum) {
        this.callNum = callNum;
    }

    public MyContactMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("name")){
                name = jsonObj.optString("name");
                callNum = jsonObj.optString("call");
            }

        } catch (JSONException e) {
            Log.e("MyContactMessage", "JSONException",e );
        }

    }


    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("name", name);
            jsonObj.put("call", callNum);
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }


    protected MyContactMessage(Parcel in){
        name = ParcelUtils.readFromParcel(in);
        callNum = ParcelUtils.readFromParcel(in);
    }

    public MyContactMessage(String name, String call){
       this.name = name;
       this.callNum = call;
    }


    /**
     * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
     *
     * @return 一个标志位，表明Parcelable对象特殊对象类型集合的排列。
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        ParcelUtils.writeToParcel(dest, name);//该类为工具类，对消息中属性进行序列化
        ParcelUtils.writeToParcel(dest, callNum);//该类为工具类，对消息中属性进行序列化

        //这里可继续增加你消息的属性
    }


    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<MyContactMessage> CREATOR = new Creator<MyContactMessage>() {
        @Override
        public MyContactMessage createFromParcel(Parcel parcel) {
            return new MyContactMessage(parcel);
        }

        @Override
        public MyContactMessage[] newArray(int i) {
            return new MyContactMessage[i];
        }
    };

}
