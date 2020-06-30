package com.social.serviceimpl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.social.domain.InstagramConnections;
import com.social.domain.ResponseObject;
import com.social.repository.InstagramConnectionRepository;
import com.social.service.InstagramService;
import com.social.utils.CommonUtils;
import com.social.utils.MessageConstants;

@Service
public class InstagramServiceImpl implements InstagramService {

	private static final Logger LOGGER = LoggerFactory.getLogger(InstagramServiceImpl.class);

	@Autowired
	private InstagramConnectionRepository instagramRepository;

	@Value("${spring.social.instagram.app-id}")
	private String appId;
	@Value("${spring.social.instagram.app-secret}")
	private String appSecret;

	@Override
	public ResponseObject saveInstagramUserTokens(String accessToken) {
		if (CommonUtils.isNull(accessToken)) {
			return new ResponseObject(null, "Please provide valid token", HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("saveInstagramUserToken(-) initiated");

		try {
			//performing validations before inserting 
			/*List<InstagramConnections> connectionList = instagramRepository.findAll();
			for (InstagramConnections connection : connectionList) {
				if (connection.getUsers().getExternalId().equals(user.getExternalId())) {

					return new ResponseObject(null, "Your connection details already stored!", HttpStatus.BAD_REQUEST);
				}
			}*/
			// getting long lived token
			String longToken = getInstagramLongLivedToken(accessToken);
			if (CommonUtils.isNull(longToken)) {
				return new ResponseObject(null, "Sorry! unable to get long lived token", HttpStatus.BAD_REQUEST);
			}
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(longToken);
			String longLivedToken = obj.get("access_token").toString();
			String expiryTime = obj.get("expires_in").toString();

			if (CommonUtils.isNull(longLivedToken) || CommonUtils.isNull(expiryTime)) {
				return new ResponseObject(null, "something went wrong! please try again later", HttpStatus.BAD_REQUEST);
			}

			LOGGER.info("Is shortLivedToken equals longLivedToken::"+accessToken.equalsIgnoreCase(longLivedToken));

			//saving user details
			InstagramConnections connections=new InstagramConnections(UUID.randomUUID().toString(), 
					longLivedToken, expiryTime, "insta", null, Calendar.getInstance(), Calendar.getInstance());
			instagramRepository.saveAndFlush(connections);

			return new ResponseObject(MessageConstants.OK,"User details inserted successfully", HttpStatus.OK);
		}
		catch (Exception e) {
			LOGGER.error("Exception occured:" + e.getMessage());
			e.printStackTrace();
		}
		return new ResponseObject(null, "Something went wrong! please try again", HttpStatus.BAD_REQUEST);
	}//saveInstagramUserTokens(-)

	@Override
	public String getInstagramLongLivedToken(String shortLivedToken) {
		LOGGER.info("getInstagramLongLivedToken(-) initiated");
		Scanner sc=null;
		String inline = "";
		String baseUrl= "https://graph.instagram.com/access_token?grant_type=ig_exchange_token&"
				+ "client_secret="+appSecret+"&access_token="+shortLivedToken;
		LOGGER.info(baseUrl);
		try {
			URL url = new URL(baseUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
			conn.setRequestMethod("GET"); 
			conn.connect(); 
			int responsecode = conn.getResponseCode(); 
			if(responsecode != 200)
				throw new RuntimeException("HttpResponseCode: " +responsecode);
			else{
				sc= new Scanner(url.openStream());
				while(sc.hasNext())
				{
					inline+=sc.nextLine();
				}
			}
		}catch (Exception e) {
			LOGGER.error("Caused exception::"+e.getMessage());
		}finally {
			try {
				if(sc!=null)
					sc.close();
			}catch (Exception e) {
				LOGGER.error("Caused exception::"+e.getMessage());
			}
		}//finally
		return inline;
	}//getLongLivedToken(-)

	@Override
	@Scheduled(cron = "0 0 9 * * *")
	public void createInstagramScheduler() {
		LOGGER.info("createInstagramScheduler() initiated");
		JSONParser parser = new JSONParser();
		// getting expiry time
		try {
			List<InstagramConnections> connections = instagramRepository.getExpiringSoonAccounts();
			connections.forEach(connection -> {
				System.out.println(connection);
			});
			for (InstagramConnections connection : connections) {
				String tokenFromMethod = getInstagramRefreshedToken(connection.getAccessToken());
				JSONObject obj = (JSONObject) parser.parse(tokenFromMethod);
				String refreshedToken = obj.get("access_token").toString();
				String expiryTime = obj.get("expires_in").toString();
				// updating with new token and expiry time
				instagramRepository.updateUserTokensWithRefreshedTokens(refreshedToken, expiryTime,
						Calendar.getInstance(), connection.getId());
				LOGGER.info("Is accessToken equals refreshed token::"+connection.getAccessToken().equalsIgnoreCase(refreshedToken));
			}
		} catch (Exception e) {
			LOGGER.error("Caused exception::" + e.getMessage());
		}
	}

	@Override
	public String getInstagramRefreshedToken(String accessToken) {
		LOGGER.info("getInstagramrefreshedToken(-) initiated");
		String baseUrl= "https://graph.instagram.com/refresh_access_token"
				+ "?grant_type=ig_refresh_token&access_token="+accessToken;
		String inline="";
		Scanner sc=null;
		LOGGER.info(baseUrl);
		try {
			URL url = new URL(baseUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
			conn.setRequestMethod("GET"); 
			conn.connect(); 
			int responsecode = conn.getResponseCode(); 
			if(responsecode != 200)
				throw new RuntimeException("HttpResponseCode: " +responsecode);
			else{
				sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					inline+=sc.nextLine();
				}
			}//else
			LOGGER.info(inline);
		}//try
		catch (Exception e) {
			LOGGER.error("Caused Exception::"+e.getMessage());
			e.printStackTrace();
		}finally {
			try {
				if(sc!=null)
					sc.close();
			}catch(Exception e){
				LOGGER.error("Caused Exception::"+e.getMessage());
			}
		}//finally
		return inline;
	}//getInstagramRefreshedToken(-)
}