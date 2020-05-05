create table comment
(
	id bigint auto_increment,
	parent_id bigint not null comment '父类ID',
	type int not null comment '父类类型',
	commentator bigint null,
	content varchar(1024) not null,
	gmt_create bigint not null,
	gmt_modified bigint not null,
	like_count bigint default 0 null,
	constraint comment_pk
		primary key (id)
);

