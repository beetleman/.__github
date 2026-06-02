# Goose compatibility summary

This file keeps only the durable Goose-specific adapter notes for this repo.

## What matters for this repo

Goose can consume the same general content model through:

- recipes
- `AGENTS.md` or `.goosehints`
- `SKILL.md`-based skills
- MCP-backed extensions

That makes Goose a reasonable target for the same portable security content, but the exact runtime wiring should come from Goose documentation rather than long repo-local migration plans.

## Recommended approach

Use upstream Goose docs as the source of truth for recipe schema, hints, skills, subagents, and extensions:

- Goose docs home: <https://goose-docs.ai/>
- recipes overview: <https://goose-docs.ai/docs/guides/recipes/>
- recipe reference: <https://goose-docs.ai/docs/guides/recipes/recipe-reference>
- reusable recipes and sharing: <https://goose-docs.ai/docs/guides/recipes/session-recipes/>
- hints and `AGENTS.md`: <https://goose-docs.ai/docs/guides/context-engineering/using-goosehints>
- skills: <https://goose-docs.ai/docs/guides/context-engineering/using-skills>
- subagents: <https://goose-docs.ai/docs/guides/context-engineering/subagents>
- extensions: <https://goose-docs.ai/docs/getting-started/using-extensions>

## Repo-specific takeaways

- keep shared security knowledge portable and capability-oriented
- avoid embedding long Goose-only migration plans in this repo
- if Goose support is added, keep the adapter layer thin and link back to vendor docs for recipe and extension mechanics
