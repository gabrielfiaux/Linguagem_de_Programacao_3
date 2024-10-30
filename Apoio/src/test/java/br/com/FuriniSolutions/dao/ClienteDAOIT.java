/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package br.com.FuriniSolutions.dao;

import br.com.FuriniSolutions.bean.Cliente;
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
public class ClienteDAOIT {
    
    private ClienteDAO clienteDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() {
        // Inicializando a conexão com o banco de dados
        connection = ConnectionsFactory.createConnetionToMySQL();
        clienteDAO = new ClienteDAO(connection);      
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
    public void testCreateCliente() {
        System.out.println("TESTANDO O CREATE");

        // Criando cliente
        Cliente c = new Cliente();
        c.setNome("Joaquim");
        c.setEndereco("Rua das fevereiro, 123");
        clienteDAO.create(c);

        // Verificando se a nota fiscal foi criada e se o ID foi gerado
        assertNotNull(c.getId(), "O ID do Cliente não deve ser nulo após a criação.");
        System.out.println(c);
    }

    @Test
    public void testRetrieveCliente() {
        System.out.println("TESTANDO O RETRIEVE");

        // Recuperando nota fiscal pelo ID
        Cliente c = clienteDAO.retrive(1);
        assertNotNull(c, "O Cliente não deve ser nulo.");
        System.out.println(c);
    }

    @Test
    public void testUpdateCliente() {
        System.out.println("TESTANDO O UPDATE");

        // Criando cliente inicial
        Cliente c = new Cliente();
        c.setNome("João Henrique");
        c.setEndereco("Rua das palmeiras, 321");
        clienteDAO.create(c);

        // Atualizando as informações de cliente c
        c.setNome("Teste do update");
        clienteDAO.update(c);
        System.out.println(c);

        // Verificando se o cliente foi atualizado corretamente
        Cliente updatedNotaFiscal = clienteDAO.retrive(c.getId());
        assertEquals(c.getNome(), updatedNotaFiscal.getNome(), "O cliente deve ser atualizado.");
    }

    @Test
    public void testDeleteCliente() {
        System.out.println("TESTANDO O DELETE");

        // Criando cliente
        Cliente c = new Cliente();
        c.setNome("Cliente para deletar");
        c.setEndereco("Rua do Teste de Deleção, 123");
        clienteDAO.create(c);

        // Deletando a nota fiscal e verificando se foi deletada com sucesso
        boolean deleteSuccess = clienteDAO.delete(c.getId());
        assertTrue(deleteSuccess, "O cliente deve ser deletado com sucesso.");
    }

    @Test
    public void testFindAllCliente() {
        System.out.println("TESTANDO O FINDALL");

        // Buscando todas as notas fiscais e verificando se a lista não está vazia
        List<Cliente> clientes = clienteDAO.findAll();
        assertNotNull(clientes, "A lista de Cliente não deve ser nula.");
        assertFalse(clientes.isEmpty(), "A lista de Cliente não deve estar vazia.");
        clientes.forEach(cliente -> System.out.println(cliente));
    }
    
}
