#!/usr/bin/env bash
set -euo pipefail
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$DIR"
if command -v gradle >/dev/null 2>&1; then
    exec gradle "$@"
else
    echo "Gradle is not installed or not on PATH. Please install Gradle or use the Android Studio wrapper."
    exit 1
fi
