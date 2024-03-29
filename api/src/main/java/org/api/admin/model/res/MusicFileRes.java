package org.api.admin.model.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbResourcePojo;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MusicFileRes extends TbResourcePojo {
    
    @ApiModelProperty("文件下载地址")
    private String rawUrl;
    
    @ApiModelProperty("文件下载地址")
    private Boolean exists;
}
