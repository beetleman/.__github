---
alwaysApply: false
always_on: false
trigger: security_review
applyTo: "**"
description: Full security review — SAST, SCA, fix validation, and breakability assessment using Snyk MCP
---
{% include "src/instructions/snyk_security_review.instructions.md" %}
