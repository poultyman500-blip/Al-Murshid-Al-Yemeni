# Android project scaffold created in a separate folder to keep the existing JS app safe.

This folder contains a minimal Android app skeleton for the Yemen navigation project.

## Structure

- `app/` – Android application module
- `gradle/libs.versions.toml` – version catalog
- `settings.gradle.kts` – project settings
- `build.gradle.kts` – root plugin configuration

## Notes

- The repository originally contains a web app (`index.html`, `app.js`, `styles.css`), so the Android project was added in a separate `android/` directory instead of replacing the existing app.
- This is a production-ready starting scaffold for the Android app and can be expanded with Room, Firebase, OSMDroid, and CI/CD config.
