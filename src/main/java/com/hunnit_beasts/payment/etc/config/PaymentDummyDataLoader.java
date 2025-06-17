package com.hunnit_beasts.payment.etc.config;

import com.hunnit_beasts.payment.adapter.out.persistence.entity.PaymentEntity;
import com.hunnit_beasts.payment.adapter.out.persistence.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentDummyDataLoader implements CommandLineRunner {

    private final PaymentJpaRepository paymentRepository;
    private final Random random = new Random();

    private final String[] orderNames = {
            "주차요금 결제", "정액권 결제", "월 정기권 결제", "야간 주차요금",
            "주말 주차비", "시간제 주차권", "연 정기권 결제", "일일 주차패스"
    };

    private final String[] paymentMethods = {"CARD", "KAKAO_PAY", "TOSS_PAY"};

    @Override
    public void run(String... args) throws Exception {
        log.info("결제 더미 데이터 생성을 시작합니다...");

        paymentRepository.deleteAll();
        createDummyPayments();

        log.info("결제 더미 데이터 {}개 생성이 완료되었습니다.", paymentRepository.count());
    }

    private void createDummyPayments() {
        List<PaymentEntity> dummyPayments = new ArrayList<>();

        for (int i = 1; i <= 15; i++) {
            long timestamp = System.currentTimeMillis() - (i * 100000L);
            LocalDateTime createdTime = LocalDateTime.now().minusHours(i);

            PaymentEntity payment = PaymentEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .identifier("order_" + timestamp)
                    .orderName(orderNames[random.nextInt(orderNames.length)])
                    .amount(new BigDecimal(String.valueOf(1000 + (random.nextInt(50) * 1000))))
                    .currency("KRW")
                    .transactionId(UUID.randomUUID().toString())
                    .pgProvider("INICIS_V2")
                    .pgTxId("StdpayCARDINIpayTest" + timestamp)
                    .paymentMethod(paymentMethods[random.nextInt(paymentMethods.length)])
                    .status("SUCCESS")
                    .createdAt(createdTime)
                    .completedAt(createdTime.plusMinutes(1))
                    .receiptUrl("https://iniweb.inicis.com/DefaultWebApp/mall/cr/cm/mCmReceipt_head.jsp?noTid=StdpayCARDINIpayTest" + timestamp + "&noMethod=1")
                    .pgResponse("{\"resultCode\":\"0000\",\"resultMsg\":\"정상처리되었습니다.\",\"TotPrice\":\"" + (1000 + (random.nextInt(50) * 1000)) + "\",\"payMethod\":\"Card\"}")
                    .updatedAt(createdTime)
                    .build();

            dummyPayments.add(payment);
        }

        paymentRepository.saveAll(dummyPayments);
    }
}
