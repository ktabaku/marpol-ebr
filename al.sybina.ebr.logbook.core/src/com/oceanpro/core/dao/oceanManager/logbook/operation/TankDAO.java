package com.oceanpro.core.dao.oceanManager.logbook.operation;

import java.util.List;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.widee.base.dao.base.GenericDAO;

/**
 * Class name: TankDAO
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
public interface TankDAO extends GenericDAO<Tank, Long>{
	void setEntityManager(EntityManager em);

	Tank getTankByName(String tankName);

	List<Tank> getAllCacheStoreRefresh();
}
