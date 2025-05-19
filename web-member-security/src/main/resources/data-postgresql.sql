INSERT INTO member(id, name, email, password) VALUES(1, '관리자', 'admin@hanbit.co.kr', '$2a$12$qpOpJ13qb.ROkaYvteEKs.mWhls9lTnGpjLj9h3Gpw0/8Y8r1MR1C') ON CONFLICT (email) DO NOTHING;
INSERT INTO authority(id, authority, member_id) VALUES(1, 'ROLE_ADMIN', 1) ON CONFLICT (id) DO NOTHING;
