-- t_employers
create table t_employers
(
   id integer not null,
   name varchar(255) not null,
   address varchar(1000) not null,
   rating integer not null,
   primary key(id)
);

-- t_job_loc
create table t_job_loc
(
   id integer not null,
   eid integer not null,
   location varchar(255) not null,
   primary key(id)
);

-- t_job_types
create table t_job_types
(
   id integer not null,
   job_type varchar(128) not null,
   primary key(id)
);

-- t_skills
create table t_skills
(
   id integer not null,
   desc varchar(128) not null,
   primary key(id)
);

-- t_jobs
create table t_jobs
(
   id integer not null,
   loc_id integer not null,
   job_type_id integer not null,
   skills varchar(1000) not null,
   availability varchar(32) not null,
   posted_on date not null,
   wage_per_hr varchar(8) not null,
   job_desc varchar(4000) not null,
   tags varchar(255),
   primary key(id)
);