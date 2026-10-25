package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.DecklogBellBookEngTelegraphDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogBellBookEngTelegraph;
import com.widee.base.dao.base.impl.GenericDAOImpl;

@Component (name="decklogBellBookEngTelegraphDAO") // Il nome serve solo per i test con la classe TestDao
public class DecklogBellBookEngTelegraphDAOImpl extends GenericDAOImpl<DecklogBellBookEngTelegraph, Long> implements DecklogBellBookEngTelegraphDAO{

	public DecklogBellBookEngTelegraphDAOImpl() {
		super(DecklogBellBookEngTelegraph.class); 
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
