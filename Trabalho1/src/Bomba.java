
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author paulo
 */
public class Bomba {
    private HashMap<String,Integer> fios;
    private int ordem;
    
    /**
     * Cria uma bomba com os parametros passados.
     * Fios é um HashMap com os fios existentes na bomba.
     * Há uma ordem para os fios serem cortados pra bomba não detonar.
     * Essa ordem é denotada pelo inteiro associado ao nome da cor do fio,
     * começando do número de fios existentes na ordem,
     * e subtraindo 1 a cada próximo fio, até o último ser 1.
     * Fios que não fazem parte da ordem recebem números negativos:
     * Se for -1 a bomba explode instantaneamente ao ser cortado.
     * Se for -2 o tempo corta pela metade.
     * Se for 0, o fio já está cortado
     * 
     * Exemplo: se a ordem for amarelo, marrom, rosa e azul.
     * amarelo = 4
     * marrom = 3
     * rosa = 2
     * azul = 1
     * vermelho = -1
     * preto = -2
     * verde = -1
     * 
     * @param fios HashMap com os fios, chave é String com a cor, valor é a ordem de corte
     */
    public Bomba(HashMap<String,Integer> fios) {
        this.fios = fios;
        boolean primeiro = true;
        int maior = 0;
        for (String s : fios.keySet()) {
            if (primeiro) {
                maior = fios.get(s);
                primeiro = false;
            }
            else {
                if (fios.get(s) > maior) {
                    maior = fios.get(s);
                }
            }
        }
        ordem = maior;
        
    }
    
    /**
     * Corta um fio
     * @param fio Cor do fio
     * @return Resultado do corte
     */
    public int cortarFio(String fio) {
        int ordemFio = fios.get(fio);
        
        if (ordemFio > 0 && ordemFio < ordem) {
            //CORTOU FIO ERRADO
            return -1;
        }
        
        //CORTA O FIO
        fios.put(fio, 0);
        if (ordemFio == ordem)
            ordem--;
        //RETORNA O RESULTADO
        return ordemFio;
    }
    
    /**
     * Retorna uma descrição da bomba com o estado dos fios.
     * @return Descrição
     */
    public String getDescricao() {
        String resposta = "A bomba possui "+fios.size()+" fios.\n";
        
        for (String s : fios.keySet()) {
            if (fios.get(s) == 0) {
                resposta += "Fio "+s+" cortado; ";
            } else {
                resposta += "Fio "+s+"; ";
            }
        }
        
        return resposta;
    }
    
}
