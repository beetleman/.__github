---
name: Vertex Security Agent
description: Security code review agent following Vertex SSDLC standards with Snyk integration
tools:
 tools:
  - mcp_github_get_file_contents
  - mcp_snyk_snyk_code_scan
  - mcp_snyk_snyk_sca_scan
  - mcp_snyk_snyk_iac_scan
  - mcp_snyk_snyk_container_scan
  - read_file
  - grep_search
  - semantic_search
  - file_search
  - get_errors
---
{% include "src/agents/Vertex-security-agent.agent.md" %}
