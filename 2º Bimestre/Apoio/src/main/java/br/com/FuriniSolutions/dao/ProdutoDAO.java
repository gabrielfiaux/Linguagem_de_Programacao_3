package br.com.FuriniSolutions.dao;

import br.com.FuriniSolutions.bean.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProdutoDAO implements Dao<Integer, Produto> { // <o tipo de dados da PK, o tipo de dados que ela vai informar ou receber>

    protected Connection con;

    public ProdutoDAO(Connection con) {
        this.con = con;
    }

    @Override
    public void create(Produto entity) {
        String sql = "INSERT INTO produto (descricao, valor) values (?, ?);";

        //try with resouces - fecha a conexao ao final
        try ( PreparedStatement query = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            query.setString(1, entity.getDescricao());
            query.setDouble(2, entity.getValor());
            query.executeUpdate();

            ResultSet rs = query.getGeneratedKeys();//pega a chave gerada pelo banco

            if (rs.next()) {  // Move o cursor para a primeira linha
                entity.setId(rs.getInt(1)); //coloca o id no produto            }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public Produto retrive(Integer pk) {
        Produto produto = null; //produto a ser retornado

        if (pk != null) {
            String sql = "SELECT id, descricao, valor FROM produto WHERE id = ?";

            try ( PreparedStatement query = con.prepareStatement(sql)) {
                query.setInt(1, pk);//coloca a pk que foi colocada como parametro do metodo

                ResultSet rs = query.executeQuery();

                if (rs.next()) {//passa o cursor para a primeira linha
                    produto = new Produto();//instanciando um produto com os dados recebidos
                    produto.setId(rs.getInt("id"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setValor(rs.getDouble("valor"));
                }
                rs.close();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return produto;
    }

    @Override
    public void update(Produto entity) {
        String sql = "UPDATE produto SET descricao = ?, valor = ? WHERE id = ?";

        try ( PreparedStatement query = con.prepareStatement(sql)) {
            query.setString(1, entity.getDescricao());
            query.setDouble(2, entity.getValor());
            query.setInt(3, entity.getId());
            query.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer pk) {
        String sql = "DELETE FROM produto WHERE id = ?";

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
    public List<Produto> findAll() {
        List<Produto> produtos = new LinkedList<>();

        String sql = "SELECT id, descricao, valor FROM produto";

        try ( PreparedStatement query = con.prepareStatement(sql)) {
            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setValor(rs.getDouble("valor"));

                produtos.add(produto);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return produtos;

    }

    public List<Produto> buscarPorDescricao(String descricao) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE descricao LIKE ?";

        try ( PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + descricao + "%"); // Busca parcial
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setValor(rs.getDouble("valor"));
                    // Preenche outros atributos, se necessário

                    produtos.add(produto);
                }
            }
        }

        return produtos;
    }

}
