-- Flyway migration që përdor të njëjtën skemë si schema.sql
-- Mund të mbash schema.sql për referencë, por Flyway do të jetë burimi kryesor i të vërtetës.

-- Kopjuar / përshtatur nga schema.sql (Zones dhe Containers vetëm)

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Zones]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[Zones] (
        [id]               NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [name]             NVARCHAR(200) NOT NULL,
        [status]           NVARCHAR(50)  NOT NULL,
        [municipality]     NVARCHAR(100) NOT NULL,
        [description]      NVARCHAR(1000) NULL,
        [criticalThreshold] INT          NOT NULL,
        [latitude]         FLOAT         NOT NULL,
        [longitude]        FLOAT         NOT NULL,
        [createdAt]        DATETIME2     NOT NULL,
        [modifiedAt]       DATETIME2     NOT NULL
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Containers]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[Containers] (
        [id]           NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [zoneId]       NVARCHAR(50)  NOT NULL,
        [type]         NVARCHAR(50)  NOT NULL,
        [fillLevel]    INT           NOT NULL,
        [status]       NVARCHAR(50)  NOT NULL,
        [capacity]     INT           NOT NULL,
        [operational]  BIT           NOT NULL,
        [latitude]     FLOAT         NOT NULL,
        [longitude]    FLOAT         NOT NULL,
        [street]       NVARCHAR(200) NOT NULL,
        [city]         NVARCHAR(100) NOT NULL,
        [municipality] NVARCHAR(100) NOT NULL,
        [postalCode]   NVARCHAR(20)  NULL,
        [lastEmptied]  DATETIME2     NULL,
        [lastUpdated]  DATETIME2     NOT NULL,
        [createdAt]    DATETIME2     NOT NULL,
        [modifiedAt]   DATETIME2     NOT NULL,
        CONSTRAINT FK_Containers_Zones FOREIGN KEY (zoneId)
            REFERENCES [dbo].[Zones] (id)
    );
END;


