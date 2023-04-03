package org.core.model.res;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("更新 音乐下载地址")
@EqualsAndHashCode(callSuper = false)
public class TbMusicUrlPojoUpdateRes extends TbMusicUrlPojoRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
