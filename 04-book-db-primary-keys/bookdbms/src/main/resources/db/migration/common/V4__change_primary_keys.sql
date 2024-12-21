alter table book modify column id BIGINT auto_increment;
alter table author change id id BIGINT auto_increment;

drop table book_seq;
drop table author_seq;