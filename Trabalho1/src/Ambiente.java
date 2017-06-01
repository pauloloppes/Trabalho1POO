import java.util.HashMap;

/**
 * Classe Ambiente - um ambiente em um jogo adventure.
 *
 * Esta classe eh parte da aplicacao "World of Zuul".
 * "World of Zuul" eh um jogo de aventura muito simples, baseado em texto.  
 *
 * Um "Ambiente" representa uma localizacao no cenario do jogo. Ele eh
 * conectado aos outros ambientes atraves de saidas. As saidas sao
 * nomeadas como norte, sul, leste e oeste. Para cada direcao, o ambiente
 * guarda uma referencia para o ambiente vizinho, ou null se nao ha
 * saida naquela direcao.
 * 
 * @author  Michael Kölling and David J. Barnes (traduzido por Julio Cesar Alves)
 * @version 2011.07.31 (2017.05.16)
 */
public class Ambiente  {
    private String descricao;
    private HashMap<String, Ambiente> saidas;
    

    /**
     * Cria um ambiente com a "descricao" passada. Inicialmente, ele
     * nao tem saidas. "descricao" eh algo como "uma cozinha" ou
     * "
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "um jardim aberto".
     * @param descricao A descricao do ambiente.
     */
    public Ambiente(String descricao)  {
        this.descricao = descricao;
        saidas = new HashMap<String, Ambiente>();
    }

    /**
     * Define as saidas do ambiente. Cada direcao ou leva a um
     * outro ambiente ou eh null (nenhuma saida para la).
     * @param direcao Direção do ambiente
     * @param ambiente Ambiente
     */
    public void ajustarSaidas(String direcao, Ambiente ambiente)  {
        saidas.put(direcao,ambiente);
    }

    /**
     * @return A descricao do ambiente.
     */
    public String getDescricao() {
        return descricao;
    }
    
    /**
     * Retorna se a saída existe no ambiente.
     * Retorna um Ambiente de destino caso a saída exista,
     * ou null caso a saída não exista.
     * @param direcao String com o nome da saída da qual você deseja retornar
     * @return Ambiente se existe, ou null se não existe
     */
    public Ambiente getAmbiente(String direcao) {
        return saidas.get(direcao);
    }
    
    /**
     * Retorna uma string com todas as direções que existem no Ambiente.
     * @return String com as saídas existentes no Ambiente separados por espaço
     */
    public String getSaidas(){
        String textoSaidas = "";
        for (String direcao : saidas.keySet()) {
            textoSaidas = textoSaidas + direcao + " ";
        }
        return textoSaidas;
    }

}
