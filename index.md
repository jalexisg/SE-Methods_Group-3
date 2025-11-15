<!--
  reports/index.md
  Placeholder index for generated reports. This file is committed so the
  CI `DeployReports` job has a `reports/` path to deploy even when no
  reports are produced yet by the application.

  The real reports are produced by the app (or by the container) and
  placed under `/tmp/reports/` inside the running container. The CI
  workflow attempts to copy that folder into this repo's `./reports`
  directory before deploying. When real reports are present they will
  replace this placeholder.
-->

