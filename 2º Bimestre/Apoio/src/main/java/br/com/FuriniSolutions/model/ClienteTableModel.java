package br.com.FuriniSolutions.model;

import br.com.FuriniSolutions.bean.Cliente;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author lucas
 */
public class ClienteTableModel extends AbstractTableModel {

    private List<Cliente> linhas = new ArrayList<>();
    private String[] colunas = {"ID", "Nome", "Endereço"};

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
        Cliente cliente = linhas.get(linha);
        return switch (coluna) {
            case 0 ->
                cliente.getId();
            case 1 ->
                cliente.getNome();
            case 2 ->
                cliente.getEndereco();
            default ->
                throw new IllegalArgumentException("Coluna inválida: " + coluna);
        };

    }

    @Override
    public String getColumnName(int coluna) {
        return colunas[coluna];
    }

    //adiciona a categoria na tabela
    public void add(Cliente cliente) {
        int rowIndex = linhas.size();
        this.linhas.add(cliente);
        this.fireTableRowsInserted(rowIndex, rowIndex);
    }

    //adiciona uma lista de categorias na lista desta classe
    public void addList(List<Cliente> clientes) {
        this.linhas.clear();
        this.linhas.addAll(clientes);
        this.fireTableDataChanged();
    }

    //deleta uma categoria da lista da tabela
    public void delete(Cliente cliente) {
        int rowIndex = linhas.indexOf(cliente);
        this.linhas.remove(cliente);
        this.fireTableRowsDeleted(rowIndex, rowIndex);//faz a alteraçõa apenas naquela linha da tabela
    }

    public Cliente getCliente(int linha) {
        return linhas.get(linha);
    }

}
