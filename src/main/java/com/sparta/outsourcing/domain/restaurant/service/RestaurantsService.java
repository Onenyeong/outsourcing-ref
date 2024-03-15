package com.sparta.outsourcing.domain.restaurant.service;

import com.sparta.outsourcing.domain.restaurant.dto.RestaurantsRequestDto;
import com.sparta.outsourcing.domain.restaurant.dto.RestaurantsResponseDto;
import java.util.List;


public interface RestaurantsService {


//  /**
//   *
//   * @param restaurantsRequestDto
//   * @param email
//   * @return
//   */

  // 슬래시 별별 엔터

  RestaurantsResponseDto createRestaurant(RestaurantsRequestDto restaurantsRequestDto,String email);

  RestaurantsResponseDto getRestaurant(Long restaurantId);



  RestaurantsResponseDto updateRestaurant(Long restaurantId,
      RestaurantsRequestDto restaurantsRequestDto,String email);

  List<RestaurantsResponseDto> getRestaurantList(); // for each문이랑 유사



  long deleteRestaurant(Long restaurantId,String email);


  List<RestaurantsResponseDto> findAllByRestaurantsPage(int page, int size);
}
