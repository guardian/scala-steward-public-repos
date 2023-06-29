Running locally for debug purposes
==================================

If the Scala Steward run is behaving incorrectly in some way, it may be worth
running the Scala Steward code locally with [Java Remote debugging](https://www.jetbrains.com/help/idea/debugging-code.html)
enabled.

These instructions are adapted from Scala Steward's [docs/running.md](https://github.com/scala-steward-org/scala-steward/blob/main/docs/running.md),
doing our best to duplicate the [parameters](https://github.com/scala-steward-org/scala-steward/blob/main/docs/help.md) which are [setup by the Scala Steward GitHub Action](https://github.com/scala-steward-org/scala-steward-action/blob/31bf30f6a29b33723e5b6d89a9a8f4cf23b05924/src/action/main.ts#L49-L71)
when we run it in [our GitHub workflow](https://github.com/guardian/scala-steward-public-repos/blob/2c3ae97fe168c0887036fac81752b7d406653823/.github/workflows/public-repos-scala-steward.yml).

1. Clone Scala Steward's codebase: [scala-steward-org/scala-steward](https://github.com/scala-steward-org/scala-steward).
   We recommend [these alterations](https://github.com/scala-steward-org/scala-steward/compare/main...rtyley:scala-steward:temporary-tweaks-for-debugging) to the code:
   * Uncomment the [`run / javaOptions`](https://github.com/scala-steward-org/scala-steward/blob/609104a04cd536418ebcda553e7f48177a5264ae/build.sbt#L213)
     build.sbt line to enable debugging.
   * Disable actual creation/update of PRs by removing [the call to `NurtureAlg.nurture()`](https://github.com/scala-steward-org/scala-steward/blob/42da4fad649dfc5e92147b3e8165a1bf860b2629/modules/core/src/main/scala/org/scalasteward/core/application/StewardAlg.scala#L82)
   * Optionally, filter the repos list to focus on a single repo that you find interesting (eg [`amigo`](https://github.com/guardian/amigo))
2. Clone _this_ repo, [guardian/scala-steward-public-repos](https://github.com/guardian/scala-steward-public-repos),
   for our [scala-steward.conf](https://github.com/guardian/scala-steward-public-repos/blob/main/scala-steward.conf)
   and [REPOSITORIES.md](https://github.com/guardian/scala-steward-public-repos/blob/main/REPOSITORIES.md) files.
3. Create a workspace folder for Scala Steward to work in, eg. `~/code/scala-steward-workspace`
4. Create a blank `askpass.sh` file (normally this would need to
   [contain a valid user GitHub token](https://github.com/scala-steward-org/scala-steward-action/blob/190e7f36cd057dfb5d1b6fde688e6399ef249560/src/modules/workspace.ts#L114),
   but it appears that's only necessary if the code is actually going to push commits to GitHub).
   ```shell
   touch ~/code/scala-steward-workspace/askpass.sh
   chmod 755 ~/code/scala-steward-workspace/askpass.sh
   ```
5. Download a [temporary GitHub App private key](https://docs.github.com/en/apps/creating-github-apps/authenticating-with-a-github-app/managing-private-keys-for-github-apps)
   (a `.private-key.pem` file) for the GitHub App (only [GitHub App Managers](https://docs.github.com/en/apps/maintaining-github-apps/about-github-app-managers)
   for [our gu-scala-steward-public-repos app](https://github.com/organizations/guardian/settings/permissions/integrations/gu-scala-steward-public-repos/managers)
   can generate/download new keys for the app). Remember to [**DELETE THIS KEY**](https://docs.github.com/en/apps/creating-github-apps/authenticating-with-a-github-app/managing-private-keys-for-github-apps#deleting-private-keys) when you've finished your debugging work.
6. Enter the sbt console in the Scala Steward project, and execute this sbt command, which uses all
   the different file resources you just set up (note your file paths won't start `/Users/Roberto_Tyley`!):
   > core/run --do-not-fork --workspace "/Users/Roberto_Tyley/code/scala-steward-workspace" --repos-file "/Users/Roberto_Tyley/code/scala-steward-public-repos/REPOSITORIES.md" --repo-config "/Users/Roberto_Tyley/code/scala-steward-public-repos/scala-steward.conf" --forge-login "gu-scala-steward-public-repos" --git-author-email "108136057+gu-scala-steward-public-repos[bot]@users.noreply.github.com" --github-app-id 214238 --github-app-key-file "/Users/Roberto_Tyley/Downloads/gu-scala-steward-public-repos.2023-06-14.private-key.pem" --git-ask-pass "/Users/Roberto_Tyley/code/scala-steward-workspace/askpass.sh"
7. Attach your debugger - for IntelliJ, note that when running in the
   [IntelliJ sbt shell](https://www.jetbrains.com/help/idea/sbt-support.html#sbt_shell),
   a discrete but useful `Attach debugger` affordance will be shown in the console:

https://github.com/guardian/scala-steward-public-repos/assets/52038/0bf51171-6023-4f5c-92e8-0416eee19ea8

