# GitHub Copilot compatibility summary

This file keeps only the durable Copilot-specific adapter notes for this repo.

## What matters for this repo

- custom agents use `.agent.md` files
- repository and path-scoped instructions use markdown files documented by Copilot
- reusable skills follow the `SKILL.md` model
- MCP configuration should follow current VS Code / GitHub Copilot documentation instead of repo-local copies of old config snippets

## Recommended approach

Keep this repository focused on portable source content and generated markdown outputs.

For Copilot runtime behavior and file-format details, use upstream documentation as the source of truth:

- custom agents: <https://code.visualstudio.com/docs/copilot/customization/custom-chat-modes>
- custom instructions: <https://code.visualstudio.com/docs/copilot/customization/custom-instructions>
- prompt files: <https://code.visualstudio.com/docs/copilot/customization/prompt-files>
- MCP servers: <https://code.visualstudio.com/docs/copilot/customization/mcp-servers>
- agent skills: <https://code.visualstudio.com/docs/copilot/customization/agent-skills>
- repository custom instructions on GitHub: <https://docs.github.com/en/copilot/customizing-copilot/adding-repository-custom-instructions-for-github-copilot>

## Repo-specific takeaways

- avoid baking legacy MCP setup snippets into shared docs
- keep generated Copilot-facing markdown in `github/`, with `.github/` reserved for workflows and maintenance metadata
- prefer portable summaries over hard-coded host-specific tool-name conventions
- if Copilot-specific behavior must be documented, document only the repo delta and link upstream for the rest
