package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbPicService;
import org.core.mybatis.mapper.TbPicMapper;
import org.core.mybatis.pojo.TbPicPojo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 音乐专辑歌单封面表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbPicServiceImpl extends ServiceImpl<TbPicMapper, TbPicPojo> implements TbPicService {
    
    /**
     * 获取封面信息
     *
     * @param path 封面路径
     * @return 封面信息
     */
    @Override
    public TbPicPojo getPicResourceByPath(String path) {
        return this.getOne(Wrappers.<TbPicPojo>lambdaQuery().eq(TbPicPojo::getPath, path));
    }
    
    /**
     * 获取封面数据
     *
     * @param paths 封面路径
     * @return 封面数据
     */
    @Override
    public List<TbPicPojo> getResourceByPath(List<String> paths) {
        return this.list(Wrappers.<TbPicPojo>lambdaQuery().in(TbPicPojo::getPath, paths));
    }
}
