package com.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "\"Tiempo\"", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"\"año\"", "trimestre"})
})
public class Tiempo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"idTiempo\"")
    private Integer idTiempo;

    @Column(name = "\"año\"", nullable = false)
    private Integer anio;

    @Column(name = "trimestre", nullable = false) 
    private Integer trimestre;


    public Tiempo() {}

    public Tiempo(Integer anio, Integer trimestre) {
        this.anio = anio;
        this.trimestre = trimestre;
    }


    public Integer getIdTiempo() { return idTiempo; }
    public void setIdTiempo(Integer idTiempo) { this.idTiempo = idTiempo; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public Integer getTrimestre() { return trimestre; }
    public void setTrimestre(Integer trimestre) { this.trimestre = trimestre; }
}