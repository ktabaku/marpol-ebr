package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.DecklogCompassDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogCompass;
import com.widee.base.dao.base.impl.GenericDAOImpl;

@Component (name="decklogCompassDAO") // Il nome serve solo per i test con la classe TestDao
public class DecklogCompassDAOImpl extends GenericDAOImpl<DecklogCompass, Long> implements DecklogCompassDAO{

	public DecklogCompassDAOImpl() {
		super(DecklogCompass.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
