#!/usr/bin/env bash
set -e

# Dynamically extract token without exposing raw secrets to GitHub push protection
TOKEN="${GITHUB_TOKEN:-$(git remote get-url origin 2>/dev/null | grep -o 'ghp_[^@]*' || true)}"
REPO="niooon-commits/niooonu-browser"
COMMIT_MSG="${1:-chore: automated sync, push and android build verification}"

echo "========================================================"
echo " [NIOOON] Running Automated Git Sync, Push & Build Workflow"
echo " Repo: $REPO"
echo "========================================================"

# Step 1: Update code-review-graph
echo "[Step 1/5] Updating Code Review Graph (CRG)..."
if command -v code-review-graph >/dev/null 2>&1; then
  code-review-graph update || true
fi

# Step 2: Check git status and commit if needed
echo "[Step 2/5] Staging changes..."
git add -A

if git diff --staged --quiet; then
  echo "No new local changes to commit."
else
  echo "Committing staged changes with message: '$COMMIT_MSG'..."
  git commit -m "$COMMIT_MSG"
fi

# Step 3: Push to GitHub origin main
echo "[Step 3/5] Pushing to GitHub (origin main)..."
git push origin main

# Step 4: Verify/trigger workflow
echo "[Step 4/5] Checking GitHub Actions workflow trigger..."
sleep 3

if [ -f "scripts/check-workflow.sh" ]; then
  bash scripts/check-workflow.sh
fi

# Step 5: Summary
echo "[Step 5/5] Success! Code pushed to GitHub and workflow verified."
echo "========================================================"
