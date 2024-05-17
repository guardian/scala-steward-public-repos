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
3. Manually run [the GitHub workflow](https://github.com/guardian/scala-steward-public-repos/actions/workflows/public-repos-scala-steward.yml)
   to check that Scala Steward is now processing your repo too - **you're all done!**

### How to double-check if Scala Steward has access to your repo

If you need to, check in `/settings/installations` (eg https://github.com/guardian/amigo/settings/installations):
![image](https://github.com/guardian/scala-steward-public-repos/assets/52038/9b7dc7b7-a6fc-46d6-b313-a4ae97d5d3ad)
