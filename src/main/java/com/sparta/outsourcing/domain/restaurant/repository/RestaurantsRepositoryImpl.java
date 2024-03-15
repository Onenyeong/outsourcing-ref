package com.sparta.outsourcing.domain.restaurant.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.outsourcing.domain.restaurant.entity.QRestaurants;
import com.sparta.outsourcing.domain.restaurant.entity.Restaurants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RestaurantsRepositoryImpl implements RestaurantsRepositoryCustom{

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Restaurants> findAllByRestaurantsPage(long offset, int pageSize) {
    QRestaurants restaurants = QRestaurants.restaurants;

    return jpaQueryFactory.selectFrom(restaurants)
        .offset(offset)
        .limit(pageSize)
        .fetch();
  }
}
