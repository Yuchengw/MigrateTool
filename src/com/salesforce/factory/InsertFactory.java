/**
 * The insert factory is the secondary factory of Tool factory
 *
 *
 */


package com.salesforce.factory;


import com.salesforce.service.Insertor;
import com.salesforce.service.MappingBean;

public class InsertFactory implements ToolFactory{
	
	public Tool useTool(MappingBean mb){
		return new Insertor(mb);
	}

}
