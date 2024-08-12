# DietProject 살 좀 빼자

## 목차
[1. 프로젝트 개요](#1-프로젝트-개요)  
[2. Git Convention](#2-git-convention)  
[3. 개발 환경](#3-개발-환경)
[4. 프로젝트 구조와 개발 일정](#4-프로젝트-구조와-개발-일정)  
[5. 요구사항과 기능 명세](#5-요구사항과-기능-명세)  
[6. 와이어프레임 / UI](#6-와이어프레임--ui)  
[7. 데이터베이스 모델링(ERD)](#7-데이터베이스-모델링erd)  
[8. 주요 기능](#8-주요-기능)  
[9. 개발 이슈](#9-개발-이슈)  
[10. 프로젝트를 진행하며 느낀점](#10-프로젝트를-진행하며-느낀점)

## 1. 프로젝트 개요

### 1.1 프로젝트 명

- DietProject

### 1.2 프로젝트 설명
- 나의 신체 정보를 입력하여 하루 섭취 권장 칼로리 등을 측정하고, 먹은 음식을 기록하여 다이어트를 위한 칼로리 섭취량을 체크하고 반성하는 앱입니다.

## 2. Git Convention

<img width=60% alt="스크린샷 2024-08-06 오전 10 21 54" src="https://github.com/user-attachments/assets/0841f1a1-fe53-4312-b954-dd86e64941ed">


## 3. 개발 환경
### 3.1 개발 환경

- IDE: Android Studio


### 3.2 기술 스택

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-039BE5?style=for-the-badge&logo=Firebase&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)
![Room](https://img.shields.io/badge/Room-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-48B983?style=for-the-badge&logo=square&logoColor=white)
![OkHttp](https://img.shields.io/badge/OkHttp-3E4348?style=for-the-badge&logo=square&logoColor=white)
![Coroutines](https://img.shields.io/badge/Coroutines-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Dagger Hilt](https://img.shields.io/badge/Dagger%20Hilt-2196F3?style=for-the-badge&logo=google&logoColor=white)
![Glide](https://img.shields.io/badge/Glide-20B2AA?style=for-the-badge&logo=android&logoColor=white)
![Git](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)



## 4. 프로젝트 구조와 개발 일정

### 4.1 개발 일정
<img src="https://github.com/user-attachments/assets/9e3166cc-19ae-41ac-a422-3ae828b70259" width=95%>



### 4.2 프로젝트 구조

도메인 주도 설계 원칙을 따르는 아키텍처 패턴으로 구현하였습니다.

📦Spring_Project   
┣ 📂application    
┃ ┣ 📂dto    
┃ ┃   ┣ 📜BoardDTO  
┃ ┃   ┣ 📜CategoryDTO    
┃ ┃   ┣ 📜CommentDTO   
┃ ┃   ┣ 📜NoticeDTO    
┃ ┃   ┣ 📜PostDTO    
┃ ┃   ┗ 📜UserDTO    
┃ ┣ 📜BoardService    
┃ ┣ 📜CategoryService   
┃ ┣ 📜CommentService   
┃ ┣ 📜NoticeService    
┃ ┣ 📜PostService    
┃ ┗ 📜UserService       
┣ 📂config   
┃ ┣ 📜PasswordEncoderConfig    
┃ ┣ 📜SecurityConfig   
┃ ┗ 📜UserStatusCheckFilter    
┣ 📂domain   
┃ ┣ 📜Authority    
┃ ┣ 📜Category   
┃ ┣ 📜Comment    
┃ ┣ 📜Notice   
┃ ┣ 📜Post   
┃ ┗ 📜User   
┣ 📂infrastructure   
┃ ┣ 📂config   
┃ ┃ ┣ 📜QueryDslConfig   
┃ ┃ ┣ 📜Category   
┃ ┃ ┣ 📜Comment    
┃ ┗ 📂persistence    
┃   ┣ 📜CategoryRepository   
┃   ┣ 📜CommentRepository   
┃   ┣ 📜NoticeRepository   
┃   ┣ 📜PostRepository   
┃   ┗ 📜UserRepository   
┗ 📂presentation   
  ┣ 📜BoardController    
  ┣ 📜CommentController    
  ┗ 📜UserController   

## 5. 요구사항과 기능 명세

### 화면 흐름도

<img width="70%" alt="화면 흐름도" src="https://github.com/user-attachments/assets/642382a4-a48c-4848-981a-a4d206975fda">

### 요구 사항 및 기능 명세

<img width="70%" alt="요구사항   기능 명세서" src="https://github.com/user-attachments/assets/d9a089f4-df47-414d-8e30-30511659a171">

## 6. 와이어프레임 / UI

### 6.1 와이어프레임

<img width=70% src="https://github.com/user-attachments/assets/22b085e8-f57c-4e3c-bd75-416081fc74f9">

### 6.2 화면 설계

<table>
    <tbody>
        <tr>
            <td>메인</td>
            <td>로그인</td>
            <td>회원가입</td>
        </tr>
        <tr>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/main.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/cb701758-1717-46cc-ae5e-a10aa81a0c84" width="250" height="350" alt="main">
                </a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/login.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/b5b88f3c-5aa3-4b9d-956d-2c2800d30b61" width="250" height="350" alt="signin">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/signup.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/627c940c-61a7-4bcb-bb85-80aa53f64f6e" width="250" height="350" alt="signup">
</a>
            </td>
        </tr>
        <tr>
            <td>정보수정</td>
            <td>관리자 페이지</td>
            <td>게시글 리스트</td>
        </tr>
        <tr>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/modify.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/3d72c248-8e2f-4c83-a3b6-cdfb156624bc" width="250" height="350" alt="userInfoEdit">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/adminpage.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/af2a96a9-cb7f-4e51-97f1-26373cd81087" width="250" height="350" alt="adminPage">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postList.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/7d1cbb21-0bb1-40bf-a6c4-2b8c42b47061" width="250" height="350" alt="postList">
</a>
            </td>
        </tr>
        <tr>
            <td>게시글 상세보기</td>
            <td>게시글 수정 / 삭제</td>
            <td>글쓰기</td>
        </tr>
        <tr>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postDetail.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/8c7e3abf-0e35-41d5-bf73-f791411a05ef" width="250" height="350" alt="postDetail">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postModify.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/40a38f3b-a941-42be-ba33-f831532e9105" width="250" height="350" alt="postEdit">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postWrite.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/f1e0452c-0945-4f61-ba0b-1179fc531269" width="250" height="350" alt="postWrite">
</a>
            </td>
        </tr>
    </tbody>
</table>


## 7. 데이터베이스 모델링(ERD)

<img src="https://github.com/user-attachments/assets/06f8b0ad-c13a-46a2-85c7-b32ae5207471" width=90%>

## 8. 주요 기능
### 8.1 게시글 작성

<img src="https://github.com/user-attachments/assets/c82f300c-2cfb-4afb-86af-2ce656e88c2a" width=60%>

- 작성자가 사용했던 앱에 대한 후기를 작성하는 기능입니다.
- 앱 분야별 카테고리를 설정할 수 있습니다.
- 앱 대표 이미지를 설정할 수 있습니다.
- URL을 통한 직관적인 연결 기능을 제공합니다.

### 8.2 게시글 수정

<img src="https://github.com/user-attachments/assets/35a49741-ac8a-4e67-a102-ca3bd38bdc80" width=60%>

- 기존에 작성한 게시글을 수정하는 기능입니다.
- 작성 시 설정하였던 아이콘, 제목, 카테고리, URL 및 본문을 수정할 수 있습니다.

### 8.3 게시글 검색

<img src="https://github.com/user-attachments/assets/e0c14b87-a0c0-4ff4-8b33-c5fd48a46538" width=60%>

- 게시글을 검색하는 기능입니다.
- 본인이 원하는 카테고리를 설정 후 제목 또는 작성자 닉네임의 일부분을 이용하여 검색할 수 있습니다.

### 8.4 댓글 및 별점 작성

<img src="https://github.com/user-attachments/assets/3ee1cfdf-2e39-40fd-8973-e65b1db31aa3" width=60%>

- 댓글 및 별점을 작성하는 기능입니다.
- 댓글 작성 시 별점 부여가 가능합니다.
- 댓글마다 부여된 별점은 평균 별점으로 게시글 본문 아래에 집계됩니다.
- 댓글 수정 및 삭제 시에도 즉각적으로 수정된 별점이 반영됩니다.

### 8.5 관리자 기능

<img src="https://github.com/user-attachments/assets/b8273fed-cc86-4709-83f3-a7fef59346ae" width=60%>

- 관리자 페이지로 유저별 권한을 설정하는 기능입니다.
- 회원가입 시 게시글, 댓글 조회 및 작성이 가능한 일반 USER로 권한이 설정됩니다.
- BAN으로 권한을 설정 시에는 공지사항만 조회할 수 있는 기능을 제공합니다.








