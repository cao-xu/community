# Sword 社区

## 资料
[Spring文档](https://spring.io/guides)  
[Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)  
[elasticsearch中文社区](https://elasticsearch.cn/)  
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)  


## 工具
[Git工具](https://git-scm.com/)

## 脚本
```sql
create schema community collate gb2312_chinese_ci;

create table user
(
	id int auto_increment
		primary key,
	account_id varchar(100) null,
	name varchar(50) null,
	token char(36) null,
	gmt_create bigint null,
	gmt_modified bigint null
);


```