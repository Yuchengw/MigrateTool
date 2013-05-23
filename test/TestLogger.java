package com.salesforce.test;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.salesforce.service.lib.log.SuperLog;


public class TestLogger{

	private static final Logger logger = SuperLog.open(TestLogger.class);
	public TestLogger(){}

	public static void main(String[] args) throws IOException{
		// could use setLevel methods to set log level: LEVEL.OFF : only SEVERE, WARNIING, INFO will be logged
		//logger.setLevel(Level.OFF);
		SuperLog.setupFile();
		logger.log(Level.INFO,"Test Logger functionality.");		
	}
	
}
