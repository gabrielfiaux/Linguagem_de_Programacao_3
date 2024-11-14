/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifpr.test;

import br.edu.ifpr.bean.atv.Endereco;
import br.edu.ifpr.bean.atv.Gerente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *
 * @author Aluno
 */
public class Main {

    public static void main(String[] args) {
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("ifpr_db");
        EntityManager em =  emf.createEntityManager();
        
        Gerente gerente = new Gerente();
        gerente.setNome("Brunão");
        
        em.getTransaction().begin();
        em.persist(gerente);
        em.getTransaction().commit();
        em.close();
        emf.close();
          
    }

}
