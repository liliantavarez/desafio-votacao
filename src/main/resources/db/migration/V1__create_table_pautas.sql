create table if not exists pautas
(
    id        bigint generated by default as identity primary key,
    titulo    varchar(200) not null,
    descricao text
);

