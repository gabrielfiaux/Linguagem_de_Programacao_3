/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifpr.generics;

/**
 * 
 * @author efbaro
 */
public class Teste {
    
    public static void main(String[] args) {
        
        Generic<String> g = new Generic();
        
        g.add("Teste");
        
        System.out.println(g.getValue());
    }
}
