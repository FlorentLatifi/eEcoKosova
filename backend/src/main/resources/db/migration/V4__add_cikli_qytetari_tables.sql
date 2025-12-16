-- Migration për CikliMbledhjes dhe Qytetari tables

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Qytetaret]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[Qytetaret] (
        [id]           NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [name]         NVARCHAR(200) NOT NULL,
        [address]      NVARCHAR(500) NULL,
        [createdAt]    DATETIME2     NOT NULL,
        [lastUpdated]  DATETIME2     NOT NULL
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[CikletMbledhjes]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[CikletMbledhjes] (
        [id]               NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [scheduleTime]      DATETIME2     NOT NULL,
        [maxCapacity]       INT           NOT NULL,
        [zoneId]           NVARCHAR(50)  NOT NULL,
        [kamioniId]        NVARCHAR(50)  NULL,
        [status]           NVARCHAR(50)  NOT NULL,
        [createdAt]        DATETIME2     NOT NULL,
        [lastUpdated]      DATETIME2     NOT NULL,
        CONSTRAINT FK_CikletMbledhjes_Zones FOREIGN KEY (zoneId)
            REFERENCES [dbo].[Zones] (id)
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[CikliCollectionDays]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[CikliCollectionDays] (
        [cikliId]      NVARCHAR(50)  NOT NULL,
        [dayOfWeek]    NVARCHAR(20)  NOT NULL,
        PRIMARY KEY ([cikliId], [dayOfWeek]),
        CONSTRAINT FK_CikliCollectionDays_Ciklet FOREIGN KEY (cikliId)
            REFERENCES [dbo].[CikletMbledhjes] (id) ON DELETE CASCADE
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[Users] (
        [id]           NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [username]     NVARCHAR(100) NOT NULL UNIQUE,
        [email]        NVARCHAR(200) NOT NULL UNIQUE,
        [passwordHash] NVARCHAR(255) NOT NULL,
        [role]         NVARCHAR(50)  NOT NULL DEFAULT 'USER',
        [enabled]      BIT           NOT NULL DEFAULT 1,
        [createdAt]    DATETIME2     NOT NULL,
        [lastUpdated]   DATETIME2     NOT NULL
    );
END;

-- Indexes
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_qytetaret_name' AND object_id = OBJECT_ID('dbo.Qytetaret'))
BEGIN
    CREATE INDEX idx_qytetaret_name ON dbo.Qytetaret(name);
END;

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_ciklet_zoneId' AND object_id = OBJECT_ID('dbo.CikletMbledhjes'))
BEGIN
    CREATE INDEX idx_ciklet_zoneId ON dbo.CikletMbledhjes(zoneId);
END;

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_ciklet_status' AND object_id = OBJECT_ID('dbo.CikletMbledhjes'))
BEGIN
    CREATE INDEX idx_ciklet_status ON dbo.CikletMbledhjes(status);
END;

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_users_username' AND object_id = OBJECT_ID('dbo.Users'))
BEGIN
    CREATE INDEX idx_users_username ON dbo.Users(username);
END;

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_users_email' AND object_id = OBJECT_ID('dbo.Users'))
BEGIN
    CREATE INDEX idx_users_email ON dbo.Users(email);
END;

