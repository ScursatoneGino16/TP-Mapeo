package com.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "\"AccesoTelevision\"")
public class AccesoTelevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"idAccesoTelevision\"")
    private Integer idAccesoTelevision; 

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "\"idProvincia\"", foreignKey = @ForeignKey(name = "FK_television_provincia"), nullable = false)
    private Provincia provincia;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "\"idTiempo\"", foreignKey = @ForeignKey(name = "FK_television_tiempo"), nullable = false)
    private Tiempo tiempo;

    @Column(name = "\"accesosTvSuscripcion\"") 
    private Integer accesosTvSuscripcion;

    
    public AccesoTelevision() {}

    
    public Integer getIdAccesoTelevision() { return idAccesoTelevision; }
    public void setIdAccesoTelevision(Integer idAccesoTelevision) { this.idAccesoTelevision = idAccesoTelevision; }

    public Provincia getProvincia() { return provincia; }
    public void setProvincia(Provincia provincia) { this.provincia = provincia; }

    public Tiempo getTiempo() { return tiempo; }
    public void setTiempo(Tiempo tiempo) { this.tiempo = tiempo; }

    public Integer getAccesosTvSuscripcion() { return accesosTvSuscripcion; }
    public void setAccesosTvSuscripcion(Integer accesosTvSuscripcion) { this.accesosTvSuscripcion = accesosTvSuscripcion; }
}