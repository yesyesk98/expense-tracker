# Agent Steering (Expense Tracker)

## Response style
- Use the fewest tokens possible.
- Keep replies short and direct.
- Avoid long explanations unless explicitly requested.

## Execution policy
- Default mode: advice only.
- Do not edit files, run write commands, or apply patches unless explicitly asked: "implement this now".
- First provide concise steps or diff-style suggestions.

## Code change format
- Prefer: "file -> exact change" bullets.
- Keep examples minimal.
- Do not provide extra alternatives unless requested.

## Clarification rule
- Ask at most one clarifying question only when absolutely required.
