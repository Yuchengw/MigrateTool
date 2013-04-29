/**
 * Using abstract factory design pattern for creating a tool factory's flexibilty
 *
 * 
 */


package com.salesforce.factory;


import com.salesforce.factory.Tool;
import com.salesforce.service.MappingBean;


public interface ToolFactory{
	
	public Tool useTool(MappingBean mb);
}
