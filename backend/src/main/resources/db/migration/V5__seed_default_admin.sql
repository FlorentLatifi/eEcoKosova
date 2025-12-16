-- Seed default admin user (password: admin123)
-- Password hash: BCrypt hash for "admin123"
-- To generate: BCrypt.hashpw("admin123", BCrypt.gensalt())

IF NOT EXISTS (SELECT * FROM [dbo].[Users] WHERE username = 'admin')
BEGIN
    INSERT INTO [dbo].[Users] (id, username, email, passwordHash, role, enabled, createdAt, lastUpdated)
    VALUES (
        'admin-001',
        'admin',
        'admin@ecokosova.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- admin123
        'ADMIN',
        1,
        GETDATE(),
        GETDATE()
    );
END;

IF NOT EXISTS (SELECT * FROM [dbo].[Users] WHERE username = 'user')
BEGIN
    INSERT INTO [dbo].[Users] (id, username, email, passwordHash, role, enabled, createdAt, lastUpdated)
    VALUES (
        'user-001',
        'user',
        'user@ecokosova.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- admin123 (same for demo)
        'USER',
        1,
        GETDATE(),
        GETDATE()
    );
END;

