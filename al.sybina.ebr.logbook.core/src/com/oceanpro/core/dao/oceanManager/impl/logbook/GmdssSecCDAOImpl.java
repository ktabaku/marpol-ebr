package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.GmdssSecCDAO;
import com.oceanpro.core.entity.oceanManager.logbook.GmdssSecC;


@Component (name="gmdssSecCDao") // Il nome serve solo per i test con la classe TestDao
public class GmdssSecCDAOImpl extends GenericDAOImpl<GmdssSecC, Long> implements GmdssSecCDAO {

	public GmdssSecCDAOImpl() {
		super(GmdssSecC.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
