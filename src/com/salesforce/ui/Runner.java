/**
 * used as consumer of the factory
 *
 *
 *
 *
 */


package com.salesforce.ui;


import java.io.*;

import com.salesforce.factory.*;

import com.sforce.ws.ConnectionException;
import com.sforce.async.*;


public class Runner{

	public Runner(ToolFactory fac){
		// migrate or other object 
		Tool tool = fac.useTool("Test__c","ycwmike@salesforce.com","Ycw880216GWD6xhmXbW6aSedxaTZ8gtWuZ");
		// start run	
		try{
			tool.startRun();
		}catch(AsyncApiException t){
			t.printStackTrace();
		}catch(ConnectionException e){
			//TODO: will apply logger in the future
			e.printStackTrace();
    }catch(IOException f){
			System.out.println(f);
		}catch(InterruptedException k){
			System.out.println(k);
		}
	}

}
