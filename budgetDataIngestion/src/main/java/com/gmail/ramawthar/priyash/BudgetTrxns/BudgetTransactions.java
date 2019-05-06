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
		/* -> Processing using OpenFaas - the logic has been imported
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
        */
		
        String input = "{\"transaction\":\""+unprocessedLine.replace("\n", "").replace("\r", "").replace(";", "-")+"\"}";
        System.out.println("input: "+input);
        pushToQueue(parsingLogic(input));
	}
	


	private String parsingLogic(String transaction){
		System.out.println("transaction: "+transaction);
		
		String trxn = transaction;

        //logic to strip info

		if (trxn.contains(" reserved for purchase @ ")){		
			trxn = trxn.substring(trxn.indexOf("FNB :-) R")+9);
	
			trxn = trxn.replace(" reserved for purchase @ ", ";")
					   .replace(" from FNB card a/c..", ";")
					   .replace(" using card..", ";")
					   .replace(". Avail R", ";")
					   .replace(". ", ";");
					
			trxn = "creditPurchase;out;"+(trxn.substring(0, trxn.length()-6))+";"+trxn.substring(trxn.length()-5);
			
		} else if(trxn.contains(" paid from cheq a/c")){
			trxn = trxn.substring(trxn.indexOf("FNB :-) R")+9);
			
			trxn = trxn.replace(" paid from cheq a/c..", ";")
					   .replace(" @ Online Banking. Avail R", ";")
					   .replace(" @ Eft. Avail R", ";")
					   .replace(". Ref.", ";")
					   .replace(". ", ";");
			
			trxn = "cheqPurchase;out;"+(trxn.substring(0, trxn.length()-6))+";"+trxn.substring(trxn.length()-5);
			
		} else if(trxn.contains(" paid to cheq a/c..")){
			trxn = trxn.substring(trxn.indexOf("FNB :-) R")+9);
			
			
			trxn = trxn.replace(" paid to cheq a/c..", ";")
					   .replace(" @ Eft. Ref.", ";")
					   .replace(". ", ";");
			
			trxn = "cheqDeposits;in;"+(trxn.substring(0, trxn.length()-6))+";"+trxn.substring(trxn.length()-5);
			
		} else if(trxn.contains(" withdrawn from cheq a/c..")){
			trxn = trxn.substring(trxn.indexOf("FNB :-) R")+9);
			
			trxn = trxn.replace(" withdrawn from cheq a/c..", ";")
					   .replace(" @ ", ";")
					   .replace(". Avail R", ";")
					   .replace(". ", ";");
				
			trxn = "cheqWithdrawal;out;"+(trxn.substring(0, trxn.length()-6))+";"+trxn.substring(trxn.length()-5);
			
		} else if(trxn.contains(" t/fer from cheq a/c..")){
			trxn = trxn.substring(trxn.indexOf("FNB :-) R")+9);
			
			trxn = trxn.replace(" t/fer from cheq a/c..", ";")
					   .replace(" to Notice a/c..", ";")
					   .replace(" @ Online Banking. Avail R", ";")
					   .replace(". ", ";");
					
			trxn = "cheqTransfer;both;"+(trxn.substring(0, trxn.length()-6))+";"+trxn.substring(trxn.length()-5);
			
			
		}

        //logic done.
		
		return trxn;
	}
	
	private void pushToQueue(String processedLine){
		//works!
		/* removed for testing - place put back
		QueueManager qm = new QueueManager();
		qm.publishToQueue(processedLine);*/
		System.out.println("processedLine  : "+processedLine);
		System.out.println("complete  :) ");
	
	}
	
	public static void main(String [] args){
		System.out.println("hi");
		BudgetTransactions bt = new BudgetTransactions("test body");
		String unprocessedLine = "• FNB :-) R122.06 reserved for purchase @ Checkers Leaping Frog from FNB card a/c..947000 using card..7046. Avail R17811. 4May 11:24";
		bt.parseLine(unprocessedLine);
		
	}
}
