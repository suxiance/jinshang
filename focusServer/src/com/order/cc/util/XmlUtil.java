package com.order.cc.util;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 
 * ����  DOM4j XML ������
 * @author С����
 * @created 2017��8��16�� 
 */
public final class XmlUtil {
    /**
     * ʹ�õĲ�ѯ����ڵ��·��
     */
    private static final String SELECT_PATH = ".//";
    /**
     * Ĭ�ϱ���
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    // Constructors
    /** default constructor */
    private XmlUtil() {

    }

    
    /**
     * 
     * ����  ȡ�õ�ǰ�ڵ��ض��ӽڵ�����ģ�ֻȡ��һ��
     * @author С����
     * @created 2017��8��16�� 
     * @param element ��ǰXML�ڵ�
     * @param fieldCode ��������
     * @return
     */
    public static String getSonFieldContent(final Element element, final String fieldCode) {
        if (element == null) {
            return null;
        }

        String content = null;
        Element ele = element.element(fieldCode);
        if (ele != null) {
            content = ele.getText();
        }
        return content;
    }


    /**
     * 
     * ���� ȡ�õ�ǰ�ڵ��ض�����ڵ�����ģ�ֻȡ��һ��
     * @author С����
     * @created 2017��8��16�� 
     * @param element ������xmlԪ��
     * @param fieldCode ��λ����
     * @return
     */
    public static Element getDesFieldElement(final Element element, final String fieldCode) {
        Element tagertElement = null;
        @SuppressWarnings("unchecked")
        List<Element> list = element.selectNodes(SELECT_PATH + fieldCode);
        if (list.size() > 0) {
            tagertElement = list.get(0);
        }

        return tagertElement;
    }

    /**
     * 
     * ����  ȡ�õ�ǰ�ڵ��ض�����ڵ�����ģ�ֻȡ��һ��
     * @author С����
     * @created 2017��8��16�� 
     * @param text ���뱨��
     * @param fieldCode  ��ǩ����
     * @return  ��ǩ����
     * @throws DocumentException
     */
    public static String getDesFieldText(final String text, final String fieldCode) throws DocumentException {
        Element rootEle = DocumentHelper.parseText(text).getRootElement();
        Element desFieldEle = getDesFieldElement(rootEle, fieldCode);
        if (desFieldEle != null) {
            return desFieldEle.getText();
        } else {
            return null;
        }
    }
    public static String getRootText(final String text) throws DocumentException {
    	Element rootEle = DocumentHelper.parseText(text).getRootElement();
    	if (rootEle != null) {
    		return rootEle.getText();
    	} else {
    		return null;
    	}
    }
    public static void main(String[] args) throws DocumentException {
		String s = "<?xml version=\"1.0\" encoding=\"utf-8\"?><string xmlns=\"http://tempuri.org/\">123</string>";
        System.out.println(getRootText(s));
	
    }

    
    /**
     * 
     * ���� ȡ�õ�ǰ�ڵ��ض�����ڵ�����ģ��ƶ�����
     * @author С����
     * @created 2017��8��16�� 
     * @param text ���뱨��
     * @param fieldCode  ��ǩ����
     * @param index �����Ԫ������
     * @return ��ǩ����
     * @throws DocumentException
     */
    public static String getDesFieldTextByIndex(final String text, final String fieldCode, final int index)
            throws DocumentException {
        Element rootEle = DocumentHelper.parseText(text).getRootElement();
        List<Element> eleList = (List<Element>) rootEle.selectNodes(SELECT_PATH + fieldCode);
        if (eleList.size() > index) {
            return eleList.get(index).getText();
        } else {
            return null;
        }

    }

    
    /**
     * 
     * ���� ȡ������ڵ㼯��
     * @author С����
     * @created 2017��8��16�� 
     * @param element ������xmlԪ��
     * @param fieldCode ��λ����
     * @return ��������
     */
    @SuppressWarnings("unchecked")
    public static List<Element> getDesFieldElementList(final Element element, final String fieldCode) {
        return element.selectNodes(SELECT_PATH + fieldCode);

    }

    
    /**
     * 
     * ����  ȡ�õ�ǰ�ڵ�������ӵ�е�ǰ���Ե�Ԫ��
     * @author С����
     * @created 2017��8��16�� 
     * @param element ��ǰԪ��
     * @param fieldCode ������������
     * @return
     */
    public static List<Element> getEleListByAttr(final Element element, final String fieldCode) {
        // ��������
        List<Element> eleList = new ArrayList<Element>();

        // ���Ҳ�ѭ�����
        @SuppressWarnings("unchecked")
        List<Attribute> attrList = element.selectNodes(SELECT_PATH + "@" + fieldCode);
        for (Attribute attr : attrList) {
            eleList.add(attr.getParent());
        }

        // ����
        return eleList;
    }

    
    /**
     * 
     * ����  ȡ�õ�ǰ�ڵ�������ӵ�е�ǰ���Ե�Ԫ��
     * @author С����
     * @created 2017��8��16�� 
     * @param msgText ��ǰԪ��
     * @param path ������������
     * @return
     */
    public static List<Element> getEleListByPath(final String msgText, final String path) {
        // ��������
        List<Element> eleList = new ArrayList<Element>();

        try {
            Document document;
            document = DocumentHelper.parseText(msgText);
            eleList = document.selectNodes(path);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // ����
        return eleList;
    }

    
    /**
     * 
     * ����  һ�������Ӷ���ӽڵ�
     * @author С����
     * @created 2017��8��16�� 
     * @param fatherEle ���׽ڵ�
     * @param sonEleList Ҫ���ӵ��ӽڵ�Ԫ��
     */
    public static void addElements(final Element fatherEle, final List<Element> sonEleList) {
        for (Element ele : sonEleList) {
            fatherEle.add((Element) ele.clone());
        }
    }

    /**
     * 
     * ���� ������Ԫ��
     * @author С����
     * @created 2017��8��16�� 
     * @param fatherEle ���׽ڵ�
     * @param sonEleName ���ӽڵ���
     * @param sonEleContent ���ӽڵ�����
     * @return ���ӽڵ�
     */
    public static Element addElement(final Element fatherEle, final String sonEleName, final String sonEleContent) {
        // �����ӽڵ�
        Element ele = DocumentHelper.createElement(sonEleName);
        if (StringUtils.isNotBlank(sonEleContent)) {
            ele.setText(sonEleContent);
        }

        // ����
        fatherEle.add(ele);

        // ����
        return ele;
    }

    
    /**
     * 
     * ���� ������һ�����ӽڵ㣬�����������Ϊ�ղ�����
     * @author С����
     * @created 2017��8��16�� 
     * @param fatherEle ���׽ڵ�
     * @param sonEleName
     * @param sonEleContent
     * @return
     */
    public static Element addFinalSonElement(final Element fatherEle, final String sonEleName,
            final String sonEleContent) {
        // �����ӽڵ�
        Element ele = null;

        // ����
        if (StringUtils.isNotBlank(sonEleContent)) {
            ele = DocumentHelper.createElement(sonEleName);
            ele.setText(sonEleContent);
            // ����
            fatherEle.add(ele);
        }

        // ����
        return ele;
    }

   
    /**
     * 
     * ���� ȡ��XMl�ַ����е�ָ��·���ַ�
     * @author С����
     * @created 2017��8��16�� 
     * @param text
     * @param url
     * @return
     */
    public static String getUrlElementText(final String text, final String url) {
        // ����ֵ
        String resultText = null;

        // ת��XML
        Element busiElement;
        try {
            busiElement = DocumentHelper.parseText(text).getRootElement();
            List<Element> eleList = busiElement.selectNodes(url);
            if (eleList.size() > 0) {
                resultText = ((Element) busiElement.selectNodes(url).get(0)).getText();
            }
        } catch (DocumentException e) {
            e.printStackTrace();

        }

        // ����
        return resultText;
    }

    /**
     * 
     * ����
     * @author С����
     * @created 2017��8��16��
     * @param doc
     * @param url
     * @return
     */
    public static String getUrlElementText(Document doc, final String url) {
        Node node = doc.selectSingleNode(url);
        if (node == null) {
            return null;
        }
        return node.getText();
    }

    /**
     * 
     * ���� ��ʽ��xml�ַ���
     * @author С����
     * @created 2017��8��16��
     * @param xmlText xml�ַ���
     * @return ��ʽ������ַ�
     * @throws Exception
     */
    public static String formatXml(final String xmlText) throws Exception {
        // �����ַ���
        String reText;

        try {
            reText = formatXml(xmlText, DEFAULT_ENCODING);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        // ����
        return reText;
    }

    
    /**
     * 
     * ���� ��ʽ��xml�ַ���
     * @author С����
     * @created 2017��8��16������11:26:19
     * @param xmlText xml�ַ���
     * @param inEncoding �ַ�����
     * @return ��ʽ������ַ�
     * @throws Exception
     */
    public static String formatXml(final String xmlText, final String inEncoding) throws Exception {
        String encoding = inEncoding;
        // �����ַ���
        String reText;

        if (StringUtils.isBlank(encoding)) {
            encoding = DEFAULT_ENCODING;
        }

        // ת��
        try {
            // ���ַ�����ʽת����document����
            Document document = DocumentHelper.parseText(xmlText);

            // ע��,�����ַ�ʽ������ָ����ʽ��format
            OutputFormat format = OutputFormat.createPrettyPrint();

            format.setEncoding(encoding);

            // ����String�����
            StringWriter out = new StringWriter();

            // ��װString��
            XMLWriter writer = new XMLWriter(out, format);

            // ����ǰ��document����д��ײ���out��
            writer.write(document);
            writer.close();
            reText = out.toString();
        } catch (Exception e) {
            String errTagMsg = "���ݸ�ʽ��ʧ�ܣ�";
            throw new Exception(errTagMsg);
        }

        // ����
        return reText;
    }


    
    /**
     * 
     * ���� ����XMLԪ�ص�ֵ����Ӧ�Ķ������Ե���
     * @author С����
     * @created 2017��8��16��
     * @param obj  ���ö���
     * @param ele  XMLԪ��
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static void copyEleToAttr(final Object obj, final Element ele)
            throws IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        // ��ȡf�����Ӧ���е�����������
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // ����ÿ�����ԣ���ȡ������
            String fieldName = fields[i].getName();
            fields[i].setAccessible(true);
            /**
             * String����ֱ�ӽ��и�ֵ������Ǹ����࣬������һ����ֵ
             */
            if (fields[i].getType().equals(String.class)) {
                String eleContent = XmlUtil.getSonFieldContent(ele, fieldName);
                if (eleContent != null && !eleContent.equals("")) {
                    fields[i].set(obj, eleContent);
                }
            } else if (fields[i].getType().equals(List.class)) {
                String eleName = fieldName.substring(0, fieldName.length() - 4);
                List<Element> eleList = ele.elements(eleName);
                List<Object> field = (List<Object>) fields[i].get(obj);

                // ȡ�÷�������
                Type fc = fields[i].getGenericType();
                ParameterizedType pt = (ParameterizedType) fc;
                Class driveClass = (Class) pt.getActualTypeArguments()[0];
                try {
                    for (Element singleEle : eleList) {
                        Object singObjct = driveClass.newInstance();
                        // �ж��Ƿ����ַ���
                        if (driveClass.equals(String.class)) {
                            singObjct = singleEle.getText();

                        } else {
                            copyEleToAttr(singObjct, singleEle);
                        }
                        field.add(singObjct);
                    }
                } catch (Exception e) {
                    for (Element singleEle : eleList) {
                        String eleContent = singleEle.getTextTrim();
                        if (eleContent != null && !eleContent.equals("")) {
                            field.add(eleContent);
                        }
                    }
                }

            } else {
                Object fieldObject = fields[i].get(obj);
                Element fieldEle = ele.element(fieldName);
                if (fieldObject != null && fieldEle != null) {
                    copyEleToAttr(fieldObject, fieldEle);
                }
            }
        }
    }
    /**
     * 
     * ���� ����XMLԪ�ص�����ֵ����Ӧ�Ķ������Ե���
     * @author sxc
     * @created 2019��1��24��15:48:58
     * @param obj  ���ö���
     * @param ele  XMLԪ��
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static void copyEleAttrToObjAttr(final Object obj, final Element ele)
    		throws IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
    	
    	// ��ȡf�����Ӧ���е�����������
    	Field[] fields = obj.getClass().getDeclaredFields();
    	for (int i = 0, len = fields.length; i < len; i++) {
    		// ����ÿ�����ԣ���ȡ������
    		String fieldName = fields[i].getName();
    		fields[i].setAccessible(true);
    		/**
    		 * String����ֱ�ӽ��и�ֵ
    		 */
    		if (fields[i].getType().equals(String.class)) {
    			List<Attribute> attrList = ele.attributes();
    			if (attrList != null) {
    				for (Attribute attribute : attrList) {
						if(attribute.getName().equals(fieldName)){
							fields[i].set(obj, attribute.getValue());
						}
					}
    			}
    		} 
    	}
    }

    
    /**
     * 
     * ����  ���ƶ�Ӧ�Ķ������Ե���XMLԪ�ص�ֵ��
     * @author С����
     * @created 2017��8��16��
     * @param obj ���ö���
     * @param ele XMLԪ��
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public static void copyAttrToEle(final Object obj, final Element ele)
            throws IllegalArgumentException, IllegalAccessException {

        // ��ȡf�����Ӧ���е�����������
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // ����ÿ�����ԣ���ȡ������
            String fieldName = fields[i].getName();
            fields[i].setAccessible(true);

            // ���˲�����Ķ���
            if (Modifier.isStatic(fields[i].getModifiers())) {
                continue;
            }

            /**
             * String����ֱ�ӽ��и�ֵ������Ǹ����࣬������һ����ֵ
             */
            if (fields[i].getType().equals(String.class)) {
                XmlUtil.addFinalSonElement(ele, fieldName, (String) fields[i].get(obj));
            } else if (fields[i].getType().equals(List.class)) {
                List<Object> fieldList = (List<Object>) fields[i].get(obj);
                for (Object object : fieldList) {
                    if (object.getClass().equals(String.class)) {
                        XmlUtil.addElement(ele, fieldName, (String) object);
                    } else {
                        copyAttrToEle(object, XmlUtil.addElement(ele, fieldName, null));
                    }
                }
            } else {
                Object fieldObject = fields[i].get(obj);
                if (fieldObject != null) {
                    Element fieldEle = XmlUtil.addElement(ele, fieldName, null);
                    copyAttrToEle(fields[i].get(obj), fieldEle);
                }
            }
        }

    }
    /**
     * 
     * ����  ���ƶ�Ӧ�Ķ������Ե���XMLԪ��������
     * @created 2019��1��24��13:43:56
     * @param obj ���ö���
     * @param ele XMLԪ��
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws UnsupportedEncodingException 
     */
    @SuppressWarnings("unchecked")
    public static void copyAttrToEleAttr(final Object obj, final Element ele)
    		throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
    	
    	// ��ȡf�����Ӧ���е�����������
    	Field[] fields = obj.getClass().getDeclaredFields();
    	for (int i = 0, len = fields.length; i < len; i++) {
    		// ����ÿ�����ԣ���ȡ������
    		String fieldName = fields[i].getName();
    		fields[i].setAccessible(true);
    		
    		// ���˲�����Ķ���
    		if (Modifier.isStatic(fields[i].getModifiers())) {
    			continue;
    		}
    		
    		/**
    		 * String����ֱ�ӽ��и�ֵ
    		 */
    		if (fields[i].getType().equals(String.class)) {
    			String str = (String) fields[i].get(obj);
    			if(null != str){
    				String decode = URLDecoder.decode(str,"UTF-8");
    				ele.addAttribute(fieldName, decode);
    			}
    		} 
    	}
    	
    }

}