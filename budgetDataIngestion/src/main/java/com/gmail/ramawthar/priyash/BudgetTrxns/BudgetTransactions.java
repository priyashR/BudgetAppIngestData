package com.gmail.ramawthar.priyash.BudgetTrxns;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.gmail.ramawthar.priyash.interfaces.ProcessEmail;
import com.gmail.ramawthar.priyash.rabbit.QueueManager;
import com.gmail.ramawthar.priyash.rabbit.QueueManager;

public class BudgetTransactions implements ProcessEmail{
	
	String emailBody;
	String result;

	public BudgetTransactions(String emailBody) {
		super();
		this.emailBody = emailBody;
		this.result = "";
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
		this.result = "";
	}
	
	public String getResult() {
		return result;
	}

	public void parseBody(){

		String trxnLine = "";
		
		//System.out.println(emailBody);
		String[] lines = emailBody.split(System.getProperty("line.separator"));
		//System.out.println(lines.length);
		
		for (int i = 0; i < lines.length; i++){
			trxnLine = "";
			if ((lines[i].contains("FNB :-) R"))&&(!(lines[i].toUpperCase().startsWith("SUBJECT")))){
				if (i+1 >= lines.length){
					trxnLine = lines[i];
				}else{
					trxnLine = lines[i]+" "+lines[i+1];
					i++;
				}
				//need to pass a line at a time instead of the whole body
				System.out.println(lines.length+" "+"trxnLine: "+trxnLine);
				
				// HACK !!! - this should be done in OpenFaas
				if (trxnLine.contains("withdrawn from FNB card")){
					trxnLine = trxnLine.replace("withdrawn from FNB card", "withdrawn from cheq").replace("\n", "").replace("\r", "");
					String before = trxnLine.substring(0, trxnLine.indexOf("using card.."));
					String after = trxnLine.substring(trxnLine.indexOf("@ ") );
					trxnLine = before + after;
				}
				// HACK !!! - this should be done in OpenFaas
				parseLine(trxnLine);
			}
		}
		
	}
	
	private void parseLine(String unprocessedLine){
		
		final String serverIP = "172.16.0.128";//do an ipconfig to get the host ip value
		//final String serverIP = "127.0.0.1";
		String lineResult = "";
		
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        
        String input = "{\"transaction\":\""+unprocessedLine.replace("\n", "").replace("\r", "").replace(";", "-")+"\"}";
        System.out.println("input: "+input);
        String uri = "http://"+serverIP+":8080/function/format-fnb-transaction";
        
        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.set("Authorization", "Basic " + "xxxxxxxxxxxx");
        HttpEntity<String> entity = new HttpEntity<String>(input, headers);
        
        ResponseEntity<String> response = restTemplate
                .exchange(uri, HttpMethod.POST, entity, String.class);
        
        lineResult = response.getBody();
        result = result + " " + lineResult;
        System.out.println("lineResult: "+lineResult);
        System.out.println("result: "+result);
        
        pushToQueue(lineResult);
	}
	private void pushToQueue(String processedLine){
		//works!
		
		QueueManager qm = new QueueManager();
		qm.publishToQueue(processedLine);
	
	}
}
