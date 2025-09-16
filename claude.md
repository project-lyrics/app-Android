# Claude AI Assistant Rules

## 개발 관련 지시사항

### 코드 편집 완료 후 필수 작업

- **모든 Kotlin 코드 편집 완료 후 반드시 `detektFormat`을 실행하세요**
- 명령어: `./gradlew detektFormat`
- 목적: 코드 스타일 자동 수정 및 formatting 규칙 적용

### 프로젝트 구조

- Android Kotlin 프로젝트 (Compose + Hilt)
- detekt 1.23.7 + ktlint formatting + Compose rules 적용
- 기본 ruleset 사용 (buildUponDefaultConfig = true)

### 코딩 스타일 가이드라인

- 최대 라인 길이: 120자
- Android/Compose 네이밍 컨벤션 준수
- Modifier 매개변수 필수 포함
- CompositionLocal 사용 제한

### 주의사항

- detekt 실행 시 발견되는 이슈들은 코드 품질 향상을 위한 것임
- formatting 오류는 detektFormat으로 자동 수정 가능
- 복잡도, 네이밍 등의 이슈는 수동 수정 필요
