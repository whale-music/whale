package org.core.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 歌曲专辑表
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_album")
@ApiModel(value = "TbAlbumPojo对象", description = "歌曲专辑表")
public class TbAlbumPojo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("专辑表ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @ApiModelProperty("专辑名")
    @TableField("album_name")
    private String albumName;
    
    @ApiModelProperty("专辑封面地址")
    @TableField("pic")
    private String pic;
    
    @ApiModelProperty("专辑简介")
    @TableField("indirect")
    private String indirect;
    
    @ApiModelProperty("修改时间")
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
    
    
}
