create table transactions (
    id integer primary key,
    amountMicros bigint not null,
    isoCurrencyCode varchar(100) not null,
    status varchar(100) not null,
    description varchar(100)
);
