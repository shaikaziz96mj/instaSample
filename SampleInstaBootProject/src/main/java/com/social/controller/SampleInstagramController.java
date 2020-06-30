package com.social.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.social.domain.ResponseObject;
import com.social.service.InstagramService;

@RestController
@RequestMapping("/api/v1")
public class SampleInstagramController {

	@Autowired
	InstagramService instagramService;
	
	@PostMapping("/saveInstagramTokens")
	public ResponseObject saveInstaUserTokens(@RequestParam("token") String accessToken) {
		return instagramService.saveInstagramUserTokens(accessToken);
	}
	
}