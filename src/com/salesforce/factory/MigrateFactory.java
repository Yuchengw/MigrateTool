/**
 * The migrate factory is the secondary factory of Tool factory
 *
 */


package com.salesforce.factory;


import com.salesforce.service.Migrator;

public class MigrateFactory implements ToolFactory{
	
	public Tool useTool(String objectAPIName, String username, String usrpwd){
		return new Migrator(objectAPIName, username, usrpwd);
	}

}
