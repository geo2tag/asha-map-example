package org.geo2tag.ashamapsexample;


import java.util.Vector;

import ru.spb.osll.json.Errno;
import ru.spb.osll.json.JsonFilterCircleRequest;
import ru.spb.osll.json.JsonFilterResponse;
import ru.spb.osll.json.JsonLoginRequest;
import ru.spb.osll.json.JsonLoginResponse;
import ru.spb.osll.json.JsonRequestException;
import ru.spb.osll.json.JsonSetDbRequest;
import ru.spb.osll.json.JsonSetDbResponse;
import ru.spb.osll.json.RequestSender;
import ru.spb.osll.utils.DateUtil;

public class Geo2TagRequests {

	private String m_authToken = null; 
	
	private static Geo2TagRequests s_instance = null;
	
	public static Geo2TagRequests getInstance(){
		
		if (s_instance == null) 
			s_instance = new Geo2TagRequests();
		
		return s_instance;
		
	}
	
	public void doLoginRequest(String login, String password) throws JsonRequestException{
		
		JsonLoginRequest req = new JsonLoginRequest(login, password, Settings.SERVER_URL) ;
		JsonLoginResponse res = new JsonLoginResponse();
		int[] errnos = {Errno.SUCCESS.intValue()};
		
		RequestSender.performRequest(req, res, errnos);
		
		System.out.println("Login request success "+res.getAuthString());
		m_authToken = res.getAuthString();
	}
	
	public void doSetDbRequest(String dbName) throws JsonRequestException, NullPointerException{
		
		JsonSetDbRequest req = new JsonSetDbRequest(getAuthToken(), dbName, Settings.SERVER_URL) ;
		JsonSetDbResponse res = new JsonSetDbResponse();
		int[] errnos = {Errno.SUCCESS.intValue()};
		
		RequestSender.performRequest(req, res, errnos);
		
	}

	public Vector doFilterCircleRequest(double latitude, double longitude, double radius)
			throws JsonRequestException, NullPointerException{
		
		String timeFrom = DateUtil.getPastTime(1);
		String timeTo = DateUtil.getCurrentTime();
		
		JsonFilterCircleRequest req = new JsonFilterCircleRequest(getAuthToken(), latitude, 
				longitude, radius, timeFrom, timeTo, Settings.SERVER_URL);
		JsonFilterResponse res = new JsonFilterResponse();
		int[] errnos = {Errno.SUCCESS.intValue()};

		RequestSender.performRequest(req, res, errnos);

		return res.getChannelsData();
		
	}
	
	public void doWriteTag(double latitude, double longitude, double altitude, String channel, String label) 
			throws JsonRequestException, NullPointerException{
		
	}
	
	private String getAuthToken() {
		// TODO Auto-generated method stub
		
		if (m_authToken == null) throw new NullPointerException();
		
		return m_authToken;
	}
}
