package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.EnginelogDAO;
import com.oceanpro.core.entity.oceanManager.logbook.Enginelog;



@Component (name="enginelogDao") // Il nome serve solo per i test con la classe TestDao
public class EnginelogDAOImpl extends GenericDAOImpl<Enginelog, Long> implements EnginelogDAO {

	public EnginelogDAOImpl() {
		super(Enginelog.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}