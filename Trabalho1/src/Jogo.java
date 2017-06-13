
import java.util.ArrayList;
import java.util.HashMap;
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
        Ambiente portaria,dccFora,dccDentro,dexFora,dexDentro,quimicaFora,quimicaDentro,
                cantina,ruFora,ruDentro,bibliotecaFora,bibliotecaDentro,
                caFora,caDentro,bancoFora,bancoDentro,hospitalFora,hospitalDentro;
        
        // cria a bomba
        HashMap<String,Integer> fios = new HashMap<>();
        fios.put("azul", 3);
        fios.put("vermelho", 2);
        fios.put("verde", 1);
        fios.put("amarelo", -1);
        fios.put("cinza", -1);
        fios.put("preto", -2);
        fios.put("laranja", 4);
        fios.put("marrom", -1);
      
        // cria os ambientes
        portaria = new Ambiente("na portaria da universidade",
                "Ha uma placa de boas vindas do lado esquerdo.\n"
                        + "Uma multidao se aglomera atras da barreira policial.",0);
        dccFora = new Ambiente("na frente do departamento de computacao.",
                "Voce esta na frente de um predio branco com portas de vidro.\n"
                        + "Nao ha movimento no interior. Parece estar trancado.",0);
        dccDentro = new Ambiente("dentro do departamento de computacao.",
                "O departamento parece estar vazio. Nao se ouve um barulho.\n"
                        + "Todas as portas estao trancadas.",2);
        dexFora = new Ambiente("na frente do departamento de exatas.",
                "Voce esta na frente de um predio verde de dois andares.\n"
                        + "Vozes ecoam vindo de dentro.",0);
        dexDentro = new Ambiente("dentro do departamento de exatas.",
                "O lugar esta revirado. Gabinetes e gavetas estao abertas.\n"
                        + "Papeis e arquivo revirados nas mesas.",0);
        quimicaFora = new Ambiente("na frente do departamento de quimica.",
                "Voce esta na frente de um predio bege com portas de vidro.\n"
                        + "Ha uma movimentacao no interior do predio.",0);
        quimicaDentro = new Ambiente("dentro do departamento de quimica.",
                "O laboratorio foi usado recentemente.\n"
                        + "Objetos estao espalhados pelas mesas com restos de fluidos.",0);
        ruFora = new Ambiente("na frente do restaurante universitario.",
                "Voce esta na frente de um predio branco com design esquisito.\n"
                        + "A porta de vidro aparenta estar trancada.",0);
        cantina = new Ambiente("na cantina da universidade.",
                "Voce esta em uma cobertura, na frente de uma lanchonete.\n"
                        + "Mesas e cadeiras te cercam.",0);
        ruDentro = new Ambiente("dentro do restaurante universitario.",
                "Varias mesas longas estao cercadas de cadeiras dos dois lados.\n"
                        + "Voce ouve uns cliques repetindo a cada segundo.",1,fios);
        bibliotecaFora = new Ambiente("na frente da biblioteca.",
                "Voce esta na frente de um predio branco enorme.\n"
                        + "Nao ha movimento no interior. Parece estar trancado.",0);
        bibliotecaDentro = new Ambiente("dentro da biblioteca.",
                "O lugar eh repleto de estantes cheias de livros.\n"
                        + "Nao se ouve um barulho sequer no interior.\n",2);
        caFora = new Ambiente("na frente do centro academico.",
                "Voce esta na frente de um predio de dois andares.\n"
                        + "Aparenta estar trancado.",0);
        caDentro = new Ambiente("dentro do centro academico.",
                "Nao ha nenhuma movimentacao.\n"
                        + "Os dois andares parecem estar vazios.",3);
        bancoFora = new Ambiente("na frente do banco.",
                "Voce esta na frente de um predio pequeno com paredes de vidro."
                        + "Ha pessoas no interior.",0);
        bancoDentro = new Ambiente("dentro do banco.",
                "Um vidro resistente separa as duas metades do predio.",0);
        hospitalFora = new Ambiente("na frente do pronto socorro.",
                "Voce esta na frente de uma sala no andar de baixo de um predio redondo.\n"
                        + "Parece estar vazio.",0);
        hospitalDentro = new Ambiente("dentro do pronto socorro.",
                "Nao ha ninguem no interior.\n"
                        + "Varios objetos estao jogados nas mesas e gavetas.",0);
        
        // inicializa as saidas dos ambientes
        portaria.ajustarSaidas("leste", dccFora);
        
        dccFora.ajustarSaidas("norte", dexFora);
        dccFora.ajustarSaidas("oeste", portaria);
        dccFora.ajustarSaidas("leste", quimicaFora);
        dccFora.ajustarSaidas("dentro", dccDentro);
        dccDentro.ajustarSaidas("fora", dccFora);
        
        dexFora.ajustarSaidas("dentro", dexDentro);
        dexFora.ajustarSaidas("sul", dccFora);
        dexFora.ajustarSaidas("leste", cantina);
        dexDentro.ajustarSaidas("fora", dexFora);
        
        quimicaFora.ajustarSaidas("dentro", quimicaDentro);
        quimicaFora.ajustarSaidas("oeste", dccFora);
        quimicaFora.ajustarSaidas("leste", bibliotecaFora);
        quimicaFora.ajustarSaidas("norte", cantina);
        quimicaDentro.ajustarSaidas("fora", quimicaFora);
        
        cantina.ajustarSaidas("oeste", dexFora);
        cantina.ajustarSaidas("sul", quimicaFora);
        cantina.ajustarSaidas("leste", bancoFora);
        cantina.ajustarSaidas("norte", ruFora);
        
        ruFora.ajustarSaidas("dentro", ruDentro);
        ruFora.ajustarSaidas("sul", cantina);
        ruFora.ajustarSaidas("leste", caFora);
        ruDentro.ajustarSaidas("fora", ruFora);
        
        bibliotecaFora.ajustarSaidas("oeste", quimicaFora);
        bibliotecaFora.ajustarSaidas("norte", bancoFora);
        bibliotecaFora.ajustarSaidas("dentro", bibliotecaDentro);
        bibliotecaDentro.ajustarSaidas("fora", bibliotecaFora);
        
        bancoFora.ajustarSaidas("dentro", bancoDentro);
        bancoFora.ajustarSaidas("sul", bibliotecaFora);
        bancoFora.ajustarSaidas("oeste", cantina);
        bancoFora.ajustarSaidas("norte", caFora);
        bancoDentro.ajustarSaidas("fora", bancoFora);
        
        caFora.ajustarSaidas("sul", bancoFora);
        caFora.ajustarSaidas("oeste", ruFora);
        caFora.ajustarSaidas("leste", hospitalFora);
        caFora.ajustarSaidas("dentro", caDentro);
        caDentro.ajustarSaidas("fora", caFora);
        
        hospitalFora.ajustarSaidas("dentro", hospitalDentro);
        hospitalFora.ajustarSaidas("oeste", caFora);
        hospitalDentro.ajustarSaidas("fora", hospitalFora);
        
        // inicializa terroristas
        ArrayList<String[]> listaItens = new ArrayList<>();
        String[] itemNormal = new String[4];
        String[] itemEspecial = new String[5];
        
        itemEspecial[0] = "2";//tipo 2: chave
        itemEspecial[1] = "chave";//nome do item
        itemEspecial[2] = "0.05";//peso
        itemEspecial[3] = "Uma chave pequena.";//descricao
        itemEspecial[4] = "1";//numero da tranca
        listaItens.add(cloneVetor(itemEspecial));
        
        itemEspecial[0] = "3";//tipo 3: curativo
        itemEspecial[1] = "curativo";//nome
        itemEspecial[2] = "0.01";//peso
        itemEspecial[3] = "Um curativo.";//descricao
        itemEspecial[4] = "10";//poder de cura
        listaItens.add(cloneVetor(itemEspecial));
        
        bancoDentro.ajustarTerroristas(70, listaItens);
        listaItens.clear();
        
        itemNormal[0] = "0";//tipo 0: item normal
        itemNormal[1] = "papel";//nome do item
        itemNormal[2] = "0.01";//peso do item em kg
        itemNormal[3] = "Anotado no papel: 'fio laranja, azul, vermelho, verde.'";//descricao do item
        listaItens.add(cloneVetor(itemNormal));//clona o item e adiciona na lista
        
        itemEspecial[0] = "3";
        itemEspecial[1] = "xarope";
        itemEspecial[2] = "0.5";
        itemEspecial[3] = "Um vidro de remedio.";
        itemEspecial[4] = "30";
        listaItens.add(cloneVetor(itemEspecial));
        
        dexDentro.ajustarTerroristas(70, listaItens);
        listaItens.clear();
        
        itemEspecial[0] = "4";
        itemEspecial[1] = "rifle";
        itemEspecial[2] = "4";
        itemEspecial[3] = "Um rifle.";
        itemEspecial[4] = "50";
        listaItens.add(cloneVetor(itemEspecial));
        
        dexDentro.ajustarTerroristas(80, listaItens);
        listaItens.clear();
        
        itemEspecial[0] = "4";
        itemEspecial[1] = "semi";
        itemEspecial[2] = "0.9";
        itemEspecial[3] = "Uma pistola semi-automatica.";
        itemEspecial[4] = "15";
        listaItens.add(cloneVetor(itemEspecial));
        
        itemNormal[0] = "0";//tipo 0: item normal
        itemNormal[1] = "planta";//nome do item
        itemNormal[2] = "0.02";//peso do item em kg
        itemNormal[3] = "Planta do restaurante universitario.";//descricao do item
        listaItens.add(cloneVetor(itemNormal));//clona o item e adiciona na lista
        
        quimicaDentro.ajustarTerroristas(82, listaItens);
        listaItens.clear();
        
        itemEspecial[0] = "4";
        itemEspecial[1] = "espingarda";
        itemEspecial[2] = "4.4";
        itemEspecial[3] = "Uma espingarda.";
        itemEspecial[4] = "25";
        listaItens.add(cloneVetor(itemEspecial));
        
        itemEspecial[0] = "3";
        itemEspecial[1] = "gaze";
        itemEspecial[2] = "0.02";
        itemEspecial[3] = "Um pedaço de gaze.";
        itemEspecial[4] = "20";
        listaItens.add(cloneVetor(itemEspecial));
        
        quimicaDentro.ajustarTerroristas(75, listaItens);
        listaItens.clear();
        
        
        // inicializa itens
        
        //fora.ajustarItens("cigarro", 0.03, "Uma bituca de cigarro.");
        //fora.ajustarItens("lixeira", 6, "Uma lixeira. Está com lixo até a metade.");

        ambienteAtual = portaria;  // o jogo comeca na portaria
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
    
    /**
     * Retorna um clone de um vetor, com todos os campos iguais em uma instancia diferente.
     * @param s Vetor de string a ser clonado
     * @return Clone
     */
    private String[] cloneVetor(String[] s) {
        String[] retorno = new String[s.length];
        
        for (int i = 0; i < s.length; i++) {
            retorno[i] = s[i];
        }
        
        return retorno;
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
            String[] i = ambienteAtual.coletarItem(comando.getSegundaPalavra());
            if (i == null) {
                System.out.println("Nao achei esse item");
            } else {
                if (jogador.pegarItem(i)){
                    tempoRestante--;
                    System.out.println("Voce coletou "+i[1]);
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
                String[] i = jogador.largarArma();
                if (i != null) {
                    tempoRestante--;
                    ambienteAtual.ajustarItens(i);
                    System.out.println("Voce largou sua arma.");
                } else {
                    System.out.println("Voce nao esta empunhando arma.");
                }
            } else {
                String[] i = jogador.largarItem(comando.getSegundaPalavra());
                if (i != null) {
                    tempoRestante--;
                    ambienteAtual.ajustarItens(i);
                    System.out.println("Voce largou "+i[1]+".");
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
            
            String[] i = jogador.largarItem(comando.getSegundaPalavra());
            if (i == null) {
                //Nao achou na mochila, procura no ambiente
                i = ambienteAtual.coletarItem(comando.getSegundaPalavra());
            }
            
            if (i == null) {
                System.out.println("Nao encontrei esse item.");
            } else {
                //Achou o item
                tempoRestante--;
                if (!i[0].equals("1")) {
                    System.out.print("Item nao eh uma arma.");
                    if (jogador.pegarItem(i)) {
                        System.out.println(" Voce guardou "+i[1]+".");
                    } else {
                        ambienteAtual.ajustarItens(i);;
                        System.out.println(" Voce largou "+i[1]+".");
                    }
                } else {
                    //Achou o item, e eh uma arma
                    if (jogador.temArma()) {
                        //Jogador larga a arma antiga e empunha a nova
                        String[] armaAntiga = jogador.largarArma();
                        jogador.empunharArma(i);
                        ambienteAtual.ajustarItens(armaAntiga);
                        System.out.println("Voce largou "+armaAntiga[1]+
                                " e comecou a empunhar "+i[1]+".");
                    } else {
                        //Jogador empunha a arma nova
                        jogador.empunharArma(i);
                        System.out.println("Voce comecou a empunhar "+i[1]+".");
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
