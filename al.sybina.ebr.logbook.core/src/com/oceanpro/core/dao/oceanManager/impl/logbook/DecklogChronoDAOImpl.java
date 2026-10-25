package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogChronoDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogChrono;

@Component (name="decklogChronoDAO") // Il nome serve solo per i test con la classe TestDao
public class DecklogChronoDAOImpl extends GenericDAOImpl<DecklogChrono, Long> implements DecklogChronoDAO {

	public DecklogChronoDAOImpl() {
		super(DecklogChrono.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
