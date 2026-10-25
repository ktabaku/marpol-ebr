package com.oceanpro.core.dao.oceanManager.logbook.operation;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.operationLog.GarbageCategory;
import com.widee.base.dao.base.GenericDAO;

/**
 * Class name: GarbageCategory
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
public interface GarbageCategoryDAO extends GenericDAO<GarbageCategory, Long>{
	
	void setEntityManager(EntityManager em);
}
