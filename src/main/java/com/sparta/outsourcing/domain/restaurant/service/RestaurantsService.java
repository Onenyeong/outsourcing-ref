package com.sparta.outsourcing.domain.restaurant.service;

import static com.sparta.outsourcing.global.success.SuccessCode.SUCCESS_CREATE_RESTAURANT;

import com.sparta.outsourcing.domain.member.model.MemberRole;
import com.sparta.outsourcing.domain.member.repository.member.MemberRepository;
import com.sparta.outsourcing.domain.restaurant.aspect.AopTimeAspect;
import com.sparta.outsourcing.domain.restaurant.aspect.AuthorizationAspect;
import com.sparta.outsourcing.domain.restaurant.aspect.TransactionAspect;
import com.sparta.outsourcing.domain.restaurant.dto.RestaurantsRequestDto;
import com.sparta.outsourcing.domain.restaurant.dto.RestaurantsResponseDto;
import com.sparta.outsourcing.domain.restaurant.entity.CommonResponse;
import com.sparta.outsourcing.domain.restaurant.entity.Restaurants;
import com.sparta.outsourcing.domain.restaurant.repository.RestaurantsRepository;
import com.sparta.outsourcing.global.exception.CustomError;
import com.sparta.outsourcing.global.exception.CustomException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantsService {

  private final RestaurantsRepository restaurantsRepository;
  private final MemberRepository memberRepository;
  private final AuthorizationAspect authorizationAspect;
  private final TransactionAspect transactionAspect;
  private final AopTimeAspect aopTimeAspect;

  /**
   *
   * @param restaurantsRequestDto
   * @param email
   * @return
   */

  // 슬래시 별별 엔터
  @Transactional
  public RestaurantsResponseDto createRestaurant(RestaurantsRequestDto restaurantsRequestDto,
      String email) {

    if (memberRepository.findMemberOrElseThrow(email).getRole() != (MemberRole.ADMIN)) {
      throw new CustomException(CustomError.NO_AUTH);
    }

    Restaurants restaurants = restaurantsRequestDto.toEntity();

    restaurantsRepository.save(restaurants);
    log.info("생성 성공했음");
    return new RestaurantsResponseDto(restaurants);
  }

  // CommonResponse를 생성하는 메서드
  private CommonResponse<RestaurantsResponseDto> buildCommonResponse(RestaurantsResponseDto dto) {
    return CommonResponse.<RestaurantsResponseDto>builder()
        .code(HttpStatus.OK.value())
        .message(SUCCESS_CREATE_RESTAURANT.getMessage())
        .data(dto)
        .build();
  }

  // 1. dto

  public RestaurantsResponseDto getRestaurant(Long restaurantId) {

    Restaurants foundRestaurant = findByRestaurantId(restaurantId);
    RestaurantsResponseDto restaurantsResponseDto = new RestaurantsResponseDto(foundRestaurant);
    return restaurantsResponseDto;
  }

  private Restaurants findByRestaurantId(Long restaurantId) {
    return restaurantsRepository.findById(restaurantId)
        .orElseThrow(() -> new NoSuchElementException("예외발생!!"));
  }


  @Transactional
  public RestaurantsResponseDto updateRestaurant(Long restaurantId,
      RestaurantsRequestDto restaurantsRequestDto, String email) {

    if (memberRepository.findMemberOrElseThrow(email).getRole() != MemberRole.ADMIN) {
      throw new CustomException(CustomError.NO_AUTH);
    }
    Restaurants restaurants = findByRestaurantId(restaurantId);
    restaurants.update(restaurantsRequestDto);

    return new RestaurantsResponseDto(restaurants);

  }

  public List<RestaurantsResponseDto> getRestaurantList() {
    return restaurantsRepository.findAllByOrderByRestaurantId().stream()
        .map(RestaurantsResponseDto::new).toList();

  } // for each문이랑 유사

  @Transactional
  public long deleteRestaurant(Long restaurantId, String email) {
    if (memberRepository.findMemberOrElseThrow(email).getRole() != (MemberRole.ADMIN)) {
      throw new CustomException(CustomError.NO_AUTH);
    }
    deleteByRestaurantId(restaurantId);

    return restaurantId;
  }
  private void deleteByRestaurantId(Long restaurantId) {
    Optional<Restaurants> restaurantsOptional = restaurantsRepository.findById(restaurantId);

    if (restaurantsOptional.isEmpty()) {
      throw new CustomException(CustomError.RESTAURANT_NOT_EXIST);
    }

    restaurantsRepository.deleteById(restaurantId);

  }
}
