package com.oceanpro.core.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.oceanpro.core.entity.oceanManager.logbook.operationLog.GarbageCategory;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.GarbageLog;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.MarpleLogSettings;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.OilLog;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.widee.base.hibernate.criteria.Finder;

public interface OperationLogService {
	
	public List<MarpleLogSettings> getAll();
	public List<MarpleLogSettings> getAllMarpleLogSettingsByShip(Long shipId);
	public MarpleLogSettings update(MarpleLogSettings settings, Collection<String> addedTankNames, Collection<String> updatedTankNames, Collection<String> deletedTankNames);

	//public List<Tank>getTanks(Long marleLogSettingsId, );
	
	
	public List<GarbageCategory> getAllGarbageCategories();
	public GarbageCategory getGarbageCategory(Long id);
	public Finder<GarbageCategory> getGarbageCategoryFinder();
	
	
	
//	public Finder<SubCategory> getSubCategoryFinder();
//	public SubCategory getSubCategoryByUuid(String uuid);
	
	public List<GarbageLog> getAllGarbageLog();
	public GarbageLog addGarbageLog(GarbageLog log);
	public GarbageLog updateGarbageLog(GarbageLog log);
	public void deleteGarbageLog(long logId);
	public Finder<GarbageLog> getGarbageLogFinder();
	public GarbageLog getGarbageLogByUuid(String uuid);
	
	
	public List<OilLog> getAllOil1Log();
	public Map<String, List<OilLog>> getAllOil1LogList();
	public Map<String, List<OilLog>> getOil1LogList(int fromIndex, int toIndex);
	public int getAllOil1LogListSize();
	public Map<String, List<OilLog>> getGroupsOil1LogList(List<String> groupUUIDList);
	public Map<String, List<OilLog>> getSignatureGroupsOil1LogList(List<String> signatureGroupUUIDList);
	public Map<String, Map<String, List<OilLog>>> getSignatureGroupsOil1LogMaps(List<String> signatureGroupUIIDList);
	public OilLog addOil1Log(OilLog log);
	public OilLog updateOil1Log(OilLog log);
	public OilLog updateDraftOil1Log(OilLog log);
	public void deleteOil1Log(long logId);
	public void deleteOil1LogGroupByUuid(String uuid);
	public Finder<OilLog> getOil1LogFinder();
	public OilLog getOil1LogByUuid(String uuid);
	
	public void addDraftOil1Log(OilLog log);
	public Map<String, List<OilLog>> getAllDraftOil1LogList();
	public Map<String, List<OilLog>> getDraftOil1LogList(int fromIndex, int toIndex);
	public int getAllDraftOil1LogListSize();
	public void deleteDraftOil1Log(long logId);
	public void deleteDraftOil1LogGroupByUuid(String groupUUID);
	public OilLog getLastOil1CodeA1Log();
	public OilLog getLastDraftOil1CodeA1Log();
	public OilLog getLastOil1CodeA3_3Log();
	public OilLog getLastDraftOil1CodeA3_3Log();
	public OilLog getLastOil1CodeA4_2Log();
	public OilLog getLastDraftOil1CodeA4_2Log();
	public OilLog getLastOil1CodeC11_3Log();
	public OilLog getLastDraftOil1CodeC11_3Log();
	public OilLog getLastOil1CodeC11_4Log();
	public OilLog getLastDraftOil1CodeC11_4Log();
	public OilLog getLastOil1CodeC12_1Log();
	public OilLog getLastDraftOil1CodeC12_1Log();
	public OilLog getLastOil1Log();
	public OilLog getLastDraftOil1Log();
	public List<OilLog> getOil1LogListByGroupUUID(String groupUUID);
	public List<OilLog> getDraftOil1LogListByGroupUUID(String groupUUID);
	public OilLog getOil1Log(String groupUUID, String codeLetter, String codeNumber);
	public OilLog getDraftOil1Log(String groupUUID, String codeLetter, String codeNumber);
	public Tank getTankByName(String tankName);
	public Map<String, List<OilLog>> getGroupsDraftOil1LogList(List<String> groupUIIDList);
	public void moveFromDraftToOil1Log(List<OilLog> logList);
	public int getAllOil1LogSignatureGroupsSize();
	public Tank updateTank(Tank tank);
	
	

}
