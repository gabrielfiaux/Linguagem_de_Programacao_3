package interfacegraficalll;


import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ClienteTableModel extends AbstractTableModel {
    private String[] colunas = {"ID", "Nome", "Email"};
    private ArrayList<Cliente> clientes;

    public ClienteTableModel(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }

    @Override
    public int getRowCount() {
        return clientes.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Cliente cliente = clientes.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return cliente.getId();
            case 1:
                return cliente.getNome();
            case 2:
                return cliente.getEmail();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    public void addCliente(Cliente cliente) {
        clientes.add(cliente);
        fireTableRowsInserted(clientes.size() - 1, clientes.size() - 1);
    }

    public void removeCliente(int rowIndex) {
        clientes.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public Cliente getClienteAt(int rowIndex) {
        return clientes.get(rowIndex);
    }

    public void updateCliente(int rowIndex, Cliente cliente) {
        clientes.set(rowIndex, cliente);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }
}