CREATE TABLE public.kpc_employe (
	seq int8 NOT NULL,
	employe_id varchar(20) NOT NULL,
	name varchar(30) NOT NULL,
	pwd varchar(20) NOT NULL,
	authority_code varchar(20) NOT NULL,
	auth_level int4 DEFAULT 0 NOT NULL,
	create_date timestamp DEFAULT now() NULL,
	use_yn varchar(1) NULL,
	team_code int4 NULL,
	division_code int4 NULL,
	position int4 DEFAULT 0 NOT NULL,
	confirm1 varchar(50) NULL,
	confirm2 varchar(50) NULL
);
