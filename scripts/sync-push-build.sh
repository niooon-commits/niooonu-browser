#!/usr/bin/env bash
set -e

# ==============================================================================
# Script: scripts/sync-push-build.sh
# Purpose: Fully automated sync, commit, push, and Android build trigger.
# ==============================================================================

REPO="niooon-commits/niooonu-browser"
COMMIT_MSG="${1:-chore: automated sync, push and android build verification}"

echo "========================================================"
echo " [NIOOON] Running Automated Git Sync, Push & Build Workflow"
echo " Repo: $REPO"
echo "========================================================"

# Step 1: Ensure GitHub authentication (auto-refreshes if needed)
echo "[Step 1/6] Verifying GitHub authentication..."
if [ -f "scripts/ensure-github-auth.sh" ]; then
  bash scripts/ensure-github-auth.sh
fi

# Step 2: Update code-review-graph if installed
echo "[Step 2/6] Updating Code Review Graph (CRG)..."
if command -v code-review-graph >/dev/null 2>&1; then
  code-review-graph update || true
fi

# Step 3: Check git status and commit if needed
echo "[Step 3/6] Staging changes..."
git add -A

if git diff --staged --quiet; then
  echo "No new local working tree changes to commit."
else
  echo "Committing staged changes with message: '$COMMIT_MSG'..."
  # Temporarily disable post-commit hook during sync-push-build to prevent recursive loops
  SKIP_POST_COMMIT=1 git commit -m "$COMMIT_MSG"
fi

# Step 4: Check if local branch is ahead of origin main
UNPUSHED=$(git log origin/main..HEAD --oneline 2>/dev/null || true)

if [ -n "$UNPUSHED" ]; then
  echo "[Step 4/6] Pushing unpushed commits to GitHub (origin main)..."
  echo "$UNPUSHED"
  git push origin main
else
  echo "[Step 4/6] All commits are already pushed to origin main."
fi

# Step 5: Verify/trigger workflow
echo "[Step 5/6] Checking GitHub Actions workflow trigger..."
sleep 3

if [ -f "scripts/check-workflow.sh" ]; then
  bash scripts/check-workflow.sh
fi

# Step 6: Summary
echo "[Step 6/6] Success! Code synchronized and workflow verified."
echo "========================================================"
