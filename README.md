# Scala Steward setup for PUBLIC Guardian repos

[![Public Repos Scala Steward](https://github.com/guardian/scala-steward-public-repos/actions/workflows/public-repos-scala-steward.yml/badge.svg)](https://github.com/guardian/scala-steward-public-repos/actions/workflows/public-repos-scala-steward.yml)

_Looking to run Scala Steward on a **private** Guardian repo? Go to
[guardian/scala-steward-private-repos](https://github.com/guardian/scala-steward-private-repos)
instead!_

## How to add a new *public* repo for scanning by Scala Steward

1. Add your repo to the list in [REPOSITORIES.md](REPOSITORIES.md)
2. Request to [install our GitHub app](https://github.com/apps/gu-scala-steward-public-repos) on your repo

This repo (`scala-steward-public-repos`) is public, so the GitHub Action minutes are
[free](https://docs.github.com/en/billing/managing-billing-for-github-actions/about-billing-for-github-actions).

### How is this instance of Scala Steward setup?

See:

* https://github.com/marketplace/actions/scala-steward-github-action
* https://hector.dev/2020/11/18/centralized-scala-steward-with-github-actions
* https://github.com/scala-steward-org/scala-steward/blob/main/docs/running.md#running-on-premise
