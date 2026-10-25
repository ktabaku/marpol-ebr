package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.EnginelogPistonCoolingTempDAO;
import com.oceanpro.core.entity.oceanManager.logbook.EnginelogPistonCoolingTemp;

@Component (name="enginelogPistonCoolingTempDao") // Il nome serve solo per i test con la classe TestDao
public class EnginelogPistonCoolingTempDAOImpl extends GenericDAOImpl<EnginelogPistonCoolingTemp, Long> implements EnginelogPistonCoolingTempDAO {

	
	public EnginelogPistonCoolingTempDAOImpl() {
		super(EnginelogPistonCoolingTemp.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
