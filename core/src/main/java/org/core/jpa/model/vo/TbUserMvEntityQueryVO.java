package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "自定义查询 用户收藏mv")
public class TbUserMvEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 用户ID
     */
    @Schema(name = "用户ID")
    private Long userId;
    
    
    /**
     * MV ID
     */
    @Schema(name = "MV ID")
    private Long mvId;
    
}
