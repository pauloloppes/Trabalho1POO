/**
 * Classe que representa o jogador, um agente do esquadrão antibomba.
 * @author paulo
 */
public class Agente {
    private double capacidade;
    
    /**
     * Cria um agente com os dados passados.
     * Capacidade é quantos kg o agente consegue carregar.
     * @param capacidade Capacidade que o agente consegue carregar em kg
     */
    public Agente(double capacidade) {
        this.capacidade = capacidade;
    }
    
    /**
     * Retorna quantos kg o agente consegue carregar.
     * @return capacidade em kg
     */
    public double getCapacidade() {
        return capacidade;
    }
    
}
