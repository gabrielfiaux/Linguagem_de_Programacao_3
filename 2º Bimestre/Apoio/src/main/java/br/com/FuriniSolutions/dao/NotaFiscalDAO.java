package br.com.FuriniSolutions.dao;

import br.com.FuriniSolutions.bean.ItemNota;
import br.com.FuriniSolutions.bean.NotaFiscal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class NotaFiscalDAO implements Dao<Integer, NotaFiscal> { // <o tipo de dados da PK, o tipo de dados que ela vai informar ou receber>

    protected Connection con;

    public NotaFiscalDAO(Connection con) {
        this.con = con;
    }

    @Override
    public void create(NotaFiscal entity) {
        String sql = "INSERT INTO notafiscal (dataEmissao, cliente_id) values (?, ?);";

        //salvando a nota fiscal
        //try with resouces - fecha a conexao ao final
        try ( PreparedStatement query = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            query.setDate(1, (Date) entity.getDataEmissao());
            query.setInt(2, entity.getCliente().getId());
            query.executeUpdate();

            try ( ResultSet rs = query.getGeneratedKeys()) {
                if (rs.next()) {  // Move o cursor para a primeira linha, pois pro padrao vem antes
                    entity.setId(rs.getInt(1)); //coloca o id no produto            }
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //salvando os dados de itens nota
        List<ItemNota> itemnotas = entity.getListaItens();

        if (itemnotas != null) {
            for (ItemNota itemDaNota : itemnotas) {

                sql = "INSERT INTO itemnota (quantidade, valorItem, produto_id, notaFiscal_id) values (?, ?, ?, ?);";
                try ( PreparedStatement query = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    query.setInt(1, itemDaNota.getQuantidade());
                    query.setDouble(2, itemDaNota.getValorItem());
                    query.setInt(3, itemDaNota.getProduto().getId());
                    query.setInt(4, entity.getId());

                    query.executeUpdate();

                    try ( ResultSet rs = query.getGeneratedKeys()) {
                        if (rs.next()) {  // Move o cursor para a primeira linha, pois pro padrao vem antes
                            itemDaNota.setId(rs.getInt(1)); //coloca o id no produto            }
                        }

                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        }

    }

    @Override
    public NotaFiscal retrive(Integer pk) {
        NotaFiscal notaFiscal = null; //produto a ser retornado

        if (pk != null) {
            String sql = "SELECT id, dataEmissao, cliente_id FROM notafiscal WHERE id = ?";

            try ( PreparedStatement query = con.prepareStatement(sql)) {
                query.setInt(1, pk);//coloca a pk que foi colocada como parametro do metodo

                try ( ResultSet rs = query.executeQuery()) {

                    if (rs.next()) {//passa o cursor para a primeira linha
                        notaFiscal = new NotaFiscal();
                        notaFiscal.setId(rs.getInt("id"));
                        notaFiscal.setDataEmissao(rs.getDate("dataEmissao"));

                        ClienteDAO clienteDao = new ClienteDAO(con);
                        notaFiscal.setCliente(clienteDao.retrive(rs.getInt("cliente_id")));                        

                    }
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return notaFiscal;
    }

    @Override
    public void update(NotaFiscal entity) {
        String sql = "UPDATE notafiscal SET dataEmissao = ?, cliente_id = ? WHERE id = ?";

        try ( PreparedStatement query = con.prepareStatement(sql)) {
            query.setDate(1, (Date) entity.getDataEmissao());
            query.setInt(2, entity.getCliente().getId());
            query.setInt(3, entity.getId());
            query.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer pk) {
        String sql = "DELETE FROM notafiscal WHERE id = ?";

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
    public List<NotaFiscal> findAll() {
        List<NotaFiscal> notasFiscais = new LinkedList<>();

        String sql = "SELECT id, dataEmissao, cliente_id FROM notafiscal";

        try ( PreparedStatement query = con.prepareStatement(sql)) {
            try ( ResultSet rs = query.executeQuery()) {
                while (rs.next()) {
                    NotaFiscal notaFiscal = new NotaFiscal();
                    notaFiscal.setId(rs.getInt("id"));
                    notaFiscal.setDataEmissao(rs.getDate("dataEmissao"));

                    ClienteDAO dao = new ClienteDAO(con);
                    notaFiscal.setCliente(dao.retrive(rs.getInt("cliente_id")));

                    ClienteDAO clienteDao = new ClienteDAO(con);
                    notaFiscal.setCliente(clienteDao.retrive(rs.getInt("cliente_id")));

                    /*ItemNotaDAO itemNotaDao = new ItemNotaDAO(con);
                        notaFiscal.setListaItens(itemNotaDao.findAllWithIDNota(rs.getInt("id")));*/
                    notasFiscais.add(notaFiscal);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return notasFiscais;
    }
}
