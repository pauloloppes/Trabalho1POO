/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Classe que representa itens que curam
 * @author paulo
 */
public class Curativo extends Item {
    private int poder;
    
    /**
     * Cria um curativo com os dados passados. Cada curativo possui um nome,
     * um peso em kg, uma descrição detalhada para quando o jogador
     * observar o curativo e o poder de cura.
     * @param nome Nome do curativo
     * @param peso Peso do curativo em kg
     * @param descricao Descrição do curativo
     * @param poder Poder de cura do curativo
     */
    public Curativo(String nome, double peso, String descricao,int poder) {
        super(nome, peso, descricao);
        this.poder = poder;
    }
    
    /**
     * Retorna se o Item é um curativo
     * @return True
     */
    @Override
    public boolean ehCurativo() {
        return true;
    }
    
    /**
     * Retorna o poder curativo do item
     * @return Numero inteiro com o poder
     */
    public int getPoder() {
        return poder;
    }
}
