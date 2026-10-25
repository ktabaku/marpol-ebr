package com.oceanpro.core.dao.oceanManager.logbook;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.DecklogBellBookEngTelegraph;
import com.widee.base.dao.base.GenericDAO;

public interface DecklogBellBookEngTelegraphDAO extends GenericDAO<DecklogBellBookEngTelegraph, Long>{

	void setEntityManager(EntityManager em);

}
