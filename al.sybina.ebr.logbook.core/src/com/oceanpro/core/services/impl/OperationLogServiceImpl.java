package com.oceanpro.core.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;

import com.oceanpro.core.dao.oceanManager.logbook.operation.GarbageCategoryDAO;
import com.oceanpro.core.dao.oceanManager.logbook.operation.GarbageLogDAO;
import com.oceanpro.core.dao.oceanManager.logbook.operation.MarpleSettingsDAO;
import com.oceanpro.core.dao.oceanManager.logbook.operation.Oil1LogDAO;
import com.oceanpro.core.dao.oceanManager.logbook.operation.TankDAO;
import com.oceanpro.core.dao.oceanManager.logbook.operation.impl.GarbageCategoryDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.operation.impl.GarbageLogDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.operation.impl.MarpleSettingsDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.operation.impl.Oil1LogDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.operation.impl.TankDAOImpl;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.GarbageCategory;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.GarbageLog;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.MarpleLogSettings;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.OilLog;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.oceanpro.core.security.entities.User;
import com.oceanpro.core.services.OperationLogService;
import com.widee.base.hibernate.criteria.Finder;

import al.sybina.ebr.commons.utils.AuditLogger;
import al.sybina.ebr.logbook.core.Activator;

/**
 * @author Gio
 *
 */
@Creatable("OperationLogService")
@Transactional
public class OperationLogServiceImpl implements OperationLogService {


	private static final Logger auditLogger = LoggerCreator.createLogger(AuditLogger.class);
	
//	@Autowired
	MarpleSettingsDAO marpleSettingsDAO;
//	@Autowired
	GarbageLogDAO garbageLogDAO;
//	@Autowired
	GarbageCategoryDAO garbageCategoryDAO;
//	@Autowired
	Oil1LogDAO oil1LogDAO;

	TankDAO tankDAO;

	private EntityManagerFactory emf;
    private EntityManager em;

	protected void activate() {
		em = Activator.getEntityManager();
		
		marpleSettingsDAO = new MarpleSettingsDAOImpl();
		marpleSettingsDAO.setEntityManager(em);

		garbageLogDAO = new GarbageLogDAOImpl();
		garbageLogDAO.setEntityManager(em);

		garbageCategoryDAO = new GarbageCategoryDAOImpl();
		garbageCategoryDAO.setEntityManager(em);

		oil1LogDAO = new Oil1LogDAOImpl();
		oil1LogDAO.setEntityManager(em);
		
		tankDAO = new TankDAOImpl();
		tankDAO.setEntityManager(em);
	}

    protected void deactivate()
    {
    	marpleSettingsDAO=null;
    	garbageLogDAO=null;
    	garbageCategoryDAO=null;
    	oil1LogDAO=null;
    	tankDAO=null;
    }
	
	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#getAll()
	 */
	@Override
	public List<MarpleLogSettings> getAll() {
		List<MarpleLogSettings> l = marpleSettingsDAO.getAllCacheStoreRefresh();
		tankDAO.getAllCacheStoreRefresh();
		if (l!=null) {
			for (MarpleLogSettings marpleLogSettings : l) {
				loadLazyProperties(marpleLogSettings);
			}
		}
		return l;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#update(com.oceanpro.core.entity.oceanManager.logbook.operationLog.MarpleLogSettings)
	 */
	@Override
	public MarpleLogSettings update(MarpleLogSettings settings, Collection<String> addedTankNames, Collection<String> updatedTankNames, Collection<String> deletedTankNames) {
		if (settings==null) return null;
		MarpleLogSettings marpleLogSettings;
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		if (settings.getId()==null) {
			settings.setUuid(UUID.randomUUID().toString());
			marpleLogSettings = loadLazyProperties(marpleSettingsDAO.create(settings));
		}
		else marpleLogSettings = loadLazyProperties(marpleSettingsDAO.update(settings));

		entityTransaction.commit(); 
		auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " updated marpol details.\n"
				+ "Added tanks: " + String.join(", ", addedTankNames) + "\n"
				+ "Updated tanks: " + String.join(", ", updatedTankNames) + "\n"
				+ "Deleted tanks: " + String.join(", ", deletedTankNames));
		return marpleLogSettings;
	}
	
	
	private MarpleLogSettings loadLazyProperties(MarpleLogSettings mls) {
		Comparator<Tank> sortByIdComparator = new Comparator<Tank>() {
			@Override
			public int compare(Tank o1, Tank o2) {
				return o1.getId().compareTo(o2.getId());
			}
		};
			
		if (mls.getCargoTanks()!=null) {
			for (Tank t : mls.getCargoTanks()) {
				t.toString();
			}
			mls.getCargoTanks().sort(sortByIdComparator);
		}
		if (mls.getFuelOilTanks()!=null) {
			for (Tank t : mls.getFuelOilTanks()) {
				t.toString();
			}
			mls.getFuelOilTanks().sort(sortByIdComparator);
		}
		if (mls.getMgoTanks()!=null) {
			for (Tank t : mls.getMgoTanks()) {
				t.toString();
			}
			mls.getMgoTanks().sort(sortByIdComparator);
		}
		if (mls.getOilyBilgeTanks()!=null) {
			for (Tank t : mls.getOilyBilgeTanks()) {
				t.toString();
			}
			mls.getOilyBilgeTanks().sort(sortByIdComparator);
		}
		if (mls.getSlopTanks()!=null) {
			for (Tank t : mls.getSlopTanks()) {
				t.toString();
			}
			mls.getSlopTanks().sort(sortByIdComparator);
		}
		if (mls.getSludgeTanks()!=null) {
			for (Tank t : mls.getSludgeTanks()) {
				t.toString();
			}
			mls.getSludgeTanks().sort(sortByIdComparator);
		}
		if (mls.getBallastTanks()!=null) {
			for (Tank t : mls.getBallastTanks()) {
				t.toString();
			}
			mls.getBallastTanks().sort(sortByIdComparator);
		}
		return mls;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#getAllGarbageCategories()
	 */
	@Override
	public List<GarbageCategory> getAllGarbageCategories() {
		return garbageCategoryDAO.getFinder().addOrderAsc("name").setDistinct().list();
//		return garbageCategoryDAO.getAll();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#getGarbageCategory(java.lang.Long)
	 */
	@Override
	public GarbageCategory getGarbageCategory(Long id) {
		return garbageCategoryDAO.get(id);
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#getGarbageCategoryFinder()
	 */
	@Override
	public Finder<GarbageCategory> getGarbageCategoryFinder() {
		return garbageCategoryDAO.getFinder();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#getAllGarbageLog()
	 */
	@Override
	public List<GarbageLog> getAllGarbageLog() {
		return garbageLogDAO.getAll();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#addGarbageLog(com.oceanpro.core.entity.oceanManager.logbook.operationLog.GarbageLog)
	 */
	@Override
	public GarbageLog addGarbageLog(GarbageLog log) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		log.setUuid(UUID.randomUUID().toString());
		GarbageLog garbageLog = garbageLogDAO.create(log);
		entityTransaction.commit(); 
		
		return garbageLog;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#updateGarbageLog(com.oceanpro.core.entity.oceanManager.logbook.operationLog.GarbageLog)
	 */
	@Override
	public GarbageLog updateGarbageLog(GarbageLog log) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		GarbageLog garbageLog = garbageLogDAO.update(log);

		entityTransaction.commit(); 
		
		return garbageLog;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#deleteGarbageLog(long)
	 */
	@Override
	public void deleteGarbageLog(long logId) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		garbageLogDAO.delete(logId);

		entityTransaction.commit(); 
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#getGarbageLogFinder()
	 */
	@Override
	public Finder<GarbageLog> getGarbageLogFinder() {
		return garbageLogDAO.getFinder();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#getGarbageLogByUuid(java.lang.String)
	 */
	@Override
	public GarbageLog getGarbageLogByUuid(String uuid) {
		return garbageLogDAO.getFinder().and("uuid").eq(uuid).result();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#getAllOil1Log()
	 */
	@Override
	public List<OilLog> getAllOil1Log() {
		return oil1LogDAO.getFinder().addOrderDesc("date").addOrderDesc("groupUuid").addOrderAsc("orderInGroup").setDistinct().list();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#addOil1Log(com.oceanpro.core.entity.oceanManager.logbook.operationLog.OilLog)
	 */
	@Override
	public OilLog addOil1Log(OilLog log) {
		if (log!=null && log.getUuid()==null) log.setUuid(UUID.randomUUID().toString());
		em.detach(log); log.setId(null);
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
//		log.setInsertDate(new Date());
		OilLog oilLog = oil1LogDAO.create(log);

		entityTransaction.commit();
		auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " added oil log " + oilLog.toString());
		return oilLog;
	}

	@Override
	public void moveFromDraftToOil1Log(List<OilLog> logList) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		for(OilLog log : logList) {
//			log.setInsertDate(new Date());
			long currentId = log.getId();
			log.setId(null);
			oil1LogDAO.create(log);
//			draftOil1LogDAO.delete(currentId);
		}
		entityTransaction.commit();
	}
	
	@Override
	public void addDraftOil1Log(OilLog log) {
		if (log!=null && log.getUuid()==null) log.setUuid(UUID.randomUUID().toString());
		em.detach(log); log.setId(null);
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
//		log.setInsertDate(new Date());
//		DraftOilLog oilLog = draftOil1LogDAO.create(DraftOilLog.getInstanceFrom(log));

		entityTransaction.commit(); 
//		return oilLog;
	}
	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#updateOil1Log(com.oceanpro.core.entity.oceanManager.logbook.operationLog.OilLog)
	 */
	@Override
	public OilLog updateOil1Log(OilLog log) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		OilLog oilLog = oil1LogDAO.update(log);

		entityTransaction.commit();
		auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " updated oil log " + oilLog.toString());
		return oilLog;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#deleteOil1Log(long)
	 */
	@Override
	public void deleteOil1Log(long logId) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		oil1LogDAO.delete(logId);

		entityTransaction.commit();
		auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " deleted oil log id: " + logId);
	}
	@Override
	public void deleteOil1LogGroupByUuid(String groupUUID) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		oil1LogDAO.deleteGroupByUuid(groupUUID);

		entityTransaction.commit();
		auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " deleted oil log groupUUID: " + groupUUID);
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#getOil1LogFinder()
	 */
	@Override
	public Finder<OilLog> getOil1LogFinder() {
		return oil1LogDAO.getFinder();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.OperationLogService#getOil1LogByUuid(java.lang.String)
	 */
	@Override
	public OilLog getOil1LogByUuid(String uuid) {
		return oil1LogDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}

	@Override
	public List<MarpleLogSettings> getAllMarpleLogSettingsByShip(Long shipId) {
		
		List<MarpleLogSettings> l = marpleSettingsDAO.getFinder().and("ship.id").eq(shipId).setDistinct().list();
		if (l!=null) {
			for (MarpleLogSettings marpleLogSettings : l) {
				loadLazyProperties(marpleLogSettings);
			}
		}
		return l;
		
	}

	@Override
	public Map<String, List<OilLog>> getAllOil1LogList() {
		return oil1LogDAO.getAllOil1LogByGroup();
	}
	@Override
	public Map<String, List<OilLog>> getOil1LogList(int fromIndex, int toIndex) {
		return oil1LogDAO.getOil1LogBySignatureGroup(fromIndex, toIndex);
	}
	@Override
	public int getAllOil1LogListSize() {
		return oil1LogDAO.getAllOil1LogByGroupSize();
	}
	@Override
	public Map<String, List<OilLog>> getSignatureGroupsOil1LogList(List<String> signatureGroupUUIDList) {
		return oil1LogDAO.getOil1LogBySignatureGroup(signatureGroupUUIDList);
	}
	@Override
	public int getAllOil1LogSignatureGroupsSize() {
		return oil1LogDAO.getAllOil1LogBySignatureGroupSize();
	}
	@Override
	public Map<String, Map<String, List<OilLog>>> getSignatureGroupsOil1LogMaps(List<String> signatureGroupUUIDList) {
		Map<String, Map<String, List<OilLog>>> result = new HashMap<String, Map<String, List<OilLog>>>();
		Map<String, List<OilLog>> signatureGroupsOil1LogLists = oil1LogDAO.getOil1LogBySignatureGroup(signatureGroupUUIDList);
		for(String signatureUUID : signatureGroupsOil1LogLists.keySet()) {
			List<OilLog> oilLogList = signatureGroupsOil1LogLists.get(signatureUUID);
			Map<String, List<OilLog>> groupsOil1LogLists = new HashMap<String, List<OilLog>>();
			for(OilLog oilLog : oilLogList) {
				List<OilLog> list = groupsOil1LogLists.get(oilLog.getGroupUuid());
				if(list==null) {
					List<OilLog> list2 = new ArrayList<OilLog>();
					list2.add(oilLog);
					groupsOil1LogLists.put(oilLog.getGroupUuid(), list2);
				} else {
					list.add(oilLog);
				}
			}
			result.put(signatureUUID, groupsOil1LogLists);
		}
		return result;
	}
	@Override
	public Map<String, List<OilLog>> getGroupsOil1LogList(List<String> groupUUIDList) {
		return oil1LogDAO.getOil1LogByGroup(groupUUIDList);
	}
	@Override
	public List<OilLog> getOil1LogListByGroupUUID(String groupUUID) {
		List<String> groupUUIDList = new ArrayList<String>();
		groupUUIDList.add(groupUUID);
		List<OilLog> oilLogList = oil1LogDAO.getOil1LogByGroup(groupUUIDList).get(groupUUID);
		for(OilLog oilLog : oilLogList) em.detach(oilLog);
		return oilLogList;
	}
	
	@Override
	public Map<String, List<OilLog>> getAllDraftOil1LogList() {
//		Map<String, List<DraftOilLog>> draftOilLogList = draftOil1LogDAO.getAllOil1LogByGroup();
//		Map<String, List<OilLog>> oilLogList = new HashMap<String, List<OilLog>>();
//		for(String key : draftOilLogList.keySet()) {
//			List<DraftOilLog> list = draftOilLogList.get(key);
//			List<OilLog> list2 = new ArrayList<OilLog>();
//			for(DraftOilLog draftOilLog : list) {
//				list2.add(DraftOilLog.toOilLog(draftOilLog));
//			}
//			oilLogList.put(key, list2);
//		}
//		return oilLogList;
		return null;
	}
	
	@Override
	public Map<String, List<OilLog>> getDraftOil1LogList(int fromIndex, int toIndex) {
//		Map<String, List<DraftOilLog>> draftOilLogList = draftOil1LogDAO.getDraftOil1LogByGroup(fromIndex, toIndex);
//		Map<String, List<OilLog>> oilLogList = new HashMap<String, List<OilLog>>();
//		for(String key : draftOilLogList.keySet()) {
//			List<DraftOilLog> list = draftOilLogList.get(key);
//			List<OilLog> list2 = new ArrayList<OilLog>();
//			for(DraftOilLog draftOilLog : list) {
//				list2.add(DraftOilLog.toOilLog(draftOilLog));
//			}
//			oilLogList.put(key, list2);
//		}
//		return oilLogList;
		return null;
	}
	
	@Override
	public int getAllDraftOil1LogListSize() {
//		return draftOil1LogDAO.getAllDraftOil1LogByGroupSize();
		return 0;
	}
	
	@Override
	public void deleteDraftOil1LogGroupByUuid(String groupUUID) {
//		EntityTransaction entityTransaction = em.getTransaction();
//		entityTransaction.begin();
//
//		draftOil1LogDAO.deleteGroupByUuid(groupUUID);
//
//		entityTransaction.commit();
	}

	@Override
	public void deleteDraftOil1Log(long logId) {
//		EntityTransaction entityTransaction = em.getTransaction();
//		entityTransaction.begin();
//
//		draftOil1LogDAO.delete(logId);
//
//		entityTransaction.commit();
	}

	@Override
	public OilLog getLastOil1CodeA1Log() {
		String oilLogNumber = "OIL1";
		String codeLetter = "A";
		String codeNumber = "1";
		return oil1LogDAO.getLastLog(oilLogNumber, codeLetter, codeNumber);
	}
	@Override
	public OilLog getLastDraftOil1CodeA1Log() {
//		String oilLogNumber = "OIL1";
//		String codeLetter = "A";
//		String codeNumber = "1";
//		return DraftOilLog.toOilLog(draftOil1LogDAO.getLastLog(oilLogNumber, codeLetter, codeNumber));
		return null;
	}

	@Override
	public OilLog getLastOil1CodeA3_3Log() {
		String oilLogNumber = "OIL1";
		String codeLetter = "A";
		String codeNumber = "3.3";
		return oil1LogDAO.getLastLog(oilLogNumber, codeLetter, codeNumber);
	}
	@Override
	public OilLog getLastDraftOil1CodeA3_3Log() {
//		String oilLogNumber = "OIL1";
//		String codeLetter = "A";
//		String codeNumber = "3.3";
//		return DraftOilLog.toOilLog(draftOil1LogDAO.getLastLog(oilLogNumber, codeLetter, codeNumber));
		return null;
	}

	@Override
	public OilLog getLastOil1CodeA4_2Log() {
		String oilLogNumber = "OIL1";
		String codeLetter = "A";
		String codeNumber = "4.2";
		return oil1LogDAO.getLastLog(oilLogNumber, codeLetter, codeNumber);
	}
	@Override
	public OilLog getLastDraftOil1CodeA4_2Log() {
//		String oilLogNumber = "OIL1";
//		String codeLetter = "A";
//		String codeNumber = "4.2";
//		return DraftOilLog.toOilLog(draftOil1LogDAO.getLastLog(oilLogNumber, codeLetter, codeNumber));
		return null;
	}

	@Override
	public OilLog getLastOil1CodeC11_3Log() {
		String oilLogNumber = "OIL1";
		String codeLetter = "C";
		String codeNumber = "11.3";
		return oil1LogDAO.getLastLog(oilLogNumber, codeLetter, codeNumber);
	}
	@Override
	public OilLog getLastDraftOil1CodeC11_3Log() {
//		String oilLogNumber = "OIL1";
//		String codeLetter = "C";
//		String codeNumber = "11.3";
//		return DraftOilLog.toOilLog(draftOil1LogDAO.getLastLog(oilLogNumber, codeLetter, codeNumber));
		return null;
	}

	@Override
	public OilLog getLastOil1CodeC11_4Log() {
		String oilLogNumber = "OIL1";
		String codeLetter = "C";
		String codeNumber = "11.4";
		return oil1LogDAO.getLastLog(oilLogNumber, codeLetter, codeNumber);
	}
	@Override
	public OilLog getLastDraftOil1CodeC11_4Log() {
//		String oilLogNumber = "OIL1";
//		String codeLetter = "C";
//		String codeNumber = "11.4";
//		return DraftOilLog.toOilLog(draftOil1LogDAO.getLastLog(oilLogNumber, codeLetter, codeNumber));
		return null;
	}
	
	@Override
	public OilLog getLastOil1CodeC12_1Log() {
		String oilLogNumber = "OIL1";
		String codeLetter = "C";
		String codeNumber = "12.1";
		return oil1LogDAO.getLastLog(oilLogNumber, codeLetter, codeNumber);
	}
	@Override
	public OilLog getLastDraftOil1CodeC12_1Log() {
//		String oilLogNumber = "OIL1";
//		String codeLetter = "C";
//		String codeNumber = "12.1";
//		return DraftOilLog.toOilLog(draftOil1LogDAO.getLastLog(oilLogNumber, codeLetter, codeNumber));
		return null;
	}
	
	@Override
	public OilLog getLastOil1Log() {
		return oil1LogDAO.getLastLog();
	}
	@Override
	public OilLog getLastDraftOil1Log() {
//		return DraftOilLog.toOilLog(draftOil1LogDAO.getLastLog());
		return null;
	}
	
	@Override
	public OilLog getOil1Log(String groupUUID, String codeLetter, String codeNumber) {
		String oilLogNumber = "OIL1";
		return oil1LogDAO.getLog(oilLogNumber, groupUUID, codeLetter, codeNumber);
	}
	@Override
	public OilLog getDraftOil1Log(String groupUUID, String codeLetter, String codeNumber) {
//		String oilLogNumber = "OIL1";
//		DraftOilLog draftOilLog = draftOil1LogDAO.getLog(oilLogNumber, groupUUID, codeLetter, codeNumber);
//		return DraftOilLog.toOilLog(draftOilLog);
		return null;
	}

	@Override
	public Tank getTankByName(String tankName) {
		return tankDAO.getTankByName(tankName);
	}

	@Override
	public Tank updateTank(Tank tank) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		Tank updatedTank = tankDAO.update(tank);
		entityTransaction.commit();
		auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " updated tank " + tank.getName());
		return updatedTank;
	}
	
	@Override
	public OilLog updateDraftOil1Log(OilLog log) {
//		EntityTransaction entityTransaction = em.getTransaction();
//		entityTransaction.begin();
//		DraftOilLog draftOilLog = draftOil1LogDAO.update(DraftOilLog.getInstanceFrom(log));
//
//		entityTransaction.commit(); 
//		return DraftOilLog.toOilLog(draftOilLog);
		return null;
	}

	@Override
	public List<OilLog> getDraftOil1LogListByGroupUUID(String groupUUID) {
//		List<String> groupUUIDList = new ArrayList<String>();
//		groupUUIDList.add(groupUUID);
//		List<DraftOilLog> draftOilLogList = draftOil1LogDAO.getDraftOil1LogByGroup(groupUUIDList).get(groupUUID);
//		List<OilLog> oilLogList = new ArrayList<OilLog>();
//		for(DraftOilLog oilLog : draftOilLogList) {
//			em.detach(oilLog);
//			oilLogList.add(DraftOilLog.toOilLog(oilLog));
//		}
//		return oilLogList;
		return null;
	}



	@Override
	public Map<String, List<OilLog>> getGroupsDraftOil1LogList(List<String> groupUIIDList) {
//		Map<String, List<DraftOilLog>> draftOilLogMap = draftOil1LogDAO.getDraftOil1LogByGroup(groupUIIDList);
//		Map<String, List<OilLog>> oilLogMap = new HashMap<String, List<OilLog>>();
//		for(String groupUIID : draftOilLogMap.keySet()) {
//			List<OilLog> tempOilLogList = new ArrayList<OilLog>();
//			for(DraftOilLog oilLog : draftOilLogMap.get(groupUIID)) {
//				tempOilLogList.add(DraftOilLog.toOilLog(oilLog));
//			}
//			oilLogMap.put(groupUIID, tempOilLogList);
//		}
//		return oilLogMap;
		return null;
	}





}
