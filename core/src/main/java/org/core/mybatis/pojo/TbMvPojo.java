package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 音乐短片(TbMv)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:31
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_mv")
@Schema(name = "TbMv", description = "音乐短片")
public class TbMvPojo extends Model<TbMvPojo> implements Serializable {
    public static final long serialVersionUID = 839599165219313300L;
    
    @Schema(title = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "Mv ID")
    @TableField("mv_id")
    private Long mvId;
    
    @Schema(title = "MD5")
    @TableField("md5")
    private String md5;
    
    @Schema(title = "mv 路径")
    @TableField("path")
    private String path;
    
    @Schema(title = "视频时长")
    @TableField("duration")
    private Long duration;
    
    @Schema(title = "上传用户")
    @TableField("user_id")
    private Long userId;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @Schema(title = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}

