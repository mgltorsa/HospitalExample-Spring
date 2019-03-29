package com.hospital.repositories;

import java.util.List;

import com.hospital.model.User;
import com.hospital.model.UserType;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository
 */
@Repository
public interface UserRepository extends CrudRepository<User,Long>{

    List<User> findByName(String name);    
    List<User> findByType(UserType type);
    
}