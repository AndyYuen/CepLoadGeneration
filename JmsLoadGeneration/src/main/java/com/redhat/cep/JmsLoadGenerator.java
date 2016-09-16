package com.redhat.cep;

import java.io.FileReader;
import java.io.IOException;

import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.redhat.cep.util.AbstractLoadGenerator;
import com.redhat.cep.util.BaseEvent;
import com.redhat.cep.util.ObjectCreator;
import com.redhat.cep.util.ReflectionBasedObjectCreator;

public class JmsLoadGenerator extends AbstractLoadGenerator {
	static Logger logger = Logger.getLogger(JmsLoadGenerator.class);

	static final int PROGRESS_COUNT = 1000;
	
	private ProducerTemplate camelTemplate;
	private int count = 0;

	public JmsLoadGenerator(FileReader reader, ObjectCreator creator, ProducerTemplate camelTemplate)
			throws IOException {
		super(reader, creator);
		this.camelTemplate = camelTemplate;
	}


	@Override
	public void processEvent(BaseEvent event, long millisSinceLastEvent) {
		if (millisSinceLastEvent > 0) {
			try {
				Thread.sleep(millisSinceLastEvent);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}


		camelTemplate.sendBody("jms:queue:events", ExchangePattern.InOnly, event);
		if ((++count % PROGRESS_COUNT) == 0) {
			logger.info(count + " events sent...");
		}


	}
	
	// 
	/**
	 * 
	 * This is a Camel client using ProducerTemplate to send events to a message queue
	 * args[0] contains the event package name eg, com.redhat.cep.stockprice.model where most events and
	 *         other objects are defined
	 * args[1] contains the csv file path info
	 */
    public static void main(final String[] args) throws Exception {

		
        logger.warn("JmsLoadGenerator requires that the CamelServer is running already!");

        AbstractApplicationContext context = new ClassPathXmlApplicationContext("JmsLoadGenerator.xml");

        // use camel ProducerTemplate 
        ProducerTemplate camelTemplate = context.getBean("camelTemplate", ProducerTemplate.class);
        
    	
		// set up fact/event object creator
		ObjectCreator creator = new ReflectionBasedObjectCreator(args[0]);
		
		// args[0] contains the csv file path info
		JmsLoadGenerator loadGen = new JmsLoadGenerator(new FileReader(args[1]), creator, camelTemplate);
		
		logger.info("start sending events...");
		
		loadGen.run();


        // close the application context
        context.close();
        
        logger.info("Finished execution.");
    }

}
