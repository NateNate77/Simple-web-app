-- Создание таблицы организаций

CREATE SEQUENCE public.auto_id_company;

ALTER SEQUENCE public.auto_id_company
    OWNER TO postgres;



CREATE TABLE public."Companies"
(
    "ID" integer NOT NULL DEFAULT nextval('auto_id_company'::regclass),
    "Name" text COLLATE pg_catalog."default",
    "HeadCompanyID" integer,
    CONSTRAINT "Companies_pkey" PRIMARY KEY ("ID"),
    CONSTRAINT "HeadCompanyID" FOREIGN KEY ("HeadCompanyID")
        REFERENCES public."Companies" ("ID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."Companies"
    OWNER to postgres;
	
	
    
    
-- Создание таблицы сотрудников  

CREATE SEQUENCE public.auto_id_staff;

ALTER SEQUENCE public.auto_id_staff
    OWNER TO postgres;
    
	
	CREATE TABLE public."Staff"
(
    "ID" integer NOT NULL DEFAULT nextval('auto_id_staff'::regclass),
    "Name" text COLLATE pg_catalog."default",
    "BossID" integer,
    "CompanyID" integer NOT NULL,
    CONSTRAINT "Staff_pkey" PRIMARY KEY ("ID"),
    CONSTRAINT "BossID" FOREIGN KEY ("BossID")
        REFERENCES public."Staff" ("ID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "CompanyID" FOREIGN KEY ("CompanyID")
        REFERENCES public."Companies" ("ID") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."Staff"
    OWNER to postgres;