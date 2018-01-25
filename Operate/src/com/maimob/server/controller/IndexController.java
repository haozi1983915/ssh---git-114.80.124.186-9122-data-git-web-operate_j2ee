package com.maimob.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.ChannelPermission;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.db.entity.Reward;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.db.service.SMSRecordService;
import com.maimob.server.protocol.BaseResponse;
import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.PWDUtils;
import com.maimob.server.utils.StringUtils;

@Controller
@RequestMapping("/Index")
public class IndexController extends BaseController {

    //注入service
    @Autowired
    private SMSRecordService smsRecordService;
	
    @Autowired
    private DaoService dao;
    
    
    
    
    @RequestMapping(value = "/index", method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String index(HttpServletRequest request,HttpServletResponse response){
        
        return "thinks for visit";
        
    }
    
    public static void main(String[] args) {
		long id = System.currentTimeMillis();
		String pp = PWDUtils.encryptMD5AndBase64("123456");
		System.out.println(id+"  "+pp);
		
		
	}

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/login", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String login(HttpServletRequest request,HttpServletResponse response){

        logger.debug("login");

        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        JSONObject jobj = JSONObject.parseObject(json);
        
        String email = jobj.getString("email");
        String pwd = jobj.getString("pwd");
        String md5Pwd = PWDUtils.encryptMD5AndBase64(pwd);
        
        
        int status = 1;
        String statusMsg ="";
        List<Admin> as = dao.findAdminByEmail(email);
        
        
        if(as == null || as.size() == 0){
            status = 1;
            statusMsg = "用户名或密码错误";
        }else{
        	Admin admin = as.get(0);
        	String md5Pwd2 = PWDUtils.encryptMD5AndBase64(as.get(0).getPwd());
        	
        	if(md5Pwd2.equals(md5Pwd))
        	{
//        		admin.setPwd(null);
                status = 0;
                admin.setLoginDate(System.currentTimeMillis());
                setAdmin(admin.getId()+"", admin);
                baseResponse.setId(admin.getId());
                baseResponse.setAdmin(admin);
                baseResponse.setSessionid(admin.getId());
        	}
        	else
        	{
                status = 1;
                statusMsg = "用户名或密码错误";
        	}
        }
    	
        baseResponse.setStatus(status);
        baseResponse.setStatusMsg(statusMsg);
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    
    
    
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/addAdmin", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String addAdmin(HttpServletRequest request,HttpServletResponse response){
        logger.debug("addAdmin");

        BaseResponse baseResponse = new BaseResponse();


        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        try {
        	json = URLDecoder.decode(json, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        

        JSONObject jobj = JSONObject.parseObject(json);
        String adminid = jobj.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        
        Admin otheradmin = JSONObject.parseObject(json, Admin.class);
        
        String statusMsg ="";
        int status = 1;
        String check = otheradmin.check();
        if(check.equals(""))
        {
        	long id = otheradmin.getId();
        	otheradmin.setDate(System.currentTimeMillis());
        	dao.saveAdmin(otheradmin);
        	baseResponse.setId(id);
            statusMsg = "添加账号成功";
            status = 0;
        }
        else
        {
            statusMsg = check;
        }
        
        baseResponse.setStatus(status);
        baseResponse.setStatusMsg(statusMsg);
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getAdmin", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getAdmin(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getAdmin");

        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        try {
        	json = URLDecoder.decode(json, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        

        if(!json.equals(""))
        {
        	
            JSONObject jobj = JSONObject.parseObject(json);
//            String adminid = jobj.getString("sessionid");
//
//            Admin admin = this.getAdmin(adminid);
//            if(admin == null)
//            {
//                baseResponse.setStatus(1);
//                baseResponse.setStatusMsg("请重新登录");
//                return JSONObject.toJSONString(baseResponse);
//            }
            
        	
            try {
            	json = URLDecoder.decode(json, "utf-8");
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}

            int first = Integer.parseInt(jobj.getString("first"));
            if(first==0)
            {
            	long listSize = dao.findAdminCou(json);
            	baseResponse.setListSize(listSize+"");
            }
            
            List<Admin> admins = dao.findAdmin(json);
            baseResponse.setAdminList(admins);
        }

        
        

        baseResponse.setStatus(0);
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    
    


    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/addProxy", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String addProxy(HttpServletRequest request,HttpServletResponse response){
        logger.debug("addProxy");
        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }
 
        try {
        	json = URLDecoder.decode(json, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        

        JSONObject jobj = JSONObject.parseObject(json);
        String adminid = jobj.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        
        
        Proxy proxy = JSONObject.parseObject(json, Proxy.class);

        String statusMsg ="";
        int status = 1;
        String check = proxy.check();
        if(check.equals(""))
        {
        	long id = proxy.getId();
        	dao.saveProxy(proxy);
        	baseResponse.setId(id);
            statusMsg = "添加渠道商成功";
            status = 0;
        }
        else
        {
            statusMsg = check;
        }
        
        baseResponse.setStatus(status);
        baseResponse.setStatusMsg(statusMsg);
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/addChannel", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String addChannel(HttpServletRequest request,HttpServletResponse response){
        logger.debug("addChannel");
        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        JSONObject jobj = JSONObject.parseObject(json);
        String adminid = jobj.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        

        Channel channel = JSONObject.parseObject(json, Channel.class);

        String statusMsg ="";
        int status = 1;

        String check = channel.check();
        if(check.equals(""))
        {
        	long id = channel.getId();
        	dao.saveChannel(channel);
        	baseResponse.setId(id);
            statusMsg = "添加渠道商成功";
            status = 0;
        }
        else
        {
            statusMsg = check;
        }
        
        baseResponse.setStatus(status);
        baseResponse.setStatusMsg(statusMsg);
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getChannel", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getChannel(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getChannel");
        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        JSONObject jobj = JSONObject.parseObject(json);
        String adminid = jobj.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        

        StringBuffer where = new StringBuffer();
        where.append(" 1 = 1 ");
        String otheradminId = "";
        if(!json.equals(""))
        {
        	
            try {
            	json = URLDecoder.decode(json, "utf-8");
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}

            JSONObject whereJson = JSONObject.parseObject(json);

            otheradminId = whereJson.getString("adminId");
            if(!StringUtils.isStrEmpty(otheradminId))
            {
            	where.append(" and adminId = "+otheradminId+" ");
            }
            
        }

        int level = admin.getLevel(); 
        List<Channel> channels = null;
        long []ids = new long[0];
        if(!StringUtils.isStrEmpty(otheradminId))
        {
        	ids = new long[1];
        	ids[0] = Long.parseLong(otheradminId);
        }
        else
        {
            if(level > 1)
            {
                
                if(level == 2)
                {
                	List<Admin> ads = dao.findAdminByHigherid(admin.getId()+"");
                	ids = new long[ads.size()+1];
                	for(int i = 0;i < ads.size();i++)
                	{
                		ids[i] = ads.get(i).getId();
                	}
                	ids[ids.length-1] = admin.getId();
                }
                else if(level == 3)
                {
                	ids = new long[1];
                	ids[ids.length-1] = admin.getId();
                }
            	
            	
            }
        }

        int first = 1;
        
        try {
            first = Integer.parseInt(jobj.getString("first"));
            baseResponse.setListSize(0+"");
            if(first==0)
            {
            	
                long listSize = dao.findChannelCouByAdminids(ids,jobj);
                baseResponse.setListSize(listSize+"");
            }
		} catch (Exception e) {
			// TODO: handle exception
		}

    	channels = dao.findChannelByAdminids(ids,jobj);
        
        
        baseResponse.setChannelList(channels);
        baseResponse.setStatus(0);
        baseResponse.setStatusMsg("");
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getChannelParameter", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getChannelParameter(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getChannelParameter");
        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        JSONObject jobj = JSONObject.parseObject(json);
        String adminid = jobj.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        
        StringBuffer where = new StringBuffer();
        where.append(" 1 = 1 ");
        String otheradminId = "";
        if(!json.equals(""))
        {
        	
            try {
            	json = URLDecoder.decode(json, "utf-8");
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}

            JSONObject whereJson = JSONObject.parseObject(json);

            otheradminId = whereJson.getString("adminId");
            if(!StringUtils.isStrEmpty(otheradminId))
            {
            	where.append(" and adminId = "+otheradminId+" ");
            }
            
        }

        int level = admin.getLevel(); 
        List<Channel> channels = null;
        long []ids = new long[0];
        if(!StringUtils.isStrEmpty(otheradminId))
        {
        	ids = new long[1];
        	ids[0] = Long.parseLong(otheradminId);
        }
        else
        {
            if(level > 1)
            {
                
                if(level == 2)
                {
                	List<Admin> ads = dao.findAdminByHigherid(admin.getId()+"");
                	ids = new long[ads.size()+1];
                	for(int i = 0;i < ads.size();i++)
                	{
                		ids[i] = ads.get(i).getId();
                	}
                	ids[ids.length-1] = admin.getId();
                }
                else if(level == 3)
                {
                	ids = new long[1];
                	ids[ids.length-1] = admin.getId();
                }
            	
            	
            }
        }


    	channels = dao.findChannelByAdminids(ids,jobj);
    	
    	ArrayList<String> channelNameList = new ArrayList<String>();
    	ArrayList<String> channelNoList = new ArrayList<String>();
    	ArrayList<String> adminIdList = new ArrayList<String>();
    	
    	for(int i = 0;i < channels.size();i++)
    	{
    		Channel channel = channels.get(i);
    		channelNameList.add(channel.getChannelName());
    		channelNoList.add(channel.getChannelNo());
        	Admin admin1 = Cache.getAdminCatche(channel.getAdminId());
    		adminIdList.add(admin1.getId()+","+admin1.getName());
    	}
    	
    	AppTools.removeDuplicate(channelNameList);
    	AppTools.removeDuplicate(channelNoList);
    	AppTools.removeDuplicate(adminIdList);
    	
        baseResponse.setChannelNameList(channelNameList);
        baseResponse.setChannelNoList(channelNoList);
        baseResponse.setAdminIdList(adminIdList);
        baseResponse.setStatus(0);
        baseResponse.setStatusMsg("");
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getChannelValue", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getChannelValue(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getChannelValue");
        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        JSONObject jobj = JSONObject.parseObject(json);
        String adminid = jobj.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        
        StringBuffer where = new StringBuffer();
        where.append(" 1 = 1 ");
        String otheradminId = "";
        if(!json.equals(""))
        {
        	
            try {
            	json = URLDecoder.decode(json, "utf-8");
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}

            JSONObject whereJson = JSONObject.parseObject(json);

            otheradminId = whereJson.getString("adminId");
            if(!StringUtils.isStrEmpty(otheradminId))
            {
            	where.append(" and adminId = "+otheradminId+" ");
            }
            
        }

    	ArrayList<String> adminIdList = new ArrayList<String>();

        int level = admin.getLevel(); 
        List<Channel> channels = null;
//        long []ids = new long[0];

        if(level > 1)
        {
            if(level == 2)
            {
            	List<Admin> ads = dao.findAdminByHigherid(admin.getId()+"");
//            	ids = new long[ads.size()+1];
            	for(int i = 0;i < ads.size();i++)
            	{
            		Admin admin1 = ads.get(i);
//            		ids[i] = admin1.getId();
            		adminIdList.add(admin1.getId()+","+admin1.getName()+","+admin1.getLevel());
            	}
//            	ids[ids.length-1] = admin.getId();
        		adminIdList.add(admin.getId()+","+admin.getName()+","+admin.getLevel());
            }
            else if(level == 3)
            {
//            	ids = new long[1];
//            	ids[ids.length-1] = admin.getId();
        		adminIdList.add(admin.getId()+","+admin.getName()+","+admin.getLevel());
            }
        }
        else
        {
        	List<Admin> ads = dao.findAdminByDepartmentId(admin.getDepartmentId());
        	for(int i = 0;i < ads.size();i++)
        	{
        		Admin admin1 = ads.get(i);
        		adminIdList.add(admin1.getId()+","+admin1.getName()+","+admin1.getLevel());
        	}
        }
        
        
        
        Cache.DicCatche(dao);
        List<Dictionary> dic1 = Cache.getDicList(1);
        List<Dictionary> dic3 = Cache.getDicList(3);
        List<Dictionary> dic4 = Cache.getDicList(4);
        List<Dictionary> dic5 = Cache.getDicList(5);
        List<Dictionary> dic6 = Cache.getDicList(6);
        List<Dictionary> dic7 = Cache.getDicList(7);
        List<Dictionary> dic8 = Cache.getDicList(8);
        
        
        baseResponse.setChannelAttribute(dic3);
        baseResponse.setChannelType(dic4);
        baseResponse.setChannelSubdivision(dic5);
        baseResponse.setAppList(dic1);
        baseResponse.setCostingList(dic7);
        baseResponse.setSettlementCycleList(dic6);
        baseResponse.setRewardTypeList(dic8);
        
        
        baseResponse.setAdminIdList(adminIdList);
        baseResponse.setStatus(0);
        baseResponse.setStatusMsg("");
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getChannelAttribute", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getChannelAttribute(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getChannelAttribute");
        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        JSONObject jobj = JSONObject.parseObject(json);
        String adminid = jobj.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        
        Cache.DicCatche(dao);
        List<Dictionary> dic3 = Cache.getDicList(3);
        List<Dictionary> dic4 = Cache.getDicList(4);
        List<Dictionary> dic5 = Cache.getDicList(5);
        
        
        baseResponse.setChannelAttribute(dic3);
        baseResponse.setChannelType(dic4);
        baseResponse.setChannelSubdivision(dic5);
        baseResponse.setStatus(0);
        baseResponse.setStatusMsg("");
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/saveChannelAttribute", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveChannelAttribute(HttpServletRequest request,HttpServletResponse response){
        logger.debug("saveChannelAttribute");
        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        JSONObject jobj = JSONObject.parseObject(json);
        String adminid = jobj.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        
        Dictionary dic = JSONObject.parseObject(json,Dictionary.class);
        dic.getId();
        String msg = "";
        if(StringUtils.isStrEmpty(dic.getName()))
        {
        	msg = "名称不能为空";
        }

        if(dic.getType() != 4 && dic.getHigherId() == 0)
        {
        	msg = "上层id不能为空";
        }

        if(dic.getType() == 0)
        {
        	msg = "类别不能为空";
        }
        if(msg.equals(""))
        {
            dao.saveDictionary(dic);
            baseResponse.setStatus(0);
            baseResponse.setStatusMsg("保存成功");
        }
        else
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg(msg);
        }
        
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    


    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getProxy", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getProxy(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getProxy");
        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        try {
        	json = URLDecoder.decode(json, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

        JSONObject whereJson = JSONObject.parseObject(json);
        String adminid = whereJson.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        

    	long[] proxyids = new long[0];
        int level = admin.getLevel();
        List<Proxy> proxys = null;
        if(level > 1)
        {
            List<Channel> channels = null;
            long [] ids = null;
            
            if(level == 2)
            {
            	List<Admin> ads = dao.findAdminByHigherid(admin.getId()+"");
            	ids = new long[ads.size()+1];
            	for(int i = 0;i < ads.size();i++)
            	{
            		ids[i] = ads.get(i).getId();
            	}
            	ids[ids.length-1] = admin.getId();
            }
            else if(level == 3)
            {
            	ids = new long[1];
            	ids[ids.length-1] = admin.getId();
            }
        	
        	channels = dao.findProxyidByAdminids(ids);
        	
        	proxyids = new long[channels.size()];
        	for(int i = 0;i < channels.size();i++)
        	{
        		proxyids[i] = channels.get(i).getProxyId();
        	}
        	
        	
        }
        int first = 1;
        
        try {
            first = Integer.parseInt(whereJson.getString("first"));
		} catch (Exception e) {
			// TODO: handle exception
		}
        if(first==0)
        {
            long listSize = dao.findProxyCouByIds(proxyids,whereJson);
            baseResponse.setListSize(listSize+"");
        }
        
    	proxys = dao.findProxyByIds(proxyids,whereJson);
    	
        baseResponse.setProxyList(proxys);
        baseResponse.setStatus(0);
        baseResponse.setStatusMsg("");
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    


    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getProxyNameList", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getProxyNameList(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getProxyNameList");
        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        try {
        	json = URLDecoder.decode(json, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

        JSONObject whereJson = JSONObject.parseObject(json);
        String adminid = whereJson.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        

    	long[] proxyids = new long[0];
        int level = admin.getLevel();
        List<Proxy> proxys = null;
        if(level > 1)
        {
            List<Channel> channels = null;
            long [] ids = null;
            
            if(level == 2)
            {
            	List<Admin> ads = dao.findAdminByHigherid(admin.getId()+"");
            	ids = new long[ads.size()+1];
            	for(int i = 0;i < ads.size();i++)
            	{
            		ids[i] = ads.get(i).getId();
            	}
            	ids[ids.length-1] = admin.getId();
            }
            else if(level == 3)
            {
            	ids = new long[1];
            	ids[ids.length-1] = admin.getId();
            }
        	
        	channels = dao.findProxyidByAdminids(ids);
        	
        	proxyids = new long[channels.size()];
        	for(int i = 0;i < channels.size();i++)
        	{
        		proxyids[i] = channels.get(i).getProxyId();
        	}
        	
        	
        }
        
    	proxys = dao.findProxyNameByIds(proxyids, whereJson);
    	
        baseResponse.setProxyList(proxys);
        baseResponse.setStatus(0);
        baseResponse.setStatusMsg("");
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getReward", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getReward(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getReward");
        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        JSONObject jobj = JSONObject.parseObject(json);
        String adminid = jobj.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        
        
        
        List<Reward> rewardList = null;
        if(!json.equals(""))
        {
            try {
            	json = URLDecoder.decode(json, "utf-8");
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}

            JSONObject whereJson = JSONObject.parseObject(json);
            String id = whereJson.getString("rewardId");
            if(!StringUtils.isStrEmpty(id))
            {
            	rewardList = dao.findRewardById(Long.parseLong(id));
            }

        }
        
        baseResponse.setRewardList(rewardList);
        baseResponse.setStatus(0);
        baseResponse.setStatusMsg("");
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    


    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getChannelPermission", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getChannelPermission(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getChannelPermission");
        BaseResponse baseResponse = new BaseResponse();
        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }
   

        JSONObject jobj = JSONObject.parseObject(json);
        String adminid = jobj.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        
        
        ChannelPermission channelPermission = null;
        if(!json.equals(""))
        {
            try {
            	json = URLDecoder.decode(json, "utf-8");
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}

            JSONObject whereJson = JSONObject.parseObject(json);
            String id = whereJson.getString("permissionId");
            if(!StringUtils.isStrEmpty(id))
            {
            	channelPermission = dao.findChannelPermissionById(Long.parseLong(id));
            }

        }
        
        baseResponse.setChannelPermission(channelPermission);
        baseResponse.setStatus(0);
        baseResponse.setStatusMsg("");
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/AdminParameter", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String AdminParameter(HttpServletRequest request,HttpServletResponse response){
        logger.debug("AdminParameter");
        BaseResponse baseResponse = new BaseResponse();
        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }


        JSONObject jobj = JSONObject.parseObject(json);
        String adminid = jobj.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }

        List<Admin> admins = dao.findAdminByLevel_departmentId(admin.getLevel(), admin.getDepartmentId());
        baseResponse.setAdminList(admins);
        baseResponse.setStatus(0);
        baseResponse.setStatusMsg("");
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    
    private void setAdmin(String adminid,Admin admin)
    {
    	admin.setLoginDate(System.currentTimeMillis());
    	Cache.AdminCatche(dao);
    	Cache.updateAdminCatche(admin);
    }
    

    private Admin getAdmin(String adminid)
    {
    	Cache.AdminCatche(dao);
    	Admin admin = Cache.getAdminCatche(Long.parseLong(adminid));
    	if(admin != null)
    	{

    		if(System.currentTimeMillis() - admin.getLoginDate() > 7200000 )
    		{
    			admin = null;
    		}
    		else
    		{
    	    	admin.setLoginDate(System.currentTimeMillis());
    		}
    	}
    	return admin;
    }
    
    
    
    
    
    
}
