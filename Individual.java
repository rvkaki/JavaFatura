
/**
 * Subclasse que representa a entidade individual
 *
 * @author GC-JRI-RV
 * @version 15/04/2018
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Individual extends Entidade{
    /** Numero do coeficiente fiscal */
    private double coeficienteFiscal;
    /** Map com atividades e valor deduzido para cada */
    private HashMap<String, Double> codigosAtividades;
    /** Posição do respetivo Agregado na lista de Agregados da Plataforma */
    private int indice;

    /**
     * Construtor por omissão
     */
    public Individual(){
        super();
        this.coeficienteFiscal = 0.0;
        this.codigosAtividades = new HashMap<String,Double>();
        this.indice = 0;
    }

    /**
     * Construtor por parametro
     * @param nif
     * @param email
     * @param nome
     * @param morada
     * @param password
     * @param listaFaturas
     * @param coeficienteFiscal
     * @param codigosAtividades
     * @param indice
     */
    public Individual(String nif, String email, String nome, String morada, String password, ArrayList<Integer> listaFaturas, double coeficienteFiscal, HashMap<String,Double> codigosAtividades, int indice){
        super(nif, email, nome, morada, password, listaFaturas);
        this.coeficienteFiscal = coeficienteFiscal;
        this.codigosAtividades = new HashMap<String,Double>(codigosAtividades.size());
        for(String s: codigosAtividades.keySet())
            this.codigosAtividades.put(s, codigosAtividades.get(s));
        this.indice = indice;
    }

    /**
     * Contrutor por cópia
     * @param c Individual orignal
     */
    public Individual(Individual c){
        super(c);
        this.coeficienteFiscal = c.getCoeficienteFiscal();
        this.codigosAtividades = c.getCodigosAtividades();
        this.indice = c.getIndice();
    }

    /**
     * Devolve o coeficiente fiscal
     * @return coeficiente fiscal
     */
    public double getCoeficienteFiscal(){
        return this.coeficienteFiscal;
    }

    /**
     * Devolve os codigos das atividades
     * @return codigos das atividades
     */
    public HashMap<String,Double> getCodigosAtividades(){
        HashMap<String,Double> res = new HashMap<String,Double>(this.codigosAtividades.size());
        for(String s: this.codigosAtividades.keySet())
            res.put(s, this.codigosAtividades.get(s));
        return res;
    }

    /**
     * Devolve o indice
     * @return indice
     */
    public int getIndice(){
        return this.indice;
    }

    /**
     * Define o coeficiente fiscal
     * @param coeficiente
     */
    public void setCoeficienteFiscal(double coeficiente){
        this.coeficienteFiscal = coeficiente;
    }

    /**
     * Define os codigos de atividades
     * @param codigos
     */
    public void setCodigosAtividades(HashMap<String,Double> codigos){
        HashMap<String,Double> res = new HashMap<String,Double>(codigos.size());
        for(String s: codigos.keySet())
            res.put(s, codigos.get(s));
        this.codigosAtividades = res;
    }

    /** Define o indice
     * @param indice
     */
    public void setIndice(int indice){
        this.indice = indice;
    }

    /**
     * Atualiza a entrada de uma determinada atividade no codigosAtividades
     * @param key chave
     * @param value valor
     */
    public void atualizaCodigosAtividades(String key, double valor){
        this.codigosAtividades.put(key, valor);
    }

    /**
     * Cria uma cópia do objecto
     * @return cópia do objeto
     */
    public Individual clone(){
        return new Individual(this);
    }

    /**
     * Verifica a igualdade dos objetos
     * @param o
     * @return true se houver igualdade, false se não houver igualdade
     */
    public boolean equals(Object o){
        if (this == o)
            return true;

        if ((o == null) || (this.getClass() != o.getClass()))
            return false;

        Individual i = (Individual) o;
        return(super.equals(i) &&
               this.coeficienteFiscal == i.getCoeficienteFiscal() &&
               this.codigosAtividades.equals(i.getCodigosAtividades()) &&
               this.indice == i.getIndice());
    }
}