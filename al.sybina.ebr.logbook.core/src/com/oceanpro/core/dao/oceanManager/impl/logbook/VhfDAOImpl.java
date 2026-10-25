package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.VhfDAO;
import com.oceanpro.core.entity.oceanManager.logbook.Vhf;


@Component (name="vhfDao") // Il nome serve solo per i test con la classe TestDao
public class VhfDAOImpl extends GenericDAOImpl<Vhf, Long> implements VhfDAO {

	public VhfDAOImpl() {
		super(Vhf.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
