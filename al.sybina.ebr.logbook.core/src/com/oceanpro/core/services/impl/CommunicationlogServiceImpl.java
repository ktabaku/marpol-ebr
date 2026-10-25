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
import com.oceanpro.core.dao.oceanManager.impl.logbook.GmdssAnnexDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.GmdssSecBDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.GmdssSecCDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.OzoneADAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.OzoneBDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.VhfDAOImpl;
import com.oceanpro.core.dao.oceanManager.impl.logbook.VisitorDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.GmdssAnnexDAO;
import com.oceanpro.core.dao.oceanManager.logbook.GmdssSecBDAO;
import com.oceanpro.core.dao.oceanManager.logbook.GmdssSecCDAO;
import com.oceanpro.core.dao.oceanManager.logbook.VhfDAO;
import com.oceanpro.core.entity.oceanManager.logbook.GmdssAnnex;
import com.oceanpro.core.entity.oceanManager.logbook.GmdssSecB;
import com.oceanpro.core.entity.oceanManager.logbook.GmdssSecC;
import com.oceanpro.core.entity.oceanManager.logbook.Vhf;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.OzoneA;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.OzoneB;
import com.oceanpro.core.exception.DocumentException;
import com.oceanpro.core.services.CommunicationlogService;
import com.widee.base.hibernate.criteria.Finder;

import al.sybina.ebr.commons.utils.EnvironmentalEntityManagerFactory;
import al.sybina.ebr.logbook.core.Activator;
import al.sybina.ebr.core.tools.PluginTools;

@Creatable("CommunicationlogService")
@Transactional
public class CommunicationlogServiceImpl implements CommunicationlogService{


//	@Autowired
	private VhfDAO vhfDAO;
//	@Autowired
	private GmdssSecBDAO gmdssbDAO;
//	@Autowired
	private GmdssSecCDAO gmdsscDAO;
//	@Autowired
	private GmdssAnnexDAO gmdssAnnexDAO;
	
	private EntityManagerFactory emf;
    private EntityManager em;

	protected void activate() {
		em = Activator.getEntityManager();

		vhfDAO = new VhfDAOImpl();
		vhfDAO.setEntityManager(em);

		gmdssbDAO = new GmdssSecBDAOImpl();
		gmdssbDAO.setEntityManager(em);

		gmdsscDAO = new GmdssSecCDAOImpl();
		gmdsscDAO.setEntityManager(em);

		gmdssAnnexDAO = new GmdssAnnexDAOImpl();
		gmdssAnnexDAO.setEntityManager(em);
	}

    protected void deactivate()
    {
    	vhfDAO=null;
    	gmdssbDAO=null;
    	gmdsscDAO=null;
    	gmdssAnnexDAO=null;
    	
    }

	// ********** VHF **********
	@Override
	public List<Vhf> getAllVhf() {
		return vhfDAO.getAll();
	}
	
	@Override
	public Vhf getVhf(Long idVhf) throws DocumentException {
		return vhfDAO.get(idVhf);
	}
	
	@Override
	public Vhf updateVhf(Vhf vhf) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		Vhf vhfUpdated = vhfDAO.update(vhf);

		entityTransaction.commit();
		return vhfUpdated;
	}
	
	@Override
	public Vhf addVhf(Vhf vhf) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		vhf.setUuid(UUID.randomUUID().toString());
		Vhf vhfAdded = vhfDAO.create(vhf);

		entityTransaction.commit();
		return vhfAdded;
	}
	
	@Override
	public Vhf getVhfByUuid(String uuid) {
		return vhfDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	@Override
	public Finder<Vhf> getVhfFinder() {
		return vhfDAO.getFinder();
	}	
	
	//********** GMDSS SEC B  **********
	@Override
	public List<GmdssSecB> getAllGmdssB() {
		return gmdssbDAO.getAll();
	}

	@Override
	public GmdssSecB getGmdssb(Long idGmdssb) throws DocumentException {
		return gmdssbDAO.get(idGmdssb);
	}

	@Override
	public GmdssSecB updateGmdssb(GmdssSecB gmdssb) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		GmdssSecB gmdssSecB = gmdssbDAO.update(gmdssb);

		entityTransaction.commit();
		return gmdssSecB; 
	}

	@Override
	public GmdssSecB addGmdssb(GmdssSecB gmdssb) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		gmdssb.setUuid(UUID.randomUUID().toString());
		GmdssSecB gmdssSecB = gmdssbDAO.create(gmdssb);

		entityTransaction.commit();
		return gmdssSecB; 
		
	}

	@Override
	public GmdssSecB getGmdssSecBByUuid(String uuid) {
		return gmdssbDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	@Override
	public Finder<GmdssSecB> getGmdssSecBFinder() {
		return gmdssbDAO.getFinder();
	}	
	
	//********** GMDSS SEC C  **********
	@Override
	public List<GmdssSecC> getAllGmdssC() {
		return gmdsscDAO.getAll();
	}

	@Override
	public GmdssSecC getGmdssc(Long idGmdssc) throws DocumentException {
		return gmdsscDAO.get(idGmdssc);
	}

	@Override
	public GmdssSecC updateGmdssc(GmdssSecC gmdssc) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		GmdssSecC gmdssSecC = gmdsscDAO.update(gmdssc);

		entityTransaction.commit();
		return gmdssSecC;
	}

	@Override
	public GmdssSecC addGmdssc(GmdssSecC gmdssc) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		gmdssc.setUuid(UUID.randomUUID().toString());
		GmdssSecC gmdssSecC = gmdsscDAO.create(gmdssc);

		entityTransaction.commit();
		return gmdssSecC;
		
		
	}
	
	@Override
	public GmdssSecC getGmdssSecCByUuid(String uuid) {
		return gmdsscDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}
	
	@Override
	public Finder<GmdssSecC> getGmdssSecCFinder() {
		return gmdsscDAO.getFinder();
	}	
	

	//********** GMDSS ANNEX  **********
	@Override
	public List<GmdssAnnex> getAllGmdssAnnex() {
		return gmdssAnnexDAO.getAll();
	}

	@Override
	public GmdssAnnex getGmdssAnnex(Long idGmdssAnnex) throws DocumentException {
		return gmdssAnnexDAO.get(idGmdssAnnex);
	}

	@Override
	public GmdssAnnex updateGmdssAnnex(GmdssAnnex gmdssAnnex) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		GmdssAnnex gmdssAnnexUpdated = gmdssAnnexDAO.update(gmdssAnnex);

		entityTransaction.commit();
		return gmdssAnnexUpdated;
	}

	@Override
	public GmdssAnnex addGmdssAnnex(GmdssAnnex gmdssAnnex) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		gmdssAnnex.setUuid(UUID.randomUUID().toString());
		GmdssAnnex gmdssAnnexCreated =  gmdssAnnexDAO.create(gmdssAnnex);

		entityTransaction.commit();
		return gmdssAnnexCreated;

	}
	
	@Override
	public GmdssAnnex getGmdssAnnexByUuid(String uuid) {
		return gmdssAnnexDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}

	@Override
	public Finder<GmdssAnnex> getGmdssAnnexFinder() {
		return gmdssAnnexDAO.getFinder();
	}	


}
