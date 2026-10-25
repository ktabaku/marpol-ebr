package com.oceanpro.core.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.shiro.SecurityUtils;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;

import com.gs.fw.common.mithra.AggregateData;
import com.gs.fw.common.mithra.AggregateList;
import com.gs.fw.common.mithra.MithraManagerProvider;
import com.gs.fw.finder.Operation;
import com.oceanpro.core.security.entities.User;
import com.oceanpro.core.services.OperationLogReladomoService;
import com.widee.base.hibernate.criteria.Finder;

import al.sybina.ebr.commons.utils.AuditLogger;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook.domain.reladomo.OilLogFinder;
import al.sybina.ebr.logbook.domain.reladomo.OilLogList;
import al.sybina.ebr.logbook.domain.reladomo.TimestampProvider;
import javafx.util.Pair;

/**
 * @author Gio
 *
 */
@Creatable("OperationLogReladomoService")
public class OperationLogReladomoServiceImpl implements OperationLogReladomoService {

	private static final Logger logger = LoggerCreator.createLogger(OperationLogReladomoServiceImpl.class);
	private static final Logger auditLogger = LoggerCreator.createLogger(AuditLogger.class);
	
	protected void activate() {

	}

    protected void deactivate() {

    }
	


	@Override
	public int getAllValidOil1LogSize(String oilLogType) {
		return getAllOil1LogSize(oilLogType, null, null);
	}
	@Override
	public int getAllOil1LogSize(String oilLogType, LocalDateTime validInBusinessDate, LocalDateTime validInProcessingDate) {
		Operation validBusinessDateOp = null;
		if(validInBusinessDate==null || (validInBusinessDate!=null && validInBusinessDate.equals(TimestampProvider.getInfinityDate()))) {
			validBusinessDateOp = OilLogFinder.businessDate().equalsEdgePoint().and(OilLogFinder.businessDateTo().eq(TimestampProvider.getInfinityDate()));
		} else {
			validBusinessDateOp = OilLogFinder.businessDate().eq(Timestamp.valueOf(validInBusinessDate));
		}
		Operation validProcessingDateOp = null;
		if(validInProcessingDate==null || (validInProcessingDate!=null && validInProcessingDate.equals(TimestampProvider.getInfinityDate()))) {
			validProcessingDateOp = null;
		} else {
			validProcessingDateOp = OilLogFinder.processingDate().eq(Timestamp.valueOf(validInProcessingDate));
		}
		Operation oilLogTypeOp = OilLogFinder.type().eq(oilLogType);
		return OilLogFinder.findMany(validProcessingDateOp==null?oilLogTypeOp.and(validBusinessDateOp):oilLogTypeOp.and(validBusinessDateOp.and(validProcessingDateOp))).count();
	}
	
	@Override
	public Map<Long, Pair<String, Integer>> getAllOil1LogGroupSizes(String oilLogType, LocalDateTime validInBusinessDate, LocalDateTime validInProcessingDate) {
		Operation validBusinessDateOp = null;
		if(validInBusinessDate==null || (validInBusinessDate!=null && validInBusinessDate.equals(TimestampProvider.getInfinityDate()))) {
			validBusinessDateOp = OilLogFinder.businessDate().equalsEdgePoint().and(OilLogFinder.businessDateTo().eq(TimestampProvider.getInfinityDate()));
		} else {
			validBusinessDateOp = OilLogFinder.businessDate().eq(Timestamp.valueOf(validInBusinessDate));
		}
		Operation validProcessingDateOp = null;
		if(validInProcessingDate==null || (validInProcessingDate!=null && validInProcessingDate.equals(TimestampProvider.getInfinityDate()))) {
			validProcessingDateOp = null;
		} else {
			validProcessingDateOp = OilLogFinder.processingDate().eq(Timestamp.valueOf(validInProcessingDate));
		}
		Operation oilLogTypeOp = OilLogFinder.type().eq(oilLogType);
		Operation resultingOp = validProcessingDateOp==null?oilLogTypeOp.and(validBusinessDateOp):oilLogTypeOp.and(validBusinessDateOp.and(validProcessingDateOp));
		
		AggregateList aggList = new AggregateList((com.gs.fw.common.mithra.finder.Operation) resultingOp);
		
		aggList.addGroupBy("groupUuid", OilLogFinder.groupUuid());
		aggList.addAggregateAttribute("count", OilLogFinder.groupUuid().count());
		aggList.addAggregateAttribute("id", OilLogFinder.id().max());
		Map<Long, Pair<String, Integer>> groupUuidMap = new TreeMap<Long, Pair<String, Integer>>();
		for(AggregateData data: aggList) {
			groupUuidMap.put(data.getAttributeAsLong("id") , new Pair<String, Integer>(data.getAttributeAsString("groupUuid"), data.getAttributeAsInteger("count")));
		}
		return groupUuidMap;
	}
	
	@Override
	public List<OilLog> getValidOil1Log(String oilLogType, LocalDateTime validInBusinessDate, LocalDateTime validInProcessingDate, int fromIndex, int toIndex) {
		Operation validBusinessDateOp = null;
		if(validInBusinessDate==null || (validInBusinessDate!=null && validInBusinessDate.equals(TimestampProvider.getInfinityDate()))) {
			validBusinessDateOp = OilLogFinder.businessDate().equalsEdgePoint().and(OilLogFinder.businessDateTo().eq(TimestampProvider.getInfinityDate()));
		} else {
			validBusinessDateOp = OilLogFinder.businessDate().eq(Timestamp.valueOf(validInBusinessDate));
		}
		Operation validProcessingDateOp = null;
		if(validInProcessingDate==null || (validInProcessingDate!=null && validInProcessingDate.equals(TimestampProvider.getInfinityDate()))) {
			validProcessingDateOp = null;
		} else {
			validProcessingDateOp = OilLogFinder.processingDate().eq(Timestamp.valueOf(validInProcessingDate));
		}
		Operation oilLogTypeOp = OilLogFinder.type().eq(oilLogType);
		OilLogList oilLogs = OilLogFinder.findMany(validProcessingDateOp==null?oilLogTypeOp.and(validBusinessDateOp):oilLogTypeOp.and(validBusinessDateOp.and(validProcessingDateOp)));
		oilLogs.setOrderBy(OilLogFinder.id().ascendingOrderBy());
		oilLogs.setMaxObjectsToRetrieve(10000);
		List<OilLog> result = new ArrayList<OilLog>();
		MutableSet<Object> mutableSet = Sets.mutable.empty();
		oilLogs.forEachWithCursor(o -> {
			mutableSet.add(o);
			if(mutableSet.size() > fromIndex) result.add(((OilLog)o).getDetachedCopy());
			return mutableSet.size() < toIndex;
		});
		return result;
	}
	
	@Override
	public List<OilLog> getValidInDatesFromToBusinessDateOil1Log(String oilLogType, LocalDateTime validInBusinessDate, LocalDateTime validInProcessingDate, LocalDateTime fromBusinessDate, LocalDateTime toBusinessDate) {
		Operation validBusinessDateOp = null;
		if(validInBusinessDate==null || (validInBusinessDate!=null && validInBusinessDate.equals(TimestampProvider.getInfinityDate()))) {
			validBusinessDateOp = OilLogFinder.businessDate().equalsEdgePoint().and(OilLogFinder.businessDateTo().eq(TimestampProvider.getInfinityDate()));
		} else {
			validBusinessDateOp = OilLogFinder.businessDate().eq(Timestamp.valueOf(validInBusinessDate));
		}
		Operation validProcessingDateOp = null;
		if(validInProcessingDate==null || (validInProcessingDate!=null && validInProcessingDate.equals(TimestampProvider.getInfinityDate()))) {
			validProcessingDateOp = null;
		} else {
			validProcessingDateOp = OilLogFinder.processingDate().eq(Timestamp.valueOf(validInProcessingDate));
		}
		Operation greaterThanFromBusinessDate = OilLogFinder.businessDateFrom().greaterThan(Timestamp.valueOf(fromBusinessDate));
		Operation lessThanToBusinessDate = OilLogFinder.businessDateFrom().lessThan(Timestamp.valueOf(toBusinessDate));
		Operation oilLogTypeOp = OilLogFinder.type().eq(oilLogType);
		OilLogList oilLogs = OilLogFinder.findMany(validProcessingDateOp==null?oilLogTypeOp.and(validBusinessDateOp.and(greaterThanFromBusinessDate.and(lessThanToBusinessDate))):oilLogTypeOp.and(validBusinessDateOp.and(validProcessingDateOp.and(greaterThanFromBusinessDate.and(lessThanToBusinessDate)))));
		oilLogs.setOrderBy(OilLogFinder.id().ascendingOrderBy());
		return oilLogs;
	}
	
	@Override
	public Map<String, List<OilLog>> getValidOil1LogNoMasterSignature(String oilLogType) {
		Operation oilLogTypeOp = OilLogFinder.type().eq(oilLogType);
		Operation validBusinessDateOp = OilLogFinder.businessDate().equalsEdgePoint().and(OilLogFinder.businessDateTo().eq(TimestampProvider.getInfinityDate()));
		Operation oilLogMasterSignatureOp = OilLogFinder.masterSignature().isNull();
		List<OilLog> rawResult = OilLogFinder.findMany(oilLogTypeOp.and(oilLogMasterSignatureOp).and(validBusinessDateOp));
		return groupByGroupUuid(rawResult);
	}
	
	@Override
	public List<OilLog> getValidFromToBusinessDateOil1Log(String oilLogType, LocalDateTime fromBusinessDate, LocalDateTime toBusinessDate) {
		Operation validBusinessDateOp = OilLogFinder.businessDate().equalsEdgePoint().and(OilLogFinder.businessDateTo().eq(TimestampProvider.getInfinityDate()));
		Operation greaterThanFromBusinessDate = OilLogFinder.businessDateFrom().greaterThan(Timestamp.valueOf(fromBusinessDate));
		Operation lessThanToBusinessDate = OilLogFinder.businessDateFrom().lessThan(Timestamp.valueOf(toBusinessDate));
		Operation oilLogTypeOp = OilLogFinder.type().eq(oilLogType);
		
		OilLogList oilLogs = OilLogFinder.findMany(oilLogTypeOp.and(validBusinessDateOp.and(greaterThanFromBusinessDate).and(lessThanToBusinessDate)));
		oilLogs.setOrderBy(OilLogFinder.id().ascendingOrderBy());
		return oilLogs;
	}
	
	@Override
	public List<OilLog> getOil1LogListByGroupUUID(String... groupUUID) {		
		Operation validBusinessDateOp = OilLogFinder.businessDate().equalsEdgePoint().and(OilLogFinder.businessDateTo().eq(TimestampProvider.getInfinityDate()));
		Operation groupUUIDOp = OilLogFinder.groupUuid().in(new HashSet<String>(Arrays.asList(groupUUID)));
		OilLogList oilLogs = OilLogFinder.findMany(groupUUIDOp.and(validBusinessDateOp));
		oilLogs.setOrderBy(OilLogFinder.id().ascendingOrderBy());
		oilLogs.setMaxObjectsToRetrieve(100);
		List<OilLog> result = new ArrayList<OilLog>();
		oilLogs.forEachWithCursor(o -> {
			OilLog oilLog = (OilLog)o;
			result.add(oilLog.getDetachedCopy());
			return true;
		});
		logger.debug("getOil1LogListByGroupUUID(String groupUUID)");
		return result;
	}
	
	@Override
	public List<OilLog> getLogModificationHistory(OilLog oilLogToEdit) {
		List<OilLog> result = new ArrayList<OilLog>();
		Operation idOp = OilLogFinder.id().eq(oilLogToEdit.getId());
        Operation processingDateOp = OilLogFinder.processingDate().equalsEdgePoint();
        Operation businessDateOp = OilLogFinder.businessDate().equalsEdgePoint();
        OilLogList oilLogs = OilLogFinder.findMany(idOp.and(processingDateOp.and(businessDateOp)));
		oilLogs.forEachWithCursor(o -> {
			OilLog oilLog = (OilLog)o;
			result.add(oilLog.getDetachedCopy());
			return true;
		});
		return result;
	}
	
	@Override
	public OilLog getLastOilLog(String oilLogType) {
		List<OilLog> result = new ArrayList<OilLog>();
		Operation validBusinessDateOp = OilLogFinder.businessDate().equalsEdgePoint().and(OilLogFinder.businessDateTo().eq(TimestampProvider.getInfinityDate()));
		Operation oilLogTypeOp = OilLogFinder.type().eq(oilLogType);
		OilLogList oilLogs = OilLogFinder.findMany(oilLogTypeOp.and(validBusinessDateOp));
		oilLogs.setOrderBy(OilLogFinder.id().descendingOrderBy());
		oilLogs.setMaxObjectsToRetrieve(1);
		oilLogs.forEachWithCursor(o -> {
			OilLog oilLog = (OilLog)o;
			result.add(oilLog.getDetachedCopy());
			return true;
		});
		return result.size()>0?result.get(0):null;
	}
	@Override
	public OilLog getLastIncludingCodeIMissedOilLog(String oilLogType) {
		List<OilLog> result = new ArrayList<OilLog>();
		Operation validBusinessDateOp = OilLogFinder.businessDate().equalsEdgePoint().and(OilLogFinder.businessDateTo().eq(TimestampProvider.getInfinityDate()));
		Operation oilLogTypeOp = OilLogFinder.type().eq(oilLogType);
		OilLogList oilLogs = OilLogFinder.findMany(oilLogTypeOp.and(validBusinessDateOp));
		oilLogs.setOrderBy(OilLogFinder.id().descendingOrderBy());
		oilLogs.setMaxObjectsToRetrieve(1);
		oilLogs.forEachWithCursor(o -> {
			OilLog oilLog = (OilLog)o;
			result.add(oilLog.getDetachedCopy());
			return true;
		});
		OilLog resultLog = result.size()>0?result.get(0):null;
		
		if(result.size()>0 && result.get(0).getNote()!=null && result.get(0).getNote().equals("Missed Operation")) {
//			Operation oilLogNoMissedFlagLogOp = OilLogFinder.codeLetter().notEq("I").and(OilLogFinder.content().notStartsWith("ENTRY PERTAINING TO AN EARLIER MISSED OPERATIONAL ENTRY"));
			Operation oilLogNoMissedLogOp = OilLogFinder.note().isNull().or(OilLogFinder.note().notEq("Missed Operation"));
			List<OilLog> result2 = new ArrayList<OilLog>();
			
			OilLogList oilLogsNoMissed = OilLogFinder.findMany(oilLogTypeOp.and(validBusinessDateOp.and(oilLogNoMissedLogOp)));
			oilLogsNoMissed.setOrderBy(OilLogFinder.id().descendingOrderBy());
			oilLogsNoMissed.setMaxObjectsToRetrieve(1);
			oilLogsNoMissed.forEachWithCursor(o2 -> {
				OilLog oilLog2 = (OilLog)o2;
				result2.add(oilLog2.getDetachedCopy());
				return true;
			});
			if(result2.size()>0) {
				if(result2.get(0).getBusinessDateFrom().after(resultLog.getBusinessDateFrom())) {
					resultLog = result2.get(0);
				}
			}
		}
		return resultLog;
	}
	
	@Override
	public OilLog getLastOil1CodeA1Log() {
		String oilLogNumber = "OIL1";
		String codeLetter = "A";
		String codeNumber = "1";
		return getLastLog(oilLogNumber, codeLetter, codeNumber);
	}
	@Override
	public OilLog getLastOil1CodeA3_3Log() {
		String oilLogNumber = "OIL1";
		String codeLetter = "A";
		String codeNumber = "3.3";
		return getLastLog(oilLogNumber, codeLetter, codeNumber);
	}
	@Override
	public OilLog getLastOil1CodeA4_2Log() {
		String oilLogNumber = "OIL1";
		String codeLetter = "A";
		String codeNumber = "4.2";
		return getLastLog(oilLogNumber, codeLetter, codeNumber);
	}
	@Override
	public OilLog getLastOil1CodeC11_3Log() {
		String oilLogNumber = "OIL1";
		String codeLetter = "C";
		String codeNumber = "11.3";
		return getLastLog(oilLogNumber, codeLetter, codeNumber);
	}
	@Override
	public OilLog getLastOil1CodeC11_4Log() {
		String oilLogNumber = "OIL1";
		String codeLetter = "C";
		String codeNumber = "11.4";
		return getLastLog(oilLogNumber, codeLetter, codeNumber);
	}
	
	@Override
	public OilLog getLastOil1CodeC12_1Log() {
		String oilLogNumber = "OIL1";
		String codeLetter = "C";
		String codeNumber = "12.1";
		return getLastLog(oilLogNumber, codeLetter, codeNumber);
	}

	private OilLog getLastLog(String oilLogType, String codeLetter, String codeNumber) {
		List<OilLog> result = new ArrayList<OilLog>();
		Operation validBusinessDateOp = OilLogFinder.businessDate().equalsEdgePoint().and(OilLogFinder.businessDateTo().eq(TimestampProvider.getInfinityDate()));
		Operation logTypeOp = OilLogFinder.type().eq(oilLogType);
		Operation codeLetterOp = OilLogFinder.codeLetter().eq(codeLetter);
		Operation codeNumberOp = OilLogFinder.codeNumber().eq(codeNumber);
		OilLogList oilLogs = OilLogFinder.findMany(logTypeOp.and(codeLetterOp).and(codeNumberOp).and(validBusinessDateOp));
		oilLogs.setOrderBy(OilLogFinder.id().descendingOrderBy());
		oilLogs.setMaxObjectsToRetrieve(1);
		oilLogs.forEachWithCursor(o -> {
			OilLog oilLog = (OilLog)o;
			result.add(oilLog.getDetachedCopy());
			return true;
		});
		OilLog resultLog = result.size()>0?result.get(0):null;
		
		if(result.size()>0 && result.get(0).getNote()!=null && result.get(0).getNote().equals("Missed Operation")) {
			Operation oilLogNoMissedLogOp = OilLogFinder.note().isNull().or(OilLogFinder.note().notEq("Missed Operation"));
			List<OilLog> result2 = new ArrayList<OilLog>();
			OilLogList oilLogsNoMissed = OilLogFinder.findMany(logTypeOp.and(codeLetterOp).and(codeNumberOp).and(validBusinessDateOp).and(oilLogNoMissedLogOp));
			oilLogsNoMissed.setOrderBy(OilLogFinder.id().descendingOrderBy());
			oilLogsNoMissed.setMaxObjectsToRetrieve(1);
			oilLogsNoMissed.forEachWithCursor(o -> {
				OilLog oilLog = (OilLog)o;
				result2.add(oilLog.getDetachedCopy());
				return true;
			});
			if(result2.size()>0) {
				if(result2.get(0).getBusinessDateFrom().after(resultLog.getBusinessDateFrom())) {
					resultLog = result2.get(0);
				}
			}
		}
		return resultLog;
	}
	
	
	
	@Override
	public void addOil1Log(OilLog log) {
		MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx -> {
			log.generateAndSetId();
			log.insert();
			return null;
		});
		auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " added oil log " + log.toString());
	}
	@Override
	public List<OilLog> addOil1Logs(List<OilLog> oilLogs) {
		return MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx -> {
			oilLogs.sort(Comparator.comparing(oilLog -> oilLog.getOrderInGroup()));
			oilLogs.forEach(oilLog -> oilLog.generateAndSetId());
			OilLogList oiLogList = new OilLogList(oilLogs);
			oiLogList.insertAll();
			auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " added oil log " + oilLogs);
			return oilLogs;
		});
	}
	
	
	@Override
	public void editSignOil1Log(LocalDateTime businessDate, OilLog oilLog, List<OilLog> sameGroupOilLogList) {
		if(oilLog.getBusinessDate().equals(Timestamp.valueOf(businessDate))) {
			MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx -> {
				oilLog.copyDetachedValuesToOriginalOrInsertIfNew();
				OilLogList oiLogList = new OilLogList(sameGroupOilLogList);
				oiLogList.copyDetachedValuesToOriginalOrInsertIfNewOrDeleteIfRemoved();
				return null;
			});
		} else if(oilLog.getBusinessDate().before(Timestamp.valueOf(businessDate))) {
			MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx -> {
//				originalDetachedOilLog.copyDetachedValuesToOriginalOrInsertIfNewUntil(Timestamp.valueOf(businessDate));
				Operation validBusinessDateOp = OilLogFinder.businessDate().eq(Timestamp.valueOf(businessDate));
				Operation idOp = OilLogFinder.id().eq(oilLog.getId());
				OilLog originalOilLog = OilLogFinder.findOne(idOp.and(validBusinessDateOp));
				originalOilLog.copyNonPrimaryKeyAttributesFrom(oilLog);
				OilLogList oiLogList = new OilLogList(sameGroupOilLogList);
				oiLogList.copyDetachedValuesToOriginalOrInsertIfNewOrDeleteIfRemoved();
				return null;
			});
		} else if(oilLog.getBusinessDate().after(Timestamp.valueOf(businessDate))) {
			MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx -> {
				Operation validBusinessDateOp = OilLogFinder.businessDate().eq(Timestamp.valueOf(businessDate));
				Operation idOp = OilLogFinder.id().eq(oilLog.getId());
				OilLog originalOilLog = OilLogFinder.findOne(idOp.and(validBusinessDateOp));
				if(originalOilLog!=null) {
					originalOilLog.copyNonPrimaryKeyAttributesFrom(oilLog);
				} else {
					Operation actuallyValidBusinessDateOp = OilLogFinder.businessDate().equalsEdgePoint().and(OilLogFinder.businessDateTo().eq(TimestampProvider.getInfinityDate()));
					OilLog actuallyValidOilLog = OilLogFinder.findOne(idOp.and(actuallyValidBusinessDateOp));
					actuallyValidOilLog.setProcessingDateTo(Timestamp.valueOf(LocalDateTime.now()));
					OilLog newOilLog = new OilLog(Timestamp.valueOf(businessDate));
					newOilLog.setId(oilLog.getId());
					newOilLog.copyNonPrimaryKeyAttributesFrom(oilLog);
					newOilLog.copyDetachedValuesToOriginalOrInsertIfNew();
				}
				OilLogList oiLogList = new OilLogList(sameGroupOilLogList);
				oiLogList.copyDetachedValuesToOriginalOrInsertIfNewOrDeleteIfRemoved();
				return null;
			});
			auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " edited/signed oil log " + oilLog);
//			LocalDateTime validInBusinessDate = businessDate;
//			Operation actuallyValidBusinessDateOp = OilLogFinder.businessDate().equalsEdgePoint().and(OilLogFinder.businessDateTo().eq(TimestampProvider.getInfinityDate()));
//			Operation validBusinessDateOp = OilLogFinder.businessDate().eq(Timestamp.valueOf(validInBusinessDate));
//            Operation id = OilLogFinder.id().eq(oilLog.getId());
//            OilLog originalValidInDateOilLog = OilLogFinder.findOne(id.and(validBusinessDateOp));
//            OilLog originalActuallyValidInDateOilLog = OilLogFinder.findOne(id.and(actuallyValidBusinessDateOp));
//			MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx -> {
//				originalActuallyValidInDateOilLog.cascadeTerminate();
//				OilLog newOilLog = new OilLog(Timestamp.valueOf(businessDate));
//				newOilLog.setId(originalActuallyValidInDateOilLog.getId());
//				newOilLog.copyNonPrimaryKeyAttributesFrom(oilLog);
//				newOilLog.insert();
//				return null;
//			});
		}
	}
	@Override
	public void editSignOil1Logs(List<OilLog> oilLogs) {
		MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx -> {
			OilLogList list = new OilLogList(oilLogs);
			list.copyDetachedValuesToOriginalOrInsertIfNewOrDeleteIfRemoved();
			return null;
		});
		auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " edited/signed oil logs " + oilLogs);
	}
	
	@Override
	public void terminateOil1Log(OilLog oilLog) {
		MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx -> {
			OilLog attachedOilLog = OilLogFinder.findOne(OilLogFinder.id().eq(oilLog.getId()).and(OilLogFinder.businessDate().equalsEdgePoint()));
			attachedOilLog.terminate();
			return null;
		});
	}





	
	
	
	@Override
	public OilLog getOil1Log(String groupUUID, String codeLetter, String codeNumber) {
//		String oilLogNumber = "OIL1";
//		return oil1LogDAO.getLog(oilLogNumber, groupUUID, codeLetter, codeNumber);
		logger.info("getOil1Log(String groupUUID, String codeLetter, String codeNumber)");
		return null;
	}
	@Override
	public List<OilLog> getAllOil1Log() {
//		return oil1LogDAO.getFinder().addOrderDesc("date").addOrderDesc("groupUuid").addOrderAsc("orderInGroup").setDistinct().list();
		logger.info("getAllOil1Log()");
		return null;
	}
	@Override
	public void deleteOil1LogGroupByUuid(String groupUUID) {
//		EntityTransaction entityTransaction = em.getTransaction();
//		entityTransaction.begin();
//
//		oil1LogDAO.deleteGroupByUuid(groupUUID);
//
//		entityTransaction.commit();
		logger.info("deleteOil1LogGroupByUuid(String groupUUID)");
	}
	@Override
	public Finder<OilLog> getOil1LogFinder() {
//		return oil1LogDAO.getFinder();
		logger.info("getOil1LogFinder()");
		return null;
	}
	@Override
	public OilLog getOil1LogByUuid(String uuid) {
//		return oil1LogDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
		logger.info("getOil1LogByUuid(String uuid)");
		return null;
	}
	@Override
	public Map<String, List<OilLog>> getAllOil1LogList() {
//		return oil1LogDAO.getAllOil1LogByGroup();
		logger.info("getAllOil1LogList()");
		return null;
	}
	@Override
	public Map<String, Map<String, List<OilLog>>> getSignatureGroupsOil1LogMaps(List<String> signatureGroupUUIDList) {
//		Map<String, Map<String, List<OilLog>>> result = new HashMap<String, Map<String, List<OilLog>>>();
//		Map<String, List<OilLog>> signatureGroupsOil1LogLists = oil1LogDAO.getOil1LogBySignatureGroup(signatureGroupUUIDList);
//		for(String signatureUUID : signatureGroupsOil1LogLists.keySet()) {
//			List<OilLog> oilLogList = signatureGroupsOil1LogLists.get(signatureUUID);
//			Map<String, List<OilLog>> groupsOil1LogLists = new HashMap<String, List<OilLog>>();
//			for(OilLog oilLog : oilLogList) {
//				List<OilLog> list = groupsOil1LogLists.get(oilLog.getGroupUuid());
//				if(list==null) {
//					List<OilLog> list2 = new ArrayList<OilLog>();
//					list2.add(oilLog);
//					groupsOil1LogLists.put(oilLog.getGroupUuid(), list2);
//				} else {
//					list.add(oilLog);
//				}
//			}
//			result.put(signatureUUID, groupsOil1LogLists);
//		}
//		return result;
		logger.info("getSignatureGroupsOil1LogMaps(List<String> signatureGroupUUIDList)");
		return null;
	}
	@Override
	public Map<String, List<OilLog>> getGroupsOil1LogList(List<String> groupUUIDList) {
//		return oil1LogDAO.getOil1LogByGroup(groupUUIDList);
		logger.info("getGroupsOil1LogList(List<String> groupUUIDList)");
		return null;
	}


	private Map<String, List<OilLog>> groupByGroupUuid(List<OilLog> source) {
		Map<String, List<OilLog>> resultMap = new HashMap<String, List<OilLog>>();
		for (OilLog oilLog : source) {
			List<OilLog> list = resultMap.get(oilLog.getGroupUuid());
			if (list == null) {
				List<OilLog> list2 = new ArrayList<OilLog>();
				list2.add(oilLog);
				resultMap.put(oilLog.getGroupUuid(), list2);
			} else {
				list.add(oilLog);
			}
		}
		return resultMap;
	}
}
