package br.com.FuriniSolutions.dao;

import br.com.FuriniSolutions.bean.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClienteDAO implements Dao<Integer, Cliente> { // <o tipo de dados da PK, o tipo de dados que ela vai informar ou receber>

    protected Connection con;

    public ClienteDAO(Connection con) {
        this.con = con;
    }

    @Override
    public void create(Cliente entity) {
        String sql = "INSERT INTO cliente (nome, endereco) values (?, ?);";

        //try with resouces - fecha a conexao ao final
        try ( PreparedStatement query = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            query.setString(1, entity.getNome());
            query.setString(2, entity.getEndereco());
            query.executeUpdate();

            try ( ResultSet rs = query.getGeneratedKeys()) { //pega a chave gerada pelo banco, esat dentro do try para fechar automaticamenete o resultset
                if (rs.next()) {  // Move o cursor para a primeira linha, e verifica se foi gerado uma chave, caso não sai do if
                    entity.setId(rs.getInt(1)); //coloca o id no produto            }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Cliente retrive(Integer pk) {
        Cliente cliente = null; //cliente a ser retornado

        if (pk != null) {
            String sql = "SELECT id, nome, endereco FROM cliente WHERE id = ?";

            try ( PreparedStatement query = con.prepareStatement(sql)) {
                query.setInt(1, pk);//coloca a pk que foi colocada como parametro do metodo

                try ( ResultSet rs = query.executeQuery()) {

                    if (rs.next()) {//passa o cursor para a primeira linha, e everifica se tem alguma chave
                        cliente = new Cliente();//instanciando um produto com os dados recebidos
                        cliente.setId(rs.getInt("id"));
                        cliente.setNome(rs.getString("nome"));
                        cliente.setEndereco(rs.getString("endereco"));
                    }

                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return cliente;
    }

    @Override
    public void update(Cliente entity) {
        String sql = "UPDATE cliente SET nome = ?, endereco = ? WHERE id = ?";

        try ( PreparedStatement query = con.prepareStatement(sql)) {
            query.setString(1, entity.getNome());
            query.setString(2, entity.getEndereco());
            query.setInt(3, entity.getId());
            query.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer pk) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try ( PreparedStatement query = con.prepareStatement(sql)) {
            query.setInt(1, pk);

            query.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Cliente> findAll() {
        List<Cliente> clientes = new LinkedList<>();

        String sql = "SELECT id, nome, endereco FROM cliente";

        try ( PreparedStatement query = con.prepareStatement(sql)) {
            try ( ResultSet rs = query.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setEndereco(rs.getString("endereco"));

                    clientes.add(cliente);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return clientes;
    }
    
    
    public List<Cliente> buscarPorDescricao(String nome) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE nome LIKE ?";

        try ( PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%"); // Busca parcial
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setEndereco(rs.getString("endereco"));

                    clientes.add(cliente);
                }
            }
        }

        return clientes;
    }

}
