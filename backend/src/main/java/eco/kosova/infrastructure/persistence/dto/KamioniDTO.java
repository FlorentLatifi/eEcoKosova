package eco.kosova.infrastructure.persistence.dto;

public class KamioniDTO {
    private String id;
    private String emri;
    private String status;
    private double latitude;
    private double longitude;
    private String targa;
    private int kapaciteti;
    private String operatoriID;

    public KamioniDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmri() { return emri; }
    public void setEmri(String emri) { this.emri = emri; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public String getTarga() { return targa; }
    public void setTarga(String targa) { this.targa = targa; }
    public int getKapaciteti() { return kapaciteti; }
    public void setKapaciteti(int kapaciteti) { this.kapaciteti = kapaciteti; }
    public String getOperatoriID() { return operatoriID; }
    public void setOperatoriID(String operatoriID) { this.operatoriID = operatoriID; }
}
