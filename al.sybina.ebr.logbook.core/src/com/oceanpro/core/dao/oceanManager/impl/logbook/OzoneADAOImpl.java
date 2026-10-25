package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.OzoneADAO;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.OzoneA;
import com.widee.base.dao.base.impl.GenericDAOImpl;


@Component (name="ozoneADao") // Il nome serve solo per i test con la classe TestDao
public class OzoneADAOImpl extends GenericDAOImpl<OzoneA, Long> implements OzoneADAO {

	
	public OzoneADAOImpl() {
		super(OzoneA.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
