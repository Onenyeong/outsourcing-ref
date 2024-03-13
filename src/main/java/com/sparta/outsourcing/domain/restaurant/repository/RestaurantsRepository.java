package com.sparta.outsourcing.domain.restaurant.repository;

import com.sparta.outsourcing.domain.restaurant.entity.Restaurants;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantsRepository extends JpaRepository<Restaurants,Long> {
  List<Restaurants> findAllByOrderByRestaurantId();
  Page<Restaurants> findAllByOrderByRestaurantId(Pageable pageable);
}
