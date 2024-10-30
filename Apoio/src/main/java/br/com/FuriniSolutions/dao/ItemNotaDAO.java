package br.com.FuriniSolutions.dao;

import br.com.FuriniSolutions.bean.ItemNota;
import br.com.FuriniSolutions.bean.NotaFiscal;
import br.com.FuriniSolutions.bean.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class ItemNotaDAO implements Dao<Integer, ItemNota> { // <o tipo de dados da PK, o tipo de dados que ela vai informar ou receber>

    protected Connection con;

    public ItemNotaDAO(Connection con) {
        this.con = con;
    }

    @Override
    public void create(ItemNota entity) {
        String sql = "INSERT INTO itemnota (quantidade, valorItem, produto_id, notaFiscal_id) values (?, ?, ?, ?);";

        //try with resouces - fecha a conexao ao final
        try ( PreparedStatement query = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            query.setDouble(1, entity.getValorItem());
            query.setInt(2, entity.getQuantidade());
            query.setInt(3, entity.getProduto().getId());
            query.setInt(4, entity.getNotaFiscal().getId());
            query.executeUpdate();

            try ( ResultSet rs = query.getGeneratedKeys()) {
                if (rs.next()) {  // Move o cursor para a primeira linha, pois pro padrao vem antes
                    entity.setId(rs.getInt(1)); //coloca o id no itemnota
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ItemNota retrive(Integer pk) {
        ItemNota itemNota = null;

        if (pk == null) {
            return null; // Se o pk for nulo, retorna null diretamente
        }

        String sql = "SELECT id, quantidade, valorItem, produto_id, notaFiscal_id FROM itemnota WHERE id = ?";

        try ( PreparedStatement query = con.prepareStatement(sql)) {
            query.setInt(1, pk);

            try ( ResultSet rs = query.executeQuery()) {
                if (rs.next()) {
                    itemNota = new ItemNota();
                    itemNota.setId(rs.getInt("id"));
                    itemNota.setQuantidade(rs.getInt("quantidade"));
                    itemNota.setValorItem(rs.getDouble("valorItem"));

                    // Usando DAOs existentes para recuperar Produto e NotaFiscal
                    ProdutoDAO produtoDao = new ProdutoDAO(con);
                    Produto produto = produtoDao.retrive(rs.getInt("produto_id"));
                    if (produto != null) {
                        itemNota.setProduto(produto);
                    } else {
                        System.out.println("Produto não encontrado para o ID: " + rs.getInt("produto_id"));
                    }

                    NotaFiscalDAO notaFiscalDao = new NotaFiscalDAO(con);
                    NotaFiscal notaFiscal = notaFiscalDao.retrive(rs.getInt("notaFiscal_id"));
                    if (notaFiscal != null) {
                        itemNota.setNotaFiscal(notaFiscal);
                    } else {
                        System.out.println("Nota Fiscal não encontrada para o ID: " + rs.getInt("notaFiscal_id"));
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao recuperar ItemNota: " + e.getMessage());
            e.printStackTrace(); // Exibe o stack trace completo para facilitar a depuração
        }

        return itemNota;
    }

    @Override
    public void update(ItemNota entity) {
        String sql = "UPDATE itemnota SET quantidade = ?, valorItem = ?, produto_id = ?, notaFiscal_id = ? WHERE id = ?";

        try ( PreparedStatement query = con.prepareStatement(sql)) {
            query.setInt(1, entity.getQuantidade());
            query.setDouble(2, entity.getValorItem());
            query.setInt(3, entity.getProduto().getId());
            query.setInt(4, entity.getNotaFiscal().getId());
            query.setInt(5, entity.getId());
            query.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer pk) {
        String sql = "DELETE FROM itemnota WHERE id = ?";

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
    public List<ItemNota> findAll() {
        List<ItemNota> itemnotas = new LinkedList<>();

        String sql = "SELECT id, quantidade, valorItem, produto_id, notaFiscal_id FROM itemnota";

        // Usar uma única conexão para todas as operações
        try (PreparedStatement query = con.prepareStatement(sql)){            

            ResultSet rs = query.executeQuery();
            // Criar os DAOs com a mesma conexão
            ProdutoDAO produtoDao = new ProdutoDAO(con);
            NotaFiscalDAO notaFiscalDao = new NotaFiscalDAO(con);

            while (rs.next()) {
                ItemNota itemNota = new ItemNota();
                itemNota.setId(rs.getInt("id"));
                itemNota.setQuantidade(rs.getInt("quantidade"));
                itemNota.setValorItem(rs.getDouble("valorItem"));

                // Reutilizando a mesma conexão para as operações de Produto e NotaFiscal
                itemNota.setProduto(produtoDao.retrive(rs.getInt("produto_id")));
                itemNota.setNotaFiscal(notaFiscalDao.retrive(rs.getInt("notaFiscal_id")));

                itemnotas.add(itemNota);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return itemnotas;
    }

    public List<ItemNota> findAllWithIDNota(Integer id_nota) {
        List<ItemNota> itemnotas = new LinkedList<>();

        String sql = "SELECT id, quantidade, valorItem, produto_id, notaFiscal_id FROM itemnota WHERE notaFiscal_id = ?";

        try {
            PreparedStatement query = con.prepareStatement(sql);
            query.setInt(1, id_nota);

            ResultSet rs = query.executeQuery();            
            // Criar os DAOs com a mesma conexão
            ProdutoDAO produtoDao = new ProdutoDAO(con);
            NotaFiscalDAO notaFiscalDao = new NotaFiscalDAO(con);

            while (rs.next()) {
                ItemNota itemNota = new ItemNota();
                itemNota.setId(rs.getInt("id"));
                itemNota.setQuantidade(rs.getInt("quantidade"));
                itemNota.setValorItem(rs.getDouble("valorItem"));

                itemNota.setProduto(produtoDao.retrive(rs.getInt("produto_id")));
                itemNota.setNotaFiscal(notaFiscalDao.retrive(rs.getInt("notaFiscal_id")));

                itemnotas.add(itemNota);
            }

        } catch (Exception e) {
            System.err.println("Erro ao buscar ItemNotas com IDNota: " + e.getMessage());
            e.printStackTrace(); // Exibe o stack trace para facilitar a depuração
        }

        return itemnotas;
    }

}
