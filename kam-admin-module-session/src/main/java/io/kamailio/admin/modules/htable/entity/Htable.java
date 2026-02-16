package io.kamailio.admin.modules.htable.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "htable")
public class Htable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "key_name", length = 64, nullable = false)
    private String keyName;
    @Column(name = "key_type")
    private Integer keyType = 0;
    @Column(name = "value_type")
    private Integer valueType = 0;
    @Column(name = "key_value", length = 128)
    private String keyValue = "";
    @Column(nullable = false)
    private Integer expires = 0;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getKeyName() { return keyName; }
    public void setKeyName(String keyName) { this.keyName = keyName; }
    public Integer getKeyType() { return keyType; }
    public void setKeyType(Integer keyType) { this.keyType = keyType; }
    public Integer getValueType() { return valueType; }
    public void setValueType(Integer valueType) { this.valueType = valueType; }
    public String getKeyValue() { return keyValue; }
    public void setKeyValue(String keyValue) { this.keyValue = keyValue; }
    public Integer getExpires() { return expires; }
    public void setExpires(Integer expires) { this.expires = expires; }
}
