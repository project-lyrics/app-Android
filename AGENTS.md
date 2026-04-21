# PROJECT KNOWLEDGE BASE

## OVERVIEW
- Feelin Android app. Single-module `:app`, Kotlin + Compose + Hilt + Retrofit + DataStore.
- Runtime entry: `FeelinApplication` -> `MainActivity` -> `navigation/FeelinNavHost.kt`.

## STRUCTURE
```text
./
├── app/                                # only Android module
│   ├── src/main/java/com/lyrics/feelin/
│   │   ├── core/                       # shared data, domain, design system
│   │   ├── navigation/                 # app graph + destinations
│   │   ├── presentation/view/          # feature screens
│   │   ├── presentation/designsystem/  # theme + CompositionLocal
│   │   └── util/                       # small extensions
│   └── detekt.yml                      # enforced quality rules
├── gradle/libs.versions.toml           # dependency source of truth
├── .github/workflows/android-ci.yml    # detekt -> assembleDebug
└── specdocument/                       # feature specs, not runtime code
```

## AGENTS HIERARCHY
- `./AGENTS.md` - repo-wide rules, build, quality gates, shared architecture.
- `app/src/main/java/com/lyrics/feelin/core/AGENTS.md` - shared layer rules.
- `app/src/main/java/com/lyrics/feelin/presentation/view/AGENTS.md` - feature-screen rules.
- `app/src/main/java/com/lyrics/feelin/presentation/view/onboarding/AGENTS.md` - multi-step onboarding flow.
- `app/src/main/java/com/lyrics/feelin/presentation/view/mypage/AGENTS.md` - profile/settings area.

## WHERE TO LOOK
| Task | Location | Notes |
|---|---|---|
| App startup | `app/src/main/java/com/lyrics/feelin/FeelinApplication.kt` | Kakao SDK init, Hilt app |
| Root UI entry | `app/src/main/java/com/lyrics/feelin/MainActivity.kt` | `FeelinTheme` + `FeelinNavHost()` |
| Navigation flow | `app/src/main/java/com/lyrics/feelin/navigation/FeelinNavHost.kt` | onboarding/main graph split |
| Shared data/auth | `app/src/main/java/com/lyrics/feelin/core/data/` | repository, datasource, interceptor, manager |
| Shared models | `app/src/main/java/com/lyrics/feelin/core/domain/model/` | auth/profile/signup models |
| Reusable UI | `app/src/main/java/com/lyrics/feelin/core/designsystem/` | common Compose components/icons |
| Theme tokens | `app/src/main/java/com/lyrics/feelin/presentation/designsystem/theme/` | `LocalFeelinColors`, theme wiring |
| Feature screens | `app/src/main/java/com/lyrics/feelin/presentation/view/` | login, onboarding, mypage, note, community |
| Build/deps | `app/build.gradle.kts`, `gradle/libs.versions.toml` | versions, plugins, Kakao key rules |
| CI | `.github/workflows/android-ci.yml` | detekt must pass before build |

## CONVENTIONS
- All responses and local docs: Korean.
- Commit messages: English only, `prefix: Subject` format. Prefixes: `init`, `feat`, `docs`, `build`, `design`, `fix`, `chore`, `refactor`, `ci`, `test`.
- Commit subject: imperative, no trailing period, preferably <= 50 chars.
- After Kotlin/code edits, always run `./gradlew detekt` before finishing work.
- If changes affect build config, app startup, or navigation flow, also run `./gradlew :app:assembleDebug`.
- If you are running under WSL, execute Gradle verification via `cmd.exe /c gradlew.bat ...` instead of invoking `./gradlew` directly.
- Compose:
  - include `modifier: Modifier = Modifier` on composables that render UI.
  - keep `modifier` as first optional parameter.
  - keep lambda parameters last.
  - use project theme tokens via `LocalFeelinColors`.
- Lint/formatting source of truth: `app/detekt.yml`.
- CI shape: `detekt` first, then `:app:assembleDebug`.

## ANTI-PATTERNS
- Do not run `detektFormat`; this repo uses `./gradlew detekt`.
- Do not remove `@OptIn(...)` or related experimental imports even if they look unused.
- Do not replace theme access with ad-hoc colors when `LocalFeelinColors` already covers it.
- Do not bypass repo conventions with Korean commit messages or vague subjects like `update code`.
- Do not treat style-only feedback as the main review target; CodeRabbit already delegates style to detekt.

## UNIQUE STYLES
- `settings.gradle.kts` uses `RepositoriesMode.FAIL_ON_PROJECT_REPOS` and adds Kakao Maven repo.
- `app/build.gradle.kts` fails fast if Kakao native key is missing locally and in env.
- Compose detekt rules are intentionally tuned: `@Composable`/`@Preview` get complexity exceptions; `modifier` is an allowed unused parameter.
- `presentation/designsystem/theme/Theme.kt` owns CompositionLocal provisioning. Keep new global UI tokens there.

## COMMANDS
```bash
./gradlew detekt
./gradlew :app:assembleDebug
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
```

## NOTES
- Only real tests in repo are generated examples under `app/src/test` and `app/src/androidTest`; most app areas are still lightly tested.
- `coderabbit.yaml` asks reviews to focus on correctness, lifecycle, coroutine, Compose state, and performance risks rather than formatting.
- `CLAUDE.md` intentionally points back to this file.
