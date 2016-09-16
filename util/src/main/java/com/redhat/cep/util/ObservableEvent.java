package com.redhat.cep.util;

import java.util.Observable;

/**
 * 
 * This class is used in applying the GOF's Observer design pattern to loosely-couple
 * the LoadGenerator and your CEP application.
 *
 */
public class ObservableEvent extends Observable {

	   private BaseEvent event = null;
	   
	   public ObservableEvent()
	   {
	   }
	   
	   public void setValue(BaseEvent event)
	   {
	      this.event = event;
	      setChanged();
	      notifyObservers();
	   }
	   
	   public BaseEvent getValue()
	   {
	      return event;
	   }
}
