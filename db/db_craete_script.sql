create table transactions
(
    id               int auto_increment
        primary key,
    transaction_date date        not null,
    amount           double      not null,
    from_currency    varchar(10) not null,
    to_currency      varchar(10) not null,
    total_amount     double      not null
);