package com.oceanpro.core.dao.oceanManager.logbook;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.DecklogFirepatrolCheck;
import com.widee.base.dao.base.GenericDAO;

public interface DecklogFirepatrolCheckDAO extends GenericDAO<DecklogFirepatrolCheck, Long> {

	void setEntityManager(EntityManager em);

}
