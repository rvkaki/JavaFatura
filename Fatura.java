
/**
 * Classe que representa a fatura de uma entidade
 *
 * @author GC-JRI-RV
 * @version 15/04/2018  
 */

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Fatura implements Serializable{
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
    /** Atividades económicas anteriores */
    private ArrayList<String> atividadesAnteriores;
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
        this.atividadesAnteriores = new ArrayList<String>();
        this.valor = 0.0;
    }
    /**
     * Construtor por parametro
     * @param nifEmitente
     * @param data
     * @param nifCliente
     * @param descricao
     * @param atividade
     * @param atividadesAnteriores
     * @param valor
     */
    public Fatura(String nifEmitente, LocalDateTime data, String nifCliente, String descricao, String atividade, ArrayList<String> atividadesAnteriores, double valor){
    	this.nifEmitente = nifEmitente;
    	this.data = data;
    	this.nifCliente = nifCliente;
    	this.descricao = descricao;
        this.atividade = atividade;
        this.atividadesAnteriores = new ArrayList<String>(atividadesAnteriores.size());
        for (String s: atividadesAnteriores)
            this.atividadesAnteriores.add(s);
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
        this.atividadesAnteriores = new ArrayList<String>(f.getAtividadesAnteriores().size());
        for (String s: f.getAtividadesAnteriores())
            this.atividadesAnteriores.add(s);
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
     * Devolve as atividades económicas anteriores
     * @return atividades económicas anteriores
     */
    public ArrayList<String> getAtividadesAnteriores() {
        ArrayList<String> res = new ArrayList<String>(this.atividadesAnteriores.size());
        for (String s: this.atividadesAnteriores)
            res.add(s);

        return res;
    }

    /**
     * Devolve o valor
     * @return valor
     */
    public double getValor(){
    	return this.valor;
    }

    /**
     * Atualiza valor de atividade
     * @param atividade
     */
    public void setAtividade(String atividade){
        this.atividadesAnteriores.add(this.atividade);
        this.atividade = atividade;
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
        s.append("Fatura emitida por " + this.nifEmitente + " em " + this.data.toLocalDate() + " às " + this.data.toLocalTime() + "\n");
        s.append("Ao contribuinte " + this.nifCliente + " no valor de " + this.valor + "€\n");
        s.append("Descrição: " + this.descricao + "\n");
        if (this.estaPendente())
            s.append("Atividade económica atual: (PENDENTE)\n");
        else
            s.append("Atividade económica atual: " + this.atividade + "\n");
        s.append("Histórico de atividades económicas:\n");
        for (String atv: this.atividadesAnteriores) {
            if (atv.equals(""))
                s.append("\t-> (PENDENTE)\n");
            else
                s.append("\t-> " + atv + "\n");

        }
        return s.toString();
    }
}
