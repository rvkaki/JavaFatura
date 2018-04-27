
/**
 * Write a description of class Fatura here.
 *
 * @author RV
 * @version 15/04/2018  
 */

import java.time.LocalDate;

public class Fatura{
    private String nifEmitente;
    private LocalDate data;
    private String nifCliente;
    private String descricao;
    private String atividade;
    private double valor;

    public Fatura(){
    	this.nifEmitente = "";
    	this.data = LocalDate.now();
    	this.nifCliente = "";
    	this.descricao = "";
    	this.atividade = "";
    	this.valor = 0.0;
    }

    public Fatura(String nifEmitente, LocalDate data, String nifCliente, String descricao, String atividade, double valor){
    	this.nifEmitente = nifEmitente;
    	this.data = data;
    	this.nifCliente = nifCliente;
    	this.descricao = descricao;
    	this.atividade = atividade;
    	this.valor = valor;
    }

    public Fatura(Fatura f){
    	this.nifEmitente = f.getNIFEmitente();
    	this.data = f.getData();
    	this.nifCliente = f.getNIFCliente();
    	this.descricao = f.getDescricao();
    	this.atividade = f.getAtividade();
    	this.valor = f.getValor();
    }

    public String getNIFEmitente(){
    	return this.nifEmitente;
    }

    public LocalDate getData(){
    	return this.data;
    }

	public String getNIFCliente(){
    	return this.nifCliente;
    }    

    public String getDescricao(){
    	return this.descricao;
    }

    public String getAtividade(){
    	return this.atividade;
    }

    public double getValor(){
    	return this.valor;
    }

    public boolean estaPendente(){
    	return this.atividade.equals("");
    }

    public Fatura clone(){
    	return new Fatura(this);
    }

    public boolean equals(Object o){
    	if(this == o)
            return true;
        
        if( (o == null) || (this.getClass() != o.getClass()))
            return false;

        Fatura f = (Fatura) o;
        return(this.nifEmitente.equals(f.getNIFEmitente()) &&
        	   this.data.equals(f.getData()) &&
        	   this.nifCliente.equals(f.getNIFCliente()) &&
        	   this.descricao.equals(f.getDescricao()) &&
        	   this.atividade.equals(f.getAtividade()) &&
        	   this.valor == f.getValor());

    }
}
