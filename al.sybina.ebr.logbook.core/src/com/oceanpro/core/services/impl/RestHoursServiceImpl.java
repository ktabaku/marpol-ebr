package com.oceanpro.core.services.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.oceanpro.core.dao.oceanManager.logbook.operation.impl.GarbageCategoryDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.operation.impl.GarbageLogDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.operation.impl.MarpleSettingsDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.operation.impl.Oil1LogDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.restHours.CrewForRestHoursDAO;
import com.oceanpro.core.dao.oceanManager.logbook.restHours.ManouvringDAO;
import com.oceanpro.core.dao.oceanManager.logbook.restHours.RestDataDAO;
import com.oceanpro.core.dao.oceanManager.logbook.restHours.RestDayDAO;
import com.oceanpro.core.dao.oceanManager.logbook.restHours.impl.CrewForRestHoursDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.restHours.impl.ManouvringDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.restHours.impl.RestDataDAOImpl;
import com.oceanpro.core.dao.oceanManager.logbook.restHours.impl.RestDayDAOImpl;
import com.oceanpro.core.entity.oceanManager.logbook.otherLog.DrugTest;
import com.oceanpro.core.entity.oceanManager.logbook.restHours.CrewForRestHours;
import com.oceanpro.core.entity.oceanManager.logbook.restHours.Manouvring;
import com.oceanpro.core.entity.oceanManager.logbook.restHours.RestData;
import com.oceanpro.core.entity.oceanManager.logbook.restHours.RestDay;
import com.oceanpro.core.entity.oceanManager.logbook.restHours.RestHour;
import com.oceanpro.core.services.RestHoursService;
import com.widee.base.hibernate.criteria.Finder;

import al.sybina.ebr.commons.utils.EnvironmentalEntityManagerFactory;
import al.sybina.ebr.logbook.core.Activator;
import al.sybina.ebr.core.tools.PluginTools;

/**
 * @author Gio
 *
 */
@Creatable("RestHoursService")
@Transactional
public class RestHoursServiceImpl implements RestHoursService {

//	@Autowired
	ManouvringDAO manouvringDAO;
//	@Autowired
	CrewForRestHoursDAO crewForRestHoursDAO;
//	@Autowired
	RestDataDAO restDataDAO;
//	@Autowired
	RestDayDAO restDayDAO;
	
	private EntityManagerFactory emf;
    private EntityManager em;

	protected void activate() {
		em = Activator.getEntityManager();

		manouvringDAO = new ManouvringDAOImpl();
		manouvringDAO.setEntityManager(em);

		crewForRestHoursDAO = new CrewForRestHoursDAOImpl();
		crewForRestHoursDAO.setEntityManager(em);

		restDataDAO = new RestDataDAOImpl();
		restDataDAO.setEntityManager(em);

		restDayDAO = new RestDayDAOImpl();
		restDayDAO.setEntityManager(em);
	}

    protected void deactivate()
    {
    	manouvringDAO=null;
    	crewForRestHoursDAO=null;
    	restDataDAO=null;
    	restDayDAO=null;
    	
    }

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#getAllCrew()
	 */
	@Override
	public List<CrewForRestHours> getAllCrew() {
		return crewForRestHoursDAO.getAll();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#addCrew(com.oceanpro.core.entity.oceanManager.logbook.restHours.CrewForRestHours)
	 */
	@Override
	public CrewForRestHours addCrew(CrewForRestHours crew) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		if (crew.getUuid()==null) crew.setUuid(UUID.randomUUID().toString());
		CrewForRestHours crewForRestHours = crewForRestHoursDAO.create(crew);
		
		entityTransaction.commit();
		return crewForRestHours;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#updateCrew(com.oceanpro.core.entity.oceanManager.logbook.restHours.CrewForRestHours)
	 */
	@Override
	public CrewForRestHours updateCrew(CrewForRestHours crew) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		CrewForRestHours crewForRestHours = crewForRestHoursDAO.update(crew);
		
		entityTransaction.commit();
		return crewForRestHours;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#deleteCrew(long)
	 */
	@Override
	public void deleteCrew(long crewId) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		crewForRestHoursDAO.delete(crewId);
		
		entityTransaction.commit();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#getCrewFinder()
	 */
	@Override
	public Finder<CrewForRestHours> getCrewFinder() {
		return crewForRestHoursDAO.getFinder();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#getCrewByUuid(java.lang.String)
	 */
	@Override
	public CrewForRestHours getCrewByUuid(String uuid) {
		return crewForRestHoursDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#getAllManouvring()
	 */
	@Override
	public List<Manouvring> getAllManouvring() {
		return manouvringDAO.getAll();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#addManouvring(com.oceanpro.core.entity.oceanManager.logbook.restHours.Manouvring)
	 */
	@Override
	public Manouvring addManouvring(Manouvring m) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();
		
		if (m.getUuid()==null) m.setUuid(UUID.randomUUID().toString());
		Manouvring manouvring = manouvringDAO.create(m);
		
		entityTransaction.commit();
		return manouvring;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#updateManouvring(com.oceanpro.core.entity.oceanManager.logbook.restHours.Manouvring)
	 */
	@Override
	public Manouvring updateManouvring(Manouvring m) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		Manouvring manouvring = manouvringDAO.update(m);
		
		entityTransaction.commit();
		return manouvring;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#deleteManouvring(long)
	 */
	@Override
	public void deleteManouvring(long mId) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		manouvringDAO.delete(mId);
		
		entityTransaction.commit();		
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#getManouvringFinder()
	 */
	@Override
	public Finder<Manouvring> getManouvringFinder() {
		return manouvringDAO.getFinder();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#getManouvringByUuid(java.lang.String)
	 */
	@Override
	public Manouvring getManouvringByUuid(String uuid) {
		return manouvringDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#getRestDataByCrewUuid(java.lang.String)
	 */
	@Override
	public RestData getRestDataByCrewUuid(String uuid) {
		return restDataDAO.getFinder().and("crew.uuid").eq(uuid).setDistinct().result();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#addRestData(com.oceanpro.core.entity.oceanManager.logbook.restHours.RestData, com.oceanpro.core.entity.oceanManager.logbook.restHours.CrewForRestHours)
	 */
	@Override
	public RestData addRestData(RestData r, CrewForRestHours crew) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		if (r.getUuid()==null) r.setUuid(UUID.randomUUID().toString());
		r.setCrew(crew);
		RestData restData = restDataDAO.create(r);
		
		entityTransaction.commit();
		return restData;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#updateRestData(com.oceanpro.core.entity.oceanManager.logbook.restHours.RestData)
	 */
	@Override
	public RestData updateRestData(RestData r) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		RestData restData = restDataDAO.update(r);
		
		entityTransaction.commit();
		return restData;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#getAllMonthDays(com.oceanpro.core.entity.oceanManager.logbook.restHours.RestData, int)
	 */
	@Override
	public List<RestDay> getAllMonthDays(RestData data, int year, int month) {
		
		// Prendo i 7 giorni del mese successivo!!!
		List<RestDay> daysPrec = new ArrayList<RestDay>();
		Calendar cp = Calendar.getInstance();
		cp.set(Calendar.YEAR, year);
		int mesePrec = month-2;
		if (mesePrec<0) mesePrec = 11;
		cp.set(Calendar.MONTH, mesePrec);
		int ultimoGiorno = cp.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = ultimoGiorno-6; i <= ultimoGiorno; i++) {
			RestDay dp= restDayDAO.getFinder().and("data.id").eq(data.getId()).and("month").eq(new Integer(month-1)).and("year").eq(new Integer(year)).and("dayNumber").eq(new Integer(i)).setDistinct().result();
			if (dp!=null) {
				// Avoid Lazy
				if (dp.getManouvrings()!=null) {
					for (Manouvring m : dp.getManouvrings()) {
						m.toString();
					}
				}
				daysPrec.add(dp);
			}
		}
		//// DEBUG
		System.out.println("num giorni mese precedente al mese "+month+" caricati:" + daysPrec.size());
		
		if (data==null || data.getId()==null) {
			// restituisco tutti i giorni come nuovi
			List<RestDay> days = createMonthDays(data, year, month);
			
			// Aggiungo precedenti
			days.addAll(daysPrec);
			
			return days;
		}
		else {
			List<RestDay> ret= restDayDAO.getFinder().and("data.id").eq(data.getId()).and("month").eq(new Integer(month)).and("year").eq(new Integer(year)).setDistinct().list();
			
			Calendar c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone("UTC"));
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, month-1);
			int endDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			if (ret==null || ret.size()<endDay) { // se incompleto lo ricreo (non dovrebbe mai succedere)
				// Aggiungo i giorni
				System.out.println("aggiungo giorni");
				ret = createMonthDays(data, year, month);
			}
			else {
				// Avoid Lazy
				for (RestDay ddd : ret) {
					//System.out.println("loop lazy giorno " + ddd.getDayNumber() + "   mese:" + ddd.getMonth());
					// DEBUG
//					for (RestHour hhh : ddd.getHours()) {
//						System.out.print("ora: " + hhh.getName() + "  val:" + hhh.getValue());
//					}
					
					if (ddd.getManouvrings()!=null) {
						for (Manouvring m : ddd.getManouvrings()) {
							m.toString();
						}
					}
				}
			}
			
			// Aggiungo precedenti
			ret.addAll(daysPrec);
			
			
			return ret;
		}
	}
	
	
	private List<RestDay> createMonthDays(RestData data, int year, int month) {
		List<RestDay> days = new ArrayList<RestDay>();
		DateFormat f = new SimpleDateFormat("EEE", Locale.ENGLISH);
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("UTC"));
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		int endDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		//System.out.println("getMonthDays - last day of month:" + endDay);
		
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		for (int i = 1; i <= endDay; i++) {
			//System.out.println("Cerco: giorno:" + i + "   mese: " + mese + "   anno:" + anno);
			c.set(Calendar.DAY_OF_MONTH, i);
			
			RestDay d = new RestDay();
			d.setUuid(UUID.randomUUID().toString());
			d.setData(data);
			d.setYear(year);
			d.setMonth(month);
			d.setDayNumber(i);
			d.setDayName(f.format(c.getTime()));
			d.createHours();
			
			days.add(d);
		}
		
		entityTransaction.commit();

		
		return days;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#addDay(com.oceanpro.core.entity.oceanManager.logbook.restHours.RestDay)
	 */
	@Override
	public RestDay addDay(RestDay d) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		if (d.getUuid()==null) d.setUuid(UUID.randomUUID().toString());
		RestDay restDay = restDayDAO.create(d);
		
		entityTransaction.commit();
		return restDay;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#updateDay(com.oceanpro.core.entity.oceanManager.logbook.restHours.RestDay)
	 */
	@Override
	public RestDay updateDay(RestDay d) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		RestDay restDay = restDayDAO.update(d);
		
		entityTransaction.commit();
		return restDay;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#deleteDay(long)
	 */
	@Override
	public void deleteDay(long mId) {
		EntityTransaction entityTransaction = em.getTransaction();
		entityTransaction.begin();

		restDayDAO.delete(mId);
		
		entityTransaction.commit();		
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#getDayFinder()
	 */
	@Override
	public Finder<RestDay> getDayFinder() {
		return restDayDAO.getFinder();
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#getManouvringsForCurrentDate(java.util.Date)
	 */
	@Override
	public List<Manouvring> getManouvringsForCurrentDate(Date currentDate) {
		System.out.println("cerco manovre per " + currentDate);
		List<Manouvring> ms = manouvringDAO.getFinder().and("start").le(currentDate).and("end").ge(currentDate).setDistinct().list();
		System.out.println("trovate " + ms.size());
		return ms;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#getManouvringsByRestDay(com.oceanpro.core.entity.oceanManager.logbook.restHours.RestDay)
	 */
	@Override
	public List<Manouvring> getManouvringsByRestDay(RestDay day) {
		RestDay rd = restDayDAO.getFinder().and("uuid").eq(day.getUuid()).result();
		if (rd!=null && rd.getManouvrings()!=null) {
			for (Manouvring m : rd.getManouvrings()) {
				m.toString();
			}
			return rd.getManouvrings();
		}
		else return null;
	}

	/* (non-Javadoc)
	 * @see com.oceanpro.core.services.RestHoursService#getRestDayByUuid(java.lang.String)
	 */
	@Override
	public RestDay getRestDayByUuid(String uuid) {
		RestDay rd = restDayDAO.getFinder().and("uuid").eq(uuid).setDistinct().result();
		if (rd!=null && rd.getManouvrings()!=null) {
			for (Manouvring m : rd.getManouvrings()) {
				m.toString();
			}
		}
		
		return rd;
	}


	
	
	
	
	

}
