package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.DecklogFirepatrolDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogFirepatrol;
import com.widee.base.dao.base.impl.GenericDAOImpl;

@Component (name="decklogFirepatrolDAO") // Il nome serve solo per i test con la classe TestDao
public class DecklogFirepatrolDAOImpl extends GenericDAOImpl<DecklogFirepatrol, Long>implements DecklogFirepatrolDAO {

	public DecklogFirepatrolDAOImpl() {
		super(DecklogFirepatrol.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
