package org.core.model.res;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("更新 用户关注歌曲家")
@EqualsAndHashCode(callSuper = false)
public class TbUserArtistPojoUpdateRes extends TbUserArtistPojoRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
