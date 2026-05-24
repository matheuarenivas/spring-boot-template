#!/usr/bin/env bash
#
# Clone this Spring Boot template into a new project.
#
# Usage:
#   ./clone-template.sh <new-project-name> [java-package] [target-parent-dir]
#
# Examples:
#   ./clone-template.sh crime-data-api
#       -> ../crime-data-api with package com.crimedata.api
#
#   ./clone-template.sh crime-data-api com.acme.crime
#       -> ../crime-data-api with package com.acme.crime
#
#   ./clone-template.sh crime-data-api com.acme.crime ~/code
#       -> ~/code/crime-data-api with package com.acme.crime
#
# After it finishes, cd into the new dir and optionally run:
#   gh repo create <name> --private --source=. --push

set -euo pipefail

if [[ $# -lt 1 ]]; then
  echo "Usage: $0 <new-project-name> [java-package] [target-parent-dir]" >&2
  exit 1
fi

NEW_NAME="$1"
TEMPLATE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PARENT_DIR="${3:-$(dirname "$TEMPLATE_DIR")}"
TARGET_DIR="$PARENT_DIR/$NEW_NAME"

# Derive default package: com.<name-with-dashes-stripped-minus-api-suffix>.api
# e.g. crime-data-api -> com.crimedata.api
derive_package() {
  local name="$1"
  local base="${name%-api}"
  base="$(echo "$base" | tr -d '_-' | tr '[:upper:]' '[:lower:]')"
  echo "com.${base}.api"
}

NEW_PACKAGE="${2:-$(derive_package "$NEW_NAME")}"
NEW_PACKAGE_PATH="$(echo "$NEW_PACKAGE" | tr '.' '/')"   # com.crimedata.api -> com/crimedata/api
NEW_GROUP_ID="${NEW_PACKAGE%.*}"                   # com.crimedata.api -> com.crimedata
NEW_ARTIFACT_ID="${NEW_PACKAGE##*.}"               # com.crimedata.api -> api
NEW_DB_NAME="$(echo "$NEW_NAME" | tr -d '_-' | tr '[:upper:]' '[:lower:]')db"

OLD_GROUP_ID="com.template"
OLD_PACKAGE="com.template.api"
OLD_PACKAGE_PATH="com/template/api"
OLD_APP_NAME="api-template"
OLD_DOCKER_TAG="api-template"
OLD_DB_NAME="templatedb"

echo "Template:     $TEMPLATE_DIR"
echo "Target:       $TARGET_DIR"
echo "Java package: $OLD_PACKAGE -> $NEW_PACKAGE"
echo "Maven group:  $OLD_GROUP_ID  -> $NEW_GROUP_ID"
echo "App name:     $OLD_APP_NAME -> $NEW_NAME"
echo "DB name:      $OLD_DB_NAME  -> $NEW_DB_NAME"
echo

if [[ -e "$TARGET_DIR" ]]; then
  echo "Error: $TARGET_DIR already exists." >&2
  exit 1
fi

# 1. Copy template (excluding .git and build artifacts)
echo "Copying files..."
mkdir -p "$TARGET_DIR"
# Use rsync if available for clean excludes; fall back to cp + rm
if command -v rsync >/dev/null 2>&1; then
  rsync -a \
    --exclude='.git/' \
    --exclude='target/' \
    --exclude='.idea/' \
    --exclude='*.iml' \
    --exclude='.DS_Store' \
    --exclude='clone-template.sh' \
    "$TEMPLATE_DIR"/ "$TARGET_DIR"/
else
  cp -R "$TEMPLATE_DIR"/. "$TARGET_DIR"/
  rm -rf "$TARGET_DIR/.git" "$TARGET_DIR/target" "$TARGET_DIR/.idea" "$TARGET_DIR/clone-template.sh"
fi

cd "$TARGET_DIR"

# 2. Move Java package directories (main + test)
echo "Renaming package directories..."
for src_root in src/main/java src/test/java; do
  old_path="$src_root/$OLD_PACKAGE_PATH"
  new_path="$src_root/$NEW_PACKAGE_PATH"
  if [[ -d "$old_path" ]]; then
    mkdir -p "$(dirname "$new_path")"
    mv "$old_path" "$new_path"
    # Clean up empty parent dirs from the old path (com/template)
    old_parent="$src_root/${OLD_PACKAGE_PATH%/*}"
    while [[ "$old_parent" != "$src_root" && -d "$old_parent" ]]; do
      rmdir "$old_parent" 2>/dev/null || break
      old_parent="${old_parent%/*}"
    done
  fi
done

# 3. Rewrite text references inside files
echo "Rewriting file contents..."
# Pick files to rewrite: tracked-style file types only, skip binaries / build output / the script itself
FILES=$(find . \
  -type d \( -name target -o -name .git -o -name .idea -o -name node_modules \) -prune -o \
  -type f \( \
       -name "*.java" \
    -o -name "*.kt" \
    -o -name "*.xml" \
    -o -name "*.properties" \
    -o -name "*.yml" \
    -o -name "*.yaml" \
    -o -name "*.sql" \
    -o -name "Dockerfile" \
    -o -name "Makefile" \
    -o -name "*.md" \
    -o -name "*.sh" \
    -o -name ".env*" \
  \) \
  ! -name "clone-template.sh" \
  -print)

# Use a portable in-place sed (works on macOS BSD sed and GNU sed)
sed_inplace() {
  if sed --version >/dev/null 2>&1; then
    sed -i -e "$1" "$2"
  else
    sed -i '' -e "$1" "$2"
  fi
}

while IFS= read -r f; do
  [[ -z "$f" ]] && continue
  sed_inplace "s|${OLD_PACKAGE}|${NEW_PACKAGE}|g" "$f"
  sed_inplace "s|${OLD_GROUP_ID}|${NEW_GROUP_ID}|g" "$f"
  sed_inplace "s|${OLD_APP_NAME}|${NEW_NAME}|g" "$f"
  sed_inplace "s|${OLD_DB_NAME}|${NEW_DB_NAME}|g" "$f"
done <<< "$FILES"

# Update pom.xml <name> and <description> (sed above already swapped api-template -> new name).
# Also update the <description> tag to a generic one if it still says "Template".
if [[ -f pom.xml ]]; then
  sed_inplace "s|<description>Spring Boot REST API Template</description>|<description>${NEW_NAME}</description>|" pom.xml
fi

# 4. Fresh git history
echo "Initializing git..."
git init -q
git add .
git -c commit.gpgsign=false commit -q -m "Initial commit from spring-boot-template"

echo
echo "Done. New project at: $TARGET_DIR"
echo
echo "Next steps:"
echo "  cd $TARGET_DIR"
echo "  gh repo create $NEW_NAME --private --source=. --push"
echo "  ./mvnw -q -DskipTests package    # or: mvn -q -DskipTests package"
