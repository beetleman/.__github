Scan the current repository for vulnerabilities.

Do the following:
1. Inspect the repo context to understand the main languages, package managers, and deployable components.
2. Run the available Snyk scans that fit the repository contents, prioritizing first-party code and dependencies.
3. Summarize confirmed findings by severity, affected component, and likely exploitability.
4. Recommend the highest-value remediations first.
5. If you make code changes, rescan the modified scope and report the post-fix status.

Return:
- `Scan coverage`
- `Key findings`
- `Recommended fixes`
- `Follow-up checks`
