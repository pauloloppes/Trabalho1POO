/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistema;

/**
 *
 * @author paulo
 */
public class AmbienteInicialNaoDefinidoException extends RuntimeException {
    
    /**
     * Retorna mensagem avisando que o ambiente inicial do jogo
     * não foi definido.
     * @return Mensagem em String
     */
    @Override
    public String getMessage() {
        return "Ambiente inicial não foi definido.\n"
                + "Verifique se o arquivo ambientes.txt está com problemas.";
    }
    
}
