package com.redhat.cep.optometrist.application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.redhat.cep.optometrist.model.Appointment;
import com.redhat.cep.optometrist.model.Optometrist;
import com.redhat.cep.optometrist.model.Patient;
import com.redhat.cep.optometrist.model.SendSmsEvent;
import com.redhat.cep.util.DirectLoadGenerator;
import com.redhat.cep.util.CepApplication;
import com.redhat.cep.util.ObjectCreator;
import com.redhat.cep.util.ObservableEvent;
import com.redhat.cep.util.PushOverNofification;
import com.redhat.cep.util.ReflectionBasedObjectCreator;
import com.redhat.cep.util.Validators;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;


// CEP application of Optometrist Practice for improving Customer Experience
// Customer will be informed via SMS of schedule delays
public class OptometristCepApplication extends CepApplication {
	
	static public final String ENTRY_POINT = "Clinic";
	static public final int ST_DELAY = 0;
	static public final int ST_SERVICE_TIME = 1;
	static public final String TIME_FORMAT = "HH:mm";
	static public final String FACT_EVENT_PACKAGE = "com.redhat.cep.optometrist.model";
	static public final String KSESSION_NAME = "SimKS";
	static public final String APP_TOKEN = "azJJFpX12psP2r4Dud15qf7777682dt"; // replace it with your own token
	
	// setup the application
	public OptometristCepApplication(String ksessionName, String entryPointName) throws IOException
	{
		super(ksessionName, entryPointName);
	}
	
	
	// print statistics collected for the application
	public void printStats()
	{
		// run Drools query to get all Optometrists 
	    QueryResults opResult = ksession.getQueryResults( "Optometrists" );
	    System.out.println( "Total Optometrist count: " + opResult.size() );
	    for( QueryResultsRow opRow: opResult ){
	    	Optometrist optometrist = (Optometrist) opRow.get( "$optometrist" );
	    
			// run Drools query to get all appointments for calculating statistics
		    QueryResults result = ksession.getQueryResults( "Appointments", new Object[] { optometrist.getName() }  );

		    System.out.println(String.format("\n---------------------------------------------\nFor Optometrist: %s of %s", 
		    		optometrist.getName(), optometrist.getClinic()));
		    System.out.println( "Total Appointment count: " + result.size() );
		    SummaryStatistics[] stats = new SummaryStatistics[] { new SummaryStatistics(), new SummaryStatistics()};
		    for( QueryResultsRow row: result ){
		        Appointment appointment = (Appointment) row.get( "$appointment");
		        try {
	        	
		        	// validate fields in appointment
					Validators.checkNotNull(appointment.getArrivalTime(), appointment.getStartServiceTime(),
							appointment.getEndServiceTime(), appointment.getScheduledStartTime());
					int delay = Minutes.minutesBetween(appointment.getScheduledStartTime(), appointment.getStartServiceTime()).getMinutes();
					int serviceTime = Minutes.minutesBetween(appointment.getStartServiceTime(), appointment.getEndServiceTime()).getMinutes();

					// calc statistics: average, std. dev and sample size
					stats[ST_DELAY].addValue(delay);
					stats[ST_SERVICE_TIME].addValue(serviceTime);
					
					// display patient info
					System.out.println(String.format("Customer = %10s, Wait Time = %3d minutes, ServiceTime = %3d minutes, Scheduled StartTime = %s",
							appointment.getPatientName(),
							delay,
							serviceTime,
							appointment.getScheduledStartTime().toString(TIME_FORMAT)
							));
				} catch (Exception e) {
					// no recorded service data in appointment
					System.out.println(String.format("Appointment statistics not available for patient: %s - %s", appointment.getPatientName(),
							e.getMessage()));
				}
	
		    }
		    
		    // print service time statistics by optometrist
		    String errorOptometrist = null;
			try {

					errorOptometrist = optometrist.getName();
					//stats = map.get(optometrist);
					System.out.println(String.format("\nAverage Wait Time   = %6.1f minutes, Std. Dev. = %6.1f minutes\n" +
							"Average ServiceTime = %6.1f minutes, std. Dev. = %6.1f minutes\n" +
							"for a total of: %d appointments\n",
							stats[ST_DELAY].getMean(), stats[ST_DELAY].getStandardDeviation(),
							stats[ST_SERVICE_TIME].getMean(), stats[ST_SERVICE_TIME].getStandardDeviation(),
							stats[ST_SERVICE_TIME].getN()
							));
/*				    QueryResults evtResult = ksession.getQueryResults( "SmsEventCount", new Object[] { optometrist.getName() }  );
				    //System.out.println( "Total sms event query result count: " + evtResult.size() );
				    for( QueryResultsRow row: evtResult ){
				    	long count = (long) row.get( "$count");
				    	System.out.println( "Total SMS events sent: " + count );
				    }*/
				    QueryResults evtResult = ksession.getQueryResults( "SmsEvents", new Object[] { optometrist.getName() }  );
				    //System.out.println( "Total sms event query result count: " + evtResult.size() );
				    System.out.println("SMS events generated:");
				    for( QueryResultsRow row: evtResult ){
				    	ArrayList<SendSmsEvent> list = (ArrayList<SendSmsEvent>) row.get( "$list");
				    	for (SendSmsEvent event : list) {
					    	System.out.println( String.format("SMS: delay of %5d minutes sent at %s", event.getDelay(), event.getCurrentTime().toString(TIME_FORMAT)));				    		
				    	}
				    }    

			} catch (Exception e) {
				// invalid sample
				System.out.println(String.format("No useful statistics for Optometrist: %s - %s", 
						errorOptometrist, e.getMessage()));
			}
	    }

	}
	
	public static void insertFacts(OptometristCepApplication app) {
		app.insert(new Appointment("P1", "OP1", 0, new LocalTime("08:00")));
		app.insert(new Appointment("P2", "OP1", 1, new LocalTime("08:30")));
		app.insert(new Appointment("P3", "OP1", 8, new LocalTime("12:00")));
		app.insert(new Appointment("P4", "OP1", 10, new LocalTime("13:00")));
		app.insert(new Appointment("Q1", "OP2", 0, new LocalTime("08:00")));
		app.insert(new Appointment("Q2", "OP2", 1, new LocalTime("08:30")));
		app.insert(new Appointment("Q3", "OP2", 2, new LocalTime("09:00")));
		app.insert(new Appointment("Q4", "OP2", 7, new LocalTime("11:30")));
		app.insert(new Appointment("Q5", "OP2", 8, new LocalTime("12:00")));
		app.insert(new Appointment("Andy", "OP2", 10, new LocalTime("13:00")));
		
		app.insert(new Patient("P1", "424222111", "xxx"));
		app.insert(new Patient("P2", "424222222", "xxx"));
		app.insert(new Patient("P3", "424222333", "xxx"));
		app.insert(new Patient("P4", "424222444", "xxx"));
		app.insert(new Patient("Q1", "405222111", "xxx"));
		app.insert(new Patient("Q2", "405222222", "xxx"));
		app.insert(new Patient("Q3", "405222333", "xxx"));
		app.insert(new Patient("Q4", "405222444", "xxx"));
		app.insert(new Patient("Q5", "405222555", "xxx"));
		app.insert(new Patient("Andy", "405222666", "xxx"));
		
		app.insert(new Optometrist("OP1", "North Sydney Optometrists", new LocalTime("08:00"), 30));
		app.insert(new Optometrist("OP2", "Crows Nest Optometrists", new LocalTime("08:00"), 30));
		

	}

	// main program to setup and run the CEP application
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		// args[0] contains the csv file path info
		// creator is the object instantiator
		// KSESSION_NAME is the ksession name in the kmodule.xml in the META-INF directory
		// ENTRY_POINT is the event entry point name
		OptometristCepApplication cepApp = new OptometristCepApplication(KSESSION_NAME, ENTRY_POINT);
		cepApp.setGlobal("notify", new PushOverNofification(APP_TOKEN));
		cepApp.setGlobal("clock",cepApp.getWallClock());
		insertFacts(cepApp);
		
		ObservableEvent observableEvent = new ObservableEvent();
		observableEvent.addObserver(cepApp);
		
		// set up fact/event object creator
		ObjectCreator creator = new ReflectionBasedObjectCreator(FACT_EVENT_PACKAGE);
		
		DirectLoadGenerator loadGenerator = new DirectLoadGenerator(new FileReader(args[0]), creator, observableEvent);

		// run application
		loadGenerator.run();
		
		// output statistics for application
		cepApp.printStats();
		
		// cleanup
		cepApp.shutdown();
	}

}
