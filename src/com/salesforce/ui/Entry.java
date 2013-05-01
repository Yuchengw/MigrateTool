/**
 * The program that performs as the entry of the program
 * The main class of program
 *
 */

package com.salesforce.main;


import java.io.*;

import com.salesforce.factory.ToolFactory;
import com.salesforce.factory.MigrateFactory;
import com.salesforce.ui.Runner;
import com.salesforce.ui.MappingParser;
import com.salesforce.service.MappingBean;

import com.sforce.async.*;
import com.sforce.ws.ConnectionException;
import com.sforce.soap.partner.PartnerConnection;

public class Entry{

	// Default values
	private static boolean VERBOSE_MODE=false;
	private static boolean INTERACTIVE_MODE=false;
    private static int  RUNNING_MODE=1;
	private static int  MIGRATE_MODE=1; // default set as MIGRATE_MODE
	
	public Entry(){
	}
		
    /**
     * main function that deal with user's input 
     * @param args input user string
     * @return void
     */
	public static void parseArgument(String[] args){
		for(String arg : args){
			if(arg.toLowerCase().contains("-v") || arg.toLowerCase().contains("--verbose")){
				VERBOSE_MODE=true; // output log info
			}
			if(arg.toLowerCase().contains("-i") || arg.toLowerCase().contains("--interactive")){
				INTERACTIVE_MODE=true; // interactively generate xml file
			}
		}
		if(VERBOSE_MODE) enableLog();
		if(INTERACTIVE_MODE) enableCLI(args);
    }

	/** TODO:
     * The function that direct log output to console 
     *
     */
	public static void enableLog(){

	}

	/**
     * The function that enable Command line tool parse user input and generate XML config file
     *
     */
	public static void enableCLI(String[] args){
		System.out.println("*********************using user input info************************");
	}
	/**
     * The function that deales with no user input configure inforation
     *
     */
	public static void disableCLI(String[] args) throws FileNotFoundException{
		System.out.println("*********************using default config file************************");
		for(String arg : args){
			if(arg.toLowerCase().contains("migrate")){
				RUNNING_MODE=MIGRATE_MODE;
				break;
			}
		}
		switch(RUNNING_MODE){
			case 1:
				enableMigrate();
				break;
			default:
				System.out.println("Please specify which tool you want to use ");
		}
	}

	/**
     * function that implements migreation function
     *
     * @throws FileNotFoundException
     */
	public static void enableMigrate() throws FileNotFoundException{
		System.out.println("*************************Starting Migration******************");
		MappingParser mp = new MappingParser();
		mp.parse();
		new Runner(createToolFactory("migrate"),mp.getMappingBean());
	}

	/**TODO: make more transparent in the future
     * function that return corresponding tool 
     *
     */
	public static ToolFactory createToolFactory(String service){
		if(service.equalsIgnoreCase("migrate")){
			return new MigrateFactory();
		}
		return null;
	}

	public static void main(String[] args) throws FileNotFoundException, AsyncApiException{
		System.out.println("******************Starting TOOL********************");
		parseArgument(args);
		// if user has not implement CLI mode, use already configured XML file
		if(!INTERACTIVE_MODE) disableCLI(args);
	}

}




