/** used for testing migrating, not good, import JUnit later.
 *
 *
 */

package com.salesforce.test;


import com.salesforce.factory.ToolFactory;
import com.salesforce.factory.MigrateFactory;
import com.salesforce.ui.Runner;

public class TestMigrate{

	public static void main(String[] args){
		System.out.println("Test Migration");
		new Runner(createToolFactory("migrate"));
	}
	
	public static ToolFactory createToolFactory(String service){
		if(service.equalsIgnoreCase("migrate")){
			return new MigrateFactory();
		}
	}

}
