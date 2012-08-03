package com.itcast.collage.utils;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.itcast.collage.bean.Container;



/**
 * Class use for parse view XML file. 
 * @author beyondsoft
 */
public class XMLHandler extends DefaultHandler {

	/**
	 * root tag name
	 */
	public static final String TAG_NAME_VIEWGROUP = "ViewGroup";
	
	/**
	 * view tag name 
	 */
	public static final String TAG_NAME_VIEW = "View";
	
	/**
	 * view tags' attributes count
	 */
	public int attrCount = 5;
	
	/**
	 * view tag a attributes
	 */
	public static final String TAG_ATTR_ID = "id";
	public static final String TAG_ATTR_PIVOTX = "topX";
	public static final String TAG_ATTR_PIVOTY = "topY";
	public static final String TAG_ATTR_WIDTH = "width";
	public static final String TAG_ATTR_HEIGHT = "height";
	
 
	private ArrayList<Container> containers;
 
	
	public ArrayList<Container> getContainers() {
		return containers;
	}

	/**
	 * support max view tags' count
	 */
	private static final int VIEW_TAG_MAX_COUNT = 7;
	
	/**
	 * support min view tags' count
	 */
	private static final int VIEW_TAG_MIN_COUNT = 3;
	
	/**
	 * indicate whole view tags' count 
	 */
	private int viewTagCount = 0;
	
	/**
	 * Constructor 
	 * @param callBack
	 */
 
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		containers = new ArrayList<Container>();
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		
		// support view tag from 3 - 7
		if (viewTagCount < VIEW_TAG_MIN_COUNT
				|| viewTagCount > VIEW_TAG_MAX_COUNT) {
			throw new IllegalArgumentException("specific view tag count must less than 7 more than 3.");
		}
	 
		
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (localName != null && !localName.equals("")) {
			if (TAG_NAME_VIEW.equals(localName)) {
				final Container container = new Container();
				final int len =  attributes.getLength();
				
				if(len < attrCount) {
					throw new IllegalArgumentException("must specific whole attribute value to view tag.");
				}
				
				for (int i = 0; i < len; i++) {
					
					final String attrName = attributes.getLocalName(i).trim();
					final String attrValue = attributes.getValue(i).trim();

					if(attrName != null && !attrName.equals("")) {
						if (attrValue == null || "".equals(attrValue)) {
							throw new IllegalArgumentException(
									"a attribute must hava a value.");
						}
						
						if (TAG_ATTR_PIVOTX.equals(attrName)) {
							container.setX(Integer.valueOf(attrValue));
						} else if (TAG_ATTR_PIVOTY.equals(attrName)) {
							container.setY(Integer.valueOf(attrValue));
						} else if (TAG_ATTR_WIDTH.equals(attrName)) {
							container.setWidth(Integer.valueOf(attrValue));
						} else if (TAG_ATTR_HEIGHT.equals(attrName)) {
							container.setHeight(Integer.valueOf(attrValue));
						} else if(TAG_ATTR_ID.equals(attrName)) {
							container.setId(Integer.valueOf(attrValue));
						}
					}
				}
				viewTagCount++;
				containers.add(container);
			}
		}
	}
}
