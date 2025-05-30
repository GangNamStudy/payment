package com.hunnit_beasts.payment.adaptertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunnit_beasts.payment.adapter.out.persistence.entity.PaymentEntity;
import com.hunnit_beasts.payment.adapter.out.persistence.repository.PaymentJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@Transactional
@DisplayName("PaymentQueryAdapter 통합 테스트")
class PaymentQueryAdapterTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private PaymentEntity testPayment1;
    private PaymentEntity testPayment2;
    private PaymentEntity testPayment3;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        paymentJpaRepository.deleteAll();
        LocalDateTime now = LocalDateTime.now();

        testPayment1 = PaymentEntity.builder()
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

        testPayment2 = PaymentEntity.builder()
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

        testPayment3 = PaymentEntity.builder()
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

        paymentJpaRepository.saveAll(List.of(testPayment1, testPayment2, testPayment3));
    }

    @Test
    @DisplayName("결제 단건 조회 - 성공")
    void getPayment_Success() throws Exception {
        mockMvc.perform(get("/api/v1/payment/{paymentId}", "payment-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paymentId").value("payment-1"))
                .andExpect(jsonPath("$.identifier").value("identifier-1"))
                .andExpect(jsonPath("$.orderName").value("Test Order 1"))
                .andExpect(jsonPath("$.amount").value(10000))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.paymentMethod").value("CARD"))
                .andExpect(jsonPath("$.paymentDate").exists());
    }

    @Test
    @DisplayName("결제 단건 조회 - 존재하지 않는 결제")
    void getPayment_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/payment/{paymentId}", "nonexistent-payment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("결제 목록 조회 - 전체 조회")
    void getPayments_All() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.payments").isArray())
                .andExpect(jsonPath("$.payments", hasSize(3)))
                .andExpect(jsonPath("$.pageInfo.totalElements").value(3))
                .andExpect(jsonPath("$.pageInfo.totalPages").value(1))
                .andExpect(jsonPath("$.pageInfo.currentPage").value(0))
                .andExpect(jsonPath("$.pageInfo.hasNext").value(false))
                .andExpect(jsonPath("$.pageInfo.hasPrevious").value(false));
    }

    @Test
    @DisplayName("결제 목록 조회 - 검색어로 필터링")
    void getPayments_WithSearch() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                        .param("search", "Test")
                        .param("searchType", "ORDER_NAME")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payments", hasSize(2)))
                .andExpect(jsonPath("$.payments[*].orderName", everyItem(containsString("Test"))))
                .andExpect(jsonPath("$.pageInfo.totalElements").value(2));
    }

    @Test
    @DisplayName("결제 목록 조회 - 상태로 필터링")
    void getPayments_WithStatus() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                        .param("status", "SUCCESS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payments", hasSize(1)))
                .andExpect(jsonPath("$.payments[0].status").value("SUCCESS"))
                .andExpect(jsonPath("$.payments[0].paymentId").value("payment-1"));
    }

    @Test
    @DisplayName("결제 목록 조회 - 결제 수단으로 필터링")
    void getPayments_WithPaymentMethod() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                        .param("paymentMethod", "CARD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payments", hasSize(1)))
                .andExpect(jsonPath("$.payments[0].paymentMethod").value("CARD"))
                .andExpect(jsonPath("$.payments[0].paymentId").value("payment-1"));
    }

    @Test
    @DisplayName("결제 목록 조회 - 날짜 범위로 필터링")
    void getPayments_WithDateRange() throws Exception {
        LocalDateTime startDate = LocalDateTime.now().minusHours(3);
        LocalDateTime endDate = LocalDateTime.now().minusMinutes(30);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        mockMvc.perform(get("/api/v1/payment")
                        .param("startDate", startDate.format(formatter))
                        .param("endDate", endDate.format(formatter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payments", hasSize(2))) // payment-1, payment-2
                .andExpect(jsonPath("$.pageInfo.totalElements").value(2));
    }

    @Test
    @DisplayName("결제 목록 조회 - 복합 조건 검색")
    void getPayments_WithMultipleConditions() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                        .param("search", "Test")
                        .param("searchType", "ORDER_NAME")
                        .param("status", "SUCCESS")
                        .param("paymentMethod", "CARD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payments", hasSize(1)))
                .andExpect(jsonPath("$.payments[0].paymentId").value("payment-1"))
                .andExpect(jsonPath("$.payments[0].orderName").value("Test Order 1"))
                .andExpect(jsonPath("$.payments[0].status").value("SUCCESS"))
                .andExpect(jsonPath("$.payments[0].paymentMethod").value("CARD"));
    }

    @Test
    @DisplayName("결제 목록 조회 - 페이징")
    void getPayments_WithPaging() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sortField", "createdAt")
                        .param("sortDirection", "DESC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payments", hasSize(2)))
                .andExpect(jsonPath("$.pageInfo.totalElements").value(3))
                .andExpect(jsonPath("$.pageInfo.totalPages").value(2))
                .andExpect(jsonPath("$.pageInfo.currentPage").value(0))
                .andExpect(jsonPath("$.pageInfo.hasNext").value(true))
                .andExpect(jsonPath("$.pageInfo.hasPrevious").value(false));
    }

    @Test
    @DisplayName("결제 목록 조회 - 정렬 테스트 (금액 오름차순)")
    void getPayments_WithSorting() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                        .param("sortField", "amount")
                        .param("sortDirection", "ASC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payments", hasSize(3)))
                .andExpect(jsonPath("$.payments[0].amount").value(10000))
                .andExpect(jsonPath("$.payments[1].amount").value(20000))
                .andExpect(jsonPath("$.payments[2].amount").value(30000));
    }

    @Test
    @DisplayName("결제 목록 조회 - 검색 결과 없음")
    void getPayments_NoResults() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                        .param("search", "NonExistent")
                        .param("searchType", "ORDER_NAME")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payments", hasSize(0)))
                .andExpect(jsonPath("$.pageInfo.totalElements").value(0))
                .andExpect(jsonPath("$.pageInfo.totalPages").value(0))
                .andExpect(jsonPath("$.pageInfo.hasNext").value(false))
                .andExpect(jsonPath("$.pageInfo.hasPrevious").value(false));
    }

    @Test
    @DisplayName("결제 목록 조회 - 잘못된 정렬 필드")
    void getPayments_WithInvalidSortField() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                        .param("sortField", "invalidField")
                        .param("sortDirection", "ASC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()) // 기본 정렬로 처리되어야 함
                .andExpect(jsonPath("$.payments", hasSize(3)))
                .andExpect(jsonPath("$.pageInfo.totalElements").value(3));
    }

    @Test
    @DisplayName("결제 목록 조회 - 빈 파라미터들")
    void getPayments_WithEmptyParams() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                        .param("search", "")
                        .param("searchType", "")
                        .param("status", "")
                        .param("paymentMethod", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payments", hasSize(3))) // 모든 결제 조회 (빈 문자열은 null로 처리)
                .andExpect(jsonPath("$.pageInfo.totalElements").value(3));
    }

    @Test
    @DisplayName("결제 목록 조회 - 페이지 사이즈 테스트")
    void getPayments_WithCustomPageSize() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                        .param("page", "1")
                        .param("size", "1")
                        .param("sortField", "createdAt")
                        .param("sortDirection", "ASC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payments", hasSize(1)))
                .andExpect(jsonPath("$.pageInfo.totalElements").value(3))
                .andExpect(jsonPath("$.pageInfo.totalPages").value(3))
                .andExpect(jsonPath("$.pageInfo.currentPage").value(1))
                .andExpect(jsonPath("$.pageInfo.hasNext").value(true))
                .andExpect(jsonPath("$.pageInfo.hasPrevious").value(true));
    }

    @Test
    @DisplayName("결제 목록 조회 - 식별자로 검색")
    void getPayments_SearchByIdentifier() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                        .param("search", "identifier-2")
                        .param("searchType", "IDENTIFIER")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payments", hasSize(1)))
                .andExpect(jsonPath("$.payments[0].identifier").value("identifier-2"))
                .andExpect(jsonPath("$.payments[0].paymentId").value("payment-2"));
    }

    @Test
    @DisplayName("결제 목록 조회 - Payment ID로 검색")
    void getPayments_SearchByPaymentId() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                        .param("search", "payment-3")
                        .param("searchType", "PAYMENT_ID")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payments", hasSize(1)))
                .andExpect(jsonPath("$.payments[0].paymentId").value("payment-3"))
                .andExpect(jsonPath("$.payments[0].status").value("FAILED"));
    }
}