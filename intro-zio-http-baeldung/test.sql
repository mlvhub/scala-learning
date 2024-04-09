create schema simple_api;

create table recipes (
    id integer primary key,
    name text not null,
    ingredients text[] not null
);

insert into recipes (id, name, ingredients) values (1, 'Pancakes', '{"flour", "milk", "eggs"}');
insert into recipes (id, name, ingredients) values (2, 'Pizza', '{"flour", "tomato", "cheese"}');
insert into recipes (id, name, ingredients) values (3, 'Cake', '{"flour", "milk", "eggs", "sugar"}');

