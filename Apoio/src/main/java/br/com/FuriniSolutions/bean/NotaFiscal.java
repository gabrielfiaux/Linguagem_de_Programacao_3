package br.com.FuriniSolutions.bean;

import java.util.Date;
import java.util.List;

public class NotaFiscal {
    
    private int id;
    private Date dataEmissao;
    private Cliente cliente;
    private List<ItemNota> listaItens;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "NotaFiscal{" + "id=" + id + ", dataEmissao=" + dataEmissao + ", cliente=" + cliente + '}';
    }

    public List<ItemNota> getListaItens() {
        return listaItens;
    }

    public void setListaItens(List<ItemNota> listaItens) {
        this.listaItens = listaItens;
    }
    
}
