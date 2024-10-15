    create table bookings (
        id integer not null auto_increment,
        created_at datetime(6),
        date date not null,
        updated_at datetime(6),
        office_id integer not null,
        user_id integer not null,
        primary key (id)
    ) engine=InnoDB;

    alter table bookings
       add constraint bookings_FK_01_officeId
       foreign key (office_id)
       references offices (id);

    alter table bookings
       add constraint bookings_FK_02_userId
       foreign key (user_id)
       references users (id);
