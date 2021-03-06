/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grafico;

import Sistema.Jogo;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Classe que monta a janela do jogo durante a partida.
 * @author paulo
 */
public class TelaJogo {
    
    private JFrame janela;
    private JTextArea areaTexto;
    private JButton botaoExecutar;
    private JTextField campoComando;
    private JScrollPane scrollAreaTexto;
    private JLabel fotoAmbiente;
    private JPanel painelNavegacao;
    private JLabel labelNavegacao;
    private JScrollPane scrollNavegacao;
    
    private Jogo jogo;
    
    /**
     * Cria a janela recebendo o tempo limite do jogo.
     * @param tempoLimite Tempo em minutos
     */
    public TelaJogo(int tempoLimite) {
        try {
            jogo = new Jogo(tempoLimite);
            construirJanela();
            montarJanela();
            atualizarFotoAmbiente();
            atualizarBotoesNavegacao();
            exibir();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "O programa está tentando acessar um arquivo inexistente.\n"
                    + e.getLocalizedMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
        } 
        
    }
    
    /**
     * Constrói a janela do jogo e chama o método para criar
     * os componentes da janela.
     */
    private void construirJanela() {
        janela = new JFrame("Counter Strike: Lavras");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        criarComponentes();
        
    }
    
    /**
     * Cria todos os componentes da janela.
     */
    private void criarComponentes() {
        areaTexto = new JTextArea(jogo.imprimirBoasVindas());
        areaTexto.setEditable(false);
        botaoExecutar = new JButton("Executar");
        campoComando = new JTextField(20);
        scrollAreaTexto = new JScrollPane(areaTexto);
        fotoAmbiente = new JLabel();
        painelNavegacao = new JPanel();
        labelNavegacao = new JLabel("Saídas:");
        scrollNavegacao = new JScrollPane(painelNavegacao);
        
        
        botaoExecutar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarComando();
            }
        });
        
        campoComando.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarComando();
            }
        });
        
    }
    
    /**
     * Desenha a janela com todos os seus componentes.
     */
    private void montarJanela() {
        janela.setSize(640, 640);
        janela.setLayout(new BorderLayout());
        
        fotoAmbiente.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        
        painelPrincipal.add(fotoAmbiente);
        painelPrincipal.add(scrollAreaTexto);
        janela.add(painelPrincipal,BorderLayout.CENTER);
        
        JPanel painelSul = new JPanel();
        painelSul.setLayout(new BoxLayout(painelSul,BoxLayout.Y_AXIS));
        
        JPanel painelComandos = new JPanel();
        painelComandos.setLayout(new FlowLayout());
        painelComandos.add(campoComando);
        painelComandos.add(botaoExecutar);
        
        painelSul.add(scrollNavegacao);
        painelSul.add(painelComandos);
        janela.add(painelSul, BorderLayout.SOUTH);
    }
    
    /**
     * Exibe a janela.
     */
    private void exibir() {
        janela.setVisible(true);
        campoComando.requestFocusInWindow();
    }
    
    /**
     * Executa um comando digitado no campo de texto.
     * Se o usuário digitar um comando "ir", a área de texto com a descrição
     * do jogo é limpa. Se o usuário digitar "sair", a janela é fechada
     * e o jogo terminado.
     */
    private void executarComando() {
        
        try {
            Scanner tokenizer = new Scanner(campoComando.getText());
            String texto = "\n";
            if(tokenizer.hasNext()) {
                String teste = tokenizer.next();
                if (teste.equals("ir")) {
                    areaTexto.setText("");
                } else if (teste.equals("sair")) {
                    janela.dispose();
                } else {
                    texto = areaTexto.getText();
                }
            } else {
                texto = areaTexto.getText();
            }

            texto += jogo.processarComando(campoComando.getText());
            areaTexto.setText(texto);
            campoComando.setText("");
            atualizarFotoAmbiente();
            atualizarBotoesNavegacao();
            
            if (jogo.getAcabouJogo()) {
                campoComando.setEnabled(false);
                botaoExecutar.setEnabled(false);
                painelNavegacao.setEnabled(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(janela, e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    /**
     * Executa um comando.
     */
    private void executarComando(String linha) {
        
        try {
            Scanner tokenizer = new Scanner(linha);
            String texto = "\n";
            if(tokenizer.hasNext()) {
                String teste = tokenizer.next();
                if (teste.equals("ir")) {
                    areaTexto.setText("");
                } else if (teste.equals("sair")) {
                    janela.dispose();
                } else {
                    texto = areaTexto.getText();
                }
            } else {
                texto = areaTexto.getText();
            }

            texto += jogo.processarComando(linha);
            areaTexto.setText(texto);
            campoComando.setText("");
            atualizarFotoAmbiente();
            atualizarBotoesNavegacao();
                    
            if (jogo.getAcabouJogo()) {
                campoComando.setEnabled(false);
                botaoExecutar.setEnabled(false);
                atualizarBotoesNavegacao();
            }
        } catch (Exception e) {
            System.out.println(e.getClass());
            JOptionPane.showMessageDialog(janela, e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    /**
     * Atualiza a foto do ambiente para o ambiente atual.
     */
    private void atualizarFotoAmbiente() {
        ImageIcon foto = new ImageIcon("ambients/img/"+jogo.getFotoAmbienteAtual());
        fotoAmbiente.setIcon(foto);
    }
    
    /**
     * Atualiza os botões das saídas para o ambiente atual.
     */
    private void atualizarBotoesNavegacao() {
        painelNavegacao.removeAll();
        painelNavegacao.setLayout(new FlowLayout());
        painelNavegacao.add(labelNavegacao);
        
        String[] saidas = jogo.getSaidasAmbienteAtual();
        for (String s : saidas) {
            JButton botao = new JButton(s);
            
            if (jogo.getAcabouJogo()) {
                botao.setEnabled(false);
            } else {
                botao.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        executarComando("ir "+botao.getText());
                    }
                });
            }

            painelNavegacao.add(botao);
        }
        
        painelNavegacao.revalidate();
        painelNavegacao.repaint();
    }
    
}
