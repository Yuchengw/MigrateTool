/**
 * The program that performs as the entry of the program
 * The main class of program
 *
 */

package com.salesforce.ui;


import com.salesforce.factroy.ToolFactory;
import com.slaesforce.factory.MigrateFactory;
import com.salesforce.ui.MappingParser;
import com.salesforce.ui.MappingBean;
import com.salesforce.service.MappingBean;

public class Entry{

	// Default values
	boolean VERBOSE_MODE=false;
	boolean INTERACTIVE_MODE=false;
	
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

	}
	/**
     * The function that deales with no user input configure inforation
     *
     */
	public static void disableCLI(){
		
	}
	public static void main(String[] args) throws FileNotFoundException, AsynsException{
		System.out.println("******************Starting TOOL********************");
		parserArgument(args);
		// if user has not implement CLI mode, use already configured XML file
		if(!INTERACTIVE_MODE) disableCLI();
	}

}
