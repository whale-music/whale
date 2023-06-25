package org.core.mybatis.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 标签表（风格）
 * </p>
 *
 * @author Sakura
 * @since 2023-06-25
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("tb_tag")
@ApiModel(value = "TbTagPojo对象", description = "标签表（风格）")
public class TbTagPojo extends Model<TbTagPojo> implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("风格（标签）")
    @TableField("tag_name")
    private String tagName;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
