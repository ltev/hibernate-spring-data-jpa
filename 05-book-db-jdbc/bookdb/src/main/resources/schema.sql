drop table if exists book;

create table book (
                      id bigint not null auto_increment,
                      title varchar(255),
                      publisher varchar(255),
                      isbn varchar(255),
                      author_id bigint,
                      primary key (id)
) engine=InnoDB