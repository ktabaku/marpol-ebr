package com.oceanpro.core.dao.oceanManager.logbook;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.DecklogBellBook;
import com.widee.base.dao.base.GenericDAO;

public interface DecklogBellBookDAO extends GenericDAO<DecklogBellBook, Long>{

	void setEntityManager(EntityManager em);

}
