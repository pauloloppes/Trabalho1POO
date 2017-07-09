package Sistema;


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
    
    /**
     * Cria um novo terrorista com a saude passada.
     * @param saude Saude do terrorista.
     */
    public Terrorista(int saude) {
        this.saude = saude;
        this.saudeMax = saude;
        arma = null;
        itens = new HashMap<>();
    }
    
    /**
     * Empunha o terrorista com uma nova arma seguindo os parâmetros passados
     * @param a Arma nova do terrorista
     */
    public void ajustarArma(Arma a) {
        arma = a;
    }
    
    /**
     * Coloca um item na lista de itens do terrorista.
     * @param i Item
     */
    public void ajustarItens(Item i) {
        itens.put(i.getNome(), i);
    }
    
    /**
     * Retorna o dano que o terrorista causa a um inimigo de acordo com sua
     * arma e munição disponível.
     * Se o terrorista não estiver empunhando uma arma ou a arma estiver sem munição,
     * o dano será entre 0 e 5. Senão, o dano será calculado de acordo com a munição
     * disponível no terrorista.
     * @return Inteiro com o dano causado
     */
    public int getDanoCausado() {
        Random rand = new Random();
        if (arma == null || arma.getMunicao() == 0) {
            return rand.nextInt(6);
        }
        
        int danoCausado = 0;
        
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
     * e os apaga.
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
    
    /**
     * Retorna se o agente está empunhando uma arma no momento
     * @return True se estiver empunhando arma, false se não
     */
    public boolean temArma() {
        return arma != null;
    }
    
}
