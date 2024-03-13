package com.sparta.outsourcing.domain.restaurant.service;

import com.sparta.outsourcing.domain.member.model.MemberRole;
import com.sparta.outsourcing.domain.member.repository.member.MemberRepository;
import com.sparta.outsourcing.domain.restaurant.aspect.AopTimeAspect;
import com.sparta.outsourcing.domain.restaurant.aspect.TransactionAspect;
import com.sparta.outsourcing.domain.restaurant.dto.RestaurantsRequestDto;
import com.sparta.outsourcing.domain.restaurant.dto.RestaurantsResponseDto;
import com.sparta.outsourcing.domain.restaurant.entity.Restaurants;
import com.sparta.outsourcing.domain.restaurant.repository.RestaurantsRepository;
import com.sparta.outsourcing.global.exception.CustomError;
import com.sparta.outsourcing.global.exception.CustomException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantsServiceImpl implements RestaurantsService {

  private final RestaurantsRepository restaurantsRepository;
  private final MemberRepository memberRepository;
  private final TransactionAspect transactionAspect;
  private final AopTimeAspect aopTimeAspect;

  /**
   *
   * @param restaurantsRequestDto
   * @param email
   * @return
   */
  @Override
  public RestaurantsResponseDto createRestaurant(RestaurantsRequestDto restaurantsRequestDto,
      String email) {

    checkRoleByEmail(email);
    Restaurants restaurants = restaurantsRequestDto.toEntity();
    restaurantsRepository.save(restaurants);
    return new RestaurantsResponseDto(restaurants);
  }

  /**
   *
   * @param restaurantId
   * @return
   */
  @Override
  public RestaurantsResponseDto getRestaurant(Long restaurantId) {

    Restaurants foundRestaurant = findByRestaurantId(restaurantId);
    RestaurantsResponseDto restaurantsResponseDto = new RestaurantsResponseDto(foundRestaurant);
    return restaurantsResponseDto;
  }

  /**
   *
   * @param restaurantId
   * @param restaurantsRequestDto
   * @param email
   * @return
   */
  @Override
  public RestaurantsResponseDto updateRestaurant(Long restaurantId,
      RestaurantsRequestDto restaurantsRequestDto, String email) {
    checkRoleByEmail(email);      // 이걸 어떻게 aop랑 분리시키지??

    Restaurants restaurants = findByRestaurantId(restaurantId);
    restaurants.update(restaurantsRequestDto);

    return new RestaurantsResponseDto(restaurants);

  }

  /**
   *
   * @return
   */
  @Override
  public List<RestaurantsResponseDto> getRestaurantList() {
    return restaurantsRepository.findAllByOrderByRestaurantId().stream()
        .map(RestaurantsResponseDto::new).toList();
  }

  /**
   *
   * @param restaurantId
   * @param email
   * @return
   */
  @Override
  public long deleteRestaurant(Long restaurantId, String email) {
    checkRoleByEmail(email);

    Restaurants restaurants = findByRestaurantId(restaurantId);

    restaurantsRepository.deleteById(restaurants.getRestaurantId());
    return restaurantId;
  }

  @Override
  public Page<RestaurantsResponseDto> getRestaurantListByPage(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
    Pageable pageRequest = createPageRequestUsing(page, size);

    List<Restaurants> restaurantsList = restaurantsRepository.findAll();
    int start = (int) pageRequest.getOffset();
    int end = Math.min((start + pageRequest.getPageSize()), restaurantsList.size());

    List<Restaurants> pageContent = restaurantsList.subList(start, end);
    return new PageImpl<>(pageContent, pageRequest, restaurantsList.size());
  }



  /**
   *
   * @param restaurantId
   * @return
   */
  private Restaurants findByRestaurantId(Long restaurantId) {
    return restaurantsRepository.findById(restaurantId)
        .orElseThrow(() -> new CustomException(CustomError.RESTAURANT_NOT_EXIST));
  }


  private void checkRoleByEmail(String email){
    if (memberRepository.findMemberOrElseThrow(email).getRole() != MemberRole.ADMIN) {
      throw new CustomException(CustomError.NO_AUTH);
    }
  }
  private Pageable createPageRequestUsing(int page, int size) {
    return PageRequest.of(page, size);
  }
}
