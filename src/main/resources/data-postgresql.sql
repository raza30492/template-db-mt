
--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: mdzahidraza
--

INSERT INTO role (id, description, name) VALUES
(1,	'Super user for Tennant management',	'ROLE_MASTER'),
(2,	'Admin User For Tenants',	'ROLE_ADMIN');



--
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mdzahidraza
--

SELECT pg_catalog.setval('role_id_seq', 2, true);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: mdzahidraza
--

INSERT INTO users (id, created_at, enabled, updated_at, account_expired, account_locked, credential_expired, email, mobile, name, otp, otp_sent_at, password, retry_count, username, company_id) VALUES
(1,	'2017-06-28 00:25:42.25',	true,	'2017-06-28 00:25:42.25',	false,	false,	false,	'zahid7292@gmail.com', '8987525008',	'Md Zahid Raza',	NULL,	NULL,	'$2a$10$8gha167uTM3jhArZwFk6UOxgNChnMhWfrR59ND/TtzvTynwo2129C',	NULL,	'zahid7292',	NULL);



--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mdzahidraza
--

SELECT pg_catalog.setval('users_id_seq', 1, true);

--
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: mdzahidraza
--

INSERT INTO user_role (user_id, role_id) VALUES
(1,	1);




--
-- Data for Name: url_interceptor; Type: TABLE DATA; Schema: public; Owner: mdzahidraza
--

INSERT INTO url_interceptor (id, access, http_method, url) VALUES
(1,	'ROLE_MASTER',	'POST', '/api/users'),
(2,	'ROLE_MASTER',	'GET',	'/api/users'),
(3,	'ROLE_MASTER',	'GET',	'/api/users/{\\d+}'),
(4,	'ROLE_MASTER',	'PUT',	'/api/users/{\\d+}'),
(5,	'ROLE_MASTER',	'PATCH',	'/api/users/{\\d+}'),
(6,	'ROLE_MASTER',	'DELETE',	'/api/users/{\\d+}'),
(7,	'ROLE_MASTER',	'POST', '/api/roles'),
(8,	'ROLE_MASTER',	'GET',	'/api/roles'),
(9,	'ROLE_MASTER',	'GET',	'/api/roles/{\\d+}'),
(10,	'ROLE_MASTER',	'PUT',	'/api/roles/{\\d+}'),
(11,	'ROLE_MASTER',	'PATCH',	'/api/roles/{\\d+}'),
(12,	'ROLE_MASTER',	'DELETE',	'/api/roles/{\\d+}'),
(13,	'ROLE_MASTER',	'POST', '/api/companies'),
(14,	'ROLE_MASTER',	'GET',	'/api/companies'),
(15,	'ROLE_MASTER',	'GET',	'/api/companies/{\\d+}'),
(16,	'ROLE_MASTER',	'PUT',	'/api/companies/{\\d+}'),
(17,	'ROLE_MASTER',	'PATCH',	'/api/companies/{\\d+}'),
(18,	'ROLE_MASTER',	'DELETE',	'/api/companies/{\\d+}'),
(19,	'ROLE_MASTER',	'POST', '/api/interceptors'),
(20,	'ROLE_MASTER',	'GET',	'/api/interceptors'),
(21,	'ROLE_MASTER',	'GET',	'/api/interceptors/{\\d+}'),
(22,	'ROLE_MASTER',	'PUT',	'/api/interceptors/{\\d+}'),
(23,	'ROLE_MASTER',	'PATCH',	'/api/interceptors/{\\d+}'),
(24,	'ROLE_MASTER',	'DELETE',	'/api/interceptors/{\\d+}');

--
-- Name: url_interceptor_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mdzahidraza
--

SELECT pg_catalog.setval('url_interceptor_id_seq', 25, false);

--
-- PostgreSQL database dump complete
--