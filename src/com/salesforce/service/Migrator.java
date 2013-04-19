/**
 * This is a the migrate object that combines delete, query and update objects 
 *
 *
 */


package com.salesforce.service;


import java.io.*;
import java.util.*;

import com.salesforce.service.bulk.*;
import com.salesforce.factory.*;

import com.sforce.async.*;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
//import com.sforce.ws.ConnectionConfig;


public class Migrator implements Tool{

	private String objectAPIName;
	private String userName;
	private String userPwd;

	// default constructor
	public Migrator(){}

	public Migrator(String objectAPIName, String userName, String userPwd){
		this.objectAPIName = objectAPIName;
		this.userName = userName; 
		this.userPwd  = userPwd;
	}

	// implements tool interface
	public String getToolInfo(){
		return "Migrating ";
	} 

	// setter and getters

	public void setObjectAPIName(String Name){
		this.objectAPIName = Name;
	}

	public void setUserName(String usrname){
		this.userName = usrname;
	}

	public void setUserPwd(String usrpwd){
		this.userPwd = usrpwd;	
	}
	
	public String getObjectAPIName(){
		return this.objectAPIName;
	}
	
	public String getUserName(){
		return this.userName;
	}

	public String getUserPwd(){
		return this.userPwd;
	}
  	
	/**
     * For Migration, we need query, delete, and insert object to achieve functionality
     * 
     * @return void
	 * @throws ConnectionException;IOException
     */
	public void startRun() throws ConnectionException, IOException, AsyncApiException{
		//TODO: implement query object, get the file or stream in csv format
		//TODO: For now, we just use a temp test.csv file for input 
		Insert up = new Insert();
		up.runCSV(getObjectAPIName(),getUserName(),getUserPwd(),"test.csv");
		
	}
}
