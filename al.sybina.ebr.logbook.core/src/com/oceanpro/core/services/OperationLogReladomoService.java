package com.oceanpro.core.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.oceanpro.core.entity.oceanManager.logbook.operationLog.GarbageCategory;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.GarbageLog;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.MarpleLogSettings;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.widee.base.hibernate.criteria.Finder;

import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import javafx.util.Pair;

public interface OperationLogReladomoService {
	
	public List<OilLog> getAllOil1Log();
	public Map<String, List<OilLog>> getAllOil1LogList();
	public List<OilLog> getValidOil1Log(String oilLogType, LocalDateTime validInBusinessDate, LocalDateTime validInProcessingDate, int fromIndex, int toIndex);
	public Map<String, List<OilLog>> getGroupsOil1LogList(List<String> groupUUIDList);
	public Map<String, Map<String, List<OilLog>>> getSignatureGroupsOil1LogMaps(List<String> signatureGroupUIIDList);
	public void addOil1Log(OilLog log);
//	public OilLog updateOil1Log(OilLog log);

	public void deleteOil1LogGroupByUuid(String uuid);
	public Finder<OilLog> getOil1LogFinder();
	public OilLog getOil1LogByUuid(String uuid);

//	public OilLog updateDraftOil1Log(OilLog log);	
//	public Map<String, List<OilLog>> getAllDraftOil1LogList();
//	public Map<String, List<OilLog>> getDraftOil1LogList(int fromIndex, int toIndex);
//	public int getAllDraftOil1LogListSize();
//	public void deleteDraftOil1Log(long logId);
//	public void deleteDraftOil1LogGroupByUuid(String groupUUID);
//	public OilLog getLastDraftOil1CodeA1Log();	
//	public OilLog getLastDraftOil1CodeA3_3Log();
//	public OilLog getLastDraftOil1CodeA4_2Log();
//	public OilLog getLastDraftOil1CodeC11_3Log();	
//	public OilLog getLastDraftOil1CodeC11_4Log();
//	public OilLog getLastDraftOil1CodeC12_1Log();	
//	public List<OilLog> getDraftOil1LogListByGroupUUID(String groupUUID);
	
//	public OilLog getDraftOil1Log(String groupUUID, String codeLetter, String codeNumber);
//	public Map<String, List<OilLog>> getGroupsDraftOil1LogList(List<String> groupUIIDList);
//	public void moveFromDraftToOil1Log(List<OilLog> logList);
	
	
	public OilLog getLastOil1CodeA1Log();
	public OilLog getLastOil1CodeA3_3Log();
	public OilLog getLastOil1CodeA4_2Log();
	public OilLog getLastOil1CodeC11_3Log();
	public OilLog getLastOil1CodeC11_4Log();
	public OilLog getLastOil1CodeC12_1Log();
	
	public OilLog getLastOilLog(String oilLogType);
	public OilLog getLastIncludingCodeIMissedOilLog(String oilLogType);
	
	public List<OilLog> getOil1LogListByGroupUUID(String... groupUUID);

	public OilLog getOil1Log(String groupUUID, String codeLetter, String codeNumber);

	public int getAllValidOil1LogSize(String oilLogType);
	public void editSignOil1Logs(List<OilLog> allOilLogList);
	public void editSignOil1Log(LocalDateTime businessDate, OilLog oilLog, List<OilLog> sameGroupOilLogList);
	public List<OilLog> addOil1Logs(List<OilLog> value);
	public int getAllOil1LogSize(String oilLogType, LocalDateTime validInBusinessDate, LocalDateTime validInProcessingDate);
	public void terminateOil1Log(OilLog oilLog);
	public List<OilLog> getLogModificationHistory(OilLog oilLogToEdit);
	public List<OilLog> getValidFromToBusinessDateOil1Log(String oilLogType, LocalDateTime fromBusinessDate, LocalDateTime toBusinessDate);
	public Map<String, List<OilLog>> getValidOil1LogNoMasterSignature(String oilLogType);
	public Map<Long, Pair<String, Integer>> getAllOil1LogGroupSizes(String oilLogType, LocalDateTime validInBusinessDate,LocalDateTime validInProcessingDate);
	public List<OilLog> getValidInDatesFromToBusinessDateOil1Log(String oilLogType, LocalDateTime validInBusinessDate,
			LocalDateTime validInProcessingDate, LocalDateTime fromBusinessDate, LocalDateTime toBusinessDate);
	
	
	
	
	
	
}
