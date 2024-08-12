# DietProject 살 좀 빼자

## 목차
[1. 프로젝트 개요](#1-프로젝트-개요)  
[2. Git Convention](#2-git-convention)  
[3. 개발 환경](#3-개발-환경)    
[4. 프로젝트 구조](#4-프로젝트-구조)    
[5. 요구사항과 기능 명세](#5-요구사항과-기능-명세)  
[6. 와이어프레임 / UI](#6-와이어프레임--ui)  
[8. 주요 기능](#8-주요-기능)  
[9. 개발 이슈](#9-개발-이슈)  
[10. 프로젝트를 진행하며 느낀점](#10-프로젝트를-진행하며-느낀점)

## 1. 프로젝트 개요

### 1.1 프로젝트 명

- DietProject

### 1.2 프로젝트 설명
- 회원가입 시 성별, 체중, 키, 활동량 입력 후 그에 따른 위한 일일 개인 권장 섭취 칼로리를 계산하여 식단에 따른 섭취량, 운동량을 계산하여 다이어트에 도움을 줄 수 있는 앱입니다.

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



## 4. 프로젝트 구조


#### data layer

    📦 data
    ├─ 📂dataSource
    │  ├─ 📂remoteDatasource
    │  │  ├─ 📜FirebaseDatasource
    │  │  └─ 📜SharedPreferenceDatasource
    │  └─ 📂localDatasource
    │  │  ├─ 📜FoodDiaryDatasource
    │  │  └─ 📜KcalDatasource
    ├─ 📂db
    │  ├─📂 local
    │  │    ├─📂 dao
    │  │    │  └─ 📜FoodDiaryDao
    │  │    ├─📂 db
    │  │    │  └─ 📜FoodDiaryDatabase
    │  │    └─📂 entity            
    │  │       └─ 📜FoodDiaryEntity
    │  ├─📂 local
    │  │      ├─ 📜FirebaseDatasource
    │  │      └─ 📜KcalDatasource
    │  └─📂 remote
    │     ├─📂 api
    │     │  └─ 📜KcalApi
    │     ├─📂 userEntity
    │     │  └─ 📜UserEntity
    │     ├─📂 interactor
    │     │  ├─ 📜NetworkErrorHandlerImpl
    │     │  └─ 📜CustomInterceptor
    │     └─📂 response
    │        └─ 📜KcalResponse
    ├─ 📂di
    │  ├─ 📜ApiModule
    │  ├─ 📜LocalModule
    │  ├─ 📜NetworkModule
    │  └─ 📜RepositoryModule
    ├─ 📂mapper
    │  ├─ 📜FirebaseMapper
    │  ├─ 📜FoodDiaryMapper
    │  └─ 📜KcalDataMapper
    └─ 📂repository
       ├─ 📜FirebaseRepositoryImpl
       ├─ 📜FoodDiaryReposiroyImpl
       ├─ 📜SharedPreferenceRepositoryImpl
       └─ 📜KcalRepositoryImpl

#### domain layer

```
📦 domain
├─ 📂error
│  ├─ 📜NetworkError
│  ├─ 📜NetworkErrorHandler
│  └─ 📜NetworkResult
├─ 📂model
│  ├─ 📜KcalModel
│  ├─ 📜FoodDiaryModel
│  └─ 📜UserModel
├─ 📂repository
│  ├─ 📜FirebaseRepository
│  ├─ 📜FoodDiaryRepository
│  ├─ 📜SharedPreferenceRepository
│  └─ 📜KcalRepository
└─ 📂usecase
   └─ so many UseCase

```

#### presentation layer

```

📦 presentation
├─ 📂calendar
│  └─ 📜Adapter, ViewHolder, ViewModel, Fragment
├─ 📂home
│  └─ 📜Fragment, ViewModel
├─ 📂info
│  └─ 📜Fragment, ViewModel
├─ 📂login
│  └─ 📜Fragment, ViewModel
├─ 📂personal_info
│  └─ 📜Fragment, ViewModel
├─ 📂signup
│  └─ 📜Fragment
├─ 📂userkcal
│  └─ 📜Adapter, ViewHolder, ViewModel, Fragment
├─ 📂util
│  ├─ 📜BackPressedHandler
│  ├─ 📜Event
│  └─ 📜Extends(Network)
├─ 📂weightchart
│  └─ 📜MarkerView, Fragment, viewModel
├─ 📜BaseFragment
├─ 📜LoadingProgress
└─ 📜MainActivity

```

## 5. 요구사항과 기능 명세

### 화면 흐름도

<img width="70%" alt="화면 흐름도" src="https://github.com/user-attachments/assets/642382a4-a48c-4848-981a-a4d206975fda">

### 요구 사항 및 기능 명세

<img width="70%" alt="요구사항   기능 명세서" src="https://github.com/user-attachments/assets/d9a089f4-df47-414d-8e30-30511659a171">

## 6. 와이어프레임 / UI

### 6.1 화면 설계

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
                <img src="https://github.com/user-attachments/assets/c7a96d68-b48d-4e37-929e-14b5671a5909" width="250" height="350" alt="main">
                </a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/login.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/da7cb3e0-fadc-4a8f-b5b1-98a17d6b079e" width="250" height="350" alt="signin">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/signup.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/b37d15ff-9085-4277-93ee-0280db6ee6e5" width="250" height="350" alt="signup">
</a>
            </td>
        </tr>
        <tr>
            <td>내 정보</td>
            <td>정보 입력</td>
            <td>검색 리스트</td>
        </tr>
        <tr>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/modify.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/6eea49e1-b747-492d-a6fb-1aa1d2e52075" width="250" height="350" alt="myPage">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/adminpage.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/d407367b-4524-4a48-9e15-0ade439ed692" width="250" height="350" alt="adminPage">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postList.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/f3255f66-f31e-4275-b877-1d9007fbb98a" width="250" height="350" alt="postList">
</a>
            </td>
        </tr>
        <tr>
            <td>즐겨찾기 리스트</td>
            <td>음식 정보</td>
            <td>달력</td>
        </tr>
        <tr>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postDetail.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/92c5559c-d5d7-4f04-af30-9586f76f3d26" width="250" height="350" alt="postDetail">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postModify.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/b8905d91-545a-4a6f-913f-d6602e80d524" width="250" height="350" alt="postEdit">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postWrite.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/84e7d24b-af0f-48a4-a4b5-ba177fe39357" width="250" height="350" alt="postWrite">
</a>
            </td>
        </tr>
        <tr>
            <td>달력 상세보기</td>
            <td>차트</td>
            <td>달력</td>
        </tr>
        <tr>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postDetail.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/5999f54a-6302-47e9-9e81-ca309190241e" width="250" height="350" alt="postDetail">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postModify.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/3493ca32-894e-432c-9605-1368aeb92a2f" width="250" height="350" alt="postEdit">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postWrite.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/84e7d24b-af0f-48a4-a4b5-ba177fe39357" width="250" height="350" alt="postWrite">
</a>
            </td>
        </tr>
</table>



## 8. 주요 기능



## 9. 개발 이슈






