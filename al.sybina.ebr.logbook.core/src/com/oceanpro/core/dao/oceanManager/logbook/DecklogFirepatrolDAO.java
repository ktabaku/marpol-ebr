package com.oceanpro.core.dao.oceanManager.logbook;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.DecklogFirepatrol;
import com.widee.base.dao.base.GenericDAO;

public interface DecklogFirepatrolDAO extends GenericDAO<DecklogFirepatrol, Long> {

	void setEntityManager(EntityManager em);

}
