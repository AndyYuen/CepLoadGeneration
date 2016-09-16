package com.redhat.cep.util;

import java.io.FileReader;
import java.io.IOException;

public class DirectLoadGenerator extends AbstractLoadGenerator {
	
	private ObservableEvent observableEvent = null;
	private Scheduler scheduler;

	public DirectLoadGenerator(FileReader reader, ObjectCreator creator, ObservableEvent observableEvent)
			throws IOException {
		super(reader, creator);
		this.observableEvent = observableEvent;
	}

	@Override
	public void processEvent(BaseEvent event, long millisSinceLastEvent) {
		observableEvent.setValue(event);

	}
	
	/**
	 * 
	 * The Scheduler object is only used in Discrete Event Simulations and
	 * is not used in CEP applications
	 */
	public Scheduler getScheduler() {
		if (scheduler == null) {
			scheduler = new Scheduler(this);
		}
		return scheduler;
	}

}
