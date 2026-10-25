package com.oceanpro.core.services;

import java.util.Date;
import java.util.List;

import com.oceanpro.core.entity.oceanManager.logbook.Decklog;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogBellBook;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogBellBookEngTelegraph;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogChrono;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogCompass;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogEcdis;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogFirepatrol;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogFirepatrolCheck;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogFirepatrolCheckItem;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogRadar;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogRouteMeteo;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogShipPosition;
import com.oceanpro.core.entity.oceanManager.logbook.DecklogUms;
import com.oceanpro.core.entity.oceanManager.logbook.MasterNightOrder;
import com.oceanpro.core.entity.oceanManager.logbook.PermanentChiefEngineerOrder;
import com.oceanpro.core.entity.oceanManager.logbook.PermanentNightOrder;
import com.oceanpro.core.entity.oceanManager.logbook.Refrigerant;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.DrugTest;
import com.oceanpro.core.entity.oceanManager.office.Ship;
import com.oceanpro.core.exception.DocumentException;
import com.widee.base.hibernate.criteria.Finder;


public interface DecklogService {

	//DECK
	public List<Decklog> getAllDeck();	
	public Decklog add(Decklog deck);
	public Decklog update(Decklog deck);
	public void delete(Long id);
	public Decklog getDeck(Long id);
	public Finder<Decklog> getFinder();
	
	public Decklog getDeck(Long idVoyage, Date registrationDate);
	public Decklog getDeckByUuid(String uuid);
	
	public Decklog getDeck(Long idVoyage, Date registrationDate, Long idShip);
	
	//UMS
	public List<DecklogUms> getDeckUms(Long idDeck);
	public DecklogUms getUms(Long idUms) throws DocumentException;
	public DecklogUms updateUms(DecklogUms ums);
	public DecklogUms addUms(DecklogUms deckUms);	
	public void deleteDeckUms(Long id);
	public DecklogUms getUmsByUuid(String uuid);
	
	public Finder<DecklogUms> getUmsFinder();
	
	//RADAR
	public List<DecklogRadar> getDeckRadar(Long idDeck);
	public DecklogRadar getRadar(Long idRadar) throws DocumentException;
	public DecklogRadar updateRadar(DecklogRadar radar);
	public DecklogRadar addRadar(DecklogRadar deckRadar);	
	public List<DecklogRadar> getAllRadar();
	public DecklogRadar getRadarByUuid(String uuid);
	
	public Finder<DecklogRadar> getDecklogRadarFinder();
	
	//ROUTE METEO 
	public List<DecklogRouteMeteo> getDeckRouteMeteo(Long idDeck);	
	public DecklogRouteMeteo updateRouteMeteo(DecklogRouteMeteo routeMeteo);
	public DecklogRouteMeteo addRouteMeteo(DecklogRouteMeteo deckRouteMeteo);
	
	public DecklogRouteMeteo getRouteMeteo(Long idRouteMeteo) throws DocumentException;
	public DecklogRouteMeteo getRouteMeteoByUuid(String uuid);
	
	//ECDIS	
	public List<DecklogEcdis> getDeckEcdis(Long idDeck);	
	public DecklogEcdis updateEcdis(DecklogEcdis ecdis);
	public DecklogEcdis addEcdis(DecklogEcdis deckEcdis);
	
	public DecklogEcdis getEcdis(Long idEcdis) throws DocumentException;
	public DecklogEcdis getEcdisByUuid(String uuid);
	
	//CHRONO
	public List<DecklogChrono> getDeckChrono(Long idDeck);
	public DecklogChrono getChrono(Long idChrono) throws DocumentException;
	public DecklogChrono updateChrono(DecklogChrono chrono);
	public DecklogChrono addChrono(DecklogChrono deckChrono);
	public DecklogChrono getChronoByUuid(String uuid);
	
	public Finder<DecklogChrono> getChronoFinder();
	
	//POSITION
	public List<DecklogShipPosition> getDeckPosition(Long idDeck);
	public DecklogShipPosition getPosition(Long idPosition) throws DocumentException;
	public DecklogShipPosition updatePosition(DecklogShipPosition position);
	public DecklogShipPosition addPosition(DecklogShipPosition deckPosition);
	public DecklogShipPosition getPositionByUuid(String uuid);
	
	public Finder<DecklogShipPosition> getPositionFinder();
	
	//FIREPATROL
	public List<DecklogFirepatrol> getDeckFirepatrol(Long idDeck);
	public DecklogFirepatrol getFirepatrol(Long idFierepatrol)throws DocumentException;
	public DecklogFirepatrol updateFirepatrol(DecklogFirepatrol fp);
	public DecklogFirepatrol addFirepatrol(DecklogFirepatrol fp);
	public DecklogFirepatrol getFirepatrolByUuid(String uuid);
	
	//FIREPATROL_CHECK
	public DecklogFirepatrolCheck getDeckFirepatrolCheck(Long idFierepatrol)throws DocumentException;
	public DecklogFirepatrolCheck updateFirepatrolCheck(DecklogFirepatrolCheck fpCheck);
	public DecklogFirepatrolCheck addFirepatrolCheck(DecklogFirepatrolCheck fpCheck);
	
	public List<DecklogFirepatrolCheck> getFirepatrolCheckByShip(Long idShip);
	
	public DecklogFirepatrolCheck getFirepatrolCheckByUuid(String uuid);

	//FIREPATROL_CHECK item
	public DecklogFirepatrolCheckItem getFirepatrolCheckItemByUuid(String uuid);

	//COMPASS
	public List<DecklogCompass> getDeckCompass(Long idDeck);	
	public DecklogCompass getCompass(Long idCompass) throws DocumentException;
	public DecklogCompass updateCompass(DecklogCompass compass);
	public DecklogCompass addCompass(DecklogCompass deckCompass);
	public DecklogCompass getCompassByUuid(String uuid);
	
	public Finder<DecklogCompass> getCompassFinder();
	
	//MASTER ORDER
	public List<MasterNightOrder> getAllMasterOrder();
	
	public MasterNightOrder getMasterOrder(Long idMasterOrder) throws DocumentException;
	public MasterNightOrder updateMasterOrder(MasterNightOrder masterOrder);
	public MasterNightOrder addMasterOrder(MasterNightOrder order);
	public Finder<MasterNightOrder> getMasterNightOrderFinder();
	
	public MasterNightOrder getActiveOrder();
	public MasterNightOrder getMasterNightOrderByUuid(String uuid);
	
	//PERMANENT ORDER
	public List<PermanentNightOrder> getAllPermanentOrder();
	
	public PermanentNightOrder getPermanentOrder(Long idPermanentOrder) throws DocumentException;
	public PermanentNightOrder updatePermanentOrder(PermanentNightOrder permanentOrder);
	public PermanentNightOrder addPermanentOrder(PermanentNightOrder permanentOrder);
	public Finder<PermanentNightOrder> getPermanentOrderFinder();
	
	public PermanentNightOrder getActivePermanentOrder();
	public PermanentNightOrder getPermanentNightOrderByUuid(String uuid);
	
	//REFRIGERANT
	public List<Refrigerant> getAllRefrigerant();	
	public Refrigerant getRefrigerant(Long idRefrigerant) throws DocumentException;
	public Refrigerant updateRefrigerant(Refrigerant refrigerant);
	public Refrigerant addRefrigerant(Refrigerant refrigerant);
	public Finder<Refrigerant> getRefrigerantFinder();
	
	public Refrigerant getRefrigerantByUuid(String uuid);
	
	
	//BELL BOOK
	public List<DecklogBellBook> getDeckBellBook(Long idDeck);
	
	public DecklogBellBook getBellBook(Long idBellBook) throws DocumentException;
	public DecklogBellBook updateBellBook(DecklogBellBook bellBook);
	public DecklogBellBook addBellBook(DecklogBellBook deckBellBook);
	
	public DecklogBellBook getBellBookByUuid(String uuid);
	
	//BELL BOOK Eng Telegraph
	public List<DecklogBellBookEngTelegraph> getDeckBellBookEngTel(Long idBellBook);
	
	public DecklogBellBookEngTelegraph getBellBookEngTel(Long idBellBookEngTel) throws DocumentException;
	public DecklogBellBookEngTelegraph updateBellBookEngTel(DecklogBellBookEngTelegraph bellBookEngTel);
	public DecklogBellBookEngTelegraph addBellBookEngTel(DecklogBellBookEngTelegraph deckBellBookEngTel);
	
	public DecklogBellBookEngTelegraph getBellBookEngTelByUuid(String uuid);
}
