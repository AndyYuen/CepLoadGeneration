package com.redhat.cep.util;

import java.io.Serializable;

public interface EventGenerator extends Serializable {

	public BaseEvent generateEvent();
	
}
