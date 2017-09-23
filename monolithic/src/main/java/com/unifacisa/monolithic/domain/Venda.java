package com.unifacisa.monolithic.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Venda.
 */
@Document(collection = "venda")
public class Venda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Field("id_pedido")
    private String idPedido;

    @Field("valor")
    private Double valor;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public Venda idPedido(String idPedido) {
        this.idPedido = idPedido;
        return this;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public Double getValor() {
        return valor;
    }

    public Venda valor(Double valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Venda venda = (Venda) o;
        if (venda.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), venda.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Venda{" +
            "id=" + getId() +
            ", idPedido='" + getIdPedido() + "'" +
            ", valor='" + getValor() + "'" +
            "}";
    }
}
