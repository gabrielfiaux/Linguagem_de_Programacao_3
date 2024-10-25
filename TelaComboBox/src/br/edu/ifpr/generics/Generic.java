/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifpr.generics;

/**
 *
 * @author efbaro
 */
public class Generic<T> {
    
    private T valor;
    
    public void add(T valor) {
        this.valor = valor;
    }
    
    public T getValue() {
        return this.valor;
    }
    
}
