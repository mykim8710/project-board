# 게시판 서비스 개발

## 개요
Springboot + JPA를 사용하여 개발한 가장 기본적이고 보편적인 게시판 기능을 둘러볼 수 있는 서비스입니다.
* 기능목록
  * 게시글
    * 게시글 목록(+페이징, 정렬, 검색, n개씩 보기)
    * 게시글 작성
    * 게시글 수정(본인이 작성한 글만)
    * 게시글 삭제(본인이 작성한 글만)
    * 해시태그 전용 검색
  * 댓글
    * 대댓글(무한단계까지 가능하나 실서비스는 1단계까지만 적용) 
    * 댓글 목록(페이징 X)
    * 댓글 작성
    * 댓글 수정(본인이 작성한 글만)
    * 댓글 삭제(본인이 작성한 글만)
  * 회원가입 / 로그인(직접가입, OAuth2 Social : Google, Naver, Kakao)

## 개발 환경

* Intellij IDEA Ultimate
* Java 17
* Gradle 7.6.1
* Spring Boot 2.7.10

## 기술 세부 스택

Spring Boot
* Spring Web
* Spring Data JPA
* QueryDSL 5.0.0
* Spring Security
* Spring Security OAuth2 Client
* Spring Boot DevTools
* Thymeleaf
* H2 Database
* MySQL Driver
* Lombok
* Validation
* Swagger UI
* aws parameter Store

그 외
* Bootstrap 5.2.3
* JavaScript

## Package Design
```
  └── src
    ├── main
    │   ├── java
    │   │     └── io.mykim.projectboard
    │   │            ├── ProjectBoardApplication(C)
    │   │            ├── article
    │   │            │    ├── api
    │   │            │    │    │── ArticleApiController(C)
    │   │            │    │    └── ArticleCommentApiController(C)
    │   │            │    ├── controller
    │   │            │    │    └── ArticleViewController(C)    
    │   │            │    ├── dto
    │   │            │    │    │── request
    │   │            │    │    │      │── ArticleCommentCreateDto(C)
    │   │            │    │    │      │── ArticleCommentEditDto(C)
    │   │            │    │    │      │── ArticleCreateDto(C)
    │   │            │    │    │      │── ArticleEditDto(C)
    │   │            │    │    │      └── ArticleSearchCondition(C)
    │   │            │    │    └── response
    │   │            │    │           │── ResponseArticleCommentFindDto(C)
    │   │            │    │           │── ResponseArticleCommentListDto(C)
    │   │            │    │           │── ResponseArticleFindDto(C)
    │   │            │    │           └── ResponseArticleListDto(C)
    │   │            │    ├── entity
    │   │            │    │    │── Article(C)
    │   │            │    │    └── ArticleComment(C)
    │   │            │    ├── repository
    │   │            │    │    │── ArticleCommentRepository(I)
    │   │            │    │    │── ArticleCommentQuerydslRepository(I)
    │   │            │    │    │── ArticleCommentQuerydslRepositoryImpl(C)
    │   │            │    │    │── ArticleRepository(I)
    │   │            │    │    │── ArticleQuerydslRepository(I)
    │   │            │    │    └── ArticleQuerydslRepositoryImpl(C)
    │   │            │    └── service
    │   │            │         │── ArticleCommentService(C)
    │   │            │         └── ArticleService(C)
    │   │            ├── global
    │   │            │    ├── config
    │   │            │    │    │── jpa
    │   │            │    │    │     │── BaseEntity(C)
    │   │            │    │    │     │── BaseTimeEntity(C)
    │   │            │    │    │     └── JpaConfig(C)
    │   │            │    │    │── security
    │   │            │    │    │     │── handler(C)
    │   │            │    │    │     │       │── CustomAccessDeniedHandler(C)
    │   │            │    │    │     │       │── CustomAuthenticationEntryPoint(C)
    │   │            │    │    │     │       │── CustomAuthenticationFailureHandler(C)
    │   │            │    │    │     │       └── CustomAuthenticationSuccessHandler(C)
    │   │            │    │    │     │── CustomAuthenticationProvider(C)
    │   │            │    │    │     │── CustomUserDetailsService(C)
    │   │            │    │    │     │── PrincipalDetail(C)
    │   │            │    │    │     └── SpringSecurityConfig(C)
    │   │            │    │    └── web
    │   │            │    │          └── WebServerCustomizer(C)
    │   │            │    ├── result
    │   │            │    │    │── enums
    │   │            │    │    │     │── CustomErrorCode(E)
    │   │            │    │    │     └── CustomSuccessCode(E)
    │   │            │    │    │── error
    │   │            │    │    │     └── ErrorPageController(C)
    │   │            │    │    │── exception
    │   │            │    │    │     │── BusinessRollbackException(C)
    │   │            │    │    │     │── DuplicateUserInfoException(C)
    │   │            │    │    │     │── NotAllowedUserException(C)
    │   │            │    │    │     │── NotFoundException(C)
    │   │            │    │    │     │── NotValidRequestException(C)
    │   │            │    │    │     └── UnAuthorizedException(C)
    │   │            │    │    │── handler
    │   │            │    │    │     └── GlobalExceptionHandler(C)
    │   │            │    │    └── model
    │   │            │    │          │── CommonResponse(C)
    │   │            │    │          └── ValidationError(C)
    │   │            │    └── result
    │   │            │         │── pagination
    │   │            │         │     │── CustomPaginationRequest(C)
    │   │            │         │     └── CustomPaginationResponse(C)
    │   │            │         └── sort
    │   │            │               └── CustomSortingRequest(C)
    │   │            └── user    
    │   │                 ├── api
    │   │                 │    └── UserApiController(C)
    │   │                 ├── controller
    │   │                 │    └── UserViewController(C)    
    │   │                 ├── dto
    │   │                 │    └── request
    │   │                 │           │── UserCreateDto(C)
    │   │                 │           └── UserInfoDuplicateCheckDto(C)
    │   │                 ├── entity
    │   │                 │    │── User(C)
    │   │                 │    └── UserType(E)
    │   │                 ├── repository
    │   │                 │    └── UserRepository(C)
    │   │                 └── service
    │   │                      └── UserService(C)   
    │   └── resources
    │       ├── static           
    │       │     ├── css
    │       │     │    ├── articles.css
    │       │     │    └── user.css
    │       │     └── js
    │       │          ├── articleComment.js
    │       │          ├── articleCommentApis.js
    │       │          └── signUp.js
    │       ├── templates
    │       │     ├── articles
    │       │     │    ├── create.html
    │       │     │    ├── edit.html
    │       │     │    ├── detail.html
    │       │     │    └── list.html
    │       │     ├── error-page
    │       │     │    ├── 403.html
    │       │     │    ├── 404.html
    │       │     │    └── 500.html
    │       │     ├── fragments
    │       │     │    ├── bodyFooter.html
    │       │     │    ├── bodyHeader.html
    │       │     │    └── header.html
    │       │     └── users
    │       │          ├── sign-in.html
    │       │          └── sign-up.html    
    │       ├── data.sql           
    │       └── application.yaml
    ├── test
    │   ├── java
    │   │     └── io.mykim.projectboard
    │   │            ├── ProjectBoardApplicationTests(C)
    │   │            ├── article
    │   │            │    ├── api
    │   │            │    │    │── ArticleApiControllerTest(C)
    │   │            │    │    └── ArticleCommentApiControllerTest(C)
    │   │            │    ├── controller
    │   │            │    │    └── ArticleViewControllerTest(C)    
    │   │            │    └── service
    │   │            │         │── ArticleCommentServiceTest(C)
    │   │            │         └── ArticleServiceTest(C)
    │   │            ├── config
    │   │            │    ├── WithAuthUser(A)
    │   │            │    └── WithAuthUserSecurityContextFactory(C)
    │   │            ├── init(Disabled)
    │   │            │    ├── DataRestTest(C)
    │   │            │    └── JpaRepositoryTest(C)
    │   │            └── user    
    │   │                 ├── api
    │   │                 │    └── UserApiControllerTest(C)
    │   │                 ├── controller
    │   │                 │    └── UserViewControllerTest(C)    
    │   │                 └── service
    │   │                      └── UserServiceTest(C)   
    │   └── resources
    │       └── application.yaml
```

## 데모 페이지(AWS 서버 종료)
url :http://ec2-3-36-158-39.ap-northeast-2.compute.amazonaws.com

## CI/CD Structure
<img src="document/CI_CD_Structure.png">

## Git branch 운용[ feature(issue) > develop > main > deploy ]
- deploy : 배포용 branch
- main : default branch, 최신버전 유지를 위한 브랜치
- develop : 개발 최상 branch, 기능별 브랜치를 머지
- feature : 기능 별 개발 branch

## Reference
* 유즈케이스 다이어그램
<img src="document/project-board_usecase_v1.svg">
<img src="document/project-board_usecase_v2.svg">


* ERD 다이어그램
<img src="document/project-board_ERD_v1.svg">
<img src="document/project-board_ERD_v2.svg">

* 클래스 다이어그램
<img src="document/project-board_CLASS_v1.svg">
<img src="document/project-board_CLASS_v2.svg">

* API Endpoint
  * https://docs.google.com/spreadsheets/d/1r8QbP8IHxHKV3ZOqgRVC4t4-3RyDnIEKAm7XFUG4RG0/edit#gid=0
  
## Admin page(test 계정 : master/1111)
 * http://ec2-3-36-158-39.ap-northeast-2.compute.amazonaws.com:9090
