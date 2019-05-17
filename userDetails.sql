
DROP TABLE admin;

DROP sequence admin_key_sequence;

CREATE sequence admin_key_sequence
START WITH 1
INCREMENT BY 1
NO MAXVALUE
NO MINVALUE
CACHE 1;

ALTER TABLE admin ADD COLUMN "status" boolean DEFAULT FALSE;

CREATE TABLE admin(
        entity_id bigint DEFAULT nextval ('admin_key_sequence':: regclass) NOT NULL,
        national_id bigint NOT NULL primary key,           
        surname character varying(50) NOT NULL,
	first_name character varying(50) NOT NULL,              
        middle_name character varying(50) NOT NULL,              
        date_of_birth      date,
        gender character varying(10),
        phone character varying(120),
        email character varying(120) NOT NULL UNIQUE,
        user_name character varying(120) NOT NULL UNIQUE,
        user_password character varying(120) NOT NULL,
        user_password_encrypted character varying(120),
        picture_file character varying(120),
        bio_metric_finger_print1 character varying(120),
        bio_metric_finger_print2 character varying(120),
        admin_role character varying(120),
        details text
    );
    
CREATE TABLE User_Profile(
        entity_id integer NOT NULL primary key,            
        surname character varying(50) NOT NULL,
	first_name character varying(50) NOT NULL,              
        middle_name character varying(50),              
        date_of_birth      date,
        gender character varying(1),
        phone character varying(120),
        email character varying(120),
	nationality character(2) NOT NULL,
	nation_of_birth character(2),
	marital_status character varying(2),
        place_of_birth character varying(50),
        picture_file character varying(120),
        details text
    );