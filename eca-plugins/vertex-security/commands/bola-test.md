---
description: Test an API surface for BOLA and IDOR weaknesses
arguments:
  - name: target
    description: API, service, endpoint group, or feature area to assess
    required: true
---

Assess `{{target}}` for broken object level authorization (BOLA) and IDOR risk.

Work in the current repository and available API context.

Do the following:
1. Define the target surface, object types, actors, trust boundaries, and likely object identifiers.
2. Enumerate the read, create, update, delete, list, batch, and admin paths that may expose object-level authorization gaps.
3. Apply a focused BOLA review using OWASP API1:2023 patterns, including identifier tampering, cross-tenant access, bulk access, nested resources, and GraphQL object traversal when relevant.
4. Call out missing ownership checks, inconsistent authorization between endpoints, and high-risk test cases.
5. Produce a concise report with:
   - `Scope`
   - `Attack paths`
   - `Findings`
   - `Recommended tests`
   - `Remediation guidance`

If repository context is thin, say what additional endpoint or auth details are needed and still provide the highest-value review plan for `{{target}}`.

