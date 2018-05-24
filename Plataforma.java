
/**
 * Classe que representa a interface
 *
 * @author GC-JRI-RV
 * @version 16/04/2018
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map;
import java.util.TreeSet;
import javafx.util.Pair;
import java.util.HashMap;
import java.lang.System;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.Arrays;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Exception;

public class Plataforma{
    /**Lista de todas as faturas */
    private ArrayList<Fatura> totalFaturas;
    /** Lista de todas as entidades*/ 
    private HashMap<String,Entidade> totalEntidades;
    /** Lista de todos os Agregados */
    private ArrayList<AgregadoFamiliar> agregados;
    /**Identificação da entidade */
    private Entidade utilizador;
    /**Limite das familias numerosas*/
    private int limiteFamiliaNumerosa;
    /**Lista das atividades económicas */
    private static ArrayList<String> atividades = new ArrayList<>(Arrays.asList("despesas gerais familiares",
                                                                   "saúde", "educação", "habitação", "lares",
                                                                   "reparação de automóveis", "reparação de motociclos", 
                                                                   "restauração e alojamento", "cabeleireiros",
                                                                   "atividades veterinárias", "transportes", "outros"));
    /**Tabela para Cálculo do IRS baseando em rendimento anual e número do agregado*/                                                                   
    private static final HashMap<Integer,double[]> irs;
        static{
            HashMap<Integer,double[]> myMap = new HashMap<Integer,double[]>();
            myMap.put(8400, new double[] {2.5,0.0,0.0,0.0,0.0,0.0});
            myMap.put(12000, new double[] {8.5,5.8,4.0,1.2,0.0,0.0});
            myMap.put(18000, new double[] {12.2,10.4,8.5,6.6,4.8,2.9});
            myMap.put(24000, new double[] {16.6,14.9,14.1,11.4,10.6,8.9});
            myMap.put(36000, new double[] {22.0,21.9,20.3,18.9,17.5,17.1});
            myMap.put(60000, new double[] {27.5,26.9,26.5,24.1,23.7,23.3});
            myMap.put(120000, new double[] {34.0,33.9,33.7,32.5,32.3,30.8});
            myMap.put(240000, new double[] {37.3,37.3,37.1,36.0,35.8,34.6});
            myMap.put(300000, new double[] {39.3,39.3,39.1,38.4,38.2,36.6});
            myMap.put(300001, new double[] {41.3,41.3,41.1,40.4,40.2,39.0});
            irs = myMap;
        }
    /**Tabela de Descontos por Atividade Económica */
    private static final HashMap<String,Pair<Double, Integer>> descontos;
        static{
            HashMap<String,Pair<Double,Integer>> myMap = new HashMap<String,Pair<Double,Integer>>();
            myMap.put("despesas gerais familiares", new Pair(35.0, 500));
            myMap.put("saúde", new Pair(15.0, 1000));
            myMap.put("educação", new Pair(30.0, 800));
            myMap.put("habitação", new Pair(15.0, 500));
            myMap.put("lares", new Pair(25.0, 400));
            myMap.put("reparação de automóveis", new Pair(15.0, 200));
            myMap.put("reparação de motociclos", new Pair(15.0, 200));
            myMap.put("restauração e alojamento", new Pair(15.0, 200));
            myMap.put("cabeleireiros", new Pair(10.0, 100));
            myMap.put("atividades veterinárias", new Pair(15.0, 200));
            myMap.put("transportes", new Pair(20.0, 100));
            myMap.put("outros", new Pair(0.0, 0));
            descontos = myMap;
        }
    /**
     * Construtor por omissão
     */                                                              
   public Plataforma(){
        this.totalFaturas = new ArrayList<Fatura>();
        this.totalEntidades = new HashMap<String,Entidade>();
        this.agregados = new ArrayList<AgregadoFamiliar>();
        this.utilizador = null;
        this.limiteFamiliaNumerosa = 4;
    }

    /**
     * Ler pedido
     * @param pedido
     * @return leitura do input
     */
    public String ler(String pedido){
        if (pedido != null)
            System.out.println(pedido);

        Scanner ler = new Scanner(System.in);
        String res = ler.nextLine();
        ler.close();
        return res;
    }

    /**
     * Ler qualquer NIF válido
     * @param pedido
     * @return nif
     */
    public String lerNIF(String pedido){
        String nif;
        do{
            nif = ler(pedido);
        }while(nif.length() != 9 || (nif.charAt(0) != '1' && nif.charAt(0) != '2' && nif.charAt(0) != '5' && !nif.equals("000000000")));

        return nif;
    }

    /**
     * Ler NIF Individual
     * @param pedido
     * @return nif
     */
    public String lerNIFIndividual(String pedido){
        String nif;
        do{
            nif = ler(pedido);
        }while(nif.length() != 9 || (nif.charAt(0) != '1' && nif.charAt(0) != '2'));

        return nif;
    }

    /**
     * Ler NIF Coletivo
     * @param pedido
     * @return nif
     */
    public String lerNIFColetivo(String pedido){
        String nif;
        do{
            nif = ler(pedido);
        }while(nif.length() != 9 || nif.charAt(0) != '5');

        return nif;
    }

    /**
     * Ler int
     * @param pedido
     * @return int
     */
    public int lerInt(String pedido) {
        int res;
        while (true) {
            try {
                res = Integer.parseInt(ler(pedido));
            } catch (NumberFormatException exc) {
                System.out.println("Por favor, introduza um número");
                continue;
            }

            break;
        }

        return res;
    }

    /**
     * Ler double
     * @param pedido
     * @return double
     */
    public double lerDouble(String pedido) {
        double res;
        while (true) {
            try {
                res = Double.parseDouble(ler(pedido));
            } catch (NumberFormatException exc) {
                System.out.println("Por favor, introduza um número");
                continue;
            }

            break;
        }

        return res;
    }

    /**
     * Função para dar tempo para ler o texto impresso. Espera que o utilizador pressione Enter antes de continuar
     */
    public void pausaParaLer(){
        System.out.println();
        System.out.println("Pressione Enter para continuar...");
        ler(null);
    }

    /**
     * Grava os dados para um ficheiro
     */
    public void save(){
        try{
            FileOutputStream novoEstado = new FileOutputStream("estado.sav");
            ObjectOutputStream save = new ObjectOutputStream(novoEstado);
            save.writeObject(this.totalFaturas);
            save.writeObject(this.totalEntidades);
            save.writeObject(this.agregados);
            save.writeObject(this.limiteFamiliaNumerosa);
            save.close();
            novoEstado.close();
        }
        catch(Exception exc){
            System.out.print('\u000C');
            System.out.println("Não foi possível guardar o estado no ficheiro");
            pausaParaLer();
        }
    }

    /**
     * Carrega os dados de um ficheiro
     */
    public void load(){
        try{
            FileInputStream estado = new FileInputStream("estado.sav");
            if(estado.available() > 0){
                ObjectInputStream restore = new ObjectInputStream(estado);

                ArrayList<Fatura> totalFaturas = (ArrayList<Fatura>) restore.readObject();
                HashMap<String,Entidade> totalEntidades = (HashMap<String,Entidade>) restore.readObject();
                ArrayList<AgregadoFamiliar> agregados = (ArrayList<AgregadoFamiliar>) restore.readObject();
                int limiteFamiliaNumerosa = (int) restore.readObject();

                this.totalFaturas = totalFaturas;
                this.totalEntidades = totalEntidades;
                this.agregados = agregados;
                this.limiteFamiliaNumerosa = limiteFamiliaNumerosa;

                restore.close();
            }
            estado.close();
        }
        catch(Exception exc){
            System.out.print('\u000C');
            System.out.println("Não foi possível ler o estado do ficheiro");
            pausaParaLer();
        }
    }

    /**
     * Dá início ao menu
     * @param args os argumentos passados ao programa
     */
    public static void main(String[] args){
        Plataforma plataforma = new Plataforma();
        plataforma.load();

        boolean exit = false;
        while (!exit)
            exit = plataforma.printMenu();

        plataforma.save();
    }

    /**
     * Imprime o menu inicial
     * @return true se o utilizador quiser sair da plataforma, false caso queira continuar a utilizá-la
     */
    public boolean printMenu(){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #                JavaFatura                  #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #               1 --> Login                  #              \n");
        menu.append("               #               2 --> Registar               #              \n");
        menu.append("               #               3 --> Sair                   #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        int escolha;
        do{
            escolha = lerInt(null);
        }while(escolha != 1 && escolha != 2 && escolha != 3);

        if (escolha == 1)
            this.login();
        else if(escolha == 2) {
            this.registar();
            this.save();
        } else if (escolha == 3)
            return true;

        return false;
    }
    /**
     * Menu para registar um utilizador na plataforma
     */
    public void registar(){ 
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #                 Registar                   #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #             NIF:                           #              \n");
        menu.append("               #             Nome:                          #              \n");
        menu.append("               #             Email:                         #              \n");
        menu.append("               #             Morada:                        #              \n");
        menu.append("               #             Password:                      #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        String nif = lerNIF("Escreva o seu NIF");

        Entidade e = null;
        if (this.totalEntidades.containsKey(nif)) {
            e = this.totalEntidades.get(nif);
            if (!e.getPassword().equals("")) {
                System.out.println("NIF já registado!");
                pausaParaLer();
                return;
            }
        } else {
            if (nif.charAt(0) == '1' || nif.charAt(0) == '2')
                e = new Individual();
            else if (nif.charAt(0) == '5')
                e = new Coletivo();
            else if (nif.equals("000000000"))
                e = new Entidade();
            
            e.setNIF(nif);
        }
        
        String nome = ler("Escreva o seu nome");
        e.setNome(nome);
        String email = ler("Escreva o seu email");
        e.setEmail(email);
        String morada = ler("Escreva a sua morada");
        e.setMorada(morada);
        String password = ler("Escreva a sua password");
        e.setPassword(password);

        if(nif.charAt(0) == '1' || nif.charAt(0) == '2')
            registarIndividual((Individual) e);
        
        else if(nif.charAt(0) == '5')
            registarColetivo((Coletivo) e);

        this.totalEntidades.put(nif, e.clone());
    }
    /**
     *  Menu para registar uma entidade individual na plataforma
     * @param e
     */
    public void registarIndividual(Individual e){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #            RegistarIndividual              #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #       Número de elementos agregado:        #              \n");
        menu.append("               #       NIF dos elementos agregado:          #              \n");
        menu.append("               #       Coeficiente Fiscal:                  #              \n");
        menu.append("               #       Rendimento anual agregado:           #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);
        
        int numeroAgregado;
        do {
            numeroAgregado = lerInt("Escreva o número de elementos do seu agregado familiar (incluindo você)");
        } while (numeroAgregado <= 0);
        HashMap<String,Boolean> nifAgregado = new HashMap<String,Boolean>();
        nifAgregado.put(e.getNIF(), false);
        if (numeroAgregado > 1) {
            int numeroFilhos;
            do{
                numeroFilhos = lerInt("Escreva o número de filhos");
            }while(numeroFilhos >= numeroAgregado);

        
            if (numeroFilhos > 0){
                System.out.println("Escreva o NIF dos filhos");
                for(int i=1; i<=numeroFilhos; i++){
                    String nifFilho = lerNIFIndividual("NIF do filho " + i + ":");
                    nifAgregado.put(nifFilho, true);
                }
            }

            if(numeroAgregado - numeroFilhos > 1){
                System.out.println("Escreva o NIF dos restantes elementos do agregado familiar (excluindo você)");
                for(int i=1; i<numeroAgregado-numeroFilhos; i++){
                    String nifFamiliar = lerNIFIndividual("NIF do elemento " + i + ":");
                    nifAgregado.put(nifFamiliar, false);
                }
            }
        }
    
        double coeficiente = lerDouble("Escreva o seu coeficiente fiscal");
        double rendimentoAtual;
        double rendimentoAgregado;
        int indice = this.agregados.size();
        String nif = null;

        for(String nif1: nifAgregado.keySet())
            if(this.totalEntidades.containsKey(nif1) && ! nif1.equals(e.getNIF())){
                nif = nif1;
                indice = ((Individual) this.totalEntidades.get(nif)).getIndice();
                break;
            }
        
        if(nif != null){
            this.agregados.get(indice).atualizaAgregado(nifAgregado);

            rendimentoAtual = this.agregados.get(indice).getRendimento();
            String resposta;
            do{
                resposta = ler("Confirma que o rendimento anual do seu agregado é " + rendimentoAtual + "? (s/n)");
            }while(!resposta.equals("s") && !resposta.equals("n"));

            if(resposta.equals("n")){
                rendimentoAgregado = lerDouble("Escreva o rendimento anual do seu agregado familiar");
                this.agregados.get(indice).setRendimento(rendimentoAgregado);
            }
        }
        else {
            rendimentoAgregado = lerDouble("Escreva o rendimento anual do seu agregado familiar");
            this.agregados.add(new AgregadoFamiliar(nifAgregado, rendimentoAgregado));
        }

        e.setIndice(indice);
        e.setCoeficienteFiscal(coeficiente);
    }
    /**
     *  Menu para registar um utilizador entidade coletivo na plataforma
     * @param e
     */
    public void registarColetivo(Coletivo e){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #            RegistarColetivo                #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #         Designação:                        #              \n");
        menu.append("               #         Coeficiente Fiscal:                #              \n");
        menu.append("               #         Atividades Económicas:             #              \n");
        menu.append("               #         Distrito:                          #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);
        
        String designacao = ler("Escreva a designação da empresa");
        double coeficiente = lerDouble("Escreva o coeficiente fiscal da empresa");
        ArrayList<String> informacaoAtividades = lerAtividadesColetivo();

        String distrito = ler("Escreva o seu distrito:");

        e.setDesignacao(designacao);
        e.setInformacaoAtividades(informacaoAtividades);
        e.setCoeficienteFiscal(coeficiente);
        e.setDistrito(distrito);
    }

    /**
     * Ler atividades económicas do coletivo
     * @return lista com as atividades do coletivo
     */
    public ArrayList<String> lerAtividadesColetivo() {
        ArrayList<String> codigosAtividades = new ArrayList<String>();
        for (String atividade: this.atividades) {
            String res;
            do {
                res = ler("Atua na área de " + atividade + "? (s/n)");
            } while (!res.equals("s") && !res.equals("n"));

            if (res.equals("s"))
                codigosAtividades.add(atividade);
        }

        return codigosAtividades;
    }

    /**
     * Fazer o log out
     */
    public void logout(){
        this.utilizador = null;
        this.save();
    }
    /**
     * Fazer o log in
     */
    public void login(){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #                  Login                     #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #             NIF:                           #              \n");
        menu.append("               #             Password:                      #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        String nif = lerNIF("Escreva o seu NIF");

        if (this.totalEntidades.containsKey(nif) && !this.totalEntidades.get(nif).getPassword().equals("")) {
            Entidade e = this.totalEntidades.get(nif);
            String password = e.getPassword();
            String tentativa;
            int numTentativas = 3;
            do{
                if (numTentativas == 0) {
                    System.out.println("Excedeu o número de tentativas. Por favor, tente novamente mais tarde");
                    pausaParaLer();
                    return;
                }
                tentativa = ler("Escreva a sua password");
                numTentativas--;
            }while(! password.equals(tentativa));

            this.utilizador = e;
        } else {
            System.out.println("NIF não existente!");
            System.out.println("Confirme se o NIF que introduziu está correto. Se estiver, registe-se primeiro");
            pausaParaLer();
            return;
        }

        // Enquanto o utilizador não fizer logout
        while (this.utilizador != null) {
            if (nif.equals("000000000"))
                printMenuAdmin();
            else if (this.utilizador instanceof Individual)
                printMenuIndividual();
            else if (this.utilizador instanceof Coletivo)
                printMenuColetivo();
        }
    }
    /**
     * Imprimir o menu de uma entidade individual
     */
    public void printMenuIndividual(){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #           Contribuinte Individual          #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #        1 --> Ver Faturas                   #              \n");
        menu.append("               #        2 --> Alterar Atividade Faturas     #              \n");
        menu.append("               #        3 --> Validar Faturas               #              \n");
        menu.append("               #        4 --> Valor Dedução Fiscal          #              \n");
        menu.append("               #        5 --> Definições da conta           #              \n");
        menu.append("               #        6 --> Logout                        #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        int escolha;
        do{
            escolha = lerInt(null);
        }while(escolha != 1 && escolha != 2 && escolha != 3 && escolha != 4 && escolha != 5 && escolha != 6);

        if(escolha == 1)
            verFaturasIndividual();
        else if (escolha == 2)
            alterarAtividadeFaturas();
        else if (escolha == 3)
            validarFaturas();
        else if (escolha == 4)
            verValorDeducaoFiscal();
        else if (escolha == 5) {
            boolean exit = false;
            while (!exit)
                exit = definicoesDaConta();
        } else if (escolha == 6)
            logout();
    }

    /**
     * Ver o valor de dedução fiscal
     */
    public void verValorDeducaoFiscal() {
        double deducaoAgregado = this.getDeducaoFiscalAgregado();
        double deducaoIndividual = this.getDeducaoFiscal();
        double irs = this.getIRS() * (1 - reducaoImposto());
        double valorFinal = Math.min(deducaoAgregado, irs);
        System.out.println("O valor pago de IRS foi " + irs + "€");
        System.out.println("O valor de dedução acumulado por si é " + deducaoIndividual + "€");
        System.out.println("O valor de dedução acumulado pelo seu agregado é " + deducaoAgregado + "€");
        System.out.println("Isto resulta num valor de dedução de " + valorFinal + "€");
        pausaParaLer();
    }

    /**
     * Alterar a atividade das faturas
     */
    public void alterarAtividadeFaturas() {
        ArrayList<Fatura> faturasAlteraveis = new ArrayList<Fatura>();
        for (int i: this.utilizador.getListaFaturas()) {
            Fatura f = this.totalFaturas.get(i);
            Coletivo c = (Coletivo) this.totalEntidades.get(f.getNIFEmitente());
            if (!f.estaPendente() && c.getNumeroAtividades() > 1)
                faturasAlteraveis.add(f);
        }

        if (faturasAlteraveis.size() == 0) {
            System.out.println("Não tem faturas para alterar. Por favor, volte mais tarde");
            pausaParaLer();

            return;
        }

        System.out.println("Pode alterar as seguintes faturas:");
        int i = 0;
        for (Fatura f: faturasAlteraveis) {
            System.out.println("Fatura " + i);
            System.out.println(f);
            i++;
        }

        int escolha;
        do {
            escolha = lerInt("Qual quer alterar?");
        } while (escolha < 0 || escolha >= faturasAlteraveis.size());

        Fatura f = faturasAlteraveis.get(escolha);
        System.out.print('\u000C');
        ArrayList<String> atividades = ((Coletivo) this.totalEntidades.get(f.getNIFEmitente())).getInformacaoAtividades();
        atividades.remove(f.getAtividade());
        System.out.println(f);
        System.out.println("Pode alterar esta fatura para:");
        i = 0;
        for (String a: atividades) {
            System.out.println(i + " --> " + a);
            i++;
        }

        do {
            escolha = lerInt(null);
        } while (escolha >= atividades.size());

        HashMap<String,Double> codigos = ((Individual) this.utilizador).getCodigosAtividades();

        String chave = f.getAtividade();
        double valor = codigos.get(chave) - f.getValor();
        ((Individual) this.utilizador).atualizaCodigosAtividades(chave, valor);

        f.setAtividade(atividades.get(escolha));

        chave = f.getAtividade();
        valor = codigos.getOrDefault(chave, 0.0) + f.getValor();
        ((Individual) this.utilizador).atualizaCodigosAtividades(chave, valor);

        pausaParaLer();
    }

    /**
     * Valida as faturas pendentes
     */
    public void validarFaturas(){
        ArrayList<Fatura> faturasPendentes = new ArrayList<Fatura>();
        for(int i: this.utilizador.getListaFaturas()){
            Fatura f = this.totalFaturas.get(i);
            if (f.estaPendente())
                faturasPendentes.add(f);
        }

        if (faturasPendentes.size() == 0) {
            System.out.println("Não tem faturas para validar. Por favor, volte mais tarde");
            pausaParaLer();

            return;
        }

        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #               Validar Faturas              #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #           Tem " + faturasPendentes.size() + " faturas pendente(s)        #              \n");
        menu.append("               #           Deseja validá-la(s)?             #              \n");
        menu.append("               #           1 --> Sim                        #              \n");
        menu.append("               #           2 --> Não, regressar ao menu     #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        int escolha;
        do{
            escolha = lerInt(null);
        }while(escolha != 1 && escolha != 2);

        if(escolha == 1){
            for(Fatura f: faturasPendentes){
                System.out.print('\u000C');
                ArrayList<String> atividades = ((Coletivo) this.totalEntidades.get(f.getNIFEmitente())).getInformacaoAtividades();

                System.out.println(f.toString());
                System.out.println("Pode associar esta fatura a:");
                int i = 0;
                for(String a: atividades){
                    System.out.println(i + " --> " + a);
                    i++;
                }

                do{
                    escolha = lerInt(null);
                }while(escolha >= atividades.size());

                f.setAtividade(atividades.get(escolha));
                HashMap<String,Double> codigos = ((Individual) this.utilizador).getCodigosAtividades();
                String chave = atividades.get(escolha);
                double valor = codigos.getOrDefault(chave, 0.0) + f.getValor();
                ((Individual) this.utilizador).atualizaCodigosAtividades(chave, valor);

            }
                
        }
        else if(escolha == 2)
            return;
    }

    /**
     * Imprimir o menu de uma entidade coletiva
     */
    public void printMenuColetivo(){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #           Contribuinte Coletivo            #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #           1 --> Emitir Fatura              #              \n");
        menu.append("               #           2 --> Ver Faturas                #              \n");
        menu.append("               #           3 --> Definições da conta        #              \n");
        menu.append("               #           4 --> Valor Faturado             #              \n");
        menu.append("               #           5 --> Imposto                    #              \n");
        menu.append("               #           6 --> Logout                     #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        int escolha;
        do{
            escolha = lerInt(null);
        }while(escolha != 1 && escolha != 2 && escolha != 3 && escolha != 4 && escolha != 5 && escolha != 6);

        if(escolha == 1)
            emitirFatura();
        else if(escolha == 2)
            verFaturasColetivo();
        else if (escolha == 3) {
            boolean exit = false;
            while (!exit)
                exit = definicoesDaConta();
        }
        else if(escolha == 4)
            verValorFaturado();
        else if (escolha == 5){
            double imposto = getImpostoColetivo() * (1.0 - reducaoImposto());
            System.out.println("O valor a pagar de imposto é " + imposto + "€");
            pausaParaLer();
        }
        else if (escolha == 6)
            logout();
    }

    /**
     * Emitir uma fatura
     */
    public void emitirFatura() {
        String nifCliente = lerNIFIndividual("Escreva o NIF do cliente");
        String descricao = ler("Escreva a descrição da fatura");
        double valor;
        do {
            valor = lerDouble("Escreva o valor da fatura");
        } while (valor < 0);

        Coletivo empresa = (Coletivo) this.utilizador;
        String atividade = empresa.getAtividadeSeUnica();

        int escolha;
        do{
            escolha = lerInt("Está a 1 --> registar uma fatura já emitida; ou a 2 --> emitir uma nova?");
        }while(escolha != 1 && escolha != 2);

        LocalDateTime data = LocalDateTime.now();
        if(escolha == 1){
            int ano;
            do{
                ano = lerInt("Indique o ano");
            }while(ano < 2000 || ano > 2018);

            int mes;
            do{
                mes = lerInt("Indique o número do mês");
            }while(mes < 1 || mes > 12);

            YearMonth x = YearMonth.of(ano, mes);

            int dia;
            do{
                dia = lerInt("Indique o dia");
            }while(!x.isValidDay(dia));

            int hora;
            do{
                hora = lerInt("Indique a hora");
            }while(hora < 0 || hora > 23);

            int minutos;
            do{
                minutos = lerInt("Indique os minutos");
            }while(minutos < 1 || minutos > 59);

            data = LocalDateTime.of(ano, mes, dia, hora, minutos);
        }

        Fatura f = new Fatura(empresa.getNIF(), data, nifCliente, descricao, atividade, new ArrayList(), valor);
        this.totalFaturas.add(f.clone());
        int indiceFatura = this.totalFaturas.indexOf(f);

        empresa.adicionarFatura(indiceFatura);
        if (!this.totalEntidades.containsKey(nifCliente)) {
            Individual i = new Individual();
            i.setNIF(nifCliente);
            this.totalEntidades.put(nifCliente, i.clone());
        }
        this.totalEntidades.get(nifCliente).adicionarFatura(indiceFatura);
        
        if(!atividade.equals("")){
            HashMap<String,Double> codigos = ((Individual) this.totalEntidades.get(nifCliente)).getCodigosAtividades();
            valor += codigos.getOrDefault(atividade, 0.0);
            ((Individual) this.totalEntidades.get(nifCliente)).atualizaCodigosAtividades(atividade, valor);
        }
    }

    /**
     * Ver o valor faturado pelo utilizador (coletivo)
     */
    public void verValorFaturado() {
        TreeSet<YearMonth> mesAno = new TreeSet<YearMonth>();
        for(int i: this.utilizador.getListaFaturas()){
            Month m = this.totalFaturas.get(i).getData().getMonth();
            int y = this.totalFaturas.get(i).getData().getYear();
            YearMonth my = YearMonth.of(y,m);
            mesAno.add(my);
        }

        if (mesAno.size() == 0) {
            System.out.println("Não exitem faturas emitidas por si. Por favor, volte mais tarde");
            pausaParaLer();
            return;
        }

        int j = 1;
        System.out.println("Possui faturas emitidas nos seguintes meses:");
        ArrayList<YearMonth> res = new ArrayList<YearMonth>();
        for(YearMonth x: mesAno){
            System.out.println(j + " --> " + x.getMonth() + "/" + x.getYear());
            res.add(x);
            j++;
        }

        int escolha;
        do{
            escolha = lerInt("Escreva o mês pretendido");
        }while(escolha < 1 || escolha > j);
        YearMonth mes = res.get(escolha - 1);
        double valor = valorFaturado(mes);
        System.out.println("No mês selecionado faturou " + valor + "€");
        pausaParaLer();
    }

    /**
     * Devolve o valor faturado num determinado período (mês)
     * @param month
     * @return valor faturado
     */
    public double valorFaturado(YearMonth month){
        double valor = 0;
        for(int i: this.utilizador.getListaFaturas()){
            Fatura f = this.totalFaturas.get(i);
            if(f.getData().getMonthValue() == month.getMonthValue() && f.getData().getYear() == month.getYear())
                valor += f.getValor();
        }
        return valor;
    }

    /**
     * Ver ou mudar as definições de conta
     * @return true se o utilizador quiser voltar ao menu, false caso contrário
     */
    public boolean definicoesDaConta() {
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #            Definições da Conta             #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #            1 --> Nome                      #              \n");
        menu.append("               #            2 --> Email                     #              \n");
        menu.append("               #            3 --> Morada                    #              \n");
        menu.append("               #            4 --> Password                  #              \n");
        menu.append("               #            5 --> Voltar ao menu            #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        int escolha;
        do {
            escolha = lerInt(null);
        } while (escolha != 1 && escolha != 2 && escolha != 3 && escolha != 4 && escolha != 5);

        if (escolha == 1) {
            this.utilizador.setNome(ler("Escreva o novo nome"));
        } else if (escolha == 2) {
            this.utilizador.setEmail(ler("Escreva o novo email"));
        } else if (escolha == 3) {
            this.utilizador.setMorada(ler("Escreva a nova morada"));
        } else if (escolha == 4) {
            String tentativa;
            do {
                tentativa = ler("Escreva a password atual");
            } while (!this.utilizador.getPassword().equals(tentativa));

            this.utilizador.setPassword(ler("Escreva a nova password"));
        } else if (escolha == 5)
            return true;

        return false;
    }

    /**
     * Devolve o imposto a pagar pela Empresa
     * @return imposto
     */
    public double getImpostoColetivo(){
        Coletivo empresa = (Coletivo) this.utilizador;
        double imposto = 0.0;
        for(int i: empresa.getListaFaturas())
            imposto += this.totalFaturas.get(i).getValor() * 0.23;
        return imposto;
    }
    /**
     * Imprimir o menu do administrador
     */
    public void printMenuAdmin(){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #                  Admin                     #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #    1 --> Contribuintes mais gastadores     #              \n");
        menu.append("               #    2 --> Contribuintes mais faturadores    #              \n");
        menu.append("               #    3 --> Alterar limite família numerosa   #              \n");
        menu.append("               #    4 --> Ver utilizadores registados       #              \n");
        menu.append("               #    5 --> Ver agregados familiares          #              \n");
        menu.append("               #    6 --> Ver faturas submetidas            #              \n");
        menu.append("               #    7 --> Logout                            #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        int escolha;
        do {
            escolha = lerInt(null);
        } while (escolha != 1 && escolha != 2 && escolha != 3 && escolha != 4 && escolha != 5 && escolha != 6 && escolha != 7);

        if (escolha == 1)
            verContribuintesMaisGastadores();
        else if(escolha == 2)
            verContribuintesMaisFaturadores();
        else if(escolha == 3)
            alterarLimiteFamiliaNumerosa();
        else if(escolha == 4)
            printUtilizadoresRegistados();
        else if(escolha == 5)
            printAgregadosFamiliares();
        else if(escolha == 6)
            printFaturasSubmetidas();
        else if(escolha == 7)
            logout();
    }

    /**
     * Ver todas as faturas submetidas
     */
    public void printFaturasSubmetidas() {
        for (Fatura f: this.totalFaturas)
            System.out.println(f);

        pausaParaLer();
    }

    /**
     * Ver todos os agregados familiares
     */
    public void printAgregadosFamiliares() {
        for (AgregadoFamiliar af: this.agregados)
            System.out.println(af);

        pausaParaLer();
    }

    /**
     * Ver os utilizadores registados no sistema
     */
    public void printUtilizadoresRegistados(){
        for(Entidade e: this.totalEntidades.values()){
            if(!e.getPassword().equals(""))
                System.out.println(e.getNIF() + " --> " + e.getNome());
        }

        pausaParaLer();
    }

    /**
     * Ver os 10 Contribuintes Individuais mais gastadores
     */
    public void verContribuintesMaisGastadores() {
        TreeSet<String> contribuintes = new TreeSet<String>((nif1,nif2) -> (int) (getValorTotal(nif2) - getValorTotal(nif1)));
        for (String s: this.totalEntidades.keySet()) {
            if (s.charAt(0) == '1' || s.charAt(0) == '2')
                contribuintes.add(s);
        }

        if (contribuintes.size() == 0) {
            System.out.println("Não existem contribuintes registados. Por favor, volte mais tarde");
            pausaParaLer();
            return;
        }
        
        int num = 10;
        if (contribuintes.size() < 10)
            num = contribuintes.size();

        System.out.println("\nTop " + num + " dos contribuintes mais gastadores");
        for (String c: contribuintes) {
            System.out.println(c + " --> " + getValorTotal(c) + "€");
            num--;
            if (num == 0)
                break;
        }

        pausaParaLer();
    }

    /**
     * Ver os X Contribuintes Coletivos mais faturadores
     */
    public void verContribuintesMaisFaturadores() {
        TreeSet<String> contribuintes = new TreeSet<String>((nif1,nif2) -> (int) (getValorTotal(nif2) - getValorTotal(nif1)));
        for (String s: this.totalEntidades.keySet()) {
            if (s.charAt(0) == '5')
                contribuintes.add(s);
        }

        if (contribuintes.size() == 0) {
            System.out.println("Não existem contribuintes registados. Por favor, volte mais tarde");
            pausaParaLer();
            return;
        }

        int num;
        do {
            num = lerInt("Existem " + contribuintes.size() + " contribuintes registados. Quantos quer ver?");
        } while (num < 1 || num > contribuintes.size());

        System.out.println("\nTop " + num + " dos contribuintes mais faturadores");
        for (String c: contribuintes) {
            System.out.println(c + " --> " + getValorTotal(c) + "€");
            num--;
            if (num == 0)
                break;
        }

        pausaParaLer();
    }

    /**
     * Devolve o valor total de todas as faturas de um contribuinte
     * @param nif NIF do contribuinte
     * @return valor total
     */
    public double getValorTotal(String nif) {
        double res = 0;
        for (Integer i: this.totalEntidades.get(nif).getListaFaturas())
            res += this.totalFaturas.get(i).getValor();

        return res;
    }

    /**
     * Altera a definição de famílias numerosas
     */
    public void alterarLimiteFamiliaNumerosa() {
        this.limiteFamiliaNumerosa = lerInt("O valor atual é " + this.limiteFamiliaNumerosa + ". Introduza o novo limite para as famílias numerosas:");
    }

    /**
     * Ver as faturas por parte dos Contribuintes Individuais
     */
    public void verFaturasIndividual() {
        if (this.utilizador.getListaFaturas().size() == 0){
            System.out.println("Não tem faturas emitidas em seu nome. Por favor, volte mais tarde");
            pausaParaLer();
            return;
        }

        int escolha;
        do {
            escolha = lerInt("Deseja imprimir 1 --> todas as faturas ou 2 --> as de uma determinada empresa?");
        } while (escolha != 1 && escolha != 2);

        String nifE = "000000000";
        if (escolha == 2) {
            HashMap<String,String> empresas = new HashMap<String,String>();
            for (Integer i: this.utilizador.getListaFaturas()) {
                String nifEmpresa = this.totalFaturas.get(i).getNIFEmitente();
                String nomeEmpresa = this.totalEntidades.get(nifEmpresa).getNome();
                empresas.put(nifEmpresa, nomeEmpresa);
            }

            System.out.println("Tem faturas emitidas pelas seguintes empresas:");
            for(String n: empresas.keySet())
                System.out.println(n + " --> " + empresas.get(n));

            do{
                nifE = lerNIFColetivo("\nEscreva o nif da empresa que deseja ver");
            }while(!empresas.containsKey(nifE));
        }

        do{
            escolha = lerInt("Deseja imprimir por 1 --> valor ou 2 --> data?");
        }while(escolha != 1 && escolha != 2);
        System.out.println();

        TreeMap<Integer,Fatura> res = null;
        if(escolha == 1){
            res = sortValor(nifE);
        }
        
        else if(escolha == 2){
            do{
                escolha = lerInt("Deseja imprimir por data 1 --> ascendente ou 2 --> descendente");
            }while(escolha != 1 && escolha != 2);
            System.out.println();

            if(escolha == 1)
                res = sortData(nifE, false);
            
            else if(escolha == 2)
                res = sortData(nifE, true);
        }
        
        ///System.out.print('\u000C');
        for(Fatura f: res.values())
            System.out.println(f);

        pausaParaLer();
    }

    /**
     * Ver as faturas por parte das Empresas
     */
    public void verFaturasColetivo() {
        if (this.utilizador.getListaFaturas().size() == 0){
            System.out.println("Não exitem faturas emitidas por si. Por favor, volte mais tarde");
            pausaParaLer();
            return;
        }

        int escolha;
        do {
            escolha = lerInt("Deseja imprimir 1 --> todas as faturas ou 2 --> as de um determinado contribuinte?");
        } while (escolha != 1 && escolha != 2);

        String nifC = "000000000";
        if (escolha == 2) {
            HashMap<String,String> contribuintes = new HashMap<String,String>();
            for (Integer i: this.utilizador.getListaFaturas()) {
                String nifContribuinte = this.totalFaturas.get(i).getNIFCliente();
                String nomeContribuinte = this.totalEntidades.get(nifContribuinte).getNome();
                if(nomeContribuinte.equals(""))
                    nomeContribuinte = "(Contribuinte não registado)";
                contribuintes.put(nifContribuinte, nomeContribuinte);
            }
            System.out.println("Tem faturas emitidas para os seguintes contribuintes:");
            for(String n: contribuintes.keySet())
                System.out.println(n + " --> " + contribuintes.get(n));

            do{
                nifC = lerNIFIndividual("\nEscreva o nif do contribuinte que deseja ver");
            }while(!contribuintes.containsKey(nifC));
        }

        do{
            escolha = lerInt("Deseja imprimir por 1 --> valor ou 2 --> data?");
        }while(escolha != 1 && escolha != 2);
        System.out.println();

        TreeMap<Integer,Fatura> res = null;
        if(escolha == 1)
            res = sortValor(nifC);
        
        else if(escolha == 2){
            do{
                escolha = lerInt("Deseja imprimir por data 1 --> ascendente ou 2 --> descendente");
            }while(escolha != 1 && escolha != 2);
            System.out.println();

            if(escolha == 1)
                res = sortData(nifC, false);
            
            else if(escolha == 2)
                res = sortData(nifC, true);
        }

        System.out.print('\u000C');
        for(Fatura f: res.values())
            System.out.println(f);

        pausaParaLer();
    }
//.ololo
    /**
     * Método que ordena as faturas de uma entidade por valor (decrescente)
     * @param nif do cliente ou emissor
     * @return res TreeMap de faturas ordenado decrescentemente
     */
    public TreeMap<Integer,Fatura> sortValor(String nif){
            TreeMap<String, Double> myMap = new TreeMap<String, Double>(new Comparator<String>()
            {
                public int compare(String o1, String o2)
                {
                    return o1.compareTo(o2);
                } s
            });

        TreeMap<Integer,Fatura> res = new TreeMap<Integer,Fatura>(Map.Entry<Integer,Fatura>o1,Map.Entry<Integer,Fatura>o2)) -> (int) (o2.getValue().getValor() - o1.getValue().getValor()));
        if(this.utilizador instanceof Individual){
            for(int i: this.utilizador.getListaFaturas()){
                if(this.totalFaturas.get(i).getNIFEmitente().equals(nif) || nif.equals("000000000"))
                    res.put(i,this.totalFaturas.get(i));
            }
        }
        else if(this.utilizador instanceof Coletivo){
            for(int i: this.utilizador.getListaFaturas()){
                if(this.totalFaturas.get(i).getNIFCliente().equals(nif) || nif.equals("000000000"))
                    res.put(i,this.totalFaturas.get(i));
            }
        }

        return res;
    }

    /**
     * Método que ordena as faturas de uma entidade por data (crescente se bool decrescente == false)
     * @param nif do cliente ou emissor
     * @return res TreeMap de faturas ordenado decrescentemente
     */
    public TreeMap<Integer,Fatura> sortData(String nif, boolean decrescente){
        TreeMap<Integer,Fatura> res = new TreeMap<Integer,Fatura>((f1,f2) -> f1.getData().compareTo(f2.getData()));
        if(this.utilizador instanceof Individual){
            for(int i: this.utilizador.getListaFaturas()){
                if(this.totalFaturas.get(i).getNIFEmitente().equals(nif) || nif.equals("000000000"))
                    res.put(i,this.totalFaturas.get(i));
            }
        }
        else if(this.utilizador instanceof Coletivo){
            for(int i: this.utilizador.getListaFaturas()){
                if(this.totalFaturas.get(i).getNIFCliente().equals(nif) || nif.equals("000000000"))
                    res.put(i,this.totalFaturas.get(i));
            }
        }
        
        if(decrescente)
            res = (TreeMap<Integer,Fatura>) res.descendingMap();
        
        return res;
    }

    /**
     * Arredonda o rendimento para aceder ao Map irs
     * @param rendimento
     * @return rendimento arredondado
     */
    public int round(double rendimento){
        int res = 0;
        double minimo = rendimento;
        for(int v: irs.keySet()){
            double x = v - rendimento;
            if(x >= 0 && x < minimo){
                    res = v;
                    break;
            }
        }
        if(res == 0)
            res = 300001;
        
        return res;
    }

    /**
     * Devolve o valor de irs associado ao agregado familiar do utilizador atual
     * @return irs
     */
    public double getIRS(){
        Individual utilizador = ((Individual) this.utilizador);
        AgregadoFamiliar agregado = this.agregados.get(utilizador.getIndice());
        int numeroAgregado = agregado.getAgregado().size();
        if(numeroAgregado > 5) numeroAgregado = 5;
        double rendimento = agregado.getRendimento();
        double valorPagoIRS = (irs.get(round(rendimento))[numeroAgregado] / 100) * rendimento;
        return valorPagoIRS;
    }

    /**
     * Devolve o valor de dedução fiscal associado ao agregado do utilizador atual
     * @return deducao
     */
    public double getDeducaoFiscalAgregado(){
        //Nota: Temos de verificar se o valor acumulado das faturas não ultrapassa o valor do rendimento anual - valor pago de IRS
        //      Os descontos devem incidir apenas sobre o valor(rendimento - valorPagoIRS), como verificar esta situação?
        Individual utilizador = ((Individual) this.utilizador);
        AgregadoFamiliar agregado = this.agregados.get(utilizador.getIndice());
        double coeficiente = utilizador.getCoeficienteFiscal();
        int numeroAgregado = agregado.getAgregado().size();
        if(numeroAgregado > 5) numeroAgregado = 5;

        HashMap<String,Double> atividadesAgregado = new HashMap<String,Double>();
        for(String nif: agregado.getAgregado().keySet()){
            if (this.totalEntidades.containsKey(nif)) {
                Individual i = (Individual) this.totalEntidades.get(nif);
                HashMap<String,Double> atividades = i.getCodigosAtividades();
                for(String s: atividades.keySet()){
                    if(atividadesAgregado.containsKey(s))
                        atividadesAgregado.put(s, atividades.get(s) + atividadesAgregado.get(s));
                    else
                        atividadesAgregado.put(s, atividades.get(s));
                }
            }
        }

        double valorADeduzir = 0.0;
        for(String a: atividadesAgregado.keySet()){
            double desconto = atividadesAgregado.get(a) * (descontos.get(a).getKey() / 100.0);
            desconto = Math.min(desconto, descontos.get(a).getValue());
            valorADeduzir += desconto;
        }
        
        valorADeduzir /= coeficiente;

        return valorADeduzir;
    }

    /**
     * Devolve o valor de dedução fiscal associado utilizador atual
     * @return deducao
     */
    public double getDeducaoFiscal(){
        Individual utilizador = ((Individual) this.utilizador);
        double coeficiente = utilizador.getCoeficienteFiscal();

        double valorADeduzir = 0.0;
        HashMap<String,Double> atividades = utilizador.getCodigosAtividades();
        for(String a: atividades.keySet()){
            double desconto = atividades.get(a) * (descontos.get(a).getKey() / 100.0);
            desconto = Math.min(desconto, descontos.get(a).getValue() / 2);
            valorADeduzir += desconto;
        }
        
        valorADeduzir /= coeficiente;

        return valorADeduzir;
    }

    /**
     * Devolve a redução de imposto associada ao utilizador atual
     * @return deducao
     */
    public double reducaoImposto() {
        double reducao = 0.0;
        if (this.utilizador instanceof Individual && isFamiliaNumerosa(this.utilizador.getNIF())) {
            reducao = this.agregados.get(((Individual) this.utilizador).getIndice()).getNumeroFilhos() * 0.05;
        } else if (this.utilizador instanceof Coletivo && ((Coletivo) this.utilizador).isInterior()) {
            reducao = 0.10;
        }

        return reducao;
    }

    /**
     * Função que diz se a família de um dado Individual é numerosa
     * @param nif NIF do Individual
     * @return true se a família for numerosa, false caso contrário
     */
    public boolean isFamiliaNumerosa(String nif){
        Individual i = (Individual) this.totalEntidades.get(nif);
        return this.agregados.get(i.getIndice()).getNumeroFilhos() >= limiteFamiliaNumerosa;
    }
}
