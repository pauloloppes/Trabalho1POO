/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author paulo
 */
public class Arma extends Item {
    private int municao;
    
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
