
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
     * @param saude Saude inicial e total do agente
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
        
        //System.out.println("Agente causou "+danoCausado+" de dano");
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
     * Faz o agente passar a possuir um item.
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
     * @param i Item que o agente irá pegar
     * @return True se o agente conseguir pegar o item, false se não conseguir pegar
     */
    public boolean pegarItem(String[] i) {
        double peso = Double.parseDouble(i[2]);
        if (capacidade >= peso) {
            Item novo = null;
            if (i[0].equals("0")) {
                novo = new Item(i[1],peso,i[3]);
            } else if (i[0].equals("1")) {
                novo = new Arma(i[1],peso,i[3],Integer.parseInt(i[4]));
            } else if (i[0].equals("2")) {
                novo = new Chave(i[1],peso,i[3],Integer.parseInt(i[4]));
            } else if (i[0].equals("3")) {
                novo = new Curativo(i[1],peso,i[3],Integer.parseInt(i[4]));
            }
            if (novo != null) {
                itens.put(i[1],novo);
                capacidade -= peso;
                return true;
            }
        }
        return false;
    }
    
    /**
     * Faz o agente largar o item em um ambiente.
     * O item pode ser um dos itens que o agente carrega
     * ou a arma que o agente está empunhando.
     * Se o item não for encontrado, a função retorna null.
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
     * @param nome Nome do item
     * @return Item que o agente largou
     */
    public String[] largarItem(String nome) {
        //Item retorno;
        String retorno[] = null;
        if (arma != null && arma.getNome().equals(nome)) {
            retorno = new String[5];
            retorno[0] = "1";
            retorno[1] = arma.getNome();
            retorno[2] = String.valueOf(arma.getPeso());
            retorno[3] = arma.getDescricao();
            retorno[4] = String.valueOf(arma.getMunicao());
            arma = null;
            return retorno;
        }
        Item i = itens.get(nome);
        if (i!= null && i.getNome().equals(nome)) {
            if (i.ehArma()) {
                Arma a = (Arma) i;
                retorno = new String[5];
                retorno[0] = "1";
                retorno[1] = a.getNome();
                retorno[2] = String.valueOf(a.getPeso());
                retorno[3] = a.getDescricao();
                retorno[4] = String.valueOf(a.getMunicao());
            } if (i.ehChave()) {
                Chave c = (Chave) i;
                retorno = new String[5];
                retorno[0] = "2";
                retorno[1] = c.getNome();
                retorno[2] = String.valueOf(c.getPeso());
                retorno[3] = c.getDescricao();
                retorno[4] = String.valueOf(c.getTranca());
            } if (i.ehCurativo()) {
                Curativo c = (Curativo) i;
                retorno = new String[5];
                retorno[0] = "3";
                retorno[1] = c.getNome();
                retorno[2] = String.valueOf(c.getPeso());
                retorno[3] = c.getDescricao();
                retorno[4] = String.valueOf(c.getPoder());
            } else {
                retorno = new String[4];
                retorno[0] = "0";
                retorno[1] = i.getNome();
                retorno[2] = String.valueOf(i.getPeso());
                retorno[3] = i.getDescricao();
            }
            itens.remove(nome);
            capacidade += i.getPeso();
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
        //System.out.println("Agente recebeu "+dano+" de dano, saude = "+saude);
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
     * @return Retorna a arma do agente, ou null se ele não estiver empunhando uma arma.
     */
    public String[] largarArma() {
        if (arma != null) {
            String retorno[] = new String[5];
            retorno[0] = "0";
            retorno[1] = arma.getNome();
            retorno[2] = String.valueOf(arma.getPeso());
            retorno[3] = arma.getDescricao();
            retorno[4] = String.valueOf(arma.getMunicao());
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
    public void empunharArma(String[] a) {
        if (a[0].equals("1")) {
            Arma nova = new Arma(a[1],Double.parseDouble(a[2]),a[3],Integer.parseInt(a[4]));
            arma = nova;
        }
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
    
    /**
     * Nome do item a ser usado
     * @param nome Nome do item
     * @return True se conseguiu usar o item, false se não
     */
    public boolean usarItem (String nome) {
        Item i = itens.get(nome);
        if (i == null || !i.ehCurativo()) {
            return false;
        }
        
        Curativo c = (Curativo) i;
        
        saude += c.getPoder();
        if (saude > saudeMax) {
            saude = saudeMax;
        }
        return true;
    }
    
    /**
     * Tenta destrancar a porta com a chave que possui.
     * A função retorna true se conseguir destrancar a porta,
     * e false se não for possível.
     * @param tranca A numeração da tranca da porta
     * @return True se destrancou, false se não
     */
    public boolean desbloquearPorta(int tranca) {
        Item i = itens.get("chave");
        if (i == null || !i.ehChave()) {
            return false;
        }
        
        Chave c = (Chave) i;
        return c.getTranca() == tranca;
        
    }
    
    /**
     * Verifica se o agente possui um alicate
     * para desarmar a bomba.
     * @return True se agente possui alicate, false se não possui
     */
    public boolean possuiAlicate() {
        return itens.get("alicate") != null;
    }
}
