package com.oceanpro.core.dao.oceanManager.logbook;

import com.widee.base.dao.base.GenericDAO;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.Bunker;


public interface BunkerDAO extends GenericDAO<Bunker, Long>{

	void setEntityManager(EntityManager em);

}
