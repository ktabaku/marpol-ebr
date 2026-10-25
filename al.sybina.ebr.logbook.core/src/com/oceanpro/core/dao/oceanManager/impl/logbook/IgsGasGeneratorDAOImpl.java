package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.IgsGasGeneratorDAO;
import com.oceanpro.core.entity.oceanManager.logbook.IgsGasGenerator;
import com.widee.base.dao.base.impl.GenericDAOImpl;


@Component (name="igsGasGeneratorDao") // Il nome serve solo per i test con la classe TestDao
public class IgsGasGeneratorDAOImpl extends GenericDAOImpl<IgsGasGenerator, Long> implements IgsGasGeneratorDAO {

	
	public IgsGasGeneratorDAOImpl() {
		super(IgsGasGenerator.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
