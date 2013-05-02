/**
 * This is insert object that used for insert csv file into an custom object on an org 
 *
 *
 */

 
package com.salesforce.service;


import java.io.*;
import java.util.*;

import com.salesforce.service.bulk.Insert;
import com.salesforce.service.MappingBean;
import com.salesforce.factory.*;
import com.salesforce.ui.MappingChanger;

import com.sforce.async.*;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;

public class Insertor implements Tool{
	
	private String toorgobjectAPIName;
	private String toorguserName;
	private String toorguserPwd;
	private MappingBean mb;
	
	public Insertor(MappingBean mb){
		this.toorgobjectAPIName = mb.getToOrgObject();
		this.toorguserName = mb.getToOrgUserName();
		this.toorguserPwd = mb.getToOrgPassword();
		this.mb = mb;	
	}
	
	public String getToolInfo(){
		return "Inserting";
	}

	public void setToOrgObjectAPIName(String name){
		this.toorgobjectAPIName = name;
	}
	
	public void setToOrgUserName(String n){
		this.toorguserName = n;
	}

	public void setToOrgUserPwd(String usrpwd){
		this.toorguserPwd = usrpwd;
	}
	
	public String getToOrgObjectAPIName(){
		return this.toorgobjectAPIName;
	}

	public String getToOrgUserName(){
		return this.toorguserName;
	}
	
	public String getToOrgUserPwd(){
		return this.toorguserPwd;
	}
	
    /**
     * For insertion, we need insert bulk api to achieve functionality
     * 
     * @return void
     * @throws ConnectionException
     * @throws IOException
     */		
	public void startRun() throws ConnectionException, IOException, AsyncApiException, InterruptedException{
		Insert is = new Insert();
		is.runCSV(getToOrgObjectAPIName(),getToOrgUserName(),getToOrgUserPwd(),"insert.csv");
	}
}

