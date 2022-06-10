# scala-steward-public-repos
_Configuration for the Guardian's in-house instance of Scala Steward, suitable for PUBLIC Guardian repos_

If you have a **public** Guardian repo you'd like to scan with Scala Steward, **just add the repo to
the list in [REPOSITORIES.md](REPOSITORIES.md)** - this repo (`scala-steward-public-repos`) is public,
so the GitHub Action minutes are [free](https://docs.github.com/en/billing/managing-billing-for-github-actions/about-billing-for-github-actions).

The Scala Steward action in this repository is only setup with an access token for **public**
GitHub repos. If your repo is **private**, you will want to add it to
https://github.com/guardian/scala-steward-private-repos - note that we pay for
the GitHub Action minutes for scanning running from that repo, because it's private, but it
at least will keep your security vulnerabilities private! 

You will probably also want to add some [repository-specific configuration](https://github.com/scala-steward-org/scala-steward/blob/main/docs/repo-specific-configuration.md) to your repo to tune how many PRs get raised, but that's all the config
that should be necessary!

### How is this instance of Scala Steward setup?

See:

* https://github.com/marketplace/actions/scala-steward-github-action
* https://hector.dev/2020/11/18/centralized-scala-steward-with-github-actions
* https://github.com/scala-steward-org/scala-steward/blob/main/docs/running.md#running-on-premise
