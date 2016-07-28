package com.philia.app;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Configuration
//@EnableTransactionManagement
/*
 * This is how you configure multiple database access
 */
@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "transactionManager", basePackages = "com.philia.repository")
@EnableWebMvc
public class Config extends WebMvcConfigurerAdapter {
	
	@Resource
	private DataSource dataSource;

	/**
	* Setup a simple strategy: use all the defaults and return XML by default when not sure. 
	*/
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(false).
	            favorParameter(false).
	            ignoreAcceptHeader(false).
	            useJaf(false).
	            defaultContentType(MediaType.APPLICATION_JSON).
	            mediaType("xml", MediaType.APPLICATION_XML).
	            mediaType("json", MediaType.APPLICATION_JSON);
	}
		  
	@Bean
    public MessageConverter jsonMessageConverter(){
        return new JsonMessageConverter();
    }
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	/*
	 * Turn off redis for now
	@Autowired
	RedisConnectionFactory connectionFactory;

	@Bean
	RedisMessageListenerContainer container() {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		// Assign our BeanMessageListener to a specific channel
		container.addMessageListener(beanMessageListener(), new ChannelTopic(
				"philia"));
		return container;
	}

	@Bean
	public MessageListener dumpToConsoleListener() {
		return new MessageListener() {
			@Override
			public void onMessage(Message message, byte[] pattern) {
				System.out.println("FROM MESSAGE: "
						+ new String(message.getBody()));
			}
		};
	}

	@Bean
	MessageListenerAdapter beanMessageListener() {
		MessageListenerAdapter listener = new MessageListenerAdapter(
				new BeanMessageListener());
		listener.setSerializer(new BeanMessageSerializer());
		return listener;
	}

	public class BeanMessageListener {
		public void handleMessage(BeanMessage msg) {
			System.out.println("msg: " + msg.message);
		}
	}

	private static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();

	@Bean
	public RedisTemplate<String, Long> longTemplate() {

		RedisTemplate<String, Long> tmpl = new RedisTemplate<String, Long>();
		tmpl.setConnectionFactory(connectionFactory);
		tmpl.setKeySerializer(STRING_SERIALIZER);
		tmpl.setValueSerializer(LongSerializer.INSTANCE);

		return tmpl;
	}
	*/
	
	/*
	 * Using XML configuration

	@Bean
	  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

	    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    vendorAdapter.setDatabase(Database.HSQL);
	    vendorAdapter.setGenerateDdl(true);

	    LocalContainerEntityManagerFactoryBean factory = 
	      new LocalContainerEntityManagerFactoryBean();
	    factory.setJpaVendorAdapter(vendorAdapter);
	    factory.setPackagesToScan(getClass().getPackage().getName());
	    factory.setDataSource(dataSource);

	    return factory;
	  }

	  @Bean
	  public PlatformTransactionManager transactionManager() {
	    JpaTransactionManager txManager = new JpaTransactionManager();
	    txManager.setEntityManagerFactory(entityManagerFactory().nativeEntityManagerFactory);
	    return txManager;
	  }
	  	 */
}