package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogRouteMeteoDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogRouteMeteo;



@Component (name="decklogRouteMeteoDao") // Il nome serve solo per i test con la classe TestDao
public class DecklogRouteMeteoDAOImpl extends GenericDAOImpl<DecklogRouteMeteo, Long> implements DecklogRouteMeteoDAO {

	public DecklogRouteMeteoDAOImpl() {
		super(DecklogRouteMeteo.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
