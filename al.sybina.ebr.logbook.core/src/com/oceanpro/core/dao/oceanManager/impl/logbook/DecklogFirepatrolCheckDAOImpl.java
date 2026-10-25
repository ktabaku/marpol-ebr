package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.DecklogFirepatrolCheckDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogFirepatrolCheck;
import com.widee.base.dao.base.impl.GenericDAOImpl;

@Component (name="decklogFirepatrolCheckDAO") // Il nome serve solo per i test con la classe TestDao
public class DecklogFirepatrolCheckDAOImpl extends GenericDAOImpl<DecklogFirepatrolCheck, Long>implements DecklogFirepatrolCheckDAO {

	public DecklogFirepatrolCheckDAOImpl() {
		super(DecklogFirepatrolCheck.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
