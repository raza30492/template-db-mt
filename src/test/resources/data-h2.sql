INSERT INTO role (id, description, name) VALUES
(1,	'Super user for Tennant management',	'ROLE_MASTER'),
(2,	'Admin User For Tenants',	'ROLE_ADMIN');

INSERT INTO users (id, modified_at, enabled, account_expired, account_locked, credential_expired, email, mobile, name, otp, otp_sent_at, password, retry_count, username, company_id) VALUES
(1,	'2017-06-28 00:25:42.25',	true, false,	false,	false,	'zahid7292@gmail.com', '8987525008',	'Md Zahid Raza',	NULL,	NULL,	'$2a$10$8gha167uTM3jhArZwFk6UOxgNChnMhWfrR59ND/TtzvTynwo2129C',	NULL,	'zahid7292',	NULL),
(2,	'2017-06-28 00:25:42.25',	true, false,	false,	false,	'taufeeque@gmail.com', '8987525008',	'Md Taufeeque Alam',	NULL,	NULL,	'$2a$10$8gha167uTM3jhArZwFk6UOxgNChnMhWfrR59ND/TtzvTynwo2129C',	NULL,	'taufeeque8',	NULL),
(3,	'2017-06-28 00:25:42.25',	true, false,	false,	false,	'jawed.akhtar1993@gmail.com', '8987525008',	'Md Jawed Akhtar',	NULL,	NULL,	'$2a$10$8gha167uTM3jhArZwFk6UOxgNChnMhWfrR59ND/TtzvTynwo2129C',	NULL,	'jawed_akhtar',	NULL);

INSERT INTO user_role (user_id, role_id) VALUES
(1,	1),
(2, 1),
(3, 1);

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
(24,	'ROLE_MASTER',	'DELETE',	'/api/interceptors/{\\d+}'),
(25,	'ROLE_ADMIN',	'POST', '/api/products'),
(26,	'ROLE_ADMIN',	'GET',	'/api/products'),
(27,	'ROLE_ADMIN',	'GET',	'/api/products/{\\d+}'),
(28,	'ROLE_ADMIN',	'PUT',	'/api/products/{\\d+}'),
(29,	'ROLE_ADMIN',	'PATCH',	'/api/products/{\\d+}'),
(30,	'ROLE_ADMIN',	'DELETE',	'/api/products/{\\d+}'),
(31,	'ROLE_MASTER',	'GET', '/metrics'),
(32,	'ROLE_MASTER',	'GET',	'/auditevents');