Guide a threat-modeling pass for `{{scope}}`.

Do the following:
1. Define the system or workflow scope, assets, actors, trust boundaries, and external dependencies.
2. Identify likely abuse cases, attacker goals, and high-impact failure modes.
3. Map threats across authentication, authorization, data handling, secrets, supply chain, and operational misuse.
4. Recommend practical mitigations, detective controls, and follow-up validation steps.
5. Return a concise write-up with:
   - `Scope`
   - `Assets and trust boundaries`
   - `Threats`
   - `Mitigations`
   - `Open questions`

If `{{scope}}` looks like an SSDLC control identifier, include control-oriented guidance alongside the system threat model.
