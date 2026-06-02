# Portable Agent Skills & Security-Agent Best Practices

## TL;DR

**Partial yes.** There is no single spec that every harness (GitHub Copilot, ECA, Goose, Claude Code, Cursor, …) fully honours, but two converging open standards get you most of the way: **Anthropic's Agent Skills spec** (`SKILL.md` + YAML front matter + progressive disclosure, now stewarded at [agentskills.io](https://agentskills.io/specification)) for *packaging task-specific knowledge*, and **`AGENTS.md`** (stewarded by the Agentic AI Foundation under the Linux Foundation, [agents.md](https://agents.md/)) for *repo-level agent instructions* read natively by Copilot, Cursor, Codex, Aider, Goose, Zed, Windsurf, Gemini CLI, Jules, Factory, Amp, Warp, RooCode, Kilo Code, Augment Code, opencode, and others. Tooling (MCP servers) is layered separately and is **already portable by design** — only the *tool-name prefix the host displays* (e.g. Copilot's `mcp_<server>_<tool>`) is harness-specific.

---

## 1. Anthropic Agent Skills

The spec previously hosted in [`anthropics/skills`](https://github.com/anthropics/skills/blob/main/spec/agent-skills-spec.md) now redirects to <https://agentskills.io/specification>. Claude's own docs explicitly call it an "[open standard](https://claude.com/docs/skills/overview) … platform-agnostic … Skills you create can work across any platform adopting the standard."

**Directory shape** ([source](https://agentskills.io/specification)):

```
skill-name/
├── SKILL.md          # Required: YAML front matter + Markdown body
├── scripts/          # Optional: executable helpers
├── references/       # Optional: deeper docs loaded on demand
└── assets/           # Optional: templates, schemas, etc.
```

**Front-matter fields** ([source](https://agentskills.io/specification)):

| Field | Required | Notes |
|---|---|---|
| `name` | yes | ≤64 chars, lowercase + digits + `-`, must match dir name |
| `description` | yes | ≤1024 chars; states *what it does* AND *when to use it* (used for activation) |
| `license` | no | License name or path to bundled license |
| `compatibility` | no | ≤500 chars, env requirements |
| `metadata` | no | arbitrary string→string map |
| `allowed-tools` | no (experimental) | space-separated pre-approved tools, e.g. `Bash(git:*) Bash(jq:*) Read` |

**Progressive disclosure** (three layers) ([source](https://agentskills.io/specification), [Claude docs](https://claude.com/docs/skills/overview)):
1. Metadata (~100 tokens per skill) — name + description loaded at startup.
2. Body — full `SKILL.md` (recommended <5 000 tokens / <500 lines) loaded when the skill is activated.
3. Resources — files under `scripts/`, `references/`, `assets/` are loaded only when needed.

**Example `SKILL.md`** (canonical, from the spec):

```markdown
---
name: pdf-processing
description: Extract PDF text, fill forms, merge files. Use when handling PDFs.
license: Apache-2.0
compatibility: Requires Python 3.14+ and uv
allowed-tools: Bash(git:*) Read
metadata:
  author: example-org
  version: "1.0"
---

# PDF processing

## Steps
1. ...

See [references/REFERENCE.md](references/REFERENCE.md) for the full API.
Run `scripts/extract.py` to extract text.
```

Validation tool: `skills-ref validate ./my-skill` ([source](https://agentskills.io/specification)).

Anthropic's launch post: ["Equipping agents for the real world with Agent Skills"](https://www.anthropic.com/engineering/equipping-agents-for-the-real-world-with-agent-skills).

---

## 2. AGENTS.md ecosystem

`AGENTS.md` is a plain-Markdown file at the repo root (no required schema, nested files allowed in monorepos; nearest file wins) ([agents.md FAQ](https://agents.md/)).

| Harness | Reads `AGENTS.md` natively? | Source |
|---|---|---|
| OpenAI Codex (CLI + cloud) | ✅ | [agents.md](https://agents.md/) |
| GitHub Copilot (coding agent / VS Code agent mode) | ✅ (since Aug 2025) | [agents.md](https://agents.md/), [adoption note](https://gist.github.com/0xfauzi/7c8f65572930a21efa62623557d83f6e) |
| Cursor | ✅ | [agents.md](https://agents.md/) |
| Jules (Google) | ✅ | [agents.md](https://agents.md/) |
| Gemini CLI | ✅ (set `context.fileName: AGENTS.md` in `.gemini/settings.json`) | [agents.md FAQ](https://agents.md/) |
| Aider | ✅ (configure `read: AGENTS.md` in `.aider.conf.yml`) | [agents.md FAQ](https://agents.md/) |
| Goose | ✅ | [agents.md](https://agents.md/) |
| opencode | ✅ | [agents.md](https://agents.md/) |
| Zed | ✅ | [agents.md](https://agents.md/) |
| Warp | ✅ | [agents.md](https://agents.md/) |
| VS Code (built-in chat) | ✅ | [agents.md](https://agents.md/) |
| Devin (Cognition) | ✅ | [agents.md](https://agents.md/) |
| Windsurf (Cognition) | ✅ | [agents.md](https://agents.md/) |
| Factory | ✅ | [agents.md](https://agents.md/) |
| Amp | ✅ | [agents.md](https://agents.md/) |
| RooCode / Kilo Code / Augment Code / Ona / Phoenix / Semgrep / Junie / UiPath Autopilot | ✅ | [agents.md](https://agents.md/) |
| Claude Code | ⚠️ Reads `CLAUDE.md` by convention; uses `AGENTS.md` only via symlink/alias | [agents.md FAQ](https://agents.md/), [comparison](https://codersera.com/blog/agents-md-vs-claude-md-vs-cursor-rules-comparison-2026/) |
| ECA (Editor Code Assistant) | ⚠️ Uses its own `AGENTS.md`-style + per-agent files (see [docs](https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/agents.md)); compatible by symlink | ECA upstream docs |
| Codeium (now Windsurf) | ✅ via Windsurf | [agents.md](https://agents.md/) |

Migration tip from the spec: `mv CLAUDE.md AGENTS.md && ln -s AGENTS.md CLAUDE.md` ([source](https://agents.md/)).

---

## 3. Snyk MCP — authoritative tool reference

The Snyk MCP server ships inside the **Snyk CLI** (`snyk mcp`), source repo: **<https://github.com/snyk/studio-mcp>** (the older `snyk/snyk-ls/mcp_extension` now redirects there — [proof](https://github.com/snyk/snyk-ls/blob/main/mcp_extension/README.md)).

Official tool surface ([README](https://github.com/snyk/studio-mcp)):

| Tool | Purpose |
|---|---|
| `snyk_sca_scan` | Open Source / dependency scan |
| `snyk_code_scan` | SAST (Snyk Code) |
| `snyk_iac_scan` | Infrastructure-as-Code misconfig scan |
| `snyk_container_scan` | Container image scan |
| `snyk_sbom_scan` | Scan an existing CycloneDX/SPDX SBOM |
| `snyk_secret_scan` | Secret detection (experimental) |
| `snyk_aibom` | Generate an AI Bill of Materials |
| `snyk_package_health_check` | Pre-install package risk/health lookup |
| `snyk_trust` | Mark a folder as trusted before scanning |
| `snyk_auth` / `snyk_auth_status` / `snyk_logout` | Auth lifecycle |
| `snyk_version` | CLI/MCP version |

Quickstart and troubleshooting (linked from upstream README):
- <https://docs.snyk.io/integrations/snyk-studio-agentic-integrations/quickstart-guides-for-snyk-studio>
- <https://docs.snyk.io/integrations/snyk-studio-agentic-integrations/troubleshooting>

> `snyk_sca_scan` may shell out to ecosystem tools (Gradle, Maven, pip, …) on the host — agents must therefore call `snyk_trust` on the target folder first ([source](https://github.com/snyk/studio-mcp)).

> Some hosts surface an additional `snyk_send_feedback` telemetry tool (visible in MCP catalogues such as the ECA-exposed one in this repo); it is not currently listed in the upstream README — treat as host-side instrumentation rather than a stable contract.

---

## 4. GitHub MCP — authoritative tool reference

Upstream: **<https://github.com/github/github-mcp-server>** (remote endpoint: `https://api.githubcopilot.com/mcp/`). Toolset table and per-toolset URLs: [`docs/remote-server.md`](https://github.com/github/github-mcp-server/blob/main/docs/remote-server.md).

Canonical (server-side) tool names are short and unprefixed — e.g.:
`get_file_contents`, `get_commit`, `list_branches`, `list_commits`, `list_pull_requests`, `pull_request_read`, `search_code`, `search_issues`, `search_pull_requests`, `create_pull_request_with_copilot` (remote-only), `get_me`, etc. (see README + `docs/remote-server.md`).

Toolsets are partitioned and can be enabled per-URL (`/x/issues`, `/x/code_security`, `/x/actions`, `/x/dependabot`, `/x/pull_requests`, …; append `/readonly` for read-only variants) — useful for **allow-listing** in a security agent ([source](https://github.com/github/github-mcp-server/blob/main/docs/remote-server.md)).

### Harness-specific naming caveat

The **canonical tool name is just `get_file_contents`**. What you see at the LLM call-site depends on the host:

- **GitHub Copilot / VS Code agent mode**: tools from an MCP server named `github` are exposed to the model as `mcp_github_get_file_contents` (pattern: `mcp_<server-name>_<tool>`). This namespace is a Copilot/VS Code convention, not part of the MCP spec.
- **Claude Code, Cursor, Goose, Codex CLI, opencode, Windsurf, Zed**: typically expose tools either by raw name or with their own server-scoping syntax (e.g. `server.tool`, `server__tool`).
- **ECA** (this repo): exposes them as `github-mpc__get_file_contents` (see tool inventory injected into this very session).

**Implication for portable skills:** never hard-code the prefixed form. Refer to tools by their canonical name plus the MCP server they come from (e.g. "the `get_file_contents` tool from the `github` MCP server") and let the host resolve the alias.

---

## 5. Security-review agent — design best practices

- **Define a deterministic phase plan in the prompt.** Recommended phases: *triage → enumerate inputs/sinks → static checks (SAST/SCA/IaC) → manual code review → remediation proposals → verification*. The Anthropic Agent Skills launch explicitly recommends modular, multi-step procedures over one mega-prompt ([source](https://www.anthropic.com/engineering/equipping-agents-for-the-real-world-with-agent-skills)).
- **Constrain tool access with `allowed-tools` / MCP allow-lists.** The Agent Skills spec defines an `allowed-tools` front-matter field for pre-approval ([source](https://agentskills.io/specification)); GitHub MCP supports per-toolset URLs and `/readonly` variants so a review agent can be wired with read-only repo access plus only the security toolsets ([source](https://github.com/github/github-mcp-server/blob/main/docs/remote-server.md)). For Snyk, restrict to `snyk_code_scan`, `snyk_sca_scan`, `snyk_iac_scan`, `snyk_container_scan`, `snyk_sbom_scan`, `snyk_package_health_check` — never `snyk_trust`/`snyk_auth` without user consent ([source](https://github.com/snyk/studio-mcp)).
- **Use progressive disclosure to keep context lean.** Top-level `SKILL.md` should be a router/checklist; push CWE catalogues, language-specific patterns, and report templates into `references/` and `assets/` so they only load when needed ([source](https://agentskills.io/specification), [Claude docs](https://claude.com/docs/skills/overview)).
- **Emit a structured output schema.** Provide a JSON/Markdown template in `assets/` (e.g. `finding.schema.json` with `id, severity, cwe, file, line, evidence, fix, confidence`). Snyk LSP already publishes diagnostics with a comparable shape (CWE/CVE, dataflow) — mirroring it makes downstream automation easier ([source](https://raw.githubusercontent.com/snyk/snyk-ls/main/README.md)).
- **Autonomy gates: scan & report autonomously; only *fix* on explicit approval.** Snyk's own MCP requires a `snyk_trust` call before invoking scanners that shell out, codifying an explicit consent boundary ([source](https://github.com/snyk/studio-mcp)). Apply the same pattern for code edits: write findings + suggested patches; require user opt-in before applying.
- **Treat repo-level conventions via `AGENTS.md`, task knowledge via `SKILL.md`.** `AGENTS.md` is for build/test/lint/security conventions the *whole* agent ecosystem reads ([source](https://agents.md/)); skills are for *task-specific procedures* discoverable via descriptions ([source](https://agentskills.io/specification)). Don't mix the two.
- **Nest `AGENTS.md` in monorepos.** Nearest-file-wins lets a `security/` subtree carry stricter rules ([source](https://agents.md/)).
- **Never hard-code host-specific tool prefixes** (`mcp_github_*`, `github-mpc__*`, etc.) — reference tools by canonical name + server (see §4).

---

## 6. Recommended portable layout for this repo

Keep the portable structure simple:

- `AGENTS.md` for repo-level behavior
- `skills/<name>/SKILL.md` for reusable task knowledge
- thin harness-specific adapters only where needed
- MCP configuration documented separately per host instead of duplicated inline

Rationale:

- `AGENTS.md` is broadly portable across many agent hosts ([source](https://agents.md/)).
- `SKILL.md` is the closest thing to a shared cross-host skill format ([source](https://agentskills.io/specification)).
- Harness-specific glue should stay small so the main security content remains reusable.
- Vendor docs should own runtime setup details; this repo should mainly document its own content model.

---

## References

1. Agent Skills specification — <https://agentskills.io/specification>
2. `anthropics/skills` (now a pointer to the spec site) — <https://github.com/anthropics/skills/blob/main/spec/agent-skills-spec.md>
3. Anthropic engineering blog, "Equipping agents for the real world with Agent Skills" — <https://www.anthropic.com/engineering/equipping-agents-for-the-real-world-with-agent-skills>
4. Claude docs, "Skills overview" — <https://claude.com/docs/skills/overview>
5. `AGENTS.md` standard — <https://agents.md/>
6. Comparison of AGENTS.md / CLAUDE.md / Cursor rules / Copilot instructions — <https://codersera.com/blog/agents-md-vs-claude-md-vs-cursor-rules-comparison-2026/>
7. AGENTS.md adoption notes (Aug 2025 Copilot landing) — <https://gist.github.com/0xfauzi/7c8f65572930a21efa62623557d83f6e>
8. Snyk MCP (Studio MCP) — <https://github.com/snyk/studio-mcp>
9. Snyk LS README (LSP diagnostics shape) — <https://github.com/snyk/snyk-ls>
10. Snyk Studio agentic integrations quickstart — <https://docs.snyk.io/integrations/snyk-studio-agentic-integrations/quickstart-guides-for-snyk-studio>
11. GitHub MCP server — <https://github.com/github/github-mcp-server>
12. GitHub MCP remote toolsets — <https://github.com/github/github-mcp-server/blob/main/docs/remote-server.md>
13. ECA agents docs — <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/agents.md>
14. Microsoft Learn, Agent Framework — Agent Skills — <https://learn.microsoft.com/en-us/agent-framework/agents/skills>
