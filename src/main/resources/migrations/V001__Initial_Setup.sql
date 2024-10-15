

create table offices (
    id integer not null auto_increment,
    capacity integer not null,
    created_at datetime(6),
    location varchar(255) not null,
    name varchar(255) not null,
    updated_at datetime(6),
    primary key (id)
);

create table users (
    id integer not null auto_increment,
    created_at datetime(6),
    email varchar(100) not null,
    full_name varchar(255) not null,
    password varchar(255) not null,
    role enum ('Admin','Employee') not null,
    updated_at datetime(6),
    primary key (id)
);

alter table users
    add constraint users_AK_01_unique_mail unique (email)