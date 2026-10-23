CREATE TABLE condominios (
    id BIGSERIAL CONSTRAINT condominios_pkey PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    condominio_bloco VARCHAR(100)
);

CREATE TABLE usuarios (
    id BIGSERIAL CONSTRAINT usuarios_pkey PRIMARY KEY,
    condominio VARCHAR(100),
    email VARCHAR(150) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    CONSTRAINT uk_kfsp0s1tflm1cwlj8idhqsad0 UNIQUE (email)
);

CREATE TABLE unidades (
    id BIGSERIAL CONSTRAINT unidades_pkey PRIMARY KEY,
    andar VARCHAR(20),
    bloco VARCHAR(20) NOT NULL,
    condominio_nome VARCHAR(150),
    garagem VARCHAR(10),
    numero VARCHAR(20) NOT NULL,
    situacao VARCHAR(50),
    condominio_id BIGINT,
    CONSTRAINT fks03ncp5n77mjqxhbt162qschk
        FOREIGN KEY (condominio_id) REFERENCES condominios(id)
);

CREATE TABLE moradores (
    id BIGSERIAL CONSTRAINT moradores_pkey PRIMARY KEY,
    bloco VARCHAR(20),
    condominio_nome VARCHAR(150),
    cpf VARCHAR(14),
    data_cadastro VARCHAR(20),
    email VARCHAR(150),
    moradores_unidade INTEGER,
    nome VARCHAR(100) NOT NULL,
    numero VARCHAR(20),
    situacao VARCHAR(50),
    telefone VARCHAR(20),
    tipo VARCHAR(50),
    unidade VARCHAR(20),
    vagas_garagem VARCHAR(10),
    condominio_id BIGINT,
    CONSTRAINT uk_1pga1fiak1swwsqbxl328q2qr UNIQUE (cpf),
    CONSTRAINT fkt35jod1fqrivxgp8or253klxy
        FOREIGN KEY (condominio_id) REFERENCES condominios(id)
);

CREATE TABLE ocorrencias (
    id BIGSERIAL CONSTRAINT ocorrencias_pkey PRIMARY KEY,
    condominio_nome VARCHAR(150),
    data VARCHAR(20),
    local VARCHAR(100),
    responsavel VARCHAR(100),
    status VARCHAR(50),
    titulo VARCHAR(200) NOT NULL,
    condominio_id BIGINT,
    CONSTRAINT fkclpa3vxrrd6g1l3j6bqygh85y
        FOREIGN KEY (condominio_id) REFERENCES condominios(id)
);

CREATE TABLE reservas (
    id BIGSERIAL CONSTRAINT reservas_pkey PRIMARY KEY,
    area VARCHAR(100) NOT NULL,
    area_info VARCHAR(200),
    condominio_nome VARCHAR(150),
    data VARCHAR(20),
    finalidade VARCHAR(200),
    horario VARCHAR(20),
    solicitante VARCHAR(100) NOT NULL,
    status VARCHAR(50),
    condominio_id BIGINT,
    CONSTRAINT fkgbafcrgwj2f8etsg2ejtn21m
        FOREIGN KEY (condominio_id) REFERENCES condominios(id)
);
