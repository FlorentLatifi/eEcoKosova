-- Seed data për EcoKosova
-- Krijon disa zona dhe kontejnerë test për demonstrim

SET NOCOUNT ON;

-- =====================
-- Seed për tabelën Zones
-- =====================
IF NOT EXISTS (SELECT 1 FROM dbo.Zones WHERE id = 'ZONE-001')
BEGIN
    INSERT INTO dbo.Zones (id, name, status, municipality, description, criticalThreshold, latitude, longitude, createdAt, modifiedAt)
    VALUES (
        'ZONE-001',
        N'Qendra e Prishtinës',
        N'Aktive',
        N'Prishtinë',
        N'Zona qendrore me densitet të lartë kontejnerësh',
        80,
        42.6629,
        21.1655,
        SYSDATETIME(),
        SYSDATETIME()
    );
END;

IF NOT EXISTS (SELECT 1 FROM dbo.Zones WHERE id = 'ZONE-002')
BEGIN
    INSERT INTO dbo.Zones (id, name, status, municipality, description, criticalThreshold, latitude, longitude, createdAt, modifiedAt)
    VALUES (
        'ZONE-002',
        N'Lagjja Dardania',
        N'Aktive',
        N'Prishtinë',
        N'Zonë rezidenciale me ndërtesa kolektive',
        75,
        42.6580,
        21.1640,
        SYSDATETIME(),
        SYSDATETIME()
    );
END;

-- =========================
-- Seed për tabelën Containers
-- =========================
IF NOT EXISTS (SELECT 1 FROM dbo.Containers WHERE id = 'CONT-001')
BEGIN
    INSERT INTO dbo.Containers (
        id, zoneId, type, fillLevel, status, capacity, operational,
        latitude, longitude, street, city, municipality, postalCode,
        lastEmptied, lastUpdated, createdAt, modifiedAt
    )
    VALUES (
        'CONT-001',
        'ZONE-001',
        N'PLASTIC',
        35,
        N'Normal',
        1100,
        1,
        42.6629,
        21.1655,
        N'Rr. B',
        N'Prishtinë',
        N'Prishtinë',
        N'10000',
        DATEADD(DAY, -1, SYSDATETIME()),
        SYSDATETIME(),
        SYSDATETIME(),
        SYSDATETIME()
    );
END;

IF NOT EXISTS (SELECT 1 FROM dbo.Containers WHERE id = 'CONT-002')
BEGIN
    INSERT INTO dbo.Containers (
        id, zoneId, type, fillLevel, status, capacity, operational,
        latitude, longitude, street, city, municipality, postalCode,
        lastEmptied, lastUpdated, createdAt, modifiedAt
    )
    VALUES (
        'CONT-002',
        'ZONE-001',
        N'MIXED',
        92,
        N'Kritike',
        1100,
        1,
        42.6632,
        21.1660,
        N'Rr. C',
        N'Prishtinë',
        N'Prishtinë',
        N'10000',
        DATEADD(DAY, -2, SYSDATETIME()),
        SYSDATETIME(),
        SYSDATETIME(),
        SYSDATETIME()
    );
END;

IF NOT EXISTS (SELECT 1 FROM dbo.Containers WHERE id = 'CONT-003')
BEGIN
    INSERT INTO dbo.Containers (
        id, zoneId, type, fillLevel, status, capacity, operational,
        latitude, longitude, street, city, municipality, postalCode,
        lastEmptied, lastUpdated, createdAt, modifiedAt
    )
    VALUES (
        'CONT-003',
        'ZONE-002',
        N'GLASS',
        68,
        N'Warning',
        800,
        1,
        42.6582,
        21.1638,
        N'Rr. Dardania',
        N'Prishtinë',
        N'Prishtinë',
        N'10000',
        DATEADD(DAY, -3, SYSDATETIME()),
        SYSDATETIME(),
        SYSDATETIME(),
        SYSDATETIME()
    );
END;


