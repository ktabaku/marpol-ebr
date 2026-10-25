package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.GmdssAnnexDAO;
import com.oceanpro.core.entity.oceanManager.logbook.GmdssAnnex;


@Component (name="gmdssAnnexDao") // Il nome serve solo per i test con la classe TestDao
public class GmdssAnnexDAOImpl extends GenericDAOImpl<GmdssAnnex, Long> implements GmdssAnnexDAO {

	public GmdssAnnexDAOImpl() {
		super(GmdssAnnex.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
