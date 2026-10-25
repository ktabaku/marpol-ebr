package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.OzoneBDAO;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.OzoneB;
import com.widee.base.dao.base.impl.GenericDAOImpl;


@Component (name="ozoneBDao") // Il nome serve solo per i test con la classe TestDao
public class OzoneBDAOImpl extends GenericDAOImpl<OzoneB, Long> implements OzoneBDAO {

	
	public OzoneBDAOImpl() {
		super(OzoneB.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
