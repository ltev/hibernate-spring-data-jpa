drop table if exists book;
drop table if exists author;

create table book (
                      id bigint not null auto_increment,
                      title varchar(255),
                      publisher varchar(255),
                      isbn varchar(255),
                      author_id bigint,
                      primary key (id)
) engine=InnoDB;

create table author (
                      id bigint not null auto_increment,
                      first_name varchar(255),
                      last_name varchar(255),
                      primary key (id)
) engine=InnoDB;