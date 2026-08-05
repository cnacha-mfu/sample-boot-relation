# sample-boot-relation — JPA relationships demo (one-to-many)

In-class live-coding demo: link two entities with `@ManyToOne` /
`@OneToMany`, query across the relationship with derived queries, and expose
it through nested REST routes (`/customers/{id}/orders`) — backed by H2.

**➡️ Follow the demonstration flow in [TODO.md](TODO.md).**

- Branch `main` — in-class version with `TODO`s we fill together
- Branch [`complete`](https://github.com/cnacha-mfu/sample-boot-relation/tree/complete) — finished code for every step
- Prepares you for the graded lab: [lab-web-relation](https://github.com/maefahluang-uni/lab-web-relation)

```
mvn spring-boot:run     # http://localhost:8080  (H2 console: /h2-console)
```
