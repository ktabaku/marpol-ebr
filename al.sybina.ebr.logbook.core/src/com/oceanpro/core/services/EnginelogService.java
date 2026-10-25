package com.oceanpro.core.services;

import java.util.Date;
import java.util.List;

import com.oceanpro.core.entity.oceanManager.logbook.Boiler;
import com.oceanpro.core.entity.oceanManager.logbook.Bunker;
import com.oceanpro.core.entity.oceanManager.logbook.ChiefEngineerOrder;
import com.oceanpro.core.entity.oceanManager.logbook.CounterEngine;
import com.oceanpro.core.entity.oceanManager.logbook.Decklog;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogEcdis;
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
import com.widee.base.hibernate.criteria.Finder;


public interface EnginelogService {


	//ENGINE
	public List<Enginelog> getAllEngine();	
	public Enginelog add(Enginelog engine);
	public Enginelog update(Enginelog engine);
	public void delete(Long id);
	public Enginelog getEngine(Long id);
	public Finder<Enginelog> getFinder();
	public Enginelog getEngine(Date registrationDate);
	
	public Enginelog getEngineByUuid(String uuid);
	
	
	
	//ENGINE - EXAUST GAS TEMP
	public List<EnginelogExaustGasTemp> getEngineExaustGas(Long idEngine);	
	public EnginelogExaustGasTemp updateExaustGasTemp(EnginelogExaustGasTemp exaustGasTemp);
	public EnginelogExaustGasTemp addExaustGasTemp(EnginelogExaustGasTemp exaustGasTemp);
	public List<EnginelogExaustGasTemp> getAllExaustGasTemp ();		
	public EnginelogExaustGasTemp getExaustGas(Long idExaustGas) throws DocumentException;

	public EnginelogExaustGasTemp getExaustGasByUuid(String uuid);
	
	//ENGINE - JACK COOLING WATER
	public List<EnginelogJackCoolingWater> getEngineJackCoolingWater(Long idEngine);	
	public EnginelogJackCoolingWater updateJackCoolingWater(EnginelogJackCoolingWater jackCoolingWater);
	public EnginelogJackCoolingWater addJackCoolingWater(EnginelogJackCoolingWater jackCoolingWater);
	public List<EnginelogJackCoolingWater> getAllJackCoolingWater();		
	public EnginelogJackCoolingWater getJackCoolingWater(Long idJackCoolingWater) throws DocumentException;
	
	public EnginelogJackCoolingWater getJackCoolingWaterByUuid(String uuid);
	
	//ENGINE - PISTON COOLING OUTLET TEMP
	public List<EnginelogPistonCoolingTemp> getEnginePistonCoolingTemp(Long idEngine);	
	public EnginelogPistonCoolingTemp updatePistonCoolingTemp(EnginelogPistonCoolingTemp pistonCoolingTemp);
	public EnginelogPistonCoolingTemp addPistonCoolingTemp(EnginelogPistonCoolingTemp pistonCoolingTemp);
	public List<EnginelogPistonCoolingTemp> getAllPistonCoolingTemp();		
	public EnginelogPistonCoolingTemp getPistonCoolingTemp(Long idPistonCoolingTemp) throws DocumentException;
	
	public EnginelogPistonCoolingTemp getPistonCoolingTempByUuid(String uuid);
	
	//ENGINE - SCAVANGE AIR TEMP TEMP
	public List<EnginelogScavangeAirTemp> getEngineScavangeAirTemp(Long idEngine);	
	public EnginelogScavangeAirTemp updateScavangeAirTemp(EnginelogScavangeAirTemp scavangeAirTemp);
	public EnginelogScavangeAirTemp addScavangeAirTemp(EnginelogScavangeAirTemp scavangeAirTemp);
	public List<EnginelogScavangeAirTemp> getAllScavangeAirTemp();		
	public EnginelogScavangeAirTemp getScavangeAirTemp(Long idScavangeAirTemp) throws DocumentException;
	
	public EnginelogScavangeAirTemp getScavangeAirTempByUuid(String uuid);
	
	//CHIEF ENG ORDER
	public List<ChiefEngineerOrder> getAllChiefEngineerOrder();
	public ChiefEngineerOrder getChiefEngineerOrder(Long idChiefEngOrder) throws DocumentException;
	public ChiefEngineerOrder updateChiefEngOrder(ChiefEngineerOrder chiefEngOrder);
	public ChiefEngineerOrder addChiefEngOrder(ChiefEngineerOrder order);
	public Finder<ChiefEngineerOrder> getChiefEngOrderFinder();
	public ChiefEngineerOrder getActiveOrder();
	
	public ChiefEngineerOrder getChiefEngineerOrderByUuid(String uuid);
	
	//PERMANENT CHIEF ENG ORDER
	public List<PermanentChiefEngineerOrder> getAllPermanentChiefEngOrder();
	public PermanentChiefEngineerOrder getPermanentChiefEngOrder(Long idPermanentChiefEngOrder) throws DocumentException;
	public PermanentChiefEngineerOrder updatePermanentChiefEngOrder(PermanentChiefEngineerOrder permanentChiefEngOrder);
	public PermanentChiefEngineerOrder addPermanentChiefEngOrder(PermanentChiefEngineerOrder permanentChiefEngOrder);
	public Finder<PermanentChiefEngineerOrder> getPermanentChiefEngOrderFinder();
	public PermanentChiefEngineerOrder getActivePermanentChiefEngOrder();
	
	public PermanentChiefEngineerOrder getPermanentChiefEngineerOrderByUuid(String uuid);
	
	
	//BOILER
	public List<Boiler> getAllBoiler();	
	public Boiler getBoiler(Long idBoiler) throws DocumentException;
	public Boiler updateBoiler(Boiler boiler);
	public Boiler addBoiler(Boiler boiler);
	public Finder<Boiler> getBoilerFinder();
	
	public Boiler getBoilerByUuid(String uuid);

	//IGS GAS GENERATOR
	public List<IgsGasGenerator> getAllIgsGasGenerator();	
	public IgsGasGenerator getIgsGasGenerator(Long idIgsGasGenerator) throws DocumentException;
	public IgsGasGenerator updateIgsGasGenerator(IgsGasGenerator igsGasGenerator);
	public IgsGasGenerator addIgsGasGenerator(IgsGasGenerator igsGasGenerator);
	public Finder<IgsGasGenerator> getIgsGasGeneratorFinder();

	public IgsGasGenerator getIgsGasGeneratorByUuid(String uuid);	
	
	
	//DIESEL GENERATOR
	public List<DieselGenerator> getAllDieselGenerator();	
	public DieselGenerator getDieselGenerator(Long idDieselGenerator) throws DocumentException;
	public DieselGenerator updateDieselGenerator(DieselGenerator dieselGenerator);
	public DieselGenerator addDieselGenerator(DieselGenerator dieselGenerator);
	public Finder<DieselGenerator> getDieselGeneratorFinder();
	
	public DieselGenerator getDieselGeneratorByUuid(String uuid);
	
	//COUNTER ENGINE
	public List<CounterEngine> getAllCounterEngine();	
	public CounterEngine getCounterEngine(Long idCounterEngine) throws DocumentException;
	public CounterEngine updateCounterEngine(CounterEngine counterEngine);
	public CounterEngine addCounterEngine(CounterEngine counterEngine);
	public Finder<CounterEngine> getCounterEngineFinder();
	
	public CounterEngine getCounterEngineByUuid(String uuid);
	
	//BUNKER
	public List<Bunker> getAllBunker();	
	public Bunker getBunker(Long idBunker) throws DocumentException;
	public Bunker updateBunker(Bunker bunker);
	public Bunker addBunker(Bunker bunker);
	public Finder<Bunker> getBunkerFinder();
	
	public Bunker getBunkerByUuid(String uuid);
	
	
}
