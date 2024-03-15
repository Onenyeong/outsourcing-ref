package com.sparta.outsourcing.domain.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Builder
@SQLRestriction("deleted_date IS NULL")
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "update payments set deleted = true,deleted_date = NOW() where id= ?")
public class Payments {



  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @NotNull
  private Long orderId;


  @Column
  @NotNull
  private Integer totalPrice;

  @Column
  @CreatedDate
  @NotNull
  private LocalDateTime createdDate;

  @Column
  private LocalDateTime deletedDate;

  @Column(nullable = false)
  private boolean deleted = false;


}
