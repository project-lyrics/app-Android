---
trigger: model_decision
description: When commit the results
---

# Git 커밋 메시지 작성 규칙

## ⚠️ 중요: 언어 규칙

**커밋 메시지는 반드시 영문으로 작성해야 합니다.**

- ✅ 영문으로만 작성
- ❌ 한국어 커밋 메시지 사용 금지

```
✅ feat: Add user profile screen
❌ feat: 사용자 프로필 화면 추가
```

## 개요

좋은 Git 커밋 메시지는 코드 변경사항의 맥락을 전달하는 가장 효과적인 방법입니다. 이 규칙들을 따라 일관되고 명확한 커밋 히스토리를 유지하세요.

## 사용 가능한 접두사

- init: 초기 설정
- feat: 새로운 기능에 대한 커밋
- docs: 문서 수정에 대한 커밋
- build: 빌드 관련 파일 수정에 대한 커밋
- design: 화면 디자인 수정에 대한 커밋
- fix: 버그 수정에 대한 커밋
- chore: 그 외 자잘한 수정에 대한 커밋
- refactor: 코드 리팩토링에 대한 커밋
- ci: CI관련 설정 수정에 대한 커밋
- test: 테스트 코드 수정에 대한 커밋

### 접두사 사용 예시

```
✅ feat: Add user profile screen
✅ fix: Resolve heart rate sensor integration issue
✅ docs: Update authentication documentation
✅ refactor: Simplify login validation logic
✅ design: Update home screen layout

❌ Add user profile screen (접두사 누락)
❌ Feat: Add feature (대문자 사용)
❌ feat : Add feature (공백 잘못됨)
❌ feat:Add feature (공백 누락)
```

### 접두사 규칙 상세

1. **형식**: `타입:` (소문자 + 콜론 + 공백 1개)
2. **위치**: 커밋 메시지 제목의 맨 앞
3. **타입 선택**: 변경사항의 성격에 맞는 타입 하나만 선택
4. **글자 수 계산**: 접두사도 50자 제한에 포함됨
5. **본문**: 본문에는 접두사 불필요

## 7가지 핵심 규칙

### 1. 제목과 본문을 빈 줄로 분리

- 간단한 변경사항: 제목만 사용
- 복잡한 변경사항: 제목 + 빈 줄 + 본문

### 2. 제목을 50자로 제한

- 권장: 50자 이내
- 절대 한계: 72자
- GitHub UI가 이 규칙을 인식하여 경고/생략 표시

### 3. 제목의 첫 글자를 대문자로 작성

```
✅ feat: Add user authentication feature
❌ feat: add user authentication feature
```

### 4. 제목 끝에 마침표 사용 금지

```
✅ fix: Fix login validation bug
❌ fix: Fix login validation bug.
```

### 5. 제목에서 명령형 어조 사용

Git 자체가 명령형을 사용하므로 일관성 유지

```
✅ feat: Add new payment method
✅ fix: Fix database connection issue
✅ refactor: Remove deprecated API endpoints

❌ feat: Added new payment method
❌ fix: Fixed database connection issue
❌ refactor: Removing deprecated API endpoints
```

**테스트 방법**: "If applied, this commit will [제목]"이 자연스러워야 함

### 6. 본문을 72자에서 줄바꿈

- Git은 자동 줄바꿈을 하지 않음
- 수동으로 72자마다 줄바꿈 필요

### 7. 본문에서 '무엇'과 '왜'를 설명 ('어떻게'가 아닌)

- 변경 이유와 배경 설명
- 이전 상태의 문제점 명시
- 코드 자체가 '어떻게'를 설명하므로 맥락에 집중

## 커밋 메시지 템플릿

```
타입: 제목 - 50자 이내로 변경사항 요약

본문: 필요시 자세한 설명 (72자에서 줄바꿈)
- 왜 이 변경이 필요한가?
- 어떤 문제를 해결하는가?
- 부작용이나 주의사항은?

- 불릿 포인트 사용 가능
- 하이픈이나 별표 사용

이슈 트래커 참조:
Resolves: #123
See also: #456, #789
```

## 실제 예시

### 좋은 예시

#### 기능 추가

```
feat: Add user session timeout feature

Implement automatic logout after 30 minutes of inactivity
to enhance security. Users will receive a warning 5 minutes
before timeout with option to extend session.

- Add session monitoring service
- Implement warning modal component
- Update authentication middleware

Resolves: #234
```

#### 버그 수정

```
fix: Fix heart rate monitoring accuracy

Improve sensor data processing algorithm to reduce noise
and provide more accurate heart rate measurements during
high-intensity activities.

- Update sensor polling interval
- Implement Kalman filter for data smoothing
- Add calibration routine

Resolves: #456
```

#### 리팩토링

```
refactor: Simplify authentication flow

Consolidate multiple authentication checks into a single
unified service for better maintainability.

- Extract auth logic to AuthService
- Remove duplicate validation code
- Update related unit tests

Resolves: #789
```

### 나쁜 예시

```
fixed stuff

changed some files and updated things
```

```
Add feature (접두사 누락)
```

```
Feat: Add feature (대문자 사용 오류)
```

```
feat: 사용자 프로필 추가 (한국어 사용 - 금지!)
```

## 추가 가이드라인

### 원자적 커밋 (Atomic Commits)

- 하나의 논리적 변경사항만 포함
- 각 커밋이 독립적으로 빌드/테스트 가능해야 함

### 커밋 빈도

- 작은 단위로 자주 커밋
- 의미 있는 진행 단계마다 커밋
- WIP(Work In Progress) 커밋은 나중에 squash

### 브랜치별 전략

- `main`: apk를 전달하거나 스토어에 출시한 경우만
- `develop`: 완성된 기능만
- `feature/*`: 세부적인 작업 단위
- `fix/*`: develop 병합 이후 발생한 급하지 않은 이슈를 수정하는 작업 단위
- 나머지 브랜칭 규칙은 Git Flow를 따름

## 도구 및 설정

### Git 설정

```bash
# 커밋 메시지 템플릿 설정
git config --global commit.template ~/.gitmessage

# 기본 에디터 설정 (예: VS Code)
git config --global core.editor "code --wait"
```

### 커밋 메시지 템플릿 파일 (~/.gitmessage)

```
# 제목 (50자 이내)

# 본문 (72자에서 줄바꿈)
# - 왜 이 변경이 필요한가?
# - 어떤 문제를 해결하는가?
# - 부작용이나 주의사항은?

# 이슈 참조
# Resolves: #
# See also: #
```

## 참고 자료

- [How to Write a Git Commit Message](https://cbea.ms/git-commit/)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Angular Commit Message Guidelines](https://github.com/angular/angular/blob/master/CONTRIBUTING.md#commit)
