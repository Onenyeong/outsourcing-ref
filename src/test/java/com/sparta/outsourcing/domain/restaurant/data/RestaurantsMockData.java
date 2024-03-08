package com.sparta.outsourcing.domain.restaurant.data;

import com.sparta.outsourcing.domain.restaurant.dto.RestaurantsRequestDto;
import com.sparta.outsourcing.domain.restaurant.entity.Restaurants;
import java.util.Arrays;
import java.util.List;

public class RestaurantsMockData {

  public static RestaurantsRequestDto getRestaurantDto(){

    return new RestaurantsRequestDto("띵호와","중식","인천 부평구","010-4920-2901");

  }

  public static Restaurants getRestaurantEntity(){

    return new Restaurants(1L,"띵호와","중식","인천 부평구","010-4920-2901");

  }

  public static List<Restaurants> getRestaurantList() {
    List<Restaurants> restaurantsList = Arrays.asList(
        new Restaurants(1L,"땅땅치킨 1호점","한식","인천 계양구","010-4789-0722"),
        new Restaurants(2L,"땅땅치킨 2호점","한식","인천 부평구","010-4788-0722"),
        new Restaurants(3L,"땅땅치킨 3호점","한식","인천 남구","010-4787-0722")
    );
    return restaurantsList;
  }


}
