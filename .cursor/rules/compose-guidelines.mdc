---
description: Jetpack Compose 개발 가이드라인과 모범 사례
globs: ["**/presentation/**/*.kt", "**/ui/**/*.kt", "**/*Composable*.kt"]
alwaysApply: false
---

# Jetpack Compose 개발 가이드라인

## 📋 Composable 함수 작성 규칙

### ✅ 올바른 예시

```kotlin
@Composable
fun MyButton(
    text: String,
    modifier: Modifier = Modifier,  // 필수: modifier 매개변수
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier  // 필수: modifier 사용
    ) {
        Text(text)
    }
}
```

### ❌ 잘못된 예시

```kotlin
@Composable
fun myButton(text: String, onClick: () -> Unit) {  // modifier 누락
    Button(onClick = onClick) {
        Text(text)
    }
}
```

## 🎯 Compose 규칙 체크리스트

- [ ] Composable 함수명은 PascalCase
- [ ] modifier 매개변수 포함 및 기본값 설정
- [ ] modifier를 root Composable에 적용
- [ ] 매개변수 순서: required → optional (modifier) → lambda
- [ ] CompositionLocal 사용 시 허용 목록 확인

## 🚨 편집 완료 후 필수 실행

```bash
./gradlew detekt
```

**주의**: `detektFormat`은 존재하지 않는 task입니다!
