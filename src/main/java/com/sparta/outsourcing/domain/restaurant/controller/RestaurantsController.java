package com.sparta.outsourcing.domain.restaurant.controller;


import static com.sparta.outsourcing.global.success.SuccessCode.SUCCESS_DELETE_RESTAURANT;
import static com.sparta.outsourcing.global.success.SuccessCode.SUCCESS_SEARCH_RESTAURANT;
import static com.sparta.outsourcing.global.success.SuccessCode.SUCCESS_UPDATE_RESTAURANT;

import com.sparta.outsourcing.domain.restaurant.dto.RestaurantsRequestDto;
import com.sparta.outsourcing.domain.restaurant.dto.RestaurantsResponseDto;
import com.sparta.outsourcing.domain.restaurant.entity.CommonResponse;
import com.sparta.outsourcing.domain.restaurant.entity.Restaurants;
import com.sparta.outsourcing.domain.restaurant.service.RestaurantsService;
import com.sparta.outsourcing.domain.restaurant.service.RestaurantsServiceImpl;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class RestaurantsController {

  private final RestaurantsService restaurantsService;
  private final MessageSource messageSource;
  @PostMapping("/restaurants")

  public ResponseEntity<CommonResponse<RestaurantsResponseDto>> createRestaurant(
      @RequestBody RestaurantsRequestDto requestDto,
      @AuthenticationPrincipal UserDetails userDetails) {
    RestaurantsResponseDto restaurantsResponseDto = restaurantsService.createRestaurant(requestDto,
        userDetails.getUsername());

    return ResponseEntity.ok().body(
        CommonResponse.<RestaurantsResponseDto>builder().code(HttpStatus.OK.value()).message(
            messageSource.getMessage("messageByCreate",null,LocaleContextHolder.getLocale())).data(restaurantsResponseDto).build());
  }

  @GetMapping("/restaurants/{restaurantId}")
  public ResponseEntity<CommonResponse<RestaurantsResponseDto>> getRestaurant(
      @PathVariable Long restaurantId) {
    RestaurantsResponseDto restaurantsResponseDto = restaurantsService.getRestaurant(restaurantId);

    return ResponseEntity.ok().body(
        CommonResponse.<RestaurantsResponseDto>builder().code(HttpStatus.OK.value())
            .message(messageSource.getMessage("messageByRead",null,LocaleContextHolder.getLocale())).data(restaurantsResponseDto).build());
  }
  @GetMapping("/restaurants")
  public ResponseEntity<Page<RestaurantsResponseDto>> getRestaurants(@RequestParam(defaultValue = "0") int page,@PageableDefault(size = 10) int size){
    Page<RestaurantsResponseDto> restaurantsPage = restaurantsService.getRestaurantListByPage(page, size);
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Page-Number", String.valueOf(restaurantsPage.getNumber()));
    headers.add("X-Page-Size", String.valueOf(restaurantsPage.getSize()));

    return ResponseEntity.ok().headers(headers).body(restaurantsPage);
  }

  @GetMapping("admin/restaurants")
  public ResponseEntity<CommonResponse<List<RestaurantsResponseDto>>> getRestaurantList() {
    List<RestaurantsResponseDto> restaurantsResponseDto = restaurantsService.getRestaurantList();

    return ResponseEntity.ok().body(
        CommonResponse.<List<RestaurantsResponseDto>>builder().code(HttpStatus.OK.value())
            .message(messageSource.getMessage("messageByRead",null,LocaleContextHolder.getLocale())).data(restaurantsResponseDto).build());
  }


  @PutMapping("/restaurants/{restaurantId}")
  public ResponseEntity<CommonResponse<RestaurantsResponseDto>> updateRestaurant(
      @PathVariable Long restaurantId,
      @RequestBody RestaurantsRequestDto restaurantsRequestDto,
      @AuthenticationPrincipal UserDetails userDetails) {

    RestaurantsResponseDto restaurantsResponseDto = restaurantsService.updateRestaurant(
        restaurantId, restaurantsRequestDto, userDetails.getUsername());

    return ResponseEntity.ok().body(
        CommonResponse.<RestaurantsResponseDto>builder().code(HttpStatus.OK.value())
            .message(messageSource.getMessage("messageByUpdate",null,LocaleContextHolder.getLocale())).data(restaurantsResponseDto).build());
  }

  @DeleteMapping("/restaurants/{restaurantId}")
  public ResponseEntity<CommonResponse> deleteRestaurant(@PathVariable Long restaurantId,
      @AuthenticationPrincipal UserDetails userDetails) {
    restaurantsService.deleteRestaurant(restaurantId, userDetails.getUsername());

    return ResponseEntity.ok().body(
        CommonResponse.<String>builder().code(HttpStatus.OK.value())
            .message(SUCCESS_DELETE_RESTAURANT.getMessage()).build());
  }
}
