package org.api.nmusic.model.vo.playlistallsong;

import lombok.Data;

@Data
public class FreeTrialPrivilege {
    private boolean userConsumable;
    private boolean resConsumable;
    private Object listenType;
}