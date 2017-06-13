/**
 * Classe principal do Programa. Apenas inicia o jogo.
 * 
 * @author Julio
 */
public class Programa {

    /**
     * Método principal. Cria um objeto da classe Jogo e inicia o jogo.
     * 
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        Jogo jogo = new Jogo(60);
        jogo.jogar();
    }

}
