create table utilizador
(
		nome      varchar(255),
		morada    varchar(255),
		telemovel varchar(50),	
		email     varchar(255) primary key,
		password  varchar(255) not null
);
insert into utilizador(email, password) values("admin", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918");