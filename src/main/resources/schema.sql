-- drop table requests;
-- drop table comments;
-- drop table bookings;
-- drop table items;
-- drop table users;

create table if not exists users
(
    user_id    bigint generated always as identity (maxvalue 2147483647)
        constraint pk_user
            primary key,
    user_name  varchar(255) not null,
    user_email varchar(512) not null
        constraint uq_user_email
            unique
);

create table if not exists items
(
    item_id          bigint generated always as identity (maxvalue 2147483647)
        primary key,
    owner_id         bigint        not null
        constraint items_users_user_id_fk
            references users,
    item_name        varchar(1000) not null,
    item_description varchar(1000),
    item_available   boolean,
    request_id       bigint
);

create table if not exists bookings
(
    booking_id bigint generated always as identity (maxvalue 2147483647)
        constraint bookings_pk
            primary key,
    start_date timestamp,
    end_date   timestamp,
    item_id    bigint not null
        constraint bookings_items_item_id_fk
            references items
            on delete cascade,
    booker_id  bigint not null
        constraint foreign_key_name
            references users,
    status     varchar(50)
);

create table if not exists requests
(
    request_id   bigint generated always as identity (maxvalue 2147483647)
        constraint key_name
            primary key,
    description  varchar not null,
    requestor_id bigint  not null
        constraint requests_users_user_id_fk
            references users
            on delete cascade,
    created      timestamp
);

create table if not exists comments
(
    comment_id   bigint generated always as identity (maxvalue 2147483647)
        constraint comments_pk
            primary key,
    comment_text varchar not null,
    item_id      bigint,
    author_id    bigint,
    created      timestamp
);