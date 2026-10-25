package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.GmdssSecBDAO;
import com.oceanpro.core.entity.oceanManager.logbook.GmdssSecB;


@Component (name="gmdssSecBDao") // Il nome serve solo per i test con la classe TestDao
public class GmdssSecBDAOImpl extends GenericDAOImpl<GmdssSecB, Long> implements GmdssSecBDAO {

	public GmdssSecBDAOImpl() {
		super(GmdssSecB.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
