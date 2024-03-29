package org.core.mybatis.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import org.core.mybatis.pojo.TbMvPojo;

/**
 * <p>
 * 音乐短片 服务类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
public interface TbMvService extends IService<TbMvPojo> {
    
    /**
     * 获取MV pojo
     *
     * @param path 路径
     * @return pojo
     */
    TbMvPojo getMvByPath(String path);
}
