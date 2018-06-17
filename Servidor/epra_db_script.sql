create table if not exists utilizadores
(
		nome      VARCHAR(255) NOT NULL,
		telemovel VARCHAR(20) NOT NULL unique,
		email     VARCHAR(255),
		password  VARCHAR(255) NOT NULL,
		admin     BOOLEAN NOT NULL DEFAULT 0,
		privilegios VARCHAR(255), -- exemplo: 1,2,7 --> são os id's das áreas que ele tem permissão
		pin 	  INT(4) NOT NULL,
		PRIMARY KEY (email)
);

CREATE TABLE IF NOT EXISTS devices
(
	nome VARCHAR(255) NOT NULL, -- exemplo: pir1, piezzo1, led1, led2, pir2, ...
	tipo ENUM('Sensor','Actuator','Simulator') NOT NULL,
	ligado BOOLEAN NOT NULL DEFAULT 0,
	PRIMARY KEY (nome)
);

CREATE TABLE IF NOT EXISTS areas
(
	id INT(11) NOT NULL AUTO_INCREMENT,
	nome VARCHAR(255) NOT NULL UNIQUE,
	alarmeLigado BOOLEAN NOT NULL DEFAULT 0, -- saber se está ativa ou não (0 = false)
	sensor VARCHAR(255) NOT NULL, -- sensor associado à área
	FOREIGN KEY (sensor) REFERENCES devices(nome),
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS historico
(
	id INT(11) NOT NULL AUTO_INCREMENT,
	evento VARCHAR(255) NOT NULL, -- exemplo: "Alarme disparado"
	datahora DATETIME NOT NULL DEFAULT NOW(),
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS simuladores
(
	id INT(11) NOT NULL AUTO_INCREMENT,
	nome VARCHAR(255) NOT NULL UNIQUE,
	estadoAtual BOOLEAN NOT NULL DEFAULT 0,
	atuador VARCHAR(255) NOT NULL,
	areaId INT(11) NOT NULL,
	FOREIGN KEY (atuador) REFERENCES devices(nome),
	FOREIGN KEY (areaId) REFERENCES areas(id),
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS alarme
(
	estadoAtual BOOLEAN NOT NULL DEFAULT 0,
	tempoAtividade TIMESTAMP NOT NULL DEFAULT NOW(),
	intensidade INT(10) NOT NULL DEFAULT 1000,
	intervalo INT(10) NOT NULL DEFAULT 1,
	tempoDeToque INT(10) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS tracking
(
	id INT(11) NOT NULL AUTO_INCREMENT,
	areaAtual INT(11) NOT NULL,
	areaEntrada INT(11) NOT NULL,
	pessoasNotificadas INT(11) NOT NULL,
	FOREIGN KEY (areaAtual) REFERENCES areas(id),
	FOREIGN KEY (areaEntrada) REFERENCES areas(id),
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS historicoTracking
(
	area INT(11) NOT NULL,
	duracao INT(11) NOT NULL,
	trackingId INT(11) NOT NULL,
	FOREIGN KEY (area) REFERENCES areas(id),
	FOREIGN KEY (trackingId) REFERENCES tracking(id)
);

CREATE TABLE IF NOT EXISTS device_tokens
(
	token VARCHAR(255) NOT NULL,
	PRIMARY KEY (token)
);

/* insert into utilizador(email, password) values("admin", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918"); */
