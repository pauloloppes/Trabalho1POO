import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe Ambiente - um ambiente em um jogo adventure.
 *
 * Esta classe eh parte da aplicacao "World of Zuul".
 * "World of Zuul" eh um jogo de aventura muito simples, baseado em texto.  
 *
 * Um "Ambiente" representa uma localizacao no cenario do jogo. Ele eh
 * conectado aos outros ambientes atraves de saidas. As saidas sao
 * nomeadas como norte, sul, leste e oeste. Para cada direcao, o ambiente
 * guarda uma referencia para o ambiente vizinho, ou null se nao ha
 * saida naquela direcao.
 * 
 * @author  Michael Kölling and David J. Barnes (traduzido por Julio Cesar Alves)
 * @version 2011.07.31 (2017.05.16)
 */
public class Ambiente  {
    private String descricao;
    private String descricaoLonga;
    private HashMap<String, Ambiente> saidas;
    private HashMap<String, Item> itens;
    private ArrayList<Terrorista> terroristas;
    private int tranca;
    private Bomba bomba;
    

    /**
     * Cria um ambiente com a "descricao" passada. Inicialmente, ele
     * nao tem saidas. "descricao" eh algo como "uma cozinha" ou
     * "um jardim aberto".
     * "descricaoLonga" eh uma descrição mais detalhada, que será
     * exibida quando o jogador observar o ambiente.
     * @param descricao A descricao do ambiente.
     * @param descricaoLonga A descricao longa do ambiente.
     * @param tranca Número da tranca do ambiente. 0 = destrancada
     */
    public Ambiente(String descricao,String descricaoLonga,int tranca)  {
        this.descricao = descricao;
        this.descricaoLonga = descricaoLonga;
        this.tranca = tranca;
        this.bomba = null;
        saidas = new HashMap<String, Ambiente>();
        itens = new HashMap<>();
        terroristas = new ArrayList<>();
    }
    
    /**
     * Cria um ambiente com a "descricao" passada. Inicialmente, ele
     * nao tem saidas. "descricao" eh algo como "uma cozinha" ou
     * "um jardim aberto".
     * "descricaoLonga" eh uma descrição mais detalhada, que será
     * exibida quando o jogador observar o ambiente.
     * "fios" eh passado quando um ambiente possui uma bomba,
     * e esse parametro recebe a ordem de desarme.
     * @param descricao A descricao do ambiente.
     * @param descricaoLonga A descricao longa do ambiente.
     * @param tranca Número da tranca do ambiente. 0 = destrancada
     * @param fios Se o ambiente possui uma bomba, recebe os fios.
     */
    public Ambiente(String descricao,String descricaoLonga,int tranca,HashMap<String,Integer> fios)  {
        this.descricao = descricao;
        this.descricaoLonga = descricaoLonga;
        this.tranca = tranca;
        this.bomba = new Bomba(fios);
        saidas = new HashMap<String, Ambiente>();
        itens = new HashMap<>();
        terroristas = new ArrayList<>();
    }

    /**
     * Define as saidas do ambiente. Cada direcao ou leva a um
     * outro ambiente ou eh null (nenhuma saida para la).
     * @param direcao Direção do ambiente
     * @param ambiente Ambiente
     */
    public void ajustarSaidas(String direcao, Ambiente ambiente)  {
        saidas.put(direcao,ambiente);
    }
    
    /**
     * Coloca um item na lista de itens do ambiente.
     * @param nome Nome do item
     * @param peso Peso do item em kg
     * @param descricao Descrição do item
     */
    public void ajustarItens(String nome,double peso,String descricao) {
        Item i = new Item(nome,peso,descricao);
        itens.put(nome,i);
    }
    
    /**
     * Coloca um item na lista de itens do ambiente.
     * String do item é estruturada da seguinte forma:
     * [0]: Inteiro, tipo do item.
     *      0 = Item comum
     *      1 = Arma
     *      2 = Chave
     *      3 = Curativo
     * [1]: String, nome do item
     * [2]: Double, peso do item
     * [3]: String, descrição do item
     * [4]: Existe se o item não for comum. É o 4º argumento dos itens especiais.
     * @param i Item
     */
    public void ajustarItens(String[] i) {
        Item novo = null;
        if (i[0].equals("0")) {
            novo = new Item(i[1],Double.parseDouble(i[2]),i[3]);
        } else if (i[0].equals("1")) {
            novo = new Arma(i[1],Double.parseDouble(i[2]),i[3],Integer.parseInt(i[4]));
        } else if (i[0].equals("2")) {
            novo = new Chave(i[1],Double.parseDouble(i[2]),i[3],Integer.parseInt(i[4]));
        } else if (i[0].equals("3")) {
            novo = new Curativo(i[1],Double.parseDouble(i[2]),i[3],Integer.parseInt(i[4]));
        }
        
        itens.put(i[1],novo);
    }
    
    /**
     * Coloca um terrorista no ambiente
     * Recebe como atributos um número inteiro representando a saúde do terrorista,
     * uma lista de itens em formato String.
     * String do item é estruturada da seguinte forma:
     * [0]: Inteiro, tipo do item.
     *      0 = Item comum
     *      1 = Arma a ser guardada
     *      2 = Chave
     *      3 = Curativo
     *      4 = Arma a ser empunhada
     * [1]: String, nome do item
     * [2]: Double, peso do item
     * [3]: String, descrição do item
     * [4]: Existe se o item não for comum. É o 4º argumento dos itens especiais.
     * @param saude Saúde do terrorista
     * @param itens Itens que o terrorista possui.
     */
    public void ajustarTerroristas(int saude,ArrayList<String[]> itens) {
        Terrorista t = new Terrorista(saude);
        
        for (String s[] : itens) {
            Item i = null;
            if (s[0].equals("0")) {
                i = new Item(s[1],Double.parseDouble(s[2]),s[3]);
            } else if (s[0].equals("1")) {
                i = new Arma(s[1],Double.parseDouble(s[2]),s[3],Integer.parseInt(s[4]));
            } else if (s[0].equals("2")) {
                i = new Chave(s[1],Double.parseDouble(s[2]),s[3],Integer.parseInt(s[4]));
            } else if (s[0].equals("3")) {
                i = new Curativo(s[1],Double.parseDouble(s[2]),s[3],Integer.parseInt(s[4]));
            } else if (s[0].equals("4")) {
                t.ajustarArma(s[1],Double.parseDouble(s[2]),s[3],Integer.parseInt(s[4]));
            }
            if (i!= null)
                t.ajustarItens(i);
        }

        terroristas.add(t);
    }

    /**
     * @return A descricao do ambiente.
     */
    public String getDescricao() {
        return descricao;
    }
    
    /**
     * @return A descricao longa do ambiente.
     */
    public String getDescricaoLonga() {
        return descricaoLonga;
    }
    
    /**
     * Retorna se a saída existe no ambiente.
     * Retorna um Ambiente de destino caso a saída exista,
     * ou null caso a saída não exista.
     * @param direcao String com o nome da saída da qual você deseja retornar
     * @return Ambiente se existe, ou null se não existe
     */
    public Ambiente getAmbiente(String direcao) {
        return saidas.get(direcao);
    }
    
    /**
     * Retorna uma string com todas as direções que existem no Ambiente.
     * @return String com as saídas existentes no Ambiente separados por ;
     */
    public String getSaidas(){
        String textoSaidas = "";
        for (String direcao : saidas.keySet()) {
            textoSaidas = textoSaidas + direcao + "; ";
        }
        return textoSaidas;
    }
    
    /**
     * Retorna uma string com todos os itens que existem no Ambiente.
     * @return String com os itens existentes no Ambiente separados por ;
     */
    public String getItens(){
        String textoItens = "";
        for (String item : itens.keySet()) {
            textoItens = textoItens + item + "; ";
        }
        
        return textoItens;
    }
    
    /**
     * Retorna uma string com a descrição dos terroristas no local.
     * Se não há nenhum terrorista, retorna uma string vazia.
     * Se há, retorna quantos terroristas incapacitados há e pula uma linha.
     * @return 
     */
    public String getDescricaoTerroristas() {
        if (terroristas.isEmpty()) {
            return "";
        } else {
            return ("Ha "+terroristas.size()+" terroristas incapacitados no local.\n");
        }
    }
    
    /**
     * Retorna a descrição de um determinado item
     * @param item Nome do item em formato string
     * @return Descrição do item em formato String se o item for encontrado, null se não for encontrado
     */
    public String getItemDescricao(String item) {
        Item i = itens.get(item);
        if (i == null) {
            return null;
        }
        return i.getDescricao();
    }
    
    /**
     * Verifica se há terroristas no ambiente.
     * A função verifica se os terroristas do ambiente estão capacitados.
     * Se todos estiverem incapacitados, ela retorna false.
     * @return true se há terroristas, false se não há terroristas
     */
    public boolean temTerrorista() {
        boolean res = false;
        for (Terrorista t : terroristas) {
            if (t.saudavel()) {
                res = true;
            }
        }
        
        return res;
    }
    
    /**
     * Retorna quantos terroristas saudáveis existem no ambiente.
     * @return Quantidade de terroristas
     */
    public int qtdeTerroristas() {
        int res = 0;
        
        for (Terrorista t : terroristas) {
            if (t.saudavel()) {
                res++;
            }
        }
        return res;
    }
    
    /**
     * Retorna quantos terroristas, saudáveis ou incapacitados, existem no ambiente.
     * @return Quantidade de terroristas
     */
    public int qtdeTotalTerroristas() {
        return terroristas.size();
    }
    
    
    /**
     * Para cada terrorista saudável no ambiente, essa função recebe um dano
     * causado por ele e um dano que será causado a ele.
     * @param dano Vetor onde cada posição é o dano que um terrorista receberá
     * @return Soma do dano total que os terroristas causaram
     */
    public int batalharTerrorista(int dano[]) {
        int danoRecebido = 0;
        int i = 0;
        
        for (Terrorista t : terroristas) {
            if (t.saudavel()) {
                danoRecebido += t.getDanoCausado();
                t.receberDano(dano[i]);
                i++;
            }
        }
        
        return danoRecebido;
    }
    
    /**
     * Coleta um item do ambiente a partir de seu nome.
     * Se o item existir, a função retorna o item. Senão retorna null.
     * String do item é estruturada da seguinte forma:
     * [0]: Inteiro, tipo do item.
     *      0 = Item comum
     *      1 = Arma
     *      2 = Chave
     *      3 = Curativo
     * [1]: String, nome do item
     * [2]: Double, peso do item
     * [3]: String, descrição do item
     * [4]: Existe se o item não for comum. É o 4º argumento dos itens especiais.
     * @param nome Nome do item a ser coletado
     * @return Item se existir, null se não existir
     */
    public String[] coletarItem(String nome) {
        Item i = itens.get(nome);
        
        String[] s = null;
        
        if (i != null) {
            if (i.ehArma()) {
                Arma a = (Arma) i;
                s = new String[5];
                s[0] = "1";
                s[1] = a.getNome();
                s[2] = String.valueOf(a.getPeso());
                s[3] = a.getDescricao();
                s[4] = String.valueOf(a.getMunicao());
            } else if (i.ehChave()) {
                Chave c = (Chave) i;
                s = new String[5];
                s[0] = "2";
                s[1] = c.getNome();
                s[2] = String.valueOf(c.getPeso());
                s[3] = c.getDescricao();
                s[4] = String.valueOf(c.getTranca());
            } else if (i.ehCurativo()) {
                Curativo c = (Curativo) i;
                s = new String[5];
                s[0] = "3";
                s[1] = c.getNome();
                s[2] = String.valueOf(c.getPeso());
                s[3] = c.getDescricao();
                s[4] = String.valueOf(c.getPoder());
            } else {
                s = new String[4];
                s[0] = "0";
                s[1] = i.getNome();
                s[2] = String.valueOf(i.getPeso());
                s[3] = i.getDescricao();
            }
            itens.remove(nome);
        }
        return s;
    }
    
    /**
     * Revista os terroristas em busca de itens.
     * Todos os itens encontrados são colocados no ambiente.
     * A função retorna uma string com 3 possibilidades:
     * -"Nao ha terroristas no ambiente" caso não há nenhum terrorista
     * -"Nenhum item encontrado" caso nenhum dos terroristas estavam carregando itens
     * -Uma listagem de itens caso encontre algum item
     * @return 
     */
    public String revistarTerroristas() {
        if (terroristas.isEmpty()) {
            return "Nao ha terroristas no ambiente";
        }
        
        boolean revistado = false;
        String itensRevistados = "";
        for (Terrorista t : terroristas) {
            ArrayList<Item> lista = t.revistarItens();
            for (Item i : lista) {
                revistado = true;
                itensRevistados+=i.getNome()+"; ";
                itens.put(i.getNome(), i);
            }
        }
        
        if (revistado) {
            return "Itens encontrados: "+itensRevistados;
        } else {
            return "Nenhum item encontrado";
        }
    }
    
    /**
     * Retorna qual é a numeração da tranca do ambiente.
     * Tranca de valor 0 significa ambiente destrancado/sem porta
     * Outros valores significa que ambiente só pode ser acessado
     * com uma chave que possui a mesma numeração;
     * @return Valor da tranca
     */
    public int getTranca() {
        return tranca;
    }
    
    /**
     * Destranca uma porta.
     * A tranca do ambiente passa a valer 0.
     */
    public void destrancar() {
        tranca = 0;
    }
    
    /**
     * Verifica se há uma bomba no ambiente.
     * @return True se há bomba, false se não há
     */
    public boolean temBomba() {
        return bomba != null;
    }
    
    /**
     * Corta um fio da bomba.
     * Retorna um resultado de acordo com o fio cortado.
     * Numero positivo > 1: fio foi cortado corretamente
     * 1: Bomba foi desarmada
     * 0: Fio já está cortado/Não aconteceu nada
     * -1: Bomba explodiu
     * -2: tempo foi cortado pela metade
     * @param fio String, nome do fio
     * @return 
     */
    public int cortarFio(String fio) {
        if (bomba != null) {
            return bomba.cortarFio(fio);
        }
        return 0;
    }
    
    /**
     * Retorna a descrição do estado da bomba.
     * @return String com a descrição, null se não houver bomba
     */
    public String getDescricaoBomba() {
        if (bomba != null) {
            return bomba.getDescricao();
        }
        return null;
    }

}
