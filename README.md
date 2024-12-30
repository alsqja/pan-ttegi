# PAN_TTEGI
### 7조 심화 프로젝트 칸반보드 만들기

---
## Tech Stack
JAVA: JDK 17
Spring Boot: 3.3.5
MySQL: Ver 8+
Gradle: 빌드 및 의존성 관리
IntelliJ IDEA: 통합 개발 환경(IDE)
Lombok: 코드 간소화

---

## **API 명세서 (팀 노션 URL)**
[URL](https://documenter.getpostman.com/view/18429295/2sAYJ4gzPD)

## **ERD**
[URL](https://dbdiagram.io/d/trello-639a6f5699cb1f3b55a17baa)

## **Wire Frame**
[URL](https://www.figma.com/design/u30AX1aTd0alV7I3PaA0BL/트렐로?node-id=0-1&p=f&t=PiG83RPjvsHKqvj2-0)

---

## **시연 영상**
[시연 영상 URL]()

## 팀원

| 이름              | Github 프로필  | 블로그     | 역할 |
| ----------------- | -------------- | ---------- | ---- |
| 김민범 | [alsqja]       | [블로그](https://velog.io/@alsqja2626/posts) | 팀장 |
| 김단빈 | [kimdanbin]    | [블로그](https://dreamcompass.tistory.com/)  | 팀원 |
| 김지윤 | [jiyoon0000]   | [블로그](https://jy3574.tistory.com/)        | 팀원 |

[jiyoon0000]: https://github.com/jiyoon0000
[alsqja]: https://github.com/alsqja
[kimdanbin]: https://github.com/kimdanbin

## 수행한 일

### 김민범

- API, ERD, WireFrame 기획 및 설계
- Entity 초기 설정 및 연관관계 생성
- GlobalExceptionHandler 구현
- Spring Security 인증 / 인가 구현
- 각 워크스페이스 별 인가 Interceptor 구현
- 유저 기능 구현
- 보드 기능 구현
- 레디스 캐싱 구현
- 레디스 분산 락 구현
- CI/CD 자동화

### 김단빈

- API, ERD, WireFrame 기획 및 설계
- 카드 기능 구현
- 댓글 기능 구현
- LexoRank 알고리즘을 사용한 카드 순서 변경
- 최적화 인덱싱 처리
- DB 이모지 관련 처리
- PPT 제작

###김지윤

- API, ERD, WireFrame 기획 및 설계
- 워크스페이스 기능 구현
- 리스트 기능 구현
- 파일 업로드 기능 구현
- GreenHopper 알고리즘을 사용한 카드 순서 변경
- 전체 End-to-End 테스트
- 영상 녹화

---

## 기능
### 공통
- 로그인 후 서버는 AccessToken, RefreshToken 을 생성하고 이를 통해 인증 / 인가가 이루어집니다.
- 각 워크스페이스 별 권한을 구분하여 Interceptor 를 통해 인가가 이루어지도록 했습니다.
- 수정, 삭제 시 token 에 저장된 로그인된 유저 정보를 통해 본인만 수정 / 삭제 할 수 있습니다.
- ControllerAdvice를 이용해 GlobalExceptionHandler 를 구현하여 통일된 에러 Response 를 message, status 등을 활용하여 반환할 수 있도록 했습니다.
- User 삭제가 진행될 때 완전히 삭제하지 않고 is_deleted 컬럼을 사용하여 Soft Delete 를 구현했습니다.
- 영속성 전이를 활용해 데이터가 삭제될 때 관련된 데이터가 같이 삭제될 수 있도록 했습니다.
- 각 테이블의 Status 혹은 Role 데이터를 Enum 을 활용해서 관리하여 안정성을 높였습니다.
- 카드와 리스트는 각각의 순서가 있고 이를 변경할 수 있습니다.

### User
- 사용자를 Create, Read, Update, Delete 할 수 있습니다.
- 프로필 조회 시 민감한 정보(password) 는 조회되지 않습니다.
- 비밀번호는 암호화된 상태로 Database 에 저장됩니다.
- 이메일은 중복될 수 없습니다.
- 비밀번호는 최소 8자 이상이며, 대문자, 소문자, 숫자, 특수문자를 최소 1개씩 포함해야 합니다.
- 유저 삭제 시 비밀번호 확인을 한 후 전달받은 쿠키를 포함하여 요청을 보내야 합니다.
- User 는 user, admin 의 role 을 가지고 있고, user 는 admin 권한이 필요한 요청을 수행할 수 없습니다.

### Workspace
- Admin 권한을 가진 사용자 만이 Workspace 를 생성 할 수 있습니다.
- Workspace 를 Create, Read, Update, Delete 할 수 있습니다.
- Workspace 별로 멤버마다 각각 다른 권한이 부여되고 할 수 있는 행동에 제약이 생깁니다.
- Workspace 는 여러개의 Board 를 가질 수 있습니다.

### Board
- Board 는 name, description, color, image_url 을 가지고 있고, 여러개의 List 를 가질 수 있습니다.
- Board 를 Create, Read, Update, Delete 할 수 있습니다.
- Board 하나를 조회하면 해당 보드가 가지고 있는 리스트, 카드 정보를 함께 조회할 수 있습니다.

### List
- List 는 title, position 을 가지고 있고, position 은 BigDecimal 로 관리됩니다.
- List 를 Create, Read, Update, Delete 할 수 있습니다.
- GreenHopper 알고리즘을 사용해 순서를 변경, 저장 합니다.
- 여러개의 Card 를 가질 수 있습니다.

### Card
- Card 는 title, description, position, end_at 을 가지고 있고, 여러개의 파일을 첨부할 수 있습니다.
- Card 를 Create, Read, Update, Delete 할 수 있습니다.
- LexoRank 알고리즘을 사용해 순서를 변경, 저장 합니다.
- 카드 정보는 인덱싱 되어 관리됩니다.
- 캐시에 카드의 조회수를 저장하고 랭킹 TOP 10 을 조회할 수 있습니다.
- 랭킹에 있는 카드 관련 정보는 매 시간 정각에 캐시에 업로드 되어 조회할 수 있습니다.
- 한 유저가 여러번 조회수를 올릴 수 없습니다.
- 조회수 관련 사항들은 매일 자정 초기화됩니다.

### File
- File 을 업로드, 조회, 삭제 할 수 있습니다.
- File 을 formdata 로 서버에 넘기면 S3 에 업로드 후 Url 을 저장합니다.

## 마치며
### 완성 소감
- **김민범** : 팀원 분들 모두 맡은 파트를 잘 진행해 주시고 적극적으로 진행 내용 공유등을 해주셔서 정말 즐겁게 프로젝트를 진행 할 수 있었습니다. 아쉬웠던 점은 다음에 진행하게 된다면 기능 구현을 좀 더 생각한 ERD 작성과 Restful한 API 작성을 고민해보고 진행해야 겠다 생각합니다. 한 주 동안의 프로젝트 였지만 정말 재밌게 코딩하고 많이 배울 수 있었습니다!
- **김단빈** : 긍정적인 팀 프로젝트 경험이 되었습니다. 팀장님이 잘 리드 해주셔서 다른 팀원들도 잘 따라갈 수 있었지않나 생각이 들기도 합니다. 설계부터 구현하여 테스트까지 각자 맡은 부분에 열심히 하여 각 기능이 제대로 돌아가도록 만드는 재밌는 프로젝트가 되었던것 같습니다.
- **김지윤** : 항상 적극적으로 도움을 주시는 팀원분들 덕분에 정말 많은 것들을 배운 프로젝트였습니다. 팀원분들께서 언제나 빠른 피드백을 주셨고, 의사소통이 원활했기에 효율적으로 진행이 되었던 것 같습니다. 많이 부족한 저를 늘 열정적으로 이끌어 주신 팀원분들께 정말 감사드립니다. 

### 아쉬웠던 점
- **김민범** : 초기 기획 단계가 중요한 것을 잘 알고 신경써서 기획을 했지만, 개발을 진행하며 여러 문제들이 발생했던 부분이 아쉬웠습니다. 도전 과제에 재미있어 보이는 내용이 많았는데 불경기에 인원 감축을 당해 시간이 부족하여 구현하지 못해 아쉽습니다.
- **김단빈** : 처음에 어디까지 구현을 할지 미리 도전기능까지 생각을 해서 설계를 했지만 이러한 방법은 그렇게 좋지 않다는 피드백을 받아서 다음 프로젝트 부터는 필수기능에 충실히 이행을 한 뒤에 추가적으로 도전기능을 생각하며 확장해나가는 시간을 가져보고싶다.
- **김지윤** : 이번 프로젝트는 하나부터 열까지 다 팀원분들의 도움이 없었다면 불가능했을 정도로 제게는 프로젝트 관련 경험과 지식이 많이 부족한 상태였습니다. 특히 학습이 충분히 되지 못해서 구현 부분이 많이 어려웠고, 다른 팀원분들의 진행 상황을 듣고도 그것이 어떤 내용인지 이해가 가지 않는 경우도 있었습니다. 이번 프로젝트 이후에 공부를 더 많이 해서 다음 프로젝트 때에는 조금 더 다양한 기능을 구현해보고 싶습니다.        










