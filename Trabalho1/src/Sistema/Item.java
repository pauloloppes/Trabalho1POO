package Sistema;

/**
 * Classe que representa um item presente no ambiente.
 * @author paulo
 */
public class Item {
    private String nome;
    private double peso;
    private String descricao;
    
    /**
     * Cria um item com os dados passados. Cada item possui um nome,
     * um peso em kg e uma descrição detalhada para quando o jogador
     * observar o item.
     * @param nome Nome do item
     * @param peso Peso do item em kg
     * @param descricao Descrição do item
     */
    public Item (String nome, double peso, String descricao){
        this.nome = nome;
        this.peso = peso;
        this.descricao = descricao;
    }
    
    /**
     * Retorna o nome do item
     * @return Nome em formato string
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Retorna o peso do item em kg
     * @return Peso em formato double
     */
    public double getPeso() {
        return peso;
    }
    
    /**
     * Retorna a descrição do item
     * @return Descrição em formato String
     */
    public String getDescricao() {
        return descricao;
    }
    
    /**
     * Retorna se o item é uma arma.
     * @return False
     */
    public boolean ehArma() {
        return false;
    }
    
    /**
     * Retorna se o item é um curativo.
     * @return False
     */
    public boolean ehCurativo() {
        return false;
    }
    
    /**
     * Retorna se o item é uma chave.
     * @return False
     */
    public boolean ehChave() {
        return false;
    }
    
}
