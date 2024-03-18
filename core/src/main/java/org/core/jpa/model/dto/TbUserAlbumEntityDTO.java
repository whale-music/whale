package org.core.jpa.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "用户收藏专辑表")
public class TbUserAlbumEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    @Schema(name = "用户ID")
    private Long userId;
    
    
    /**
     * 专辑ID
     */
    @Schema(name = "专辑ID")
    private Long albumId;
    
}
