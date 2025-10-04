# AI Agent Development Guidelines

## 📝 Git 커밋 메시지 규칙

### 핵심 원칙

좋은 Git 커밋 메시지는 코드 변경사항의 **맥락(context)**을 전달하는 가장 효과적인 방법입니다. diff는 **무엇이** 바뀌었는지 보여주지만, 커밋 메시지만이 **왜** 바뀌었는지 설명할 수 있습니다.

### ⚠️ 중요: 영문 작성 필수

**모든 커밋 메시지는 반드시 영문으로 작성해야 합니다.**

- 국제적인 협업과 코드베이스 일관성을 위해 영문 사용
- 기술 용어의 명확한 전달
- Git 히스토리의 표준화

### 팀 커밋 접두사 컨벤션

변경사항의 유형을 명확히 하기 위해 다음 접두사를 사용합니다:

- `init:` - Initial setup
- `feat:` - New feature
- `docs:` - Documentation changes
- `build:` - Build system or dependency changes
- `design:` - UI/UX design changes
- `fix:` - Bug fixes
- `chore:` - Other minor changes
- `refactor:` - Code refactoring
- `ci:` - CI configuration changes (e.g., detekt.yml, GitHub Actions)
- `test:` - Test code changes

**Format**: `prefix: message` (lowercase prefix + colon + space + message)

**Examples**:

```
feat: Add Kakao login feature
fix: Resolve token refresh error
ci: Optimize detekt rules
```

### 7가지 필수 규칙

#### 1. 제목과 본문을 빈 줄로 분리

```
Subject: Summary of changes

Body: Detailed explanation (if needed)
```

#### 2. 제목을 50자로 제한

- Recommended: Within 50 characters
- Hard limit: 72 characters

#### 3. 제목의 첫 글자를 대문자로 작성

```
✅ feat: Add user authentication
✅ fix: Resolve login validation bug

❌ feat: add user authentication
❌ fix: resolved login validation bug
```

#### 4. 제목 끝에 마침표 사용 금지

```
✅ fix: Login validation bug
❌ fix: Login validation bug.
```

#### 5. 제목에서 명령형 어조 사용

```
✅ feat: Add new feature
✅ fix: Fix critical bug
✅ refactor: Remove deprecated code

❌ feat: Added new feature
❌ fix: Fixed critical bug
❌ refactor: Removing deprecated code
```

**Test**: "If applied, this commit will [subject]" should read naturally

#### 6. 본문을 72자에서 줄바꿈

Git does not automatically wrap text, so manual line breaks are required

#### 7. 본문에서 '무엇'과 '왜'를 설명

- ✅ Explain why the change is needed
- ✅ Describe the problem being solved
- ✅ Note any side effects or considerations
- ❌ Don't explain how (the code shows that)

### 커밋 메시지 템플릿

```
prefix: Subject within 50 characters

Body (if needed):
- Why is this change necessary?
- What problem does it solve?
- Are there any side effects or considerations?

Issue references:
Resolves: #123
See also: #456
```

### 실제 예시

#### 좋은 커밋 메시지

```
feat: Add user session timeout feature

Implement automatic logout after 30 minutes of inactivity
to enhance security. Users receive warning 5 minutes before
timeout with option to extend session.

- Add session monitoring service
- Implement warning modal component
- Update authentication middleware

Resolves: #234
```

```
fix: Resolve token refresh infinite loop

Token refresh was triggering multiple simultaneous requests
causing race conditions. Add mutex lock to ensure only one
refresh attempt at a time.

Resolves: #456
```

```
ci: Update detekt configuration

Enable autoCorrect and add exception rules for Composable
functions to improve code quality automation and reduce
false positives.

- Set autoCorrect: true
- Add Composable complexity exception
- Add modifier unused parameter exception
```

#### 나쁜 커밋 메시지

```
fixed stuff
updated files
minor changes
bug fix
update code
WIP
```

### 원자적 커밋 (Atomic Commits)

- Include only one logical change per commit
- Each commit should build and test independently
- Separate unrelated changes into different commits

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
