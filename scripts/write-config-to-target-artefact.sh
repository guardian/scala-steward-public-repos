#! /bin/bash

VERSION=$(gh pr view $TARGET_PR -c --json comments -q "[.comments[] | select(.author.login == \"gu-scala-library-release\")][-1].body" | grep "\-PREVIEW")

if [[ -z "$VERSION" ]]; then
  echo "No version found for PR $TARGET_PR. Exiting."
  cat << EndOfFile >> $GITHUB_STEP_SUMMARY
# Run failed
Couldn't find a preview release for the specified PR: [$TARGET_PR]($TARGET_PR). Does it have one yet?

You can create a preview release by following [these instructions.](https://github.com/guardian/gha-scala-library-release-workflow/blob/main/docs/making-a-release.md)
EndOfFile
  exit 1
fi

echo "Found version: $VERSION"

set -e

PULL_REQUEST_SLUG=$(gh pr view "$TARGET_PR" --json url | jq -r ".url" | cut -d/ -f 4-)
REPO_ID=$(echo "$PULL_REQUEST_SLUG" | cut -d/ -f -2)

TAG_URL=$(gh api \
  -H "Accept: application/vnd.github+json" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
    "/repos/$REPO_ID/git/ref/tags/v$VERSION" \
    -q ".object.url" | sed "s/https:\/\/api.github.com\///")

echo "Tag url: $TAG_URL"

ARTIFACT_PATH=$(gh api \
  -H "Accept: application/vnd.github+json" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
    $TAG_URL \
    -q ".message" | grep ".pom$" | head -n 1 | cut -d" " -f3)

echo "Artifact path: $ARTIFACT_PATH"

POM=$(curl https://repo1.maven.org/maven2/$(echo $ARTIFACT_PATH | cut -d" " -f1))

echo "POM ... is large"

GROUP_ID=$(echo $POM | xq -x /project/groupId)
ARTIFACT_ID=$(echo $POM | xq -x /project/artifactId | rev | cut -d"_" -f2- | rev)


echo "Grouping name: $PULL_REQUEST_SLUG"

cat << EOF > targeted-scala-steward.conf
updates.allow = [{ groupId = "$GROUP_ID", artifactId = "$ARTIFACT_ID", version = "$VERSION" }]
commits.message = "Update \${artifactName} from \${currentVersion} to \${nextVersion}"
pullRequests.draft = true
pullRequests.grouping = [{
  name = "$PULL_REQUEST_SLUG",
  title = "Update \`$GROUP_ID:$ARTIFACT_ID\` to \`$VERSION\`",
  filter = [{"group" = "*"}]
}]
pullRequests.customLabels = ["targeted-update"]
updates.allowPreReleases = [{ groupId = "$GROUP_ID" }]
EOF
cat targeted-scala-steward.conf
