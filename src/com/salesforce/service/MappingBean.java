/**
 * The Mapping Rule bean
 *
 *
 *
 */

package com.salesforce.service;


import java.util.ArrayList;


public class MappingBean{

	private String toolname;
	private String fromorgusername;
	private String toorgusername;
	private String fromorgpassword; 		
	private String toorgpassword;
	private String fromorgobjectname;
	private String toorgobjectname;
	private ArrayList<String> fromList, toList;
		

	public MappingBean(){
		if(fromList == null){
			fromList = new ArrayList<String>();
		}
        if(toList == null){
			toList = new ArrayList<String>();
		}	
	}

	public String getRoot(){
		return toolname;
	}

	public ArrayList<String> getFromList(){
		return fromList;
	}		

	public ArrayList<String> getToList(){
		return toList;
	}
	
	public String getFromOrgUserName(){
		return  fromorgusername;
	}

	public String getFromOrgPassword(){
		return fromorgpassword;
	}
	
	public String getFromOrgObject(){
		return fromorgobjectname;
	}

	public String getToOrgUserName(){
		return toorgusername;
	}	

	public String getToOrgPassword(){
		return toorgpassword;
	}
	
	public String getToOrgObject(){
		return toorgobjectname;
	}
	
	public void setRoot(String s){
		this.toolname = s;
	}

	public void setFromOrgUserName(String s){
		this.fromorgusername = s;
	}	

	public void setFromOrgUserPassword(String p){
		this.fromorgpassword = p;
	}	
	
	public void setFromOrgObject(String o){
		this.fromorgobjectname = o;
	}

	public void setToOrgUserName(String s){
		this.toorgusername = s;
	}

	public void setToOrgUserPassword(String p){
		this.toorgpassword = p;
	}

	public void setToOrgObject(String o){
		this.toorgobjectname = o;
	}
}

