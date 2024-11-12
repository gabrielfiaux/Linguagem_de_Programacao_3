package br.com.FuriniSolutions.model;

import br.com.FuriniSolutions.bean.Cliente;
import br.com.FuriniSolutions.bean.Produto;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author lucas
 */
public class ProdutoTableModel extends AbstractTableModel {

    private List<Produto> linhas = new ArrayList<>();
    private String[] colunas = {"ID", "Descrição", "Valor (R$)"};
    private DecimalFormat formatadorDecimal = new DecimalFormat("#,##0.00");

    @Override
    public int getRowCount() {
        return linhas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    //metodo para colocar os valores nas colunas
    @Override
    public Object getValueAt(int linha, int coluna) {
        Produto produto = linhas.get(linha);
        return switch (coluna) {
            case 0 ->
                produto.getId();
            case 1 ->
                produto.getDescricao();
            case 2 ->
                formatadorDecimal.format(produto.getValor());
            default ->
                throw new IllegalArgumentException("Coluna inválida: " + coluna);
        };

    }

    @Override
    public String getColumnName(int coluna) {
        return colunas[coluna];
    }

    //adiciona a categoria na tabela
    public void add(Produto produto) {
        int rowIndex = linhas.size();
        this.linhas.add(produto);
        this.fireTableRowsInserted(rowIndex, rowIndex);
    }

    //adiciona uma lista de categorias na lista desta classe
    public void addList(List<Produto> produtos) {
        this.linhas.clear();
        this.linhas.addAll(produtos);
        this.fireTableDataChanged();
    }

    //deleta uma categoria da lista da tabela
    public void delete(Produto produto) {
        int rowIndex = linhas.indexOf(produto);
        this.linhas.remove(produto);
        this.fireTableRowsDeleted(rowIndex, rowIndex);//faz a alteraçõa apenas naquela linha da tabela
    }

    public Produto getProduto(int linha) {
        return linhas.get(linha);
    }

    public void setProdutos(List<Produto> produtos) {
        this.linhas = produtos;
    }

}
