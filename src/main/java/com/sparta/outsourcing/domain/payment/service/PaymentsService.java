package com.sparta.outsourcing.domain.payment.service;

import static com.sparta.outsourcing.global.exception.CustomError.NOT_EXIST_PAYMENT;

import com.sparta.outsourcing.domain.payment.dto.PaymentsResponseDto;
import com.sparta.outsourcing.domain.payment.entity.Payments;
import com.sparta.outsourcing.domain.payment.repository.PaymentsJpaRepository;
import com.sparta.outsourcing.global.exception.CustomException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentsService {

  private final PaymentsJpaRepository paymentsRepository;

  public PaymentsResponseDto getPayment(Long id) {
    Payments payments = findByPaymentId(id);

    PaymentsResponseDto paymentsResponseDto = new PaymentsResponseDto(payments);
    return paymentsResponseDto;
  }

  public List<PaymentsResponseDto> getPaymentList() {
    return paymentsRepository.findAllByOrderById().stream()
        .map(PaymentsResponseDto::new).toList();
  }

  public Long deletePayment(Long id) {

    deleteByPaymentId(id);
    return id;
  }


  private Payments findByPaymentId(Long id) {
    return paymentsRepository.findById(id)
        .orElseThrow(() -> new CustomException(NOT_EXIST_PAYMENT));
  }

  private void deleteByPaymentId(Long paymentId) {

    Optional<Payments> paymentOptional = paymentsRepository.findById(paymentId);

    if (paymentOptional.isEmpty()) {
      throw new CustomException(NOT_EXIST_PAYMENT);
    } else {
      paymentsRepository.deleteById(paymentId);
    }
  }


}
