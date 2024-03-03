package org.api.admin.model.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageReqCommon;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AlbumPageReq extends PageReqCommon {
    @ApiModelProperty("all name")
    private String name;
    
    @ApiModelProperty("音乐")
    private String musicName;
    
    @ApiModelProperty("专辑")
    private String albumName;
    
    @ApiModelProperty("歌手名")
    private String artistName;
    
    @ApiModelProperty(value = "排序", example = "sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序")
    private String orderBy = "sort";
    
    @ApiModelProperty(value = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order = false;
    
    @ApiModelProperty("开始时间")
    private LocalDateTime beforeTime;
    
    @ApiModelProperty("结束时间")
    private LocalDateTime laterTime;
}
