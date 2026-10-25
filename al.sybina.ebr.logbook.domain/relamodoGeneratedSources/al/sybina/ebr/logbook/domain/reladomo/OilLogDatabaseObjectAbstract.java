package al.sybina.ebr.logbook.domain.reladomo;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gs.fw.common.mithra.*;
import com.gs.fw.common.mithra.attribute.*;
import com.gs.fw.common.mithra.util.*;
import com.gs.fw.common.mithra.notification.*;
import com.gs.fw.common.mithra.notification.listener.*;
import com.gs.fw.common.mithra.list.cursor.Cursor;
import com.gs.fw.common.mithra.bulkloader.*;
import java.util.*;
import java.sql.*;
import com.gs.fw.common.mithra.*;
import com.gs.fw.common.mithra.attribute.update.AttributeUpdateWrapper;
import com.gs.fw.common.mithra.behavior.*;
import com.gs.fw.common.mithra.behavior.state.DatedPersistenceState;
import com.gs.fw.common.mithra.bulkloader.BulkLoader;
import com.gs.fw.common.mithra.bulkloader.BulkLoaderException;
import com.gs.fw.common.mithra.cache.*;
import com.gs.fw.common.mithra.cache.offheap.*;
import com.gs.fw.common.mithra.connectionmanager.*;
import com.gs.fw.common.mithra.database.*;
import com.gs.fw.common.mithra.databasetype.*;
import com.gs.fw.common.mithra.finder.*;
import com.gs.fw.common.mithra.finder.asofop.AsOfOperation;
import com.gs.fw.common.mithra.finder.orderby.OrderBy;
import com.gs.fw.common.mithra.finder.integer.IntegerResultSetParser;
import com.gs.fw.common.mithra.querycache.CachedQuery;
import com.gs.fw.common.mithra.remote.RemoteMithraService;
import com.gs.fw.common.mithra.transaction.BatchUpdateOperation;
import com.gs.fw.common.mithra.transaction.UpdateOperation;
/**
* This file was automatically generated using Mithra 17.1.4. Please do not modify it.
* Add custom logic to its subclass instead.
*/
public abstract class OilLogDatabaseObjectAbstract extends MithraAbstractDatedTransactionalDatabaseObject
implements MithraDatedTransactionalDatabaseObject, MithraDatedTransactionalObjectFactory
{
	private SourcelessConnectionManager connectionManager;
	private SchemaManager schemaManager;
	private TablePartitionManager tablePartitionManager;
	private static final String COL_LIST_WITHOUT_PK = "UUID,SHIP_ID,TYPE,CODELETTER,CODENUMBER,CONTENT,SIGNATURE,GROUPUUID,ORDERINGROUP,SIGNERRANK,SIGNERNAME,SIGNERSURNAME,NOTE,CORRECTIONNOTE,MASTERSIGNERNAME,MASTERSIGNERSURNAME,MASTERSIGNATURE,MASTERSIGDATE,SIGNATUREGROUPUUID,ATTACHMENTNAME,ATTACHMENTDATA,FROM_Z,THRU_Z,IN_Z,OUT_Z";
	private static final String COL_LIST_WITHOUT_PK_WITH_ALIAS = "t0.UUID,t0.SHIP_ID,t0.TYPE,t0.CODELETTER,t0.CODENUMBER,t0.CONTENT,t0.SIGNATURE,t0.GROUPUUID,t0.ORDERINGROUP,t0.SIGNERRANK,t0.SIGNERNAME,t0.SIGNERSURNAME,t0.NOTE,t0.CORRECTIONNOTE,t0.MASTERSIGNERNAME,t0.MASTERSIGNERSURNAME,t0.MASTERSIGNATURE,t0.MASTERSIGDATE,t0.SIGNATUREGROUPUUID,t0.ATTACHMENTNAME,t0.ATTACHMENTDATA,t0.FROM_Z,t0.THRU_Z,t0.IN_Z,t0.OUT_Z";
	private static final String PK_WITH_ALIAS = "t0.ID = ?";
	private static final String PK_INDEX_COLS = "ID";
	protected OilLogDatabaseObjectAbstract()
	{
		super("OilLog", "al.sybina.ebr.logbook.domain.reladomo.OilLogFinder",
			26, 26,
			COL_LIST_WITHOUT_PK, COL_LIST_WITHOUT_PK_WITH_ALIAS,
			true, false, false,
			PK_WITH_ALIAS,
			PK_INDEX_COLS);
	}

	public MithraObjectPortal getMithraObjectPortal()
	{
		return OilLogFinder.getMithraObjectPortal();
	}

	public RelatedFinder getFinder()
	{
		return OilLogFinder.getFinderInstance();
	}

	public static OilLogData allocateOnHeapData()
	{
		return new OilLogData();
	}

	public static OilLogData allocateOffHeapData()
	{
		throw new RuntimeException("no off heap implementation");
	}

	public MithraDataObject deserializeFullData(ObjectInput in) throws IOException, ClassNotFoundException
	{
		MithraDataObject data = new OilLogData();
		data.zDeserializeFullData(in);
		return data;
	}

	public void deserializeAsOfAttributes(ObjectInput in, Timestamp[] asof) throws IOException, ClassNotFoundException
	{
		asof[0] = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestampWithInfinity(in, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate()), OilLogFinder.isFullCache());
		asof[1] = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestampWithInfinity(in, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate()), OilLogFinder.isFullCache());
	}

	public MithraObject deserializeForRefresh(ObjectInput in) throws IOException, ClassNotFoundException
	{
		OilLogData data = new OilLogData();
		data.zDeserializePrimaryKey(in);
		Timestamp[] asof = new Timestamp[2];
		asof[0] = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestampWithInfinity(in, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate()), OilLogFinder.isFullCache());
		asof[1] = TimestampPool.getInstance().getOrAddToCache(MithraTimestamp.readTimezoneInsensitiveTimestampWithInfinity(in, al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate()), OilLogFinder.isFullCache());
		return this.createObject(data, asof);
	}

	public Cache instantiateFullCache(MithraConfigurationManager.Config config)
	{
		Cache result;
		if (config.isParticipatingInTx())
		{
			result = new FullDatedTransactionalCache(OilLogFinder.getPrimaryKeyAttributes(), OilLogFinder.getAsOfAttributes(), this, OilLogFinder.getImmutableAttributes());
		}
		else
		{
			result = new FullDatedCache(OilLogFinder.getPrimaryKeyAttributes(), OilLogFinder.getAsOfAttributes(), this, OilLogFinder.getImmutableAttributes());
		}

		initPortal(result, config);
		return result;
	}

	public Cache instantiatePartialCache(MithraConfigurationManager.Config config)
	{
		Cache result;
		if (config.isParticipatingInTx())
		{
			result = new PartialDatedTransactionalCache(OilLogFinder.getPrimaryKeyAttributes(), OilLogFinder.getAsOfAttributes(), this, OilLogFinder.getImmutableAttributes(), config.getCacheTimeToLive(), config.getRelationshipCacheTimeToLive());
		}
		else
		{
			result = new PartialDatedCache(OilLogFinder.getPrimaryKeyAttributes(), OilLogFinder.getAsOfAttributes(), this, OilLogFinder.getImmutableAttributes(), config.getCacheTimeToLive(), config.getRelationshipCacheTimeToLive());
		}

		initPortal(result, config);
		return result;
	}

	private void initPortal(Cache cache, MithraConfigurationManager.Config config)
	{
		if (config.isThreeTierClient())
		{
			OilLogFinder.initializeClientPortal(this, cache, config);
		}
		else
		{
			OilLogFinder.initializePortal(this, cache, config);
		}

		if (config.isParticipatingInTx())
		{
			OilLog.zConfigFullTx();
		}
		else
		{
			OilLog.zConfigNonTx();
		}
	}

	public List getSimulatedSequenceInitValues()
	{
		ArrayList simulatedSequenceInitValues = new ArrayList(1);
		Attribute[] primaryKeyAttributes = OilLogFinder.getPrimaryKeyAttributes();
		SimulatedSequenceInitValues initValues = null;
		return simulatedSequenceInitValues;
	}

	public Object getSourceAttributeValueForSelectedObjectGeneric(SqlQuery query, int queryNumber)
	{
		return null;
	}

	public Object getSourceAttributeValueFromObjectGeneric(MithraDataObject object)
	{
		return null;
	}

	public Object getSourceAttributeValueGeneric(SqlQuery query, MapperStackImpl mapperStack, int queryNumber)
	{
		return null;
	}

	public String getDatabaseIdentifierGenericSource (Object source)
	{
		return connectionManager.getDatabaseIdentifier();
	}

	public DatabaseType getDatabaseTypeGenericSource(Object source)
	{
		return connectionManager.getDatabaseType();
	}

	public TimeZone getDatabaseTimeZoneGenericSource(Object source)
	{
		return getDatabaseTimeZone();
	}

	public Connection getConnectionGenericSource(Object source)
	{
		return connectionManagerWrapper.getConnection();
	}

	public BulkLoader createBulkLoaderGenericSource(Object source) throws BulkLoaderException 
	{
		return connectionManager.createBulkLoader();
	}

	public MithraDataObject inflateDataGenericSource(ResultSet rs, Object source, DatabaseType dt)
	throws SQLException 
	{
		return inflateOilLogData(rs, dt);
	}

	public void inflateNonPkDataGenericSource(MithraDataObject data, ResultSet rs, Object source, DatabaseType dt)
	throws SQLException 
	{
		inflateNonPkOilLogData(1, (OilLogData) data, rs, dt);
	}

	public MithraDataObject inflatePkDataGenericSource(ResultSet rs, Object source, DatabaseType dt)
	throws SQLException 
	{
		return inflateOilLogPkData(rs, dt);
	}

	public String getSchemaGenericSource(Object source)
	{
		if (this.schemaManager != null)
		{
			return this.schemaManager.getSchema(this.getDefaultSchema());
		}

		return this.getDefaultSchema();
	}

	public String getTableNameGenericSource(Object source) throws MithraDatabaseException
	{
		return getOilLogTableName();
	}

	public String getOilLogTableName() throws MithraDatabaseException
	{
		if (this.tablePartitionManager != null)
		{
			return this.tablePartitionManager.getTableName(this.getDefaultTableName());
		}

		return this.getDefaultTableName();
	}

	public void setPrimaryKeyAttributes(PreparedStatement stm, int pos, MithraDataObject dataObj,
		TimeZone databaseTimeZone, DatabaseType dt) throws SQLException
	{
		OilLogData data = (OilLogData)dataObj;
		TimeZone conversionTimeZone = null;
		pos = setPrimaryKeyAttributesWithoutOptimistic(stm,pos,dataObj,databaseTimeZone,dt);
		if (OilLogFinder.getMithraObjectPortal().getTxParticipationMode().isOptimisticLocking())
		{
			conversionTimeZone = MithraTimestamp.DefaultTimeZone;
			dt.setTimestamp(stm, pos, data.getProcessingDateFrom(), false, conversionTimeZone);
			pos++;
		}
	}

	public int setPrimaryKeyAttributesWithoutOptimistic(PreparedStatement stm, int pos, MithraDataObject dataObj,
		TimeZone databaseTimeZone, DatabaseType dt) throws SQLException
	{
		OilLogData data = (OilLogData)dataObj;
		TimeZone conversionTimeZone = null;
		stm.setLong(pos++, data.getId());
		conversionTimeZone = MithraTimestamp.DefaultTimeZone;
		if (data.getBusinessDateTo().getTime() == OilLogFinder.businessDate().getInfinityDate().getTime())
		{
			conversionTimeZone = MithraTimestamp.DefaultTimeZone;
		}

		dt.setTimestamp(stm, pos, data.getBusinessDateTo(), false, conversionTimeZone);
		pos++;
		conversionTimeZone = MithraTimestamp.DefaultTimeZone;
		if (data.getProcessingDateTo().getTime() == OilLogFinder.processingDate().getInfinityDate().getTime())
		{
			conversionTimeZone = MithraTimestamp.DefaultTimeZone;
		}

		dt.setTimestamp(stm, pos, data.getProcessingDateTo(), false, conversionTimeZone);
		pos++;
		return pos;
	}

	public String getPrimaryKeyWhereSql()
	{
		if (OilLogFinder.getMithraObjectPortal().getTxParticipationMode().isOptimisticLocking())
		{
		}

		return "ID = ?";
	}

	public String getPrimaryKeyWhereSqlWithNullableAttribute(MithraDataObject dataObj)
	{
		return "";
	}

	public String getPrimaryKeyWhereSqlWithNullableAttributeWithDefaultAlias(MithraDataObject dataObj)
	{
		return "";
	}

	public String getColumnListWithPk(String databaseAlias)
	{
		if (databaseAlias.equals(SqlQuery.DEFAULT_DATABASE_ALIAS))
		{
			return "t0.ID,t0.UUID,t0.SHIP_ID,t0.TYPE,t0.CODELETTER,t0.CODENUMBER,t0.CONTENT,t0.SIGNATURE,t0.GROUPUUID,t0.ORDERINGROUP,t0.SIGNERRANK,t0.SIGNERNAME,t0.SIGNERSURNAME,t0.NOTE,t0.CORRECTIONNOTE,t0.MASTERSIGNERNAME,t0.MASTERSIGNERSURNAME,t0.MASTERSIGNATURE,t0.MASTERSIGDATE,t0.SIGNATUREGROUPUUID,t0.ATTACHMENTNAME,t0.ATTACHMENTDATA,t0.FROM_Z,t0.THRU_Z,t0.IN_Z,t0.OUT_Z";
		}

		StringBuffer result = new StringBuffer((databaseAlias.length()+15)*26);
		result.append(databaseAlias).append(".").append("ID");
		result.append(",").append(databaseAlias).append(".").append("UUID");
		result.append(",").append(databaseAlias).append(".").append("SHIP_ID");
		result.append(",").append(databaseAlias).append(".").append("TYPE");
		result.append(",").append(databaseAlias).append(".").append("CODELETTER");
		result.append(",").append(databaseAlias).append(".").append("CODENUMBER");
		result.append(",").append(databaseAlias).append(".").append("CONTENT");
		result.append(",").append(databaseAlias).append(".").append("SIGNATURE");
		result.append(",").append(databaseAlias).append(".").append("GROUPUUID");
		result.append(",").append(databaseAlias).append(".").append("ORDERINGROUP");
		result.append(",").append(databaseAlias).append(".").append("SIGNERRANK");
		result.append(",").append(databaseAlias).append(".").append("SIGNERNAME");
		result.append(",").append(databaseAlias).append(".").append("SIGNERSURNAME");
		result.append(",").append(databaseAlias).append(".").append("NOTE");
		result.append(",").append(databaseAlias).append(".").append("CORRECTIONNOTE");
		result.append(",").append(databaseAlias).append(".").append("MASTERSIGNERNAME");
		result.append(",").append(databaseAlias).append(".").append("MASTERSIGNERSURNAME");
		result.append(",").append(databaseAlias).append(".").append("MASTERSIGNATURE");
		result.append(",").append(databaseAlias).append(".").append("MASTERSIGDATE");
		result.append(",").append(databaseAlias).append(".").append("SIGNATUREGROUPUUID");
		result.append(",").append(databaseAlias).append(".").append("ATTACHMENTNAME");
		result.append(",").append(databaseAlias).append(".").append("ATTACHMENTDATA");
		result.append(",").append(databaseAlias).append(".").append("FROM_Z");
		result.append(",").append(databaseAlias).append(".").append("THRU_Z");
		result.append(",").append(databaseAlias).append(".").append("IN_Z");
		result.append(",").append(databaseAlias).append(".").append("OUT_Z");
		return result.toString();
	}

	public Object getConnectionManager()
	{
		return connectionManager;
	}

	public void setConnectionManager(Object connectionManager, ConnectionManagerWrapper wrapper)
	{
		this.connectionManager = (SourcelessConnectionManager)connectionManager;
		this.connectionManagerWrapper = wrapper;
	}

	public OilLogData inflateOilLogData(ResultSet rs, DatabaseType dt)
	throws SQLException
	{
		OilLogData data = inflateOilLogPkData(rs, dt);
		inflateNonPkOilLogData(2, data, rs, dt);
		return data;
	}

	public OilLogData inflateOilLogPkData(ResultSet _rs, DatabaseType _dt)
	throws SQLException
	{
		OilLogData _data = new OilLogData();
		int _pos = 1;
		_data.setId(_rs.getLong(_pos++));
		checkNullPrimitive(_rs, _data, "id");
		return _data;
	}

	public void inflateNonPkOilLogData(int _pos, OilLogData _datax, ResultSet _rs, DatabaseType _dt)
	throws SQLException
	{
		{
			OilLogData _data = _datax;
			_data.setUuid(trimString(_rs.getString(_pos++)));
			_data.setShip(_rs.getLong(_pos++));
			if (_rs.wasNull())
			{
				_data.setShipNull();
			}

			_data.setType(trimString(_rs.getString(_pos++)));
			_data.setCodeLetter(trimString(_rs.getString(_pos++)));
			_data.setCodeNumber(trimString(_rs.getString(_pos++)));
			_data.setContent(trimString(_rs.getString(_pos++)));
			_data.setSignature(trimString(_rs.getString(_pos++)));
			_data.setGroupUuid(trimString(_rs.getString(_pos++)));
			_data.setOrderInGroup(trimString(_rs.getString(_pos++)));
			_data.setSignerRank(trimString(_rs.getString(_pos++)));
			_data.setSignerName(trimString(_rs.getString(_pos++)));
			_data.setSignerSurname(trimString(_rs.getString(_pos++)));
			_data.setNote(trimString(_rs.getString(_pos++)));
			_data.setCorrectionNote(trimString(_rs.getString(_pos++)));
			_data.setMasterSignerName(trimString(_rs.getString(_pos++)));
			_data.setMasterSignerSurname(trimString(_rs.getString(_pos++)));
			_data.setMasterSignature(trimString(_rs.getString(_pos++)));
			_data.setMasterSigDate(_rs.getDate(_pos++));
			_data.setSignatureGroupUuid(trimString(_rs.getString(_pos++)));
			_data.setAttachmentName(trimString(_rs.getString(_pos++)));
			_data.setAttachmentData(_rs.getBytes(_pos++));
			Timestamp businessDateFromtimestampValue = _dt.getTimestampFromResultSet(_rs, _pos++, MithraTimestamp.DefaultTimeZone);
			_data.setBusinessDateFrom(businessDateFromtimestampValue);
			Timestamp businessDateTotimestampValue = _dt.getTimestampFromResultSet(_rs, _pos++, MithraTimestamp.DefaultTimeZone);
			if (businessDateTotimestampValue == null)
			{
				throw new MithraBusinessException("attribute ' businessDateTo ' is null in database but is not marked as nullable in mithra xml for primary key / "+_data.zGetPrintablePrimaryKey());
			}

			businessDateTotimestampValue = MithraTimestamp.zFixInfinity(businessDateTotimestampValue, MithraTimestamp.DefaultTimeZone,
				OilLogFinder.businessDate().getInfinityDate());
			_data.setBusinessDateTo(businessDateTotimestampValue);
			Timestamp processingDateFromtimestampValue = _dt.getTimestampFromResultSet(_rs, _pos++, MithraTimestamp.DefaultTimeZone);
			_data.setProcessingDateFrom(processingDateFromtimestampValue);
			Timestamp processingDateTotimestampValue = _dt.getTimestampFromResultSet(_rs, _pos++, MithraTimestamp.DefaultTimeZone);
			if (processingDateTotimestampValue == null)
			{
				throw new MithraBusinessException("attribute ' processingDateTo ' is null in database but is not marked as nullable in mithra xml for primary key / "+_data.zGetPrintablePrimaryKey());
			}

			processingDateTotimestampValue = MithraTimestamp.zFixInfinity(processingDateTotimestampValue, MithraTimestamp.DefaultTimeZone,
				OilLogFinder.processingDate().getInfinityDate());
			_data.setProcessingDateTo(processingDateTotimestampValue);
		}
	}

	public DatabaseType getDatabaseType()
	{
		return connectionManager.getDatabaseType();
	}

	public TimeZone getDatabaseTimeZone()
	{
		return connectionManager.getDatabaseTimeZone();
	}

	protected String getSchema()
	{
		return this.getSchemaGenericSource(null);
	}

	public String getFullyQualifiedTableName()
	{
		String schema = this.getSchemaGenericSource(null);
		String tableName = getOilLogTableName();
		return this.getDatabaseType().getFullyQualifiedTableName(schema, tableName);
	}

	public void setSchemaManager(Object schemaManager)
	{
		if( schemaManager instanceof SchemaManager )
		{
			this.schemaManager = (SchemaManager) schemaManager;
		}
		else
		{
			throw new IllegalArgumentException( "Schema manager class " + schemaManager.getClass().getName()
			+ " does not implement SchemaManager.class" );
		}
	}

	public void setTablePartitionManager(Object tablePartitionManager)
	{
		if( tablePartitionManager instanceof TablePartitionManager )
		{
			this.tablePartitionManager = (TablePartitionManager) tablePartitionManager;
		}
		else
		{
			throw new IllegalArgumentException( "Table partition manager class " + tablePartitionManager.getClass().getName()
			+ " does not implement TablePartitionManager.class" );
		}
	}

	public String getTableName()
	{
		return this.getDefaultTableName();
	}

	public String getDefaultTableName()
	{
		return "OCEAN_LOGBOOK_OPERATION_OIL_LOG";
	}

	public void setInsertAttributes(PreparedStatement stm, MithraDataObject dataObj,
		TimeZone databaseTimeZone, int pos, DatabaseType dt) throws SQLException
	{
		OilLogData data = (OilLogData)dataObj;
		TimeZone conversionTimeZone = null;
		stm.setLong(pos++, data.getId());
		if (data.isUuidNull())
		{
			stm.setNull(pos++, java.sql.Types.VARCHAR);
		}
		else
		{
			stm.setString(pos++, data.getUuid());
		}

		if (data.isShipNull())
		{
			stm.setNull(pos++, java.sql.Types.BIGINT);
		}
		else
		{
			stm.setLong(pos++, data.getShip());
		}

		if(data.isTypeNull())
		{
			throwNullAttribute("type");
		}

		stm.setString(pos++, data.getType());
		if(data.isCodeLetterNull())
		{
			throwNullAttribute("codeLetter");
		}

		stm.setString(pos++, data.getCodeLetter());
		if(data.isCodeNumberNull())
		{
			throwNullAttribute("codeNumber");
		}

		stm.setString(pos++, data.getCodeNumber());
		if(data.isContentNull())
		{
			throwNullAttribute("content");
		}

		stm.setString(pos++, data.getContent());
		if (data.isSignatureNull())
		{
			stm.setNull(pos++, java.sql.Types.VARCHAR);
		}
		else
		{
			stm.setString(pos++, data.getSignature());
		}

		if(data.isGroupUuidNull())
		{
			throwNullAttribute("groupUuid");
		}

		stm.setString(pos++, data.getGroupUuid());
		if (data.isOrderInGroupNull())
		{
			stm.setNull(pos++, java.sql.Types.VARCHAR);
		}
		else
		{
			stm.setString(pos++, data.getOrderInGroup());
		}

		if (data.isSignerRankNull())
		{
			stm.setNull(pos++, java.sql.Types.VARCHAR);
		}
		else
		{
			stm.setString(pos++, data.getSignerRank());
		}

		if (data.isSignerNameNull())
		{
			stm.setNull(pos++, java.sql.Types.VARCHAR);
		}
		else
		{
			stm.setString(pos++, data.getSignerName());
		}

		if (data.isSignerSurnameNull())
		{
			stm.setNull(pos++, java.sql.Types.VARCHAR);
		}
		else
		{
			stm.setString(pos++, data.getSignerSurname());
		}

		if (data.isNoteNull())
		{
			stm.setNull(pos++, java.sql.Types.VARCHAR);
		}
		else
		{
			stm.setString(pos++, data.getNote());
		}

		if (data.isCorrectionNoteNull())
		{
			stm.setNull(pos++, java.sql.Types.VARCHAR);
		}
		else
		{
			stm.setString(pos++, data.getCorrectionNote());
		}

		if (data.isMasterSignerNameNull())
		{
			stm.setNull(pos++, java.sql.Types.VARCHAR);
		}
		else
		{
			stm.setString(pos++, data.getMasterSignerName());
		}

		if (data.isMasterSignerSurnameNull())
		{
			stm.setNull(pos++, java.sql.Types.VARCHAR);
		}
		else
		{
			stm.setString(pos++, data.getMasterSignerSurname());
		}

		if (data.isMasterSignatureNull())
		{
			stm.setNull(pos++, java.sql.Types.VARCHAR);
		}
		else
		{
			stm.setString(pos++, data.getMasterSignature());
		}

		if (data.isMasterSigDateNull())
		{
			stm.setNull(pos++, java.sql.Types.DATE);
		}
		else
		{
			stm.setDate(pos++, data.getMasterSigDate());
		}

		if (data.isSignatureGroupUuidNull())
		{
			stm.setNull(pos++, java.sql.Types.VARCHAR);
		}
		else
		{
			stm.setString(pos++, data.getSignatureGroupUuid());
		}

		if (data.isAttachmentNameNull())
		{
			stm.setNull(pos++, java.sql.Types.VARCHAR);
		}
		else
		{
			stm.setString(pos++, data.getAttachmentName());
		}

		if (data.isAttachmentDataNull())
		{
			stm.setNull(pos++, java.sql.Types.VARBINARY);
		}
		else
		{
			stm.setBytes(pos++, data.getAttachmentData());
		}

		if(data.isBusinessDateFromNull())
		{
			throwNullAttribute("businessDateFrom");
		}

		conversionTimeZone = MithraTimestamp.DefaultTimeZone;
		dt.setTimestamp(stm, pos, data.getBusinessDateFrom(), false, conversionTimeZone);
		pos++;
		if(data.isBusinessDateToNull())
		{
			throwNullAttribute("businessDateTo");
		}

		conversionTimeZone = MithraTimestamp.DefaultTimeZone;
		if (data.getBusinessDateTo().getTime() == OilLogFinder.businessDate().getInfinityDate().getTime())
		{
			conversionTimeZone = MithraTimestamp.DefaultTimeZone;
		}

		dt.setTimestamp(stm, pos, data.getBusinessDateTo(), false, conversionTimeZone);
		pos++;
		if(data.isProcessingDateFromNull())
		{
			throwNullAttribute("processingDateFrom");
		}

		conversionTimeZone = MithraTimestamp.DefaultTimeZone;
		dt.setTimestamp(stm, pos, data.getProcessingDateFrom(), false, conversionTimeZone);
		pos++;
		if(data.isProcessingDateToNull())
		{
			throwNullAttribute("processingDateTo");
		}

		conversionTimeZone = MithraTimestamp.DefaultTimeZone;
		if (data.getProcessingDateTo().getTime() == OilLogFinder.processingDate().getInfinityDate().getTime())
		{
			conversionTimeZone = MithraTimestamp.DefaultTimeZone;
		}

		dt.setTimestamp(stm, pos, data.getProcessingDateTo(), false, conversionTimeZone);
		pos++;
	}

	public String getInsertFields()
	{
		return "ID,UUID,SHIP_ID,TYPE,CODELETTER,CODENUMBER,CONTENT,SIGNATURE,GROUPUUID,ORDERINGROUP,SIGNERRANK,SIGNERNAME,SIGNERSURNAME,NOTE,CORRECTIONNOTE,MASTERSIGNERNAME,MASTERSIGNERSURNAME,MASTERSIGNATURE,MASTERSIGDATE,SIGNATUREGROUPUUID,ATTACHMENTNAME,ATTACHMENTDATA,FROM_Z,THRU_Z,IN_Z,OUT_Z";
	}

	public String getInsertQuestionMarks()
	{
		return "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
	}

	public String getOptimisticLockingWhereSql()
	{
		return "AND IN_Z = ?";
	}

	public Timestamp[] getAsOfDates() 
	{
		return new Timestamp[2];
	}

	public MithraDatedObject createObject(MithraDataObject data, Timestamp[] asOfDates)
	{
		OilLog newObject = new OilLog(asOfDates[0]
		, asOfDates[1]
		);
		newObject.zSetFromOilLogData((OilLogData) data);
		return newObject;
	}

	public TemporalContainer createContainer(MithraTransaction tx)
	{
		return new BiTemporalTransactionalDataContainer(tx.getPerPortalTemporalContainer(OilLogFinder.getMithraObjectPortal(),
			OilLogFinder.processingDate())
		,OilLogFinder.businessDate());
	}

	public String getAsOfAttributeWhereSql(MithraDataObject data) 
	{
		String result = "";
		result += " AND ";
		result +="THRU_Z = ?";
		result += " AND ";
		result +="OUT_Z = ?";
		return result;
	}

	public int setPrimaryKeyAttributesWithoutDates(PreparedStatement stm, int pos, MithraDataObject dataObject, TimeZone databaseTimeZone, DatabaseType dt) throws SQLException
	{
		OilLogData data = (OilLogData)dataObject;
		TimeZone conversionTimeZone = null;
		stm.setLong(pos++, data.getId());
		return pos;
	}

	public List getForDateRange(MithraDataObject obj, Timestamp start, Timestamp end) throws MithraDatabaseException
	{
		return this.getForDateRange(obj, start, end, OilLogFinder.businessDate(),
			OilLogFinder.processingDate());
	}
}
