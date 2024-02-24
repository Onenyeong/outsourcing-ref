package com.sparta.outsourcing.domain.member.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

  private Long id;
  private String email;
  private String nickname;
  private String password;
  private String address;
  private String number;
}
