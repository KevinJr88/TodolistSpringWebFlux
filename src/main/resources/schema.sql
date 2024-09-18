CREATE TABLE IF NOT EXISTS userdata
(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50)   NOT NULL,
    last_name VARCHAR(50)  NOT NULL,
    password VARCHAR(500)  NOT NULL,
    address VARCHAR(100)  NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    roles VARCHAR [] NOT NULL,
    read_access VARCHAR [],
    edit_access VARCHAR [],
    enabled boolean DEFAULT true
);
TRUNCATE userdata RESTART IDENTITY;

INSERT INTO userdata (first_name, last_name, password, address, email,  roles)
VALUES ('Kevin','Rahadinata', 'yUIixNNy9y/I3Y3goS4IDOtklaJmm8QNAUv2cpwXOE8=', 'Jl Suryo', 'kevin777@gmail.com', '{"ROLE_USER"}');