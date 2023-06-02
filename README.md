# Scala Steward setup for PUBLIC Guardian repos

[![Public Repos Scala Steward](https://github.com/guardian/scala-steward-public-repos/actions/workflows/public-repos-scala-steward.yml/badge.svg)](https://github.com/guardian/scala-steward-public-repos/actions/workflows/public-repos-scala-steward.yml)

_Looking to run Scala Steward on a **private** Guardian repo? Go to
[guardian/scala-steward-private-repos](https://github.com/guardian/scala-steward-private-repos)
instead!_

## How to add a new *public* repo for scanning by Scala Steward

* **Grant [our GitHub app](https://github.com/apps/gu-scala-steward-public-repos) access to your repo** (click
  [`Configure`](https://github.com/organizations/guardian/settings/installations/26822732) - you may need
  a repo admin to do this) 
  * You can check if the app is installed on your repo by examining `/settings/installations`
    (eg https://github.com/guardian/amigo/settings/installations):
    ![image](https://github.com/guardian/scala-steward-public-repos/assets/52038/9b7dc7b7-a6fc-46d6-b313-a4ae97d5d3ad)
* Only add your repo to `REPOSITORIES.md` if you're specifying a non-default branch to update (after
  PR [#40](https://github.com/guardian/scala-steward-public-repos/pull/40) this is probably no longer needed).

This repo (`scala-steward-public-repos`) is public, so the GitHub Action minutes are
[free](https://docs.github.com/en/billing/managing-billing-for-github-actions/about-billing-for-github-actions).

### How is this instance of Scala Steward setup?

See:

* https://github.com/marketplace/actions/scala-steward-github-action
* https://hector.dev/2020/11/18/centralized-scala-steward-with-github-actions
* https://github.com/scala-steward-org/scala-steward/blob/main/docs/running.md#running-on-premise
