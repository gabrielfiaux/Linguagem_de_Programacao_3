/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfacegraficalll;

/**
 *
 * @author gfiau
 */
public class Cliente {
    private int id;
    private String nome;
    private String telResidencial;
    private String telComercial;
    private String telCelular;
    private String email;

    public Cliente(int id, String nome, String telResidencial, String telComercial, String telCelular, String email) {
        this.id = id;
        this.nome = nome;
        this.telResidencial = telResidencial;
        this.telComercial = telComercial;
        this.telCelular = telCelular;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelResidencial() {
        return telResidencial;
    }

    public String getTelComercial() {
        return telComercial;
    }

    public String getTelCelular() {
        return telCelular;
    }

    public String getEmail() {
        return email;
    }
    
    
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelResidencial(String telResidencial) {
        this.telResidencial = telResidencial;
    }

    public void setTelComercial(String telComercial) {
        this.telComercial = telComercial;
    }

    public void setTelCelular(String telCelular) {
        this.telCelular = telCelular;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    
}
