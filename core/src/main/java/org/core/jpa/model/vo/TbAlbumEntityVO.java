package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
@ApiModel("保存 歌曲专辑表")
public class TbAlbumEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 专辑表ID
     */
    @NotNull(message = "id can not null")
    @ApiModelProperty("专辑表ID")
    private Long id;
    
    
    /**
     * 专辑名
     */
    @NotNull(message = "albumName can not null")
    @ApiModelProperty("专辑名")
    private String albumName;
    
    
    /**
     * 专辑版本（比如录音室版，现场版）
     */
    @ApiModelProperty("专辑版本（比如录音室版，现场版）")
    private String subType;
    
    
    /**
     * 专辑简介
     */
    @ApiModelProperty("专辑简介")
    private String description;
    
    
    /**
     * 发行公司
     */
    @ApiModelProperty("发行公司")
    private String company;
    
    
    /**
     * 专辑发布时间
     */
    @ApiModelProperty("专辑发布时间")
    private Date publishTime;
    
    
    /**
     * 上传用户ID
     */
    @NotNull(message = "userId can not null")
    @ApiModelProperty("上传用户ID")
    private Long userId;
    
    
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    
}