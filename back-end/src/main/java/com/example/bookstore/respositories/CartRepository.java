package com.example.bookstore.respositories;

import com.example.bookstore.entities.CartEntity;
import com.example.bookstore.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {

    Optional<CartEntity> findByUser(UserEntity userEntity);
}
