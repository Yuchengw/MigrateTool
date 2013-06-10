/** used for testing migrating, not good, import JUnit later.
 *
 *
 */

package com.salesforce.test;

import java.io.*;

import com.salesforce.factory.ToolFactory;
import com.salesforce.factory.MigrateFactory;
import com.salesforce.ui.Runner;
import com.salesforce.ui.MappingParser;
import com.salesforce.service.MappingBean;

import com.sforce.async.*;
import com.sforce.ws.ConnectionException;
import com.sforce.soap.partner.PartnerConnection;

public class TestMigrate{

	public static void main(String[] args) throws AsyncApiException,FileNotFoundException{
		System.out.println("Test Migration");
		MappingParser mp = new MappingParser();
		mp.parse();	
		new Runner(createToolFactory("migrate"),mp.getMappingBean(), "INFO");
	}
	
	public static ToolFactory createToolFactory(String service){
		if(service.equalsIgnoreCase("migrate")){
			return new MigrateFactory();
		}
		return null;
	}

}
