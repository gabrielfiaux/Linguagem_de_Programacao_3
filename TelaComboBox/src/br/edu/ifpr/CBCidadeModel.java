/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpr;

import java.util.Vector;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author alunolab01
 */
public class CBCidadeModel extends DefaultComboBoxModel<Cidade>{

    public CBCidadeModel(Vector<Cidade> items) {
        super(items);
    }

    @Override
    public Cidade getSelectedItem() {
        Cidade cidade = (Cidade) super.getSelectedItem();
        return cidade;
    }
    
    
    
}
