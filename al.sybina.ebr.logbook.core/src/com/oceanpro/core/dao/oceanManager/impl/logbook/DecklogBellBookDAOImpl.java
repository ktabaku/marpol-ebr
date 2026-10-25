package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.DecklogBellBookDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogBellBook;
import com.widee.base.dao.base.impl.GenericDAOImpl;

@Component (name="decklogBellBookDAO") // Il nome serve solo per i test con la classe TestDao
public class DecklogBellBookDAOImpl extends GenericDAOImpl<DecklogBellBook, Long> implements DecklogBellBookDAO{

	public DecklogBellBookDAOImpl() {
		super(DecklogBellBook.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
