package com.oceanpro.core.dao.oceanManager.logbook;

import com.widee.base.dao.base.GenericDAO;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.Vhf;


public interface VhfDAO extends GenericDAO<Vhf, Long>{

	void setEntityManager(EntityManager em);

}
