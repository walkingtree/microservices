package com.wtt.util;

import java.net.URI;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wtt.dao.SocialDao;
import com.wtt.model.UserBean;

/**
 *
 * @author ManikantaReddy
 */

@Service
public class TokenValidation {

	@Value("${todo.doogle.profile.url}")
	private String GOOGLE_PROFILE_URL;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	SocialDao socialDaoImpl;

	public UserBean validateAccessToken(String token) throws Exception {
		GoogleUserProfile userProfile = null;
		UserBean existingUser = null;
		try {
			userProfile = restTemplate.getForObject(GOOGLE_PROFILE_URL, GoogleUserProfile.class, token);
			if (userProfile != null) {
				existingUser = socialDaoImpl.getProfile(userProfile.getEmail());
				if (existingUser == null) {
					synchronized (this) {
						if (existingUser == null) {
							existingUser = new UserBean();
							existingUser.setEmail(userProfile.getEmail());
							existingUser.setFirstName(userProfile.getGiven_name());
							existingUser.setLastName(userProfile.getFamily_name());
							existingUser.setProvider("Gmail");
							if (userProfile.getPicture() != null && !userProfile.getPicture().isEmpty()) {
								URI url = new URI(userProfile.getPicture());
								byte data[] = restTemplate.execute(url, HttpMethod.GET, null, new BinaryFileExtractor());
								String imageStr = Base64.encodeBase64String(data);
								existingUser.setImage(imageStr);
							}
							existingUser.setPin(0);
							socialDaoImpl.saveProfile(existingUser);
							return existingUser;
						}
					}
				} else {
					return existingUser;
				}

			}
		} catch (Exception e) {
			throw new Exception("Authentication failed. Please use valid credentials: " + e.getMessage());
		}
		return existingUser;
	}

}
