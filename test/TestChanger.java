
package com.salesforce.test;

import com.salesforce.ui.MappingChanger;
import com.salesforce.ui.MappingParser;
import com.salesforce.service.MappingBean;

import java.util.ArrayList;
import java.io.*;

public class TestChanger{

	public TestChanger(){}
	
	public static void main(String[] args) throws FileNotFoundException,IOException{
		MappingParser mp = new MappingParser();		
	    mp.parse();
		MappingBean mb = mp.getMappingBean();
		MappingChanger mc = new MappingChanger();
		mc.change("query.csv",mb);
	}

}
