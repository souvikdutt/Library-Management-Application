package com.epam.bookmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epam.bookmanager.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	
}
