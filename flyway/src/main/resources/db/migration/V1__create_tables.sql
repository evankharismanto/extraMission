create table m_vehicle(
    id int unsigned not null auto_increment,
    form varchar(50),
    brand varchar(50),
    model varchar(50),
    color varchar(50),
    power decimal,
    powunit varchar(20),
    primary key(id)
);