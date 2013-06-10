/**
 * The insert factory is the secondary factory of Tool factory
 *
 *
 */


package com.salesforce.factory;


import com.salesforce.service.Insertor;
import com.salesforce.service.Bean;
import com.salesforce.service.MappingBean;

public class InsertFactory implements ToolFactory{
	
	public Tool useTool(Bean mb, String loglevel){
		return new Insertor((MappingBean)mb, loglevel);
	}

}
