package com.sparta.outsourcing.domain.restaurant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.sparta.outsourcing.domain.member.model.Member;
import com.sparta.outsourcing.domain.member.model.MemberRole;
import com.sparta.outsourcing.domain.member.repository.member.MemberRepository;
import com.sparta.outsourcing.domain.restaurant.data.MemberMockData;
import com.sparta.outsourcing.domain.restaurant.data.RestaurantsMockData;
import com.sparta.outsourcing.domain.restaurant.dto.RestaurantsRequestDto;
import com.sparta.outsourcing.domain.restaurant.dto.RestaurantsResponseDto;
import com.sparta.outsourcing.domain.restaurant.entity.Restaurants;
import com.sparta.outsourcing.domain.restaurant.repository.RestaurantsRepository;
import com.sparta.outsourcing.global.exception.CustomException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//@SpringBootTest
//// 단위테스트, 통합테스트
@ExtendWith(MockitoExtension.class)
class RestaurantsServiceImplTest {

  @InjectMocks
  private RestaurantsServiceImpl restaurantsServiceImpl;

  @Mock
  RestaurantsRepository restaurantsRepository;
  @Mock
  MemberRepository memberRepository;

  @Nested // 중첩클래스정의
  class CreateRestaurntTest {

    @Test
    void 가게등록실패_유저권한() {

      // given
      MemberRole role = MemberRole.USER;

      Member member = MemberMockData.getMemberUserRoleEntity();
      // reflection 문법 최대한 자제, 성능 저하, 오브젝트 바이트 변환이라서 코드의방어선을 무너뜨림,private도 강제주입이가능한수준

      RestaurantsRequestDto requestDto = RestaurantsMockData.getRestaurantDto();

      given(memberRepository.findMemberOrElseThrow(anyString())).willReturn(member);

      // when
      Exception exception = assertThrows(CustomException.class, () -> {
        restaurantsServiceImpl.createRestaurant(requestDto, member.getEmail());
      });
      // then
      assertEquals("[CUSTOM ERROR] 해당 유저의 권한이 없습니다.", exception.getMessage());
    }

    @Test
    void 가게정상등록_관리자권한() {

      // given, (입력으로 requestDto, email을 받기위해 test 데이터 생성)
      MemberRole role = MemberRole.ADMIN;
      RestaurantsRequestDto requestDto = RestaurantsMockData.getRestaurantDto();

      Member member = MemberMockData.getMemberAdminRoleEntity();

      Restaurants restaurants = RestaurantsMockData.getRestaurantEntity();

      given(memberRepository.findMemberOrElseThrow(anyString())).willReturn(member);
      given(restaurantsRepository.save(any())).willReturn(restaurants);

      // when
      RestaurantsResponseDto result = restaurantsServiceImpl.createRestaurant(requestDto,
          member.getEmail());

      // then
      verify(restaurantsRepository, times(1)).save(ArgumentMatchers.any(Restaurants.class));
      assertThat(result.getName()).isEqualTo(restaurants.getName());
    }
  }

  @Nested
  class GetRestaurantTest {

    @Test
    void 가게조회실패_가게없을때() {

      // given
      Restaurants restaurants = RestaurantsMockData.getRestaurantEntity();
      given(restaurantsRepository.findById(anyLong())).willReturn(Optional.empty());
      // when
      Exception exception = assertThrows(CustomException.class, () -> {
        restaurantsServiceImpl.getRestaurant(restaurants.getRestaurantId());
      });
      // then
      assertEquals("[CUSTOM ERROR] 가게가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    void 가게조회_관리자_유저_모두가능() {

      // given
      Restaurants restaurants = RestaurantsMockData.getRestaurantEntity();
      given(restaurantsRepository.findById(anyLong())).willReturn(Optional.of(restaurants));
      // when
      RestaurantsResponseDto result = restaurantsServiceImpl.getRestaurant(
          restaurants.getRestaurantId());
      // then
      assertEquals(result.getRestaurantId(), restaurants.getRestaurantId());
    }

    @Test
    void 가게전체정상조회() {

      // given
      List<Restaurants> restaurantsList = RestaurantsMockData.getRestaurantList();
      given(restaurantsRepository.findAllByOrderByRestaurantId()).willReturn(restaurantsList);
      // when
      List<RestaurantsResponseDto> result = restaurantsServiceImpl.getRestaurantList();
      // then
      for (int i = 0; i < 3; i++) {
        assertEquals(result.get(i).getRestaurantId(), restaurantsList.get(i).getRestaurantId());
        assertEquals(result.get(i).getName(), restaurantsList.get(i).getName());
      }
    }
  }

  @Nested
  class UpdateRestaurantTest {

    @Test
    void 가게수정실패_유저권한() {
      // given

      MemberRole role = MemberRole.USER;
      Member member = MemberMockData.getMemberUserRoleEntity();
      Restaurants restaurants = RestaurantsMockData.getRestaurantEntity();
      RestaurantsRequestDto requestDto = RestaurantsMockData.getRestaurantDto();
      given(memberRepository.findMemberOrElseThrow(anyString())).willReturn(member);

      // when
      Exception exception = assertThrows(CustomException.class, () -> {
        restaurantsServiceImpl.updateRestaurant(restaurants.getRestaurantId(), requestDto,
            member.getEmail());
      });
      // then
      assertEquals("[CUSTOM ERROR] 해당 유저의 권한이 없습니다.", exception.getMessage());

      // then
    }

    @Test
    void 가게수정성공_관리자권한() {

      // given
      MemberRole role = MemberRole.ADMIN;
      Member member = MemberMockData.getMemberAdminRoleEntity();
      Restaurants restaurants = RestaurantsMockData.getRestaurantEntity();
      RestaurantsRequestDto requestDto = RestaurantsMockData.getRestaurantDto();
      given(memberRepository.findMemberOrElseThrow(anyString())).willReturn(member);
      given(restaurantsRepository.findById(anyLong())).willReturn(Optional.of(restaurants));
      restaurants.update(requestDto);
      // when
      RestaurantsResponseDto result = restaurantsServiceImpl.updateRestaurant(
          restaurants.getRestaurantId(), requestDto,
          member.getEmail());
      // then
      assertEquals(result.getRestaurantId(), restaurants.getRestaurantId());
      assertEquals(result.getName(), restaurants.getName());
    }

  }

  @Nested
  class DeleteRestaurantTest {

    @Test
    void 가게삭제실패_유저권한() {
      // given
      MemberRole role = MemberRole.USER;
      Restaurants restaurants = RestaurantsMockData.getRestaurantEntity();
      Member member = MemberMockData.getMemberUserRoleEntity();
      given(memberRepository.findMemberOrElseThrow(anyString())).willReturn(member);
      // when
      Exception exception = assertThrows(CustomException.class, () -> {
        restaurantsServiceImpl.deleteRestaurant(restaurants.getRestaurantId(), member.getEmail());
      });
      // then
      assertEquals("[CUSTOM ERROR] 해당 유저의 권한이 없습니다.", exception.getMessage());
    }

    @Test
    void 가게삭제실패_관리자권한_가게없을떄() {
      // given

      MemberRole role = MemberRole.ADMIN;
      Restaurants restaurants = RestaurantsMockData.getRestaurantEntity();
      Member member = MemberMockData.getMemberAdminRoleEntity();
      given(memberRepository.findMemberOrElseThrow(anyString())).willReturn(member);
      given(restaurantsRepository.findById(anyLong())).willReturn(Optional.empty());
      // when
      Exception exception = assertThrows(CustomException.class, () -> {
        restaurantsServiceImpl.deleteRestaurant(restaurants.getRestaurantId(), member.getEmail());
      });// then
      assertEquals("[CUSTOM ERROR] 가게가 존재하지 않습니다.", exception.getMessage());

    }

    @Test
    void 가게삭제성공_관리자권한() {
      // given

      MemberRole role = MemberRole.ADMIN;
      Restaurants restaurants = RestaurantsMockData.getRestaurantEntity();
      Member member = MemberMockData.getMemberAdminRoleEntity();
      given(memberRepository.findMemberOrElseThrow(anyString())).willReturn(member);
      given(restaurantsRepository.findById(anyLong())).willReturn(Optional.of(restaurants));
      // when
      Long result = restaurantsServiceImpl.deleteRestaurant(restaurants.getRestaurantId(),
          member.getEmail());
      // then
      assertEquals(result, restaurants.getRestaurantId());
    }
  }

}