-- t_books
create table t_books
(
   id integer auto_increment primary key,
   name varchar(255) not null,
   author varchar(255) not null,
   price double not null,
   updated_on timestamp not null default current_timestamp on update current_timestamp
);