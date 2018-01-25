package com.maimob.server.db.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.utils.StringUtils;
 

//注入
@Repository
public class ChannelDaoImpl extends BaseDaoHibernate5<Channel>{
    
    @SuppressWarnings("unchecked") 
    public List<Channel> findAllByName(String Name) {
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Channel en where en.name = ?0")
                .setParameter(0, Name)
                .getResultList();
    }
    

    @SuppressWarnings("unchecked") 
    public List<Channel> findAll(String where) {

    	String hql = "select en from Channel en ";
    	if(!StringUtils.isStrEmpty(where))
    		hql = hql+" where "+where;
    	
        return sessionFactory.getCurrentSession()
                .createQuery("hql")
                .getResultList();
    }

    @SuppressWarnings("deprecation")
    public long findCouByProxyId(long pid){
        return (Long)sessionFactory.getCurrentSession()
                .createQuery("select count(*) from Channel en where en.proxyId = ?0")
                .setParameter(0, pid)
                .uniqueResult();
    }
    

    @SuppressWarnings("unchecked") 
    public long findCouByParameter(long[] ids,String where) {
    	
    	String hql = "select count(1) from Channel en ";
    	hql += where;
    	
    	if(ids.length > 0)
    		hql += " and en.adminId in (:ids) ";
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	if(ids.length > 0)
    		q.setParameter("ids", ids);
    	
        return (long)q.uniqueResult();
    }


    @SuppressWarnings("deprecation")
    public List<Channel> findByAdminids(long[] ids,String where,int start,int maxCount){

    	String hql = "select en from Channel en ";
    	hql += where;
    	
    	if(ids.length > 0)
    		hql += " and en.adminId in (:ids) ";
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	if(ids.length > 0)
    		q.setParameter("ids", ids);
    	
        q.setMaxResults(maxCount)
        .setFirstResult(start);
        return q.getResultList();
    }
    
    

    @SuppressWarnings("deprecation")
    public List<Channel> findMainChannel(long proxyId){
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Channel en where en.proxyId in (:proxyId) and level = 1")
                .setParameter("proxyId", proxyId)
                .getResultList();
    }
      
    
    

    @SuppressWarnings("deprecation")
    public List<Channel> findProxyidByAdminIds(long[] adminids){
        return sessionFactory.getCurrentSession()
                .createQuery("select distinct en.proxyId from Channel en where en.adminId in (:adminids)")
                .setParameter("adminids", adminids)
                .getResultList();
    }
      
    

    
    
}
