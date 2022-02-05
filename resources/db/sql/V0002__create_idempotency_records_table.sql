create table idempotency_records (
    idempotencyKey varchar(100) not null primary key,
    idempotencyRecord blob(1000) not null
);
