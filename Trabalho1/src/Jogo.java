
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Essa eh a classe principal da aplicacao "Counter-Strike: Lavras".
 *  Usuarios podem caminhar em um cenario. E eh tudo! Ele realmente
 *  precisa ser estendido para fazer algo interessante!
 * 
 *  Para jogar esse jogo, crie uma instancia dessa classe e chame o metodo
 *  "jogar".
 * 
 *  Essa classe principal cria e inicializa todas as outras: ela cria os
 *  ambientes, cria o analisador e comeca o jogo. Ela tambeme avalia e 
 *  executa os comandos que o analisador retorna.
 * 
 * @author  Michael Kölling and David J. Barnes (traduzido por Julio Cesar Alves)
 * @version 2011.07.31 (2017.05.16)
 */

public class Jogo  {
    private Analisador analisador;
    private Ambiente ambienteAtual;
    private Agente jogador;
    private int tempoRestante;
        
    /**
     * Cria o jogo e incializa seu mapa interno.
     * @param tempo Tempo limite que o jogador tem para desativar a bomba, em minutos
     */
    public Jogo(int tempo) {
        tempoRestante = tempo;
        criarAmbientes();
        analisador = new Analisador();
        jogador = new Agente(5,100);
    }
    
    /**
     * Cria uma bomba a partir de um arquivo texto.
     * @param arq Arquivo texto a ser lido
     * @return HashMap com os fios da bomba e seus respectivos efeitos
     */
    private HashMap<String, Integer> criarBomba(BufferedReader arq) {
        HashMap<String,Integer> fios = null;
        
        try {
            String leitura = arq.readLine();
            fios = new HashMap<>();
            while (!leitura.equals("END_DEF")) {
                
                String[] s = leitura.split(" ");
                fios.put(s[0], Integer.parseInt(s[1]));
                
                leitura = arq.readLine();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fios;
    }
    
    /**
     * Cria um novo ambiente a partir de um arquivo texto.
     * @param arq Arquivo texto a ser lido
     * @return Ambiente criado
     */
    private Ambiente criarNovoAmbiente(BufferedReader arq) {
        Ambiente a = null;
        try {
            String nome = arq.readLine();
            int tranca = Integer.parseInt(arq.readLine());
            String desc = arq.readLine();
            String descLonga = "";
            String leitura = arq.readLine();
            while (!leitura.equals("END_DEF")) {
                descLonga += leitura;
                leitura = arq.readLine();
                if (!leitura.equals("END_DEF"))
                    descLonga += "\n";
            }
            a = new Ambiente(nome,desc,descLonga,tranca);
        } catch (IOException ex) {
            Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return a;
    }
    
    /**
     * Ajusta as saídas de um ambiente a partir de um arquivo texto.
     * É preciso que todos os ambientes já tenham sido criados neste ponto.
     * @param a Ambiente que receberá as novas saídas
     * @param arq Arquivo texto a ser lido
     * @param ambientes HashMap com todos os ambientes criados
     */
    private void criarSaidas(Ambiente a,BufferedReader arq,HashMap<String,Ambiente> ambientes) {
        try {
            String leitura = arq.readLine();
            while (!leitura.equals("END_DEF")) {
                String[] s = leitura.split(" ");
                Ambiente b = ambientes.get(s[1]);
                if (b!= null)
                    a.ajustarSaidas(s[0], b);
                leitura = arq.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Cria um terrorista a partir de um arquivo texto.
     * @param arq Arquivo texto a ser lido
     * @return Terrorista criado
     */
    private Terrorista criarTerrorista(BufferedReader arq) {
        Terrorista t = null;
        
        try {
            int saude = Integer.parseInt(arq.readLine());
            t = new Terrorista(saude);
            String leitura = arq.readLine();
            while (!leitura.equals("END_DEF")) {
                if (leitura.equals("Item")) {
                    String nome = arq.readLine();
                    double peso = Double.parseDouble(arq.readLine());
                    String desc = arq.readLine();
                    t.ajustarItens(new Item(nome,peso,desc));
                } else if (leitura.equals("Arma")) {
                    String nome = arq.readLine();
                    double peso = Double.parseDouble(arq.readLine());
                    String desc = arq.readLine();
                    int municao = Integer.parseInt(arq.readLine());
                    t.ajustarItens(new Arma(nome,peso,desc,municao));
                } else if (leitura.equals("ArmaAtiva")) {
                    String nome = arq.readLine();
                    double peso = Double.parseDouble(arq.readLine());
                    String desc = arq.readLine();
                    int municao = Integer.parseInt(arq.readLine());
                    t.ajustarArma(new Arma(nome,peso,desc,municao));
                } else if (leitura.equals("Chave")) {
                    String nome = arq.readLine();
                    double peso = Double.parseDouble(arq.readLine());
                    String desc = arq.readLine();
                    int trava = Integer.parseInt(arq.readLine());
                    t.ajustarItens(new Chave(nome,peso,desc,trava));
                } else if (leitura.equals("Curativo")) {
                    String nome = arq.readLine();
                    double peso = Double.parseDouble(arq.readLine());
                    String desc = arq.readLine();
                    int poder = Integer.parseInt(arq.readLine());
                    t.ajustarItens(new Curativo(nome,peso,desc,poder));
                }
                leitura = arq.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return t;
    }
    
    /**
     * Cria os itens de um ambiente a partir de um arquivo texto.
     * A função recebe o Ambiente e já adiciona os itens automaticamente.
     * @param a Ambiente que receberá os itens criados
     * @param arq Arquivo texto a ser lido
     */
    private void criarItensAmbiente(Ambiente a, BufferedReader arq) {
        
        try {
            String leitura = arq.readLine();
            while (!leitura.equals("END_DEF")) {
                if (leitura.equals("Item")) {
                    String nome = arq.readLine();
                    double peso = Double.parseDouble(arq.readLine());
                    String desc = arq.readLine();
                    a.ajustarItens(new Item(nome,peso,desc));
                } else if (leitura.equals("Arma")) {
                    String nome = arq.readLine();
                    double peso = Double.parseDouble(arq.readLine());
                    String desc = arq.readLine();
                    int municao = Integer.parseInt(arq.readLine());
                    a.ajustarItens(new Arma(nome,peso,desc,municao));
                } else if (leitura.equals("Chave")) {
                    String nome = arq.readLine();
                    double peso = Double.parseDouble(arq.readLine());
                    String desc = arq.readLine();
                    int trava = Integer.parseInt(arq.readLine());
                    a.ajustarItens(new Chave(nome,peso,desc,trava));
                } else if (leitura.equals("Curativo")) {
                    String nome = arq.readLine();
                    double peso = Double.parseDouble(arq.readLine());
                    String desc = arq.readLine();
                    int poder = Integer.parseInt(arq.readLine());
                    a.ajustarItens(new Curativo(nome,peso,desc,poder));
                }
                leitura = arq.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Lê um arquivo texto até chegar no fim do bloco atual.
     * A função é necessária quando há algum erro na hora de ler o arquivo,
     * seja com algum ambiente que não existe, tipo de item não especificado.
     * Ela percorre o arquivo até chegar na próxima linha marcada com END_DEF
     * para poder iniciar um novo bloco.
     * @param arq Arquivo texto a ser lido
     */
    private void lerAteEndDef(BufferedReader arq) {
        try {
            String leitura = arq.readLine();
            while (!leitura.equals("END_DEF")) {
                leitura = arq.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Cria todos os ambientes e liga as saidas deles.
     * O método lê todas as informações de todos os ambientes a partir
     * de um arquivo texto.
     */
    private void criarAmbientes() {
        
        HashMap<String,Ambiente> ambientes = new HashMap<>();
        try {
            BufferedReader arqAmbientes = new BufferedReader(new FileReader("ambients/ambientes.txt"));
            String linha = arqAmbientes.readLine();
            while (linha != null) {
                if (linha.equals("DEF_AMBIENT")) {
                    Ambiente a = criarNovoAmbiente(arqAmbientes);
                    if (a!=null)
                        ambientes.put(a.getNome(), a);
                    else
                        lerAteEndDef(arqAmbientes);
                        
                } else if (linha.equals("DEF_AMBIENT_BOMB")) {
                    String nome = arqAmbientes.readLine();
                    HashMap<String,Integer> bomba = criarBomba(arqAmbientes);
                    if (bomba != null) {
                        Ambiente a = ambientes.get(nome);
                        if (a!=null)
                            a.setBomba(bomba);
                    } else
                        lerAteEndDef(arqAmbientes);
                } else if (linha.equals("DEF_AMBIENT_EXITS")) {
                    String nome = arqAmbientes.readLine();
                    Ambiente a = ambientes.get(nome);
                    if (a != null)
                        criarSaidas(a,arqAmbientes,ambientes);
                    else
                        lerAteEndDef(arqAmbientes);
                } else if (linha.equals("DEF_AMBIENT_TERROR")) {
                    String nome = arqAmbientes.readLine();
                    Ambiente a = ambientes.get(nome);
                    if (a != null) {
                        Terrorista t = criarTerrorista(arqAmbientes);
                        if (t != null)
                            a.ajustarTerroristas(t);
                        else
                            lerAteEndDef(arqAmbientes);
                    } else
                        lerAteEndDef(arqAmbientes);
                } else if (linha.equals("DEF_AMBIENT_ITENS")) {
                    String nome = arqAmbientes.readLine();
                    Ambiente a = ambientes.get(nome);
                    if (a!=null) {
                        criarItensAmbiente(a,arqAmbientes);
                    } else
                        lerAteEndDef(arqAmbientes);
                } else if (linha.equals("DEF_AMBIENTE_ATUAL")) {
                    String nome = arqAmbientes.readLine();
                    Ambiente a = ambientes.get(nome);
                    if (a!=null) {
                        ambienteAtual = a;
                    }
                }
                
                linha = arqAmbientes.readLine();
            }
            
            arqAmbientes.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Arquivo nao encontrado.");
        } catch (IOException ex) {
            Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     *  Rotina principal do jogo. Fica em loop ate terminar o jogo.
     */
    public void jogar() {            
        imprimirBoasVindas();

        // Entra no loop de comando principal. Aqui nos repetidamente lemos
        // comandos e os executamos ate o jogo terminar.
                
        boolean terminado = false;
        while (! terminado) {
            Comando comando = analisador.pegarComando();
            System.out.println();
            terminado = processarComando(comando);
        }
        System.out.println("Obrigado por jogar. Ate mais!");
    }

    /**
     * Imprime a mensagem de abertura para o jogador.
     */
    private void imprimirBoasVindas() {
        System.out.println();
        System.out.println("Bem-vindo ao Counter-Strike: Lavras!");
        System.out.println("Digite 'ajuda' se voce precisar de ajuda.");
        System.out.println();
        
        exibirAmbienteAtual();
    }

    /**
     * Dado um comando, processa-o (ou seja, executa-o)
     * @param comando O Comando a ser processado.
     * @return true se o comando finaliza o jogo.
     */
    private boolean processarComando(Comando comando) {
        boolean querSair = false;
        boolean venceu = false;

        if(comando.ehDesconhecido()) {
            System.out.println("Eu nao entendi o que voce disse...");
            return false;
        }

        String palavraDeComando = comando.getPalavraDeComando();
        if (palavraDeComando.equals("ajuda")) {
            imprimirAjuda();
        }
        else if (palavraDeComando.equals("ir")) {
            irParaAmbiente(comando);
        }
        else if (palavraDeComando.equals("sair")) {
            querSair = sair(comando);
        }
        else if (palavraDeComando.equals("observar")) {
            observar(comando);
        } else if (palavraDeComando.equals("coletar")) {
            coletar(comando);
        } else if (palavraDeComando.equals("largar")) {
            largar(comando);
        } else if (palavraDeComando.equals("empunhar")) {
            empunhar(comando);
        } else if (palavraDeComando.equals("revistar")) {
            revistar(comando);
        } else if (palavraDeComando.equals("usar")) {
            usar(comando);
        } else if (palavraDeComando.equals("cortar")) {
            venceu = cortar(comando);
        } 
        
        if (tempoRestante <= 0) {
            exibirGameOver(2);
            querSair = true;
        } else if (!jogador.saudavel()) {
            exibirGameOver(1);
            querSair = true;
        } else if (venceu) {
            exibirGameOver(3);
            querSair = true;
        }

        return querSair;
    }

    // Implementacoes dos comandos do usuario

    /**
     * Printe informacoes de ajuda.
     * Aqui nos imprimimos algo bobo e enigmatico e a lista de 
     * palavras de comando
     */
    private void imprimirAjuda() {
        System.out.println("Plantaram uma bomba no Restaurante Universitario.");
        System.out.println("Voce precisa correr contra o tempo para desarma-la.");
        System.out.println("Tempo restante: "+tempoRestante+" minutos.");
        System.out.println();
        System.out.println("Suas palavras de comando sao:");
        System.out.println("   "+analisador.pegarComandosValidos());
    }

    /** 
     * Tenta ir em uma direcao. Se existe uma saida entra no 
     * novo ambiente, caso contrario imprime mensagem de erro.
     */
    private void irParaAmbiente(Comando comando) {
        if(!comando.temSegundaPalavra()) {
            // se nao ha segunda palavra, nao sabemos pra onde ir...
            System.out.println("Ir pra onde?");
            return;
        }

        String direcao = comando.getSegundaPalavra();

        // Tenta sair do ambiente atual
        Ambiente proximoAmbiente = null;
        proximoAmbiente = ambienteAtual.getAmbiente(direcao);
        

        if (proximoAmbiente == null) {
            System.out.println("Nao ha passagem!");
        }
        else {
            boolean passar = true;
            
            if (proximoAmbiente.getTranca() != 0) {
                System.out.print("Porta esta trancada. ");
                //Tenta achar uma chave que desbloqueia
                if (jogador.desbloquearPorta(proximoAmbiente.getTranca())) {
                    System.out.println("Voce a destrancou.");
                    proximoAmbiente.destrancar();
                } else {
                    passar = false;
                    System.out.println("Voce precisa da chave para entrar.");
                }    
            }
            
            if (passar) {
                ambienteAtual = proximoAmbiente;
                tempoRestante--;

                if (tempoRestante > 0) {
                    if (ambienteAtual.temTerrorista()) {
                        Random r = new Random();
                        System.out.println(batalha());
                        tempoRestante-= 1+r.nextInt(3);
                    }

                    if (jogador.saudavel() && tempoRestante > 0) {
                        exibirAmbienteAtual();
                    }
                }
            }
        }
    }
    
    /**
     * Exibe a mensagem de GAME OVER.
     * Você precisa passar qual foi a condição que acabou o jogo.
     * Condição 1: Você foi incapacitado, sua saúde chegou a menos de 20%
     * Condição 2: A bomba explodiu, quando o tempo acaba
     * @param condicao Número inteiro com a condição
     */
    private void exibirGameOver(int condicao) {
        if (condicao == 1) {
            System.out.println("Voce foi incapacitado.");
        } else if (condicao == 2) {
            System.out.println("A bomba explodiu.");
        } else if (condicao == 3) {
            System.out.println("Voce salvou a universidade faltando apenas "+tempoRestante+" minutos.");
            System.out.println("Parabens! Voce venceu!");
        }
        
        System.out.println("GAME OVER");
    }
    
    /**
     * Batalha os terroristas que estão no ambiente atual.
     * Essa função retorna um log com o resultado da batalha para ser exibido.
     * @return String com o resultado da batalha
     */
    private String batalha() {
        int nTerror= ambienteAtual.qtdeTerroristas();
        String logBatalha = "Ha "+nTerror+" terroristas no local.\n";
        int danoRecebido = 0;
        
        while (ambienteAtual.temTerrorista() && danoRecebido < jogador.getSaude()) {
            int nTerrorRodada= ambienteAtual.qtdeTerroristas();
            int danos[] = new int[nTerrorRodada];
            for (int i = 0;i < nTerrorRodada;i++) {
                danos[i] = jogador.getDanoCausado();
            }
            boolean armado = jogador.getMunicao() > 0;
            danoRecebido += ambienteAtual.batalharTerrorista(danos,armado);
        }
        
        if (danoRecebido > jogador.getSaude()) {
            danoRecebido = jogador.getSaude();
        }
        
        logBatalha += "Voce conseguiu incapacitar "+(nTerror-ambienteAtual.qtdeTerroristas())+
                " terroristas.\nVoce recebeu "+danoRecebido+" de dano.";
        jogador.receberDano(danoRecebido);
        return logBatalha;
    }
    
    /**
     * Tenta observar algo. Se o comando for somente a palavra "observar",
     * o jogador observa o ambiente e todos os itens contidos nele.
     * Se o comando possuir uma segunda palavra, o jogador tenta observar
     * o que foi passado nessa segunda palavra, seja um item ou outra coisa.
     * @param comando Palavras de comando digitadas.
     */
    private void observar(Comando comando) {
        if (comando.temSegundaPalavra()) {
            if (comando.getSegundaPalavra().equals("bomba")) {
                if (ambienteAtual.temBomba()) {
                    System.out.println(ambienteAtual.getDescricaoBomba());
                } else {
                    System.out.println("Nao ha uma bomba no local.");
                }
            } else {
                //Se há uma segunda palavra no comando, observa o item
                String desc = ambienteAtual.getItemDescricao(comando.getSegundaPalavra());
                if (desc == null) {
                    desc = jogador.getItemDescricao(comando.getSegundaPalavra());
                }

                if (desc != null) {
                    tempoRestante--;
                    System.out.println(desc);
                } else {
                    System.out.println("Nao achei esse item");
                }
            }
        } else {
            //Se não há segunda palavra, observa o ambiente
            tempoRestante--;
            System.out.println(ambienteAtual.getDescricaoLonga());
            if (ambienteAtual.temBomba()) {
                System.out.println("Ha uma bomba no local.\n");
            }
            System.out.print(ambienteAtual.getDescricaoTerroristas());
            System.out.println("Itens no ambiente: "+ambienteAtual.getItens());
            System.out.println("Itens na mochila: "+jogador.getItens());
            System.out.println("Saude: "+jogador.getSaude());
            if (jogador.temArma()) {
                System.out.println("Municao: "+jogador.getMunicao());
            }
            System.out.println("Tempo restante: "+tempoRestante);
        }
    }
    
    /**
     * Tenta coletar algo. 
     * O jogador tenta coletar o que foi passado na segunda palavra do comando,
     * seja um item ou outra coisa.
     * @param comando Palavras de comando digitadas.
     */
    private void coletar(Comando comando) {
        if (comando.temSegundaPalavra()) {
            //String[] 
            Item i = ambienteAtual.coletarItem(comando.getSegundaPalavra());
            if (i == null) {
                System.out.println("Nao achei esse item");
            } else {
                if (jogador.pegarItem(i)){
                    tempoRestante--;
                    System.out.println("Voce coletou "+i.getNome());
                } else {
                    System.out.println("Item eh pesado demais.");
                    ambienteAtual.ajustarItens(i);
                }
            }
        } else {
            System.out.println("Coletar o que?");
        }
    }
    
    /**
     * Tenta largar algo.
     * O jogador tenta largar o que foi passado na segunda palavra do comando.
     * Se ele digitar "largar arma", ele larga a arma que estiver empunhando.
     * 
     * @param comando Palavras de comando digitadas.
     */
    private void largar(Comando comando) {
        if (comando.temSegundaPalavra()) {
            if (comando.getSegundaPalavra().equals("arma")) {
                Item i = jogador.largarArma();
                if (i != null) {
                    tempoRestante--;
                    ambienteAtual.ajustarItens(i);
                    System.out.println("Voce largou sua arma.");
                } else {
                    System.out.println("Voce nao esta empunhando arma.");
                }
            } else {
                Item i = jogador.largarItem(comando.getSegundaPalavra());
                if (i != null) {
                    tempoRestante--;
                    ambienteAtual.ajustarItens(i);
                    System.out.println("Voce largou "+i.getNome()+".");
                } else {
                    System.out.println("Nao achei esse item");
                }
            }
        } else {
            System.out.println("Largar o que?");
        }
    }
    
    /**
     * Tenta empunhar uma arma.
     * O jogador tenta empunhar o que foi passado na segunda palavra de comando.
     * Se o jogador estiver carregando a arma ou se a arma estiver no ambiente,
     * o jogador larga a arma antiga e passa a empunhar a nova.
     * @param comando Palavras de comando digitadas.
     */
    private void empunhar(Comando comando) {
        if (comando.temSegundaPalavra()) {
            
            Item i = jogador.largarItem(comando.getSegundaPalavra());
            if (i == null) {
                //Nao achou na mochila, procura no ambiente
                i = ambienteAtual.coletarItem(comando.getSegundaPalavra());
            }
            
            if (i == null) {
                System.out.println("Nao encontrei esse item.");
            } else {
                //Achou o item
                tempoRestante--;
                if (!i.ehArma()) {
                    System.out.print("Item nao eh uma arma.");
                    if (jogador.pegarItem(i)) {
                        System.out.println(" Voce guardou "+i.getNome()+".");
                    } else {
                        ambienteAtual.ajustarItens(i);;
                        System.out.println(" Voce largou "+i.getNome()+".");
                    }
                } else {
                    //Achou o item, e eh uma arma
                    if (jogador.temArma()) {
                        //Jogador larga a arma antiga e empunha a nova
                        Item armaAntiga = jogador.largarArma();
                        jogador.empunharArma((Arma) i);
                        ambienteAtual.ajustarItens(armaAntiga);
                        System.out.println("Voce largou "+armaAntiga.getNome()+
                                " e comecou a empunhar "+i.getNome()+".");
                    } else {
                        //Jogador empunha a arma nova
                        jogador.empunharArma((Arma) i);
                        System.out.println("Voce comecou a empunhar "+i.getNome()+".");
                    }
                }
            }
        } else {
            System.out.println("Empunhar o que?");
        }
    }
    
    /**
     * Tenta revistar os terroristas.
     * Comando será executado se o jogador digitar as opções:
     * "revistar" "revistar terrorista" ou "revistar terroristas"
     * @param comando 
     */
    private void revistar(Comando comando) {
        boolean executa = false;
        if (comando.temSegundaPalavra()) {
            String s = comando.getSegundaPalavra();
            if (s.equals("terrorista") || s.equals("terroristas")) {
                executa = true;
            }
        } else {
            executa = true;
        }
        
        if (executa) {
            tempoRestante -= ambienteAtual.qtdeTotalTerroristas();
            System.out.println(ambienteAtual.revistarTerroristas());
        } else {
            System.out.println("Revistar o que?");
        }
    }
    
    /**
     * Tenta usar um item de cura.
     * @param comando Comando
     */
    private void usar(Comando comando) {
        if (comando.temSegundaPalavra()) {
            if (jogador.usarItem(comando.getSegundaPalavra())) {
                tempoRestante--;
                System.out.println("Voce foi curado. Saude atual: "+jogador.getSaude());
            } else {
                System.out.println("Item nao pode ser usado.");
            }
        } else {
            System.out.println("Usar o que?");
        }
    }
    
    /**
     * Tenta cortar um fio da bomba.
     * Entra aqui quando o jogador digitar "cortar",
     * e tenta cortar o nome do fio passado.
     * @param comando Comando
     * @return True se a bomba foi desarmada, false se não foi desarmada
     */
    private boolean cortar(Comando comando) {
        if (comando.temSegundaPalavra()) {
            if (ambienteAtual.temBomba()) {
                if (jogador.possuiAlicate()) {
                    int corte = ambienteAtual.cortarFio(comando.getSegundaPalavra());
                    tempoRestante--;
                    if (corte >= 0 && corte != 1) {
                        System.out.println("Nao aconteceu nada.");
                    } else if (corte == -1) {
                        tempoRestante = 0;
                    } else if (corte == -2) {
                        tempoRestante /= 2;
                        System.out.println("O tempo diminuiu pela metade. Restam "+tempoRestante+" minutos.");
                    } else if (corte == 1) {
                        System.out.println("A bomba parou de clicar.");
                        return true;
                    }
                } else {
                    System.out.println("Voce precisa de um alicate para cortar o fio.");
                }
            } else {
                System.out.println("Nao ha nenhuma bomba aqui.");
            }
        } else {
            System.out.println("Cortar o que?");
        }
        return false;
    }

    /** 
     * "Sair" foi digitado. Verifica o resto do comando pra ver
     * se nos queremos realmente sair do jogo.
     * @return true, se este comando sai do jogo, false, caso contrario
     */
    private boolean sair(Comando comando) {
        if(comando.temSegundaPalavra()) {
            System.out.println("Sair o que?");
            return false;
        }
        else {
            return true;  // sinaliza que nos queremos sair
        }
    }
    
    /**
     * Printe em qual ambiente o jogador está.
     * Imprime o ambiente atual do jogador
     * e em seguida quais saídas aquele ambiente dispõe.
     */
    private void exibirAmbienteAtual() {
        System.out.println("Tempo restante: "+tempoRestante+" min.");
        System.out.println("Voce esta " + ambienteAtual.getDescricao());
        if (ambienteAtual.temBomba()) {
            System.out.println("Ha uma bomba no local.");
        }
        System.out.println("Sua saude atual eh "+jogador.getSaude());
        System.out.println("Saidas: "+ambienteAtual.getSaidas());
    }
}
