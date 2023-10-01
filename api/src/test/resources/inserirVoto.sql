INSERT INTO associados (id,nome, cpf)
VALUES (1,'Jéssica Márcia Teixeira','27476986296');
INSERT INTO associados (id,nome, cpf)
VALUES (2,'Laís Vanessa Lopes','54063273806');

INSERT INTO pautas (id, titulo)
VALUES (1, 'Novas funcionalidades');
INSERT INTO pautas (id, titulo)
VALUES (2, 'Planejamento segundo semestre');

INSERT INTO sessoes (id, data_abertura, data_encerramento, status_sessao, pauta_id)
VALUES (1, '2023-09-26 13:43:00', '2030-09-26 15:43:03',  'ABERTA', 1);
INSERT INTO sessoes (id, data_abertura, data_encerramento, status_sessao, pauta_id)
VALUES (2, '2023-09-26 13:43:00', '2030-06-26 15:43:03','ABERTA', 2);

INSERT INTO votos (id, voto, associado_id, sessao_id)
VALUES (1, 'SIM', 1, 1);
INSERT INTO votos (id, voto, associado_id, sessao_id)
VALUES (2, 'SIM', 2, 1);

INSERT INTO votos (id, voto, associado_id, sessao_id)
VALUES (3, 'NAO', 1, 2);
INSERT INTO votos (id, voto, associado_id, sessao_id)
VALUES (4, 'NAO', 2, 2);
