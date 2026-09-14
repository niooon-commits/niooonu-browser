#!/usr/bin/env bash
set -e

# ==============================================================================
# Script: scripts/check-unpushed.sh
# Purpose: Automatic monitor & reminder for uncommitted or unpushed changes.
#          If changes or unpushed commits exist, automatically syncs and pushes.
# ==============================================================================

# Ensure auth is configured
if [ -f "scripts/ensure-github-auth.sh" ]; then
  bash scripts/ensure-github-auth.sh
fi

CHANGES=$(git status --porcelain 2>/dev/null || true)
UNPUSHED=$(git log origin/main..HEAD --oneline 2>/dev/null || true)

if [ -n "$CHANGES" ] || [ -n "$UNPUSHED" ]; then
  echo ""
  echo "🚨 ========================================================"
  echo "🚨 [AUTOMATION REMINDER] Uncommitted or Unpushed Changes Found!"
  if [ -n "$CHANGES" ]; then
    echo "🚨 Staged/Unstaged files:"
    echo "$CHANGES" | head -n 10
  fi
  if [ -n "$UNPUSHED" ]; then
    echo "🚨 Unpushed commits awaiting GitHub push:"
    echo "$UNPUSHED"
  fi
  echo "🚨 --------------------------------------------------------"
  echo "🚨 Automatically executing sync & push to GitHub origin main..."
  echo "🚨 ========================================================"
  echo ""

  bash scripts/sync-push-build.sh "chore: automated sync of uncommitted/unpushed changes"
else
  echo "✅ [Auto-Sync Reminder] Repo is clean. All changes are committed and pushed to GitHub main."
fi
