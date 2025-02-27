package com.wimoor.amazon.util;
import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlOper
{
    /**
     *构造函数说明：       <p>
     *参数说明：   <p>
    **/
    private XmlOper()
    {        
    }
     
 
    public static NodeList getNodeList(Node parent)
    {
        return parent.getChildNodes();
    }
     
 
    public static ArrayList<Node> getNodesByName(Node parent,String name)
    { if(parent==null||parent.getChildNodes()==null)return null;
        ArrayList<Node> resList=new ArrayList<Node>();
        NodeList nl=getNodeList(parent);
      
        for(int i=0;i<nl.getLength();i++)
        {
            Node nd=nl.item(i);
            if(nd.getNodeName().equals(name))
            {
                resList.add(nd);
            }
        }
        return resList;
    }
    public static  Node  getNodeByName(Node parent,String name)
    { if(parent==null||parent.getChildNodes()==null)return null;
        NodeList nl=parent.getChildNodes();
        for(int i=0;i<nl.getLength();i++)
        {
            Node nd=nl.item(i);
            if(nd.getNodeName().equals(name))
            {
                return nd;
            }
        }
        return null;
    }
    public static  String  getNodeValueByName(Node parent,String name)
    {
        if(parent==null||parent.getChildNodes()==null)return null;
        NodeList nl=parent.getChildNodes();
        for(int i=0;i<nl.getLength();i++)
        {
            Node nd=nl.item(i);
            if(nd.getNodeName().equals(name))
            {   
                return nd.getFirstChild().getNodeValue();
            }
        }
        return null;
    }
  
    public static String getNodeName(Node element)
    {
        return element.getNodeName();
    }
  
    public static String getNodeValue(Node element)
    { if(element==null||element.getChildNodes()==null)return null;
        NodeList nl=element.getChildNodes();
        for(int i=0;i<nl.getLength();i++)
        {
            if(nl.item(i).getNodeType()==Node.TEXT_NODE)//是一个Text Node
            {            
                return element.getFirstChild().getNodeValue();
            }
        }   
        return null;
    }
  
     
    public static String getNodeAttr(Node element,String attr)
    {
        return element.getAttributes().getNamedItem(attr).toString();
    }
}