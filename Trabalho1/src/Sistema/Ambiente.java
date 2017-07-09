package Sistema;

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
    private String nome;
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
     * @param nome O nome do ambiente.
     * @param descricao A descricao do ambiente.
     * @param descricaoLonga A descricao longa do ambiente.
     * @param tranca Número da tranca do ambiente. 0 = destrancada
     */
    public Ambiente(String nome,String descricao,String descricaoLonga,int tranca)  {
        this.nome = nome;
        this.descricao = descricao;
        this.descricaoLonga = descricaoLonga;
        this.tranca = tranca;
        this.bomba = null;
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
     * @param i Item a ser colocado no ambiente
     */
    public void ajustarItens(Item i) {
        itens.put(i.getNome(),i);
    }
    
    /**
     * Coloca um terrorista no ambiente.
     * @param t Terrorista a ser adicionado no ambiente.
     */
    public void ajustarTerroristas(Terrorista t) {
        terroristas.add(t);
    }
    
    /**
     * Retorna o nome do ambiente.
     * @return Nome do ambiente.
     */
    public String getNome(){
        return nome;
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
     * @param agenteArmado Boolean que indica se o agente está armado
     * @return Soma do dano total que os terroristas causaram
     */
    public int batalharTerrorista(int dano[],boolean agenteArmado) {
        int danoRecebido = 0;
        int i = 0;
        
        for (Terrorista t : terroristas) {
            if (t.saudavel()) {
                //Terrorista causa dano somente se estiver armado
                // ou se ambos ele e o jogador estiverem desarmados.
                if (t.temArma() ||
                        (!t.temArma() && !agenteArmado)) {
                    danoRecebido += t.getDanoCausado();
                }
                t.receberDano(dano[i]);
                i++;
            }
        }
        
        return danoRecebido;
    }
    
    /**
     * Coleta um item do ambiente a partir de seu nome.
     * Se o item existir, a função retorna o item. Senão retorna null.
     * @param nome Nome do item a ser coletado
     * @return Item se existir, null se não existir
     */
    public Item coletarItem(String nome) {
        Item i = itens.get(nome);
        if (i != null)
            itens.remove(nome);
        return i;
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
     * Cria uma nova bomba para colocar no ambiente.
     * @param fios HashMap com cada fio da bomba e seu efeito.
     */
    public void setBomba(HashMap<String,Integer> fios) {
        bomba = new Bomba(fios);
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
