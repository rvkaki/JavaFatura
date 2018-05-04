
/**
 * Classe que representa a entidade
 *
 * @author RV
 * @version 16/04/2018
 */

import java.io.Serializable;
import java.util.ArrayList;

public class Entidade implements Serializable{
    /** Nif da entidade */
    private String nif;
    /** Email da entidade */
    private String email;
    /** Nome da entidade */
    private String nome;
    /** Morada da entidade */
    private String morada;
    /** Password da entidade */
    private String password;
    /** Lista com as faturas da entidade */
    private ArrayList<Integer> listaFaturas;
    /**
     * Construtor por omissão
     */
    public Entidade(){
        this.nif = "";
        this.email = "";
        this.nome = "";
        this.morada = "";
        this.password = "";
        this.listaFaturas = new ArrayList<Integer>();
    }
    /**
     * Construtor por parametro 
     * @param nif
     * @param email
     * @param nome
     * @param morada
     * @param password
     * @param listaFaturas
     */
    public Entidade(String nif, String email, String nome, String morada, String password, ArrayList<Integer> listaFaturas){
        this.nif = nif;
        this.email = email;
        this.nome = nome;
        this.morada = morada;
        this.password = password;
        this.listaFaturas = listaFaturas;
    }
    /**
     * Construtor por cópia
     * @param e entidade original
     */
    public Entidade(Entidade e){
        this.nif = e.getNIF();
        this.email = e.getEmail();
        this.nome = e.getNome();
        this.morada = e.getMorada();
        this.password = e.getPassword();
        this.listaFaturas = e.getListaFaturas();
    }
    /**
     * Devolve o nif da entidade
     * @return o nif
     */
    public String getNIF(){
        return this.nif;
    }
    /** 
     * Devolve o email da entidade
     * @return email
     */
    public String getEmail(){
        return this.email;
    }
    /**
     * Devolve o nome da entidade
     * @return nome
     */
    public String getNome(){
        return this.nome;
    }
    /**
     * Devolve a morada da entidade
     * @return morada
     */
    public String getMorada(){
        return this.morada;
    }
    /**
     * Devolve a password da entidade
     * @return password
     */
    public String getPassword(){
        return this.password;
    }
    /**
     * Devolve a lista de faturas da entidade
     * @return a lista de faturas
     */
    public ArrayList<Integer> getListaFaturas(){
        ArrayList<Integer> res = new ArrayList<Integer>();
        for(Integer x: this.listaFaturas)
            res.add(x);
        return res;
    }
    /**
     * Define o nif da entidade
     * @param nif
     */
    public void setNIF(String nif){
        this.nif = nif;
    }
    /**
     * Define o email da entidade
     * @param email
     */
    public void setEmail(String email){
        this.email = email;
    }
    /**
     * Define o nome da entidade
     * @param nome
     */
    public void setNome(String nome){
        this.nome = nome;
    }
    /**
     * Define a morada da entidade
     * @param morada
     */
    public void setMorada(String morada){
        this.morada = morada;
    }
    /**
     * Define a password da entidade
     * @param password
     */
    public void setPassword(String password){
        this.password = password;
    }
    /**
     * Define a lista de faturas
     * @param fatura
     */
    public void adicionarFatura(int fatura){
        this.listaFaturas.add(fatura);
    }
    /**
     * Cria uma cópia do objeto
     * @return cópia do objeto
     */
    public Entidade clone(){
    	return new Entidade(this);
    }
    /**
     * Verifica a igualdade dos objetos
     * @param o
     * @return true se houver igualdade, false se não houver igualdade
     */
    public boolean equals(Object o){
        if(this == o)
            return true;
        
        if( (o == null) || (this.getClass() != o.getClass()))
            return false;

        Entidade e = (Entidade) o;
        return(this.nif.equals(e.getNIF()) &&
               this.email.equals(e.getEmail()) &&
               this.nome.equals(e.getNome()) &&
               this.morada.equals(e.getMorada()) &&
               this.password.equals(e.getPassword()) &&
               this.listaFaturas.equals(e.getListaFaturas()));
    }
}
