/** used for testing migrating, not good, import JUnit later.
 *
 *
 */

package com.salesforce.test;


import com.salesforce.factory.ToolFactory;
import com.salesforce.factory.MigrateFactory;
import com.salesforce.ui.Runner;

import com.sforce.async.*;
import com.sforce.ws.ConnectionException;
import com.sforce.soap.partner.PartnerConnection;

public class TestMigrate{

	public static void main(String[] args) throws AsyncApiException{
		System.out.println("Test Migration");
		new Runner(createToolFactory("migrate"));
	}
	
	public static ToolFactory createToolFactory(String service){
		if(service.equalsIgnoreCase("migrate")){
			return new MigrateFactory();
		}
		return null;
	}

}
