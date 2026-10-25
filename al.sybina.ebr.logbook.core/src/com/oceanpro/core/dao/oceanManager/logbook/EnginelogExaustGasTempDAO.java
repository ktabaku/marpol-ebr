package com.oceanpro.core.dao.oceanManager.logbook;

import com.widee.base.dao.base.GenericDAO;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.EnginelogExaustGasTemp;

public interface EnginelogExaustGasTempDAO extends GenericDAO<EnginelogExaustGasTemp, Long>{

	void setEntityManager(EntityManager em);

}
