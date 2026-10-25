package com.oceanpro.core.dao.oceanManager.impl.logbook;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.osgi.service.component.annotations.Component;

import com.widee.base.dao.base.impl.GenericDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogRadarDAO;
import com.oceanpro.core.dao.oceanManager.logbook.EnginelogExaustGasTempDAO;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogRadar;
import com.oceanpro.core.entity.oceanManager.logbook.EnginelogExaustGasTemp;

@Component (name="enginelogExaustGasTempDao") // Il nome serve solo per i test con la classe TestDao
public class EnginelogExaustGasTempDAOImpl extends GenericDAOImpl<EnginelogExaustGasTemp, Long> implements EnginelogExaustGasTempDAO {

	
	public EnginelogExaustGasTempDAOImpl() {
		super(EnginelogExaustGasTemp.class);
	}
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
