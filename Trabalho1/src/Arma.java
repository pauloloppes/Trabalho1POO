/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Classe que representa armas
 * @author paulo
 */
public class Arma extends Item {
    private int municao;
    
    /**
     * Cria uma arma com os dados passados. Cada arma possui um nome,
     * um peso em kg, uma descrição detalhada para quando o jogador
     * observar a arma e o numero de munição disponível.
     * @param nome Nome da arma
     * @param peso Peso da arma em kg
     * @param descricao Descrição da arma
     * @param municao Munição da arma
     */
    public Arma(String nome, double peso, String descricao,int municao) {
        super(nome, peso, descricao);
        this.municao = municao;
    }
    
    /**
     * Retorna a quantidade de munição disponivel
     * @return Municao, numero inteiro
     */
    public int getMunicao() {
        return municao;
    }
    
    /**
     * Diminui a munição disponível na arma
     * @param gasto Numero inteiro que será subtraído da munição
     */
    public void gastaMunicao(int gasto) {
        municao -= gasto;
        if (municao < 0) 
            municao = 0;
    }
    
    /**
     * Retorna se o Item é uma arma
     * @return True
     */
    @Override
    public boolean ehArma() {
        return true;
    }
    
}
