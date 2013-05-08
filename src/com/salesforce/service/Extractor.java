/**
 * This is the query object query one object's fields or according self-defined sql
 *
 *
 */


package com.salesforce.service;


import java.io.*;
import java.util.*;

import com.salesforce.service.bulk.*;
import com.salesforce.service.QueryBean;
import com.salesforce.factory.*;

import com.sforce.async.*;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
//import com.sforce.ws.ConnectionConfig;


public class Extractor implements Tool{

	private String queryorgobjectAPIName;
	private String queryorguserName;
	private String queryorguserPwd;
	private String queryorgsql;
	private ArrayList<String> queryorglist;
	private QueryBean qb;

	// default constructor
	public Extractor(QueryBean qb){
		this.queryorgobjectAPIName = qb.getQueryOrgObject();
		this.queryorguserName = qb.getQueryOrgUserName();
		this.queryorgsql = qb.getQueryOrgSQL();
		this.queryorguserPwd = qb.getQueryOrgPassword();
		this.queryorglist = qb.getQueryList();
		this.qb = qb;
	}
	
	// implements tool interface
	public String getToolInfo(){
		return "Querying ";
	} 

	// setter and getters
	public void setQueryOrgObjectAPIName(String name){
		this.queryorgobjectAPIName = name;
	}


	public void setQueryOrgUserName(String usrname){
		this.queryorguserName = usrname;
	}

	public void setQueryOrgSQL(String sql){
		this.queryorgsql = sql;
	}

	public void setQueryOrgUserPwd(String usrpwd){
		this.queryorguserPwd = usrpwd;	
	}
	
	public String getQueryOrgObjectAPIName(){
		return this.queryorgobjectAPIName;
	}
	
	public String getQueryOrgUserName(){
		return this.queryorguserName;
	}

	public String getQueryOrgSQL(){
		return this.queryorgsql;
	}

	public String getQueryOrgUserPwd(){
		return this.queryorguserPwd;
	}

	public ArrayList<String> getQueryOrgFields(){
		return this.queryorglist;
	}
	/**
     * For Query
     * 
     * @return void
	 * @throws ConnectionException
     * @throws IOException
     */
	public void startRun() throws ConnectionException, IOException, AsyncApiException, InterruptedException{
		Query qy = new Query();
		if(getQueryOrgSQL() != null){
				qy.runCSV(getQueryOrgObjectAPIName(),getQueryOrgUserName(),getQueryOrgUserPwd(),getQueryOrgSQL());
		}else if(getQueryOrgSQL() == null && getQueryOrgFields() != null){
				qy.runCSV(getQueryOrgObjectAPIName(),getQueryOrgUserName(),getQueryOrgUserPwd(),getQueryOrgFields());
		}
	}
}
