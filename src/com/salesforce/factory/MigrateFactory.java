/**
 * The migrate factory is the secondary factory of Tool factory
 *
 */


package com.salesforce.factory;


import com.salesforce.service.Migrator;
import com.salesforce.service.MappingBean;
import com.salesforce.service.Bean;

public class MigrateFactory implements ToolFactory{
	
	public Tool useTool(Bean mb){
		return new Migrator((MappingBean)mb);
	}

}
