---
description: 스테이징된 변경을 기준으로 커밋 메시지를 제안하고 커밋은 실행하지 않음
---

스테이징된 변경만 기준으로 커밋 메시지를 생성하고 제안한다.
절대로 `git commit`을 실행하지 않는다.
staged 변경이 없으면 메시지 제안도 진행하지 않는다.

## Step 1: Gather Context

```bash
# What's staged?
git diff --cached --stat
git diff --cached --name-only

# What branch are we on?
git branch --show-current

# Recent commits for style reference
git log --oneline -5
```

언제나 staged 변경만 대상으로 보고, unstaged 변경이나 untracked 파일은 자동으로 포함하지 않는다.

## Step 2: Analyze Changes

staged diff를 읽고 변경의 성격을 파악한다.

```bash
git diff --cached
```

변경 유형은 아래 중 하나로 분류한다.

- init: 초기 설정
- feat: 신규 기능
- docs: 문서 변경
- build: 빌드 관련 변경
- design: 화면 디자인 변경
- fix: 버그 수정
- chore: 자잘한 유지보수
- refactor: 리팩터링
- ci: CI 설정 변경
- test: 테스트 변경
- perf: 성능 개선

## Step 3: Generate Commit Message

기본 형식:

```text
<type>: <Subject>

<body - what and why, not how>
```

## Core rules

### 1. 커밋 메시지는 영문으로 쓴다

- type은 소문자 영어를 유지한다
- 제목과 본문 모두 영문으로 작성한다
- 고유명사나 코드 용어는 그대로 유지할 수 있다

### 2. 제목과 본문 사이는 한 줄 비운다

- 단순 변경이면 제목만 사용 가능
- 맥락이 필요하면 제목 + 빈 줄 + 본문 사용
- 제목 50자 안에 변경 의도를 충분히 담기 어렵다면 본문을 추가한다

### 3. 제목은 50자 이내를 우선한다

- 권장: 50자 이내
- 최대: 72자 이내

### 4. 제목은 명령형으로 쓴다

- Git 자체가 명령형을 사용하므로 일관성을 유지한다

### 5. 제목 끝에는 마침표를 붙이지 않는다

### 6. 본문은 72자 기준으로 줄바꿈한다

- Git은 자동 줄바꿈을 하지 않음
- 수동으로 72자마다 줄바꿈 필요

### 7. 본문은 how보다 what과 why를 설명한다

- 변경 이유와 배경 설명
- 이전 상태의 문제점 명시
- 코드 자체가 how를 설명하므로 맥락에 집중

## Step 4: Present Options

채팅에 커밋 메시지 후보 3개를 코드 블록으로 제안한다.
사용자가 바로 복사해서 쓸 수 있도록 형식을 유지한다.

```markdown
## Suggested Commits

### Option 1 (recommended)
```
feat: Add automatic token refresh

Refresh access tokens before session expiry so long-running usage
does not unexpectedly force the user to log in again.
```

### Option 2 (minimal)
```
feat: Add token refresh
```

### Option 3 (detailed)
```
feat: Introduce proactive JWT token refresh

Reduce unexpected session expiration during long-running usage by
renewing tokens before they expire.
```
```

## Final rule

- `git commit`을 실행하지 않는다
- 사용자가 원하더라도 이 명령 자체에서는 메시지 제안만 수행한다
- 실제 커밋이 필요하면 별도 커밋 워크플로우 또는 명령을 사용하도록 안내한다
- staged 변경만 기준으로 제안한다

# Reference article

@`.agent/rules/git-commit-rules.md`
