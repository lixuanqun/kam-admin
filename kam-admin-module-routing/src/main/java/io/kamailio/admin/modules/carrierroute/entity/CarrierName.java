package io.kamailio.admin.modules.carrierroute.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "carrier_name")
public class CarrierName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64, nullable = false)
    private String carrier;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }
}
