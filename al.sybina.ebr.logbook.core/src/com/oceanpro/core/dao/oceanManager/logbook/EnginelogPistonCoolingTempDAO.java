package com.oceanpro.core.dao.oceanManager.logbook;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.EnginelogPistonCoolingTemp;
import com.widee.base.dao.base.GenericDAO;

public interface EnginelogPistonCoolingTempDAO extends GenericDAO<EnginelogPistonCoolingTemp, Long>{

	void setEntityManager(EntityManager em);

}
