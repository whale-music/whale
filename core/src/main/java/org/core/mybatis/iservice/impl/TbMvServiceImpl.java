package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbMvService;
import org.core.mybatis.mapper.TbMvMapper;
import org.core.mybatis.pojo.TbMvPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 音乐短片 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbMvServiceImpl extends ServiceImpl<TbMvMapper, TbMvPojo> implements TbMvService {
    
    /**
     * 获取MV pojo
     *
     * @param path 路径
     * @return pojo
     */
    @Override
    public TbMvPojo getMvByPath(String path) {
        return this.getOne(Wrappers.<TbMvPojo>lambdaQuery().eq(TbMvPojo::getPath, path));
    }
}
