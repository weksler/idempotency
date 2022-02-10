alter table idempotency_records
add column updatedAt bigint;

create index updatedAtIdempotencyRecords on idempotency_records (updatedAt);
