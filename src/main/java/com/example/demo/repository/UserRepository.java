package com.example.demo.repository;

import java.util.Optional;
import java.util.Set;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.models.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailIgnoreCase(String emailId);
    Boolean existsByUserNickName(String username);
    Boolean existsByEmail(String email);
    @Query(nativeQuery = true, value = "select * from users u join user_roles ur on u.id = ur.user_id where ur.role_id = 3" )
    User findAdmin();
}
