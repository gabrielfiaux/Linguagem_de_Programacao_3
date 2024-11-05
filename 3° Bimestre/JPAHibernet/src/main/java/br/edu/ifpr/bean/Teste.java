/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifpr.bean;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.jpa.HibernatePersistenceProvider;
public class Teste {
    public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql_db");
        EntityManager em = emf.createEntityManager();
        
        Categoria categoria = new Categoria();
        categoria.setNome("Moveis");
        em.getTransaction().begin();
        em.persist(categoria);
        em.getTransaction().commit();
        em.close();
        emf.close();
                
    }
    
    
}
