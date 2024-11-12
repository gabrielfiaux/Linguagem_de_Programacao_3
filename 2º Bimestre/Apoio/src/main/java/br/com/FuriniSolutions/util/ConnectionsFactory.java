package br.com.FuriniSolutions.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * Classe de conexão com o banco de dados Usuário: root Senha: ""
 *
 * utilizamos esta estrutura para evitar repetição de código e simplificar a
 * obtenção de conexão com o banco de dados
 *
 */
public class ConnectionsFactory {

    // nome do usuario  de acesso do Mysql
    private static final String USERNAME = "root"; // final é uma variável constante - variável que não altera - todos os objetos compartilha a mesma variável

    // senha de acesso do mysql
    private static final String PASSWORD = "";

    // String de conexão com o banco de dados
    // porta e nome do banco de dados no qual pretende-se conectar
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/furini"; // localhost porque é onde está rodando o banco de dados dentro do xamp-htdocs

    public static Connection createConnetionToMySQL() {

        try {
            // cria a conexão com o banco de dados
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);

            return connection;

            // quandor der erro ele retorna isso
        } catch (SQLException e) {
            System.out.println("não foi possível conectar ao banco de dados");
            System.out.println(DATABASE_URL);
            System.out.println(e.getMessage());
            return null;
        }

    }

    public static void main(String[] args) {

        Connection con = createConnetionToMySQL();

        if (con != null) {
            System.out.println("Conexão realizada com sucesso" + con);

        } else {
            System.out.println("Não foi possível conectar");

        }
    }

}
