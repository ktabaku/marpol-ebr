package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogPositionDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogShipPosition;

@Component (name="decklogPositionDAO") // Il nome serve solo per i test con la classe TestDao
public class DecklogPositionDAOImpl extends GenericDAOImpl<DecklogShipPosition, Long> implements DecklogPositionDAO{

	public DecklogPositionDAOImpl() {
		super(DecklogShipPosition.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
