package com.maimob.server.db.daoImpl;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.ChannelPermission;
 

//注入
@Repository
public class ChannelPermissionDaoImpl extends BaseDaoHibernate5<ChannelPermission>{
    
    @SuppressWarnings("unchecked") 
    public ChannelPermission findById(long id) {
        return (ChannelPermission)sessionFactory.getCurrentSession()
                .createQuery("select en from operate_channel_permission en where en.id = ?0")
                .setParameter(0, id).getSingleResult();
    }
    
    
    
    
}
