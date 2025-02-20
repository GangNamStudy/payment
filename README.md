# payment

주차장 관리 프로그램의 결제 시스템입니다.

## 개요
- Spring Boot 기반의 결제 처리 시스템
- REST API를 통한 결제 처리, 조회, 취소 기능 제공
- 결제 내역 검색 및 필터링 기능

## Features
- 결제 요청 처리
- 결제 정보 조회
- 결제 내역 검색
- 결제 취소 처리

## API Endpoints

### 1. 결제 요청
```http
POST /api/v1/payment
```

<details>
<summary><strong>Request/Response</strong></summary>

#### Request
```json
{
    "identifier": "ORD123456",            // 결제 식별자
    "orderName": "2시간 주차권",          // 주문명
    "amount": 5000,                       // 결제 금액
    "paymentMethod": "CARD",              // 결제 수단
    "paymentInfo": {
        "type": "CARD",                   // 결제 상세 타입
        "cardNumber": "1234-5678-9012-3456",  // 카드번호
        "expiry": "0525",                 // 유효기간 (MMYY)
        "cvc": "123",                     // CVC
        "installmentMonths": 0            // 할부개월 (optional)
    }
}
```

#### Response
```json
{
    "paymentId": 987654,                  // 결제 고유 ID
    "identifier": "ORD123456",            // 결제 식별자
    "paymentDateTime": "2024-02-20T14:30:00",  // 결제시간
    "amount": 5000,                       // 결제금액
    "paymentMethod": "CARD",              // 결제수단
    "status": "SUCCESS",                  // 결제상태
    "orderName": "2시간 주차권",          // 주문명
    "receiptUrl": "https://..."          // 영수증 URL (optional)
}
```
</details>

### 2. 결제 조회
```http
GET /api/v1/payment/{paymentId}
```

<details>
<summary><strong>Response</strong></summary>

```json
{
    "paymentId": 987654,
    "identifier": "ORD123456",
    "paymentDateTime": "2024-02-20T14:30:00",
    "amount": 5000,
    "paymentMethod": "CARD",
    "status": "SUCCESS",
    "orderName": "2시간 주차권",
    "receiptUrl": "https://..."
}
```
</details>

### 3. 결제 목록 조회
```http
GET /api/v1/payment
```

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | Integer | No | 페이지 번호 (default: 0) |
| size | Integer | No | 페이지 크기 (default: 10) |
| search | String | No | 검색어 |
| searchType | String | No | 검색 타입 (PAYMENT_ID/IDENTIFIER/ORDER_NAME) |
| startDate | String | No | 시작 날짜 (ISO 8601) |
| endDate | String | No | 종료 날짜 (ISO 8601) |
| status | String | No | 결제 상태 |
| paymentMethod | String | No | 결제 수단 |
| sortField | String | No | 정렬 필드 |
| sortDirection | String | No | 정렬 방향 (ASC/DESC) |

<details>
<summary><strong>Response</strong></summary>

```json
{
    "payments": [
        {
            "paymentId": 987654,
            "identifier": "ORD123456",
            "amount": 5000,
            "paymentDate": "2024-02-20T14:30:00",
            "status": "SUCCESS",
            "orderName": "2시간 주차권",
            "paymentMethod": "CARD"
        }
    ],
    "pageInfo": {
        "currentPage": 0,
        "totalPages": 5,
        "totalElements": 42,
        "hasNext": true,
        "hasPrevious": false
    }
}
```
</details>

### 4. 결제 취소
```http
PUT /api/v1/payment/{paymentId}/cancel
```

<details>
<summary><strong>Request/Response</strong></summary>

#### Request
```json
{
    "reason": "고객 요청",        // 취소 사유
    "cancelAmount": 5000         // 취소 금액
}
```

#### Response
```json
{
    "paymentId": 987654,
    "cancelDateTime": "2024-02-20T15:30:00",
    "cancelAmount": 5000,
    "status": "CANCELED",
    "originalPaymentInfo": {
        "paymentId": 987654,
        "amount": 5000,
        "paymentDate": "2024-02-20T14:30:00"
    }
}
```
</details>

## Status Codes & Types

### Payment Status
| Status | Description |
|--------|-------------|
| SUCCESS | 결제 성공 |
| FAILED | 결제 실패 |
| PENDING | 결제 진행 중 |
| CANCELED | 결제 취소됨 |

### Payment Methods
| Method | Description |
|--------|-------------|
| CARD | 카드 결제 |
| KAKAO_PAY | 카카오페이 |
| TOSS_PAY | 토스페이 |

### Error Response
```json
{
    "errorCode": "PAYMENT_FAILED",          // 에러 코드
    "errorMessage": "결제 처리 실패",        // 에러 메시지
    "timestamp": "2024-02-20T14:30:00",    // 발생 시간
    "path": "/api/v1/payment"              // 요청 경로
}
```

## Tech Stack
- Spring Boot 3.5.x
- Java 21
- Spring Data JPA
- MySQL 8.0
- Gradle

## Setup
1. Clone repository
```bash
git clone https://github.com/GangNamStudy/payment.git
```

2. Configure database (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/payment
    username: root
    password: password
```

3. Run application
```bash
./gradlew bootRun
```