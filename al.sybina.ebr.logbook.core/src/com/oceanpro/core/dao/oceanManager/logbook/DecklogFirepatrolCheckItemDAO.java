package com.oceanpro.core.dao.oceanManager.logbook;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.DecklogFirepatrolCheckItem;
import com.widee.base.dao.base.GenericDAO;

public interface DecklogFirepatrolCheckItemDAO extends GenericDAO<DecklogFirepatrolCheckItem, Long> {

	void setEntityManager(EntityManager em);

}
