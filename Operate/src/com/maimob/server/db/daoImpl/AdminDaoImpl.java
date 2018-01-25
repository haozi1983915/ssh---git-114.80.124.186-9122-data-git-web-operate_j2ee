package com.maimob.server.db.daoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.utils.StringUtils;
 

//注入
@Repository
public class AdminDaoImpl extends BaseDaoHibernate5<Admin>{
    
    @SuppressWarnings("unchecked") 
    public List<Admin> findAllByName(String Name) {
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Admin en where en.name = ?0")
                .setParameter(0, Name)
                .getResultList();
    }

    @SuppressWarnings("unchecked") 
    public List<Admin> findAllByEmail(String Email) {
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Admin en where en.email = ?0")
                .setParameter(0, Email)
                .getResultList();
    }
    

    @SuppressWarnings("unchecked") 
    public List<Admin> findAllByLevel_departmentId(long level,long departmentId) {
        return sessionFactory.getCurrentSession()
                .createQuery("select new Admin(en.id,en.name,en.level)  from Admin en where en.level <= 2 and en.departmentId = ?0 order by level asc ")
//                .setParameter(0, level)
                .setParameter(0, departmentId)
                .getResultList();
    }
    
    
    
    @SuppressWarnings("unchecked") 
    public List<Admin> findAllByHigherid(String higherid) {
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Admin en where en.higherId = ?0 order by level asc ")
                .setParameter(0, higherid)
                .getResultList();
    }

    @SuppressWarnings("unchecked") 
    public List<Admin> findAllByDepartmentId(long departmentId) {
    	
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Admin en where en.departmentId = ?0  order by level asc ")
                .setParameter(0, departmentId)
                .getResultList();
    }
    

    @SuppressWarnings("unchecked") 
    public List<Admin> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("select new Admin(id,name) from Admin en")
                .getResultList();
    }
    


    @SuppressWarnings("unchecked") 
    public long findCouByParameter(String where) {
    	
        return (long)sessionFactory.getCurrentSession()
                .createQuery("select count(1) from Admin en  "+where+"  ").uniqueResult();
    }

    @SuppressWarnings("unchecked") 
    public List<Admin> findAllByParameter(String where,int start,int maxCount) {
    	
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Admin en "+where+"  ")
                .setMaxResults(maxCount)
                .setFirstResult(start)
                .getResultList();
        
    }
    
    
}















