package Sistema;


import java.io.IOException;
import java.util.Random;

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
    private boolean acabouJogo;
        
    /**
     * Cria o jogo e incializa seu mapa interno.
     * @param tempo Tempo limite que o jogador tem para desativar a bomba, em minutos
     * @throws java.io.IOException Lança exceção caso aconteça erro de leitura de arquivo.
     */
    public Jogo(int tempo) throws IOException {
        tempoRestante = tempo;
        criarAmbientes();
        analisador = new Analisador();
        jogador = new Agente(5,100);
        acabouJogo = false;
    }
    
    /**
     * Cria todos os ambientes e liga as saidas deles.
     * O método lê todas as informações de todos os ambientes a partir
     * de um arquivo texto.
     */
    private void criarAmbientes() throws IOException {
        OperadorArquivo o = new OperadorArquivo("ambients/ambientes.txt");
        ambienteAtual = o.criarAmbientes();
        if (ambienteAtual == null) {
            throw new AmbienteInicialNaoDefinidoException();
        }
        
    }

    public String imprimirBoasVindas() {
        String retorno = "\n";
        retorno += "Bem-vindo ao Counter-Strike: Lavras!\n";
        retorno += "Digite 'ajuda' se voce precisar de ajuda.\n\n";
        retorno += exibirAmbienteAtual();
        return retorno + "\n";
    }

    /**
     * Dado um comando, processa-o (ou seja, executa-o)
     * @param linha O Comando a ser processado.
     * @return true se o comando finaliza o jogo.
     */ 
    public String processarComando(String linha) {
        Comando comando = analisador.pegarComando(linha);
        if(comando.ehDesconhecido()) {
            return "Eu não entendi o que você disse...\n";
        }
        
        String retorno = "";
        String palavraDeComando = comando.getPalavraDeComando();
        if (palavraDeComando.equals("ajuda")) {
            retorno += imprimirAjuda();
        }
        else if (palavraDeComando.equals("ir")) {
            retorno += irParaAmbiente(comando);
        }
        else if (palavraDeComando.equals("observar")) {
            retorno += observar(comando);
        } else if (palavraDeComando.equals("coletar")) {
            retorno += coletar(comando);
        } else if (palavraDeComando.equals("largar")) {
            retorno += largar(comando);
        } else if (palavraDeComando.equals("empunhar")) {
            retorno += empunhar(comando);
        } else if (palavraDeComando.equals("revistar")) {
            retorno += revistar(comando);
        } else if (palavraDeComando.equals("usar")) {
            retorno += usar(comando);
        } else if (palavraDeComando.equals("cortar")) {
            retorno += cortar(comando);
        } 
        if (tempoRestante <= 0) {
            acabouJogo = true;
            retorno += exibirGameOver(2);
        } else if (!jogador.saudavel()) {
            acabouJogo = true;
            retorno += exibirGameOver(1);
        }
        
        if (acabouJogo) {
            retorno += "Obrigado por jogar. Até mais!\n";
        }
        return retorno + "\n";
    }

    // Implementacoes dos comandos do usuario

    /**
     * Retorna informacoes de ajuda.
     * @return String com as informações de ajuda.
     */ 
    private String imprimirAjuda() {
        String retorno = "Plantaram uma bomba no Restaurante Universitário.\n";
        retorno += "Você precisa correr contra o tempo para desarmá-la.\n";
        retorno += "Tempo restante: "+tempoRestante+" minutos.\n\n";
        retorno += "Suas palavras de comando são:\n";
        retorno += "   "+analisador.pegarComandosValidos()+"\n";
        return retorno;
    }

    /** 
     * Tenta ir em uma direcao. Se existe uma saida entra no 
     * novo ambiente, caso contrario imprime mensagem de erro.
     * @return Log com as informações do comando processado
     */
    private String irParaAmbiente(Comando comando) {
        if(!comando.temSegundaPalavra()) {
            // se nao ha segunda palavra, nao sabemos pra onde ir...
            return "Ir pra onde?\n";
        }

        String direcao = comando.getSegundaPalavra();
        String retorno = "";
        // Tenta sair do ambiente atual
        Ambiente proximoAmbiente = null;
        proximoAmbiente = ambienteAtual.getAmbiente(direcao);
        

        if (proximoAmbiente == null) {
            retorno += "Nao há passagem!\n";
        }
        else {
            boolean passar = true;
            
            if (proximoAmbiente.getTranca() != 0) {
                retorno += "Porta está trancada. ";
                //Tenta achar uma chave que desbloqueia
                if (jogador.desbloquearPorta(proximoAmbiente.getTranca())) {
                    retorno += "Você a destrancou.\n";
                    proximoAmbiente.destrancar();
                } else {
                    passar = false;
                    retorno += "Você precisa da chave para entrar.\n";
                }    
            }
            
            if (passar) {
                ambienteAtual = proximoAmbiente;
                tempoRestante--;

                if (tempoRestante > 0) {
                    if (ambienteAtual.temTerrorista()) {
                        Random r = new Random();
                        retorno += batalha();
                        tempoRestante-= 1+r.nextInt(3);
                    }

                    if (jogador.saudavel() && tempoRestante > 0) {
                        retorno += exibirAmbienteAtual();
                    }
                }
            }
        }
        return retorno;
    }
    
    /**
     * Exibe a mensagem de GAME OVER.
     * Você precisa passar qual foi a condição que acabou o jogo.
     * Condição 1: Você foi incapacitado, sua saúde chegou a menos de 20%
     * Condição 2: A bomba explodiu, quando o tempo acaba
     * @param condicao Número inteiro com a condição
     * @return Mensagem de Game Over
     */
    private String exibirGameOver(int condicao) {
        
        if (condicao == 1) {
            return "Você foi incapacitado.\nGAME OVER\n";
        } else if (condicao == 2) {
            return "A bomba explodiu.\nGAME OVER\n";
        } else if (condicao == 3) {
            return "Você salvou a universidade faltando apenas "+tempoRestante+" minutos.\n"
                    + "Parabéns! Você venceu!\n";
        } else if (condicao == 4) {
            
        }
        return "";
    }
    
    /**
     * Batalha os terroristas que estão no ambiente atual.
     * Essa função retorna um log com o resultado da batalha para ser exibido.
     * @return String com o resultado da batalha
     */
    private String batalha() {
        int nTerror= ambienteAtual.qtdeTerroristas();
        String logBatalha = "Há "+nTerror+" terroristas no local.\n";
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
        
        logBatalha += "Você conseguiu incapacitar "+(nTerror-ambienteAtual.qtdeTerroristas())+
                " terroristas.\nVocê recebeu "+danoRecebido+" de dano.\n";
        jogador.receberDano(danoRecebido);
        return logBatalha;
    }
    
    /**
     * Tenta observar algo. Se o comando for somente a palavra "observar",
     * o jogador observa o ambiente e todos os itens contidos nele.
     * Se o comando possuir uma segunda palavra, o jogador tenta observar
     * o que foi passado nessa segunda palavra, seja um item ou outra coisa.
     * @param comando Palavras de comando digitadas.
     * @return String com o log do comando processado
     */
    private String observar(Comando comando) {
        String retorno = "";
        if (comando.temSegundaPalavra()) {
            if (comando.getSegundaPalavra().equals("bomba")) {
                if (ambienteAtual.temBomba()) {
                    retorno += ambienteAtual.getDescricaoBomba() + "\n";
                } else {
                    retorno += "Não há uma bomba no local.\n";
                }
            } else {
                //Se há uma segunda palavra no comando, observa o item
                String desc = ambienteAtual.getItemDescricao(comando.getSegundaPalavra());
                if (desc == null) {
                    desc = jogador.getItemDescricao(comando.getSegundaPalavra());
                }

                if (desc != null) {
                    tempoRestante--;
                    retorno += desc + "\n";
                } else {
                    retorno += "Nao achei esse item\n";
                }
            }
        } else {
            //Se não há segunda palavra, observa o ambiente
            tempoRestante--;
            retorno += ambienteAtual.getDescricaoLonga() + "\n";
            if (ambienteAtual.temBomba()) {
                retorno += "Há uma bomba no local.\n\n";
            }
            retorno += "Saídas: "+ambienteAtual.getSaidas()+"\n";
            retorno += ambienteAtual.getDescricaoTerroristas();
            retorno += "Itens no ambiente: "+ambienteAtual.getItens() + "\n";
            retorno += "Itens na mochila: "+jogador.getItens() + "\n";
            retorno += "Saúde: "+jogador.getSaude() + "\n";
            if (jogador.temArma()) {
                retorno += "Munição: "+jogador.getMunicao() + "\n";
            }
            retorno += "Tempo restante: "+tempoRestante + "\n";
        }
        return retorno;
    }
    
    /**
     * Tenta coletar algo. 
     * O jogador tenta coletar o que foi passado na segunda palavra do comando,
     * seja um item ou outra coisa.
     * @param comando Palavras de comando digitadas.
     * @return String com o log do comando processado
     */
    private String coletar(Comando comando) {
        String retorno = "";
        if (comando.temSegundaPalavra()) {
            //String[] 
            Item i = ambienteAtual.coletarItem(comando.getSegundaPalavra());
            if (i == null) {
                retorno += "Nao achei esse item\n";
            } else {
                if (jogador.pegarItem(i)){
                    tempoRestante--;
                    retorno += "Voce coletou "+i.getNome()+"\n";
                } else {
                    retorno+="Item é pesado demais.\n";
                    ambienteAtual.ajustarItens(i);
                }
            }
        } else {
            retorno += "Coletar o quê?\n";
        }
        return retorno;
    }
    
    /**
     * Tenta largar algo.
     * O jogador tenta largar o que foi passado na segunda palavra do comando.
     * Se ele digitar "largar arma", ele larga a arma que estiver empunhando.
     * 
     * @param comando Palavras de comando digitadas.
     * @return String com o log do comando processado
     */
    private String largar(Comando comando) {
        String retorno = "";
        if (comando.temSegundaPalavra()) {
            if (comando.getSegundaPalavra().equals("arma")) {
                Item i = jogador.largarArma();
                if (i != null) {
                    tempoRestante--;
                    ambienteAtual.ajustarItens(i);
                    retorno += "Você largou sua arma.\n";
                } else {
                    retorno += "Voce não está empunhando arma.\n";
                }
            } else {
                Item i = jogador.largarItem(comando.getSegundaPalavra());
                if (i != null) {
                    tempoRestante--;
                    ambienteAtual.ajustarItens(i);
                    retorno += "Você largou "+i.getNome()+".\n";
                } else {
                    retorno += "Não achei esse item\n";
                }
            }
        } else {
            retorno += "Largar o quê?\n";
        }
        return retorno;
    }
    
    /**
     * Tenta empunhar uma arma.
     * O jogador tenta empunhar o que foi passado na segunda palavra de comando.
     * Se o jogador estiver carregando a arma ou se a arma estiver no ambiente,
     * o jogador larga a arma antiga e passa a empunhar a nova.
     * @param comando Palavras de comando digitadas.
     * @return String com o log do comando processado
     */
    private String empunhar(Comando comando) {
        String retorno = "";
        if (comando.temSegundaPalavra()) {
            
            Item i = jogador.largarItem(comando.getSegundaPalavra());
            if (i == null) {
                //Nao achou na mochila, procura no ambiente
                i = ambienteAtual.coletarItem(comando.getSegundaPalavra());
            }
            
            if (i == null) {
                retorno += "Não encontrei esse item.\n";
            } else {
                //Achou o item
                tempoRestante--;
                if (!i.ehArma()) {
                    retorno += "Item nao é uma arma.";
                    if (jogador.pegarItem(i)) {
                        retorno += " Você guardou "+i.getNome()+".\n";
                    } else {
                        ambienteAtual.ajustarItens(i);;
                        retorno += " Você largou "+i.getNome()+".\n";
                    }
                } else {
                    //Achou o item, e eh uma arma
                    if (jogador.temArma()) {
                        //Jogador larga a arma antiga e empunha a nova
                        Item armaAntiga = jogador.largarArma();
                        jogador.empunharArma((Arma) i);
                        ambienteAtual.ajustarItens(armaAntiga);
                        retorno += "Você largou "+armaAntiga.getNome()+
                                " e começou a empunhar "+i.getNome()+".\n";
                    } else {
                        //Jogador empunha a arma nova
                        jogador.empunharArma((Arma) i);
                        retorno += "Você começou a empunhar "+i.getNome()+".\n";
                    }
                }
            }
        } else {
            retorno += "Empunhar o quê?\n";
        }
        return retorno;
    }
    
    /**
     * Tenta revistar os terroristas.
     * Comando será executado se o jogador digitar as opções:
     * "revistar" "revistar terrorista" ou "revistar terroristas"
     * @param comando 
     * @return String com o log do comando processado
     */
    private String revistar(Comando comando) {
        String retorno = "";
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
            retorno += ambienteAtual.revistarTerroristas()+"\n";
        } else {
            retorno += "Revistar o quê?\n";
        }
        return retorno;
    }
    
    /**
     * Tenta usar um item de cura.
     * @param comando Comando
     * @return String com o log do comando processado
     */
    private String usar(Comando comando) {
        String retorno = "";
        if (comando.temSegundaPalavra()) {
            if (jogador.usarItem(comando.getSegundaPalavra())) {
                tempoRestante--;
                retorno += "Voce foi curado. Saúde atual: "+jogador.getSaude()+"\n";
            } else {
                retorno += "Item não pode ser usado.\n";
            }
        } else {
            retorno += "Usar o quê?\n";
        }
        return retorno;
    }
    
    /**
     * Tenta cortar um fio da bomba.
     * Entra aqui quando o jogador digitar "cortar",
     * e tenta cortar o nome do fio passado.
     * @param comando Comando
     * @return True se a bomba foi desarmada, false se não foi desarmada
     */
    private String cortar(Comando comando) {
        String retorno = "";
        if (comando.temSegundaPalavra()) {
            if (ambienteAtual.temBomba()) {
                if (jogador.possuiAlicate()) {
                    int corte = ambienteAtual.cortarFio(comando.getSegundaPalavra());
                    tempoRestante--;
                    if (corte >= 0 && corte != 1) {
                        retorno += "Não aconteceu nada.\n";
                    } else if (corte == -1) {
                        tempoRestante = 0;
                    } else if (corte == -2) {
                        tempoRestante /= 2;
                        retorno += "O tempo diminuiu pela metade. Restam "+tempoRestante+" minutos.\n";
                    } else if (corte == 1) {
                        retorno += "A bomba parou de clicar.\n";
                        acabouJogo = true;
                        retorno += exibirGameOver(3);
                    }
                } else {
                    retorno += "Você precisa de um alicate para cortar o fio.\n";
                }
            } else {
                retorno += "Nao há nenhuma bomba aqui.\n";
            }
        } else {
            retorno += "Cortar o quê?\n";
        }
        return retorno;
    }

    /**
     * Printe em qual ambiente o jogador está.
     * Imprime o ambiente atual do jogador
     * e em seguida quais saídas aquele ambiente dispõe.
     * @return String com a descrição do ambiente atual
     */    
    private String exibirAmbienteAtual() {
        String retorno = "";
        retorno += "Tempo restante: "+tempoRestante+" min.\n";
        retorno += "Você está " + ambienteAtual.getDescricao() + "\n";
        if (ambienteAtual.temBomba()) {
            retorno+="Há uma bomba no local.\n";
        }
        retorno += "Sua saúde atual é "+jogador.getSaude() + "\n";
        retorno += "Saídas: "+ambienteAtual.getSaidas()+"\n";
        return retorno;
    }
    
    /**
     * Retorna se o jogo acabou ou não.
     * @return True se o jogo acabou, false se não acabou
     */
    public boolean getAcabouJogo() {
        return acabouJogo;
    }
    
}
