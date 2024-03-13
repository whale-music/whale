package org.core.jpa.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "音乐与歌手中间表")
public class TbMusicArtistEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 音乐ID
     */
    @Schema(name = "音乐ID")
    private Long musicId;
    
    
    /**
     * 艺术家ID
     */
    @Schema(name = "艺术家ID")
    private Long artistId;
    
}
