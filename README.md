
# Parkmate Review Service

## 📌 서비스 소개

**Parkmate Review Service**는 주차장 이용자들이 직접 리뷰(평점, 후기, 사진, 동영상 등)를 남기고, 다른 사람의 리뷰를 확인할 수 있도록 도와주는 백엔드 서버입니다.  
이 서비스는 마이크로서비스 아키텍처 환경에서 독립적으로 동작하며, 주차장 예약/결제 시스템 등과 연동하여 실제 사용자 경험을 풍부하게 만듭니다.

---

## 🏗️ 전체 구조 한눈에 보기

```
reviewservice/
├── src/
│   └── main/
│       └── java/com/parkmate/reviewservice/
│           ├── common/              # 공통 예외, 응답, 설정 등
│           ├── facade/              # 여러 도메인/외부 연동을 묶는 파사드 계층
│           ├── kafka/               # Kafka 이벤트 발행/수신
│           ├── review/              # 리뷰 도메인(실제 비즈니스 로직)
│           ├── reviewimagemapping/  # 리뷰 이미지/동영상 관리
│           ├── reviewreactions/     # 리뷰 반응(좋아요/싫어요) 관리
│           └── ReviewserviceApplication.java # 메인 실행 클래스
├── build.gradle                     # 빌드 및 의존성 관리 파일
└── README.md                        # 이 문서
```

---

## 🌟 이 서비스가 제공하는 기능

### 1. 리뷰 등록
- 주차장 이용 후, 사용자가 직접 리뷰(평점, 후기, 사진, 동영상)를 남길 수 있습니다.
- **이미지 최대 5장, 동영상 최대 1개**까지 첨부 가능.
- 결제 건/유저/주차장 중복 체크로, 한 결제/한 유저/한 주차장에 중복 리뷰 방지.

### 2. 리뷰 조회
- 리뷰의 고유 ID(UUID)로 상세 정보를 조회할 수 있습니다.
- 첨부된 이미지/동영상 URL도 함께 반환.

### 3. 리뷰 수정
- 본인이 작성한 리뷰만 수정할 수 있습니다.
- 내용, 평점, 첨부 이미지/동영상 모두 변경 가능.

### 4. 리뷰 삭제 (Soft Delete)
- 리뷰를 실제로 DB에서 삭제하지 않고, 상태값만 변경하여 "삭제됨"으로 표시합니다.
- 추후 복구나 로그 추적이 필요할 때 유용.

### 5. 예약 코드로 리뷰 존재 여부 확인
- 예약 코드로 해당 결제에 리뷰가 이미 등록되어 있는지 확인할 수 있습니다.

### 6. 리뷰 반응(좋아요/싫어요)
- 다른 사용자가 남긴 리뷰에 대해 "좋아요" 또는 "싫어요" 반응을 남길 수 있습니다.
- 본인이 남긴 반응을 조회할 수도 있습니다.

---

## 🛠️ 사용 기술 및 환경

- **Java 17**
- **Spring Boot 3.3.1** (최신 스프링 프레임워크)
- **Spring Data JPA** (DB 연동)
- **MySQL** (관계형 데이터베이스)
- **Kafka** (이벤트 발행/구독)
- **Eureka** (서비스 레지스트리, 마이크로서비스 환경)
- **Swagger (springdoc-openapi)** (API 문서 자동화)
- **Lombok** (코드 간결화)
- **Gradle** (빌드 도구)

---

## 🚀 빠른 시작 가이드

### 1. 필수 환경 준비

- Java 17 이상 설치
- MySQL, Kafka, Eureka 서버 실행(로컬 또는 외부)
- (선택) Docker 환경 사용 가능

### 2. 데이터베이스 및 환경 변수 설정

- `src/main/resources/application.yml` 또는 환경변수에 아래 정보 입력
  - MySQL 접속 정보 (url, username, password)
  - Kafka, Eureka 서버 주소

### 3. 의존성 설치 및 빌드

```bash
./gradlew build
```

### 4. 서버 실행

```bash
./gradlew bootRun
```

### 5. API 문서(Swagger) 확인

- 서버 실행 후 웹 브라우저에서  
  [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
  접속하면 모든 API를 예시와 함께 쉽게 테스트할 수 있습니다.

---

## 📚 주요 API 예시

### 1. 리뷰 등록

```http
POST /api/v1/reviews
Content-Type: application/json
X-User-UUID: 사용자-UUID

{
  "parkingLotUuid": "주차장-UUID",
  "rating": 5,
  "content": "주차장이 넓고 깨끗해요!",
  "imageMappings": [
    {"imageUrl": "https://.../img1.jpg", "type": "IMAGE"},
    {"imageUrl": "https://.../video1.mp4", "type": "VIDEO"}
  ]
}
```

### 2. 리뷰 상세 조회

```http
GET /api/v1/reviews/{리뷰-UUID}
```

### 3. 리뷰 수정

```http
PUT /api/v1/reviews/{리뷰-UUID}
Content-Type: application/json
X-User-UUID: 사용자-UUID

{
  "rating": 4,
  "content": "조금 좁지만 친절해요.",
  "imageMappings": []
}
```

### 4. 리뷰 삭제(Soft Delete)

```http
PUT /api/v1/reviews/{리뷰-UUID}/softdelete
X-User-UUID: 사용자-UUID
```

### 5. 예약 코드로 리뷰 존재 확인

```http
GET /api/v1/reviews?reservationCode={예약코드}
```

### 6. 리뷰 반응 추가/조회

```http
POST /api/v1/reviews/{리뷰-UUID}/reactions
X-User-UUID: 사용자-UUID
{
  "reactionType": "LIKE"
}

GET /api/v1/reviews/{리뷰-UUID}/reactions
X-User-UUID: 사용자-UUID
```

---

## 🧩 내부 동작 흐름 예시

1. **리뷰 등록 요청 → Facade → Service → DB 저장 → Kafka 이벤트 발행 → 이미지/동영상 저장**
2. **리뷰 조회 요청 → Facade → Service → DB 조회 → 이미지/동영상 URL 함께 반환**
3. **리뷰 수정/삭제도 비슷하게 Facade → Service → DB 처리 → Kafka 이벤트 발행**

---

## 📝 자주 묻는 질문(FAQ)

- **Q. 리뷰 삭제하면 진짜로 DB에서 없어지나요?**  
  A. 아닙니다. 상태값만 "삭제됨"으로 바뀌고, 실제 데이터는 남아있어 추후 복구나 감사가 가능합니다.

- **Q. 이미지/동영상 첨부 제한이 있나요?**  
  A. 네, 이미지 최대 5장, 동영상 최대 1개까지 첨부할 수 있습니다.

- **Q. Swagger가 뭔가요?**  
  A. API 명세와 테스트를 웹에서 쉽게 할 수 있게 해주는 도구입니다.  
  (http://localhost:8080/swagger-ui.html 접속)

- **Q. Kafka, Eureka는 왜 쓰나요?**  
  A. Kafka는 리뷰 등록/수정/삭제 이벤트를 다른 서비스와 실시간으로 연동하기 위해,  
  Eureka는 여러 마이크로서비스가 서로를 쉽게 찾고 통신할 수 있게 해줍니다.

---

## 🛠️ 개발/운영 팁

- **코드 수정 후에는 반드시 빌드(`./gradlew build`) 후 실행**
- **DB, Kafka, Eureka 등 외부 서비스가 정상 동작 중인지 확인**
- **Swagger로 API 테스트 후 실제 프론트엔드/앱 연동**
- **에러 발생 시 로그와 ResponseStatus 코드 참고**

---

## 👨‍💻 기여 및 문의

- Pull Request, Issue 등록 환영
- 문의: 담당자 또는 팀 이메일

---

이 문서는 처음 보는 분도 쉽게 이해할 수 있도록 최대한 상세하게 작성되었습니다.  
실제 API 요청/응답 예시, 내부 동작 흐름, 용어 설명까지 모두 포함되어 있습니다.  
더 궁금한 점이 있으면 언제든 문의해 주세요!

=======
# parkmate-review-service

## 📌 서비스 소개

**Parkmate Review Service**는 주차장 이용자들이 직접 리뷰(평점, 후기, 사진, 동영상 등)를 남기고, 다른 사람의 리뷰를 확인할 수 있도록 도와주는 백엔드 서버입니다.  
이 서비스는 마이크로서비스 아키텍처 환경에서 독립적으로 동작하며, 주차장 예약/결제 시스템 등과 연동하여 실제 사용자 경험을 풍부하게 만듭니다.

---

## 🏗️ 전체 구조 한눈에 보기

```
reviewservice/
├── src/
│   └── main/
│       └── java/com/parkmate/reviewservice/
│           ├── common/              # 공통 예외, 응답, 설정 등
│           ├── facade/              # 여러 도메인/외부 연동을 묶는 파사드 계층
│           ├── kafka/               # Kafka 이벤트 발행/수신
│           ├── review/              # 리뷰 도메인(실제 비즈니스 로직)
│           ├── reviewimagemapping/  # 리뷰 이미지/동영상 관리
│           ├── reviewreactions/     # 리뷰 반응(좋아요/싫어요) 관리
│           └── ReviewserviceApplication.java # 메인 실행 클래스
├── build.gradle                     # 빌드 및 의존성 관리 파일
└── README.md                        # 이 문서
```

---

## 🌟 이 서비스가 제공하는 기능

### 1. 리뷰 등록
- 주차장 이용 후, 사용자가 직접 리뷰(평점, 후기, 사진, 동영상)를 남길 수 있습니다.
- **이미지 최대 5장, 동영상 최대 1개**까지 첨부 가능.
- 결제 건/유저/주차장 중복 체크로, 한 결제/한 유저/한 주차장에 중복 리뷰 방지.

### 2. 리뷰 조회
- 리뷰의 고유 ID(UUID)로 상세 정보를 조회할 수 있습니다.
- 첨부된 이미지/동영상 URL도 함께 반환.

### 3. 리뷰 수정
- 본인이 작성한 리뷰만 수정할 수 있습니다.
- 내용, 평점, 첨부 이미지/동영상 모두 변경 가능.

### 4. 리뷰 삭제 (Soft Delete)
- 리뷰를 실제로 DB에서 삭제하지 않고, 상태값만 변경하여 "삭제됨"으로 표시합니다.
- 추후 복구나 로그 추적이 필요할 때 유용.

### 5. 예약 코드로 리뷰 존재 여부 확인
- 예약 코드로 해당 예약에 리뷰가 이미 등록되어 있는지 확인할 수 있습니다.

### 6. 리뷰 반응(좋아요/싫어요)
- 다른 사용자가 남긴 리뷰에 대해 "좋아요" 또는 "싫어요" 반응을 남길 수 있습니다.
- 본인이 남긴 반응을 조회할 수도 있습니다.

---

## 🛠️ 사용 기술 및 환경

- **Java 17**
- **Spring Boot 3.3.1** (최신 스프링 프레임워크)
- **Spring Data JPA** (DB 연동)
- **MySQL** (관계형 데이터베이스)
- **Kafka** (이벤트 발행/구독)
- **Eureka** (서비스 레지스트리, 마이크로서비스 환경)
- **Swagger (springdoc-openapi)** (API 문서 자동화)
- **Lombok** (코드 간결화)
- **Gradle** (빌드 도구)

---

## 🚀 빠른 시작 가이드

### 1. 필수 환경 준비

- Java 17 이상 설치
- MySQL, Kafka, Eureka 서버 실행(로컬 또는 외부)
- (선택) Docker 환경 사용 가능

### 2. 데이터베이스 및 환경 변수 설정

- `src/main/resources/application.yml` 또는 환경변수에 아래 정보 입력
  - MySQL 접속 정보 (url, username, password)
  - Kafka, Eureka 서버 주소

### 3. 의존성 설치 및 빌드

```bash
./gradlew build
```

### 4. 서버 실행

```bash
./gradlew bootRun
```

### 5. API 문서(Swagger) 확인

- 서버 실행 후 웹 브라우저에서  
  [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
  접속하면 모든 API를 예시와 함께 쉽게 테스트할 수 있습니다.

---

## 📚 주요 API 예시

### 1. 리뷰 등록

```http
POST /api/v1/reviews
Content-Type: application/json
X-User-UUID: 사용자-UUID

{
  "parkingLotUuid": "주차장-UUID",
  "rating": 5,
  "content": "주차장이 넓고 깨끗해요!",
  "imageMappings": [
    {"imageUrl": "https://.../img1.jpg", "type": "IMAGE"},
    {"imageUrl": "https://.../video1.mp4", "type": "VIDEO"}
  ]
}
```

### 2. 리뷰 상세 조회

```http
GET /api/v1/reviews/{리뷰-UUID}
```

### 3. 리뷰 수정

```http
PUT /api/v1/reviews/{리뷰-UUID}
Content-Type: application/json
X-User-UUID: 사용자-UUID

{
  "rating": 4,
  "content": "조금 좁지만 친절해요.",
  "imageMappings": []
}
```

### 4. 리뷰 삭제(Soft Delete)

```http
PUT /api/v1/reviews/{리뷰-UUID}/softdelete
X-User-UUID: 사용자-UUID
```

### 5. 예약 코드로 리뷰 존재 확인

```http
GET /api/v1/reviews?reservationCode={예약코드}
```

### 6. 리뷰 반응 추가/조회

```http
POST /api/v1/reviews/{리뷰-UUID}/reactions
X-User-UUID: 사용자-UUID
{
  "reactionType": "LIKE"
}

GET /api/v1/reviews/{리뷰-UUID}/reactions
X-User-UUID: 사용자-UUID
```

---

## 🧩 내부 동작 흐름 예시

1. **리뷰 등록 요청 → Facade → Service → DB 저장 → Kafka 이벤트 발행 → 이미지/동영상 저장**
2. **리뷰 조회 요청 → Facade → Service → DB 조회 → 이미지/동영상 URL 함께 반환**
3. **리뷰 수정/삭제도 비슷하게 Facade → Service → DB 처리 → Kafka 이벤트 발행**

---

## 📝 자주 묻는 질문(FAQ)

- **Q. 리뷰 삭제하면 진짜로 DB에서 없어지나요?**  
  A. 아닙니다. 상태값만 "삭제됨"으로 바뀌고, 실제 데이터는 남아있어 추후 복구나 감사가 가능합니다.

- **Q. 이미지/동영상 첨부 제한이 있나요?**  
  A. 네, 이미지 최대 5장, 동영상 최대 1개까지 첨부할 수 있습니다.

- **Q. Swagger가 뭔가요?**  
  A. API 명세와 테스트를 웹에서 쉽게 할 수 있게 해주는 도구입니다.  
  (http://localhost:8080/swagger-ui.html 접속)

- **Q. Kafka, Eureka는 왜 쓰나요?**  
  A. Kafka는 리뷰 등록/수정/삭제 이벤트를 다른 서비스와 실시간으로 연동하기 위해,  
  Eureka는 여러 마이크로서비스가 서로를 쉽게 찾고 통신할 수 있게 해줍니다.

---

## 🛠️ 개발/운영 팁

- **코드 수정 후에는 반드시 빌드(`./gradlew build`) 후 실행**
- **DB, Kafka, Eureka 등 외부 서비스가 정상 동작 중인지 확인**
- **Swagger로 API 테스트 후 실제 프론트엔드/앱 연동**
- **에러 발생 시 로그와 ResponseStatus 코드 참고**

---

## 👨‍💻 기여 및 문의

- Pull Request, Issue 등록 환영
- 문의: 담당자 또는 팀 이메일

---

이 문서는 처음 보는 분도 쉽게 이해할 수 있도록 최대한 상세하게 작성되었습니다.  
실제 API 요청/응답 예시, 내부 동작 흐름, 용어 설명까지 모두 포함되어 있습니다.  
더 궁금한 점이 있으면 언제든 문의해 주세요!

