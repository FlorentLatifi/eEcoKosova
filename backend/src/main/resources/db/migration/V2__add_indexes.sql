-- Indekse për performancë queries

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_containers_zoneId' AND object_id = OBJECT_ID('dbo.Containers'))
BEGIN
    CREATE INDEX idx_containers_zoneId ON dbo.Containers(zoneId);
END;

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_containers_status' AND object_id = OBJECT_ID('dbo.Containers'))
BEGIN
    CREATE INDEX idx_containers_status ON dbo.Containers(status);
END;

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_zones_municipality' AND object_id = OBJECT_ID('dbo.Zones'))
BEGIN
    CREATE INDEX idx_zones_municipality ON dbo.Zones(municipality);
END;

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_zones_status' AND object_id = OBJECT_ID('dbo.Zones'))
BEGIN
    CREATE INDEX idx_zones_status ON dbo.Zones(status);
END;


