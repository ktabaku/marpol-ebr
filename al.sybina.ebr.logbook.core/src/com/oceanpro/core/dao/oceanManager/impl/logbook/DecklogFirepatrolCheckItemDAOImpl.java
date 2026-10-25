package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.DecklogFirepatrolCheckItemDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogFirepatrolCheckItem;
import com.widee.base.dao.base.impl.GenericDAOImpl;

@Component (name="decklogFirepatrolCheckItemDAO") // Il nome serve solo per i test con la classe TestDao
public class DecklogFirepatrolCheckItemDAOImpl extends GenericDAOImpl<DecklogFirepatrolCheckItem, Long>implements DecklogFirepatrolCheckItemDAO {

	public DecklogFirepatrolCheckItemDAOImpl() {
		super(DecklogFirepatrolCheckItem.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
