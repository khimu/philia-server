package com.philia;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.philia.app.Application;
import com.philia.entity.User;
import com.philia.repository.UserRepository;

/**
 * Integration test to show the usage of Java 8 date time APIs with Spring Data JPA auditing.
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Available in spring boot 1.4.0
//@RunWith(SpringRunner.class)
//@SpringBootTest
@ContextConfiguration(classes = Application.class)
@TestPropertySource(locations="classpath:application.properties")
@Transactional(transactionManager = "transactionManager")
public class UserRepositoryTest {
	


		@Autowired @Qualifier("entityManagerFactory") EntityManager em;

		@Autowired
	UserRepository repository;
	
	User user;

	@Before
	public void setUp() {

		user = new User();
		user.setEmail("foobar@gmail.com");
		user.setFirstName("firstname");
		user.setLastName("lastname");
	}

	@Test
	public void findSavedUserById() {

		user = repository.save(user);

		assertThat(repository.findOne(user.getId()), is(user));
	}

	@Test
	public void findSavedUserByLastname() throws Exception {

		user = repository.save(user);

		List<User> users = repository.findByLastName("lastname");

		assertThat(users, is(notNullValue()));
		assertThat(users.contains(user), is(true));
	}

	@Test
	public void findByFirstnameOrLastname() throws Exception {

		user = repository.save(user);

		List<User> users = repository.findByFirstNameOrLastName("lastname");

		assertThat(users.contains(user), is(true));
	}

	@Ignore
	public void useOptionalAsReturnAndParameterType() {

		assertThat(repository.findByEmail(Optional.of("foobar")), is(Optional.empty()));

		repository.save(user);

		assertThat(repository.findByEmail(Optional.of("foobar")).isPresent(), is(true));
	}

	@Test
	public void removeByLastname() {

		// create a 2nd user with the same lastname as user
		User user2 = new User();
		user2.setLastName(user.getLastName());

		// create a 3rd user as control group
		User user3 = new User();
		user3.setLastName("no-positive-match");

		repository.save(Arrays.asList(user, user2, user3));

		assertThat(repository.removeByLastName(user.getLastName()), is(2L));
		assertThat(repository.exists(user3.getId()), is(true));
	}

	@Test
	public void useSliceToLoadContent() {

		repository.deleteAll();

		// int repository with some values that can be ordered
		int totalNumberUsers = 11;
		List<User> source = new ArrayList<User>(totalNumberUsers);

		for (int i = 1; i <= totalNumberUsers; i++) {

			User user = new User();
			user.setLastName(this.user.getLastName());
			user.setEmail(user.getLastName() + "-" + String.format("%03d", i));
			source.add(user);
		}

		repository.save(source);

		Slice<User> users = repository.findByLastNameOrderByEmailAsc(this.user.getLastName(), new PageRequest(1, 5));

		assertThat(users, contains(source.subList(5, 10).toArray()));
	}

	@Test
	public void findFirst2ByOrderByLastnameAsc() {

		User user0 = new User();
		user0.setLastName("lastname-0");

		User user1 = new User();
		user1.setLastName("lastname-1");

		User user2 = new User();
		user2.setLastName("lastname-2");

		// we deliberatly save the items in reverse
		repository.save(Arrays.asList(user2, user1, user0));

		List<User> result = repository.findFirst2ByOrderByLastNameAsc();

		assertThat(result.size(), is(2));
		assertThat(result, hasItems(user0, user1));
	}

	@Test
	public void findTop2ByWithSort() {

		User user0 = new User();
		user0.setLastName("lastname-0");

		User user1 = new User();
		user1.setLastName("lastname-1");

		User user2 = new User();
		user2.setLastName("lastname-2");

		// we deliberately save the items in reverse
		repository.save(Arrays.asList(user2, user1, user0));

		List<User> resultAsc = repository.findTop2By(new Sort(Direction.ASC, "lastName"));

		assertThat(resultAsc.size(), is(2));
		assertThat(resultAsc, hasItems(user0, user1));

		List<User> resultDesc = repository.findTop2By(new Sort(Direction.DESC, "lastName"));

		assertThat(resultDesc.size(), is(2));
		assertThat(resultDesc, hasItems(user1, user2));
	}

	@Test
	public void findByFirstnameOrLastnameUsingSpEL() {

		User first = new User();
		first.setLastName("lastname");

		User second = new User();
		second.setFirstName("firstname");

		User third = new User();

		repository.save(Arrays.asList(first, second, third));

		User reference = new User();
		reference.setFirstName("firstname");
		reference.setLastName("lastname");

		Iterable<User> users = repository.findByFirstNameOrLastName(reference);

		assertThat(users, is(iterableWithSize(2)));
		assertThat(users, hasItems(first, second));
	}

}