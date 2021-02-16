-- Create t_users
create table t_users
(
    id bigint not null auto_increment,
    username varchar(30) not null,
    firstname varchar(50) not null,
    lastname varchar(50) not null,
    email varchar(120) not null,
    address varchar(500) not null,
    primary key(id)
);

-- Create t_users_seq
create table t_users_seq
(
    next_val bigint not null
)
