DROP DATABASE IF EXISTS furini;

CREATE DATABASE IF NOT EXISTS furini;
use furini;

CREATE TABLE IF NOT EXISTS produto (
    id INT NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(45) NOT NULL,
    valor DOUBLE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cliente (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(120),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS notafiscal (
    id INT NOT NULL AUTO_INCREMENT,
    dataEmissao DATE NOT NULL,
    cliente_id INT NOT NULL, 
    PRIMARY KEY (id),
    FOREIGN KEY (cliente_id) REFERENCES cliente (id)
);

CREATE TABLE IF NOT EXISTS itemnota (
    id INT NOT NULL AUTO_INCREMENT,
    quantidade INT NOT NULL,
    valorItem DOUBLE,
    produto_id INT NOT NULL,
    notaFiscal_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (produto_id) REFERENCES produto (id),
    FOREIGN KEY (notaFiscal_id) REFERENCES notafiscal (id)    
);

INSERT INTO cliente (nome, endereco) VALUES
('João Silva', 'Rua A, 123'),
('Maria Oliveira', 'Rua B, 456'),
('Pedro Santos', 'Rua C, 789'),
('Ana Souza', 'Rua D, 101'),
('Carlos Pereira', 'Rua E, 202'),
('Fernanda Costa', 'Rua F, 303'),
('Ricardo Nunes', 'Rua G, 404'),
('Juliana Almeida', 'Rua H, 505'),
('Gustavo Martins', 'Rua I, 606'),
('Larissa Monteiro', 'Rua J, 707'),
('Bruno Fernandes', 'Rua K, 808'),
('Isabela Ferreira', 'Rua L, 909'),
('Lucas Rocha', 'Rua M, 1010'),
('Gabriela Mendes', 'Rua N, 1111'),
('Marcos Lima', 'Rua O, 1212');

INSERT INTO produto (descricao, valor) VALUES
('Teclado Mecânico', 350.00),
('Mouse Gamer', 250.00),
('Monitor Full HD', 1200.00),
('Cadeira Gamer', 800.00),
('Headset Bluetooth', 400.00),
('Notebook 16GB RAM', 4500.00),
('Impressora Multifuncional', 600.00),
('Mesa para Computador', 450.00),
('Pen Drive 32GB', 50.00),
('HD Externo 1TB', 350.00),
('Fonte 600W', 300.00),
('Placa de Vídeo 8GB', 3500.00),
('Processador 6 Núcleos', 2000.00),
('Teclado Sem Fio', 150.00),
('Webcam HD', 250.00);

