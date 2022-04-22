package com.epam.librarymanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.epam.librarymanager.model.Library;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Integer> {

	@Query(value = "select book_id from library where username=?1",nativeQuery = true)
	List<Integer> findBookIdByUsername(String username);

	@Modifying
	void deleteAllByBookId(int book_id);
	
	@Modifying
	void deleteByBookIdAndUsername(int book_id, String username);

	@Modifying
	void deleteByUsername(String username);

	@Query(value = "select count(*) from library group by username=?1",nativeQuery = true)
	int countIssuedBooksToUser(String username);

	Library findByBookIdAndUsername(int book_id, String username);

	boolean existsByBookIdAndUsername(int book_id, String username);

	boolean existsByUsername(String username);

}
