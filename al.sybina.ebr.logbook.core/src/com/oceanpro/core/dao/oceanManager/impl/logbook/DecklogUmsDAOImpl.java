package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogUmsDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogUms;




@Component (name="decklogUmsDao") // Il nome serve solo per i test con la classe TestDao
public class DecklogUmsDAOImpl extends GenericDAOImpl<DecklogUms, Long> implements DecklogUmsDAO {

	public DecklogUmsDAOImpl() {
		super(DecklogUms.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}