package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.EnginelogScavangeAirTempDAO;
import com.oceanpro.core.entity.oceanManager.logbook.EnginelogScavangeAirTemp;

@Component (name="enginelogScavangeAirTempDao") // Il nome serve solo per i test con la classe TestDao
public class EnginelogScavangeAirTempDAOImpl extends GenericDAOImpl<EnginelogScavangeAirTemp, Long> implements EnginelogScavangeAirTempDAO {

	
	public EnginelogScavangeAirTempDAOImpl() {
		super(EnginelogScavangeAirTemp.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
