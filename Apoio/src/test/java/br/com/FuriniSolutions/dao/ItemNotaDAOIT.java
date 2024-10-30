package br.com.FuriniSolutions.dao;

import br.com.FuriniSolutions.bean.Cliente;
import br.com.FuriniSolutions.bean.ItemNota;
import br.com.FuriniSolutions.bean.NotaFiscal;
import br.com.FuriniSolutions.bean.Produto;
import br.com.FuriniSolutions.util.ConnectionsFactory;
import br.com.FuriniSolutions.util.DataUtil;
import java.sql.Connection;
import java.util.List;
import javax.sql.ConnectionPoolDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para ItemNotaDAO.
 */
public class ItemNotaDAOIT {

    @BeforeEach
    public void setUp() {
        System.out.println("INICIANDO TESTES DE ITEM NOTA");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("FINALIZANDO TESTES DE ITEM NOTA");
    }

    @Test
    public void testCreate() {
        System.out.println("TESTANDO CREATE");

        try (Connection connection = ConnectionsFactory.createConnetionToMySQL()) {
            ProdutoDAO produtoDAO = new ProdutoDAO(connection);
            ClienteDAO clienteDAO = new ClienteDAO(connection);
            NotaFiscalDAO notaFiscalDAO = new NotaFiscalDAO(connection);
            ItemNotaDAO itemNotaDAO = new ItemNotaDAO(connection);

            // Criando produto
            Produto produto = new Produto();
            produto.setDescricao("Produto Teste");
            produto.setValor(20.0);
            produtoDAO.create(produto);

            // Criando cliente e nota fiscal
            Cliente cliente = new Cliente();
            cliente.setNome("Cliente Teste");
            cliente.setEndereco("Rua Exemplo");
            clienteDAO.create(cliente);

            NotaFiscal notaFiscal = new NotaFiscal();
            notaFiscal.setDataEmissao(DataUtil.dataAtual());
            notaFiscal.setCliente(cliente);
            notaFiscalDAO.create(notaFiscal);

            // Criando item nota
            ItemNota itemNota = new ItemNota();
            itemNota.setProduto(produto);
            itemNota.setNotaFiscal(notaFiscal);
            itemNota.setQuantidade(5);
            itemNota.setValorItem(100.0);

            itemNotaDAO.create(itemNota);

            // Verificando se o ID foi gerado
            assertNotNull(itemNota.getId(), "O ID do ItemNota não deve ser nulo após a criação.");
            System.out.println(itemNota);
        } catch (Exception e) {
            System.err.println("Erro durante o teste de criação de ItemNota: " + e.getMessage());
        }
    }

    @Test
    public void testRetrieve() {
        System.out.println("TESTANDO RETRIEVE");

        try (Connection connection = ConnectionsFactory.createConnetionToMySQL()) {
            ProdutoDAO produtoDAO = new ProdutoDAO(connection);
            ClienteDAO clienteDAO = new ClienteDAO(connection);
            NotaFiscalDAO notaFiscalDAO = new NotaFiscalDAO(connection);
            ItemNotaDAO itemNotaDAO = new ItemNotaDAO(connection);

            // Criando e salvando um itemNota para teste
            Produto produto = new Produto();
            produto.setDescricao("Produto Teste");
            produto.setValor(20.0);
            produtoDAO.create(produto);

            Cliente cliente = new Cliente();
            cliente.setNome("Cliente Teste");
            cliente.setEndereco("Rua Exemplo");
            clienteDAO.create(cliente);

            NotaFiscal notaFiscal = new NotaFiscal();
            notaFiscal.setDataEmissao(DataUtil.dataAtual());
            notaFiscal.setCliente(cliente);
            notaFiscalDAO.create(notaFiscal);

            ItemNota itemNota = new ItemNota();
            itemNota.setProduto(produto);
            itemNota.setNotaFiscal(notaFiscal);
            itemNota.setQuantidade(3);
            itemNota.setValorItem(60.0);
            itemNotaDAO.create(itemNota);

            // Recuperando o itemNota
            ItemNota retrievedItemNota = itemNotaDAO.retrive(itemNota.getId());
            assertNotNull(retrievedItemNota, "O ItemNota não deve ser nulo.");
            assertEquals(itemNota.getId(), retrievedItemNota.getId(), "O ID deve corresponder.");
        } catch (Exception e) {
            System.err.println("Erro durante o teste de recuperação de ItemNota: " + e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        System.out.println("TESTANDO UPDATE");

        try (Connection connection = ConnectionsFactory.createConnetionToMySQL()) {
            ProdutoDAO produtoDAO = new ProdutoDAO(connection);
            ClienteDAO clienteDAO = new ClienteDAO(connection);
            NotaFiscalDAO notaFiscalDAO = new NotaFiscalDAO(connection);
            ItemNotaDAO itemNotaDAO = new ItemNotaDAO(connection);

            // Criando produto e nota fiscal para teste
            Produto produto = new Produto();
            produto.setDescricao("Produto Teste");
            produto.setValor(30.0);
            produtoDAO.create(produto);

            Cliente cliente = new Cliente();
            cliente.setNome("Cliente Teste");
            cliente.setEndereco("Rua Exemplo");
            clienteDAO.create(cliente);

            NotaFiscal notaFiscal = new NotaFiscal();
            notaFiscal.setDataEmissao(DataUtil.dataAtual());
            notaFiscal.setCliente(cliente);
            notaFiscalDAO.create(notaFiscal);

            ItemNota itemNota = new ItemNota();
            itemNota.setProduto(produto);
            itemNota.setNotaFiscal(notaFiscal);
            itemNota.setQuantidade(2);
            itemNota.setValorItem(60.0);
            itemNotaDAO.create(itemNota);

            // Atualizando o itemNota
            itemNota.setQuantidade(10);
            itemNotaDAO.update(itemNota);

            // Verificando se a atualização foi bem-sucedida
            ItemNota updatedItemNota = itemNotaDAO.retrive(itemNota.getId());
            assertEquals(10, updatedItemNota.getQuantidade(), "A quantidade deve ter sido atualizada.");
        } catch (Exception e) {
            System.err.println("Erro durante o teste de atualização de ItemNota: " + e.getMessage());
        }
    }

    @Test
    public void testDelete() {
        System.out.println("TESTANDO DELETE");

        try (Connection connection = ConnectionsFactory.createConnetionToMySQL()) {
            ProdutoDAO produtoDAO = new ProdutoDAO(connection);
            ClienteDAO clienteDAO = new ClienteDAO(connection);
            NotaFiscalDAO notaFiscalDAO = new NotaFiscalDAO(connection);
            ItemNotaDAO itemNotaDAO = new ItemNotaDAO(connection);

            // Criando produto e nota fiscal para teste
            Produto produto = new Produto();
            produto.setDescricao("Produto Teste");
            produto.setValor(20.0);
            produtoDAO.create(produto);

            Cliente cliente = new Cliente();
            cliente.setNome("Cliente Teste");
            cliente.setEndereco("Rua Exemplo");
            clienteDAO.create(cliente);

            NotaFiscal notaFiscal = new NotaFiscal();
            notaFiscal.setDataEmissao(DataUtil.dataAtual());
            notaFiscal.setCliente(cliente);
            notaFiscalDAO.create(notaFiscal);

            ItemNota itemNota = new ItemNota();
            itemNota.setProduto(produto);
            itemNota.setNotaFiscal(notaFiscal);
            itemNota.setQuantidade(5);
            itemNota.setValorItem(100.0);
            itemNotaDAO.create(itemNota);

            // Deletando o itemNota
            boolean result = itemNotaDAO.delete(itemNota.getId());
            assertTrue(result, "O ItemNota deve ser deletado com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro durante o teste de deleção de ItemNota: " + e.getMessage());
        }
    }

    @Test
    public void testFindAll() {
        System.out.println("TESTANDO FINDALL");

        try (Connection connection = ConnectionsFactory.createConnetionToMySQL()) {
            ItemNotaDAO itemNotaDAO = new ItemNotaDAO(connection);

            List<ItemNota> itemNotas = itemNotaDAO.findAll();
            assertNotNull(itemNotas, "A lista de ItemNota não deve ser nula.");
            assertFalse(itemNotas.isEmpty(), "A lista de ItemNota não deve estar vazia.");
            itemNotas.forEach(itemNota -> System.out.println(itemNota));
        } catch (Exception e) {
            System.err.println("Erro durante o teste de listagem de todos os ItemNotas: " + e.getMessage());
        }
    }

    @Test
public void testFindAllWithIDNota() {
    System.out.println("TESTANDO FINDALLWITHIDNOTA");

    try (Connection connection = ConnectionsFactory.createConnetionToMySQL()) {
        ProdutoDAO produtoDAO = new ProdutoDAO(connection);
        ClienteDAO clienteDAO = new ClienteDAO(connection);
        NotaFiscalDAO notaFiscalDAO = new NotaFiscalDAO(connection);
        ItemNotaDAO itemNotaDAO = new ItemNotaDAO(connection);

        // Criando produto
        Produto produto = new Produto();
        produto.setDescricao("Produto Teste");
        produto.setValor(20.0);
        produtoDAO.create(produto);

        // Criando cliente e nota fiscal
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setEndereco("Rua Exemplo");
        clienteDAO.create(cliente);

        NotaFiscal notaFiscal = new NotaFiscal();
        notaFiscal.setDataEmissao(DataUtil.dataAtual());
        notaFiscal.setCliente(cliente);
        notaFiscalDAO.create(notaFiscal);

        // Criando item nota
        ItemNota itemNota = new ItemNota();
        itemNota.setProduto(produto);
        itemNota.setNotaFiscal(notaFiscal);
        itemNota.setQuantidade(5);
        itemNota.setValorItem(100.0);
        itemNotaDAO.create(itemNota);

        // Chamando o método findAllWithIDNota
        List<ItemNota> retrievedItemNotas = itemNotaDAO.findAllWithIDNota(notaFiscal.getId());

        // Verificando se o item foi retornado
        assertNotNull(retrievedItemNotas, "A lista de ItemNota não deve ser nula.");
        assertFalse(retrievedItemNotas.isEmpty(), "A lista de ItemNota não deve estar vazia.");
        assertEquals(1, retrievedItemNotas.size(), "Deve haver um item na lista.");
        ItemNota retrievedItemNota = retrievedItemNotas.get(0);

        // Verificando os valores
        assertEquals(itemNota.getId(), retrievedItemNota.getId(), "O ID do ItemNota deve corresponder.");
        assertEquals(itemNota.getQuantidade(), retrievedItemNota.getQuantidade(), "A quantidade deve corresponder.");
        assertEquals(itemNota.getValorItem(), retrievedItemNota.getValorItem(), "O valor do item deve corresponder.");
    } catch (Exception e) {
        System.err.println("Erro durante o teste de busca de ItemNotas com IDNota: " + e.getMessage());
    }
}


}
