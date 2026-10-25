package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.oceanpro.core.dao.oceanManager.logbook.RefrigerantDAO;
import com.oceanpro.core.entity.oceanManager.logbook.Refrigerant;
import com.widee.base.dao.base.impl.GenericDAOImpl;


@Component (name="refrigerantDao") // Il nome serve solo per i test con la classe TestDao
public class RefrigerantDAOImpl extends GenericDAOImpl<Refrigerant, Long> implements RefrigerantDAO {

	
	public RefrigerantDAOImpl() {
		super(Refrigerant.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
