---
description: Run a deep manual security review of the current codebase
---
Perform a deep manual security review of the current repository.

Do the following:
1. Map the main trust boundaries, entry points, sensitive data flows, and privileged operations.
2. Review first-party code for authentication, authorization, business logic, secrets handling, injection, and data exposure issues.
3. Focus on vulnerabilities that automated scanners commonly miss.
4. Prioritize findings by realistic impact and ease of abuse.
5. Return a concise report with:
   - `Architecture and trust boundaries`
   - `Findings`
   - `Evidence`
   - `Recommended fixes`
   - `Residual risk`

If the repository is large, start with the highest-risk surfaces and say what remains out of scope.
