package com.hunnit_beasts.payment.adaptertest;

import com.hunnit_beasts.payment.adapter.out.persistence.entity.PaymentEntity;
import com.hunnit_beasts.payment.adapter.out.persistence.mapper.PaymentMapper;
import com.hunnit_beasts.payment.adapter.out.persistence.repository.PaymentJpaRepository;
import com.hunnit_beasts.payment.adapter.out.persistence.repository.PaymentPersistenceAdapter;
import com.hunnit_beasts.payment.domain.enums.SearchType;
import com.hunnit_beasts.payment.domain.enums.SortDirection;
import com.hunnit_beasts.payment.domain.model.money.Money;
import com.hunnit_beasts.payment.domain.model.payment.*;
import com.hunnit_beasts.payment.port.in.dto.PaymentSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("PaymentPersistenceAdapter 통합 테스트")
class PaymentPersistenceAdapterTest {

    @Autowired
    private PaymentPersistenceAdapter paymentPersistenceAdapter;

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    private PaymentEntity testPaymentEntity1;
    private PaymentEntity testPaymentEntity2;
    private PaymentEntity testPaymentEntity3;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PaymentPersistenceAdapter paymentPersistenceAdapter(
                PaymentJpaRepository repository,
                PaymentMapper mapper) {
            return new PaymentPersistenceAdapter(repository, mapper);
        }

        @Bean
        public PaymentMapper paymentMapper() {
            return new PaymentMapper(); // 실제 매퍼 사용
        }
    }

    @BeforeEach
    void setUp() {
        paymentJpaRepository.deleteAll();

        LocalDateTime now = LocalDateTime.now();

        testPaymentEntity1 = PaymentEntity.builder()
                .id("payment-1")
                .identifier("identifier-1")
                .orderName("Test Order 1")
                .amount(BigDecimal.valueOf(10000))
                .currency("KRW")
                .transactionId("tx-1")
                .pgProvider("portone")
                .pgTxId("pg-tx-1")
                .paymentMethod("CARD")
                .status("SUCCESS")
                .createdAt(now.minusHours(2))
                .completedAt(now.minusHours(2))
                .receiptUrl("https://receipt.example.com/1")
                .pgResponse("{\"pg_response\": \"test1\"}")
                .updatedAt(now.minusHours(2))
                .build();

        testPaymentEntity2 = PaymentEntity.builder()
                .id("payment-2")
                .identifier("identifier-2")
                .orderName("Test Order 2")
                .amount(BigDecimal.valueOf(20000))
                .currency("KRW")
                .transactionId("tx-2")
                .pgProvider("portone")
                .pgTxId("pg-tx-2")
                .paymentMethod("KAKAO_PAY")
                .status("PENDING")
                .createdAt(now.minusHours(1))
                .updatedAt(now.minusHours(1))
                .build();

        testPaymentEntity3 = PaymentEntity.builder()
                .id("payment-3")
                .identifier("identifier-3")
                .orderName("Another Order")
                .amount(BigDecimal.valueOf(30000))
                .currency("KRW")
                .transactionId("tx-3")
                .pgProvider("portone")
                .pgTxId("pg-tx-3")
                .paymentMethod("TOSS_PAY")
                .status("FAILED")
                .createdAt(now)
                .completedAt(now)
                .pgResponse("{\"pg_response\": \"test3\"}")
                .updatedAt(now)
                .build();

        paymentJpaRepository.saveAll(List.of(testPaymentEntity1, testPaymentEntity2, testPaymentEntity3));
    }

    @Test
    @DisplayName("결제 정보 저장 및 조회")
    void saveAndFind() {
        // given
        Payment payment = createPayment();

        // when
        Payment savedPayment = paymentPersistenceAdapter.save(payment);
        Optional<Payment> foundPayment = paymentPersistenceAdapter.findById(savedPayment.getId());

        // then
        assertThat(savedPayment).isNotNull();
        assertThat(foundPayment).isPresent();
        assertThat(foundPayment.get().getIdentifier().getValue()).isEqualTo("new-identifier");
        assertThat(foundPayment.get().getOrderName()).isEqualTo("New Order");
        assertThat(foundPayment.get().getAmount().getAmount()).isEqualByComparingTo(BigDecimal.valueOf(15000));
    }

    @Test
    @DisplayName("외부 식별자로 결제 조회")
    void findByIdentifier() {
        // when
        Optional<Payment> result = paymentPersistenceAdapter.findByIdentifier("identifier-1");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId().getValue()).isEqualTo("payment-1");
        assertThat(result.get().getOrderName()).isEqualTo("Test Order 1");
        assertThat(result.get().getStatusInfo().getStatus()).isEqualTo(PaymentStatus.SUCCESS);
    }

    @Test
    @DisplayName("검색어로 주문명 검색")
    void searchByOrderName() {
        // given
        PaymentSearchCriteria criteria = PaymentSearchCriteria.builder()
                .searchText("Test")
                .searchType(SearchType.ORDER_NAME)
                .page(0)
                .size(10)
                .sortField("createdAt")
                .sortDirection(SortDirection.DESC)
                .build();

        // when
        Page<Payment> result = paymentPersistenceAdapter.search(criteria);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getOrderName()).isEqualTo("Test Order 2");
        assertThat(result.getContent().get(1).getOrderName()).isEqualTo("Test Order 1");
    }

    @Test
    @DisplayName("결제 상태로 검색")
    void searchByStatus() {
        // given
        PaymentSearchCriteria criteria = PaymentSearchCriteria.builder()
                .status(PaymentStatus.SUCCESS)
                .page(0)
                .size(10)
                .build();

        // when
        Page<Payment> result = paymentPersistenceAdapter.search(criteria);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getStatusInfo().getStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(result.getContent().getFirst().getId().getValue()).isEqualTo("payment-1");
    }

    @Test
    @DisplayName("결제 수단으로 검색")
    void searchByPaymentMethod() {
        // given
        PaymentSearchCriteria criteria = PaymentSearchCriteria.builder()
                .paymentType("CARD")
                .page(0)
                .size(10)
                .build();

        // when
        Page<Payment> result = paymentPersistenceAdapter.search(criteria);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getProcessingInfo().getPayMethod()).isEqualTo("CARD");
    }

    @Test
    @DisplayName("날짜 범위로 검색")
    void searchByDateRange() {
        // given
        LocalDateTime startDate = LocalDateTime.now().minusHours(3);
        LocalDateTime endDate = LocalDateTime.now().minusMinutes(30);

        PaymentSearchCriteria criteria = PaymentSearchCriteria.builder()
                .startDate(startDate)
                .endDate(endDate)
                .page(0)
                .size(10)
                .build();

        // when
        Page<Payment> result = paymentPersistenceAdapter.search(criteria);

        // then
        assertThat(result.getContent()).hasSize(2); // payment-1, payment-2
        assertThat(result.getContent()).extracting(p -> p.getId().getValue())
                .containsExactlyInAnyOrder("payment-1", "payment-2");
    }

    @Test
    @DisplayName("복합 조건으로 검색")
    void searchWithMultipleConditions() {
        // given
        PaymentSearchCriteria criteria = PaymentSearchCriteria.builder()
                .searchText("Test")
                .searchType(SearchType.ORDER_NAME)
                .status(PaymentStatus.SUCCESS)
                .paymentType("CARD")
                .page(0)
                .size(10)
                .build();

        // when
        Page<Payment> result = paymentPersistenceAdapter.search(criteria);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getId().getValue()).isEqualTo("payment-1");
        assertThat(result.getContent().getFirst().getOrderName()).contains("Test");
        assertThat(result.getContent().getFirst().getStatusInfo().getStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(result.getContent().getFirst().getProcessingInfo().getPayMethod()).isEqualTo("CARD");
    }

    @Test
    @DisplayName("페이징 테스트")
    void searchWithPaging() {
        // given
        PaymentSearchCriteria criteria = PaymentSearchCriteria.builder()
                .page(0)
                .size(2)
                .sortField("createdAt")
                .sortDirection(SortDirection.DESC)
                .build();

        // when
        Page<Payment> result = paymentPersistenceAdapter.search(criteria);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.isFirst()).isTrue();
        assertThat(result.hasNext()).isTrue();
    }

    @Test
    @DisplayName("정렬 테스트 - 금액 기준 오름차순")
    void searchWithSortByAmount() {
        // given
        PaymentSearchCriteria criteria = PaymentSearchCriteria.builder()
                .page(0)
                .size(10)
                .sortField("amount")
                .sortDirection(SortDirection.ASC)
                .build();

        // when
        Page<Payment> result = paymentPersistenceAdapter.search(criteria);

        // then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent().get(0).getAmount().getAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(10000));
        assertThat(result.getContent().get(1).getAmount().getAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(20000));
        assertThat(result.getContent().get(2).getAmount().getAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(30000));
    }

    private Payment createPayment() {
        PaymentProcessingInfo processingInfo = PaymentProcessingInfo.of(
                "tx-new",
                "portone",
                "pg-tx-new",
                "CARD"
        );

        PaymentStatusInfo statusInfo = PaymentStatusInfo.of(
                PaymentStatus.SUCCESS,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "https://receipt.example.com/new"
        );

        return Payment.reconstitute(
                PaymentId.of("new-payment"),
                ExternalIdentifier.of("new-identifier"),
                "New Order",
                Money.won(15000),
                processingInfo,
                statusInfo,
                "{\"pg_response\": \"test_new\"}"
        );
    }
}