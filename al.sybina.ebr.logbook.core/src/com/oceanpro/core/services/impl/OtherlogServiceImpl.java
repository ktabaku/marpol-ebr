package com.oceanpro.core.services.impl;

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

import com.oceanpro.core.dao.oceanManager.impl.logbook.AlcoholTestDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.DrugTestDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.OzoneADAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.OzoneBDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.VisitorDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.AlcoholTestDAO;
import com.oceanpro.core.dao.oceanManager.logbook.DrugTestDAO;
import com.oceanpro.core.dao.oceanManager.logbook.OzoneADAO;
import com.oceanpro.core.dao.oceanManager.logbook.OzoneBDAO;
import com.oceanpro.core.dao.oceanManager.logbook.VisitorDAO;
import com.oceanpro.core.entity.oceanManager.logbook.Bunker;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.AlcoholTest;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.DrugTest;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.OzoneA;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.OzoneB;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.Visitor;
import com.oceanpro.core.exception.DocumentException;
import com.oceanpro.core.services.OtherlogService;
import com.widee.base.hibernate.criteria.Finder;

import al.sybina.ebr.commons.utils.EnvironmentalEntityManagerFactory;
import al.sybina.ebr.logbook.core.Activator;
import al.sybina.ebr.core.tools.PluginTools;

@Creatable("OtherlogService")
@Transactional
public class OtherlogServiceImpl implements OtherlogService{

	
//	@Autowired
	private VisitorDAO visitorDAO;
	
//	@Autowired
	private AlcoholTestDAO alcoholTestDAO;

//	@Autowired
	private DrugTestDAO drugTestDAO;
	
//	@Autowired
	private OzoneADAO ozoneADAO;
	
//	@Autowired
	private OzoneBDAO ozoneBDAO;

	
	private EntityManagerFactory emf;
    private EntityManager em;

	protected void activate() {
		em = Activator.getEntityManager();

		visitorDAO = new VisitorDAOImpl();
		visitorDAO.setEntityManager(em);

		alcoholTestDAO = new AlcoholTestDAOImpl();
		alcoholTestDAO.setEntityManager(em);

		drugTestDAO = new DrugTestDAOImpl();
		drugTestDAO.setEntityManager(em);

		ozoneADAO = new OzoneADAOImpl();
		ozoneADAO.setEntityManager(em);

		ozoneBDAO = new OzoneBDAOImpl();
		ozoneBDAO.setEntityManager(em);
	}

    protected void deactivate()
    {
    	visitorDAO=null;
    	alcoholTestDAO=null;
    	drugTestDAO=null;
    	ozoneADAO=null;
    	ozoneBDAO=null;

    }
    
    
	//VISITOR
	@Override
	public List<Visitor> getAllVisitor() {
		return visitorDAO.getAll();
	}

	@Override
	public Visitor getVisitor(Long idVisitor) throws DocumentException {
		return visitorDAO.get(idVisitor);
	}

	@Override
	public Visitor updateVisitor(Visitor visitor) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		Visitor visitorUpdated = visitorDAO.update(visitor);
		
		entityTransaction.commit();
		return visitorUpdated; 
	}

	@Override
	public Visitor addVisitor(Visitor visitor) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		visitor.setUuid(UUID.randomUUID().toString());
		Visitor visitorAdded = visitorDAO.create(visitor);
		
		entityTransaction.commit();
		return visitorAdded; 
	}

	@Override
	public Finder<Visitor> getVisitorFinder() {
		return visitorDAO.getFinder();
	}	

	
	@Override
	public Visitor getVisitorByUuid(String uuid) {
		return visitorDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}

	
	//ALCOHOL TEST
	@Override
	public List<AlcoholTest> getAllAlcoholTest() {
		return alcoholTestDAO.getAll();
	}

	@Override
	public AlcoholTest getAlcoholTest(Long idAlcoholTest) throws DocumentException {
		return alcoholTestDAO.get(idAlcoholTest);
	}

	@Override
	public AlcoholTest updateAlcoholTest(AlcoholTest alcoholTest) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		AlcoholTest alcoholTestUpdated = alcoholTestDAO.update(alcoholTest);
		
		entityTransaction.commit();
		return alcoholTestUpdated;
	}

	@Override
	public AlcoholTest addAlcoholTest(AlcoholTest alcoholTest) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		alcoholTest.setUuid(UUID.randomUUID().toString());
		AlcoholTest alcoholTestAdded = alcoholTestDAO.create(alcoholTest);
		
		entityTransaction.commit();
		return alcoholTestAdded;
	}

	@Override
	public Finder<AlcoholTest> getAlcoholTestFinder() {
		return alcoholTestDAO.getFinder();
	}	

	@Override
	public AlcoholTest getAlcoholTestByUuid(String uuid) {
		return alcoholTestDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	//OZONE A
	@Override
	public List<OzoneA> getAllOzoneA() {
		return ozoneADAO.getAll();
	}

	@Override
	public OzoneA getOzoneA(Long idOzoneA) throws DocumentException {
		return ozoneADAO.get(idOzoneA);
	}

	@Override
	public OzoneA updateOzoneA(OzoneA ozoneA) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		OzoneA ozoneAUpdated = ozoneADAO.update(ozoneA);
		
		entityTransaction.commit();
		return ozoneAUpdated; 
	}

	@Override
	public OzoneA addOzoneA(OzoneA ozoneA) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		ozoneA.setUuid(UUID.randomUUID().toString());
		OzoneA ozoneAAdded = ozoneADAO.create(ozoneA);
		
		entityTransaction.commit();
		return ozoneAAdded; 
	}

	@Override
	public Finder<OzoneA> getOzoneAFinder() {
		return ozoneADAO.getFinder();
	}	
	
	@Override
	public OzoneA getOzoneAByUuid(String uuid) {
		return ozoneADAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	//OZONE B
	@Override
	public List<OzoneB> getAllOzoneB() {
		return ozoneBDAO.getAll();
	}

	@Override
	public OzoneB getOzoneB(Long idOzoneB) throws DocumentException {
		return ozoneBDAO.get(idOzoneB);
	}

	@Override
	public OzoneB updateOzoneB(OzoneB ozoneB) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		OzoneB ozoneBUpdated = ozoneBDAO.update(ozoneB);
		
		entityTransaction.commit();
		return ozoneBUpdated;
	}

	@Override
	public OzoneB addOzoneB(OzoneB ozoneB) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		ozoneB.setUuid(UUID.randomUUID().toString());
		OzoneB ozoneBAdded = ozoneBDAO.create(ozoneB);
		
		entityTransaction.commit();
		return ozoneBAdded;
	}

	@Override
	public Finder<OzoneB> getOzoneBFinder() {
		return ozoneBDAO.getFinder();
	}	
	
	@Override
	public OzoneB getOzoneBByUuid(String uuid) {
		return ozoneBDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	//DRUG TEST
	@Override
	public List<DrugTest> getAllDrugTest() {
		return drugTestDAO.getAll();
	}

	@Override
	public DrugTest getDrugTest(Long idDrugTest) throws DocumentException {
		return drugTestDAO.get(idDrugTest);
	}

	@Override
	public DrugTest updateDrugTest(DrugTest drugTest) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		DrugTest drugTestUpdated = drugTestDAO.update(drugTest);
		
		entityTransaction.commit();
		return drugTestUpdated;
	}

	@Override
	public DrugTest addDrugTest(DrugTest drugTest) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		drugTest.setUuid(UUID.randomUUID().toString());
		DrugTest drugTestAdded = drugTestDAO.create(drugTest);
		
		entityTransaction.commit();
		return drugTestAdded;
	}

	@Override
	public Finder<DrugTest> getDrugTestFinder() {
		return drugTestDAO.getFinder();
	}	
	
	@Override
	public DrugTest getDrugTestByUuid(String uuid) {
		return drugTestDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	
}
