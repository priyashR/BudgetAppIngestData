package com.gmail.ramawthar.priyash.DataIngestion;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.gmail.ramawthar.priyash.BudgetTrxns.BudgetTransactions;
import com.gmail.ramawthar.priyash.interfaces.ProcessEmail;


public class FetchEmails {
	
	//public static void main(String arg[]) throws Exception {
	
	public void run() throws Exception {
		
		
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext(
				"/META-INF/spring/integration/gmail-imap-idle-config.xml");
		DirectChannel inputChannel = ac.getBean("receiveChannel", DirectChannel.class);
		inputChannel.subscribe(new MessageHandler() {
			public void handleMessage(Message<?> message) throws MessagingException {
				//logger.info("Message: " + message);
				try{
				ProcessEmail emailProcessor;
				MimeMessage mm = (MimeMessage) message.getPayload();
				
				Multipart mp = (Multipart) mm.getContent();
				mm.getFolder().open(Folder.READ_ONLY);
				System.out.println("mp.getCount(): "+mp.getCount());
				mm.getFolder().open(Folder.READ_ONLY);
				for (int i = 0; i < mp.getCount(); i++) {
					
					//Add logic to identify the correct email processing class
					//if body is like this and subject is like that
					//then use a different instance of the email processor
					//
					//now we just use BudgetTransactions
					
					
					System.out.println("i count: "+i);
	                BodyPart part = mp.getBodyPart(i);
	              
	                //System.out.println("Body type: "+part.getContentType());
	                //System.out.println("Body type: "+part.getContent().toString());
	                
	                if (part.isMimeType("text/plain")) {
	                	System.out.println("Body Priyash: " + part.getContent().toString());
	                	emailProcessor = new BudgetTransactions(part.getContent().toString());
	                	emailProcessor.parseBody();
	                	
	                }
	                if (part.isMimeType("multipart/ALTERNATIVE")) {
	                	Multipart mpLevelTwo = (Multipart) part.getContent();
	    				mm.getFolder().open(Folder.READ_ONLY);
	    				System.out.println("mpLevelTwo.getCount(): "+mpLevelTwo.getCount());
	    				mm.getFolder().open(Folder.READ_ONLY);
	    				for (int j = 0; j < mpLevelTwo.getCount(); j++) {
	    					System.out.println("j count: "+j);
	    	                BodyPart partLevelTwo = mpLevelTwo.getBodyPart(j);
	    	                //System.out.println("Body type: "+partLevelTwo.getContentType());
	    	                //System.out.println("Body type: "+partLevelTwo.getContent().toString());
	    	                if (partLevelTwo.isMimeType("TEXT/PLAIN")) {

	    	                	emailProcessor = new BudgetTransactions(partLevelTwo.getContent().toString());
	    	                	emailProcessor.parseBody();
	    	                }

	    				}
	                }
	                
	                
	            }
				}catch(Exception e){
					
					e.printStackTrace();
				}
			}
		});		
	}

}
