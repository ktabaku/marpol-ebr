package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.BunkerDAO;
import com.oceanpro.core.entity.oceanManager.logbook.Bunker;
import com.widee.base.dao.base.impl.GenericDAOImpl;


@Component (name="bunkerDao") // Il nome serve solo per i test con la classe TestDao
public class BunkerDAOImpl extends GenericDAOImpl<Bunker, Long> implements BunkerDAO {

	
	public BunkerDAOImpl() {
		super(Bunker.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
