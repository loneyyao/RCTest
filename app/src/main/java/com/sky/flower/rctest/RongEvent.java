package com.sky.flower.rctest;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import com.sky.flower.rctest.application.App;
import com.sky.flower.rctest.custom.extension.MyExtensionModule;
import com.sky.flower.rctest.custom.message.MyContactMessage;
import com.sky.flower.rctest.custom.message.MyContactMessageProvider;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

public class RongEvent {
    private static RongEvent instance;
    private Context mContext;

    private RongEvent() {
        this.mContext = App.getApplication();
    }

    public static RongEvent getInstance() {
        if (instance == null) {
            synchronized (RongEvent.class) {
                if (instance == null) {
                    instance = new RongEvent();
                }
            }
        }
        return instance;
    }

    public void connect(final String token, final ConnectCallBack callback) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                callback.onTokenIncorrect();
            }

            @Override
            public void onSuccess(String s) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("login",mContext.MODE_PRIVATE).edit();
                editor.putString("token",token);
                editor.apply();

                RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
                RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
                //放入假的联系人和群数据
                initFakeData();
                callback.onsuccess(s);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                //记录错误
                //重新huoqu token
                Toast.makeText(mContext, "errorCode:" + errorCode, Toast.LENGTH_SHORT).show();
                callback.onError(errorCode);
            }
        });
    }

    private void initFakeData() {
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String userID) {
                if (userID.equals("95588")) {
                    return new UserInfo(userID, "工商银行", Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528174510180&di=ddd90d2ece52d80ca11bd3002070d008&imgtype=0&src=http%3A%2F%2Fwww.289.com%2Fup%2Farticle%2F2016%2F0608%2F102819_41562844.jpg"));
                } else if (userID.equals("95566")) {
                    return new UserInfo(userID, "中国银行", Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528174512406&di=25c5723e89cf3d76ffc384480fdc182f&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D3862911550%2C1216904169%26fm%3D214%26gp%3D0.jpg"));
                }
                return null;
            }
        }, true);
        RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
            @Override
            public Group getGroupInfo(String groupId) {
                if (groupId.equals("999999")) {
                    return new Group(groupId, "军情观察处", Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528174510180&di=ddd90d2ece52d80ca11bd3002070d008&imgtype=0&src=http%3A%2F%2Fwww.289.com%2Fup%2Farticle%2F2016%2F0608%2F102819_41562844.jpg"));
                } else if (groupId.equals("888888")) {
                    return new Group(groupId, "五角大楼", Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528263319575&di=e600ce831940196f736a96ab1e0d6d2e&imgtype=0&src=http%3A%2F%2Fscimg.jb51.net%2Fallimg%2F160122%2F10-160122163410S7.jpg"));
                }
                return null;
            }
        }, true);
    }

    public void setMyExtensionModule() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
            }
        }
    }


    public void insertMyMessageType() {
        RongIM.registerMessageType(MyContactMessage.class);
        RongIM.registerMessageTemplate(new MyContactMessageProvider());
    }


    public void setHaveRead() {
        Conversation.ConversationType[] types = new Conversation.ConversationType[] {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.DISCUSSION
        };
        RongIM.getInstance().setReadReceiptConversationTypeList(types);
    }

    public interface ConnectCallBack {
        void onsuccess(String s);

        void onError(RongIMClient.ErrorCode errorCode);

        void onTokenIncorrect();
    }

}
