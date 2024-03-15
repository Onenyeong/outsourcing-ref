package com.sparta.outsourcing.domain.restaurant.service;

import com.sparta.outsourcing.domain.member.model.MemberRole;
import com.sparta.outsourcing.domain.member.repository.member.MemberRepository;
import com.sparta.outsourcing.domain.restaurant.aspect.AopTimeAspect;
import com.sparta.outsourcing.domain.restaurant.aspect.TransactionAspect;
import com.sparta.outsourcing.domain.restaurant.dto.RestaurantsRequestDto;
import com.sparta.outsourcing.domain.restaurant.dto.RestaurantsResponseDto;
import com.sparta.outsourcing.domain.restaurant.entity.Restaurants;
import com.sparta.outsourcing.domain.restaurant.repository.RestaurantsRepository;
import com.sparta.outsourcing.domain.restaurant.repository.RestaurantsRepositoryCustom;
import com.sparta.outsourcing.global.exception.CustomError;
import com.sparta.outsourcing.global.exception.CustomException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantsServiceImpl implements RestaurantsService {

  private final RestaurantsRepository restaurantsRepository;
  private final RestaurantsRepositoryCustom restaurantsRepositoryCustom;
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
// 생성자 주입과 빌더 패턴의차이
  // 빌더가 유연할것같다 생성자 주입시 데이터 변경하면 생성자도 변경해야한다. 많이쓰는건 생성자 주입
  // 둘다 결과는 같지만, 가시적으로 빌더가 유리하다. 절대적으로 데이터가 필요할땐 생성자주입으로하는게낫다.
  // setter를 지양하지만 안쓰는건아니다. ex) 수정할게 너무많을때마다 메서드를 만들어서 데이터를 우회적으로 가져와야하니까 불편하다
  // 무분별한 setter사용을 줄이자
  // builder vs setter -> builder를 더 선호
  // builder : private 데이터들의 집합체 (public), 어떤걸 세팅할지 명시적으로 보여줌
  // setter : public 데이터를 set, 접근하기가 쉬움, 너무쉽게 바꿀수가있음,어떤 작업을 하는건지 잘 알수 없음

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
  public List<RestaurantsResponseDto> findAllByRestaurantsPage(int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size);

    return restaurantsRepositoryCustom.findAllByRestaurantsPage(pageRequest.getOffset(),pageRequest.getPageSize()).stream().map(RestaurantsResponseDto::new).collect(
        Collectors.toList());
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
