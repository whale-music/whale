package org.plugin.service.impl;

import org.core.common.constant.PluginConstant;
import org.plugin.common.ComboSearchPlugin;
import org.plugin.converter.PluginLabelValue;

import java.util.List;

public class ComboSearchMusicPlugin implements ComboSearchPlugin {
    
    
    /**
     * 获取插件类型
     * 普通插件
     * 交互插件
     * 聚合插件
     *
     * @return 插件类型
     */
    @Override
    public String getType() {
        return PluginConstant.INTERACTIVE;
    }
    
    /**
     * 获取插件调用参数
     *
     * @return 参数
     */
    @Override
    public List<PluginLabelValue> getParams() {
        return null;
    }
    
    /**
     * 搜索
     * 返回值label已html解析
     *
     * @param params 运行参数
     * @param name   关键词
     * @return 搜索到的数据
     */
    @Override
    public List<PluginLabelValue> search(List<PluginLabelValue> params, String name) {
        return null;
    }
    
    /**
     * 需要同步的数据
     * 返回结果以html解析
     * 可以使用默认返回ok和error前端会自动解析
     *
     * @param data          数据
     * @param type          ID类型 可能没有。需要自行判断,类型可能是Music ID Album ID Artist ID
     * @param id            需要同步的ID。可能没有,需要自行判断。
     * @param pluginPackage 插件服务
     * @return 返回成功或失败数据。
     */
    @Override
    public String sync(List<PluginLabelValue> data, String type, Long id, PluginPackage pluginPackage) {
        return null;
    }
}
