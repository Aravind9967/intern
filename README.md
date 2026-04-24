# intern

“I used Redis as a gatekeeper to ensure thread safety before hitting the database.

For limiting bot replies, I used Redis INCR, which is atomic. So even under high concurrency, only the first 100 requests are allowed, and the rest are rejected.

For nesting, I simply check the depth in code and reject anything beyond the limit.

For spam control, I used Redis SETNX with a TTL. It ensures that once a bot interacts with a user, it can’t repeat the action for 10 minutes.

Since all these Redis operations are atomic, they prevent race conditions and ensure only valid requests reach PostgreSQL.”
