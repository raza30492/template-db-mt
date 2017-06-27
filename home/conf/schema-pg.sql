--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.3
-- Dumped by pg_dump version 9.6.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: product; Type: TABLE; Schema: public; Owner: mdzahidraza
--

CREATE TABLE product (
    id bigint NOT NULL,
    created_at timestamp without time zone,
    enabled boolean,
    updated_at timestamp without time zone,
    description character varying(255),
    name character varying(255)
);


ALTER TABLE product OWNER TO mdzahidraza;

--
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: mdzahidraza
--

CREATE SEQUENCE product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE product_id_seq OWNER TO mdzahidraza;

--
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mdzahidraza
--

ALTER SEQUENCE product_id_seq OWNED BY product.id;


--
-- Name: product id; Type: DEFAULT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY product ALTER COLUMN id SET DEFAULT nextval('product_id_seq'::regclass);

--
-- Name: product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mdzahidraza
--

SELECT pg_catalog.setval('product_id_seq', 1, false);

--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);

