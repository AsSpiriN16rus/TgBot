CREATE TABLE IF NOT EXISTS subscribers
(
    uuid character varying(250) NOT NULL,
    id character varying(250) NOT NULL,
    price numeric,
    last_notification character varying(250) ,
    CONSTRAINT subscribers_pkey PRIMARY KEY (uuid)
);
