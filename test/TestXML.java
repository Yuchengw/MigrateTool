
package com.salesforce.test;

import com.salesforce.ui.MappingParser;
import com.salesforce.service.MappingBean;

import java.util.ArrayList;
import java.io.*;

public class TestXML{

	public TestXML(){}

	public static void checkMappingBean(MappingBean mb){
		System.out.println("Root name: " + mb.getRoot());
		System.out.println("From Org User Name: " + mb.getFromOrgUserName());
		System.out.println("To Org User Name: " + mb.getToOrgUserName());
		System.out.println("From org User Password: " + mb.getFromOrgPassword());
		System.out.println("To org User Password: " + mb.getToOrgPassword());
		System.out.println("From org object name : " + mb.getFromOrgObject());
		System.out.println("To org object name : " + mb.getToOrgObject());
		ArrayList<String> fromorglist = mb.getFromList();
		ArrayList<String> toorglist = mb.getToList();
		System.out.println("From org info : " );
		for(int i = 0; i < fromorglist.size(); i++){
			System.out.print(fromorglist.get(i) + "   ");
		}
		System.out.println(" ");
		System.out.println("To org info : " );
		for(int i = 0; i < toorglist.size(); i++){
			System.out.print(toorglist.get(i) + "   ");
		}
		System.out.println("  ");
		StringBuilder query = new StringBuilder();
       	query.append("SELECT ");
        for(int i = 0; i < fromorglist.size(); i++){
            query.append(fromorglist.get(i) + " "); 
        }
        query.append("FROM ");
        query.append("Test");
		System.out.println(" test query " + query);
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		MappingParser mp = new MappingParser();		
		mp.parse();
		MappingBean mb = mp.getMappingBean();
		checkMappingBean(mb);
		
	}

}
