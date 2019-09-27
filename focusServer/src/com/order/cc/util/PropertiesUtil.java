package com.order.cc.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
	    private static Properties props;
	    static{
	    	loadProps();
	    }
	    synchronized static private void loadProps(){
	        props = new Properties();
	        InputStream in = null;
	        try {
	        	in = PropertiesUtil.class.getClassLoader().getResourceAsStream("parameter.properties");
	            props.load(new InputStreamReader(in,"UTF-8"));
	        } catch (FileNotFoundException e) {
	        } catch (IOException e) {
	        } finally {
	            try {
	                if(null != in) {
	                    in.close();
	                }
	            } catch (IOException e) {
	            }
	        }
	    }
	    
	    public static String getProperty(String key){
	    	if(props==null){
	    		loadProps();
	    	}
	    	return props.getProperty(key);
	    }
	    
	    public static String getProperty(String key,String defaultValue){
	    	if(props==null){
	    		loadProps();
	    	}
	    	return props.getProperty(key, defaultValue);
	    }
	}
