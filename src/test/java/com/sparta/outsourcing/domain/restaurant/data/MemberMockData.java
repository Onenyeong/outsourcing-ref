package com.sparta.outsourcing.domain.restaurant.data;

import com.sparta.outsourcing.domain.member.model.Member;
import com.sparta.outsourcing.domain.member.model.MemberRole;

public class MemberMockData {

  public static Member getMemberUserRoleEntity() {

    return new Member(1L, "user@gmail.com", MemberRole.USER, "냥콩이", "user", "인천 부평구",
        "010-0899-2212");

  }

  public static Member getMemberAdminRoleEntity() {

    return new Member(2L, "admin@gmail.com", MemberRole.ADMIN, "냥멍이", "admin", "인천 부평구",
        "010-0899-2213");

  }
}
