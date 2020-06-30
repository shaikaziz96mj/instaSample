package com.social.repository;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.social.domain.InstagramConnections;

public interface InstagramConnectionRepository extends JpaRepository<InstagramConnections, Long> {

	@Query(value = "SELECT * FROM instagram_connections WHERE sysdate()-modified_on>=58",nativeQuery = true)
	public List<InstagramConnections> getExpiringSoonAccounts();
	
	@Transactional
	@Modifying
	@Query(value="UPDATE instagram_connections SET access_token=:newToken,"
			+ "expiry_time=:newTime,modified_on=:modifiedOn WHERE id=:userId",nativeQuery = true)
	public void updateUserTokensWithRefreshedTokens(String newToken,String newTime,
			Calendar modifiedOn,Long userId);
	
}