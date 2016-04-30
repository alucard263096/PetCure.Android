package com.helpfooter.steve.petcure.loader;

import android.content.Context;
import android.provider.ContactsContract;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by steve on 2016/4/29.
 */
public class WebXmlLoader extends WebLoader {
    public WebXmlLoader(Context ctx, String url, HashMap<String, String> urlStaticParam) {
        super(ctx, url, urlStaticParam);
    }

    public WebXmlLoader(Context ctx, String url) {
        super(ctx, url);
    }
    @Override
    public Object processData(Object res){
        ArrayList<HashMap<String,String>> xmlMap=ChangeXmlToArray(String.valueOf(res));
        return xmlMap;
    }
    protected ArrayList<HashMap<String,String>>  ChangeXmlToArray(String  is) {

        ArrayList<HashMap<String,String>> lstRow=new ArrayList<HashMap<String,String>>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(is)));
            Element elmtInfo = doc.getDocumentElement();

            NodeList StartNode = elmtInfo.getChildNodes();
            //Node tableNode=StartNode.item(0);
            //NodeList rowNodeList=tableNode.getChildNodes();
            //Log.i("XmlDataTableReaderRowNumber",String.valueOf(StartNode.getLength()));
            for (int i = 0; i < StartNode.getLength(); i++){

                Node rowNode = StartNode.item(i);
                NodeList colList= rowNode.getChildNodes();
                HashMap<String,String> dictCol=getColDict(colList);

                lstRow.add(dictCol);
            }
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return lstRow;
    }


    private HashMap<String,String> getColDict(NodeList nl){
        HashMap<String,String> dictRowValue=new HashMap<String,String>();
        for (int i=0;i<nl.getLength();i++){
            dictRowValue.put(nl.item(i).getNodeName(),nl.item(i).getTextContent());
        }
        return  dictRowValue;
    }
}
