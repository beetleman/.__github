---
description: Review a pull request for security issues
arguments:
  - name: pull_request
    description: Pull request URL or owner/repo#number reference
    required: true
---
{% verbatim %}
Review pull request `{{pull_request}}` for security issues.

Do the following:
1. Resolve the pull request target and inspect the changed files, checks, and review context available through GitHub tools.
2. Run a focused security review of the diff, combining automated scanning where available with manual analysis.
3. Prioritize exploitable issues in authentication, authorization, data exposure, injection, secrets, dependency changes, and risky infrastructure drift.
4. Distinguish confirmed findings from follow-up questions or unverified concerns.
5. Return:
   - `PR summary`
   - `Findings`
   - `Severity and rationale`
   - `Recommended fixes`
   - `Verification notes`

If the pull request reference is ambiguous, ask for the exact URL or `owner/repo#number` form before continuing.
{% endverbatim %}
