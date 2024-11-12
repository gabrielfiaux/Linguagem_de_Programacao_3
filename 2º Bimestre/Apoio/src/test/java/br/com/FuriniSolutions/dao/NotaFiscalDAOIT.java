package br.com.FuriniSolutions.dao;

import br.com.FuriniSolutions.bean.Cliente;
import br.com.FuriniSolutions.bean.ItemNota;
import br.com.FuriniSolutions.bean.NotaFiscal;
import br.com.FuriniSolutions.bean.Produto;
import br.com.FuriniSolutions.util.ConnectionsFactory;
import br.com.FuriniSolutions.util.DataUtil;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Order;

/**
 * Classe de testes para NotaFiscalDAO.
 */
public class NotaFiscalDAOIT {

    @BeforeEach
    public void setUp() {
        System.out.println("INICIANDO TESTES DE NOTA FISCAL");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("FINALIZANDO TESTES DE NOTA FISCAL");
    }

    @Test
    public void testCreateNotaFiscal() {
        System.out.println("TESTANDO O CREATE");

        try (Connection connection = ConnectionsFactory.createConnetionToMySQL()) {
            ClienteDAO clienteDAO = new ClienteDAO(connection);
            ProdutoDAO produtoDAO = new ProdutoDAO(connection);
            NotaFiscalDAO notaFiscalDAO = new NotaFiscalDAO(connection);

            // Criando cliente
            Cliente c = new Cliente();
            c.setNome("Ana");
            c.setEndereco("Rua das flores, 123");
            clienteDAO.create(c);

            // Criando nota fiscal e associando ao cliente
            NotaFiscal nf = new NotaFiscal();
            nf.setDataEmissao(DataUtil.dataAtual());
            nf.setCliente(c);

            // Cria uma lista de itens da nota
            ItemNota item1 = new ItemNota();
            item1.setQuantidade(10);
            item1.setValorItem(100.0);

            Produto p = new Produto();
            p.setDescricao("cadeira");
            p.setValor(100.00);
            produtoDAO.create(p);
            item1.setProduto(p);

            ItemNota item2 = new ItemNota();
            item2.setQuantidade(5);
            item2.setValorItem(50.0);

            Produto p2 = new Produto();
            p2.setDescricao("Bolsa");
            p2.setValor(98.00);
            produtoDAO.create(p2);
            item2.setProduto(p2);

            nf.setListaItens(Arrays.asList(item1, item2));

            notaFiscalDAO.create(nf);

            // Verificando se a nota fiscal foi criada e se o ID foi gerado
            System.out.println(nf);
            assertNotNull(nf.getId(), "O ID da NotaFiscal não deve ser nulo após a criação.");
        } catch (Exception e) {
            System.err.println("Erro durante o teste de criação de nota fiscal: " + e.getMessage());
        }
    }

    @Test
    public void testRetrieveNotaFiscal() {
        System.out.println("TESTANDO O RETRIEVE");

        try (Connection connection = ConnectionsFactory.createConnetionToMySQL()) {
            NotaFiscalDAO notaFiscalDAO = new NotaFiscalDAO(connection);

            // Recuperando nota fiscal pelo ID
            NotaFiscal nf = notaFiscalDAO.retrive(1);
            System.out.println(nf);
            assertNotNull(nf, "A NotaFiscal não deve ser nula.");
        } catch (Exception e) {
            System.err.println("Erro durante o teste de recuperação de nota fiscal: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateNotaFiscal() {
        System.out.println("TESTANDO O UPDATE");

        try (Connection connection = ConnectionsFactory.createConnetionToMySQL()) {
            ClienteDAO clienteDAO = new ClienteDAO(connection);
            NotaFiscalDAO notaFiscalDAO = new NotaFiscalDAO(connection);

            // Criando cliente inicial
            Cliente c = new Cliente();
            c.setNome("João Henrique");
            c.setEndereco("Rua das palmeiras, 321");
            clienteDAO.create(c);

            // Criando nota fiscal associada ao cliente
            NotaFiscal nf = new NotaFiscal();
            nf.setDataEmissao(DataUtil.dataAtual());
            nf.setCliente(c);
            notaFiscalDAO.create(nf);
            System.out.println(nf);

            // Criando um novo cliente e atualizando a nota fiscal para este novo cliente
            Cliente c1 = new Cliente("Henrique", "Rua America, 852");
            clienteDAO.create(c1);
            nf.setCliente(c1);
            notaFiscalDAO.update(nf);
            System.out.println(nf);

            // Verificando se o cliente foi atualizado corretamente na nota fiscal
            NotaFiscal updatedNotaFiscal = notaFiscalDAO.retrive(nf.getId());
            assertEquals(c1.getId(), updatedNotaFiscal.getCliente().getId(), "O cliente da nota fiscal deve ser atualizado.");
        } catch (Exception e) {
            System.err.println("Erro durante o teste de atualização de nota fiscal: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteNotaFiscal() {
        System.out.println("TESTANDO O DELETE");

        try (Connection connection = ConnectionsFactory.createConnetionToMySQL()) {
            ClienteDAO clienteDAO = new ClienteDAO(connection);
            NotaFiscalDAO notaFiscalDAO = new NotaFiscalDAO(connection);

            // Criando cliente
            Cliente c = new Cliente();
            c.setNome("Cliente para deletar");
            c.setEndereco("Rua do Teste de Deleção, 123");
            clienteDAO.create(c);

            // Criando nota fiscal associada ao cliente
            NotaFiscal nf = new NotaFiscal();
            nf.setDataEmissao(DataUtil.dataAtual());
            nf.setCliente(c);
            notaFiscalDAO.create(nf);

            // Deletando a nota fiscal e verificando se foi deletada com sucesso
            boolean deleteSuccess = notaFiscalDAO.delete(nf.getId());
            assertTrue(deleteSuccess, "A NotaFiscal deve ser deletada com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro durante o teste de deleção de nota fiscal: " + e.getMessage());
        }
    }

    @Test
    public void testFindAllNotaFiscal() {
        System.out.println("TESTANDO FIND ALL");

        try (Connection connection = ConnectionsFactory.createConnetionToMySQL()) {
            NotaFiscalDAO notaFiscalDAO = new NotaFiscalDAO(connection);

            // Buscando todas as notas fiscais e verificando se a lista não está vazia
            List<NotaFiscal> notasFiscais = notaFiscalDAO.findAll();
            assertNotNull(notasFiscais, "A lista de NotaFiscal não deve ser nula.");
            assertFalse(notasFiscais.isEmpty(), "A lista de NotaFiscal não deve estar vazia.");
            notasFiscais.forEach(notaFiscal -> System.out.println(notaFiscal));
        } catch (Exception e) {
            System.err.println("Erro durante o teste de busca de todas as notas fiscais: " + e.getMessage());
        }
    }
}
