package com.epam.usermanager.reporsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epam.usermanager.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
