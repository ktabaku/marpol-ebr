package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.DrugTestDAO;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.DrugTest;
import com.widee.base.dao.base.impl.GenericDAOImpl;


@Component (name="drugTestDao") // Il nome serve solo per i test con la classe TestDao
public class DrugTestDAOImpl extends GenericDAOImpl<DrugTest, Long> implements DrugTestDAO {

	
	public DrugTestDAOImpl() {
		super(DrugTest.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
