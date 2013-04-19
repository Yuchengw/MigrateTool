/**
 * Using abstract factory design pattern for creating a tool factory's flexibilty
 *
 * 
 */


package com.salesforce.factory;


import com.salesforce.factory.Tool;


public interface ToolFactory{
	
	public Tool useTool(String objAPIName, String username, String usrpwd);
}
