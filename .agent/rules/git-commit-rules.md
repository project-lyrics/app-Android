---
trigger: manual
---

# Git Commit Message Rules

## Overview

A good Git commit message is the most effective way to communicate the context of code changes. Follow these rules to maintain a consistent and clear commit history.

## 7 Essential Rules

### 1. Separate subject from body with a blank line

- Simple changes: subject only
- Complex changes: subject + blank line + body

### 2. Limit the subject line to 50 characters

- Recommended: within 50 characters
- Hard limit: 72 characters
- GitHub UI recognizes this rule and shows warnings/truncation

### 3. Capitalize the subject line

```
✅ Add user authentication feature
❌ add user authentication feature
```

### 4. Do not end the subject line with a period

```
✅ Fix login validation bug
❌ Fix login validation bug.
```

### 5. Use the imperative mood in the subject line

Git itself uses imperative mood, so maintain consistency

```
✅ Add new payment method
✅ Fix database connection issue
✅ Remove deprecated API endpoints

❌ Added new payment method
❌ Fixed database connection issue
❌ Removing deprecated API endpoints
```

**Test method**: "If applied, this commit will [subject]" should sound natural

### 6. Wrap the body at 72 characters

- Git does not auto-wrap text
- Manually wrap at 72 characters

### 7. Use the body to explain what and why vs. how

- Explain the reason and background for the change
- Specify what was wrong with the previous state
- The code itself explains how, so focus on context

## Commit Message Template

```
Subject: Summarize changes in 50 characters or less

Body: Detailed explanation if needed (wrap at 72 characters)
- Why is this change necessary?
- What problem does it solve?
- Any side effects or caveats?

- Bullet points are fine
- Use hyphens or asterisks

Issue tracker references:
Resolves: #123
See also: #456, #789
```

## Examples

### Good Example

```
Add user session timeout feature

Implement automatic logout after 30 minutes of inactivity
to enhance security. Users will receive a warning 5 minutes
before timeout with option to extend session.

- Add session monitoring service
- Implement warning modal component
- Update authentication middleware

Resolves: #234
```

### Bad Example

```
fixed stuff

changed some files and updated things
```

## Additional Guidelines

### Atomic Commits

- Include only one logical change
- Each commit should be independently buildable/testable

### Commit Frequency

- Commit often in small units
- Commit at meaningful progress points
- Squash WIP (Work In Progress) commits later

### Branch Strategy

- `main/master`: completed features only
- `feature/*`: detailed work units

## Tools and Configuration

### Git Configuration

```bash
# Set commit message template
git config --global commit.template ~/.gitmessage

# Set default editor (e.g., VS Code)
git config --global core.editor "code --wait"
```

### Commit Message Template File (~/.gitmessage)

```
# Subject (50 characters or less)

# Body (wrap at 72 characters)
# - Why is this change necessary?
# - What problem does it solve?
# - Any side effects or caveats?

# Issue references
# Resolves: #
# See also: #
```

## References

- [How to Write a Git Commit Message](https://cbea.ms/git-commit/)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Angular Commit Message Guidelines](https://github.com/angular/angular/blob/master/CONTRIBUTING.md#commit)
