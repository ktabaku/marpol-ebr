package com.oceanpro.core.dao.oceanManager.logbook;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.EnginelogJackCoolingWater;
import com.widee.base.dao.base.GenericDAO;

public interface EnginelogJackCoolingWaterDAO extends GenericDAO<EnginelogJackCoolingWater, Long>{

	void setEntityManager(EntityManager em);

}
