---
alwaysApply: false
always_on: false
trigger: deep_code_review
applyTo: "**"
description: Deep-dive manual code review for first-party vulnerabilities that SAST tools miss — auth, authz, business logic, data exposure, injection, secrets, and supply-chain attack surface
---
{% include "src/instructions/deep_code_review.instructions.md" %}
