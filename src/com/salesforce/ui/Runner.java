/**
 * used as consumer of the factory
 *
 *
 *
 *
 */


package com.salesforce.ui;


import java.io.*;
import java.util.logging.Logger;

import com.salesforce.service.lib.log.SuperLog;

import com.salesforce.factory.*;
import com.salesforce.service.MappingBean;
import com.salesforce.service.QueryBean;
import com.salesforce.service.Bean;

import com.sforce.ws.ConnectionException;
import com.sforce.async.*;


public class Runner{
		
	private static Logger LOGGER = null;

	public Runner(ToolFactory fac, Bean mb, String loglevel){
	
		LOGGER = SuperLog.open(Runner.class, loglevel);
		Tool tool = fac.useTool(mb, loglevel);
		// start run	
		try{
			tool.startRun();
		}catch(AsyncApiException t){
			t.printStackTrace();
		}catch(ConnectionException e){
			//TODO: will apply logger in the future
			e.printStackTrace();
    }catch(IOException f){
			//System.out.println(f);
			LOGGER.info(f.toString());
		}catch(InterruptedException k){
			//System.out.println(k);
			LOGGER.info(k.toString());	
		}
	}

}
