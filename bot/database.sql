DROP TABLE IF EXISTS kicks;
DROP TABLE IF EXISTS bans;
DROP TABLE IF EXISTS warnings;

CREATE TABLE IF NOT EXISTS kicks(
    id SERIAL PRIMARY KEY,
    user_id VARCHAR NOT NULL,
    user_name VARCHAR NOT NULL,
    admin_id VARCHAR NOT NULL,
    admin_name VARCHAR NOT NULL,
    reason VARCHAR DEFAULT 'No reason specified'
);

CREATE TABLE IF NOT EXISTS bans(
    id SERIAL PRIMARY KEY,
    user_id VARCHAR NOT NULL,
    user_name VARCHAR NOT NULL,
    admin_id VARCHAR NOT NULL,
    admin_name VARCHAR NOT NULL,
    reason VARCHAR DEFAULT 'No reason specified',
    duration INT NOT NULL
);

CREATE TABLE IF NOT EXISTS warnings(
    id SERIAL PRIMARY KEY,
    user_id VARCHAR NOT NULL,
    user_name VARCHAR NOT NULL,
    admin_id VARCHAR NOT NULL,
    admin_name VARCHAR NOT NULL,
    reason VARCHAR DEFAULT 'No reason specified',
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS giveaways(

);