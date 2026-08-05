# In-Class Demonstration Flow — JPA Relationships (one-to-many)

This repository has these branches:

- **`main`** — the in-class version. Some code is blanked out with `TODO` comments. We fill these in together during the demonstration.
- **[`complete`](https://github.com/cnacha-mfu/sample-boot-relation/tree/complete)** — the finished code for every step. Use it to catch up or check your work.

**Start the application first** and restart it after each change:

```
mvn spring-boot:run
```

> **Postman:** import [`sample-boot-relation.postman_collection.json`](sample-boot-relation.postman_collection.json) — every request used below, in order, with status-code tests.

---

## Step 1: Two entities, no relationship yet

**Files:** `Customer.java`, `SaleOrder.java`, `data.sql` (read together)

**Idea:** last week each entity stood alone. Today: a customer HAS MANY orders, an order BELONGS TO one customer. In the database that's a **foreign key** (`customer_id` on the order table); in JPA it's a pair of annotations. `data.sql` ships 20 customers and 20 products.

**Try it:** run the app — it fails: `Could not determine type for: th.mfu.Customer` on `SaleOrder.customer`. Hibernate sees a `Customer` field but has no idea two tables are related. That's our job in Step 2.

---

## Step 2: Wire the relationship — @ManyToOne / @OneToMany

**Files:** `SaleOrder.java`, then `Customer.java`

**Idea:** the **many** side owns the relationship: `@ManyToOne` on `SaleOrder.customer` makes Hibernate add a `customer_id` column to `sale_order`. The **one** side mirrors it: `@OneToMany(mappedBy = "customer")` on `Customer.saleOrders` — `mappedBy` means "no new column here; the other side already owns it". The `@JsonBackReference` / `@JsonManagedReference` pair stops JSON from looping customer → orders → customer → … forever.

**Fill in together:**
1. `SaleOrder.customer` — add `@ManyToOne` and `@JsonBackReference`.
2. `Customer.saleOrders` — add `@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)` and `@JsonManagedReference`.

**Try it:** run again — find `create table sale_order` in the console and look for the **`customer_id`** column that appeared from nowhere. Check it in the H2 console (<http://localhost:8080/h2-console>, JDBC URL `jdbc:h2:mem:testdb`).

**Solution:** [SaleOrder](https://github.com/cnacha-mfu/sample-boot-relation/blob/complete/src/main/java/th/mfu/SaleOrder.java) · [Customer](https://github.com/cnacha-mfu/sample-boot-relation/blob/complete/src/main/java/th/mfu/Customer.java)

---

## Step 3: Derived queries across the relationship

**File:** `SaleOrderRepository.java` (already complete — read together)

**Idea:** `findByCustomerId(Long id)` — Spring Data walks the relationship from the method name: *find orders whose customer's id is…*. No JOIN written by us. The lab asks you to declare `findByConcertId` and `deleteByConcertId` the same way.

---

## Step 4: Nested REST routes — an order lives under a customer

**File:** `SaleOrderController.java`

**Idea:** the URL mirrors the relationship: `POST /customers/{customerId}/orders`. The controller finds the parent, **sets it on the child** (`order.setCustomer(...)` — this one line creates the foreign key value), and saves. Missing parent → `404 NOT FOUND` before touching the child.

**Fill in together:**
1. `createOrder` — find customer by id → absent? `404`; else set it on the order, save, `201 CREATED`.
2. `getOrdersByCustomer` — customer absent? `404`; else `orderRepo.findByCustomerId(...)` with `200 OK`.

**Try it (Postman):** create an order under customer `10001` (201), under customer `99999` (404), list customer `10001`'s orders — the JSON shows the order but **not** the customer inside it (that's `@JsonBackReference` at work).

**Solution:** [SaleOrderController](https://github.com/cnacha-mfu/sample-boot-relation/blob/complete/src/main/java/th/mfu/SaleOrderController.java)

---

## Step 5: @DataJpaTest across two tables — how the lab grades you

**File:** `src/test/java/th/mfu/SaleOrderRepositoryTest.java` (already complete — read it together)

**Idea:** save a customer, save two orders pointing at it, then find them back **through** the relationship with `findByCustomerId` — and prove another customer sees zero. The lab's `ConcertControllerTest` verifies your Concert/Seat code the same way on every push.

**Try it:**

```
mvn test -Dtest=SaleOrderRepositoryTest
```

Run it **before** Step 2 (red — no relationship) and **after** (green).

**Solution:** [SaleOrderRepositoryTest](https://github.com/cnacha-mfu/sample-boot-relation/blob/complete/src/test/java/th/mfu/SaleOrderRepositoryTest.java)

---

## Extra credit: @ManyToMany

`SaleOrder.products` is already annotated `@ManyToMany` — one order can hold many products, one product appears in many orders. Find the **join table** (`sale_order_products`) Hibernate created in the H2 console. No extra entity class needed.

---

## After class

You are now ready for the graded lab: [lab-web-relation](https://github.com/maefahluang-uni/lab-web-relation) — a **concert ticket reservation API** built with exactly these pieces:

| Lab requirement | Demo step |
|---|---|
| `Seat` `@ManyToOne` `Concert` (CascadeType.MERGE) | Step 2 (`SaleOrder`→`Customer`) |
| `findByConcertId` / `deleteByConcertId` derived queries | Step 3 |
| `POST /concerts/{id}/seats` — find parent, set it, save, 404/201 | Step 4 |
| `GET /concerts/{id}/seats`, book a seat (200/404) | Step 4 |
| Graded by `ConcertControllerTest` (`mvn verify`) | Step 5 |

> **Tip:** the lab app runs on port **8100** (`server.port` in its properties); this demo uses 8080. If a port is already in use, stop the other server first.
