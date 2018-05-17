
/**
 * Classe que representa a interface
 *
 * @author GC-JRI-RV
 * @version 16/04/2018
 */

import java.util.ArrayList;
import java.util.Scanner;
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
     * Ler o NIF para registar
     * @param pedido
     * @return nif
     */
    public String lerNIFRegisto(String pedido){
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
                this.totalFaturas = (ArrayList<Fatura>) restore.readObject();
                this.totalEntidades = (HashMap<String,Entidade>) restore.readObject();
                this.agregados = (ArrayList<AgregadoFamiliar>) restore.readObject();
                this.limiteFamiliaNumerosa = (int) restore.readObject();
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
     * Ler pedido
     * @param pedido
     * @return leitura do input
     */
    public String ler(String pedido){
        System.out.println("Escreva " + pedido);
        Scanner ler = new Scanner(System.in);
        String res = ler.nextLine();
        ler.close();
        return res;
    }
    /**
     * Função para dar tempo para ler o texto impresso, que espera que o utilizador pressione Enter para continuar
     */
    public void pausaParaLer(){
        System.out.println();
        System.out.println("Pressione Enter para continuar...");
        Scanner s = new Scanner(System.in);
        s.nextLine();
        s.close();
    }
    /**
     * Ler atividades económicas do coletivo
     * @return lista com as atividades do coletivo
     */
    public ArrayList<String> lerAtividadesColetivo() {
        ArrayList<String> codigosAtividades = new ArrayList<String>();
        Scanner ler = new Scanner(System.in);
        for (String atividade: this.atividades) {
            System.out.println("Atua na área de " + atividade + "? (s/n)");
            String res = ler.nextLine();
            if (res.equals("s"))
                codigosAtividades.add(atividade);
        }
        ler.close();

        return codigosAtividades;
    }
    /**
     * Imprime o menu inicial
     * @return true se o utilizador quiser sair da plataforma, false caso queira continuar a utilizá-la
     */
    public boolean printMenu(){
        StringBuilder menu = new StringBuilder();
        int escolha;

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

        Scanner s = new Scanner(System.in);
        do{
            escolha = s.nextInt();
        }while(escolha != 1 && escolha != 2 && escolha != 3);
        s.close();

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
        String nif;
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

        nif = lerNIFRegisto("NIF");

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
        
        String nome = ler("nome");
        e.setNome(nome);
        String email = ler("email");
        e.setEmail(email);
        String morada = ler("morada");
        e.setMorada(morada);
        String password = ler("password");
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
            numeroAgregado = Integer.parseInt(ler("número de elementos do agregado familiar (incluindo você)"));
        } while (numeroAgregado <= 0);
        HashMap<String,Boolean> nifAgregado = new HashMap<String,Boolean>();
        nifAgregado.put(e.getNIF(), false);
        if (numeroAgregado > 1) {
            int numeroFilhos;
            do{
                numeroFilhos = Integer.parseInt(ler("número de filhos"));
            }while(numeroFilhos >= numeroAgregado);

        
            if (numeroFilhos > 0){
                System.out.println("Escreva o NIF dos filhos");
                for(int i=1; i<=numeroFilhos; i++){
                    String nifFilho = lerNIFIndividual("NIF filho " + i);
                    nifAgregado.put(nifFilho, true);
                }
            }

            if(numeroAgregado - numeroFilhos > 1){
                System.out.println("Escreva o NIF dos restantes elementos do agregado familiar (excluindo você)");
                for(int i=1; i<numeroAgregado-numeroFilhos; i++){
                    String nifFamiliar = lerNIFIndividual("NIF " + i);
                    nifAgregado.put(nifFamiliar, false);
                }
            }
        }
    
        double coeficiente = Double.parseDouble(ler("coeficiente Fiscal"));
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
            Scanner s = new Scanner(System.in);
            String resposta;
            do{
                System.out.println("Confirma que o rendimento anual do seu agregado é " + rendimentoAtual + "? (s/n)");
                resposta = s.nextLine();
            }while(!resposta.equals("s") && !resposta.equals("n"));
            s.close();

            if(resposta.equals("n")){
                rendimentoAgregado = Double.parseDouble(ler("rendimento anual do agregado familiar"));
                this.agregados.get(indice).setRendimento(rendimentoAgregado);
            }
        }
        else {
            rendimentoAgregado = Double.parseDouble(ler("rendimento anual do agregado familiar"));
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
        menu.append("               #         Interior do país:                  #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);
        
        String designacao = ler("designacao");
        double coeficiente = Double.parseDouble(ler("coeficiente Fiscal"));
        ArrayList<String> informacaoAtividades = lerAtividadesColetivo();
        
        Scanner ler = new Scanner(System.in);
        System.out.println("É uma empresa do interior? (s/n)");
        String res = ler.nextLine();
        boolean interior = false;
        if (res.equals("s"))
            interior = true;
        ler.close();

        e.setDesignacao(designacao);
        e.setInformacaoAtividades(informacaoAtividades);
        e.setCoeficienteFiscal(coeficiente);
        e.setInterior(interior);
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

        String nif;
        do{
            nif = ler("NIF");
        }while(nif.length() != 9 || (nif.charAt(0) != '1' && nif.charAt(0) != '2' && nif.charAt(0) != '5' && !nif.equals("000000000")));

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
                tentativa = ler("Password");
                numTentativas--;
            }while(! password.equals(tentativa));

            this.utilizador = e;
        } else {
            System.out.println("NIF não existente!");
            System.out.println("Confirme se o NIF que introduziu está correto e se estiver registe-se");
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
        int escolha;
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

        Scanner s = new Scanner(System.in);
        do{
            escolha = s.nextInt();
        }while(escolha != 1 && escolha != 2 && escolha != 3 && escolha != 4 && escolha != 5 && escolha != 6);
        s.close();

        if(escolha == 1)
            verFaturasIndividual();
        else if (escolha == 2)
            alterarAtividadeFaturas();
        else if (escolha == 3)
            validarFaturas();
        else if (escolha == 4){
            double deducaoAgregado = this.getDeducaoFiscalAgregado();
            double deducaoIndividual = this.getDeducaoFiscal();
            double irs = this.getIRS();
            double valorFinal = Math.min(deducaoAgregado, irs);
            System.out.println("O valor pago de IRS foi " + irs);
            System.out.println("O valor de dedução acumulado por si é " + deducaoIndividual);
            System.out.println("O valor de dedução acumulado pelo seu agregado é " + deducaoAgregado);
            System.out.println("Isto resulta num valor de dedução de " + valorFinal);
            pausaParaLer();
        }
        else if (escolha == 5) {
            boolean exit = false;
            while (!exit)
                exit = definicoesDaConta();
        } else if (escolha == 6)
            logout();
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

        System.out.println("Qual quer alterar?");
        int escolha;
        Scanner s = new Scanner(System.in);
        do {
            escolha = s.nextInt();
        } while (escolha < 0 || escolha >= faturasAlteraveis.size());
        s.close();

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

        s = new Scanner(System.in);
        do {
            escolha = s.nextInt();
        } while (escolha >= atividades.size());
        s.close();

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
            if(f.estaPendente() && f.getNIFCliente().equals(this.utilizador.getNIF()))
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
        menu.append("               #           Deseja validá-las?               #              \n");
        menu.append("               #           1 --> Sim                        #              \n");
        menu.append("               #           2 --> Não, regressar ao menu     #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        int escolha;
        Scanner s = new Scanner(System.in);
        do{
            escolha = s.nextInt();
        }while(escolha != 1 && escolha != 2);
        s.close();

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

                s = new Scanner(System.in);
                do{
                    escolha = s.nextInt();
                }while(escolha >= atividades.size());
                s.close();

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
        menu.append("               #           5 --> Logout                     #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        Scanner s = new Scanner(System.in);
        int escolha;
        do{
            escolha = s.nextInt();
        }while(escolha != 1 && escolha != 2 && escolha != 3 && escolha != 4 && escolha != 5);
        s.close();
        

        if(escolha == 1){
            String nifCliente = lerNIFIndividual("NIF do cliente");
            String descricao = ler("descricao da fatura");
            double valor;
            s = new Scanner(System.in);
            do{
                System.out.println("Escreva valor da Fatura");
                valor = s.nextDouble();
            }while(valor < 0);
            s.close();
            emitirFatura(nifCliente, descricao, valor);
        }else if(escolha == 2)
            verFaturasColetivo();
        else if (escolha == 3) {
            boolean exit = false;
            while (!exit)
                exit = definicoesDaConta();
        }
        else if(escolha == 4){ 
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
            s = new Scanner(System.in);
            do{
                System.out.println("Escolha o mês pretendido:");
                escolha = s.nextInt();
            }while(escolha < 1 || escolha > j);
            YearMonth mes = res.get(escolha - 1);
            double valor = valorFaturado(mes);
            System.out.println("No mês selecionado faturou " + valor + "€");
            pausaParaLer();
        }
        else if (escolha == 5)
            logout();
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

        Scanner s = new Scanner(System.in);
        int escolha;
        do {
            escolha = s.nextInt();
        } while (escolha != 1 && escolha != 2 && escolha != 3 && escolha != 4 && escolha != 5);
        s.close();

        Scanner novasDefinicoes = new Scanner(System.in);
        if (escolha == 1) {
            System.out.println("Introduza o novo nome");
            this.utilizador.setNome(novasDefinicoes.nextLine());
        } else if (escolha == 2) {
            System.out.println("Introduza o novo email");
            this.utilizador.setEmail(novasDefinicoes.nextLine());
        } else if (escolha == 3) {
            System.out.println("Introduza a nova morada");
            this.utilizador.setMorada(novasDefinicoes.nextLine());
        } else if (escolha == 4) {
            String tentativa;
            do {
                System.out.println("Introduza a password atual");
                tentativa = novasDefinicoes.nextLine();
            } while (!this.utilizador.getPassword().equals(tentativa));
            System.out.println("Introduza a nova password");
            this.utilizador.setPassword(novasDefinicoes.nextLine());
        } else if (escolha == 5) {
            novasDefinicoes.close();
            return true;
        }

        novasDefinicoes.close();
        return false;
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

        Scanner s = new Scanner(System.in);
        int escolha;
        do {
            escolha = s.nextInt();
        } while (escolha != 1 && escolha != 2 && escolha != 3 && escolha != 4 && escolha != 5 && escolha != 6 && escolha != 7);
        s.close();

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

        Scanner s = new Scanner(System.in);
        int num;
        do {
            System.out.println("Existem " + contribuintes.size() + " contribuintes registados. Quantos quer ver?");
            num = s.nextInt(); 
        } while (num < 1 || num > contribuintes.size());
        s.close();

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
        System.out.println("O valor atual é " + this.limiteFamiliaNumerosa + ". Introduza o novo limite para as famílias numerosas:");
        Scanner s = new Scanner(System.in);
        this.limiteFamiliaNumerosa = s.nextInt();
        s.close();
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

        Scanner s = new Scanner(System.in);
        int escolha;
        do {
            System.out.println("Deseja imprimir 1 --> todas as faturas ou 2 --> as de uma determinada empresa?");
            escolha = s.nextInt();
        } while (escolha != 1 && escolha != 2);
        s.close();

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

            s = new Scanner(System.in);
            do{
                System.out.println("\nEscreva o nif da empresa que deseja ver");
                nifE = s.nextLine();
            }while(!empresas.containsKey(nifE));
            s.close();
        }

        s = new Scanner(System.in);
        do{
            System.out.println("Deseja imprimir por 1 --> valor ou 2 --> data?");
            escolha = s.nextInt();
        }while(escolha != 1 && escolha != 2);
        s.close();
        System.out.println();

        TreeSet<Fatura> res = null;
        if(escolha == 1)
            res = sortValor(nifE);
        
        else if(escolha == 2){
            s = new Scanner(System.in);
            do{
                System.out.println("Deseja imprimir por data 1 --> ascendente ou 2 --> descendente");
                escolha = s.nextInt();
            }while(escolha != 1 && escolha != 2);
            System.out.println();
            s.close();

            if(escolha == 1)
                res = sortData(nifE, false);
            
            else if(escolha == 2)
                res = sortData(nifE, true);
        }

        System.out.print('\u000C');
        for(Fatura f: res)
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

        Scanner s = new Scanner(System.in);
        int escolha;
        do {
            System.out.println("Deseja imprimir 1 --> todas as faturas ou 2 --> as de um determinado contribuinte?");
            escolha = s.nextInt();
        } while (escolha != 1 && escolha != 2);
        s.close();

        String nifC = "000000000";
        if (escolha == 2) {
            HashMap<String,String> contribuintes = new HashMap<String,String>();
            for (Integer i: this.utilizador.getListaFaturas()) {
                String nifContribuinte = this.totalFaturas.get(i).getNIFCliente();
                String nomeContribuinte = this.totalEntidades.get(nifContribuinte).getNome();
                contribuintes.put(nifContribuinte, nomeContribuinte);
            }
            System.out.println("Tem faturas emitidas para os seguintes contribuintes:");
            for(String n: contribuintes.keySet())
                System.out.println(n + " --> " + contribuintes.get(n));

            s = new Scanner(System.in);
            do{
                System.out.println("\nEscreva o nif do contribuinte que deseja ver");
                nifC = s.nextLine();
            }while(!contribuintes.containsKey(nifC));
            s.close();
        }

        s = new Scanner(System.in);
        do{
            System.out.println("Deseja imprimir por 1 --> valor ou 2 --> data?");
            escolha = s.nextInt();
        }while(escolha != 1 && escolha != 2);
        s.close();
        System.out.println();

        TreeSet<Fatura> res = null;
        if(escolha == 1)
            res = sortValor(nifC);
        
        else if(escolha == 2){
            s = new Scanner(System.in);
            do{
                System.out.println("Deseja imprimir por data 1 --> ascendente ou 2 --> descendente");
                escolha = s.nextInt();
            }while(escolha != 1 && escolha != 2);
            System.out.println();
            s.close();

            if(escolha == 1)
                res = sortData(nifC, false);
            
            else if(escolha == 2)
                res = sortData(nifC, true);
        }

        System.out.print('\u000C');
        for(Fatura f: res)
            System.out.println(f);

        pausaParaLer();
    }
    /**
     * Emitir as faturas
     * @param nifCliente
     * @param descricao
     * @param valor
     */
    public void emitirFatura(String nifCliente, String descricao, double valor) {
        Coletivo empresa = (Coletivo) this.utilizador;
        String atividade = empresa.getAtividadeSeUnica();

        System.out.println("Está a 1 --> registar uma fatura já emitida; ou a 2 --> emitir uma nova?");
        Scanner s = new Scanner(System.in);
        int escolha;
        do{
            escolha = s.nextInt();
        }while(escolha != 1 && escolha != 2);
        s.close();

        LocalDateTime data = LocalDateTime.now();
        if(escolha == 1){
            s = new Scanner(System.in);
            System.out.println("Indique o ano");
            int ano;
            do{
                ano = s.nextInt();
            }while(ano < 2000 || ano > 2018);

            System.out.println("Indique o número do mês");
            int mes;
            do{
                mes = s.nextInt();
            }while(mes < 1 || mes > 12);

            YearMonth x = YearMonth.of(ano, mes);

            System.out.println("Indique o dia");
            int dia;
            do{
                dia = s.nextInt();
            }while(!x.isValidDay(dia));

            System.out.println("Indique a hora");
            int hora;
            do{
                hora = s.nextInt();
            }while(hora < 0 || hora > 23);

            System.out.println("Indique os minutos");
            int minutos;
            do{
                minutos = s.nextInt();
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
     * Método que ordena as faturas de uma entidade por valor (decrescente)
     * @param nif do cliente ou emissor
     * @return res TreeSet de faturas ordenado decrescentemente
     */
    public TreeSet<Fatura> sortValor(String nif){
        TreeSet<Fatura> res = new TreeSet<Fatura>((f1,f2) -> (int) (f2.getValor() - f1.getValor()));
        if(this.utilizador instanceof Individual){
            for(int i: this.utilizador.getListaFaturas()){
                if(this.totalFaturas.get(i).getNIFEmitente().equals(nif) || nif.equals("000000000"))
                    res.add(this.totalFaturas.get(i));
            }
        }
        else if(this.utilizador instanceof Coletivo){
            for(int i: this.utilizador.getListaFaturas()){
                if(this.totalFaturas.get(i).getNIFCliente().equals(nif) || nif.equals("000000000"))
                    res.add(this.totalFaturas.get(i));
            }
        }

        return res;
    }

    /**
     * Método que ordena as faturas de uma entidade por data (crescente se bool decrescente == false)
     * @param nif do cliente ou emissor
     * @return res TreeSet de faturas ordenado decrescentemente
     */
    public TreeSet<Fatura> sortData(String nif, boolean decrescente){
        TreeSet<Fatura> res = new TreeSet<Fatura>((f1,f2) -> f1.getData().compareTo(f2.getData()));
        if(this.utilizador instanceof Individual){
            for(int i: this.utilizador.getListaFaturas()){
                if(this.totalFaturas.get(i).getNIFEmitente().equals(nif) || nif.equals("000000000"))
                    res.add(this.totalFaturas.get(i));
            }
        }
        else if(this.utilizador instanceof Coletivo){
            for(int i: this.utilizador.getListaFaturas()){
                if(this.totalFaturas.get(i).getNIFCliente().equals(nif) || nif.equals("000000000"))
                    res.add(this.totalFaturas.get(i));
            }
        }
        
        if(decrescente)
            res = (TreeSet<Fatura>) res.descendingSet();
        
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
    ///////////////////////////////// Falta acrecentar os valores reais da redução de imposto!!!!!
    public double reducaoImposto() {
        if (this.utilizador instanceof Individual && isFamiliaNumerosa(this.utilizador.getNIF())) {
            ;
        } else if (this.utilizador instanceof Coletivo && ((Coletivo) this.utilizador).getInterior()) {
            ;
        }

        return 0.0;
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
