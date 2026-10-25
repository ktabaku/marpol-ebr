package com.oceanpro.core.dao.oceanManager.logbook;

import com.widee.base.dao.base.GenericDAO;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.DecklogShipPosition;

public interface DecklogPositionDAO extends GenericDAO<DecklogShipPosition, Long>{

	void setEntityManager(EntityManager em);

}
