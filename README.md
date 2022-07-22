# scala-steward-public-repos
_Configuration for the Guardian's in-house instance of Scala Steward, suitable for PUBLIC Guardian repos_

If you have a **public** Guardian repo you'd like to scan with Scala Steward, **request to [install the GitHub app](https://github.com/apps/gu-scala-steward-public-repos) on your repository and add it to the list in [REPOSITORIES.md](REPOSITORIES.md)** - this repo (`scala-steward-public-repos`) is public,
so the GitHub Action minutes are [free](https://docs.github.com/en/billing/managing-billing-for-github-actions/about-billing-for-github-actions).

The Scala Steward action in this repository should only be given access to **public**
GitHub repos. If your repo is **private**, you will want to add it to
https://github.com/guardian/scala-steward-private-repos - note that we pay for
the GitHub Action minutes for scanning running from that repo, because it's private, but it
at least will keep your security vulnerabilities private!

You may also want to add some [repository-specific configuration](https://github.com/scala-steward-org/scala-steward/blob/main/docs/repo-specific-configuration.md)
to your repo to tune how many PRs get raised, but note that we have a [default config](.github/.scala-steward.conf)
that should already be filtering out the most-noisy dependencies (like `software.amazon.awssdk`, which has automatic
daily releases without obvious security value).

If you would like Scala Steward to update a non-default branch you can do so by adding branches individually to [REPOSITORIES.md](./REPOSITORIES.md) as described in the [Scala Steward Action docs](https://github.com/scala-steward-org/scala-steward-action#updating-a-custom-branch).

### Why do we have our own instance of Scala Steward for public repos?

The public instance of Scala Steward run by Frank Thomas, the creator of Scala Steward, was shutdown
in [June 2022](https://github.com/scala-steward-org/repos/issues/1029), so we need to run our own instance!
Note that until https://github.com/VirtusLab/scala-steward-repos/pull/23 is merged on the _new_ community
instance, we may have duplicate PRs :(

### How is this instance of Scala Steward setup?

See:

* https://github.com/marketplace/actions/scala-steward-github-action
* https://hector.dev/2020/11/18/centralized-scala-steward-with-github-actions
* https://github.com/scala-steward-org/scala-steward/blob/main/docs/running.md#running-on-premise
