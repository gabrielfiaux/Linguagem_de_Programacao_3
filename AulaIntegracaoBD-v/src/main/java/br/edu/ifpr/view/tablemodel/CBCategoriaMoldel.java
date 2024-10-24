/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifpr.view.tablemodel;

import br.edu.ifpr.bean.Categoria;
import java.util.Collection;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Aluno
 */
public class CBCategoriaMoldel extends DefaultComboBoxModel<Categoria>{

    public CBCategoriaMoldel(Vector<Categoria> items) {
        super(items);
    }

    public CBCategoriaMoldel() {
    }

    
    @Override
    public Categoria getSelectedItem() {
        return (Categoria) super.getSelectedItem();
    }
    
    
    
}
