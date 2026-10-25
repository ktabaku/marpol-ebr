package com.oceanpro.core.dao.oceanManager.logbook.operation.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.config.QueryHints;
import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.operation.TankDAO;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.OilLog;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.widee.base.dao.base.impl.GenericDAOImpl;

/**
 * Class name: TankDAOImpl
 *
 * Description: 
 * 
 *
 * Company: 
 *
 * @author Gio
 * @date 30/jan/2012
 *
 */
@Component (name="tankDAOImpl") // Il nome serve solo per i test con la classe TestDao
public class TankDAOImpl extends GenericDAOImpl<Tank, Long> implements TankDAO {

	public TankDAOImpl() {
		super(Tank.class);
	}
	
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
	public Tank getTankByName(String tankName) {
		String q="from Tank c where c.name=:tankName";
		TypedQuery<Tank> query = getEntityManager().createQuery(q, Tank.class);
		query.setParameter("tankName", tankName);
		query.setMaxResults(1);
		query.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
		List<Tank> result = query.getResultList();
		if(result.size()>0) return result.get(0);
		else return null;
	}
	
	@Override
	public Tank update(Tank obj) {
		return (Tank) getEntityManager().merge(obj);
	}

	@Override
	public List<Tank> getAllCacheStoreRefresh() {
		Query query = getEntityManager().createQuery("from " + this.persistentClass.getName() + " pc ORDER BY pc.id ASC").setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
		return query.getResultList();
	}
}


