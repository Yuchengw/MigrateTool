/**
 * This is the upper-level interface program that deals with the xml mapping file
 *
 *
 *
 *
 */

package com.salesforce.ui;


import com.salesforce.ui.Runner;
import com.salesforce.service.MappingBean;

import java.io.File;
import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class MappingParser{

	private MappingBean mb;

	public MappingParser(){
		//TODO: embeded with log
		System.out.println("*****************Start Parsing*****************");
		//for future multi-thread development
		mb = new MappingBean();
	}	
	
	/**
     * Function that check if mapping file exists
     *
     */
	public MappingBean parse() throws FileNotFoundException{
		File mappingfile = new File("config.xml");
		if(!mappingfile.exists()){
			throw new FileNotFoundException("config.xml is not in current directory");	
		}
		return parseImpl(mappingfile);
	}

	/**
     * Implementation Function that parse xml file construct mapping Bean
     *
     * @param File 
     * @return MappingBean
     */
	public MappingBean parseImpl(File mappingfile){
		try{
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(mappingfile);
		    doc.getDocumentElement().normalize();
		    //TODO: should add logger
		    System.out.println("root of xml file" + doc.getDocumentElement().getNodeName());	
		    mb.setRoot(doc.getDocumentElement().getNodeName());
		    //Extract vlaues from mapping file
		    setFromOrg(mb,doc);
		    setToOrg(mb,doc);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		    return mb;
	}
	
	public void setFromOrg(MappingBean mb, Document doc){
		NodeList nodes = doc.getElementsByTagName("fromorg").item(0).getChildNodes();
		ArrayList<String> mbfromlist = mb.getFromList();
		Node an;
		for( int i = 0; i < nodes.getLength(); i++){		
			Node node = nodes.item(i);
		    if(node.getNodeType() == Node.ELEMENT_NODE){
				NodeList nl2 = node.getChildNodes();
				for(int k = 0; k < nl2.getLength(); k++){
					an = nl2.item(k);
					if(node.getNodeName() == "fusername"){
						mb.setFromOrgUserName(an.getNodeValue());
					}else if(node.getNodeName() == "fuserpassword"){
						mb.setFromOrgUserPassword(an.getNodeValue());
					}else if(node.getNodeName() == "fobject"){
						mb.setFromOrgObject(an.getNodeValue());
					}else if(node.getNodeName() == "fsql"){
						mb.setFromOrgSQL(an.getNodeValue());
					}else{
		    	   	 	mbfromlist.add(an.getNodeValue());
					}
				}
		    }
    	 }	
	}

	public void setToOrg(MappingBean mb, Document doc){
		NodeList nodes = doc.getElementsByTagName("toorg").item(0).getChildNodes();
		ArrayList<String> mbtolist = mb.getToList();	
		Node an;
		if( mbtolist == null) System.out.println("to list is null");
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = nodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				NodeList nl2 = node.getChildNodes();
				for( int k = 0; k <nl2.getLength();k++){	
					 an = nl2.item(k);
				     if(node.getNodeName() == "tusername"){
				     	mb.setToOrgUserName(an.getNodeValue());
				     }else if(node.getNodeName() == "tuserpassword"){
				     	mb.setToOrgUserPassword(an.getNodeValue());
					 }else if(node.getNodeName() == "tobject"){
						mb.setToOrgObject(an.getNodeValue());
					 }else if(node.getNodeName() == "externalId"){
						mb.setToOrgExternalId(an.getNodeValue());
				     }else{
				     	mbtolist.add(an.getNodeValue());
				     }
				}
			}
		}
	}
	
	public MappingBean getMappingBean(){
		return mb;
	}	
	
}
	

