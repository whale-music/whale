package org.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.core.pojo.TbMusicArtistPojo;

/**
 * <p>
 * 音乐与歌手中间表 Mapper 接口
 * </p>
 *
 * @author Sakura
 * @since 2023-05-11
 */
@Mapper
public interface TbMusicArtistMapper extends BaseMapper<TbMusicArtistPojo> {

}
