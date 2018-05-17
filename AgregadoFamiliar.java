/**
 * Classe que representa um Agregado Familiar.
 *
 * @author GC-JRI-RV
 * @version 14/05/2018
 */

import java.io.Serializable;
import java.util.HashMap;

public class AgregadoFamiliar implements Serializable{
    /**Map que a cada nif associa True se se tratar de um filho */
    private HashMap<String,Boolean> agregado;
    /**Rendimento do agregado familiar */
    private double rendimento;

    /**
     * Construtor por omissão
     */
    public AgregadoFamiliar(){
        this.agregado = new HashMap<String,Boolean>();
        this.rendimento = 0.0;
    }

    /**
     * Construtor por parâmetro
     * @param agregado
     * @param rendimento
     */
    public AgregadoFamiliar(HashMap<String,Boolean> agregado, double rendimento){
        this.agregado = new HashMap<String,Boolean>();
        for(String s: agregado.keySet())
            this.agregado.put(s, agregado.get(s));
        this.rendimento = rendimento;
    }

    /**
     * Construtor de cópia
     * @param af o AgregadoFamiliar original
     */
    public AgregadoFamiliar(AgregadoFamiliar af){
        this.agregado = af.getAgregado();
        this.rendimento = af.getRendimento();
    }

    /**
     * Devolve o agregado
     * @return agregado
     */
    public HashMap<String,Boolean> getAgregado(){
        HashMap<String,Boolean> res = new HashMap<String,Boolean>();
        for(String s: this.agregado.keySet())
            res.put(s, this.agregado.get(s));
        
        return res;
    }

    /**
     * Devolve o rendimento
     * @return rendimento
     */
    public double getRendimento(){
        return this.rendimento;
    }

    /**
     * Devolve o número de filhos do agregado
     * @return número de filhos
     */
    public int getNumeroFilhos(){
        int res = 0;
        for (boolean filho: this.agregado.values()) {
            if (filho)
                res += 1;
        }

        return res;
    }

    /**
     * Define o agregado
     * @param agregado
     */
    public void setAgregado(HashMap<String,Boolean> agregado){
        for(String s: agregado.keySet())
            this.agregado.put(s, agregado.get(s));
    }

    /**
     * Define o rendimento
     * @param rendimento
     */
    public void setRendimento(double rendimento){
        this.rendimento = rendimento;
    }

    /**
     * Função que diz se um dado nif é um dos filhos do agregado
     * @param agregado
     */
    public Boolean isFilho(String nif){
        return this.agregado.getOrDefault(nif, false);
    }

    /**
     * Função que atualiza o agregado, adicionando os elementos de newAgregado que ainda não estão no agregado
     * @param newAgregado
     */
    public void atualizaAgregado(HashMap<String,Boolean> newAgregado){
        for (String nif: newAgregado.keySet()) {
            if (!this.agregado.containsKey(nif))
                this.agregado.put(nif, newAgregado.get(nif));
        }
    }

    /**
     * Contrói a String do agregado
     * @return String do agregado
     */
    public String toString(){
        StringBuilder s = new StringBuilder();
        int i = 1;
        for (String str : this.agregado.keySet()) {
            if (!isFilho(str)) {
                s.append("Elemento " + i + ": " + str + "\n");
                i++;
            }
        }

        i = 1;
        for (String str : this.agregado.keySet()) {
            if (isFilho(str)) {
                s.append("Filho " + i + ": " + str + "\n");
                i++;
            }
        }

        s.append("O rendimento anual deste agregado é " + this.rendimento + "€\n");

        return s.toString();
    }
}
