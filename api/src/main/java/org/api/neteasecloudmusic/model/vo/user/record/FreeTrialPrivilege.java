package org.api.neteasecloudmusic.model.vo.user.record;

import lombok.Data;

@Data
public class FreeTrialPrivilege{
	private boolean userConsumable;
	private boolean resConsumable;
	private Object listenType;
}