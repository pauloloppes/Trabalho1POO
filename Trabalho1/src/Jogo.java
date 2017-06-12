
import java.util.ArrayList;
import java.util.Random;

/**
 *  Essa eh a classe principal da aplicacao "World of Zull".
 *  "World of Zuul" eh um jogo de aventura muito simples, baseado em texto.
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
     */
    public Jogo(int tempo) {
        tempoRestante = tempo;
        criarAmbientes();
        analisador = new Analisador();
        jogador = new Agente(5,100);
    }

    /**
     * Cria todos os ambientes e liga as saidas deles
     */
    private void criarAmbientes() {
        Ambiente fora, anfiteatro, cantina, laboratorio, escritorio;
      
        // cria os ambientes
        fora = new Ambiente("do lado de fora da entrada principal de uma universidade",
                "Descrever o ambiente",0);
        anfiteatro = new Ambiente("no anfiteatro",
                "Descrever o ambiente",0);
        cantina = new Ambiente("na cantina do campus",
                "Descrever o ambiente",0);
        laboratorio = new Ambiente("no laboratorio de computacao",
                "Descrever o ambiente",0);
        escritorio = new Ambiente("na sala de administracao dos computadores",
                "Descrever o ambiente",1);
        
        // inicializa as saidas dos ambientes
        fora.ajustarSaidas("leste", anfiteatro);
        fora.ajustarSaidas("sul", laboratorio);
        fora.ajustarSaidas("oeste", cantina);
        
        anfiteatro.ajustarSaidas("oeste", fora);
        
        cantina.ajustarSaidas("leste", fora);
        
        laboratorio.ajustarSaidas("norte", fora);
        laboratorio.ajustarSaidas("leste", escritorio);
        
        escritorio.ajustarSaidas("oeste", laboratorio);
        
        // inicializa terroristas
        String itens[][] = {{"chave1","0.05","Uma chave pequena."}};
        ArrayList<Item> itens2 = new ArrayList<>();
        itens2.add(new Curativo("curativo",0.02,"Um curativo.",10));
        itens2.add(new Curativo("xarope",0.02,"Um vidro de remedio.",30));
        itens2.add(new Chave("chave",0.05,"Uma chave pequena.",1));
        laboratorio.ajustarTerroristas(70, itens2);
        String arma[] = {"rifle","4","Um rifle.","50"};
        laboratorio.ajustarTerroristas(80, itens, arma);
        String itens3[][] = {};
        laboratorio.ajustarTerroristas(70, itens3, arma);
        cantina.ajustarTerroristas(70, itens2);
        cantina.ajustarTerroristas(80, itens, arma);
        cantina.ajustarTerroristas(70, itens3, arma);
        
        
        // inicializa itens
        
        fora.ajustarItens("cigarro", 0.03, "Uma bituca de cigarro.");
        fora.ajustarItens("lixeira", 6, "Uma lixeira. Está com lixo até a metade.");

        ambienteAtual = fora;  // o jogo comeca do lado de fora
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
        System.out.println("Bem-vindo ao World of Zuul!");
        System.out.println("World of Zuul eh um novo jogo de aventura, incrivelmente chato.");
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
        } 
        
        if (tempoRestante <= 0) {
            exibirGameOver(2);
            querSair = true;
        } else if (!jogador.saudavel()) {
            exibirGameOver(1);
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
        System.out.println("Tempo restante: "+tempoRestante);
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
            danoRecebido += ambienteAtual.batalharTerrorista(danos);
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
            //Se há uma segunda palavra no comando, observa o item
            String desc = ambienteAtual.getItemDescricao(comando.getSegundaPalavra());
            if (desc != null) {
                tempoRestante--;
                System.out.println(desc);
            } else {
                System.out.println("Nao achei esse item");
            }
        } else {
            //Se não há segunda palavra, observa o ambiente
            tempoRestante--;
            System.out.println(ambienteAtual.getDescricaoLonga());
            System.out.print(ambienteAtual.getDescricaoTerroristas());
            System.out.println("Itens no ambiente: "+ambienteAtual.getItens());
            System.out.println("Itens na mochila: "+jogador.getItens());
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
                        jogador.empunharArma(i);
                        ambienteAtual.ajustarItens(armaAntiga);
                        System.out.println("Voce largou "+armaAntiga.getNome()+
                                " e comecou a empunhar "+i.getNome()+".");
                    } else {
                        //Jogador empunha a arma nova
                        jogador.empunharArma(i);
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
        System.out.println("Sua saude atual eh "+jogador.getSaude());
        System.out.println("Saidas: "+ambienteAtual.getSaidas());
    }
}
