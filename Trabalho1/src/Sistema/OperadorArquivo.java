/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistema;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Classe que faz operações de leitura e escrita com arquivos.
 * @author paulo
 */
public class OperadorArquivo {
    
    private String nomeArquivo;
    
    /**
     * Cria um novo OperadorArquivo recebendo o nome do arquivo
     * com o qual ele irá trabalhar.
     * @param nome Nome do arquivo
     */
    public OperadorArquivo(String nome) {
        nomeArquivo = nome;
    }
    
    /**
     * Cria uma bomba a partir de um arquivo texto.
     * @param arq Arquivo texto a ser lido
     * @return HashMap com os fios da bomba e seus respectivos efeitos
     */
    private HashMap<String, Integer> criarBomba(BufferedReader arq) throws IOException {
    HashMap<String,Integer> fios = null;
        String leitura = arq.readLine();
        fios = new HashMap<>();
        while (!leitura.equals("END_DEF")) {

            String[] s = leitura.split(" ");
            fios.put(s[0], Integer.parseInt(s[1]));

            leitura = arq.readLine();
        }
        return fios;
    }
    
    /**
     * Cria um novo ambiente a partir de um arquivo texto.
     * @param arq Arquivo texto a ser lido
     * @return Ambiente criado
     */
    private Ambiente criarNovoAmbiente(BufferedReader arq) throws IOException {
        Ambiente a = null;

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

        return a;
    }
    
    /**
     * Ajusta as saídas de um ambiente a partir de um arquivo texto.
     * É preciso que todos os ambientes já tenham sido criados neste ponto.
     * @param a Ambiente que receberá as novas saídas
     * @param arq Arquivo texto a ser lido
     * @param ambientes HashMap com todos os ambientes criados
     */
    private void criarSaidas(Ambiente a,BufferedReader arq,HashMap<String,Ambiente> ambientes) throws IOException {

        String leitura = arq.readLine();
        while (!leitura.equals("END_DEF")) {
            String[] s = leitura.split(" ");
            Ambiente b = ambientes.get(s[1]);
            if (b!= null)
                a.ajustarSaidas(s[0], b);
            leitura = arq.readLine();
        }

    }
    
    /**
     * Cria um terrorista a partir de um arquivo texto.
     * @param arq Arquivo texto a ser lido
     * @return Terrorista criado
     */
    private Terrorista criarTerrorista(BufferedReader arq) throws IOException {
        Terrorista t = null;
        
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

        
        return t;
    }
    
    /**
     * Cria os itens de um ambiente a partir de um arquivo texto.
     * A função recebe o Ambiente e já adiciona os itens automaticamente.
     * @param a Ambiente que receberá os itens criados
     * @param arq Arquivo texto a ser lido
     */
    private void criarItensAmbiente(Ambiente a, BufferedReader arq) throws IOException {
        
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

    }
    
    /**
     * Lê um arquivo texto até chegar no fim do bloco atual.
     * A função é necessária quando há algum erro na hora de ler o arquivo,
     * seja com algum ambiente que não existe, tipo de item não especificado.
     * Ela percorre o arquivo até chegar na próxima linha marcada com END_DEF
     * para poder iniciar um novo bloco.
     * @param arq Arquivo texto a ser lido
     */
    private void lerAteEndDef(BufferedReader arq) throws IOException {

        String leitura = arq.readLine();
        while (!leitura.equals("END_DEF")) {
            leitura = arq.readLine();
        }

    }
    
    /**
     * Cria todos os ambientes e liga as saidas deles.
     * O método lê todas as informações de todos os ambientes a partir
     * de um arquivo texto. Ele retorna qual será o ambiente inicial do jogo.
     * @return Ambiente inicial do jogo
     * @throws java.io.FileNotFoundException Lança exceção caso o arquivo não exista
     */
    public Ambiente criarAmbientes() throws IOException {
        
        HashMap<String,Ambiente> ambientes = new HashMap<>();
        Ambiente atual = null;

        BufferedReader arqAmbientes = new BufferedReader(new FileReader(nomeArquivo));
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
                    atual = a;
                }
            }

            linha = arqAmbientes.readLine();
        }

        arqAmbientes.close();
        
        return atual;
        
    }
    
}
