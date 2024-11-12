/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.FuriniSolutions.dao;

import br.com.FuriniSolutions.bean.Produto;
import br.com.FuriniSolutions.util.ConnectionsFactory;
import java.sql.Connection;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author lucas
 */
public class ProdutoDAOIT {

    private ProdutoDAO produtoDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() {
        // Inicializando a conexão com o banco de dados e os DAOs
        connection = ConnectionsFactory.createConnetionToMySQL();
        produtoDAO = new ProdutoDAO(connection);
    }

    @AfterEach
    public void tearDown() {
        // Fechando a conexão com o banco de dados após cada teste
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    @Test
    public void testCreateProduto() {
        System.out.println("TESTANDO O CREATE");

        // Criando produto
        Produto p = new Produto();

        p.setDescricao("cadeira");
        p.setValor(10.20);

        produtoDAO.create(p);

        // Verificando se o produto foi criado e se o ID foi gerado
        assertNotNull(p.getId(), "O ID do Produto não deve ser nulo após a criação.");
        System.out.println(p);
    }

    @Test
    public void testRetrieveProduto() {
        System.out.println("TESTANDO O RETRIEVE");

        // Recuperando produto pelo ID
        Produto p = produtoDAO.retrive(1);
        System.out.println(p);
        assertNotNull(p, "O Produto não deve ser nulo.");        
    }

    @Test
    public void testUpdateProduto() {
        System.out.println("TESTANDO O UPDATE");

        Produto p = new Produto();
        p.setDescricao("Bolsa");
        p.setValor(98.00);
        produtoDAO.create(p);
        System.out.println(p);

        p.setDescricao("testandoUpdate");
        produtoDAO.update(p);
        System.out.println(p);

        // Verificando se o cliente foi atualizado corretamente na nota fiscal
        Produto updatedProduto = produtoDAO.retrive(p.getId());
        assertEquals(p.getDescricao(), updatedProduto.getDescricao(), "O produto deve ser atualizado.");
    }

    @Test
    public void testDeleteProduto() {
        System.out.println("TESTANDO O DELETE");

        //criando o produto a ser deletado
        Produto p = new Produto();
        p.setDescricao("produto deletado");
        p.setValor(98.00);
        produtoDAO.create(p);        

        // Deletando a nota fiscal e verificando se foi deletada com sucesso
        boolean deleteSuccess = produtoDAO.delete(p.getId());
        assertTrue(deleteSuccess, "O Produto deve ser deletado com sucesso.");
    }

    @Test
    public void testFindAllNotaFiscal() {
        System.out.println("TESTANDO O FINDALL");

        // Buscando todas as notas fiscais e verificando se a lista não está vazia
        List<Produto> produtos = produtoDAO.findAll();
        assertNotNull(produtos, "A lista de NotaFiscal não deve ser nula.");
        assertFalse(produtos.isEmpty(), "A lista de NotaFiscal não deve estar vazia.");
        produtos.forEach(produto -> System.out.println(produto));
    }

}
