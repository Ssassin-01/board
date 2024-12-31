# 🚀 **Board 프로젝트**

---

## 📌 **1. 프로젝트 개요**

**Board**는 사용자가 게시글을 작성, 수정, 삭제하고 댓글을 통해 소통할 수 있는 **게시판 웹 애플리케이션**입니다.  
Spring Boot, React, PostgreSQL, JWT 등의 기술을 활용하여 **안전하고 효율적인 플랫폼**을 제공합니다.

---

## 📌 **2. 주요 기능**

### ✅ **1. 사용자 관리**
- 회원가입 / 회원정보 수정 / 회원탈퇴  
- 이메일 인증 및 비밀번호 찾기  
- OAuth2 소셜 로그인 (Google, Naver 등)  

### ✅ **2. 게시판 기능**
- 게시글 작성 / 수정 / 삭제  
- 게시글 검색 및 필터링  
- 게시글 상세 조회  

### ✅ **3. 댓글 기능**
- 댓글 작성 / 수정 / 삭제  

### ✅ **4. 보안**
- JWT(JSON Web Token) 기반 사용자 인증 및 인가  

### ✅ **5. 실시간 채팅**
- 실시간 채팅 기능을 통해 사용자 간 소통 가능  

---

## 📌 **3. 기술 스택**

### 🔹 **Backend**
- **Language:** Java 17  
- **Framework:** Spring Boot  
- **ORM:** JPA (Java Persistence API)  
- **Security:** Spring Security + JWT  
- **Build Tool:** Gradle  

### 🔹 **Frontend**
- **Language:** JavaScript  
- **Library:** React  
- **Package Manager:** Yarn  

### 🔹 **Database**
- **Primary DB:** PostgreSQL  
- **Cache:** Redis *(추후 추가 예정)*  

### 🔹 **Infrastructure**
- **Containerization:** Docker  
- **Web Server:** NGINX  
- **Cloud Hosting:** AWS EC2  

### 🔹 **CI/CD**
- **Version Control:** GitLab  
- **CI/CD Pipeline:** GitLab CI/CD  

### 🔹 **Documentation & Testing**
- **API Documentation:** Postman  
- **Testing:** JUnit5  

---

## 📌 **4. 프로젝트 아키텍처**

```plaintext
+---------------------------------------+
|          GitLab CI/CD Pipeline         |
+-------------------------+-------------+
                          |
+-------------------+          +-------------------------+
|   React (Frontend)|          |  Spring Framework       |
|      (Docker)     | <------> |    (Backend, Docker)     |
+---------+---------+          +-------------+-----------+
          |                                  |
          |                                  |
+---------+---------+          +-------------+-----------+
|  Docker Compose   |          |    PostgreSQL (Database) |
| (서비스 컨테이너화)|          |        (Docker)          |
+---------+---------+          +-------------+-----------+
          |                                  |
          +----------------------------------+
                      AWS (EC2, RDS, S3)

## 📌 **5. 설치 및 실행 방법**

---
