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
CREATE TABLE module
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
    courseplan   BIGINT NOT NULL,
    lecturer     BIGINT NOT NULL,
    module       BIGINT NOT NULL,
    room         BIGINT NOT NULL,
    max_capacity BIGINT NOT NULL,
    start        TIME   NOT NULL,
    end          TIME   NOT NULL,
    weekday      TIME   NOT NULL,
    FOREIGN KEY (module) REFERENCES module (id),
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

insert into users(usrname, firstname, email, matr_nr, user_type)
values ('am', 'amatus', 'amatus@test.de', 10000, 'STUDENT');
