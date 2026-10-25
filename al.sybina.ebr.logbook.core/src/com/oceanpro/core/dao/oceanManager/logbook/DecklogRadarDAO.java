package com.oceanpro.core.dao.oceanManager.logbook;

import com.widee.base.dao.base.GenericDAO;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.DecklogRadar;


public interface DecklogRadarDAO extends GenericDAO<DecklogRadar, Long>{

	void setEntityManager(EntityManager em);

}
