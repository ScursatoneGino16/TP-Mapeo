package com.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "\"AccesoTelefonia\"")
public class AccesoTelefonia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"idAccesoTelefonia\"")
    private Integer idAccesoTelefonia; 

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "\"idProvincia\"", foreignKey = @ForeignKey(name = "FK_telefonia_provincia"), nullable = false)
    private Provincia provincia;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "\"idTiempo\"", foreignKey = @ForeignKey(name = "FK_telefonia_tiempo"), nullable = false)
    private Tiempo tiempo;

    @Column(name = "\"hogares\"")
    private Integer hogares;

    @Column(name = "\"comercial\"")
    private Integer comercial;

    @Column(name = "\"gobierno\"")
    private Integer gobierno;

    @Column(name = "\"otros\"")
    private Integer otros;

    @Column(name = "\"total\"")
    private Integer total;

    
    public AccesoTelefonia() {}

    
    public Integer getIdAccesoTelefonia() { return idAccesoTelefonia; }
    public void setIdAccesoTelefonia(Integer idAccesoTelefonia) { this.idAccesoTelefonia = idAccesoTelefonia; }

    public Provincia getProvincia() { return provincia; }
    public void setProvincia(Provincia provincia) { this.provincia = provincia; }

    public Tiempo getTiempo() { return tiempo; }
    public void setTiempo(Tiempo tiempo) { this.tiempo = tiempo; }

    public Integer getHogares() { return hogares; }
    public void setHogares(Integer hogares) { this.hogares = hogares; }

    public Integer getComercial() { return comercial; }
    public void setComercial(Integer comercial) { this.comercial = comercial; }

    public Integer getGobierno() { return gobierno; }
    public void setGobierno(Integer gobierno) { this.gobierno = gobierno; }

    public Integer getOtros() { return otros; }
    public void setOtros(Integer otros) { this.otros = otros; }

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
}