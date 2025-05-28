VERSION=$(gh pr view $TARGET_PR -c --json comments -q "[.comments[] | select(.author.login == \"gu-scala-library-release\")][-1].body" | grep "\-PREVIEW")

TAG_URL=$(gh api \
  -H "Accept: application/vnd.github+json" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
    /repos/guardian/redirect-resolver/git/ref/tags/v$VERSION \
    -q ".object.url" | sed "s/https:\/\/api.github.com\///")

ARTIFACT_PATH=$(gh api \
  -H "Accept: application/vnd.github+json" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
    $TAG_URL \
    -q ".message" | grep ".pom$" | head -n 1 | cut -d" " -f3)

POM=$(curl https://repo1.maven.org/maven2/$(echo $ARTIFACT_PATH | cut -d" " -f1))

GROUP_ID=$(echo $POM | xq -x /project/groupId)
ARTIFACT_ID=$(echo $POM | xq -x /project/artifactId | rev | cut -d"_" -f2- | rev)
TARGET_ARTIFACT="$GROUP_ID:$ARTIFACT_ID:$VERSION"

mkdir common-config
cat << EOF > common-config/scala-steward.conf
updates.allow = [{ groupId = "$GROUP_ID", artifactId = "$ARTIFACT_ID", version = "$VERSION" }]
commits.message = "[TEST - please ignore]: Update \${artifactName} from \${currentVersion} to \${nextVersion}"
pullRequests.draft = true
pullRequests.grouping = [{
  name = "$TARGET_ARTIFACT",
  title = "[TEST - please ignore]: Update $TARGET_ARTIFACT",
  filter = [{"group" = "*"}]
}]
updates.allowPreReleases = [{ groupId = "$GROUP_ID" }]
EOF
cat common-config/scala-steward.conf