/**
 * The migrate factory is the secondary factory of Tool factory
 *
 */


package com.salesforce.factory;


import com.salesforce.service.Migrator;
import com.salesforce.service.MappingBean;

public class MigrateFactory implements ToolFactory{
	
	public Tool useTool(MappingBean mb){
		return new Migrator(mb);
	}

}
