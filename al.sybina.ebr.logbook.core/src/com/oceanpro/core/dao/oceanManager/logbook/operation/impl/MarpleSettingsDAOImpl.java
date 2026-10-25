package com.oceanpro.core.dao.oceanManager.logbook.operation.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;
import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.operation.MarpleSettingsDAO;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.MarpleLogSettings;
import com.oceanpro.core.security.entities.User;
import com.widee.base.dao.base.impl.GenericDAOImpl;

/**
 * Class name: MarpleSettingsDAOImpl
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
@Component (name="MarpleSettingsDAO") // Il nome serve solo per i test con la classe TestDao
public class MarpleSettingsDAOImpl extends GenericDAOImpl<MarpleLogSettings, Long> implements MarpleSettingsDAO {

	public MarpleSettingsDAOImpl() {
		super(MarpleLogSettings.class);
	}
	
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public List<MarpleLogSettings> getAllCacheStoreRefresh() {
		Query query = getEntityManager().createQuery("from " + this.persistentClass.getName() + " pc ORDER BY pc.id ASC").setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
		return query.getResultList();
	}
}


