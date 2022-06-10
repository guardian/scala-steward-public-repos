# scala-steward-public-repos
_Configuration for the Guardian's in-house instance of Scala Steward, suitable for PUBLIC Guardian repos_

If you'd like our internal instance of Scala Steward to scan your repos, **just add the repo to
the list in [REPOSITORIES.md](REPOSITORIES.md)**.

Note that if your repo is private, you may want to add it to the _private_ Guardian repo for
that purpose: https://github.com/guardian/scala-steward-private-repos - note that we pay for
the GitHub Action minutes for scanning running in that repo, because it's private, but it
at least will keep your security vulnerabilities private! 

You will probably also want to add some [repository-specific configuration](https://github.com/scala-steward-org/scala-steward/blob/main/docs/repo-specific-configuration.md) to your repo to tune how many PRs get raised, but that's all the config
that should be necessary!

### How is this instance of Scala Steward setup?

See:

* https://github.com/marketplace/actions/scala-steward-github-action
* https://hector.dev/2020/11/18/centralized-scala-steward-with-github-actions
* https://github.com/scala-steward-org/scala-steward/blob/main/docs/running.md#running-on-premise
