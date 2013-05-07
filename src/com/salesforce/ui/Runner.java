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
import com.salesforce.service.MappingBean;
import com.salesforce.service.QueryBean;
import com.salesforce.service.Bean;

import com.sforce.ws.ConnectionException;
import com.sforce.async.*;


public class Runner{

	public Runner(ToolFactory fac, Bean mb){
	
		Tool tool = fac.useTool(mb);
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
