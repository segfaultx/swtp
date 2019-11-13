--liquibase formatted sql

--changeset amatus:1

CREATE TABLE users
(
    usrname   VARCHAR(255) primary key,
    firstname VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL,
    matr_nr   BIGINT,
    user_type VARCHAR(255) NOT NULL
);
CREATE TABLE pos
(
    id            BIGINT primary key,
    year          DATE         NOT NULL,
    degree_course VARCHAR(255) NOT NULL
);
CREATE TABLE rooms
(
    id       BIGINT primary key,
    room_nr  BIGINT       NOT NULL,
    location VARCHAR(255) NOT NULL
);
CREATE TABLE modules
(
    id          BIGINT PRIMARY KEY,
    module_name VARCHAR(255) NOT NULL,
    assigned_po BIGINT       NOT NULL,
    FOREIGN KEY (assigned_po) REFERENCES pos (id)
);
CREATE TABLE courseplans
(
    id BIGINT PRIMARY KEY
);
CREATE TABLE appointments
(
    id           BIGINT PRIMARY KEY,
    type         VARCHAR(255) NOT NULL,
    courseplan   BIGINT       NOT NULL,
    lecturer     VARCHAR(255) NOT NULL,
    module       BIGINT       NOT NULL,
    room         BIGINT       NOT NULL,
    max_capacity BIGINT,
    start        TIME         NOT NULL,
    end          TIME         NOT NULL,
    weekday      INTEGER      NOT NULL,
    FOREIGN KEY (module) REFERENCES modules (id),
    FOREIGN KEY (courseplan) REFERENCES courseplans (id),
    FOREIGN KEY (lecturer) REFERENCES users (usrname),
    FOREIGN KEY (room) REFERENCES rooms (id)
);
CREATE TABLE user_appointments
(
    user_id        BIGINT NOT NULL,
    appointment_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (usrname),
    FOREIGN KEY (appointment_id) REFERENCES appointments (id)
);

--changeset amatus:2


-- testusers
insert into users(usrname, firstname, email, matr_nr, user_type)
values ('am', 'amatus', 'amatus@test.de', 10000, 'STUDENT');
insert into users(usrname, firstname, email, matr_nr, user_type)
values ('bm', 'bmatus', 'bmatus@test.de', 10001, 'STUDENT');
insert into users(usrname, firstname, email, matr_nr, user_type)
values ('ta', 'Testlecturer', 'text@lecturer.com', null, 'LECTURER');

-- testpo

insert into pos(id, year, degree_course)
values (1, '2017-01-01', 'Medieninformatik');

-- testplan

insert into courseplans(id)
values (1);

-- modules

insert into modules(id, module_name, assigned_po)
values (1, 'Analysis', 1);
insert into modules(id, module_name, assigned_po)
values (2, 'Einführung in die Gestaltung', 1);
insert into modules(id, module_name, assigned_po)
values (3, 'Einführung in die Medieninformatik', 1);
insert into modules(id, module_name, assigned_po)
values (4, 'Grundlagen der Betriebswirtschaftslehre', 1);
insert into modules(id, module_name, assigned_po)
values (5, 'Programmieren 1', 1);

-- rooms

insert into rooms(id, room_nr, location)
values (1, 14, 'D');
insert into rooms(id, room_nr, location)
values (2, 11, 'D');
insert into rooms(id, room_nr, location)
values (3, 12, 'D');
insert into rooms(id, room_nr, location)
values (4, 13, 'D');
insert into rooms(id, room_nr, location)
values (5, 17, 'D');

-- first appointments

insert into appointments(id, type, courseplan, lecturer, module, room, max_capacity, start, end, weekday)
values (1, 'LECTURE', 1, 'ta', 1, 1, null, '08:00:00', '10:00:00', 1);
insert into appointments(id, type, courseplan, lecturer, module, room, max_capacity, start, end, weekday)
values (2, 'LECTURE', 1, 'ta', 2, 2, null, '10:00:00', '11:30:00', 2);
insert into appointments(id, type, courseplan, lecturer, module, room, max_capacity, start, end, weekday)
values (3, 'LECTURE', 1, 'ta', 3, 3, null, '11:30:00', '13:15:00', 3);
insert into appointments(id, type, courseplan, lecturer, module, room, max_capacity, start, end, weekday)
values (4, 'LECTURE', 1, 'ta', 4, 4, null, '15:45:00', '17:30:00', 4);
insert into appointments(id, type, courseplan, lecturer, module, room, max_capacity, start, end, weekday)
values (5, 'LECTURE', 1, 'ta', 5, 5, null, '08:00:00', '10:00:00', 5);