package com.oceanpro.core.dao.oceanManager.logbook;

import com.widee.base.dao.base.GenericDAO;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.CounterEngine;


public interface CounterEngineDAO extends GenericDAO<CounterEngine, Long>{

	void setEntityManager(EntityManager em);

}
