package com.oceanpro.core.dao.oceanManager.logbook;

import javax.persistence.EntityManager;

import com.oceanpro.core.entity.oceanManager.logbook.DecklogCompass;
import com.widee.base.dao.base.GenericDAO;

public interface DecklogCompassDAO extends GenericDAO<DecklogCompass, Long>{

	void setEntityManager(EntityManager em);

}
