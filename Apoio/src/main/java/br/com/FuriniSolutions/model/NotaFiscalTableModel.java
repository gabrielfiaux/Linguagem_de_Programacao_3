package br.com.FuriniSolutions.model;

import br.com.FuriniSolutions.bean.NotaFiscal;
import br.com.FuriniSolutions.util.DataUtil;
import br.com.FuriniSolutions.util.Observer;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author lucas
 */
public class NotaFiscalTableModel extends AbstractTableModel {

    private List<NotaFiscal>linhas = new ArrayList<>();
    private String[] colunas = {"ID", "Cliente", "Data emissão"};

    @Override
    public int getRowCount() {
        return linhas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        NotaFiscal nota = linhas.get(linha);
        return switch (coluna) {
            case 0 ->
                nota.getId();
            case 1 ->
                nota.getCliente().getNome();
            case 2 ->
                DataUtil.formatarData(nota.getDataEmissao());            
            default ->
                throw new IllegalArgumentException("Coluna inválida: " + coluna);
        };

    }

    @Override
    public String getColumnName(int coluna) {
        return colunas[coluna];
    }

    public NotaFiscal getProduto(int linha) {
        return linhas.get(linha);
    }

    public void setProdutos(List<NotaFiscal> itens) {
        this.linhas = itens;
    }

    public void add(NotaFiscal item) {
        int rowIndex = linhas.size();
        this.linhas.add(item);
        this.fireTableRowsInserted(rowIndex, rowIndex);
    }

    public void addList(List<NotaFiscal> notas) {
        this.linhas.clear();
        this.linhas.addAll(notas);
        this.fireTableDataChanged();
    }

    public void delete(NotaFiscal nota) {
        int rowIndex = linhas.indexOf(nota);
        this.linhas.remove(nota);
        this.fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public NotaFiscal getNota(int linha) {
        return linhas.get(linha);
    }

    // Método para atualizar um item na tabela com base no indice
    public void updateItemAt(int rowIndex, NotaFiscal updatedItem) {
        if (rowIndex >= 0 && rowIndex < linhas.size()) {
            // Atualiza o item na posição indicada
            linhas.set(rowIndex, updatedItem);
            // Notifica que a linha foi atualizada
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }
    
    public List<NotaFiscal> getlist(){
        return this.linhas;
    }
    
    public void removeAll() {
        linhas.clear(); // Limpa a lista de dados
        fireTableDataChanged(); // Notifica a tabela que os dados foram removidos
    }    

}
