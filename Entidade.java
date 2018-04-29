
/**
 * Write a description of class Entidade here.
 *
 * @author RV
 * @version 16/04/2018
 */

import java.util.ArrayList;

public class Entidade{
    private String nif;
    private String email;
    private String nome;
    private String morada;
    private String password;
    private ArrayList<Integer> listaFaturas;

    public Entidade(){
        this.nif = "";
        this.email = "";
        this.nome = "";
        this.morada = "";
        this.password = "";
        this.listaFaturas = new ArrayList<Integer>();
    }

    public Entidade(String nif, String email, String nome, String morada, String password, ArrayList<Integer> listaFaturas){
        this.nif = nif;
        this.email = email;
        this.nome = nome;
        this.morada = morada;
        this.password = password;
        this.listaFaturas = listaFaturas;
    }

    public Entidade(Entidade e){
        this.nif = e.getNIF();
        this.email = e.getEmail();
        this.nome = e.getNome();
        this.morada = e.getMorada();
        this.password = e.getPassword();
        this.listaFaturas = e.getListaFaturas();
    }

    public String getNIF(){
        return this.nif;
    }

    public String getEmail(){
        return this.email;
    }

    public String getNome(){
        return this.nome;
    }

    public String getMorada(){
        return this.morada;
    }

    public String getPassword(){
        return this.password;
    }

    public ArrayList<Integer> getListaFaturas(){
        ArrayList<Integer> res = new ArrayList<Integer>();
        for(Integer x: this.listaFaturas)
            res.add(x);
        return res;
    }

    public void setNIF(String nif){
        this.nif = nif;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setMorada(String morada){
        this.morada = morada;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void adicionarFatura(int fatura){
        this.listaFaturas.add(fatura);
    }

    public Entidade clone(){
    	return new Entidade(this);
    }

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
