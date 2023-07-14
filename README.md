# BE_DAMDA
![](https://velog.velcdn.com/images/yeeeerim_/post/3a6d51e8-0f36-429f-8666-2c3a7f1b3179/image.gif)
## 📢 프로젝트 소개
### 개발내용
> 옷장정리 서비스 기업인 열다컴퍼니의 유저단 서비스 플로우 자동화 및 어드민단 개발

### 개발인원 
FE(4명), BE(3명)
### 개발기간
2023/05/30 ~ 2023/06/28
### 기술스택 
- FE : Next.js, Emotion, React-query, Zustand, React Beautiful dnd, Framer-motion, Date-fns, Axios
- BE : SpringBoot, JPA, Querydsl, EC2, S3, Load Balancing, mysql, OAuth, apache poi, solapi
### ERD
![](https://velog.velcdn.com/images/yeeeerim_/post/1e732ed1-3c2a-48b7-a8e6-eeb2d0cbe06b/image.png)
### 아키텍처
![](https://velog.velcdn.com/images/yeeeerim_/post/cf72bc63-4144-42fc-b67d-5129ddb634ad/image.png)
    
## 기능소개

### 🙋🏻‍♀️ 카카오 로그인
![](https://velog.velcdn.com/images/yeeeerim_/post/2ac64dc4-0bd1-44a1-8adc-b1eacc21efce/image.gif)
유저 로그인은 카카오 로그인만 지원한다. 
### 🙋🏻‍♀️ 예약 접수
![](https://velog.velcdn.com/images/yeeeerim_/post/22ba63ac-8ffa-417c-9a8e-4d864ae832ba/image.gif)
![](https://velog.velcdn.com/images/yeeeerim_/post/94e44ce1-8229-4a0e-9f69-111fd1a23d90/image.gif)
 유저들은 유저페이지에서 예약접수를 할 수 있고, 서비스를 예약지역은 실제 활동 매니저가 있는 지역들만 노출된다. 
![](https://velog.velcdn.com/images/yeeeerim_/post/9571125f-ed20-40d6-a307-105205c0e262/image.png)

 - 예약이 접수되면 매칭중(해당 지역의 매니저가 응답을 하기 전)이라는 상태로 등록되며 유저에게는 예약접수 알림톡이, 해당 지역의 매니저들에게는 서비스 접수 알림톡을 보낸다. 


### 🧑🏻‍💼 매니저 매칭수락
![](https://velog.velcdn.com/images/yeeeerim_/post/58c35b80-81e1-4262-81a8-aaebbdbbc322/image.gif)
매니저는 알림톡의 링크에 접속해 해당 예약을수락/거절을 할 수 있다. 
이때 매니저는 로그인 상태여야 한다. 
### 🧑🏻‍💻 어드민 매칭승인
![](https://velog.velcdn.com/images/yeeeerim_/post/cb5ada6f-baf4-4ba2-a088-40544fad737c/image.gif)
- 매칭 매니저들 중 한 명이상 대답하면 예약상태는 매칭대기중으로 변경된다. 이때 어드민은 매칭리스트를 확인할 수 있으며 매니저의 정보를 보고 예약의 매니저들을 골라 매칭승인을 할 수 있다. 이때 해당 예약에 매칭되어야 하는 매니저들보다 많이 선택한다면 오류가 나며, 적게 매칭된다면 상태는 변경되지 않는다. 
- 예약에 매칭되어야 하는 매니저 수는 예약 접수 시 DB에 저장되어 있다. 
- 매칭 매니저 수 = 승인 매니저 수라면 서비스 완료 상태로 변경된다. 
![](https://velog.velcdn.com/images/yeeeerim_/post/8d28c6c9-0434-480e-816c-3b84c252c54b/image.png)
- 매칭이 끝나면 매칭실패매니저, 매칭성공매니저, 유저에게 알림톡을 보낸다
![](https://velog.velcdn.com/images/yeeeerim_/post/4d146b53-b025-488e-9148-ae5b1a8aed35/image.png)
- 또 예약 하루 전 보내는 리마인드 메시지와 매니저 서비스 완료폼 제출 메시지를 예약한다. 
### 🙅🏻예약 취소
- 예약취소는 어드민 페이지에서만 가능하다. 
![](https://velog.velcdn.com/images/yeeeerim_/post/a6cb265b-c472-409b-adcb-5669a43b78a6/image.png)
- 매칭 전 예약 취소가 되었다면 예약상태를 예약취소로만 바꾼다. 
![](https://velog.velcdn.com/images/yeeeerim_/post/7261ec8e-f442-4d98-ba9c-a3894bf5e0c5/image.png)
- 예약 취소를 할때 필요한 예약메시지 GroupId를 저장해놓은 테이블을 활용해 예약메시지를 취소한다. 
- 매칭 매니저들에게 예약 취소 알림톡을 보낸다. 
### 🧑🏻‍💼 서비스 완료
![](https://velog.velcdn.com/images/yeeeerim_/post/42e9fc8f-7e43-47c1-96f8-e767600e5f4c/image.gif)
- 매니저는 서비스 완료시간 30분전에 서비스 완료 폼 제출 완료톡을 받고 해당 링크를 통해 비포/애프터 사진을 제출한다. 
- 한 예약당 서비스 완료 폼은 한 번만 제출할 수 있기 때문에 링크 알림톡은 레벨이 높은 매니저 1명에게만 전송한다. 

![](https://velog.velcdn.com/images/yeeeerim_/post/dea5a4b4-a037-46a3-a77b-ff2dce5ccdec/image.gif)
![](https://velog.velcdn.com/images/yeeeerim_/post/f22cdb3a-67f5-4e39-bf89-927a6cc64a1a/image.png)
- 서비스 완료폼이 제출되면 예약은 서비스 완료 상태로 바뀌고, 유저에게 결제 안내 알림톡을 전송한다. 
- 서비스 가격은 예약 접수 시 미리 예약정보에 저장되어 있다. 
### 💳 결제완료
![](https://velog.velcdn.com/images/yeeeerim_/post/f4b6d559-6b66-493e-a2c3-36a51e074a8b/image.png)
- 결제완료 시 유저에게 추천인 코드가 생성되고 해당 코드를 확인 할 수 있는 알림톡이 전송된다. 
![](https://velog.velcdn.com/images/yeeeerim_/post/cd50d797-2e56-40d8-86f5-ea9474b0b76c/image.png)
### 👩🏻‍💻 예약 폼 양식 변경
![](https://velog.velcdn.com/images/yeeeerim_/post/eadc828c-16f8-42d6-a5c5-e975d849da35/image.gif)
- 어드민 단에서 유저단에서 보이는 예약 폼 양식을 변경 할 수 있다. 
### 📨 매니저 지원
![](https://velog.velcdn.com/images/yeeeerim_/post/68133288-70c1-4f20-97e6-a4b13bd55a5b/image.gif)
- 유저는 매니저를 지원할 수 있다. 
![](https://velog.velcdn.com/images/yeeeerim_/post/524ae71f-ca9a-4afe-86bc-68efb504d330/image.gif)
- 어드민단에서 매니저들을 관리할 수 있으며 처음 접수된 매니저들은 대기 상태이다. 
- 매니저들의 상태를 나누어 관리할 수 있다. 
![](https://velog.velcdn.com/images/yeeeerim_/post/18c6c0e8-a70e-4c15-b45a-62e3c6759cd8/image.gif)
- 어드민은 매니저의 모든 정보들을 수정/ 변경할 수 있으며 지원폼과 예약내역등을 확인할 수 있다. 

### 🌟 리뷰
![](https://velog.velcdn.com/images/yeeeerim_/post/8d1fe2e3-5196-46ff-a9ea-2e8558d3b0a2/image.gif)
- 리뷰는 어드민단에서 작성이 가능하다. 
- 서비스 완료 리스트에서 고객을 선택하고 해당 서비스 완료 폼에 제출된 이미지들을 가져와 제목, 본문을 작성 할 수 있다. 

![](https://velog.velcdn.com/images/yeeeerim_/post/0901b510-85ed-46a2-b790-ff8568126806/image.gif)
- 작성한 리뷰는 유저 페이지에서 확인가능하다. 
### 👩🏻‍💻 고객관리
![](https://velog.velcdn.com/images/yeeeerim_/post/bef2dd48-09f6-4dab-b149-9ef947754147/image.gif)

### 📊 예약내역 엑셀 다운로드
![](https://velog.velcdn.com/images/yeeeerim_/post/4a8ccd1d-066f-4055-bb6c-0a870c482d69/image.gif)

### Q&A
![](https://velog.velcdn.com/images/yeeeerim_/post/4a9662ca-1e89-4112-b56e-c6fd134fb640/image.gif)
![](https://velog.velcdn.com/images/yeeeerim_/post/0dd0a1e0-9a35-44b8-b68a-4f0409d2e687/image.gif)
