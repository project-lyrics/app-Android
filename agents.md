# AI Agent Development Guidelines

## 🚀 필수 실행 지침

### ⚠️ **코드 편집 완료 후 필수 작업**

```bash
# 모든 Kotlin 코드 편집이 완료되면 반드시 실행
./gradlew detekt
```

**중요:** `detektFormat`은 존재하지 않는 task입니다. `./gradlew detekt`를 사용하세요.

**목적:**

- 코드 분석 및 품질 검사
- 자동 formatting 적용 (autoCorrect: true 설정)
- ktlint 규칙 및 Compose 규칙 적용
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

## 🛠️ detekt 사용법

### 사용 가능한 task들

```bash
# 기본 detekt 실행 (분석 + 자동 formatting)
./gradlew detekt

# 다른 유용한 task들
./gradlew detektMain        # production 코드만 분석
./gradlew detektTest        # test 코드만 분석
./gradlew detektBaseline    # baseline 파일 생성
```

### 주요 이슈 유형

- **Formatting**: autoCorrect로 자동 수정됨
- **Complexity**: 수동 리팩토링 필요 (함수 분할, 매개변수 축소)
- **Naming**: 네이밍 컨벤션 수동 수정
- **Compose**: Compose 규칙 준수 확인

### 예외 처리 설정 완료

- **@Composable**: complexity 규칙에서 제외
- **@Preview**: UnusedPrivateMember에서 제외
- **modifier**: UnusedParameter에서 제외
- **fromXXX**: ReturnCount에서 제외

## 🧪 Kotlin Experimental API 사용 규칙

### ⚠️ @OptIn 및 Experimental Import 보호

**개발자가 수동으로 추가한 `@OptIn` 어노테이션 및 관련 Experimental import는 절대 삭제하지 마세요.**

#### 핵심 원칙

```kotlin
// ✅ 올바른 패턴 - Experimental import 유지
import kotlin.time.ExperimentalTime  // @OptIn에서 사용 - 삭제 금지!
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class OAuthToken(val expiresAt: Instant? = null)
```

#### AI Agent 필수 체크사항

1. **파일 수정 전:**

   - `@OptIn` 어노테이션이 있는지 확인
   - `@OptIn(...)`에 전달된 클래스의 import 식별
   - 해당 import를 "보호 목록"에 추가

2. **Import 정리 시:**

   - `@OptIn`에 사용된 import는 **"unused"로 표시되어도 절대 삭제 금지**
   - detekt가 경고를 표시해도 무시하고 유지
   - 개발자가 명시적으로 제거를 요청하기 전까지 보존

3. **일반적인 Experimental API:**
   ```kotlin
   import kotlin.time.ExperimentalTime
   import kotlinx.coroutines.ExperimentalCoroutinesApi
   import androidx.compose.ui.ExperimentalComposeUiApi
   import androidx.compose.foundation.ExperimentalFoundationApi
   import androidx.compose.material3.ExperimentalMaterial3Api
   ```

> **Golden Rule**: `@OptIn` 어노테이션과 함께 사용되는 Experimental import는 **코드에서 직접 참조되지 않아도 컴파일에 필수적**입니다. 절대 자동으로 삭제하지 마세요!

## 📚 참고 리소스

- [Compose Rules](https://mrmans0n.github.io/compose-rules/detekt/)
- [detekt Documentation](https://detekt.dev/)
- [Android Coding Style](https://developer.android.com/kotlin/style-guide)
- [Kotlin Opt-in Requirements](https://kotlinlang.org/docs/opt-in-requirements.html)
