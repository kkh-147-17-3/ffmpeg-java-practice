# backend-test



## 1. 서버 구동 방법
**1. repository를 clone 합니다.**
```shell
~/github$: git clone https://github.com/kkh-147-17-3/backend-test.git
```

<br>

**2. clone한 directory의 /backend-test 에서 spring boot project를 maven 빌드합니다.**
```shell
// linux
~/backend-test$: mvn package
```

```powershell
// windows
PS C:\Users\kkh\github\backend-test> ./mvnw package

```
<br>

**3. clone한 directory의 /backend-test 에서 다음과 같이 docker를 구동합니다.**
```
docker-compose up --build
```

**4. 파일 저장경로 및 url 구조는 application.properties에 다음과 같이 정의되어 있습니다.**

```properties
## 업로드된 파일이 저장되는 위치입니다.
file.video-upload-dir=/usr/local/shoplive/video/

## 리사이징 및 썸네일 생성 대상 video 파일이 위치한 경로입니다. 위와 동일하게 설정되어 있습니다.
video.origin-path=${file.video-upload-dir}

## 리사이징된 video file이 저장되는 경로입니다. 업로드된 video 파일이 저장되는 경로와 동리하게 설정되어 있습니다.
video.convert-save-path=${file.video-upload-dir}

## 생성된 thumbnail이 저장되는 경로입니다.
video.thumbnail-save-path=/usr/local/shoplive/thumbnail/

## thumbnail 생성 시 파일명에 추가되는 접미사입니다.
video.thumbnail-suffix=_thumb

## video 파일의 domain 주소입니다.
video.origin-url=http://localhost:8080

## video 파일의 url 위치입니다.
video.video-url=/path/to/video

## thumbnail 파일의 url 위치입니다.
video.thumbnail-url=/path/to/thumbnail
```


## 2. 테스트 방법

**1. 브라우저에 http://localhost:8080/video 입력하여 페이지에 접속합니다.**

<br>

**2. 다음과 같이 동영상 파일을 선택합니다.**

  ![image](https://user-images.githubusercontent.com/102606939/220869798-e66eb78d-8f9f-40b1-805d-a11c98a45eea.png)

<br>

**3. 동영상이 정상적으로 업로드되면 다음과 같이 결과물과 진행사항을 확인할 수 있습니다.**

  ![image](https://user-images.githubusercontent.com/102606939/220872185-7a5a04c0-3e98-4685-ab8b-c4f626291181.png)

  + 업로드에 성공하면 서버에서는 thumbnail 생성 작업을 실행 후 resizing 작업을 비동기로 실행한 뒤 성공 response를 응답합니다. 
    (thumnail 생성 resizing 비동기작업 실행 전 은 동기작업으로 진행합니다.)
  + client에서는 성공 응답을 받으면 매 1초 마다 progress 상태를 확인할 수 있는 request를 지속적으로 전송합니다.
  + 먼저 thumbnail 에 대한 progress를 확인 후 thumbnail 작업이 완료된 것을 client에서 확인하면 resizing progress를 확인하는 요청을 매 1초마다 전송합니다.
  + thumbnail progress 및 resizing progress가 100%가 되면 videoId를 이용하여 video 정보를 요청하고, 이를 통해 브라우저에서는 thumbnail과 리사이징된 영상을 불러와 load합니다.
  
  <br>
  
 **4. DB에 정상적으로 반영된 video 정보는 video 테이블의 id를 이용하여 아래와 같이 세부 정보를 조회할 수 있습니다.**
 
  ![image](https://user-images.githubusercontent.com/102606939/220873541-41ea43b5-8a16-49e3-bb86-bbe778cc54f9.png)

<br>

**5. 확장자가 .mp4가 아니거나, 또는 100MB를 초과하는 파일을 업로드하는 경우 변환 및 등록이 진행되지 않고, 아래와 같이 예외가 발생합니다.**
  ![image](https://user-images.githubusercontent.com/102606939/220874349-acb2c553-3cc6-4af7-8d83-36ce9ee19896.png)

  ![image](https://user-images.githubusercontent.com/102606939/220874651-7a7af916-da2b-490e-b248-0c661fdb552c.png)

<br>

## 3. 단위 테스트 방법 (linux 환경)

**1. linux 환경에서repository를 clone 합니다.

**2. clone한 directory의 /backend-test/의 src/main/resources 에 위치한 application.properties의 db 속성값을 test db가 위치한 곳으로 변경합니다.**

   - 아래의 application.properties 내용 참고
   ```
   spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
   spring.datasource.url=jdbc:mariadb://localhost:3306/shoplivedb
   spring.datasource.username=root
   spring.datasource.password=1234
   ```
   
   docker의 mariadb container가 있다면 다음과 같이 mariadb를 구동합니다.
   ```
   docker run --name mariadb -d -p 3306:3306 --restart=always -e MYSQL_ROOT_PASSWORD=1234 -e MARIADB_DATABASE=shoplivedb mariadb
   ```

**3. pom.xml에서 아래 <plugin>의 <skipTests> 부분을 주석처리합니다.

```xml
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-surefire-plugin</artifactId>
				<version>2.22.0</version>
				<configuration>
        			<useSystemClassLoader>false</useSystemClassLoader>
        			<!-- <skipTests>true</skipTests> -->
				</configuration>
			</plugin>
```


**4. clone한 directory의 /backend-test/에서 다음을 입력합니다. 이때 build 과정에서 junit 테스트가 시작됩니다.**


<br>

  ```
  mvn clean pakckage
  ```
  * test과정에서 오류가 발생한 경우 test를 root 권한으로 실행합니다.

<br>


**4. 다음의 경로에서 jacoco 의 테스트 커버리지 report를 확인합니다.**
  ```
  target/site/jacoco/index.html
  ```
