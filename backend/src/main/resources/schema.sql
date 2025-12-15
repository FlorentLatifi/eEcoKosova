-- =============================================
--  MSSQL Schema për sistemin "EcoKosova"
--  Bazuar në Dokumentimi i Modelit Konceptual.txt
--  dhe në modelin ekzistues të domain-it (Zone, Kontenier)
-- =============================================

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

-- ================== Konceptual Entities ==================

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Qytetare]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[Qytetare] (
        [qytetariID] NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [emri]       NVARCHAR(200) NOT NULL,
        [adresa]     NVARCHAR(400) NOT NULL
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[KontrollPanele]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[KontrollPanele] (
        [panelID]        NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [gjuha]          NVARCHAR(50)  NOT NULL,
        [tema]           NVARCHAR(50)  NULL,
        [gjendjaEkranit] NVARCHAR(50)  NULL,
        [qytetariID]     NVARCHAR(50)  NULL UNIQUE,
        CONSTRAINT FK_KontrollPanel_Qytetar FOREIGN KEY (qytetariID)
            REFERENCES [dbo].[Qytetare] (qytetariID)
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Ora]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[Ora] (
        [oraID]  INT IDENTITY(1,1) PRIMARY KEY,
        [koha]   DATETIME2 NOT NULL,
        [data]   DATE      NOT NULL
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[EcoKosova]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[EcoKosova] (
        [ecoID]   INT IDENTITY(1,1) PRIMARY KEY,
        [statusi] NVARCHAR(50) NOT NULL
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Vendndodhje]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[Vendndodhje] (
        [vendndodhjaID] NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [emri]          NVARCHAR(200) NOT NULL,
        [koordinata]    NVARCHAR(200) NOT NULL
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Paisje]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[Paisje] (
        [paisjeID]      NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [emri]          NVARCHAR(200) NOT NULL,
        [statusi]       NVARCHAR(50)  NOT NULL,
        [latitude]      FLOAT         NULL,
        [longitude]     FLOAT         NULL,
        [dataInstalimit] DATE         NULL,
        [vendndodhjaID] NVARCHAR(50)  NULL,
        CONSTRAINT FK_Paisje_Vendndodhje FOREIGN KEY (vendndodhjaID)
            REFERENCES [dbo].[Vendndodhje] (vendndodhjaID)
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[KontenierKoncept]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[KontenierKoncept] (
        [paisjeID]    NVARCHAR(50) NOT NULL PRIMARY KEY,
        [lloji]       NVARCHAR(50) NOT NULL,
        [niveli]      INT          NOT NULL,
        [kapaciteti]  INT          NOT NULL,
        [operueshem]  BIT          NOT NULL,
        [zonaID]      NVARCHAR(50) NULL,
        CONSTRAINT FK_Kontenier_Paisje FOREIGN KEY (paisjeID)
            REFERENCES [dbo].[Paisje] (paisjeID),
        CONSTRAINT FK_Kontenier_ZonaKoncept FOREIGN KEY (zonaID)
            REFERENCES [dbo].[ZonaKoncept] (zonaID)
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Operatori]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[Operatori] (
        [operatoriID] NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [emri]        NVARCHAR(200) NOT NULL
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Kamioni]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[Kamioni] (
        [paisjeID]    NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [targa]       NVARCHAR(50)  NOT NULL,
        [kapaciteti]  INT           NOT NULL,
        [operatoriID] NVARCHAR(50)  NULL,
        CONSTRAINT FK_Kamioni_Paisje FOREIGN KEY (paisjeID)
            REFERENCES [dbo].[Paisje] (paisjeID),
        CONSTRAINT FK_Kamioni_Operatori FOREIGN KEY (operatoriID)
            REFERENCES [dbo].[Operatori] (operatoriID)
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ZonaKoncept]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[ZonaKoncept] (
        [zonaID]       NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [emri]         NVARCHAR(200) NOT NULL,
        [niveliKritik] INT           NOT NULL
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Konfigurimi]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[Konfigurimi] (
        [konfigID]    NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [parametrat]  NVARCHAR(MAX) NULL,
        [verzioni]    NVARCHAR(50)  NOT NULL,
        [dataKrijimit] DATE         NOT NULL,
        [vendndodhjaID] NVARCHAR(50) NULL,
        CONSTRAINT FK_Konfigurimi_Vendndodhje FOREIGN KEY (vendndodhjaID)
            REFERENCES [dbo].[Vendndodhje] (vendndodhjaID)
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[CikliMbledhjes]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[CikliMbledhjes] (
        [cikliID]       NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [orari]         DATETIME2     NOT NULL,
        [kapacitetiMax] INT           NOT NULL,
        [dita]          NVARCHAR(100) NOT NULL,
        [ecoID]         INT           NULL,
        [konfigID]      NVARCHAR(50)  NULL,
        CONSTRAINT FK_Cikli_EcoKosova FOREIGN KEY (ecoID)
            REFERENCES [dbo].[EcoKosova] (ecoID),
        CONSTRAINT FK_Cikli_Konfigurimi FOREIGN KEY (konfigID)
            REFERENCES [dbo].[Konfigurimi] (konfigID)
    );
END;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Raporte]') AND type = N'U')
BEGIN
    CREATE TABLE [dbo].[Raporte] (
        [raportiID] NVARCHAR(50)  NOT NULL PRIMARY KEY,
        [data]      DATE          NOT NULL,
        [pershkrimi] NVARCHAR(500) NULL,
        [paisjeID]  NVARCHAR(50)  NULL,
        [oraID]     INT           NULL,
        CONSTRAINT FK_Raport_Paisje FOREIGN KEY (paisjeID)
            REFERENCES [dbo].[Paisje] (paisjeID),
        CONSTRAINT FK_Raport_Ora FOREIGN KEY (oraID)
            REFERENCES [dbo].[Ora] (oraID)
    );
END;


