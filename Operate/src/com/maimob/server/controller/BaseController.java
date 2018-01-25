package com.maimob.server.controller;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {

    protected static Logger logger = LoggerFactory.getLogger(IndexController.class);
	public String checkParameter(HttpServletRequest request)
	{
		String value = "";
		try {
			request.setCharacterEncoding("UTF-8");
			String uri = request.getRequestURI();
			

			 StringBuffer sb = new StringBuffer();
			  String line = null;
			  try {
			    BufferedReader reader = request.getReader();
			    while ((line = reader.readLine()) != null)
			    	sb.append(line);
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
			
			  value = sb.toString();
			
			
			
//			 while(ps.hasMoreElements()){
//	            String value = (String)ps.nextElement();//调用nextElement方法获得元素
//	            rp.rps.put(value, request.getParameter(value).trim());
//	            sb.append(value).append(" {").append(request.getParameter(value)).append("}");
//	            
////	            logger.debug("register mobileNo = {} password = {} yzm = {} inviteCode={}",mobileNo,password,yzm,inviteCode);
//		        
//	        }
			 logger.debug(uri+"--"+value);
		} 
		catch (UnsupportedEncodingException e) {
            logger.error("h5Register UnsupportedEncodingException e = {}",e);
        }
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return value;
		
	}
	

    public static String getId(){
        return System.currentTimeMillis()+"";
    }
	

}
