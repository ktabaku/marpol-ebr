package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.CounterEngineDAO;
import com.oceanpro.core.entity.oceanManager.logbook.CounterEngine;
import com.widee.base.dao.base.impl.GenericDAOImpl;


@Component (name="igsCounterEngineDao") // Il nome serve solo per i test con la classe TestDao
public class CounterEngineDAOImpl extends GenericDAOImpl<CounterEngine, Long> implements CounterEngineDAO {

	
	public CounterEngineDAOImpl() {
		super(CounterEngine.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
