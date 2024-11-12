/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifpr.test;

import br.edu.ifpr.bean.Artista;
import br.edu.ifpr.bean.Cd;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *
 * @author Aluno
 */
public class TesteDePersistencia {
    
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ifpr_db");
        EntityManager em =  emf.createEntityManager();
        
        Cd cd1 = new Cd();
        cd1.setTitulo("CD123");
        
        Cd cd2 = new Cd();
        cd2.setTitulo("CD321");
        
        Artista artista1 = new Artista();
        artista1.setNome("Vocalista do CD123 e tambem 321");
        artista1.setIdade(32);
        artista1.addCd(cd1);
        artista1.addCd(cd2);
        
        cd1.setArtista(artista1);
        cd2.setArtista(artista1);

        em.getTransaction().begin();
        em.persist(artista1);
        em.getTransaction().commit();
        em.close();
        emf.close();
        
    }
    
}
