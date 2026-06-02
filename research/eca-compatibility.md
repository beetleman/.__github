# ECA compatibility summary

This file keeps only the durable ECA-specific adapter notes for this repo.

## What matters for this repo

ECA separates concerns across several extension points:

- agents
- rules
- skills
- commands
- runtime config

For this repository, the practical split is:

- canonical content lives under `src/`
- generated ECA plugin artifacts live under `eca-plugins/vertex-security/`
- the plugin marketplace entry lives at `.eca-plugin/marketplace.json`
- runtime config remains local and is not committed by this repo

## Recommended approach

Use upstream ECA docs as the source of truth for config keys, front matter, tool approval, MCP configuration, and skill loading:

- config introduction: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/introduction.md>
- agents: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/agents.md>
- commands: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/commands.md>
- rules: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/rules.md>
- skills: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/skills.md>
- tools and MCP: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/tools.md>
- models: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/models.md>
- JSON schema: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config.json>
- agent skills spec: <https://agentskills.io/specification>

## Repo-specific takeaways

- do not commit shared runtime credentials or user-specific ECA config
- keep generated plugin artifacts under `eca-plugins/vertex-security/` as build output
- keep `.eca-plugin/marketplace.json` committed so ECA can discover the plugin source
- treat the repo root as the ECA plugin source and install `vertex-security` by name
- do not document or reintroduce an old `.eca/` copy/link workflow for generated content
- document only repo-specific expectations, such as required MCP server names used by generated tool references
- replace long ECA porting plans with short repo-specific notes plus upstream links
