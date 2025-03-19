INSERT INTO users (user_name, password, role_id) VALUES
    ('developer', '$2a$10$tIeEa7c96MgMgOQlD/5K/O3pTK/sPSMa/FSXeFNKXPBoqvsUzH25y', 
        (SELECT id FROM roles WHERE name = 'DEVELOPER')),
    ('product_owner', '$2a$10$teoKd8onoLPBywyPT0xgNOcY7hDsV27Ot7zLifqAtWfx8LXgN54eW', 
        (SELECT id FROM roles WHERE name = 'PRODUCT_OWNER')),
    ('scrum_master', '$2a$10$igLjHxr5.gbm./ShoZZDxO7ul0ixNpcWFSiHZgzcHJwaZJjawqrMO', 
        (SELECT id FROM roles WHERE name = 'SCRUM_MASTER'));
