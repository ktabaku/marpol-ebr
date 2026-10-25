package com.oceanpro.core.dao.oceanManager.logbook;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.PermanentChiefEngineerOrder;
import com.widee.base.dao.base.GenericDAO;

public interface PermanentChiefEngineerOrderDAO extends GenericDAO<PermanentChiefEngineerOrder, Long>{

	void setEntityManager(EntityManager em);

}
