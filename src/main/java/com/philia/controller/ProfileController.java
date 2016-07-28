package com.philia.controller;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.philia.entity.User;
import com.philia.model.Profile;
import com.philia.repository.UserRepository;
import com.philia.service.IProfileService;

/**
 * 
 * @author khimung
 *
 */
@RestController
public class ProfileController {
	
	private final static Logger logger = Logger.getLogger(ProfileController.class);
	
	@Resource
	private IProfileService profileService;
	
	@Resource
	private UserRepository userRepository;
	
	@Resource
	private RabbitTemplate amqpTemplate;
	
	@Resource
	private PasswordEncoder passwordEncoder;
	
	/**
	 * {@link /profile}
	 * 
	 * @param profile
	 */
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String create(@RequestBody Profile profile) {
    	final String retrievalKey = RandomStringUtils.randomAlphabetic(20)+"@philia.com";
		String hashedPassword = passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(5));
		
		if(profile.getAge() == null) {
			logger.info("Age is null");
		}
		else {
			logger.info("age is " + profile.getAge());
		}
		
		if(profile.getFirstName() == null ) {
			logger.info("first name is null");
		}
    	
    	User user = new User();
    	user.setEmail(retrievalKey);
    	user.setPassword(hashedPassword);
    	user.setCity(profile.getCity());
    	user.setState(profile.getState());
    	user.setCountry(profile.getCountry());
    	user.setFirstName(profile.getFirstName());
    	user.setLastName(profile.getLastName());
    	user.setAge(Integer.parseInt(profile.getAge()));
    	user.setClearImage(profile.getClearImage());
    	user.setBlurredImage(profile.getBlurredImage());
    	
    	User savedUser = userRepository.save(user);
    	
    	profile.setUserId(savedUser.getId().toString());
    	profile.setCreated(new Date());
    	
    	profileService.saveProfile(profile);

    	CompletableFuture.runAsync(() -> {
        	/*
        	 * send enough information to make a match record
        	 */
        	amqpTemplate.convertAndSend(profile);
    	});
    	return retrievalKey;
    }
    
    @RequestMapping(value = "/profile/{userId}", method = RequestMethod.GET)
    public Profile get(@PathVariable("userId") String userId) {
    	return profileService.findByUser(userId);
    }
    
    @RabbitListener(queues = "profiles")
    public void listen(String foo) {
    	logger.info("this is foo " + foo);
	}
}
