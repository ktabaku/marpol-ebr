package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.AlcoholTestDAO;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.AlcoholTest;
import com.widee.base.dao.base.impl.GenericDAOImpl;


@Component (name="alcoholTestDao") // Il nome serve solo per i test con la classe TestDao
public class AlcoholTestDAOImpl extends GenericDAOImpl<AlcoholTest, Long> implements AlcoholTestDAO {

	
	public AlcoholTestDAOImpl() {
		super(AlcoholTest.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
