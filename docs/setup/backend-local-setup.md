# Spring Boot + MySQL 로컬 개발 환경 설정

이 문서는 StockSim 백엔드 프로젝트를 로컬에서 실행하기 위한 설정 가이드입니다.

## 1. Java 21 확인

프로젝트는 Java 21을 사용합니다.

이미 Java 21이 설치되어 있다면 아래 명령어로 버전을 확인합니다.

```bash
java -version
```

출력에 `21`이 포함되어 있으면 됩니다.

예시:

```text
openjdk version "21.x.x"
```

## 2. MySQL 9 설치 및 실행

MySQL 9가 설치되어 있어야 합니다.

macOS에서 Homebrew를 사용한다면 아래처럼 설치할 수 있습니다.

```bash
brew install mysql
```

설치 후 MySQL 서버를 실행합니다.

```bash
brew services start mysql
```

MySQL이 실행 중인지 확인합니다.

```bash
mysql --version
```

MySQL 접속이 되는지도 확인합니다.

```bash
mysql -u root -p
```

비밀번호가 없다면 Enter를 눌러 접속합니다.

접속에 성공하면 아래처럼 MySQL 프롬프트가 보입니다.

```text
mysql>
```

종료하려면 아래 명령어를 입력합니다.

```sql
exit;
```

## 3. 데이터베이스 생성

MySQL에 접속합니다.

```bash
mysql -u root -p
```

프로젝트에서 사용할 데이터베이스를 생성합니다.

```sql
CREATE DATABASE stocksim;
```

생성 여부를 확인합니다.

```sql
SHOW DATABASES;
```

목록에 `stocksim`이 보이면 성공입니다.

```sql
exit;
```

## 4. application.yaml 작성

백엔드 설정 파일 위치는 아래와 같습니다.

```text
backend/src/main/resources/application.yaml
```

아래 내용을 작성합니다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/stocksim?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: root
    password:
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

MySQL root 계정에 비밀번호가 있다면 `password:` 뒤에 비밀번호를 입력합니다.

예시:

```yaml
password: mypassword
```

비밀번호가 없다면 비워둡니다.

```yaml
password:
```

## 5. SQL 실행

프로젝트에는 MySQL 8 이상 기준 SQL 파일이 `backend/sql` 폴더에 분리되어 있습니다.

```text
backend/sql/
├── 001_users.sql
├── 002_stocks.sql
├── 003_stock_price_histories.sql
├── 004_portfolios.sql
├── 005_trades.sql
└── 006_dummy_data.sql
```

프로젝트 루트에서 아래 명령어를 순서대로 실행합니다.

```bash
mysql -u root -p stocksim < backend/sql/001_users.sql
mysql -u root -p stocksim < backend/sql/002_stocks.sql
mysql -u root -p stocksim < backend/sql/003_stock_price_histories.sql
mysql -u root -p stocksim < backend/sql/004_portfolios.sql
mysql -u root -p stocksim < backend/sql/005_trades.sql
mysql -u root -p stocksim < backend/sql/006_dummy_data.sql
```

비밀번호가 없다면 명령어 실행 후 Enter를 누르면 됩니다.

테이블 생성 여부는 아래처럼 확인할 수 있습니다.

```bash
mysql -u root -p stocksim
```

```sql
SHOW TABLES;
```

아래 테이블이 보이면 정상입니다.

```text
portfolios
stock_price_histories
stocks
trades
users
```

더미 주식 데이터가 들어갔는지도 확인합니다.

```sql
SELECT code, name, current_price FROM stocks;
```

확인이 끝나면 MySQL을 종료합니다.

```sql
exit;
```

## 6. Spring Boot 실행

백엔드 폴더로 이동합니다.

```bash
cd backend
```

Gradle로 Spring Boot 서버를 실행합니다.

```bash
./gradlew bootRun
```

실행 중 아래와 비슷한 로그가 보이면 서버가 정상 실행된 것입니다.

```text
Tomcat started on port 8080
Started StocksimApplication
```

서버를 종료하려면 터미널에서 `Ctrl + C`를 누릅니다.

## 7. health 확인

서버가 켜진 상태에서 새 터미널을 열고 아래 명령어를 실행합니다.

```bash
curl http://localhost:8080/health
```

정상이라면 아래처럼 응답합니다.

```text
OK
```

브라우저에서 직접 확인해도 됩니다.

```text
http://localhost:8080/health
```

## 자주 발생하는 문제

### MySQL 접속이 안 되는 경우

MySQL 서버가 실행 중인지 확인합니다.

```bash
brew services list
```

실행 중이 아니라면 다시 시작합니다.

```bash
brew services start mysql
```

### Access denied가 나오는 경우

`application.yaml`의 `username`, `password`가 MySQL 계정과 일치하는지 확인합니다.

터미널에서 아래 명령어로 같은 계정 접속이 되는지 먼저 확인합니다.

```bash
mysql -u root -p
```

### 8080 포트가 이미 사용 중인 경우

이미 실행 중인 서버가 있을 수 있습니다.

기존 서버 터미널에서 `Ctrl + C`로 종료한 뒤 다시 실행합니다.

```bash
./gradlew bootRun
```
