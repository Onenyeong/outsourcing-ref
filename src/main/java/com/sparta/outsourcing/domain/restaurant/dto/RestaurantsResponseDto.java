package com.sparta.outsourcing.domain.restaurant.dto;

import com.sparta.outsourcing.domain.restaurant.entity.Restaurants;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // 보이지않게 자동적으로 set을하는 부분이 있음. setter를 써놓으면 마음대로 값이 변경되므로 다른부분들도 연쇄적으로 쇼트남 부분적으로 set을 했을땐 이점을 막을수있다.
public class RestaurantsResponseDto {
  private Long restaurantId;
  private String name;
  private String category;
  private String address;
  private String number;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
  private LocalDateTime deletedDate;

  public RestaurantsResponseDto (Restaurants restaurants) {
    this.restaurantId = restaurants.getRestaurantId();
    this.name = restaurants.getName();
    this.category = restaurants.getCategory();
    this.address = restaurants.getAddress();
    this.number = restaurants.getNumber();
    this.createdDate = restaurants.getCreatedDate();
    this.updatedDate = restaurants.getUpdatedDate();
    this.deletedDate = restaurants.getDeletedDate();
  }



}
