package com.maimob.server.protocol;

import java.util.ArrayList;
import java.util.List;

import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.ChannelPermission;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.db.entity.Reward;

public class BaseResponse {

    protected int status;

    protected String statusMsg;
    
    protected long sessionid;
    
    protected long id;
    
    private List<Proxy> proxyList;
    
    private List<Channel> channelList;
    
    private List<Reward> rewardList;
    

    private List<Admin> adminList;
    private ChannelPermission channelPermission = null;
    
    private Admin admin; 
    

    private String listSize; 
     

	ArrayList<String> channelNameList;
	ArrayList<String> channelNoList;
	ArrayList<String> adminIdList;
    
	List<Dictionary> channelAttribute; 
	List<Dictionary> channelType; 
	List<Dictionary> channelSubdivision; 

	List<Dictionary> appList; 

	List<Dictionary> costingList;
	List<Dictionary> settlementCycleList;
	

	List<Dictionary> RewardTypeList;
	
	public List<Dictionary> getRewardTypeList() {
		return RewardTypeList;
	}
	public void setRewardTypeList(List<Dictionary> rewardTypeList) {
		RewardTypeList = rewardTypeList;
	}
	public List<Dictionary> getCostingList() {
		return costingList;
	}
	public void setCostingList(List<Dictionary> costingList) {
		this.costingList = costingList;
	}
	public List<Dictionary> getSettlementCycleList() {
		return settlementCycleList;
	}
	public void setSettlementCycleList(List<Dictionary> settlementCycleList) {
		this.settlementCycleList = settlementCycleList;
	}
	public List<Dictionary> getAppList() {
		return appList;
	}
	public void setAppList(List<Dictionary> appList) {
		this.appList = appList;
	}
	public List<Dictionary> getChannelAttribute() {
		return channelAttribute;
	}
	public void setChannelAttribute(List<Dictionary> channelAttribute) {
		this.channelAttribute = channelAttribute;
	}
	public List<Dictionary> getChannelType() {
		return channelType;
	}
	public void setChannelType(List<Dictionary> channelType) {
		this.channelType = channelType;
	}
	public List<Dictionary> getChannelSubdivision() {
		return channelSubdivision;
	}
	public void setChannelSubdivision(List<Dictionary> channelSubdivision) {
		this.channelSubdivision = channelSubdivision;
	}
	public ArrayList<String> getChannelNameList() {
		return channelNameList;
	}
	public void setChannelNameList(ArrayList<String> channelNameList) {
		this.channelNameList = channelNameList;
	}
	public ArrayList<String> getChannelNoList() {
		return channelNoList;
	}
	public void setChannelNoList(ArrayList<String> channelNoList) {
		this.channelNoList = channelNoList;
	}
	public ArrayList<String> getAdminIdList() {
		return adminIdList;
	}
	public void setAdminIdList(ArrayList<String> adminIdList) {
		this.adminIdList = adminIdList;
	}
	public String getListSize() {
		return listSize;
	}
	public void setListSize(String listSize) {
		this.listSize = listSize;
	}
	public long getSessionid() {
		return sessionid;
	}
	public void setSessionid(long sessionid) {
		this.sessionid = sessionid;
	} 
	public List<Admin> getAdminList() {
		return adminList;
	}
	public void setAdminList(List<Admin> adminList) {
		this.adminList = adminList;
	}
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		admin.setPwd(null);
		this.admin = admin;
	}
	public ChannelPermission getChannelPermission() {
		return channelPermission;
	}
	public void setChannelPermission(ChannelPermission channelPermission) {
		this.channelPermission = channelPermission;
	}
	public List<Reward> getRewardList() {
		return rewardList;
	}
	public void setRewardList(List<Reward> rewardList) {
		this.rewardList = rewardList;
	}
	public List<Proxy> getProxyList() {
		return proxyList;
	}
	public void setProxyList(List<Proxy> proxyList) {
		this.proxyList = proxyList;
	}
	public List<Channel> getChannelList() {
		return channelList;
	}
	public void setChannelList(List<Channel> channelList) {
		this.channelList = channelList;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getStatusMsg() {
        return statusMsg;
    }
    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    
    public BaseResponse(){}
    
}
