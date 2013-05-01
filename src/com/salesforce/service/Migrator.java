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
import com.salesforce.service.MappingBean;

import com.sforce.async.*;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
//import com.sforce.ws.ConnectionConfig;


public class Migrator implements Tool{

	private String fromorgobjectAPIName;
	private String fromorguserName;
	private String fromorguserPwd;
	private String toorgobjectAPIName;
	private String toorguserName;
	private String toorguserPwd;
	private ArrayList<String> fromorglist;
	private ArrayList<String> toorglist;

	// default constructor
	public Migrator(MappingBean mb){
		this.fromorgobjectAPIName = mb.getFromOrgObject();
		this.fromorguserName = mb.getFromOrgUserName();
		this.fromorguserPwd = mb.getFromOrgPassword();
		this.toorgobjectAPIName = mb.getToOrgObject();
		this.toorguserName = mb.getToOrgUserName();
		this.toorguserPwd = mb.getToOrgPassword();
		this.fromorglist = mb.getFromList();
		this.toorglist = mb.getToList();
	}
	
	// implements tool interface
	public String getToolInfo(){
		return "Migrating ";
	} 

	// setter and getters
	public void setFromOrgObjectAPIName(String name){
		this.fromorgobjectAPIName = name;
	}

	public void setToOrgObjectAPIName(String name){
		this.toorgobjectAPIName = name;
	}

	public void setFromOrgUserName(String usrname){
		this.fromorguserName = usrname;
	}

	public void setToOrgObjectUserName(String usrname){
		this.toorguserName = usrname;
	}

	public void setFromOrgUserPwd(String usrpwd){
		this.fromorguserPwd = usrpwd;	
	}

	public void setToOrgUserPwd(String usrpwd){
		this.toorguserPwd = usrpwd;	
	}
	
	public String getFromOrgObjectAPIName(){
		return this.fromorgobjectAPIName;
	}
	
	public String getToOrgObjectAPIName(){
		return this.toorgobjectAPIName;
	}

	public String getFromOrgUserName(){
		return this.fromorguserName;
	}

	public String getToOrgUserName(){
		return this.toorguserName;
	}

	public String getFromOrgUserPwd(){
		return this.fromorguserPwd;
	}

	public String getToOrgUserPwd(){
		return this.toorguserPwd;
	}
  	
	public ArrayList<String> getFromOrgFields(){
		return this.fromorglist;
	}
	
	public ArrayList<String> getToOrgFields(){
		return this.toorglist;	
	}
	/**
     * For Migration, we need query, delete, and insert object to achieve functionality
     * 
     * @return void
	 * @throws ConnectionException;IOException
     */
	public void startRun() throws ConnectionException, IOException, AsyncApiException, InterruptedException{
		//Insert is = new Insert();
		//is.runCSV(getFromOrgObjectAPIName(),getFromOrgUserName(),getFromOrgUserPwd(),"query.csv");
		Query qy = new Query();
		qy.runCSV(getFromOrgObjectAPIName(),getFromOrgUserName(),getFromOrgUserPwd(),getFromOrgFields());
		Thread.sleep(2000L);
		Upsert up = new Upsert();
		up.runCSV(getToOrgObjectAPIName(),getToOrgUserName(),getToOrgUserPwd(),"query.csv");
	}
}
