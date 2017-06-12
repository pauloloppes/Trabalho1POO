/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Classe que representa chaves
 * @author paulo
 */
public class Chave extends Item{
    private int tranca;
    
    public Chave(String nome, double peso, String descricao,int tranca) {
        super(nome, peso, descricao);
        this.tranca = tranca;
    }
    
    /**
     * Retorna se o Item é uma chave
     * @return True
     */
    @Override
    public boolean ehChave() {
        return true;
    }
    
    /**
     * Retorna qual é a numeração da tranca que a chave abre
     * @return Numero inteiro com a tranca
     */
    public int getTranca() {
        return tranca;
    }
}
