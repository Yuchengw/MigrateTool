/**
 * Using abstract factory design pattern for creating a tool factory's flexibilty
 *
 * 
 */


package com.salesforce.factory;


import com.salesforce.factory.Tool;
import com.salesforce.service.Bean;


public interface ToolFactory{
	
	public Tool useTool(Bean mb, String loglevel);
}
