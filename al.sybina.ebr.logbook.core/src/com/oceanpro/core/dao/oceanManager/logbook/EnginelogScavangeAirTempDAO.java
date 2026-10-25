package com.oceanpro.core.dao.oceanManager.logbook;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.EnginelogScavangeAirTemp;
import com.widee.base.dao.base.GenericDAO;

public interface EnginelogScavangeAirTempDAO extends GenericDAO<EnginelogScavangeAirTemp, Long>{

	void setEntityManager(EntityManager em);

}
