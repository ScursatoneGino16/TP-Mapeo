package com.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "\"Provincia\"")
public class Provincia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"idProvincia\"")
    private Integer idProvincia;

    @Column(name = "\"nombreProvincia\"", nullable = false, unique = true, length = 100)
    private String nombreProvincia;


    public Provincia() {}

    public Provincia(String nombreProvincia) {
        this.nombreProvincia = nombreProvincia;
    }


    public Integer getIdProvincia() { return idProvincia; }
    public void setIdProvincia(Integer idProvincia) { this.idProvincia = idProvincia; }

    public String getNombreProvincia() { return nombreProvincia; }
    public void setNombreProvincia(String nombreProvincia) { this.nombreProvincia = nombreProvincia; }
}