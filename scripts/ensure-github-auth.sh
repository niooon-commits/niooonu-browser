#!/usr/bin/env bash
set -e

# ==============================================================================
# Script: scripts/ensure-github-auth.sh
# Purpose: Automatically refreshes and ensures valid GitHub credentials using
#          the NIOOON platform vault key so pushes never fail or ask the user.
# ==============================================================================

REPO="niooon-commits/niooonu-browser"

CURRENT_TOKEN=$(git remote get-url origin 2>/dev/null | grep -o 'ghp_[^@]*' || true)

# If no token in URL, check cached token
if [ -z "$CURRENT_TOKEN" ] && [ -f ".git/cached_github_token" ]; then
  CURRENT_TOKEN=$(cat .git/cached_github_token 2>/dev/null | tr -d '\r\n ' || true)
fi

# 1. Test if current token is valid with GitHub API
TOKEN_VALID=0
if [ -n "$CURRENT_TOKEN" ]; then
  HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $CURRENT_TOKEN" https://api.github.com/user || true)
  if [ "$HTTP_STATUS" = "200" ]; then
    TOKEN_VALID=1
    export GITHUB_TOKEN="$CURRENT_TOKEN"
  fi
fi

if [ "$TOKEN_VALID" -eq 1 ]; then
  exit 0
fi

echo "[NIOOON Auth] Current Git credentials missing or expired. Refreshing token via NIOOON Vault..."

# 2. Locate NIOOON Platform Key
PLATFORM_KEY="${NIOOON_PLATFORM_KEY:-}"

if [ -z "$PLATFORM_KEY" ] && [ -f ".env" ]; then
  PLATFORM_KEY=$(grep -E '^NIOOON_PLATFORM_KEY=' .env 2>/dev/null | head -n 1 | cut -d'=' -f2- | tr -d '"' | tr -d "'" | tr -d ' ' || true)
fi

if [ -z "$PLATFORM_KEY" ] && [ -f ".git/niooon_vault_key" ]; then
  PLATFORM_KEY=$(cat .git/niooon_vault_key 2>/dev/null | tr -d '\r\n ' || true)
fi

if [ -z "$PLATFORM_KEY" ] && [ -f "$HOME/.niooon_vault_key" ]; then
  PLATFORM_KEY=$(cat "$HOME/.niooon_vault_key" 2>/dev/null | tr -d '\r\n ' || true)
fi

if [ -z "$PLATFORM_KEY" ]; then
  echo "❌ Error: Could not find NIOOON_PLATFORM_KEY in .env, .git, or environment."
  exit 1
fi

# 3. Exchange platform key for fresh scoped credentials
RESPONSE=$(curl -s -X POST https://nioon.lovable.app/api/public/vault/tokens \
  -H "Authorization: Bearer $PLATFORM_KEY" \
  -H "Content-Type: application/json" \
  -d '{"providers":["github","vercel","supabase"]}')

NEW_TOKEN=$(echo "$RESPONSE" | grep -o '"github":"[^"]*"' | cut -d'"' -f4 || true)

if [ -z "$NEW_TOKEN" ] || [ "$NEW_TOKEN" = "null" ]; then
  echo "❌ Error: Failed to retrieve GitHub token from NIOOON Vault."
  echo "Vault response: $RESPONSE"
  exit 1
fi

# 4. Update git origin remote URL and cache
git remote set-url origin "https://niooon-commits:${NEW_TOKEN}@github.com/${REPO}.git"
echo "$NEW_TOKEN" > .git/cached_github_token 2>/dev/null || true
export GITHUB_TOKEN="$NEW_TOKEN"

echo "✅ [NIOOON Auth] GitHub token refreshed successfully! Git origin remote updated."
