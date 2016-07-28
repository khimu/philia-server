package com.philia.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import com.philia.entity.User;

/**
 * Repository to manage {@link User} instances.
 * 
 * @author khimung
 */
@Transactional
public interface UserRepository extends CrudRepository<User, BigInteger> {

	/**
	 * Find the user with the given username. This method will be translated
	 * into a query using the {@link javax.persistence.NamedQuery} annotation at
	 * the {@link User} class.
	 * 
	 * @param lastname
	 * @return
	 */
	User findByTheEmail(String email);

	/**
	 * Uses {@link Optional} as return and parameter type.
	 * 
	 * @param username
	 * @return
	 */
	@Deprecated
	Optional<User> findByEmail(@Param("email") Optional<String> email);

	/**
	 * Find all users with the given lastname. This method will be translated
	 * into a query by constructing it directly from the method name as there is
	 * no other query declared.
	 * 
	 * @param lastname
	 * @return
	 */
	List<User> findByLastName(String lastname);

	/**
	 * Returns all users with the given firstname. This method will be
	 * translated into a query using the one declared in the {@link Query}
	 * annotation declared one.
	 * 
	 * @param firstname
	 * @return
	 */
	@Query("select u from User u where u.firstName = :firstName")
	List<User> findByFirstName(String firstname);

	/**
	 * Returns all users with the given name as first- or lastname. This makes
	 * the query to method relation much more refactoring-safe as the order of
	 * the method parameters is completely irrelevant.
	 * 
	 * @param name
	 * @return
	 */
	@Query("select u from User u where u.firstName = :name or u.lastName = :name")
	List<User> findByFirstNameOrLastName(@Param("name") String name);

	/**
	 * Returns the total number of entries deleted as their lastnames match the
	 * given one.
	 * 
	 * @param lastname
	 * @return
	 */
	Long removeByLastName(String lastname);

	/**
	 * Returns a {@link Slice} counting a maximum number of
	 * {@link Pageable#getPageSize()} users matching given criteria starting at
	 * {@link Pageable#getOffset()} without prior count of the total number of
	 * elements available.
	 * 
	 * @param lastname
	 * @param page
	 * @return
	 */
	Slice<User> findByLastNameOrderByEmailAsc(String lastname, Pageable page);

	/**
	 * Return the first 2 users ordered by their lastname asc.
	 * 
	 * <pre>
	 * Example for findFirstK / findTopK functionality.
	 * </pre>
	 * 
	 * @return
	 */
	List<User> findFirst2ByOrderByLastNameAsc();

	/**
	 * Return the first 2 users ordered by the given {@code sort} definition.
	 * 
	 * <pre>
	 * This variant is very flexible because one can ask for the first K results when a ASC ordering
	 * is used as well as for the last K results when a DESC ordering is used.
	 * </pre>
	 * 
	 * @param sort
	 * @return
	 */
	List<User> findTop2By(Sort sort);

	/**
	 * Return all the users with the given firstname or lastname. Makes use of
	 * SpEL (Spring Expression Language).
	 *
	 * @param user
	 * @return
	 */
	@Query("select u from User u where u.firstName = :#{#user.firstName} or u.lastName = :#{#user.lastName}")
	Iterable<User> findByFirstNameOrLastName(@Param("user") User user);

	@Async
	CompletableFuture<List<User>> readAllBy();
}