# ONBOARDING FEATURE GUIDE

## OVERVIEW
- Multi-step user setup flow. Route order and shared parent ViewModel matter more here than in other features.

## STRUCTURE
```text
onboarding/
├── OnboardingViewModel.kt
├── favoriteartist/
├── genderage/
├── profile/
├── terms/
└── welcome/
```

## WHERE TO LOOK
| Task | Location | Notes |
|---|---|---|
| Shared onboarding state | `OnboardingViewModel.kt` | saved across multiple steps |
| Terms step | `terms/OnboardingTermsScreen.kt` | start gate |
| Gender/age step | `genderage/OnboardingGenderAgeScreen.kt` | saves first profile inputs |
| Profile step | `profile/ProfileScreen.kt` | nickname/profile selection |
| Welcome step | `welcome/WelcomeScreen.kt` | completes flow |
| Favorite artist subfeature | `favoriteartist/` | local state/viewmodel exists, but not in active nav flow |

## CONVENTIONS
- Navigation assumes one parent graph-scoped `OnboardingViewModel`; preserve that shared-state model when adding steps.
- Step screens should emit input and navigation intent, not own long-term persistence logic.
- Save user data through `UserRepository` via the parent ViewModel, not per-screen ad-hoc storage.
- Keep route order explicit in `FeelinNavHost.kt` when inserting or reordering onboarding steps.
- Current active flow is `login -> terms -> genderage -> profile -> welcome`.

## ANTI-PATTERNS
- Do not create separate graph-level ViewModels for each simple step.
- Do not bypass the parent onboarding graph back stack entry when resolving `hiltViewModel(parentEntry)`.
- Do not hide step transitions inside deeply nested child composables.
- Do not document parked subfeatures as active onboarding steps before they are wired in `FeelinNavHost.kt` and `FeelinDestination.kt`.

## NOTES
- This is the most structured feature flow in the repo.
- `favoriteartist` behaves like a parked subfeature with its own local state objects, but it is currently disconnected from the main onboarding route chain.
