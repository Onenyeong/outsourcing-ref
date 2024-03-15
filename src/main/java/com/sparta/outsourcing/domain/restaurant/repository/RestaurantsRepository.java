package com.sparta.outsourcing.domain.restaurant.repository;

import com.sparta.outsourcing.domain.restaurant.entity.Restaurants;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantsRepository extends JpaRepository<Restaurants,Long> {
  List<Restaurants> findAllByOrderByRestaurantId();

  // 레스토랑아이디와 좋아요 개수를 담을수있는 responsedto생성
  // 서로의 아이디로 조인

}
