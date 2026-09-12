#!/usr/bin/env bash
set -e

# Dynamically extract token without exposing raw secrets to GitHub push protection
TOKEN="${GITHUB_TOKEN:-$(git remote get-url origin 2>/dev/null | grep -o 'ghp_[^@]*' || true)}"
REPO="niooon-commits/niooonu-browser"

echo "========================================================"
echo " [NIOOON] Checking GitHub Actions Workflow Status"
echo " Repo: $REPO"
echo "========================================================"

if [ -z "$TOKEN" ]; then
  echo "Warning: GitHub token could not be derived. Public rate-limits apply."
  AUTH_HEADER=()
else
  AUTH_HEADER=(-H "Authorization: Bearer $TOKEN")
fi

RUN_DATA=$(curl -s "${AUTH_HEADER[@]}" \
  "https://api.github.com/repos/$REPO/actions/runs?per_page=1")

RUN_ID=$(echo "$RUN_DATA" | grep -o '"id": [0-9]*' | head -n 1 | awk '{print $2}')
RUN_NAME=$(echo "$RUN_DATA" | grep -o '"name": "[^"]*"' | head -n 1 | cut -d'"' -f4)
RUN_STATUS=$(echo "$RUN_DATA" | grep -o '"status": "[^"]*"' | head -n 1 | cut -d'"' -f4)
RUN_CONCLUSION=$(echo "$RUN_DATA" | grep -o '"conclusion": [^,]*' | head -n 1 | awk '{print $2}' | tr -d '"')
RUN_HTML=$(echo "$RUN_DATA" | grep -o '"html_url": "[^"]*"' | head -n 1 | cut -d'"' -f4)

echo "Workflow Name   : $RUN_NAME"
echo "Latest Run ID   : $RUN_ID"
echo "Status          : $RUN_STATUS"
echo "Conclusion      : ${RUN_CONCLUSION:-running/pending}"
echo "URL             : $RUN_HTML"
echo "========================================================"

RELEASE_DATA=$(curl -s "${AUTH_HEADER[@]}" \
  "https://api.github.com/repos/$REPO/releases/tags/v1.0.0")

APK_URL=$(echo "$RELEASE_DATA" | grep -o '"browser_download_url": "[^"]*niooonu-browser-release.apk"' | head -n 1 | cut -d'"' -f4)
UPDATED_AT=$(echo "$RELEASE_DATA" | grep -o '"updated_at": "[^"]*"' | head -n 1 | cut -d'"' -f4)

if [ -n "$APK_URL" ]; then
  echo "Release Tag     : v1.0.0"
  echo "Release Updated : $UPDATED_AT"
  echo "APK Download URL: $APK_URL"
else
  echo "Release APK     : Not yet published or compiling..."
fi
echo "========================================================"
