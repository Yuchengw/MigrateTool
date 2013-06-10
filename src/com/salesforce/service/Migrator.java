/**
 * This is the migrate object that combines delete, query and update objects 
 *
 *
 */


package com.salesforce.service;


import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import com.salesforce.service.bulk.*;
import com.salesforce.service.MappingBean;
import com.salesforce.factory.*;
import com.salesforce.ui.MappingChanger;

import com.sforce.async.*;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
//import com.sforce.ws.ConnectionConfig;
import com.salesforce.service.lib.log.SuperLog;


public class Migrator implements Tool{

	private String fromorgobjectAPIName;
	private String fromorguserName;
	private String fromorguserPwd;
	private String fromorgwhere;
	private String toorgobjectAPIName;
	private String toorguserName;
	private String toorguserPwd;
	private String toorgexternalId;
	private String loglevel;
	private ArrayList<String> fromorglist;
	private ArrayList<String> toorglist;
	private MappingChanger mc;
	private MappingBean mb;
	private static Logger logger = null;

	// default constructor
	public Migrator(MappingBean mb, String loglevel){
		this.fromorgobjectAPIName = mb.getFromOrgObject();
		this.fromorguserName = mb.getFromOrgUserName();
		this.fromorgwhere = mb.getFromOrgWhere();
		this.fromorguserPwd = mb.getFromOrgPassword();
		this.toorgobjectAPIName = mb.getToOrgObject();
		this.toorguserName = mb.getToOrgUserName();
		this.toorguserPwd = mb.getToOrgPassword();
		this.fromorglist = mb.getFromList();
		this.toorglist = mb.getToList();
		this.toorgexternalId = mb.getToOrgExternalId();
		this.loglevel = loglevel;
		mc = new MappingChanger();
		this.mb = mb;
		this.logger = SuperLog.open(Migrator.class,loglevel);
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

	public void setFromOrgWhere(String w){
		this.fromorgwhere = w;
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

	public String getFromOrgWhere(){
		return this.fromorgwhere;
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
  	
	public String getToOrgExternalId(){
		return this.toorgexternalId;
	}

	public ArrayList<String> getFromOrgFields(){
		return this.fromorglist;
	}
	
	public ArrayList<String> getToOrgFields(){
		return this.toorglist;	
	}
	/**
     * For Migration, we need query, delete, and insert api to achieve functionality
     * 
     * @return void
	 * @throws ConnectionException
     * @throws IOException
     */
	public void startRun() throws ConnectionException, IOException, AsyncApiException, InterruptedException{
		
		Query qy = new Query(this.loglevel);
		if(getFromOrgWhere() != null){
				qy.runCSV(getFromOrgObjectAPIName(),getFromOrgUserName(),getFromOrgUserPwd(),getFromOrgFields(),getFromOrgWhere());
		}else if(getFromOrgWhere() == null && getFromOrgFields() != null){
				qy.runCSV(getFromOrgObjectAPIName(),getFromOrgUserName(),getFromOrgUserPwd(),getFromOrgFields());
		}
		Thread.sleep(2000L);
		mc.change("query.csv",this.mb);	
		Upsert up = new Upsert(this.loglevel);
		if(getToOrgExternalId() == null){
			logger.info("Has not notify to org's external id");
		}
		up.runCSV(getToOrgObjectAPIName(),getToOrgUserName(),getToOrgUserPwd(),getToOrgExternalId(),"query.csv");
		//Insert is = new Insert();
		//is.runCSV(getFromOrgObjectAPIName(),getFromOrgUserName(),getFromOrgUserPwd(),"query.csv");
	}
}
