package org.api.neteasecloudmusic.model.vo.recommend.songs;

import lombok.Data;

import java.util.List;

@Data
public class Al {
    private String picUrl;
    private String name;
    private List<Object> tns;
    private String picStr;
    private Long id;
    private Long pic;
}