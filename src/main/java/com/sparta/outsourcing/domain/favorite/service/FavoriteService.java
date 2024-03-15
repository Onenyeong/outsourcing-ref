package com.sparta.outsourcing.domain.favorite.service;

import static com.sparta.outsourcing.global.exception.CustomError.ALREADY_REGISTER_FAVORITE;
import static com.sparta.outsourcing.global.exception.CustomError.MEMBER_NOT_EXISTS;
import static com.sparta.outsourcing.global.exception.CustomError.NOT_EXIST_FAVORITE;
import static com.sparta.outsourcing.global.exception.CustomError.RESTAURANT_NOT_EXIST;

import com.sparta.outsourcing.domain.favorite.model.dto.FavoriteRequestDto;
import com.sparta.outsourcing.domain.favorite.model.dto.FavoriteResponseDto;
import com.sparta.outsourcing.domain.favorite.model.entity.Favorite;
import com.sparta.outsourcing.domain.favorite.repository.FavoriteRepository;
import com.sparta.outsourcing.domain.member.model.entity.MemberEntity;
import com.sparta.outsourcing.domain.member.repository.member.MemberJpaRepository;
import com.sparta.outsourcing.domain.restaurant.repository.RestaurantsRepository;
import com.sparta.outsourcing.global.exception.CustomException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

  private final FavoriteRepository favoriteRepository;
  private final RestaurantsRepository restaurantsRepository;
  private final MemberJpaRepository memberJpaRepository;

  public FavoriteResponseDto createFavorite(FavoriteRequestDto requestDto,
      UserDetails userDetails) {
    MemberEntity member = memberJpaRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

    if (!restaurantsRepository.existsById(requestDto.getRestaurantId())) {
      throw new CustomException(RESTAURANT_NOT_EXIST);
    }

    boolean favoriteExists = favoriteRepository.existsByMemberIdAndRestaurantId(member.getId(),
        requestDto.getRestaurantId());
    if (favoriteExists) {
      throw new CustomException(ALREADY_REGISTER_FAVORITE);
    }

    Favorite favorite = getFavorite(requestDto,
        member);

    Favorite savedFavorite = favoriteRepository.save(favorite);
    return new FavoriteResponseDto(savedFavorite.getId(), savedFavorite.getMemberId(),
        savedFavorite.getRestaurantId(), savedFavorite.getCreatedDate());
  }

  private Favorite getFavorite(FavoriteRequestDto requestDto, MemberEntity member) {
    Favorite favorite = new Favorite();
    favorite.setMemberId(member.getId());
    favorite.setRestaurantId(requestDto.getRestaurantId());
    favorite.setCreatedDate(LocalDate.now());
    return favorite;
  }

  public List<FavoriteResponseDto> findAllFavoritesByMemberId(UserDetails userDetails) {
    MemberEntity member = memberJpaRepository.findByEmail(userDetails.getUsername()).orElseThrow(
        () -> new CustomException(MEMBER_NOT_EXISTS)
    );

    List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
    return favorites.stream()
        .map(favorite -> new FavoriteResponseDto(favorite.getId(), favorite.getMemberId(),
            favorite.getRestaurantId(), favorite.getCreatedDate()))
        .collect(Collectors.toList());
  }

  public FavoriteResponseDto findFavoritesByRestaurantId(Long restaurantId,
      UserDetails userDetails) {
    MemberEntity member = memberJpaRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

    if (!restaurantsRepository.existsById(restaurantId)) {
      throw new CustomException(RESTAURANT_NOT_EXIST);
    }

    Favorite favorite = favoriteRepository.findByRestaurantIdAndMemberId(restaurantId,
            member.getId())
        .orElseThrow(() -> new CustomException(NOT_EXIST_FAVORITE));

    return new FavoriteResponseDto(favorite.getId(), favorite.getMemberId(),
        favorite.getRestaurantId(), favorite.getCreatedDate());
  }

  public void deleteFavorite(Long restaurantId, UserDetails userDetails) {

    MemberEntity member = memberJpaRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

    Favorite favorite = favoriteRepository.findByRestaurantIdAndMemberId(restaurantId,
            member.getId())
        .orElseThrow(() -> new CustomException(NOT_EXIST_FAVORITE));

    favoriteRepository.delete(favorite);
  }

}
