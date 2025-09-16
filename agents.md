# AI Agent Development Guidelines

## 🚀 필수 실행 지침

### ⚠️ **코드 편집 완료 후 필수 작업**

```bash
# 모든 Kotlin 코드 편집이 완료되면 반드시 실행
./gradlew detektFormat
```

**목적:**

- 코드 스타일 자동 수정
- ktlint formatting 규칙 적용
- Compose 규칙 준수
- 일관된 코드 품질 유지

## 📋 프로젝트 설정 정보

### 🔧 도구 구성

- **detekt**: 1.23.7 (기본 룰셋 사용)
- **ktlint**: detekt-formatting을 통해 통합
- **Compose Rules**: 0.4.14 (Compose 전용 규칙)

### 📏 코딩 규칙

- **최대 라인 길이**: 120자
- **Composable 함수**: 대문자로 시작, modifier 매개변수 필수
- **네이밍**: Android 컨벤션 준수 (camelCase, PascalCase)
- **들여쓰기**: 4 spaces

### 🎯 주요 체크 포인트

1. **Modifier 관련**:

   - 모든 Composable에 modifier 매개변수 포함
   - modifier에 기본값 `= Modifier` 설정
   - Root에서 modifier 사용 확인

2. **CompositionLocal 사용**:

   - 허용된 CompositionLocal만 사용
   - 네이밍 규칙: `Local` 접두사

3. **매개변수 순서**:
   - Composable에서 modifier는 첫 번째 선택적 매개변수
   - lambda는 마지막 매개변수

## 🛠️ 에러 처리 가이드

### detekt 실행 결과

```bash
# 코드 검사 (이슈 확인만)
./gradlew detekt

# 자동 수정 (formatting)
./gradlew detektFormat
```

### 주요 이슈 유형

- **Formatting**: detektFormat으로 자동 수정 가능
- **Complexity**: 수동 리팩토링 필요 (함수 분할, 매개변수 축소)
- **Naming**: 네이밍 컨벤션 수동 수정
- **Compose**: Compose 규칙 준수 확인

## 📚 참고 리소스

- [Compose Rules](https://mrmans0n.github.io/compose-rules/detekt/)
- [detekt Documentation](https://detekt.dev/)
- [Android Coding Style](https://developer.android.com/kotlin/style-guide)
