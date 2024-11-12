package br.com.FuriniSolutions.bean;

public class ItemNota {
    
    private int id;
    private int quantidade;
    private Double valorItem;
    private Produto produto;
    private NotaFiscal notaFiscal;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValorItem() {
        return valorItem;
    }

    public void setValorItem(Double valorItem) {
        this.valorItem = valorItem;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public NotaFiscal getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(NotaFiscal notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    @Override
    public String toString() {
        return "ItemNota{" + "id=" + id + ", quantidade=" + quantidade + ", valorItem=" + valorItem + ", produto=" + produto + ", notaFiscal=" + notaFiscal + '}';
    }
    
       
}
