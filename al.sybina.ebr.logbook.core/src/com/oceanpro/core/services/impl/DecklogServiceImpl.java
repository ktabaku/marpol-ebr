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

import com.oceanpro.core.dao.oceanManager.impl.logbook.DecklogBellBookDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DecklogBellBookEngTelegraphDAOImpl;
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
import com.oceanpro.core.dao.oceanManager.impl.logbook.MasterNightOrderDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.PermanentNightOrderDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.RefrigerantDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogBellBookDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogBellBookEngTelegraphDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogChronoDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogCompassDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogEcdisDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogFirepatrolCheckDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogFirepatrolCheckItemDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogFirepatrolDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogPositionDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogRadarDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogRouteMeteoDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DecklogUmsDAO;
import com.oceanpro.core.dao.oceanManager.logbook.MasterNightOrderDAO;
import com.oceanpro.core.dao.oceanManager.logbook.PermanentNightOrderDAO;
import com.oceanpro.core.dao.oceanManager.logbook.RefrigerantDAO;
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
import com.oceanpro.core.entity.oceanManager.logbook.GmdssAnnex;
import com.oceanpro.core.entity.oceanManager.logbook.MasterNightOrder;
import com.oceanpro.core.entity.oceanManager.logbook.PermanentNightOrder;
import com.oceanpro.core.entity.oceanManager.logbook.Refrigerant;
import com.oceanpro.core.exception.DocumentException;
import com.oceanpro.core.services.DecklogService;
import com.widee.base.hibernate.criteria.Finder;

import al.sybina.ebr.commons.utils.EnvironmentalEntityManagerFactory;
import al.sybina.ebr.logbook.core.Activator;
import al.sybina.ebr.core.tools.PluginTools;

@Creatable("DecklogService")
@Transactional
public class DecklogServiceImpl implements DecklogService{

//	@Autowired
	private DecklogDAO decklogDAO;
	 
//	@Autowired
	private DecklogUmsDAO decklogUmsDAO;
	
//	@Autowired
	private DecklogRadarDAO decklogRadarDAO;
	
//	@Autowired
	private DecklogRouteMeteoDAO decklogRouteMeteoDAO;
	
//	@Autowired
	private DecklogEcdisDAO decklogEcdisDAO;
	
//	@Autowired
	private DecklogChronoDAO decklogChronoDAO;
	
//	@Autowired
	private DecklogPositionDAO decklogPositionDAO;
	
//	@Autowired
	private DecklogFirepatrolCheckDAO decklogFirepatrolCheckDAO;
	
//	@Autowired
	private DecklogFirepatrolCheckItemDAO decklogFirepatrolCheckItemDAO;
	
//	@Autowired
	private DecklogFirepatrolDAO decklogFirepatrolDAO;
	
//	@Autowired
	private DecklogCompassDAO decklogCompassDAO;
	
//	@Autowired
	private MasterNightOrderDAO masterNightOrderDAO;
	
//	@Autowired
	private PermanentNightOrderDAO permanentOrderDAO;
	
//	@Autowired
	private RefrigerantDAO refrigerantDAO;
	
	
//	@Autowired
	private DecklogBellBookDAO decklogBellBookDAO;

//	@Autowired
	private DecklogBellBookEngTelegraphDAO decklogBellBookEngTelDAO;
	
	
	private EntityManagerFactory emf;
    private EntityManager em;

	protected void activate() {
		em = Activator.getEntityManager();

		decklogDAO = new DecklogDAOImpl();
		decklogDAO.setEntityManager(em);

		decklogUmsDAO = new DecklogUmsDAOImpl();
		decklogUmsDAO.setEntityManager(em);

		decklogRadarDAO = new DecklogRadarDAOImpl();
		decklogRadarDAO.setEntityManager(em);

		decklogRouteMeteoDAO = new DecklogRouteMeteoDAOImpl();
		decklogRouteMeteoDAO.setEntityManager(em);

		decklogEcdisDAO = new DecklogEcdisDAOImpl();
		decklogEcdisDAO.setEntityManager(em);

		decklogChronoDAO = new DecklogChronoDAOImpl();
		decklogChronoDAO.setEntityManager(em);

		decklogPositionDAO = new DecklogPositionDAOImpl();
		decklogPositionDAO.setEntityManager(em);

		decklogFirepatrolCheckDAO = new DecklogFirepatrolCheckDAOImpl();
		decklogFirepatrolCheckDAO.setEntityManager(em);

		decklogFirepatrolCheckItemDAO = new DecklogFirepatrolCheckItemDAOImpl();
		decklogFirepatrolCheckItemDAO.setEntityManager(em);

		decklogFirepatrolDAO = new DecklogFirepatrolDAOImpl();
		decklogFirepatrolDAO.setEntityManager(em);

		decklogCompassDAO = new DecklogCompassDAOImpl();
		decklogCompassDAO.setEntityManager(em);

		masterNightOrderDAO = new MasterNightOrderDAOImpl();
		masterNightOrderDAO.setEntityManager(em);

		permanentOrderDAO = new PermanentNightOrderDAOImpl();
		permanentOrderDAO.setEntityManager(em);

		refrigerantDAO = new RefrigerantDAOImpl();
		refrigerantDAO.setEntityManager(em);

		permanentOrderDAO = new PermanentNightOrderDAOImpl();
		permanentOrderDAO.setEntityManager(em);

		decklogBellBookDAO = new DecklogBellBookDAOImpl();
		decklogBellBookDAO.setEntityManager(em);

		decklogBellBookEngTelDAO = new DecklogBellBookEngTelegraphDAOImpl();
		decklogBellBookEngTelDAO.setEntityManager(em);

	}

    protected void deactivate()
    {
    	decklogDAO=null;
    	decklogUmsDAO=null;
    	decklogRadarDAO=null;
    	decklogRouteMeteoDAO=null;
    	decklogEcdisDAO=null;
    	decklogChronoDAO=null;
    	decklogPositionDAO=null;
    	decklogFirepatrolCheckDAO=null;
    	decklogFirepatrolCheckItemDAO=null;
    	decklogFirepatrolDAO=null;
    	decklogCompassDAO=null;
    	masterNightOrderDAO=null;
    	permanentOrderDAO=null;
    	decklogBellBookDAO=null;
    	decklogBellBookEngTelDAO=null;
    	
    }
	// ********** DECK **********
	@Override
	public List<Decklog> getAllDeck() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Decklog add(Decklog deck) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		deck.setUuid(UUID.randomUUID().toString());		
		Decklog decklog = decklogDAO.create(deck);

		entityTransaction.commit();
		return decklog;
	}

	@Override
	public Decklog getDeck(Long id){
		return decklogDAO.get(id);
	}

	@Override
	public Decklog getDeck(Long idVoyage, Date registrationDate) {
		
		Decklog dlu = decklogDAO.getFinder().and("registrationDate").eq(registrationDate).and("voyage.id").eq(idVoyage).setDistinct().result();
		return dlu;
	}

	@Override
	public Decklog update(Decklog deck) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
			
		Decklog decklog = decklogDAO.update(deck);

		entityTransaction.commit();
		return decklog;
	}

	@Override
	public void delete(Long id) {
		if (id!=null) {
			EntityTransaction entityTransaction = em.getTransaction();
			entityTransaction.begin();
				
			decklogDAO.delete(id);

			entityTransaction.commit();
		}
	}
	
	@Override
	public Finder<Decklog> getFinder() {
		return decklogDAO.getFinder();
	}

	@Override
	public Decklog getDeckByUuid(String uuid) {
		return decklogDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	

	@Override
	public Decklog getDeck(Long idVoyage, Date registrationDate, Long idShip) {
		
		Decklog dlu = decklogDAO.getFinder().and("registrationDate").eq(registrationDate).and("voyage.id").eq(idVoyage).and("ship.id").eq(idShip).setDistinct().result();
		return dlu;
	}
	

	
	
	
	// ********** UMS **********
	@Override
	public List<DecklogUms> getDeckUms(Long idDeck) {
		
		List<DecklogUms> dlu = decklogUmsDAO.getFinder().and("deck.id").eq(idDeck).setDistinct().list();
		return dlu;
	}


	@Override
	public DecklogUms getUms(Long idUms) throws DocumentException{
		return decklogUmsDAO.get(idUms);
		
	}

	@Override
	public void deleteDeckUms(Long id) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
			
		decklogUmsDAO.delete(id);

		entityTransaction.commit();
		
		
	}

	@Override
	public DecklogUms updateUms(DecklogUms ums) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
			
		DecklogUms decklogUms = decklogUmsDAO.update(ums);

		entityTransaction.commit();
		return decklogUms;
	}

	@Override
	public DecklogUms addUms(DecklogUms deckUms) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		deckUms.setUuid(UUID.randomUUID().toString());
		DecklogUms decklogUms =  decklogUmsDAO.create(deckUms);

		entityTransaction.commit();
		return decklogUms;

	}
	
	@Override
	public DecklogUms getUmsByUuid(String uuid) {
		return decklogUmsDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	@Override
	public Finder<DecklogUms> getUmsFinder() {
		return decklogUmsDAO.getFinder();
	}
	
	
	// ********** RADAR **********
	@Override
	public List<DecklogRadar> getDeckRadar(Long idDeck) {
		List<DecklogRadar> dlr = decklogRadarDAO.getFinder().and("deck.id").eq(idDeck).setDistinct().list();
		return dlr;
	}

	@Override
	public DecklogRadar getRadar(Long idRadar) throws DocumentException {
		return decklogRadarDAO.get(idRadar);
	}

	@Override
	public DecklogRadar updateRadar(DecklogRadar radar) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		DecklogRadar decklogRadar =  decklogRadarDAO.update(radar);

		entityTransaction.commit();
		return decklogRadar;
	}

	@Override
	public DecklogRadar addRadar(DecklogRadar deckRadar) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		deckRadar.setUuid(UUID.randomUUID().toString());
		DecklogRadar decklogRadar = decklogRadarDAO.create(deckRadar);

		entityTransaction.commit();
		return decklogRadar;
	}
	
	@Override
	public List<DecklogRadar> getAllRadar() {
		return decklogRadarDAO.getAll();
	}

	@Override
	public DecklogRadar getRadarByUuid(String uuid) {
		return decklogRadarDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	@Override
	public Finder<DecklogRadar> getDecklogRadarFinder() {
		return decklogRadarDAO.getFinder();
	}

	
	
	//********** ROUTE METEO   **********
	@Override
	public List<DecklogRouteMeteo> getDeckRouteMeteo(Long idDeck) {
		List<DecklogRouteMeteo> dlrm = decklogRouteMeteoDAO.getFinder().and("deck.id").eq(idDeck).setDistinct().list();
		return dlrm;
	}

	@Override
	public DecklogRouteMeteo updateRouteMeteo(DecklogRouteMeteo routeMeteo) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		DecklogRouteMeteo decklogRouteMeteo = decklogRouteMeteoDAO.update(routeMeteo);

		entityTransaction.commit();
		return decklogRouteMeteo; 
	}

	@Override
	public DecklogRouteMeteo addRouteMeteo(DecklogRouteMeteo deckRouteMeteo) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		deckRouteMeteo.setUuid(UUID.randomUUID().toString());
		DecklogRouteMeteo decklogRouteMeteo = decklogRouteMeteoDAO.create(deckRouteMeteo);

		entityTransaction.commit();
		return decklogRouteMeteo; 

	}
	

	@Override
	public DecklogRouteMeteo getRouteMeteo(Long idRouteMeteo) throws DocumentException {
		return decklogRouteMeteoDAO.get(idRouteMeteo);
	}
	
	@Override
	public DecklogRouteMeteo getRouteMeteoByUuid(String uuid) {
		return decklogRouteMeteoDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}

	//********** ECDIS   **********
	@Override
	public List<DecklogEcdis> getDeckEcdis(Long idDeck) {
		List<DecklogEcdis> dle = decklogEcdisDAO.getFinder().and("deck.id").eq(idDeck).setDistinct().list();
		return dle;
	}

	@Override
	public DecklogEcdis updateEcdis(DecklogEcdis ecdis) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		DecklogEcdis decklogEcdis = decklogEcdisDAO.update(ecdis);

		entityTransaction.commit();
		return decklogEcdis; 
	}

	@Override
	public DecklogEcdis addEcdis(DecklogEcdis deckEcdis) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		deckEcdis.setUuid(UUID.randomUUID().toString());
		DecklogEcdis decklogEcdis = decklogEcdisDAO.create(deckEcdis);

		entityTransaction.commit();
		return decklogEcdis; 

	}
	
	@Override
	public DecklogEcdis getEcdis(Long idEcdis) throws DocumentException {
		return decklogEcdisDAO.get(idEcdis);
	}
	
	@Override
	public DecklogEcdis getEcdisByUuid(String uuid) {
		return decklogEcdisDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	

	//********** CHRONOMETER   **********
	@Override
	public List<DecklogChrono> getDeckChrono(Long idDeck) {
		List<DecklogChrono> dlch = decklogChronoDAO.getFinder().and("deck.id").eq(idDeck).setDistinct().list();
		return dlch;
	}

	@Override
	public DecklogChrono getChrono(Long idChrono) throws DocumentException {
		return decklogChronoDAO.get(idChrono);
	}

	@Override
	public DecklogChrono updateChrono(DecklogChrono chrono) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		DecklogChrono decklogChrono = decklogChronoDAO.update(chrono);;

		entityTransaction.commit();
		return decklogChrono;  
	}

	@Override
	public DecklogChrono addChrono(DecklogChrono deckChrono) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		deckChrono.setUuid(UUID.randomUUID().toString());
		DecklogChrono decklogChrono = decklogChronoDAO.create(deckChrono);

		entityTransaction.commit();
		return decklogChrono;
	}
	
	@Override
	public DecklogChrono getChronoByUuid(String uuid) {
		return decklogChronoDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	@Override
	public Finder<DecklogChrono> getChronoFinder() {
		return decklogChronoDAO.getFinder();
	}

	//********** POSITION   **********
	@Override
	public List<DecklogShipPosition> getDeckPosition(Long idDeck) {
		List<DecklogShipPosition> dlspos = decklogPositionDAO.getFinder().and("deck.id").eq(idDeck).setDistinct().list();
		return dlspos;
	}

	@Override
	public DecklogShipPosition getPosition(Long idPosition) throws DocumentException {
		return decklogPositionDAO.get(idPosition);
	}

	@Override
	public DecklogShipPosition updatePosition(DecklogShipPosition position) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		DecklogShipPosition decklogShipPosition = decklogPositionDAO.update(position);

		entityTransaction.commit();
		return decklogShipPosition;
	}

	@Override
	public DecklogShipPosition addPosition(DecklogShipPosition deckPosition) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		deckPosition.setUuid(UUID.randomUUID().toString());
		DecklogShipPosition decklogShipPosition = decklogPositionDAO.create(deckPosition);
		
		entityTransaction.commit();
		return decklogShipPosition;
	}

	@Override
	public DecklogShipPosition getPositionByUuid(String uuid) {
		return decklogPositionDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}

	@Override
	public Finder<DecklogShipPosition> getPositionFinder() {
		return decklogPositionDAO.getFinder();
	}
	
	//********** FIREPATROL CHECK   **********
	@Override
	public DecklogFirepatrolCheck getDeckFirepatrolCheck(Long idFierepatrol) throws DocumentException {
		return decklogFirepatrolCheckDAO.get(idFierepatrol);
	}

	@Override
	public DecklogFirepatrolCheck updateFirepatrolCheck(DecklogFirepatrolCheck fpCheck) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		DecklogFirepatrolCheck decklogFirepatrolCheck = decklogFirepatrolCheckDAO.update(fpCheck);
		
		entityTransaction.commit();
		return decklogFirepatrolCheck;
	}

	@Override
	public DecklogFirepatrolCheck addFirepatrolCheck(DecklogFirepatrolCheck fpCheck) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		fpCheck.setUuid(UUID.randomUUID().toString());
		DecklogFirepatrolCheck decklogFirepatrolCheck = decklogFirepatrolCheckDAO.create(fpCheck);
		entityTransaction.commit();
		
		return decklogFirepatrolCheck;
	}

	@Override
	public List<DecklogFirepatrolCheck> getFirepatrolCheckByShip(Long idShip) {
		
		if(idShip == null){
			List<DecklogFirepatrolCheck> dlfpcheckn = decklogFirepatrolCheckDAO.getAll();
			return dlfpcheckn;
		}
		List<DecklogFirepatrolCheck> dlfpcheck = decklogFirepatrolCheckDAO.getFinder().and("ship.id").eq(idShip).setDistinct().list();
		return dlfpcheck;
	}
	
	@Override
	public DecklogFirepatrolCheck getFirepatrolCheckByUuid(String uuid) {
		return decklogFirepatrolCheckDAO.getFinder().and("uuid").eq(uuid).result();
	}

	
	//********** FIREPATROL    check item **********
	@Override
	public DecklogFirepatrolCheckItem getFirepatrolCheckItemByUuid(String uuid) {
		return decklogFirepatrolCheckItemDAO.getFinder().and("uuid").eq(uuid).result();
	}

	

	//********** FIREPATROL    **********
	@Override
	public List<DecklogFirepatrol> getDeckFirepatrol(Long idDeck) {
		List<DecklogFirepatrol> dlfirep = decklogFirepatrolDAO.getFinder().and("deck.id").eq(idDeck).setDistinct().list();
		return dlfirep;
	}

	@Override
	public DecklogFirepatrol getFirepatrol(Long idFierepatrol) throws DocumentException {
		return decklogFirepatrolDAO.get(idFierepatrol);
	}

	@Override
	public DecklogFirepatrol updateFirepatrol(DecklogFirepatrol fp) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		DecklogFirepatrol decklogFirepatrol = decklogFirepatrolDAO.update(fp);
		entityTransaction.commit();
		
		return decklogFirepatrol;
	}

	@Override
	public DecklogFirepatrol addFirepatrol(DecklogFirepatrol fp) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		fp.setUuid(UUID.randomUUID().toString());
		DecklogFirepatrol decklogFirepatrol = decklogFirepatrolDAO.create(fp);
		
		entityTransaction.commit();
		
		return decklogFirepatrol;
	}
	
	@Override
	public DecklogFirepatrol getFirepatrolByUuid(String uuid) {
		DecklogFirepatrol aa = decklogFirepatrolDAO.getFinder().and("uuid").eq(uuid).result();
		if (aa!=null && aa.getCheckItem()!=null) aa.getCheckItem().size();
		return aa;
	}


	//********** COMPASS    **********
	@Override
	public List<DecklogCompass> getDeckCompass(Long idDeck) {
		List<DecklogCompass> dlcompass = decklogCompassDAO.getFinder().and("deck.id").eq(idDeck).setDistinct().list();
		return dlcompass;
	}

	@Override
	public DecklogCompass getCompass(Long idCompass) throws DocumentException {
		return decklogCompassDAO.get(idCompass);
	}
	
	@Override
	public DecklogCompass updateCompass(DecklogCompass compass) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		DecklogCompass decklogCompass = decklogCompassDAO.update(compass);
		
		entityTransaction.commit();
		
		return decklogCompass;
	}

	@Override
	public DecklogCompass addCompass(DecklogCompass deckCompass) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		deckCompass.setUuid(UUID.randomUUID().toString());
		DecklogCompass decklogCompass = decklogCompassDAO.create(deckCompass);
		
		entityTransaction.commit();
		
		return decklogCompass;
	}

	@Override
	public DecklogCompass getCompassByUuid(String uuid) {
		return decklogCompassDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}

	@Override
	public Finder<DecklogCompass> getCompassFinder() {
		return decklogCompassDAO.getFinder();
	}

	//********** Master Order    **********
	@Override
	public List<MasterNightOrder> getAllMasterOrder() {
		return masterNightOrderDAO.getAll();
	}

	@Override
	public MasterNightOrder getMasterOrder(Long idMasterOrder) throws DocumentException {
		return masterNightOrderDAO.get(idMasterOrder);
	}

	@Override
	public MasterNightOrder updateMasterOrder(MasterNightOrder masterOrder) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		MasterNightOrder masterNightOrder = masterNightOrderDAO.update(masterOrder);
		
		entityTransaction.commit();		
		return masterNightOrder;
	}

	@Override
	public MasterNightOrder addMasterOrder(MasterNightOrder order) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		order.setUuid(UUID.randomUUID().toString());
		order.setActive(false);
		MasterNightOrder masterNightOrder = masterNightOrderDAO.create(order);
		
		entityTransaction.commit();		
		return masterNightOrder;

	}

	@Override
	public Finder<MasterNightOrder> getMasterNightOrderFinder() {
		return masterNightOrderDAO.getFinder();
	}

	@Override
	public MasterNightOrder getActiveOrder() {
		return masterNightOrderDAO.getFinder().and("active").eq(true).result();
	}

	@Override
	public MasterNightOrder getMasterNightOrderByUuid(String uuid) {
		return masterNightOrderDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	//********** Permanent Order    **********
	
	@Override
	public List<PermanentNightOrder> getAllPermanentOrder() {
		return permanentOrderDAO.getAll();
	}

	@Override
	public PermanentNightOrder getPermanentOrder(Long idPermanentOrder) throws DocumentException {
		return permanentOrderDAO.get(idPermanentOrder);
	}

	@Override
	public PermanentNightOrder updatePermanentOrder(PermanentNightOrder permanentOrder) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		PermanentNightOrder permanentNightOrder =permanentOrderDAO.update(permanentOrder);
		
		entityTransaction.commit();		
		return permanentNightOrder;
	}

	@Override
	public PermanentNightOrder addPermanentOrder(PermanentNightOrder permanentOrder) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		permanentOrder.setUuid(UUID.randomUUID().toString());
		permanentOrder.setActive(false);
		PermanentNightOrder permanentNightOrder = permanentOrderDAO.create(permanentOrder);
		
		entityTransaction.commit();		
		return permanentNightOrder;
	}

	@Override
	public Finder<PermanentNightOrder> getPermanentOrderFinder() {
		return permanentOrderDAO.getFinder();
	}

	@Override
	public PermanentNightOrder getActivePermanentOrder() {
		return permanentOrderDAO.getFinder().and("active").eq(true).result();
	}
	
	@Override
	public PermanentNightOrder getPermanentNightOrderByUuid(String uuid) {
		return permanentOrderDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}

	//********** Refrigerant    **********
	@Override
	public List<Refrigerant> getAllRefrigerant() {
		return refrigerantDAO.getAll();
	}

	@Override
	public Refrigerant getRefrigerant(Long idRefrigerant) throws DocumentException {
		return refrigerantDAO.get(idRefrigerant);
	}

	@Override
	public Refrigerant updateRefrigerant(Refrigerant refrigerant) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		Refrigerant refrigerantUpdated = refrigerantDAO.update(refrigerant);
		
		entityTransaction.commit();		
		return refrigerantUpdated;
	}

	@Override
	public Refrigerant addRefrigerant(Refrigerant refrigerant) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		refrigerant.setUuid(UUID.randomUUID().toString());
		Refrigerant refrigerantUpdated = refrigerantDAO.create(refrigerant);
		
		entityTransaction.commit();		
		return refrigerantUpdated;

	}

	@Override
	public Finder<Refrigerant> getRefrigerantFinder() {
		return refrigerantDAO.getFinder();
	}	
	
	@Override
	public Refrigerant getRefrigerantByUuid(String uuid) {
		return refrigerantDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	

	
	
	//********** BELL BOOK    **********
	@Override
	public List<DecklogBellBook> getDeckBellBook(Long idDeck) {
		List<DecklogBellBook> dlbellBook = decklogBellBookDAO.getFinder().and("deck.id").eq(idDeck).setDistinct().list();
		return dlbellBook;
	}

	@Override
	public DecklogBellBook getBellBook(Long idBellBook) throws DocumentException {
		return decklogBellBookDAO.get(idBellBook);
	}

	@Override
	public DecklogBellBook updateBellBook(DecklogBellBook bellBook) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		DecklogBellBook decklogBellBook = decklogBellBookDAO.update(bellBook);
		
		entityTransaction.commit();		
		return decklogBellBook;
	}

	@Override
	public DecklogBellBook addBellBook(DecklogBellBook deckBellBook) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		deckBellBook.setUuid(UUID.randomUUID().toString());
		DecklogBellBook decklogBellBookCreated = decklogBellBookDAO.create(deckBellBook);
		
		entityTransaction.commit();		
		return decklogBellBookCreated;
	}

	@Override
	public DecklogBellBook getBellBookByUuid(String uuid) {
//		return decklogBellBookDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
		
		DecklogBellBook aa = decklogBellBookDAO.getFinder().and("uuid").eq(uuid).result(); 
		if (aa!=null && aa.getBellBookEngTelegraph()!=null) aa.getBellBookEngTelegraph().size();
		return aa;		
		
	}

	
	
	//********** BELL BOOK  ENG TELEGRAPH  **********
	@Override
	public List<DecklogBellBookEngTelegraph> getDeckBellBookEngTel(Long idBellBook) {
		List<DecklogBellBookEngTelegraph> dlbellBookET = decklogBellBookEngTelDAO.getFinder().and("bellBook.id").eq(idBellBook).setDistinct().list();
		return dlbellBookET;
	}

	@Override
	public DecklogBellBookEngTelegraph getBellBookEngTel(Long idBellBookEngTel) throws DocumentException {
		return decklogBellBookEngTelDAO.get(idBellBookEngTel);
	}

	@Override
	public DecklogBellBookEngTelegraph updateBellBookEngTel(DecklogBellBookEngTelegraph bellBookEngTel) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		DecklogBellBookEngTelegraph decklogBellBookEngTelegraph = decklogBellBookEngTelDAO.update(bellBookEngTel);
		
		entityTransaction.commit();		
		return decklogBellBookEngTelegraph;
	}

	@Override
	public DecklogBellBookEngTelegraph addBellBookEngTel(DecklogBellBookEngTelegraph deckBellBookEngTel) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		deckBellBookEngTel.setUuid(UUID.randomUUID().toString());
		DecklogBellBookEngTelegraph decklogBellBookEngTelegraph = decklogBellBookEngTelDAO.create(deckBellBookEngTel);
		
		entityTransaction.commit();		
		return decklogBellBookEngTelegraph;
	}

	@Override
	public DecklogBellBookEngTelegraph getBellBookEngTelByUuid(String uuid) {
		return decklogBellBookEngTelDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}


	

}
