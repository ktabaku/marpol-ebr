package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogRadarDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogRadar;

@Component (name="decklogRadarDao") // Il nome serve solo per i test con la classe TestDao
public class DecklogRadarDAOImpl extends GenericDAOImpl<DecklogRadar, Long> implements DecklogRadarDAO {

	
	public DecklogRadarDAOImpl() {
		super(DecklogRadar.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
}
