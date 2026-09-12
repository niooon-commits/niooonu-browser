#!/usr/bin/env bash
set -e

# ==============================================================================
# NIOOON Zero-Token Workflow Waiter
# Runs completely in bash; prevents multiple AI agent round-trips and token burn.
# ==============================================================================

TOKEN="${GITHUB_TOKEN:-$(git remote get-url origin 2>/dev/null | grep -o 'ghp_[^@]*' || true)}"
REPO="niooon-commits/niooonu-browser"
TIMEOUT_SECS="${1:-600}" # Default 10 minutes
INTERVAL=10

if [ -z "$TOKEN" ]; then
  AUTH_HEADER=()
else
  AUTH_HEADER=(-H "Authorization: Bearer $TOKEN")
fi

echo "========================================================"
echo " [NIOOON] Zero-Token Background Workflow Watcher"
echo " Repo     : $REPO"
echo " Timeout  : ${TIMEOUT_SECS}s (polling every ${INTERVAL}s in bash)"
echo " Note     : No LLM/AI tokens are consumed during this wait"
echo "========================================================"

START_TIME=$(date +%s)

# Fetch latest run ID
RUN_DATA=$(curl -s "${AUTH_HEADER[@]}" "https://api.github.com/repos/$REPO/actions/runs?per_page=1")
RUN_ID=$(echo "$RUN_DATA" | grep -o '"id": [0-9]*' | head -n 1 | awk '{print $2}')
RUN_NAME=$(echo "$RUN_DATA" | grep -o '"name": "[^"]*"' | head -n 1 | cut -d'"' -f4)
RUN_URL=$(echo "$RUN_DATA" | grep -o '"html_url": "[^"]*"' | head -n 1 | cut -d'"' -f4)

if [ -z "$RUN_ID" ]; then
  echo "Error: No workflow runs found."
  exit 1
fi

echo "Watching Run ID : $RUN_ID ($RUN_NAME)"
echo "Workflow URL    : $RUN_URL"
echo "--------------------------------------------------------"

while true; do
  NOW=$(date +%s)
  ELAPSED=$((NOW - START_TIME))

  if [ "$ELAPSED" -ge "$TIMEOUT_SECS" ]; then
    echo "Timeout reached (${TIMEOUT_SECS}s). Workflow still running."
    echo "Check manually at: $RUN_URL"
    exit 2
  fi

  STATUS_DATA=$(curl -s "${AUTH_HEADER[@]}" "https://api.github.com/repos/$REPO/actions/runs/$RUN_ID")
  STATUS=$(echo "$STATUS_DATA" | grep -o '"status": "[^"]*"' | head -n 1 | cut -d'"' -f4)
  CONCLUSION=$(echo "$STATUS_DATA" | grep -o '"conclusion": [^,]*' | head -n 1 | awk '{print $2}' | tr -d '"')

  # Fetch active job step name if still in progress
  CURRENT_STEP=""
  if [ "$STATUS" = "in_progress" ]; then
    JOBS_DATA=$(curl -s "${AUTH_HEADER[@]}" "https://api.github.com/repos/$REPO/actions/runs/$RUN_ID/jobs")
    CURRENT_STEP=$(echo "$JOBS_DATA" | grep -B 1 '"status": "in_progress"' | grep '"name":' | head -n 1 | cut -d'"' -f4 || true)
  fi

  MINS=$((ELAPSED / 60))
  SECS=$((ELAPSED % 60))
  TIME_STR=$(printf "%02d:%02d" "$MINS" "$SECS")

  if [ -n "$CURRENT_STEP" ]; then
    echo "[$TIME_STR] Status: $STATUS | Current Step: $CURRENT_STEP"
  else
    echo "[$TIME_STR] Status: $STATUS | Conclusion: ${CONCLUSION:-pending}"
  fi

  if [ "$STATUS" = "completed" ]; then
    echo "========================================================"
    if [ "$CONCLUSION" = "success" ]; then
      echo "🎉 WORKFLOW FINISHED SUCCESSFULLY!"
      
      # Check release APK
      RELEASE_DATA=$(curl -s "${AUTH_HEADER[@]}" "https://api.github.com/repos/$REPO/releases/tags/v1.0.0")
      APK_URL=$(echo "$RELEASE_DATA" | grep -o '"browser_download_url": "[^"]*niooonu-browser-release.apk"' | head -n 1 | cut -d'"' -f4)
      if [ -n "$APK_URL" ]; then
        echo "APK Download URL: $APK_URL"
      fi
      exit 0
    else
      echo "❌ WORKFLOW FAILED with conclusion: $CONCLUSION"
      exit 1
    fi
  fi

  sleep "$INTERVAL"
done
