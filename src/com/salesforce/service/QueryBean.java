/**
 * The Query Rule bean
 *
 *
 */
package com.salesforce.service;


import java.util.ArrayList;

import com.salesforce.service.Bean;


public class QueryBean implements Bean{

	private String toolname;
	private String queryorgusername;
	private String queryorgpassword;
	private String queryorgobjectname;
	private String queryorgwhere;

	private ArrayList<String> queryList;
	
	public QueryBean(){
		queryList = new ArrayList<String>();
	}
	
	public String getRoot(){
		return toolname;
	}
	
	public ArrayList<String> getQueryList(){
		return queryList;
	}

	public String getQueryOrgWhere(){
		return queryorgwhere;
	}

	public String getQueryOrgUserName(){
		return queryorgusername;
	}

	public String getQueryOrgPassword(){
		return queryorgpassword;
	}
	
	public String getQueryOrgObject(){
		return queryorgobjectname;
	}	

	public void setRoot(String s){
		this.toolname = s;
	}

	public void setQueryOrgUserName(String s){
		this.queryorgusername =  s;
	}

	public void setQueryOrgPassword(String p){
		this.queryorgpassword = p;
	}	

	public void setQueryOrgObject(String o){
		this.queryorgobjectname = o;
	}

	public void setQueryOrgWhere(String w){
		this.queryorgwhere = w;
	}
}
