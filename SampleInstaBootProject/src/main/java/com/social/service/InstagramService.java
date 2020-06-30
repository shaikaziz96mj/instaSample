package com.social.service;

import com.social.domain.ResponseObject;

public interface InstagramService {

	public ResponseObject saveInstagramUserTokens(String accessToken);

	public String getInstagramLongLivedToken(String shortLivedToken);

	public String getInstagramRefreshedToken(String accessToken);

	public void createInstagramScheduler();

}