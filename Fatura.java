
/**
 * Classe que representa a fatura de uma entidade
 *
 * @author GC-JRI-RV
 * @version 15/04/2018  
 */

import java.time.LocalDateTime;

public class Fatura{
    /** Nif do emitente */
    private String nifEmitente;
    /** Data da fatura  */
    private LocalDateTime data;
    /** Nif do cliente */
    private String nifCliente;
    /** Descrição da fatura */
    private String descricao;
    /** Atividade económica da fatura */
    private String atividade;
    /** Valor da fatura */
    private double valor;
    /**
     * Construtor por omissão
     */
    public Fatura(){
    	this.nifEmitente = "";
    	this.data = LocalDateTime.now();
    	this.nifCliente = "";
    	this.descricao = "";
    	this.atividade = "";
    	this.valor = 0.0;
    }
    /**
     * Construtor por parametro
     * @param nifEmitente
     * @param data
     * @param nifCliente
     * @param descricao
     * @param atividade
     * @param valor
     */
    public Fatura(String nifEmitente, LocalDateTime data, String nifCliente, String descricao, String atividade, double valor){
    	this.nifEmitente = nifEmitente;
    	this.data = data;
    	this.nifCliente = nifCliente;
    	this.descricao = descricao;
    	this.atividade = atividade;
    	this.valor = valor;
    }
    /**
     * Contrutor por cópia
     * @param f Fatura original
     */
    public Fatura(Fatura f){
    	this.nifEmitente = f.getNIFEmitente();
    	this.data = f.getData();
    	this.nifCliente = f.getNIFCliente();
    	this.descricao = f.getDescricao();
    	this.atividade = f.getAtividade();
    	this.valor = f.getValor();
    }
    /**
     * Devolve o nif do emitente
     * @return nif
     */
    public String getNIFEmitente(){
    	return this.nifEmitente;
    }
    /**
     * Devolve a data da emissão 
     * @return data
     */
    public LocalDateTime getData(){
    	return this.data;
    }
    /**
     * Devolve o nif do cliente
     * @return nif
     */
	public String getNIFCliente(){
    	return this.nifCliente;
    }    
    /**
     * Devolve a descrição da fatura
     * @return descrição
     */
    public String getDescricao(){
    	return this.descricao;
    }
    /**
     * Devolve a atividade económica
     * @return atividade económica
     */
    public String getAtividade(){
    	return this.atividade;
    }
    /**
     * Devolve o valor
     * @return valor
     */
    public double getValor(){
    	return this.valor;
    }
    /**
     * Verifica se esta pedente????????
     * @return true se esta pendente, false se não estiver
     */
    public boolean estaPendente(){
    	return this.atividade.equals("");
    }
    /**
     * Cria uma cópia do objecto
     * @return cópia do objeto
     */
    public Fatura clone(){
    	return new Fatura(this);
    }
    /**
     * Verifica a igualdade de dois objectos
     * @param o
     * @return true se existe igualdade, false se não houver igualdade
     */
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
    /**
     * Contrói a fatura em String
     * @return fatura em String
     */
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("Fatura emitida por " + this.nifEmitente + " em " + this.data + "\n");
        s.append("Ao contribuinte " + this.nifCliente + " no valor de " + this.valor + "\n");
        if (this.estaPendente())
            s.append("Atividade Económica: (PENDENTE)\n");
        else
            s.append("Atividade Económica: " + this.atividade + "\n");
        s.append("Descrição do emitente: " + this.descricao + "\n");
        return s.toString();
    }
}
