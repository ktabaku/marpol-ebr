package com.oceanpro.core.dao.oceanManager.logbook.operation;

import java.util.List;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.operationLog.MarpleLogSettings;
import com.widee.base.dao.base.GenericDAO;

/**
 * Class name: MarpleSettingsDAO
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
public interface MarpleSettingsDAO extends GenericDAO<MarpleLogSettings, Long>{

	void setEntityManager(EntityManager em);

	List<MarpleLogSettings> getAllCacheStoreRefresh();
}
