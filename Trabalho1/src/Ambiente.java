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
    //PERGUNTAR PRO JULIO SE É MELHOR USAR ARRAYLIST OU HASHMAP
    private HashMap<String, Item> itens;
    

    /**
     * Cria um ambiente com a "descricao" passada. Inicialmente, ele
     * nao tem saidas. "descricao" eh algo como "uma cozinha" ou
     * "um jardim aberto".
     * "descricaoLonga" eh uma descrição mais detalhada, que será
     * exibida quando o jogador observar o ambiente.
     * @param descricao A descricao do ambiente.
     * @param descricaoLonga A descricao longa do ambiente.
     */
    public Ambiente(String descricao,String descricaoLonga)  {
        this.descricao = descricao;
        this.descricaoLonga = descricaoLonga;
        saidas = new HashMap<String, Ambiente>();
        itens = new HashMap<>();
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

}
