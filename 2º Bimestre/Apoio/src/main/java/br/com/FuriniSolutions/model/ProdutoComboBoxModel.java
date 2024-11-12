/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.FuriniSolutions.model;

import br.com.FuriniSolutions.bean.Produto;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author lucas
 */
public class ProdutoComboBoxModel extends DefaultComboBoxModel<Produto> {

    private List<Produto> produtos;
    private Produto selectedProduto;
    private List<ListDataListener> dataListeners = new ArrayList<>(); // Lista para armazenar os listeners

    public ProdutoComboBoxModel(List<Produto> produtos) {
        super();
        for (Produto produto : produtos) {
            this.addElement(produto);
        }
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (anItem instanceof Produto) {
            super.setSelectedItem(anItem);
        }
    }

    public void setProdutos(List<Produto> produtos) {
        removeAllElements();

        for (Produto produto : produtos) {
            this.addElement(produto);
        }
    }
}
