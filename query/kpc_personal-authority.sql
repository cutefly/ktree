CREATE TABLE public.kpc_personal_authority (
	seq int8 NOT NULL,
	employe_id varchar(20) NULL,
	authority_level int4 NULL,
	create_date timestamp NULL,
	CONSTRAINT kpc_personal_authority_pkey PRIMARY KEY (seq)
);