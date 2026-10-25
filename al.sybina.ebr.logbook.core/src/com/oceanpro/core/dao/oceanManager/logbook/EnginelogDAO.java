package com.oceanpro.core.dao.oceanManager.logbook;

import com.widee.base.dao.base.GenericDAO;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.Enginelog;



public interface EnginelogDAO extends GenericDAO<Enginelog, Long>{

	void setEntityManager(EntityManager em);
}
