
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



/**
 * Classe que representa um terrorista em algum ambiente.
 * @author paulo
 */
public class Terrorista {
    private int saude;
    private int saudeMax;
    private HashMap<String,Item> itens;
    private Arma arma;
    
    public Terrorista(int saude) {
        this.saude = saude;
        this.saudeMax = saude;
        arma = null;
        itens = new HashMap<>();
    }
    
    /**
     * Empunha o terrorista com uma nova arma seguindo os parâmetros passados
     * @param nome Nome da arma
     * @param peso Peso da arma, número real
     * @param descricao Descrição da arma
     * @param municao Quantidade de munição, número inteiro
     */
    public void ajustarArma(String nome, double peso, String descricao,int municao) {
        arma = new Arma(nome,peso,descricao,municao);
    }
    
    /**
     * Coloca um item na lista de itens do terrorista.
     * @param nome Nome do item
     * @param peso Peso do item em kg
     * @param descricao Descrição do item
     */
    public void ajustarItens(String nome, double peso, String descricao) {
        Item i = new Item(nome,peso,descricao);
        itens.put(nome, i);
    }
    
    /**
     * Retorna o dano que o terrorista causa a um inimigo de acordo com sua
     * arma e munição disponível.
     * Se o terrorista não estiver empunhando uma arma ou a arma estiver sem munição,
     * o dano será 0. Senão, o dano será calculado de acordo com a munição
     * disponível no terrorista.
     * @return Inteiro com o dano causado
     */
    public int getDanoCausado() {
        if (arma == null) {
            return 0;
        }
        
        int danoCausado = 0;
        Random rand = new Random();
        int municao = arma.getMunicao();
        int gasto = 1+rand.nextInt(4);
        
        if (municao == 0) {
            return 0;
        } else if (municao >= gasto) {
            danoCausado += 10 + rand.nextInt(11);
            arma.gastaMunicao(gasto);
        } else {
            danoCausado += (3*municao) + rand.nextInt(11);
            arma.gastaMunicao(municao);
        }
        
        System.out.println("Terror causou "+danoCausado+" de dano");
        return danoCausado;
    }
    
    /**
     * Diminui a saúde do terrorista pelo parâmetro passado
     * @param dano Número inteiro com o dano que o terrorista recebe
     */
    public void receberDano(int dano) {
        saude -= dano;
        if (saude < 0) {
            saude = 0;
        }
    }
    
    /**
     * Retorna se o terrorista está saudável ou incapacitado
     * @return True se o terrorista está saudável, false se está incapacitado.
     */
    public boolean saudavel() {
        return ((saude/(double)saudeMax) >= 0.3);
    }
    
    /**
     * Retorna uma lista com todos os itens presentes no terrorista,
     * e os apanga.
     * @return Lista de itens
     */
    public ArrayList<Item> revistarItens() {
        ArrayList<Item> retorno = new ArrayList<>();
        
        if (arma != null) {
            retorno.add(arma);
            arma = null;
        }
        
        for (String s : itens.keySet()) {
            retorno.add(itens.get(s));
        }
        
        itens.clear();
        
        return retorno;
    }
    
}
