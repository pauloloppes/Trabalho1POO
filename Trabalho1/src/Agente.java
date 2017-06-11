
import java.util.HashMap;
import java.util.Random;

/**
 * Classe que representa o jogador, um agente do esquadrão antibomba.
 * @author paulo
 */
public class Agente {
    private double capacidade;
    private int saude;
    private int saudeMax;
    private HashMap<String, Item> itens;
    private Arma arma;
    
    /**
     * Cria um agente com os dados passados.
     * Capacidade é quantos kg o agente consegue carregar.
     * @param capacidade Capacidade que o agente consegue carregar em kg
     */
    public Agente(double capacidade,int saude) {
        this.capacidade = capacidade;
        this.saude = saude;
        this.saudeMax = saude;
        itens = new HashMap<>();
        itens.put("alicate", new Item("alicate",0.5,"Um alicate."));
        arma = new Arma("pistola",0.8,"Uma pistola comum.",6);
    }
    
    /**
     * Retorna quantos kg o agente consegue carregar.
     * @return capacidade em kg
     */
    public double getCapacidade() {
        return capacidade;
    }
    
    /**
     * Retorna uma string com todos os itens que existem na mochila.
     * @return String com os itens existentes no Agente separados por ;
     */
    public String getItens(){
        String textoItens = "";
        for (String item : itens.keySet()) {
            textoItens = textoItens + item + "; ";
        }
        
        return textoItens;
    }
    
    /**
     * Retorna o valor da saúde atual do agente.
     * @return Valor da saúde
     */
    public int getSaude() {
        return saude;
    }
    
    /**
     * Retorna o dano que o agente causa a um inimigo de acordo com sua saúde,
     * arma e munição disponível.
     * Se o agente não estiver empunhando uma arma ou a arma estiver sem munição,
     * o dano será 0. Senão, o dano será calculado de acordo com a saúde e munição
     * disponível no agente.
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
            danoCausado += 30 + rand.nextInt(31);
            arma.gastaMunicao(gasto);
        } else {
            danoCausado += (10*municao) + rand.nextInt(21);
            arma.gastaMunicao(municao);
        }
        
        double saudePerc = saude/saudeMax;
        if (saudePerc >= 0.9) {
            danoCausado += 40;
        } else if (saudePerc >= 0.6) {
            danoCausado += 25;
        } else if (saudePerc >= 0.2) {
            danoCausado += 10;
        }
        
        System.out.println("Agente causou "+danoCausado+" de dano");
        return danoCausado;
    }
    
    /**
     * Retorna um item, se o agente o possuir.
     * Retorna um item caso o item exista,
     * ou null caso o item não exista.
     * @param nome String com o nome do item o qual você deseja retornar
     * @return Item se existe, ou null se não existe
     */
    public Item getItem(String nome) {
        return itens.get(nome);
    }
    
    /**
     * Faz o agente passar a possuir um item.
     * @param i Item que o agente irá pegar
     * @return True se o agente conseguir pegar o item, false se não conseguir pegar
     */
    public boolean pegarItem(Item i) {
        double peso = i.getPeso();
        if (capacidade >= peso) {
            itens.put(i.getNome(),i);
            capacidade -= peso;
            return true;
        }
        return false;
    }
    
    /**
     * Faz o agente largar o item em um ambiente.
     * O item pode ser um dos itens que o agente carrega
     * ou a arma que o agente está empunhando.
     * Se o item não for encontrado, a função retorna null.
     * @param nome Nome do item
     * @return Item que o agente largou
     */
    public Item largarItem(String nome) {
        Item retorno;
        if (arma.getNome().equals(nome)) {
            retorno = arma;
            arma = null;
            return retorno;
        }
        retorno = itens.get(nome);
        if (retorno!= null && retorno.getNome().equals(nome)) {
            itens.remove(nome);
            capacidade += retorno.getPeso();
            return retorno;
        }
        return retorno;
    }
    
    /**
     * Função que causa dano ao agente
     * @param dano Dano que o agente receberá
     */
    public void receberDano(int dano) {
        saude -= dano;
        if (saude < 0) {
            saude = 0;
        }
        System.out.println("Agente recebeu "+dano+" de dano, saude = "+saude);
    }
    
    /**
     * Retorna se o agente está saudável ou incapacitado.
     * O agente está incapacitado se sua saúde estiver abaixo de 20%.
     * @return True se estiver saudável, false se estiver incapacitado
     */
    public boolean saudavel() {
        return (saude/(double)saudeMax >= 0.2);
    }
    
    /**
     * Faz o agente largar a arma que está empunhando atualmente.
     * @return Retorna a arma do agente, ou null se ele não estiver empunhando uma arma.
     */
    public Item largarArma() {
        if (arma != null) {
            Item retorno = arma;
            arma = null;
            return retorno;
        }
        return null;
    }
    
    /**
     * Retorna se o agente está empunhando uma arma no momento
     * @return True se estiver empunhando arma, false se não
     */
    public boolean temArma() {
        return arma != null;
    }
    
    /**
     * Faz o agente empunhar uma arma nova
     * @param a Arma que será empunhada
     */
    public void empunharArma(Item a) {
        if (a.ehArma())
            arma = (Arma) a;
    }
    
    /**
     * Retorna a quantidade de munição da arma atual
     * @return Número de munição
     */
    public int getMunicao() {
        if (arma != null) {
            return arma.getMunicao();
        }
        return 0;
    }
}
