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

    public AgregadoFamiliar(){
        this.agregado = new HashMap<String,Boolean>();
        this.rendimento = 0.0;
    }

    public AgregadoFamiliar(HashMap<String,Boolean> agregado, double rendimento){
        this.agregado = new HashMap<String,Boolean>();
        for(String s: agregado.keySet())
            this.agregado.put(s, agregado.get(s));
        this.rendimento = rendimento;
    }

    public AgregadoFamiliar(AgregadoFamiliar af){
        this.agregado = af.getAgregado();
        this.rendimento = af.getRendimento();
    }

    public HashMap<String,Boolean> getAgregado(){
        HashMap<String,Boolean> res = new HashMap<String,Boolean>();
        for(String s: this.agregado.keySet())
            res.put(s, this.agregado.get(s));
        
        return res;
    }

    public double getRendimento(){
        return this.rendimento;
    }

    public void setAgregado(HashMap<String,Boolean> agregado){
        for(String s: agregado.keySet())
            this.agregado.put(s, agregado.get(s));
    }

    public void setRendimento(double rendimento){
        this.rendimento = rendimento;
    }

    public Boolean isFilho(String nif){
        return this.agregado.getOrDefault(nif, false);
    }


}
