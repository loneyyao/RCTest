package com.sky.flower.rctest.custom.extension;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imkit.widget.provider.LocationPlugin;
import io.rong.imlib.model.Conversation;

public class MyExtensionModule extends DefaultExtensionModule {

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        //   List<IPluginModule> pluginModules =  super.getPluginModules(conversationType);

        // 自定义排序
        List<IPluginModule> pluginModules = new ArrayList<>();
        pluginModules.add(new MyPlugin());
        pluginModules.add(new ImagePlugin());
        pluginModules.add(new FilePlugin());
        pluginModules.add(new LocationPlugin());
        return pluginModules;
    }


    @Override
    public List<IEmoticonTab> getEmoticonTabs() {
        List<IEmoticonTab> emoticonTabs = super.getEmoticonTabs();

        emoticonTabs.add(new MyEmotionTab());

        return emoticonTabs;
    }
}
