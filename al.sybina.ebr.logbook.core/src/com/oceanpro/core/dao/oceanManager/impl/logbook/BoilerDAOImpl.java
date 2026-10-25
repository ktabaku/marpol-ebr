package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.BoilerDAO;
import com.oceanpro.core.entity.oceanManager.logbook.Boiler;
import com.widee.base.dao.base.impl.GenericDAOImpl;


@Component (name="boilerDao") // Il nome serve solo per i test con la classe TestDao
public class BoilerDAOImpl extends GenericDAOImpl<Boiler, Long> implements BoilerDAO {

	
	public BoilerDAOImpl() {
		super(Boiler.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
