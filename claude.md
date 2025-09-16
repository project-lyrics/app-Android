# Claude AI Assistant Rules

## 개발 관련 지시사항

### 코드 편집 완료 후 필수 작업

- **모든 Kotlin 코드 편집 완료 후 반드시 `./gradlew detekt`를 실행하세요**
- **주의**: `detektFormat`은 존재하지 않는 task입니다
- 목적: 코드 분석, 품질 검사 및 자동 formatting 적용

### 프로젝트 구조

- Android Kotlin 프로젝트 (Compose + Hilt)
- detekt 1.23.7 + ktlint formatting + Compose rules 적용
- 기본 ruleset 사용 (buildUponDefaultConfig = true)

### 코딩 스타일 가이드라인

- 최대 라인 길이: 120자
- Android/Compose 네이밍 컨벤션 준수
- Modifier 매개변수 필수 포함
- CompositionLocal 사용 제한

### detekt 최적화 설정 완료

- **@Composable, @Preview 함수**: complexity 및 unused 검사 예외
- **modifier 파라미터**: unused parameter 검사 예외
- **fromXXX 메서드**: return count 제한 예외
- **autoCorrect: true**: formatting 자동 적용 설정
