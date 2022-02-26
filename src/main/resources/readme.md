Script to crate table before run the app

CREATE TABLE academy_awards (
    id bigint NOT NULL AUTO_INCREMENT,
    year varchar(20) NOT NULL,
    category varchar(255) NOT NULL,
    nominee varchar(1000) NOT NULL,
    additional_info varchar(1000) NOT NULL,
    awarded varchar(255) NOT NULL,
    rating double NOT NULL DEFAULT 0,
    votes_number bigint NOT NULL DEFAULT 0,
    box_office int NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);