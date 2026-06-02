# ECA setup

This repo exposes its ECA content as a local plugin source. It is not consumed by copying generated files into `.eca/`.

## What this repo provides

After `mise run build`, the generated ECA plugin artifacts live under:

```text
.eca-plugin/marketplace.json
eca-plugins/
└── vertex-security/
    ├── agents/
    ├── commands/
    ├── rules/
    └── skills/
```

`.eca-plugin/marketplace.json` declares the `vertex-security` plugin and points ECA at `eca-plugins/vertex-security`.

## Local config workflow

Per upstream ECA plugin semantics, other users should point a plugin source at the repo root and install the plugin by name.

A concrete user-local config example:

```json
{
  "plugins": {
    "vertex-local": {
      "source": "/Users/your-name/path/to/Vertex-Security-Expert-Copilot-Agent"
    },
    "install": ["vertex-security"]
  }
}
```

Keep that runtime config user-local or workspace-local, for example in:

- `~/.config/eca/config.json`, or
- an uncommitted workspace `.eca/config.json`

That workspace `.eca/config.json` is only a local ECA config location. It is not the consumption target for this repo's generated agents, rules, or skills.

Use upstream ECA docs as the source of truth for config structure and supported keys:

- config introduction: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/introduction.md>
- plugins: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/plugins.md>
- agents: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/agents.md>
- skills: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/skills.md>
- tools and MCP: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config/tools.md>
- JSON schema: <https://raw.githubusercontent.com/editor-code-assistant/eca/master/docs/config.json>

## Expected MCP server names

Generated ECA artifacts in this repo assume these MCP server names:

- `github`
- `snyk`

Those names matter because generated prompts may refer to tool ids using those prefixes.

## Secrets

Keep tokens out of committed files. Prefer shell environment variables or another local secret-management mechanism.

Common variables used with local ECA setups for this repo:

- `GITHUB_TOKEN`
- `SNYK_TOKEN`

## Repo-specific workflow

1. Run `mise run build`.
2. Configure ECA plugins with a source pointing at this repo root and `install: ["vertex-security"]`.
3. Start or reload ECA and use the generated plugin-provided agents, commands, rules, and skills.
4. Invoke plugin commands with the plugin prefix, for example `/vertex-security:scan-repo` or `/vertex-security:review-pr https://github.com/owner/repo/pull/123`.
