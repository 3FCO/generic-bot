DROP TABLE IF EXISTS kicks;
DROP TABLE IF EXISTS bans;
DROP TABLE IF EXISTS warnings;
DROP TABLE IF EXISTS giveaways;
DROP TABLE IF EXISTS giveaway_entries;

CREATE TABLE IF NOT EXISTS kicks(
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    user_name VARCHAR NOT NULL,
    admin_id BIGINT NOT NULL,
    admin_name VARCHAR NOT NULL,
    reason VARCHAR DEFAULT 'No reason specified'
);

CREATE TABLE IF NOT EXISTS bans(
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    user_name VARCHAR NOT NULL,
    admin_id BIGINT NOT NULL,
    admin_name VARCHAR NOT NULL,
    reason VARCHAR DEFAULT 'No reason specified',
    duration INT NOT NULL
);

CREATE TABLE IF NOT EXISTS warnings(
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    user_name VARCHAR NOT NULL,
    admin_id BIGINT NOT NULL,
    admin_name VARCHAR NOT NULL,
    reason VARCHAR DEFAULT 'No reason specified',
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS giveaways(
    id BIGINT PRIMARY KEY,
    price VARCHAR NOT NULL,
    end_time BIGINT NOT NULL,
    winner_amount INT NOT NULL,
    winners BIGINT[] DEFAULT '{}',
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS giveaway_entries(
    user_id BIGINT NOT NULL,
    giveaway_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, giveaway_id)
);
/*
INSERT INTO giveaways (id, price, end_time, winner_amount) VALUES (123, 'Mulah 1', 123123123, 3);
INSERT INTO giveaways (id, price, end_time, winner_amount) VALUES (321, 'Mulah 2', 321321321, 2);

INSERT INTO giveaway_entries (user_id, giveaway_id) VALUES (1234567890, 123);
INSERT INTO giveaway_entries (user_id, giveaway_id) VALUES (3456789000, 123);
INSERT INTO giveaway_entries (user_id, giveaway_id) VALUES (0987654321, 123);

INSERT INTO giveaway_entries (user_id, giveaway_id) VALUES (8888888888, 321);
INSERT INTO giveaway_entries (user_id, giveaway_id) VALUES (7777777777, 321);

SELECT g.id, g.price, g.end_time, g.winner_amount, array_agg(ge.user_id) as entries FROM giveaways g
LEFT JOIN giveaway_entries ge on g.id = ge.giveaway_id
WHERE g.end_time <= 3
GROUP BY g.id

SELECT id, price, end_time, winner_amount, array_agg(user_id) as entries FROM giveaways LEFT JOIN giveaway_entries on id = giveaway_id WHERE active=TRUE GROUP BY id;
*/