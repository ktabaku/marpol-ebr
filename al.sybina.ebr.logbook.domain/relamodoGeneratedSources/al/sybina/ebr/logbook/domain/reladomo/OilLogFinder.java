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
import com.gs.fw.common.mithra.*;
import com.gs.fw.common.mithra.attribute.*;
import com.gs.fw.common.mithra.attribute.calculator.procedure.ObjectProcedure;
import com.gs.fw.common.mithra.behavior.txparticipation.*;
import com.gs.fw.common.mithra.cache.Cache;
import com.gs.fw.common.mithra.cache.bean.*;
import com.gs.fw.common.mithra.extractor.*;
import com.gs.fw.common.mithra.finder.*;
import com.gs.fw.common.mithra.finder.booleanop.*;
import com.gs.fw.common.mithra.finder.integer.*;
import com.gs.fw.common.mithra.finder.longop.*;
import com.gs.fw.common.mithra.finder.orderby.OrderBy;
import com.gs.fw.common.mithra.finder.string.*;
import com.gs.fw.common.mithra.extractor.NormalAndListValueSelector;
import com.gs.fw.common.mithra.list.NulledRelation;
import com.gs.fw.common.mithra.querycache.CachedQuery;
import com.gs.fw.common.mithra.querycache.QueryCache;
import com.gs.fw.common.mithra.portal.*;
import com.gs.fw.common.mithra.remote.*;
import com.gs.fw.common.mithra.transaction.MithraObjectPersister;
import com.gs.fw.common.mithra.util.TimestampPool;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import java.io.Serializable;
/**
* This file was automatically generated using Mithra 17.1.4. Please do not modify it.
* Add custom logic to its subclass instead.
*/
public class OilLogFinder
{
	private static final Timestamp businessDateInfinity = TimestampPool.getInstance().getOrAddToCache(al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate(), true);
	private static final Timestamp businessDateDefault = TimestampPool.getInstance().getOrAddToCache(null, true);
	private static final Timestamp processingDateInfinity = TimestampPool.getInstance().getOrAddToCache(al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate(), true);
	private static final Timestamp processingDateDefault = TimestampPool.getInstance().getOrAddToCache(al.sybina.ebr.logbook.domain.reladomo.TimestampProvider.getInfinityDate(), true);
	private static final String IMPL_CLASS_NAME_WITH_SLASHES = "al/sybina/ebr/logbook/domain/reladomo/OilLog";
	private static final String BUSINESS_CLASS_NAME_WITH_DOTS = "al.sybina.ebr.logbook.domain.reladomo.OilLog";
	private static final FinderMethodMap finderMethodMap;
	private static boolean isFullCache;
	private static boolean isOffHeap;
	private static volatile MithraObjectPortal objectPortal = new UninitializedPortal("al.sybina.ebr.logbook.domain.reladomo.OilLog");
	private static final OilLogSingleFinder<OilLog, Object, OilLog> finder = new OilLogSingleFinder<OilLog, Object, OilLog>();
	private static ConstantStringSet[] constantStringSets = new ConstantStringSet[0];
	private static ConstantIntSet[] constantIntSets = new ConstantIntSet[0];
	private static ConstantShortSet[] constantShortSets = new ConstantShortSet[0];
	static
	{
		finderMethodMap = new FinderMethodMap(OilLogFinder.OilLogRelatedFinder.class);
		finderMethodMap.addNormalAttributeName("id");
		finderMethodMap.addNormalAttributeName("uuid");
		finderMethodMap.addNormalAttributeName("ship");
		finderMethodMap.addNormalAttributeName("type");
		finderMethodMap.addNormalAttributeName("codeLetter");
		finderMethodMap.addNormalAttributeName("codeNumber");
		finderMethodMap.addNormalAttributeName("content");
		finderMethodMap.addNormalAttributeName("signature");
		finderMethodMap.addNormalAttributeName("groupUuid");
		finderMethodMap.addNormalAttributeName("orderInGroup");
		finderMethodMap.addNormalAttributeName("signerRank");
		finderMethodMap.addNormalAttributeName("signerName");
		finderMethodMap.addNormalAttributeName("signerSurname");
		finderMethodMap.addNormalAttributeName("note");
		finderMethodMap.addNormalAttributeName("correctionNote");
		finderMethodMap.addNormalAttributeName("masterSignerName");
		finderMethodMap.addNormalAttributeName("masterSignerSurname");
		finderMethodMap.addNormalAttributeName("masterSignature");
		finderMethodMap.addNormalAttributeName("masterSigDate");
		finderMethodMap.addNormalAttributeName("signatureGroupUuid");
		finderMethodMap.addNormalAttributeName("attachmentName");
		finderMethodMap.addNormalAttributeName("attachmentData");
		finderMethodMap.addNormalAttributeName("businessDateFrom");
		finderMethodMap.addNormalAttributeName("businessDateTo");
		finderMethodMap.addNormalAttributeName("processingDateFrom");
		finderMethodMap.addNormalAttributeName("processingDateTo");
		finderMethodMap.addNormalAttributeName("businessDate");
		finderMethodMap.addNormalAttributeName("processingDate");
	}

	public static Attribute[] allPersistentAttributes()
	{
		return finder.getPersistentAttributes();
	}

	public static List<RelatedFinder> allRelatedFinders()
	{
		return finder.getRelationshipFinders();
	}

	public static List<RelatedFinder> allDependentRelatedFinders()
	{
		return finder.getDependentRelationshipFinders();
	}

	public static ConstantStringSet zGetConstantStringSet(int index)
	{
		return constantStringSets[index];
	}

	public static ConstantIntSet zGetConstantIntSet(int index)
	{
		return constantIntSets[index];
	}

	public static ConstantShortSet zGetConstantShortSet(int index)
	{
		return constantShortSets[index];
	}

	public static SourceAttributeType getSourceAttributeType()
	{
		return null;
	}

	public static OilLog findOne(com.gs.fw.finder.Operation operation)
	{
		return findOne(operation, false);
	}

	public static OilLog findOneBypassCache(com.gs.fw.finder.Operation operation)
	{
		return findOne(operation, true);
	}

	public static OilLogList findMany(com.gs.fw.finder.Operation operation)
	{
		return (OilLogList) finder.findMany(operation);
	}

	public static OilLogList findManyBypassCache(com.gs.fw.finder.Operation operation)
	{
		return (OilLogList) finder.findManyBypassCache(operation);
	}

	private static OilLog findOne(com.gs.fw.finder.Operation operation, boolean bypassCache)
	{
		List found = getMithraObjectPortal().find((Operation) operation, bypassCache);
		return (OilLog) FinderUtils.findOne(found);
	}

	public static OilLog findByPrimaryKey(long id, Timestamp businessDate, Timestamp processingDate)
	{
		return finder.findByPrimaryKey(id, businessDate, processingDate);
	}

	private static final RelationshipHashStrategy forPrimaryKey = new PrimaryKeyRhs();
	private static final class PrimaryKeyRhs implements RelationshipHashStrategy
	{
		public boolean equalsForRelationship(Object _srcObject, Object _srcData, Object _targetData, Timestamp _asOfDate0, Timestamp _asOfDate1)
		{
			I3O3L3 _bean = (I3O3L3) _srcData;
			OilLogData _castedTargetData = (OilLogData) _targetData;
			if (_bean.getL1AsLong() == _castedTargetData.getId())
			{
				return OilLogFinder.businessDate().dataMatches(_castedTargetData, _asOfDate0) && OilLogFinder.processingDate().dataMatches(_castedTargetData, _asOfDate1);
			}

			return false;
		}

		public int computeHashCodeFromRelated(Object _srcObject, Object _srcData)
		{
			I3O3L3 _bean = (I3O3L3) _srcData;
			return HashUtil.hash(_bean.getL1AsLong());
		}

		public int computeOffHeapHashCodeFromRelated(Object _srcObject, Object _srcData)
		{
			I3O3L3 _bean = (I3O3L3) _srcData;
			return HashUtil.hash(_bean.getL1AsLong());
		}
	}

	public static OilLog zFindOneForRelationship(Operation operation)
	{
		List found = getMithraObjectPortal().findAsCachedQuery(operation, null, false, true, 0).getResult();
		return (OilLog) FinderUtils.findOne(found);
	}

	public static MithraObjectPortal getMithraObjectPortal()
	{
		return objectPortal.getInitializedPortal();
	}

	public static void clearQueryCache()
	{
		objectPortal.clearQueryCache();
	}

	public static void reloadCache()
	{
		objectPortal.reloadCache();
	}

	public static class OilLogRelatedFinder<ParentOwnerType, ReturnType, ReturnListType extends List, OwnerType> extends AbstractRelatedFinder<OilLog, ParentOwnerType, ReturnType, ReturnListType, OwnerType>
	{
		private List<RelatedFinder> relationshipFinders;
		private List<RelatedFinder> dependentRelationshipFinders;
		private LongAttribute<ParentOwnerType> id;
		private StringAttribute<ParentOwnerType> uuid;
		private LongAttribute<ParentOwnerType> ship;
		private StringAttribute<ParentOwnerType> type;
		private StringAttribute<ParentOwnerType> codeLetter;
		private StringAttribute<ParentOwnerType> codeNumber;
		private StringAttribute<ParentOwnerType> content;
		private StringAttribute<ParentOwnerType> signature;
		private StringAttribute<ParentOwnerType> groupUuid;
		private StringAttribute<ParentOwnerType> orderInGroup;
		private StringAttribute<ParentOwnerType> signerRank;
		private StringAttribute<ParentOwnerType> signerName;
		private StringAttribute<ParentOwnerType> signerSurname;
		private StringAttribute<ParentOwnerType> note;
		private StringAttribute<ParentOwnerType> correctionNote;
		private StringAttribute<ParentOwnerType> masterSignerName;
		private StringAttribute<ParentOwnerType> masterSignerSurname;
		private StringAttribute<ParentOwnerType> masterSignature;
		private DateAttribute<ParentOwnerType> masterSigDate;
		private StringAttribute<ParentOwnerType> signatureGroupUuid;
		private StringAttribute<ParentOwnerType> attachmentName;
		private ByteArrayAttribute<ParentOwnerType> attachmentData;
		private TimestampAttribute<ParentOwnerType> businessDateFrom;
		private TimestampAttribute<ParentOwnerType> businessDateTo;
		private TimestampAttribute<ParentOwnerType> processingDateFrom;
		private TimestampAttribute<ParentOwnerType> processingDateTo;
		private AsOfAttribute<ParentOwnerType> businessDate;
		private AsOfAttribute<ParentOwnerType> processingDate;
		private transient AsOfAttribute[] asOfAttributes;
		public synchronized AsOfAttribute[] getAsOfAttributes()
		{
			if (asOfAttributes == null)
			{
				asOfAttributes = new AsOfAttribute[2];
				asOfAttributes[0] = this.businessDate();
				asOfAttributes[1] = this.processingDate();
			}

			return this.asOfAttributes;
		}

		public OilLogRelatedFinder()
		{
			super();
		}

		public OilLogRelatedFinder(Mapper mapper)
		{
			super(mapper);
		}

		public String getFinderClassName()
		{
			return "al.sybina.ebr.logbook.domain.reladomo.OilLogFinder";
		}

		public RelatedFinder getRelationshipFinderByName(String relationshipName)
		{
			return OilLogFinder.finderMethodMap.getRelationshipFinderByName(relationshipName, this);
		}

		public Attribute getAttributeByName(String attributeName)
		{
			return OilLogFinder.finderMethodMap.getAttributeByName(attributeName, this);
		}

		public com.gs.fw.common.mithra.extractor.Function getAttributeOrRelationshipSelector(String attributeName)
		{
			return OilLogFinder.finderMethodMap.getAttributeOrRelationshipSelectorFunction(attributeName, this);
		}

		public Attribute[] getPersistentAttributes()
		{
			Attribute[] attributes = new Attribute[26];
			attributes[0] = this.id();
			attributes[1] = this.uuid();
			attributes[2] = this.ship();
			attributes[3] = this.type();
			attributes[4] = this.codeLetter();
			attributes[5] = this.codeNumber();
			attributes[6] = this.content();
			attributes[7] = this.signature();
			attributes[8] = this.groupUuid();
			attributes[9] = this.orderInGroup();
			attributes[10] = this.signerRank();
			attributes[11] = this.signerName();
			attributes[12] = this.signerSurname();
			attributes[13] = this.note();
			attributes[14] = this.correctionNote();
			attributes[15] = this.masterSignerName();
			attributes[16] = this.masterSignerSurname();
			attributes[17] = this.masterSignature();
			attributes[18] = this.masterSigDate();
			attributes[19] = this.signatureGroupUuid();
			attributes[20] = this.attachmentName();
			attributes[21] = this.attachmentData();
			attributes[22] = this.businessDateFrom();
			attributes[23] = this.businessDateTo();
			attributes[24] = this.processingDateFrom();
			attributes[25] = this.processingDateTo();
			return attributes;
		}

		public List<RelatedFinder> getRelationshipFinders()
		{
			if (relationshipFinders == null)
			{
				List<RelatedFinder> relatedFinders = new ArrayList<RelatedFinder>(0);
				relationshipFinders = Collections.unmodifiableList(relatedFinders);
			}

			return relationshipFinders;
		}

		public List<RelatedFinder> getDependentRelationshipFinders()
		{
			if (dependentRelationshipFinders == null)
			{
				List<RelatedFinder> dependentRelatedFinders = new ArrayList<RelatedFinder>(0);
				dependentRelationshipFinders = Collections.unmodifiableList(dependentRelatedFinders);
			}

			return dependentRelationshipFinders;
		}

		public OilLog findOne(com.gs.fw.finder.Operation operation)
		{
			return OilLogFinder.findOne(operation, false);
		}

		public OilLog findOneBypassCache(com.gs.fw.finder.Operation operation)
		{
			return OilLogFinder.findOne(operation, true);
		}

		public MithraList<? extends OilLog> findMany(com.gs.fw.finder.Operation operation)
		{
			return new OilLogList((Operation) operation);
		}

		public MithraList<? extends OilLog> findManyBypassCache(com.gs.fw.finder.Operation operation)
		{
			OilLogList result = (OilLogList) this.findMany(operation);
			result.setBypassCache(true);
			return result;
		}

		public MithraList<? extends OilLog> constructEmptyList()
		{
			return new OilLogList();
		}

		public int getSerialVersionId()
		{
			return -1359183665;
		}

		public boolean isPure()
		{
			return false;
		}

		public boolean isTemporary()
		{
			return false;
		}

		public int getHierarchyDepth()
		{
			return 0;
		}

		public LongAttribute<ParentOwnerType> id()
		{
			LongAttribute<ParentOwnerType> result = this.id;
			if (result == null)
			{
				result = mapper == null ? SingleColumnLongAttribute.generate("ID","","id",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,false,true,this,null,true,false,false,-1,-1,-1,false, false) :
					new MappedLongAttribute(OilLogFinder.id(), this.mapper, this.zGetValueSelector());
				this.id = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> uuid()
		{
			StringAttribute<ParentOwnerType> result = this.uuid;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("UUID","","uuid",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.uuid(), this.mapper, this.zGetValueSelector());
				this.uuid = result;
			}

			return result;
		}

		public LongAttribute<ParentOwnerType> ship()
		{
			LongAttribute<ParentOwnerType> result = this.ship;
			if (result == null)
			{
				result = mapper == null ? SingleColumnLongAttribute.generate("SHIP_ID","","ship",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,false,-1,-1,-1,false, false) :
					new MappedLongAttribute(OilLogFinder.ship(), this.mapper, this.zGetValueSelector());
				this.ship = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> type()
		{
			StringAttribute<ParentOwnerType> result = this.type;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("TYPE","","type",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,false,true,this,null,true,false,-1,-1,-1,4,true, false) :
					new MappedStringAttribute(OilLogFinder.type(), this.mapper, this.zGetValueSelector());
				this.type = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> codeLetter()
		{
			StringAttribute<ParentOwnerType> result = this.codeLetter;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("CODELETTER","","codeLetter",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,false,true,this,null,true,false,-1,-1,-1,1,true, false) :
					new MappedStringAttribute(OilLogFinder.codeLetter(), this.mapper, this.zGetValueSelector());
				this.codeLetter = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> codeNumber()
		{
			StringAttribute<ParentOwnerType> result = this.codeNumber;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("CODENUMBER","","codeNumber",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,false,true,this,null,true,false,-1,-1,-1,8,true, false) :
					new MappedStringAttribute(OilLogFinder.codeNumber(), this.mapper, this.zGetValueSelector());
				this.codeNumber = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> content()
		{
			StringAttribute<ParentOwnerType> result = this.content;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("CONTENT","","content",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,false,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.content(), this.mapper, this.zGetValueSelector());
				this.content = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> signature()
		{
			StringAttribute<ParentOwnerType> result = this.signature;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("SIGNATURE","","signature",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.signature(), this.mapper, this.zGetValueSelector());
				this.signature = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> groupUuid()
		{
			StringAttribute<ParentOwnerType> result = this.groupUuid;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("GROUPUUID","","groupUuid",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,false,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.groupUuid(), this.mapper, this.zGetValueSelector());
				this.groupUuid = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> orderInGroup()
		{
			StringAttribute<ParentOwnerType> result = this.orderInGroup;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("ORDERINGROUP","","orderInGroup",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.orderInGroup(), this.mapper, this.zGetValueSelector());
				this.orderInGroup = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> signerRank()
		{
			StringAttribute<ParentOwnerType> result = this.signerRank;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("SIGNERRANK","","signerRank",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.signerRank(), this.mapper, this.zGetValueSelector());
				this.signerRank = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> signerName()
		{
			StringAttribute<ParentOwnerType> result = this.signerName;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("SIGNERNAME","","signerName",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.signerName(), this.mapper, this.zGetValueSelector());
				this.signerName = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> signerSurname()
		{
			StringAttribute<ParentOwnerType> result = this.signerSurname;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("SIGNERSURNAME","","signerSurname",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.signerSurname(), this.mapper, this.zGetValueSelector());
				this.signerSurname = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> note()
		{
			StringAttribute<ParentOwnerType> result = this.note;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("NOTE","","note",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.note(), this.mapper, this.zGetValueSelector());
				this.note = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> correctionNote()
		{
			StringAttribute<ParentOwnerType> result = this.correctionNote;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("CORRECTIONNOTE","","correctionNote",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.correctionNote(), this.mapper, this.zGetValueSelector());
				this.correctionNote = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> masterSignerName()
		{
			StringAttribute<ParentOwnerType> result = this.masterSignerName;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("MASTERSIGNERNAME","","masterSignerName",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.masterSignerName(), this.mapper, this.zGetValueSelector());
				this.masterSignerName = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> masterSignerSurname()
		{
			StringAttribute<ParentOwnerType> result = this.masterSignerSurname;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("MASTERSIGNERSURNAME","","masterSignerSurname",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.masterSignerSurname(), this.mapper, this.zGetValueSelector());
				this.masterSignerSurname = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> masterSignature()
		{
			StringAttribute<ParentOwnerType> result = this.masterSignature;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("MASTERSIGNATURE","","masterSignature",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.masterSignature(), this.mapper, this.zGetValueSelector());
				this.masterSignature = result;
			}

			return result;
		}

		public DateAttribute<ParentOwnerType> masterSigDate()
		{
			DateAttribute<ParentOwnerType> result = this.masterSigDate;
			if (result == null)
			{
				result = mapper == null ? SingleColumnDateAttribute.generate("MASTERSIGDATE","","masterSigDate",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,false, false) :
					new MappedDateAttribute(OilLogFinder.masterSigDate(), this.mapper, this.zGetValueSelector());
				this.masterSigDate = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> signatureGroupUuid()
		{
			StringAttribute<ParentOwnerType> result = this.signatureGroupUuid;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("SIGNATUREGROUPUUID","","signatureGroupUuid",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.signatureGroupUuid(), this.mapper, this.zGetValueSelector());
				this.signatureGroupUuid = result;
			}

			return result;
		}

		public StringAttribute<ParentOwnerType> attachmentName()
		{
			StringAttribute<ParentOwnerType> result = this.attachmentName;
			if (result == null)
			{
				result = mapper == null ? SingleColumnStringAttribute.generate("ATTACHMENTNAME","","attachmentName",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE,true, false) :
					new MappedStringAttribute(OilLogFinder.attachmentName(), this.mapper, this.zGetValueSelector());
				this.attachmentName = result;
			}

			return result;
		}

		public ByteArrayAttribute<ParentOwnerType> attachmentData()
		{
			ByteArrayAttribute<ParentOwnerType> result = this.attachmentData;
			if (result == null)
			{
				result = mapper == null ? SingleColumnByteArrayAttribute.generate("ATTACHMENTDATA","","attachmentData",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,true,true,this,null,true,false,-1,-1,-1,Integer.MAX_VALUE, false) :
					new MappedByteArrayAttribute(OilLogFinder.attachmentData(), this.mapper, this.zGetValueSelector());
				this.attachmentData = result;
			}

			return result;
		}

		public TimestampAttribute<ParentOwnerType> businessDateFrom()
		{
			TimestampAttribute<ParentOwnerType> result = this.businessDateFrom;
			if (result == null)
			{
				result = mapper == null ? SingleColumnTimestampAttribute.generate("FROM_Z","","businessDateFrom",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,false,true,this,null,true,false,-1,-1,-1,TimestampAttribute.CONVERT_NONE,false,false,OilLogFinder.businessDateInfinity, (byte) 0, false) :
					new MappedTimestampAttribute(OilLogFinder.businessDateFrom(), this.mapper, this.zGetValueSelector());
				this.businessDateFrom = result;
			}

			return result;
		}

		public TimestampAttribute<ParentOwnerType> businessDateTo()
		{
			TimestampAttribute<ParentOwnerType> result = this.businessDateTo;
			if (result == null)
			{
				result = mapper == null ? SingleColumnTimestampAttribute.generate("THRU_Z","","businessDateTo",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,false,true,this,null,true,false,-1,-1,-1,TimestampAttribute.CONVERT_NONE,false,true,OilLogFinder.businessDateInfinity, (byte) 0, false) :
					new MappedTimestampAttribute(OilLogFinder.businessDateTo(), this.mapper, this.zGetValueSelector());
				this.businessDateTo = result;
			}

			return result;
		}

		public TimestampAttribute<ParentOwnerType> processingDateFrom()
		{
			TimestampAttribute<ParentOwnerType> result = this.processingDateFrom;
			if (result == null)
			{
				result = mapper == null ? SingleColumnTimestampAttribute.generate("IN_Z","","processingDateFrom",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,false,true,this,null,true,false,-1,-1,-1,TimestampAttribute.CONVERT_NONE,false,false,OilLogFinder.processingDateInfinity, (byte) 0, false) :
					new MappedTimestampAttribute(OilLogFinder.processingDateFrom(), this.mapper, this.zGetValueSelector());
				this.processingDateFrom = result;
			}

			return result;
		}

		public TimestampAttribute<ParentOwnerType> processingDateTo()
		{
			TimestampAttribute<ParentOwnerType> result = this.processingDateTo;
			if (result == null)
			{
				result = mapper == null ? SingleColumnTimestampAttribute.generate("OUT_Z","","processingDateTo",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,false,true,this,null,true,false,-1,-1,-1,TimestampAttribute.CONVERT_NONE,false,true,OilLogFinder.processingDateInfinity, (byte) 0, false) :
					new MappedTimestampAttribute(OilLogFinder.processingDateTo(), this.mapper, this.zGetValueSelector());
				this.processingDateTo = result;
			}

			return result;
		}

		public AsOfAttribute<ParentOwnerType> businessDate()
		{
			AsOfAttribute<ParentOwnerType> result = this.businessDate;
			if (result == null)
			{
				result = mapper == null ? AsOfAttribute.generate("businessDate",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,false,true,this,null,true,false,this.businessDateFrom(),this.businessDateTo(),OilLogFinder.businessDateInfinity,true,false,OilLogFinder.businessDateDefault,false,false) :
					new MappedAsOfAttribute(OilLogFinder.businessDate(), this.mapper, this.zGetValueSelector());
				this.businessDate = result;
			}

			return result;
		}

		public AsOfAttribute<ParentOwnerType> processingDate()
		{
			AsOfAttribute<ParentOwnerType> result = this.processingDate;
			if (result == null)
			{
				result = mapper == null ? AsOfAttribute.generate("processingDate",BUSINESS_CLASS_NAME_WITH_DOTS,IMPL_CLASS_NAME_WITH_SLASHES,false,true,this,null,true,false,this.processingDateFrom(),this.processingDateTo(),OilLogFinder.processingDateInfinity,false,false,OilLogFinder.processingDateDefault,true,false) :
					new MappedAsOfAttribute(OilLogFinder.processingDate(), this.mapper, this.zGetValueSelector());
				this.processingDate = result;
			}

			return result;
		}

		public Attribute getSourceAttribute()
		{
			return null;
		}

		private Mapper combineWithMapperIfExists(Mapper newMapper)
		{
			if (this.mapper != null)
			{
				return new LinkedMapper(this.mapper, newMapper);
			}

			return newMapper;
		}

		public Attribute[] getPrimaryKeyAttributes()
		{
			return OilLogFinder.getPrimaryKeyAttributes();
		}

		public VersionAttribute getVersionAttribute()
		{
			return null;
		}

		public MithraObjectPortal getMithraObjectPortal()
		{
			return OilLogFinder.getMithraObjectPortal();
		}
	}

	public static class OilLogCollectionFinder<ParentOwnerType, ReturnType extends List, OwnerType> extends OilLogRelatedFinder<ParentOwnerType, ReturnType, OilLogList, OwnerType>
	{
		public OilLogCollectionFinder(Mapper mapper)
		{
			super(mapper);
		}
	}

	public static abstract class OilLogCollectionFinderForRelatedClasses<ParentOwnerType, ReturnType extends List, OwnerType>
	extends OilLogCollectionFinder<ParentOwnerType, ReturnType, OwnerType>
	implements DeepRelationshipAttribute<ParentOwnerType, ReturnType>
	{
		public OilLogCollectionFinderForRelatedClasses(Mapper mapper)
		{
			super(mapper);
		}

		protected NormalAndListValueSelector zGetValueSelector()
		{
			return this;
		}
	}

	public static class OilLogSingleFinder<ParentOwnerType, ReturnType, OwnerType> extends OilLogRelatedFinder<ParentOwnerType, ReturnType, OilLogList, OwnerType>
	implements ToOneFinder
	{
		public OilLogSingleFinder(Mapper mapper)
		{
			super(mapper);
		}

		public OilLogSingleFinder()
		{
			super(null);
		}

		public Operation eq(OilLog other)
		{
			return this.id().eq(other.getId())
			;
		}
		// this implementation uses private API. Do NOT copy to application code. Application code must use normal operations for lookups.
		public OilLog findByPrimaryKey(long id, Timestamp businessDate, Timestamp processingDate)
		{
			OilLog _result = null;
			Operation _op = null;
			Object _related = null;
			{
				I3O3L3 _bean = I3O3L3.POOL.getOrConstruct();
				_bean.setL1AsLong(id);
				MithraObjectPortal _portal = this.getMithraObjectPortal();
				_related = _portal.getAsOneFromCacheForFind(_bean, _bean, forPrimaryKey, businessDate, processingDate);
				_bean.release();
			}

			if (!(_related instanceof NulledRelation)) _result = (OilLog) _related;
			if (_related == null)
			{
				_op = this.id().eq(id).and(this.businessDate().eq(businessDate)).and(this.processingDate().eq(processingDate));
			}

			if (_op != null)
			{
				_result = this.findOne(_op);
			}

			return _result;
		}
	}

	public static abstract class OilLogSingleFinderForRelatedClasses<ParentOwnerType, ReturnType, OwnerType> extends OilLogSingleFinder<ParentOwnerType, ReturnType, OwnerType> implements DeepRelationshipAttribute<ParentOwnerType, ReturnType>
	{
		public OilLogSingleFinderForRelatedClasses(Mapper mapper)
		{
			super(mapper);
		}

		protected NormalAndListValueSelector zGetValueSelector()
		{
			return this;
		}
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.ID **/
	public static LongAttribute<OilLog> id()
	{
		return finder.id();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.UUID **/
	public static StringAttribute<OilLog> uuid()
	{
		return finder.uuid();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.SHIP_ID **/
	public static LongAttribute<OilLog> ship()
	{
		return finder.ship();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.TYPE **/
	public static StringAttribute<OilLog> type()
	{
		return finder.type();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.CODELETTER **/
	public static StringAttribute<OilLog> codeLetter()
	{
		return finder.codeLetter();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.CODENUMBER **/
	public static StringAttribute<OilLog> codeNumber()
	{
		return finder.codeNumber();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.CONTENT **/
	public static StringAttribute<OilLog> content()
	{
		return finder.content();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.SIGNATURE **/
	public static StringAttribute<OilLog> signature()
	{
		return finder.signature();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.GROUPUUID **/
	public static StringAttribute<OilLog> groupUuid()
	{
		return finder.groupUuid();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.ORDERINGROUP **/
	public static StringAttribute<OilLog> orderInGroup()
	{
		return finder.orderInGroup();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.SIGNERRANK **/
	public static StringAttribute<OilLog> signerRank()
	{
		return finder.signerRank();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.SIGNERNAME **/
	public static StringAttribute<OilLog> signerName()
	{
		return finder.signerName();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.SIGNERSURNAME **/
	public static StringAttribute<OilLog> signerSurname()
	{
		return finder.signerSurname();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.NOTE **/
	public static StringAttribute<OilLog> note()
	{
		return finder.note();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.CORRECTIONNOTE **/
	public static StringAttribute<OilLog> correctionNote()
	{
		return finder.correctionNote();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.MASTERSIGNERNAME **/
	public static StringAttribute<OilLog> masterSignerName()
	{
		return finder.masterSignerName();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.MASTERSIGNERSURNAME **/
	public static StringAttribute<OilLog> masterSignerSurname()
	{
		return finder.masterSignerSurname();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.MASTERSIGNATURE **/
	public static StringAttribute<OilLog> masterSignature()
	{
		return finder.masterSignature();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.MASTERSIGDATE **/
	public static DateAttribute<OilLog> masterSigDate()
	{
		return finder.masterSigDate();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.SIGNATUREGROUPUUID **/
	public static StringAttribute<OilLog> signatureGroupUuid()
	{
		return finder.signatureGroupUuid();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.ATTACHMENTNAME **/
	public static StringAttribute<OilLog> attachmentName()
	{
		return finder.attachmentName();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.ATTACHMENTDATA **/
	public static ByteArrayAttribute<OilLog> attachmentData()
	{
		return finder.attachmentData();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.FROM_Z **/
	public static TimestampAttribute<OilLog> businessDateFrom()
	{
		return finder.businessDateFrom();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.THRU_Z **/
	public static TimestampAttribute<OilLog> businessDateTo()
	{
		return finder.businessDateTo();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.IN_Z **/
	public static TimestampAttribute<OilLog> processingDateFrom()
	{
		return finder.processingDateFrom();
	}

	/** maps to OCEAN_LOGBOOK_OPERATION_OIL_LOG.OUT_Z **/
	public static TimestampAttribute<OilLog> processingDateTo()
	{
		return finder.processingDateTo();
	}

	public static AsOfAttribute<OilLog> businessDate()
	{
		return finder.businessDate();
	}

	public static AsOfAttribute<OilLog> processingDate()
	{
		return finder.processingDate();
	}

	public static Operation eq(OilLog other)
	{
		return finder.eq(other);
	}

	public static Operation all()
	{
		return new All(id());
	}

	public static OilLogSingleFinder<OilLog, Object, OilLog> getFinderInstance()
	{
		return finder;
	}

	public static Attribute[] getPrimaryKeyAttributes()
	{
		return new Attribute[] 
		{
			id()
		}

		;
	}

	public static Attribute[] getImmutableAttributes()
	{
		return new Attribute[] 
		{
			id()
			, id()
		}

		;
	}

	public static AsOfAttribute[] getAsOfAttributes()
	{
		return new AsOfAttribute[] 
		{
			businessDate()
			, processingDate()
		}

		;
	}

	protected static void initializeIndicies(Cache cache)
	{
	}

	protected static void initializePortal(MithraObjectDeserializer objectFactory, Cache cache,
		MithraConfigurationManager.Config config)
	{
		initializeIndicies(cache);
		isFullCache = cache.isFullCache();
		isOffHeap = cache.isOffHeap();
		MithraObjectPortal portal;
		if (config.isParticipatingInTx())
		{
			portal = new MithraTransactionalPortal(objectFactory, cache, getFinderInstance(),
				config.getRelationshipCacheSize(), config.getMinQueriesToKeep(), null,
				null, null, 0,
				(MithraObjectPersister) objectFactory);
		}
		else
		{
			portal = new MithraReadOnlyPortal(objectFactory, cache, getFinderInstance(),
				config.getRelationshipCacheSize(), config.getMinQueriesToKeep(), null,
				null, null, 0,
				(MithraObjectPersister) objectFactory);
		}

		portal.setIndependent(true);
		config.initializePortal(portal);
		objectPortal.destroy();
		objectPortal = portal;
	}

	protected static void initializeClientPortal(MithraObjectDeserializer objectFactory, Cache cache,
		MithraConfigurationManager.Config config)
	{
		initializeIndicies(cache);
		isFullCache = cache.isFullCache();
		isOffHeap = cache.isOffHeap();
		MithraObjectPortal portal;
		if (config.isParticipatingInTx())
		{
			portal = new MithraTransactionalPortal(objectFactory, cache, getFinderInstance(),
				config.getRelationshipCacheSize(), config.getMinQueriesToKeep(),
				null, null,
				null, 0,
				new RemoteMithraObjectPersister(config.getRemoteMithraService(), getFinderInstance(), true));
		}
		else
		{
			portal = new MithraReadOnlyPortal(objectFactory, cache, getFinderInstance(),
				config.getRelationshipCacheSize(), config.getMinQueriesToKeep(),
				null, null,
				null, 0,
				new RemoteMithraObjectPersister(config.getRemoteMithraService(), getFinderInstance(), true));
		}

		portal.setIndependent(true);
		config.initializePortal(portal);
		objectPortal.destroy();
		objectPortal = portal;
	}

	public static boolean isFullCache()
	{
		return isFullCache;
	}

	public static boolean isOffHeap()
	{
		return isOffHeap;
	}

	public static Attribute getAttributeByName(String attributeName)
	{
		return finder.getAttributeByName(attributeName);
	}

	public static com.gs.fw.common.mithra.extractor.Function getAttributeOrRelationshipSelector(String attributeName)
	{
		return finder.getAttributeOrRelationshipSelector(attributeName);
	}

	public static RelatedFinder getRelatedFinderByName(String relationshipName)
	{
		return finder.getRelationshipFinderByName(relationshipName);
	}

	public static DoubleAttribute[] zGetDoubleAttributes()
	{
		DoubleAttribute[] result = new DoubleAttribute[0];
		return result;
	}

	public static BigDecimalAttribute[] zGetBigDecimalAttributes()
	{
		BigDecimalAttribute[] result = new BigDecimalAttribute[0];
		return result;
	}

	public static void zResetPortal()
	{
		objectPortal.destroy();
		objectPortal = new UninitializedPortal("al.sybina.ebr.logbook.domain.reladomo.OilLog");
		isFullCache = false;
		isOffHeap = false;
	}

	public static void setTransactionModeFullTransactionParticipation(MithraTransaction tx)
	{
		tx.setTxParticipationMode(objectPortal, FullTransactionalParticipationMode.getInstance());
	}

	public static void setTransactionModeReadCacheWithOptimisticLocking(MithraTransaction tx)
	{
		tx.setTxParticipationMode(objectPortal, ReadCacheWithOptimisticLockingTxParticipationMode.getInstance());
	}

	public static void registerForNotification(MithraApplicationClassLevelNotificationListener listener)
	{
		getMithraObjectPortal().registerForApplicationClassLevelNotification(listener);
	}
}
