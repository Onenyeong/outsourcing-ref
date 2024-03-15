package com.sparta.outsourcing.domain.restaurant.repository;

import com.sparta.outsourcing.domain.restaurant.entity.Restaurants;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantsRepositoryCustom{
  List<Restaurants> findAllByRestaurantsPage(long offset, int pageSize);
}
