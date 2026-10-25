package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.DieselGeneratorDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DieselGenerator;
import com.widee.base.dao.base.impl.GenericDAOImpl;


@Component (name="dieselGeneratorDao") // Il nome serve solo per i test con la classe TestDao
public class DieselGeneratorDAOImpl extends GenericDAOImpl<DieselGenerator, Long> implements DieselGeneratorDAO {

	
	public DieselGeneratorDAOImpl() {
		super(DieselGenerator.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
