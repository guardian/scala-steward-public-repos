# Scala Steward setup for PUBLIC Guardian repos

[![Public Repos Scala Steward](https://github.com/guardian/scala-steward-public-repos/actions/workflows/public-repos-scala-steward.yml/badge.svg)](https://github.com/guardian/scala-steward-public-repos/actions/workflows/public-repos-scala-steward.yml)

[Scala Steward](https://github.com/scala-steward-org/scala-steward) is a bot that helps you keep your Scala projects up-to-date! ✨

_Looking to run Scala Steward on a **private** Guardian repo? Go to
[guardian/scala-steward-private-repos](https://github.com/guardian/scala-steward-private-repos)
instead! This repo (`scala-steward-public-repos`) is public, so the GitHub Action minutes are
[free](https://docs.github.com/en/billing/managing-billing-for-github-actions/about-billing-for-github-actions)._

## How to add a new *public* repo for scanning by Scala Steward

Just grant our GitHub app [@gu-scala-steward-public-repos](https://github.com/apps/gu-scala-steward-public-repos) **access to your repo** - you
will need to be a repo admin to do this): 

1. Click `Configure` on [the GitHub app's page](https://github.com/apps/gu-scala-steward-public-repos): ![image](https://github.com/guardian/scala-steward-public-repos/assets/52038/7f120478-e5e1-4117-9e35-323cc170d982)
2. On the next page, add your repo to the list of selected repos, and click `Save`.
3. If you have `Write` access to this repo, manually run [the GitHub workflow](https://github.com/guardian/scala-steward-public-repos/actions/workflows/public-repos-scala-steward.yml), otherwise wait until the next scheduled run
   to check that Scala Steward is now processing your repo too - **you're all done!**

## How to remove a *public* repo, that no longer uses Scala from scanning by Scala Steward

Only repo admins can do this, if you are not a repo admin, the repo will not appear in the list of repos that you can deselect even if it is part of the list. In that case, you will need to ask a repo admin to do it for you.

The process is the same as [adding a repo](#how-to-add-a-new-public-repo-for-scanning-by-scala-steward), but instead of selecting the repo, you will need to deselect it and click `Save` to remove it from the list of repos that Scala Steward scans.
