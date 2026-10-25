package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogDAO;
import com.oceanpro.core.entity.oceanManager.logbook.Decklog;



@Component (name="decklogDao") // Il nome serve solo per i test con la classe TestDao
public class DecklogDAOImpl extends GenericDAOImpl<Decklog, Long> implements DecklogDAO {

	public DecklogDAOImpl() {
		super(Decklog.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}