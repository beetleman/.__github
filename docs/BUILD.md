# Build system

This repo keeps canonical content under `src/` and generates harness-specific outputs for GitHub Copilot and ECA.

Harness-aware source templates live under `src/`, shared fragments live under `src/common/`, `scripts/targets.yaml` is a routing table, and `scripts/build.clj` stays a thin renderer/dispatcher.

## Source of truth

If you want to change content, edit `src/` and regenerate.

Treat `scripts/targets.yaml` as routing data: it selects harness source paths via `from` and output destinations via `to`, while canonical metadata and presentation live with the source file under `src/`.

## Generated outputs

The build writes committed runtime artifacts for the supported harnesses.

Use `scripts/targets.yaml` for the exact output matrix and destination paths.

At a high level, outputs land under:

- `github/` for generated GitHub Copilot export artifacts
- `.github/workflows/` for repository automation
- `eca-plugins/` for ECA plugin artifacts
- `.eca-plugin/` for the ECA plugin marketplace metadata

## Build inputs

The generation pipeline is currently driven by:

- `scripts/build.clj`
- `scripts/targets.yaml`
- `src/` and `src/common/`
- `mise.toml`

The current architecture is:

- keep canonical source material in `src/`
- keep harness-aware source templates under `src/`
- keep reusable front matter/body fragments under `src/common/`
- keep build mechanics and routing thin in `scripts/`

## Architectural boundary

Keep responsibilities split across the build assets:

- `scripts/build.clj` is the thin runner/mechanics layer. It loads configuration, walks entries, renders the configured `from` source file, and writes outputs. It should stay free of artifact-family-specific shaping logic.
- `scripts/targets.yaml` is the routing table. It should answer: which direct `from` source file renders this artifact, and where does the output go?
- `src/` is the canonical source for generated artifact families, including harness-aware front matter and presentation details.
- `src/common/` is the canonical home for shared fragments reused by those source templates.

As a rule of thumb:

- change `scripts/build.clj` for pipeline mechanics only
- change `scripts/targets.yaml` for routing only
- change `src/` and `src/common/` for canonical content and rendering

## Main commands

Use `mise` as the entrypoint for local and CI workflows.

| Command | Purpose |
|---|---|
| `mise install` | Install pinned tooling |
| `mise run build` | Generate all committed outputs from `src/` |
| `mise run build-copilot` | Generate only GitHub Copilot outputs |
| `mise run build-eca` | Generate only ECA plugin outputs |
| `mise run check-generated` | Compare regenerated output with committed output |
| `mise run check-schema` | Validate `scripts/targets.yaml` against the schema |
| `mise run check` | Run both generated-file and schema checks |

## Typical change flow

1. Edit canonical content under `src/`.
2. Update `scripts/targets.yaml` only when routing or output destinations change.
3. Keep harness-specific rendering in the source template under `src/` and reuse fragments from `src/common/` when possible.
4. Run `mise run build`.
5. Run `mise run check`.
6. If you need to narrow down a failure, run `mise run check-generated` or `mise run check-schema` directly.
7. Commit the canonical files plus regenerated outputs.

## Repo-specific notes

- ECA runtime configuration is not generated here.
- User- or workspace-local ECA runtime setup should follow upstream ECA documentation.
- This repo owns the generated plugin artifacts, not user-local ECA config.

For repo-specific ECA setup, see [`docs/ECA-SETUP.md`](ECA-SETUP.md).
