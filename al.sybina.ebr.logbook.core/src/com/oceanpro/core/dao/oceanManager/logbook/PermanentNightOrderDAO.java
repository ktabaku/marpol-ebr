package com.oceanpro.core.dao.oceanManager.logbook;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.PermanentNightOrder;
import com.widee.base.dao.base.GenericDAO;

public interface PermanentNightOrderDAO extends GenericDAO<PermanentNightOrder, Long>{

	void setEntityManager(EntityManager em);

}
