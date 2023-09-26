create table if not exists sessoes
(
    id        bigint generated by default as identity primary key,
    sessao_id    bigint not null,
    associado_id    bigint not null unique ,
    voto varchar(3)
);