INSERT INTO roles (name) VALUES
    ('PRODUCT_OWNER'),
    ('SCRUM_MASTER'),
    ('DEVELOPER')
ON CONFLICT (name) DO NOTHING;