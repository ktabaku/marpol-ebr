package com.oceanpro.core.services;

import java.util.Date;
import java.util.List;

import com.oceanpro.core.entity.oceanManager.logbook.restHours.CrewForRestHours;
import com.oceanpro.core.entity.oceanManager.logbook.restHours.Manouvring;
import com.oceanpro.core.entity.oceanManager.logbook.restHours.RestData;
import com.oceanpro.core.entity.oceanManager.logbook.restHours.RestDay;
import com.widee.base.hibernate.criteria.Finder;

public interface RestHoursService {
	
	public List<CrewForRestHours> getAllCrew();
	public CrewForRestHours addCrew(CrewForRestHours crew);
	public CrewForRestHours updateCrew(CrewForRestHours crew);
	public void deleteCrew(long crewId);
	public Finder<CrewForRestHours> getCrewFinder();
	public CrewForRestHours getCrewByUuid(String uuid);
	
	public List<Manouvring> getAllManouvring();
	public Manouvring addManouvring(Manouvring m);
	public Manouvring updateManouvring(Manouvring m);
	public void deleteManouvring(long mId);
	public Finder<Manouvring> getManouvringFinder();
	public Manouvring getManouvringByUuid(String uuid);
	public List<Manouvring> getManouvringsForCurrentDate(Date currentDate);
	public List<Manouvring> getManouvringsByRestDay(RestDay day);
	
	public RestData getRestDataByCrewUuid(String uuid);
	public RestData addRestData(RestData r, CrewForRestHours crew);
	public RestData updateRestData(RestData r);
	
	public List<RestDay> getAllMonthDays(RestData data, int year, int month);
	public RestDay addDay(RestDay d);
	public RestDay updateDay(RestDay d);
	public void deleteDay(long mId);
	public Finder<RestDay> getDayFinder();
	public RestDay getRestDayByUuid(String uuid);
}
