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

--For tenant_1

SET search_path = tenant_1, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: people; Type: TABLE; Schema: public; Owner: mdzahidraza
--

CREATE TABLE people (
    id bigint NOT NULL,
    enabled boolean,
    blocked_at timestamp without time zone,
    email character varying(255) NOT NULL,
    last_modified timestamp without time zone,
    mobile character varying(255),
    name character varying(255) NOT NULL,
    otp character varying(255),
    otp_sent_at timestamp without time zone,
    password character varying(255) NOT NULL,
    retry_count integer,
    role character varying(255) NOT NULL,
    username character varying(255) NOT NULL
);


ALTER TABLE people OWNER TO mdzahidraza;

--
-- Name: people_id_seq; Type: SEQUENCE; Schema: public; Owner: mdzahidraza
--

CREATE SEQUENCE people_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE people_id_seq OWNER TO mdzahidraza;

--
-- Name: people_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mdzahidraza
--

ALTER SEQUENCE people_id_seq OWNED BY people.id;


--
-- Name: people id; Type: DEFAULT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY people ALTER COLUMN id SET DEFAULT nextval('people_id_seq'::regclass);

--
-- Name: people_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mdzahidraza
--

SELECT pg_catalog.setval('people_id_seq', 1, true);


--
-- Name: people people_pkey; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY people
    ADD CONSTRAINT people_pkey PRIMARY KEY (id);


--
-- Name: people uk_sw73blrfiqs1etfk8qecdieyx; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY people
    ADD CONSTRAINT uk_sw73blrfiqs1etfk8qecdieyx UNIQUE (email);


--
-- Name: people uk_tnekt3qb2ipe5fxfdbjqxce3o; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY people
    ADD CONSTRAINT uk_tnekt3qb2ipe5fxfdbjqxce3o UNIQUE (username);


--
-- Name: idx9069oysxnf9y0ku6mrv1lrohr; Type: INDEX; Schema: public; Owner: mdzahidraza
--

CREATE INDEX idx9069oysxnf9y0ku6mrv1lrohr ON people USING btree (name, email);


--
-- PostgreSQL database dump complete
--

