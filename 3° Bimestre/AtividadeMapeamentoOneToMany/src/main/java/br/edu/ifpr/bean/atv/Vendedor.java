/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifpr.bean.atv;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author Aluno
 */
@Entity
public class Vendedor extends Funcionario {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     
    @Basic
    private double meta_salarial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getMeta_salarial() {
        return meta_salarial;
    }

    public void setMeta_salarial(double meta_salarial) {
        this.meta_salarial = meta_salarial;
    }
    
    

}
