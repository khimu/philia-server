package com.philia.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.philia.entity.User;
import com.philia.model.Match;
import com.philia.model.Profile;
import com.philia.repository.UserRepository;
import com.philia.service.IMatchService;
import com.philia.service.IProfileService;

/**
 * 
 * @author khimung
 *
 */
@RestController
public class ProfileController {
	
	private final static Logger logger = Logger.getLogger(ProfileController.class);
	
	/*
	 * According to stackoverflow this is thread safe
	 */
	private static final Gson gson = new Gson();
	
	@Resource
	private IProfileService profileService;
	
	@Resource
	private IMatchService matchService;
	
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

    	/*
    	CompletableFuture.runAsync(() -> {
        	*
        	 * send enough information to make a match record
        	 *
        	amqpTemplate.convertAndSend(profile);
    	});
    	*/
    	
    	amqpTemplate.convertAndSend("myExchange", "com.philia.model.Profile", profile);
    	return retrievalKey;
    }
    
    @RequestMapping(value = "/profile/{userId}", method = RequestMethod.GET)
    public Profile get(@PathVariable("userId") String userId) {
    	return profileService.findByUser(userId);
    }
    
    
    /*
     * username: guest
     * password: guest
     * 
     * http://107.170.234.144:15672/
     * 
     * tail -f /var/log/rabbitmq/rabbit@ubuntu-1gb-sfo1-01.log
     * 
     * By default, you should enable 5672 (rabbit mq port) and 4365 (empd port)
     */        
    //@RabbitListener(queues = "profiles", containerFactory="connectionFactory")
    public void listen(Profile profile) {
    	logger.info("Calling listen");
    	
    	if(profile != null) {
    		logger.info("profile user id " + profile.getUserId());
	    	/*
	    	 * TODO
	    	 * 
	    	 * Query mongodb for all profile where x = x, y=y, etc
	    	 * Create Match for given profile, Create List<Matches> and save 
	    	 * Create Matches and update List<Matches> for each List<Profile> that matches given profile
	    	 * 
	    	 */    	
	    	int start = 0;
	    	List<Profile> profiles = null;
	    	do {
	    		
	    		try {
		    		profiles = profileService.findAllMatches(start, profile);
		    		
		    		logger.info("profiles size: " + profiles.size());
		    		
		    		if(profiles != null) {
			    		for(Profile p : profiles) {
			    			logger.info("matching profile user id " + p.getUserId());
				    		Match userMatch = new Match();
				    		userMatch.setUserId(profile.getUserId());
				    		userMatch.setMatchedWithUserId(p.getUserId());
				    		userMatch.setCreated(new Date());
				    		userMatch.setWeight(100);
				    		userMatch.setClearImage(profile.getClearImage());
				    		userMatch.setBlurredImage(profile.getBlurredImage());
			
				    		Match matchMatchUser = new Match();
				    		matchMatchUser.setUserId(p.getUserId());
				    		matchMatchUser.setMatchedWithUserId(profile.getUserId());
				    		userMatch.setCreated(new Date());
				    		userMatch.setWeight(100);
				    		userMatch.setClearImage(p.getClearImage());
				    		userMatch.setBlurredImage(p.getBlurredImage());
				    		
				    		matchService.saveMatch(userMatch, matchMatchUser, profile.getUserId(), p.getUserId());
			    		}
		    		}	    		
	    		}catch(Exception e) {
	    			logger.error("Unable to find matches for " + profile.getUserId() + " due to " + e.getMessage());
	    		}
	    		
	    		start += 100;
	    	} while (profiles != null && (profiles.size() > 0));
    	} 
    	else {
    		logger.warn("profile is null");
    	}
	}
}
