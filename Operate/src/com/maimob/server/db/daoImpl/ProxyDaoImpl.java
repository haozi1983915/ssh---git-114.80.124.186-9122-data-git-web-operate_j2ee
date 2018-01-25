package com.maimob.server.db.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Proxy;

//注入
@Repository
public class ProxyDaoImpl extends BaseDaoHibernate5<Proxy>{
    
    @SuppressWarnings("unchecked") 
    public List<Proxy> findAllById(long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Proxy en where en.id = ?0")
                .setParameter(0, id)
                .getResultList();
    }
    

//    @SuppressWarnings("unchecked") 
//    public List<Proxy> findAll() {
//        return findAll("");
//    }
    

//    @SuppressWarnings("unchecked") 
//    public List<Proxy> findAll(String where) {
//    	
//    	String hql = "select en from Proxy en";
//    	if(!StringUtils.isStrEmpty(where))
//    		hql = hql+" where "+where;
//        return sessionFactory.getCurrentSession()
//                .createQuery(hql)
//                .getResultList();
//    }
    

    @SuppressWarnings("unchecked") 
    public long findCouByParameter(long[] ids,String where) {
    	
    	String hql = "select count(1) from Proxy en ";
    	hql += where;
    	
    	if(ids.length > 0)
    		hql += " and en.id in (:ids) ";
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	if(ids.length > 0)
    		q.setParameter("ids", ids);
    	
        return (long)q.uniqueResult();
    }

    @SuppressWarnings("unchecked") 
    public List<Admin> findAllByParameter(String where,int start,int maxCount) {
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Proxy en "+where+"  ")
                .setMaxResults(maxCount)
                .setFirstResult(start)
                .getResultList();
    }
    

    @SuppressWarnings("unchecked") 
    public int Update(long ProxyId, long permissionId,long cou,String channelNo) {
//        return sessionFactory.getCurrentSession()
//                .createQuery("update en from Proxy en "+where+"  ")
//                .setParameter(0, AdminId)
//                .getResultList();
    	Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        int n = 0;
        try {
        	tx = session.beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("update Proxy s set s.permissionId="+permissionId+",s.channelCou="+cou+",s.channelNo='"+channelNo+"' where s.proxyId="+ProxyId);
            n = query.executeUpdate();
            
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        
        return n;
    }
    
    
    

    @SuppressWarnings("unchecked") 
    public List<Proxy> findAllByAdminId(long AdminId) {
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Proxy en where en.adminid = ?0")
                .setParameter(0, AdminId)
                .getResultList();
    }

    @SuppressWarnings("deprecation")
    public List<Proxy> findByIds(long[] ids,String where,int start,int maxCount){

    	String hql = "select en from Proxy en ";
    	hql += where;
    	
    	if(ids.length > 0)
    		hql += " and en.id in (:ids) ";
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	if(ids.length > 0)
    		q.setParameter("ids", ids);
    	
        q.setMaxResults(maxCount)
        .setFirstResult(start);
        return q.getResultList();
    }
    
    

    @SuppressWarnings("deprecation")
    public List<Proxy> findNameByIds(long[] ids,String where){

    	String hql = "select new Proxy(en.id,en.company) from Proxy en ";
    	hql += where;
    	
    	if(ids.length > 0)
    		hql += " and en.id in (:ids) ";
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	if(ids.length > 0)
    		q.setParameter("ids", ids);
    	
        return q.getResultList();
    }
      
      
    
}
