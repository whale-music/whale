package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "自定义查询 歌手和专辑中间表")
public class TbAlbumArtistEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 专辑ID
     */
    @Schema(name = "专辑ID")
    private Long albumId;
    
    
    /**
     * 歌手ID
     */
    @Schema(name = "歌手ID")
    private Long artistId;
    
}
