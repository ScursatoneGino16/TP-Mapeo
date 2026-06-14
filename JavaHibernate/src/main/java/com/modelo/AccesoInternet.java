package com.modelo;

import jakarta.persistence.*;

@Entity

@Table(name = "\"AccesoInternet\"")
public class AccesoInternet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"idAccesoInternet\"")
    private Integer idAccesoInternet; 

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "\"idProvincia\"", foreignKey = @ForeignKey(name = "FK_internet_provincia"), nullable = false)
    private Provincia provincia;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "\"idTiempo\"", foreignKey = @ForeignKey(name = "FK_internet_tiempo"), nullable = false)
    private Tiempo tiempo;

    @Column(name = "\"bandaAnchaFija\"")
    private Integer bandaAnchaFija;

    @Column(name = "\"dialUp\"") 
    private Integer dialUp;

    @Column(name = "\"total\"")
    private Integer total;

    
    public AccesoInternet() {}

    
    public Integer getIdAccesoInternet() { return idAccesoInternet; }
    public void setIdAccesoInternet(Integer idAccesoInternet) { this.idAccesoInternet = idAccesoInternet; }

    public Provincia getProvincia() { return provincia; }
    public void setProvincia(Provincia provincia) { this.provincia = provincia; }

    public Tiempo getTiempo() { return tiempo; }
    public void setTiempo(Tiempo tiempo) { this.tiempo = tiempo; }

    public Integer getBandaAnchaFija() { return bandaAnchaFija; }
    public void setBandaAnchaFija(Integer bandaAnchaFija) { this.bandaAnchaFija = bandaAnchaFija; }

    public Integer getDialUp() { return dialUp; }
    public void setDialUp(Integer dialUp) { this.dialUp = dialUp; }

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
}