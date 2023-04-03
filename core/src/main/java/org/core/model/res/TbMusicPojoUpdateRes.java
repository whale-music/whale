package org.core.model.res;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("更新 所有音乐列表")
@EqualsAndHashCode(callSuper = false)
public class TbMusicPojoUpdateRes extends TbMusicPojoRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
