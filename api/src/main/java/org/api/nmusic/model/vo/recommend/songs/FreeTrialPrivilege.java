package org.api.nmusic.model.vo.recommend.songs;

import lombok.Data;

@Data
public class FreeTrialPrivilege {
    private boolean userConsumable;
    private boolean resConsumable;
    private Object listenType;
}