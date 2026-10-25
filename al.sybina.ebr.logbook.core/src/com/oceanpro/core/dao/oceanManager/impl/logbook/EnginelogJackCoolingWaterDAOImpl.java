package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.EnginelogJackCoolingWaterDAO;
import com.oceanpro.core.entity.oceanManager.logbook.EnginelogJackCoolingWater;

@Component (name="enginelogJackCoolingWaterDao") // Il nome serve solo per i test con la classe TestDao
public class EnginelogJackCoolingWaterDAOImpl extends GenericDAOImpl<EnginelogJackCoolingWater, Long> implements EnginelogJackCoolingWaterDAO {

	
	public EnginelogJackCoolingWaterDAOImpl() {
		super(EnginelogJackCoolingWater.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
