package br.com.fiap.helios.rest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Painel solar — entidade principal do CRUD REST.
 *
 * <p>Herda os atributos comuns de {@link Ativo} e acrescenta a potência nominal
 * (energia esperada) e a localização, usada depois para consultar o ambiente.</p>
 */
@Entity
@Table(name = "painel_solar")
public class PainelSolar extends Ativo {

    /** Potência nominal (W) — referência de energia esperada para o soiling ratio. */
    @Column(name = "potencia_nominal_w", nullable = false)
    private double potenciaNominalW;

    private Double latitude;

    private Double longitude;

    public PainelSolar() {
    }

    @Override
    public String tipo() {
        return "PAINEL_SOLAR";
    }

    public double getPotenciaNominalW() { return potenciaNominalW; }
    public void setPotenciaNominalW(double potenciaNominalW) { this.potenciaNominalW = potenciaNominalW; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}
