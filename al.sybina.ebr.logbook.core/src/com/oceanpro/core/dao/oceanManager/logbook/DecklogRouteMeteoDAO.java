package com.oceanpro.core.dao.oceanManager.logbook;

import com.widee.base.dao.base.GenericDAO;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.DecklogRouteMeteo;

public interface DecklogRouteMeteoDAO extends GenericDAO<DecklogRouteMeteo, Long>{

	void setEntityManager(EntityManager em);

}
