package com.oceanpro.core.services.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.oceanpro.core.dao.oceanManager.impl.logbook.BoilerDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.BunkerDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.ChiefEngineerOrderDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.CounterEngineDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DecklogChronoDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DecklogCompassDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DecklogDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DecklogEcdisDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DecklogFirepatrolCheckDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DecklogFirepatrolCheckItemDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DecklogFirepatrolDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DecklogPositionDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DecklogRadarDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DecklogRouteMeteoDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DecklogUmsDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DieselGeneratorDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.EnginelogDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.EnginelogExaustGasTempDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.EnginelogJackCoolingWaterDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.EnginelogPistonCoolingTempDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.EnginelogScavangeAirTempDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.IgsGasGeneratorDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.MasterNightOrderDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.PermanentChiefEngineerOrderDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.BoilerDAO;
import com.oceanpro.core.dao.oceanManager.logbook.BunkerDAO;
import com.oceanpro.core.dao.oceanManager.logbook.ChiefEngineerOrderDAO;
import com.oceanpro.core.dao.oceanManager.logbook.CounterEngineDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DieselGeneratorDAO;
import com.oceanpro.core.dao.oceanManager.logbook.EnginelogDAO;
import com.oceanpro.core.dao.oceanManager.logbook.EnginelogExaustGasTempDAO;
import com.oceanpro.core.dao.oceanManager.logbook.EnginelogJackCoolingWaterDAO;
import com.oceanpro.core.dao.oceanManager.logbook.EnginelogPistonCoolingTempDAO;
import com.oceanpro.core.dao.oceanManager.logbook.EnginelogScavangeAirTempDAO;
import com.oceanpro.core.dao.oceanManager.logbook.IgsGasGeneratorDAO;
import com.oceanpro.core.dao.oceanManager.logbook.PermanentChiefEngineerOrderDAO;
import com.oceanpro.core.dao.oceanManager.logbook.VisitorDAO;
import com.oceanpro.core.entity.oceanManager.logbook.Boiler;
import com.oceanpro.core.entity.oceanManager.logbook.Bunker;
import com.oceanpro.core.entity.oceanManager.logbook.ChiefEngineerOrder;
import com.oceanpro.core.entity.oceanManager.logbook.CounterEngine;
import com.oceanpro.core.entity.oceanManager.logbook.Decklog;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogBellBookEngTelegraph;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogEcdis;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogRouteMeteo;
import com.oceanpro.core.entity.oceanManager.logbook.DieselGenerator;
import com.oceanpro.core.entity.oceanManager.logbook.Enginelog;
import com.oceanpro.core.entity.oceanManager.logbook.EnginelogExaustGasTemp;
import com.oceanpro.core.entity.oceanManager.logbook.EnginelogJackCoolingWater;
import com.oceanpro.core.entity.oceanManager.logbook.EnginelogPistonCoolingTemp;
import com.oceanpro.core.entity.oceanManager.logbook.EnginelogScavangeAirTemp;
import com.oceanpro.core.entity.oceanManager.logbook.IgsGasGenerator;
import com.oceanpro.core.entity.oceanManager.logbook.PermanentChiefEngineerOrder;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.OilLog;
import com.oceanpro.core.exception.DocumentException;
import com.oceanpro.core.services.EnginelogService;
import com.widee.base.hibernate.criteria.Finder;

import al.sybina.ebr.commons.utils.EnvironmentalEntityManagerFactory;
import al.sybina.ebr.logbook.core.Activator;
import al.sybina.ebr.core.tools.PluginTools;

@Creatable("EnginelogService")
@Transactional
public class EnginelogServiceImpl implements EnginelogService{

//	@Autowired
	private ChiefEngineerOrderDAO chiefEngOrderDAO;
	
//	@Autowired
	private PermanentChiefEngineerOrderDAO permanentChiefEngOrderDAO;
		
//	@Autowired
	private BoilerDAO boilerDAO;
	
//	@Autowired
	private IgsGasGeneratorDAO igsGasGeneratorDAO;
	
//	@Autowired
	private EnginelogDAO enginelogDAO;
	
//	@Autowired
	private EnginelogExaustGasTempDAO enginelogExaustGasTempDAO;
	
//	@Autowired
	private EnginelogJackCoolingWaterDAO enginelogJackCoolingWaterDAO;
	
//	@Autowired
	private EnginelogPistonCoolingTempDAO enginelogPistonCoolingTempDAO;

//	@Autowired
	private EnginelogScavangeAirTempDAO enginelogScavangeAirTempDAO;
	
//	@Autowired
	private DieselGeneratorDAO dieselGeneratorDAO;
	
//	@Autowired
	private CounterEngineDAO counterEngineDAO;
	
//	@Autowired
	private BunkerDAO bunkerDAO;
	
	private EntityManagerFactory emf;
    private EntityManager em;

	protected void activate() {
		em = Activator.getEntityManager();

		chiefEngOrderDAO = new ChiefEngineerOrderDAOImpl();
		chiefEngOrderDAO.setEntityManager(em);

		permanentChiefEngOrderDAO = new PermanentChiefEngineerOrderDAOImpl();
		permanentChiefEngOrderDAO.setEntityManager(em);

		boilerDAO = new BoilerDAOImpl();
		boilerDAO.setEntityManager(em);

		igsGasGeneratorDAO = new IgsGasGeneratorDAOImpl();
		igsGasGeneratorDAO.setEntityManager(em);

		enginelogDAO = new EnginelogDAOImpl();
		enginelogDAO.setEntityManager(em);

		enginelogExaustGasTempDAO = new EnginelogExaustGasTempDAOImpl();
		enginelogExaustGasTempDAO.setEntityManager(em);

		enginelogJackCoolingWaterDAO = new EnginelogJackCoolingWaterDAOImpl();
		enginelogJackCoolingWaterDAO.setEntityManager(em);

		enginelogPistonCoolingTempDAO = new EnginelogPistonCoolingTempDAOImpl();
		enginelogPistonCoolingTempDAO.setEntityManager(em);

		enginelogScavangeAirTempDAO = new EnginelogScavangeAirTempDAOImpl();
		enginelogScavangeAirTempDAO.setEntityManager(em);

		dieselGeneratorDAO = new DieselGeneratorDAOImpl();
		dieselGeneratorDAO.setEntityManager(em);

		counterEngineDAO = new CounterEngineDAOImpl();
		counterEngineDAO.setEntityManager(em);

		bunkerDAO = new BunkerDAOImpl();
		bunkerDAO.setEntityManager(em);
	}
	
    protected void deactivate()
    {
    	chiefEngOrderDAO=null;
    	permanentChiefEngOrderDAO=null;
    	boilerDAO=null;
    	igsGasGeneratorDAO=null;
    	enginelogDAO=null;
    	enginelogExaustGasTempDAO=null;
    	enginelogJackCoolingWaterDAO=null;
    	enginelogPistonCoolingTempDAO=null;
    	enginelogScavangeAirTempDAO=null;
    	dieselGeneratorDAO=null;
    	counterEngineDAO=null;
    	bunkerDAO=null;

    }
	//**********    Engine log    **********	
	@Override
	public List<Enginelog> getAllEngine() {
		return enginelogDAO.getAll();
	}
	
	@Override
	public Enginelog add(Enginelog engine) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		engine.setUuid(UUID.randomUUID().toString());	
		Enginelog enginelog = enginelogDAO.create(engine);
		
		entityTransaction.commit();		
		return enginelog;
	}
	
	@Override
	public Enginelog update(Enginelog engine) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		Enginelog enginelog = enginelogDAO.update(engine);
		
		entityTransaction.commit();		
		return enginelog;
	}

	@Override
	public void delete(Long id) {
		if (id!=null) {
			EntityTransaction entityTransaction = em.getTransaction();
			entityTransaction.begin();
			
			enginelogDAO.delete(id);
			
			entityTransaction.commit();
		}
		
	}

	@Override
	public Enginelog getEngine(Long id) {
		return  enginelogDAO.get(id);
	}
	
	@Override
	public Finder<Enginelog> getFinder() {
		return enginelogDAO.getFinder();
	}

	@Override
	public Enginelog getEngine(Date registrationDate) {
		Enginelog el = enginelogDAO.getFinder().and("registrationDate").eq(registrationDate).setDistinct().result();
		return el;
	}
	
	@Override
	public Enginelog getEngineByUuid(String uuid) {
		return enginelogDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}

	
	//********** Enginelog Exaust Gas Temp  **********

	@Override
	public List<EnginelogExaustGasTemp> getEngineExaustGas(Long idEngine) {
		List<EnginelogExaustGasTemp> elegt = enginelogExaustGasTempDAO.getFinder().and("engine.id").eq(idEngine).setDistinct().list();
		return elegt;
	}

	@Override
	public EnginelogExaustGasTemp updateExaustGasTemp(EnginelogExaustGasTemp exaustGasTemp) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		EnginelogExaustGasTemp enginelogExaustGasTemp = enginelogExaustGasTempDAO.update(exaustGasTemp);
		
		entityTransaction.commit();
		return enginelogExaustGasTemp;
	}
	
	@Override
	public EnginelogExaustGasTemp addExaustGasTemp(EnginelogExaustGasTemp exaustGasTemp) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		exaustGasTemp.setUuid(UUID.randomUUID().toString());
		EnginelogExaustGasTemp enginelogExaustGasTemp = enginelogExaustGasTempDAO.create(exaustGasTemp);
		
		entityTransaction.commit();
		return enginelogExaustGasTemp;

	}

	@Override
	public List<EnginelogExaustGasTemp> getAllExaustGasTemp() {
		return enginelogExaustGasTempDAO.getAll();
	}
	
	@Override
	public EnginelogExaustGasTemp getExaustGas(Long idExaustGas) throws DocumentException {
		return enginelogExaustGasTempDAO.get(idExaustGas);
	}
	
	
	@Override
	public EnginelogExaustGasTemp getExaustGasByUuid(String uuid) {
		return enginelogExaustGasTempDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	

	//********** Enginelog Jack Cooling Water  **********	
	@Override
	public List<EnginelogJackCoolingWater> getEngineJackCoolingWater(Long idEngine) {
		List<EnginelogJackCoolingWater> eljcw = enginelogJackCoolingWaterDAO.getFinder().and("engine.id").eq(idEngine).setDistinct().list();
		return eljcw;
	}

	@Override
	public EnginelogJackCoolingWater updateJackCoolingWater(EnginelogJackCoolingWater jackCoolingWater) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		EnginelogJackCoolingWater enginelogJackCoolingWater = enginelogJackCoolingWaterDAO.update(jackCoolingWater);
		
		entityTransaction.commit();
		return enginelogJackCoolingWater;
	}

	@Override
	public EnginelogJackCoolingWater addJackCoolingWater(EnginelogJackCoolingWater jackCoolingWater) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		jackCoolingWater.setUuid(UUID.randomUUID().toString());
		EnginelogJackCoolingWater enginelogJackCoolingWater = enginelogJackCoolingWaterDAO.create(jackCoolingWater);
		
		entityTransaction.commit();
		return enginelogJackCoolingWater;
	}

	@Override
	public List<EnginelogJackCoolingWater> getAllJackCoolingWater() {
		return enginelogJackCoolingWaterDAO.getAll();
	}

	@Override
	public EnginelogJackCoolingWater getJackCoolingWater(Long idJackCoolingWater) throws DocumentException {
		return enginelogJackCoolingWaterDAO.get(idJackCoolingWater);
	}
	
	@Override
	public EnginelogJackCoolingWater getJackCoolingWaterByUuid(String uuid) {
		return enginelogJackCoolingWaterDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	

	//********** Enginelog  Piston Cooling Outlet Temp  **********
	@Override
	public List<EnginelogPistonCoolingTemp> getEnginePistonCoolingTemp(Long idEngine) {
		List<EnginelogPistonCoolingTemp> elpct = enginelogPistonCoolingTempDAO.getFinder().and("engine.id").eq(idEngine).setDistinct().list();
		return elpct;
	}

	@Override
	public EnginelogPistonCoolingTemp updatePistonCoolingTemp(EnginelogPistonCoolingTemp pistonCoolingTemp) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		EnginelogPistonCoolingTemp enginelogPistonCoolingTemp = enginelogPistonCoolingTempDAO.update(pistonCoolingTemp);
		
		entityTransaction.commit();
		return enginelogPistonCoolingTemp;
	}

	@Override
	public EnginelogPistonCoolingTemp addPistonCoolingTemp(EnginelogPistonCoolingTemp pistonCoolingTemp) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		pistonCoolingTemp.setUuid(UUID.randomUUID().toString());
		EnginelogPistonCoolingTemp enginelogPistonCoolingTemp = enginelogPistonCoolingTempDAO.create(pistonCoolingTemp);
		
		entityTransaction.commit();
		return enginelogPistonCoolingTemp;
	}

	@Override
	public List<EnginelogPistonCoolingTemp> getAllPistonCoolingTemp() {
		return enginelogPistonCoolingTempDAO.getAll();
	}

	@Override
	public EnginelogPistonCoolingTemp getPistonCoolingTemp(Long idPistonCoolingTemp) throws DocumentException {
		return enginelogPistonCoolingTempDAO.get(idPistonCoolingTemp);
	}

	@Override
	public EnginelogPistonCoolingTemp getPistonCoolingTempByUuid(String uuid) {
		return enginelogPistonCoolingTempDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	

	//********** Enginelog Scavange Air Temp  **********
	@Override
	public List<EnginelogScavangeAirTemp> getEngineScavangeAirTemp(Long idEngine) {
		List<EnginelogScavangeAirTemp> elsat = enginelogScavangeAirTempDAO.getFinder().and("engine.id").eq(idEngine).setDistinct().list();
		return elsat;
	}

	@Override
	public EnginelogScavangeAirTemp updateScavangeAirTemp(EnginelogScavangeAirTemp scavangeAirTemp) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		EnginelogScavangeAirTemp enginelogScavangeAirTemp = enginelogScavangeAirTempDAO.update(scavangeAirTemp);
		
		entityTransaction.commit();
		return enginelogScavangeAirTemp;
	}

	@Override
	public EnginelogScavangeAirTemp addScavangeAirTemp(EnginelogScavangeAirTemp scavangeAirTemp) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		scavangeAirTemp.setUuid(UUID.randomUUID().toString());
		EnginelogScavangeAirTemp enginelogScavangeAirTemp = enginelogScavangeAirTempDAO.create(scavangeAirTemp);
		
		entityTransaction.commit();
		return enginelogScavangeAirTemp;
		
	}

	@Override
	public List<EnginelogScavangeAirTemp> getAllScavangeAirTemp() {
		return enginelogScavangeAirTempDAO.getAll();
	}

	@Override
	public EnginelogScavangeAirTemp getScavangeAirTemp(Long idScavangeAirTemp) throws DocumentException {
		return enginelogScavangeAirTempDAO.get(idScavangeAirTemp);
	}

	@Override
	public EnginelogScavangeAirTemp getScavangeAirTempByUuid(String uuid) {
		return enginelogScavangeAirTempDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	
	//********** Chif Eng Order    **********
	@Override
	public List<ChiefEngineerOrder> getAllChiefEngineerOrder() {
		return chiefEngOrderDAO.getAll();
	}

	@Override
	public ChiefEngineerOrder getChiefEngineerOrder(Long idChiefEngOrder) throws DocumentException {
		return chiefEngOrderDAO.get(idChiefEngOrder);
	}

	@Override
	public ChiefEngineerOrder updateChiefEngOrder(ChiefEngineerOrder chiefEngOrder) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		ChiefEngineerOrder chiefEngineerOrder = chiefEngOrderDAO.update(chiefEngOrder);
		
		entityTransaction.commit();
		return chiefEngineerOrder;
	}
	
	@Override
	public ChiefEngineerOrder addChiefEngOrder(ChiefEngineerOrder order) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		order.setUuid(UUID.randomUUID().toString());
		order.setActive(false);
		ChiefEngineerOrder chiefEngineerOrder = chiefEngOrderDAO.create(order);
		
		entityTransaction.commit();
		return chiefEngineerOrder;
	}
	
	@Override
	public Finder<ChiefEngineerOrder> getChiefEngOrderFinder() {
		return chiefEngOrderDAO.getFinder();
	}

	@Override
	public ChiefEngineerOrder getActiveOrder() {
		return chiefEngOrderDAO.getFinder().and("active").eq(true).result();
	}

	@Override
	public ChiefEngineerOrder getChiefEngineerOrderByUuid(String uuid) {
		return chiefEngOrderDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	
	
	//********** Permanent Chieg Eng Order    **********
	@Override
	public List<PermanentChiefEngineerOrder> getAllPermanentChiefEngOrder() {
		return permanentChiefEngOrderDAO.getAll();
	}
	
	@Override
	public PermanentChiefEngineerOrder getPermanentChiefEngOrder(Long idPermanentChiefEngOrder) throws DocumentException {
		return permanentChiefEngOrderDAO.get(idPermanentChiefEngOrder);
	}
	
	@Override
	public PermanentChiefEngineerOrder updatePermanentChiefEngOrder(PermanentChiefEngineerOrder permanentChiefEngOrder) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		PermanentChiefEngineerOrder permanentChiefEngineerOrder = permanentChiefEngOrderDAO.update(permanentChiefEngOrder);
		
		entityTransaction.commit();
		return permanentChiefEngineerOrder;
	}

	@Override
	public PermanentChiefEngineerOrder addPermanentChiefEngOrder(PermanentChiefEngineerOrder permanentChiefEngOrder) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		permanentChiefEngOrder.setUuid(UUID.randomUUID().toString());
		permanentChiefEngOrder.setActive(false);
		PermanentChiefEngineerOrder permanentChiefEngineerOrder = permanentChiefEngOrderDAO.create(permanentChiefEngOrder);
		
		entityTransaction.commit();
		return permanentChiefEngineerOrder;
	}

	@Override
	public Finder<PermanentChiefEngineerOrder> getPermanentChiefEngOrderFinder() {
		return permanentChiefEngOrderDAO.getFinder();
	}

	@Override
	public PermanentChiefEngineerOrder getActivePermanentChiefEngOrder() {
		return permanentChiefEngOrderDAO.getFinder().and("active").eq(true).result();
	}

	@Override
	public PermanentChiefEngineerOrder getPermanentChiefEngineerOrderByUuid(String uuid) {
		return permanentChiefEngOrderDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	
	
	//BOILER
	@Override
	public List<Boiler> getAllBoiler() {
		return boilerDAO.getAll();
	}

	@Override
	public Boiler getBoiler(Long idBoiler) throws DocumentException {
		return boilerDAO.get(idBoiler);
	}

	@Override
	public Boiler updateBoiler(Boiler boiler) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		Boiler boilerUpdated = boilerDAO.update(boiler);
		
		entityTransaction.commit();
		return boilerUpdated;
	}

	@Override
	public Boiler addBoiler(Boiler boiler) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		boiler.setUuid(UUID.randomUUID().toString());
		Boiler boilerUpdated = boilerDAO.create(boiler);
		
		entityTransaction.commit();
		return boilerUpdated;
	}

	@Override
	public Finder<Boiler> getBoilerFinder() {
		return boilerDAO.getFinder();
	}
	
	@Override
	public Boiler getBoilerByUuid(String uuid) {
		return boilerDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}


	//IGS GAS GENERATOR
	@Override
	public List<IgsGasGenerator> getAllIgsGasGenerator() {
		return igsGasGeneratorDAO.getAll();
	}

	@Override
	public IgsGasGenerator getIgsGasGenerator(Long idIgsGasGenerator) throws DocumentException {
		return igsGasGeneratorDAO.get(idIgsGasGenerator);
	}

	@Override
	public IgsGasGenerator updateIgsGasGenerator(IgsGasGenerator igsGasGenerator) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		IgsGasGenerator igsGasGeneratorUpdated = igsGasGeneratorDAO.update(igsGasGenerator);
		
		entityTransaction.commit();
		return igsGasGeneratorUpdated;
	}

	@Override
	public IgsGasGenerator addIgsGasGenerator(IgsGasGenerator igsGasGenerator) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		igsGasGenerator.setUuid(UUID.randomUUID().toString());
		IgsGasGenerator igsGasGeneratorAdded = igsGasGeneratorDAO.create(igsGasGenerator);
		
		entityTransaction.commit();
		return igsGasGeneratorAdded;
	}

	@Override
	public Finder<IgsGasGenerator> getIgsGasGeneratorFinder() {
		return igsGasGeneratorDAO.getFinder();
	}

	@Override
	public IgsGasGenerator getIgsGasGeneratorByUuid(String uuid) {
		return igsGasGeneratorDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	

	//DIESEL GENERATOR
	@Override
	public List<DieselGenerator> getAllDieselGenerator() {
		return dieselGeneratorDAO.getAll();
	}

	@Override
	public DieselGenerator getDieselGenerator(Long idDieselGenerator) throws DocumentException {
		return dieselGeneratorDAO.get(idDieselGenerator);
	}

	@Override
	public DieselGenerator updateDieselGenerator(DieselGenerator dieselGenerator) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		DieselGenerator dieselGeneratorUpdated = dieselGeneratorDAO.update(dieselGenerator);
		
		entityTransaction.commit();
		return dieselGeneratorUpdated; 
	}

	@Override
	public DieselGenerator addDieselGenerator(DieselGenerator dieselGenerator) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		dieselGenerator.setUuid(UUID.randomUUID().toString());
		DieselGenerator dieselGeneratorAdded = dieselGeneratorDAO.create(dieselGenerator);
		
		entityTransaction.commit();
		return dieselGeneratorAdded; 
	}

	@Override
	public Finder<DieselGenerator> getDieselGeneratorFinder() {
		return dieselGeneratorDAO.getFinder();
	}


	@Override
	public DieselGenerator getDieselGeneratorByUuid(String uuid) {
		return dieselGeneratorDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	//COUNTER ENGINE
	@Override
	public List<CounterEngine> getAllCounterEngine() {
		return counterEngineDAO.getAll();
	}

	@Override
	public CounterEngine getCounterEngine(Long idCounterEngine) throws DocumentException {
		return counterEngineDAO.get(idCounterEngine);
	}

	@Override
	public CounterEngine updateCounterEngine(CounterEngine counterEngine) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		CounterEngine counterEngineUpdated = counterEngineDAO.update(counterEngine);
		
		entityTransaction.commit();
		return counterEngineUpdated;
	}

	@Override
	public CounterEngine addCounterEngine(CounterEngine counterEngine) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		counterEngine.setUuid(UUID.randomUUID().toString());
		CounterEngine counterEngineAdded = counterEngineDAO.create(counterEngine);
		
		entityTransaction.commit();
		return counterEngineAdded;
	}

	@Override
	public Finder<CounterEngine> getCounterEngineFinder() {
		return counterEngineDAO.getFinder();
	}
	
	@Override
	public CounterEngine getCounterEngineByUuid(String uuid) {
		return counterEngineDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	
	//BUNKER
	@Override
	public List<Bunker> getAllBunker() {
		return bunkerDAO.getAll();
	}

	@Override
	public Bunker getBunker(Long idBunker) throws DocumentException {
		return bunkerDAO.get(idBunker);
	}

	@Override
	public Bunker updateBunker(Bunker bunker) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		Bunker bunkerUpdated = bunkerDAO.update(bunker);
		
		entityTransaction.commit();
		return bunkerUpdated;
	}

	@Override
	public Bunker addBunker(Bunker bunker) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		bunker.setUuid(UUID.randomUUID().toString());
		Bunker bunkerAdded = bunkerDAO.create(bunker);
		
		entityTransaction.commit();
		return bunkerAdded;
	}

	@Override
	public Finder<Bunker> getBunkerFinder() {
		return bunkerDAO.getFinder();
	}	

	@Override
	public Bunker getBunkerByUuid(String uuid) {
		return bunkerDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}	


}
