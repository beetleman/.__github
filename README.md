# Vertex Security Expert Agent Pack

A portable security-agent content pack built from one canonical source tree.

This repository keeps the durable security knowledge under `src/` and generates harness-specific outputs for supported runtimes:

- `github/` for GitHub Copilot-oriented export artifacts
- `eca-plugins/vertex-security/` for Editor Code Assistant plugin artifacts

The goal is to keep the security guidance portable while keeping harness-specific setup thin.

## What is in this repo

- security-review agents
- SSDLC guidance
- Snyk review instructions
- deep manual security-review instructions
- API-security skills
- harness-aware source templates and shared fragments that generate harness-specific markdown

## Source of truth

Edit canonical content under `src/`.

Generated outputs under `github/` and `eca-plugins/vertex-security/` are derived from `src/` and committed so they can be consumed directly by supported tools. The `.github/` tree is reserved for workflows and maintenance metadata. The ECA plugin marketplace entry lives in `.eca-plugin/marketplace.json`.

All generated artifact families now render from harness-aware source templates under `src/`, with reusable Selmer fragments under `src/common/`.

For ECA, consumers should point ECA at this repo root as a plugin source and install `vertex-security`. This repo is not consumed by copying generated files into `.eca/`.

See [`docs/BUILD.md`](docs/BUILD.md) for the generation model and repo layout.

## Supported harnesses

This repo currently ships generated content for:

- GitHub Copilot-oriented exports via `github/`
- ECA via the `vertex-security` plugin at `eca-plugins/vertex-security/`

Other tools may be able to reuse the same content model through `AGENTS.md` and `SKILL.md`-style patterns, but their exact setup should follow the vendor documentation.

## Setup

This repo intentionally avoids embedding large, tool-specific setup guides in the main README.

Use the vendor documentation for runtime setup, then use this repo as a generated content source:

- ECA config and plugin docs:
  - <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/introduction.md>
  - <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/agents.md>
  - <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/skills.md>
  - <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/tools.md>
  - <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/plugins.md>
- VS Code / GitHub Copilot customization docs:
  - <https://code.visualstudio.com/docs/copilot/customization/custom-instructions>
  - <https://code.visualstudio.com/docs/copilot/customization/custom-chat-modes>
  - <https://code.visualstudio.com/docs/copilot/customization/agent-skills>
  - <https://code.visualstudio.com/docs/copilot/customization/mcp-servers>

Repo-specific setup notes for ECA are kept in [`docs/ECA-SETUP.md`](docs/ECA-SETUP.md).

## Usage

Once the agent is installed, you can either select the agent and ask in natural language or, in ECA, invoke the plugin's slash commands directly.

- **GitHub Copilot:** open Copilot Chat (`Cmd+Shift+I`) → open the agent picker → select **`Vertex Security Agent`**.
- **ECA natural-language flow:** select the **`vertex-security-agent`** agent and ask normally. It can route work to the bundled skills automatically.
- **ECA slash-command flow:** run plugin-prefixed commands such as `/vertex-security:scan-repo`. Keep using the agent when you want broader free-form routing beyond the command shortcut itself.

| Goal | Natural-language example | ECA slash command |
|---|---|---|
| Threat modeling guidance | `Walk me through DES-A01 threat modeling` | `/vertex-security:threat-model DES-A01` |
| BOLA / IDOR testing | `How do I test an API for BOLA?` | `/vertex-security:bola-test payments-api` |
| Repo-wide vulnerability scan | `Scan this repo for vulnerabilities` | `/vertex-security:scan-repo` |
| Deep manual review | `Do a deep security review` | `/vertex-security:deep-review` |
| Full PR security review | `Full security review of this PR` | `/vertex-security:review-pr https://github.com/owner/repo/pull/123` |

## Typical workflow

1. Edit canonical markdown under `src/`.
2. Run `mise install` if needed.
3. Run `mise run build`.
4. Run `mise run check` (this now includes schema validation).
5. If you need to narrow down a failure, run `mise run check-generated` or `mise run check-schema` directly.
6. Commit both the canonical sources and regenerated outputs.

## Research and design notes

Short compatibility summaries live in `research/`:

- `research/portable-skills-and-best-practices.md`
- `research/copilot-compatibility.md`
- `research/eca-compatibility.md`
- `research/goose-compatibility.md`

These files are intentionally summary-first and link to upstream documentation instead of duplicating vendor docs inline.
