package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogEcdisDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogEcdis;



@Component (name="decklogEcdisDao") // Il nome serve solo per i test con la classe TestDao
public class DecklogEcdisDAOImpl extends GenericDAOImpl<DecklogEcdis, Long> implements DecklogEcdisDAO {
	
	public DecklogEcdisDAOImpl() {
		super(DecklogEcdis.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
