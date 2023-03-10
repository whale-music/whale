package org.api.admin.model.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class PageCommon {
    @ApiModelProperty("当前页数")
    private Integer pageIndex;
    
    @ApiModelProperty("每页展示行数")
    private Integer pageNum;
}
