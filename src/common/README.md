# Shared direct-source fragments

This directory holds reusable Selmer fragments for the harness-aware direct-source path under `src/`.

Current shared fragments cover the families that now render directly from `src/` via `from`-routed source files:

- `frontmatter/name-description.selmer` for repeated `name` + `description` front matter
- `frontmatter/copilot-knowledge-agent.selmer` for Copilot knowledge wrappers that add shared agent metadata
- `frontmatter/skill-domain.selmer` for ECA skill `domain` + `subdomain` metadata
- `frontmatter/skill-version.selmer` for skill `version` metadata
- `instructions/description.selmer` for instruction-style `description` lines
- `skills/api-security/*.selmer` for shared skill body includes used by the API-security family
- `skills/security-review/*.selmer` for shared security-review skill wrappers used by both harnesses

Keep fragments small and harness-neutral where possible. Shared shaping logic should live here when it is reused across `src/` source templates.
