/**
 * The query factory is the second factory of Tool factory
 *
 */


package com.salesforce.factory;


import com.salesforce.service.Extractor;
import com.salesforce.service.QueryBean;
import com.salesforce.service.Bean;


public class QueryFactory implements ToolFactory{

	public Tool useTool(Bean qb){
		return new Extractor((QueryBean)qb);
	}

}
