/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.FuriniSolutions.view;

import br.com.FuriniSolutions.bean.Cliente;
import br.com.FuriniSolutions.bean.ItemNota;
import br.com.FuriniSolutions.bean.NotaFiscal;
import br.com.FuriniSolutions.bean.Produto;
import br.com.FuriniSolutions.dao.ClienteDAO;
import br.com.FuriniSolutions.dao.NotaFiscalDAO;
import br.com.FuriniSolutions.dao.ProdutoDAO;
import br.com.FuriniSolutions.model.ItemNotaTableModel;
import br.com.FuriniSolutions.util.ConnectionsFactory;
import br.com.FuriniSolutions.util.DataUtil;
import br.com.FuriniSolutions.util.Observer;
import br.com.FuriniSolutions.util.Subject;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author lucas
 */
public class NotaCreateView extends javax.swing.JFrame implements Subject {

    private List<Observer> observers = new ArrayList<>();
    private List<Produto> produtos = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();
    private Produto produtoSelecionado = new Produto();
    private Cliente clienteSelecionado = new Cliente();
    private ItemNota itemSelecionado = null;
    private int rowIndexUpdate;
    private DecimalFormat formatadorDecimal = new DecimalFormat("#,##0.00");
    private ItemNotaTableModel tablemodel = new ItemNotaTableModel();

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        int index = observers.indexOf(observer);

        if (index > -1) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers(NotaFiscal notaNova) {
        for (Observer o : observers) {
            o.update(notaNova);
        }
    }

    private enum OperationType {
        SAVE, EDIT
    };
    private OperationType type = OperationType.SAVE;

    public NotaCreateView() {
        initComponents();
        tableProdutos.setModel(tablemodel);

        // Campos não editáveis
        jtfIdProduto.setBackground(Color.LIGHT_GRAY);
        jtfIdProduto.setForeground(Color.DARK_GRAY);
        jtfIdProduto.setEditable(false);

        jtfValor.setBackground(Color.LIGHT_GRAY);
        jtfValor.setForeground(Color.DARK_GRAY);
        jtfValor.setEditable(false);

        jtfTotal.setBackground(Color.LIGHT_GRAY);
        jtfTotal.setForeground(Color.DARK_GRAY);
        jtfTotal.setEditable(false);

        jtfIDCliente.setBackground(Color.LIGHT_GRAY);
        jtfIDCliente.setForeground(Color.DARK_GRAY);
        jtfIDCliente.setEditable(false);
        
        jtfTotalNota.setBackground(Color.LIGHT_GRAY);
        jtfTotalNota.setForeground(Color.DARK_GRAY);
        jtfTotalNota.setEditable(false);

        atualizarEstadoBotoes(true);

        jtfINomeCliente.requestFocus();

        //adicionando a lista ao panel
        menuProdutos.add(panelPesquisaProdutos);
        menuClientes.add(panelPesquisaClientes);

        // Adicionar o listener para o campo de descrição do produto
        jtfDescricaoProduto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                buscarProdutos(jtfDescricaoProduto.getText().trim());  // Chama a função de busca
            }
        });

        // Adicionar mouse listener para selecionar o item na lista do produto
        jltProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {  // Seleção com duplo clique
                    produtoSelecionado = produtos.get(jltProdutos.getSelectedIndex());
                    preencherCamposProduto(produtoSelecionado);  // Preenche os campos com o produto selecionado
                    menuProdutos.setVisible(false);  // Esconde o menu após a seleção
                }
            }
        });

        //adicionando listener na quantidade do produto para permitir apenas numeros
        jtfQuantidade.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                // Permite números, backspace e delete
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    evt.consume(); // Ignora o evento de tecla se não for número ou backspace/delete
                }
            }
        });

        // Adicionar mouse listener para selecionar o item na lista do cliente
        jltClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {  // Seleção com duplo clique
                    clienteSelecionado = clientes.get(jltClientes.getSelectedIndex());
                    preencherCamposCliente(clienteSelecionado);  // Preenche os campos com o produto selecionado
                    menuClientes.setVisible(false);  // Esconde o menu após a seleção
                }
            }
        });

        //listener para o cliente, caso apertar alguma tecla
        jtfINomeCliente.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                buscarClientes(jtfINomeCliente.getText().trim());  // Chama a função de busca
            }
        });

        //quando clicar na table de itens nota seleciona
        tableProdutos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tableProdutos.getSelectedRow();
                ItemNota itemNota = tablemodel.getItemNota(linha);

                itemSelecionado = itemNota;
                rowIndexUpdate = linha;

                atualizarEstadoBotoes(false);

            }

        });

    }

    private void atualizarEstadoBotoes(boolean estadoSalvarCancelar) {
        jbtnAdicionar.setEnabled(estadoSalvarCancelar);
        jbtnCancelar.setEnabled(estadoSalvarCancelar);

        jbtnExcluir.setEnabled(!estadoSalvarCancelar);
        jbtnEditar.setEnabled(!estadoSalvarCancelar);
    }

    private void buscarProdutos(String search) {
        try ( Connection connection = ConnectionsFactory.createConnetionToMySQL()) {
            ProdutoDAO produtoDAO = new ProdutoDAO(connection);

            // Se o campo de busca estiver vazio, busca todos os produtos
            if (search.trim().isEmpty()) {
                produtos = produtoDAO.findAll();  // Busca todos os produtos
            } else {
                produtos = produtoDAO.buscarPorDescricao(search);  // Filtra pela descrição
            }

            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (int i = 0; i < produtos.size(); i++) {
                listModel.addElement(produtos.get(i).getDescricao());
            }
            jltProdutos.setModel(listModel);

            menuProdutos.show(jtfDescricaoProduto, 0, jtfDescricaoProduto.getHeight());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar produtos: " + e.getMessage());
        }
    }

    private void preencherCamposProduto(Produto produto) {
        jtfIdProduto.setText(String.valueOf(produto.getId()));
        jtfValor.setText(String.valueOf(formatadorDecimal.format(produto.getValor())));
        jtfDescricaoProduto.setText(String.valueOf(produto.getDescricao()));
        jtfTotal.setText(formatadorDecimal.format(produto.getValor()));
        jtfQuantidade.requestFocus();

    }

    private void preencherCamposProduto(ItemNota item) {
        jtfIdProduto.setText(String.valueOf(item.getProduto().getId()));
        jtfValor.setText(String.valueOf(formatadorDecimal.format(item.getValorItem())));
        jtfDescricaoProduto.setText(String.valueOf(item.getProduto().getDescricao()));
        jtfTotal.setText(formatadorDecimal.format(item.getValorItem()));
        jtfQuantidade.requestFocus();

    }

    private void buscarClientes(String search) {
        try ( Connection connection = ConnectionsFactory.createConnetionToMySQL()) {
            ClienteDAO clienteDAO = new ClienteDAO(connection);

            // Se a pesquisa estiver vazia, busca todos os clientes
            if (search.trim().isEmpty()) {
                clientes = clienteDAO.findAll();  // Busca todos os clientes
            } else {
                clientes = clienteDAO.buscarPorDescricao(search);  // Filtra os clientes pela descrição
            }

            // Atualizando o JList com os clientes encontrados
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (int i = 0; i < clientes.size(); i++) {
                listModel.addElement(clientes.get(i).getNome());
            }
            jltClientes.setModel(listModel);

            // Exibir o menu popup
            menuClientes.show(jtfINomeCliente, 0, jtfINomeCliente.getHeight());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar clientes: " + e.getMessage());
        }
    }

    private void preencherCamposCliente(Cliente cliente) {
        jtfIDCliente.setText(String.valueOf(cliente.getId()));
        jtfINomeCliente.setText(cliente.getNome());
        jtfDescricaoProduto.requestFocus();

    }

    private void limparCamposProduto() {
        jtfIdProduto.setText("");
        jtfDescricaoProduto.setText("");
        jtfQuantidade.setText("");
        jtfTotal.setText("");
        jtfValor.setText("");
        jtfTotalNota.setText("");
    }

    private void atualizarTotalNota() {
        double totalNota = 0.0;

        for (int i = 0; i < tablemodel.getRowCount(); i++) {
            ItemNota itemNota = tablemodel.getItemNota(i);
            totalNota += itemNota.getValorItem() * itemNota.getQuantidade();
        }

        jtfTotalNota.setText("R$ " + formatadorDecimal.format(totalNota));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuProdutos = new javax.swing.JPopupMenu();
        panelPesquisaProdutos = new javax.swing.JPanel();
        jspProdutos = new javax.swing.JScrollPane();
        jltProdutos = new javax.swing.JList<>();
        panelPesquisaClientes = new javax.swing.JPanel();
        jspClientes = new javax.swing.JScrollPane();
        jltClientes = new javax.swing.JList<>();
        menuClientes = new javax.swing.JPopupMenu();
        jLayerProduto = new javax.swing.JLayeredPane();
        jbtnCancelar = new javax.swing.JButton();
        jbtnAdicionar = new javax.swing.JButton();
        jtfIdProduto = new javax.swing.JTextField();
        lblID = new javax.swing.JLabel();
        lblDescricao = new javax.swing.JLabel();
        lblValor1 = new javax.swing.JLabel();
        jtfValor = new javax.swing.JTextField();
        jtfQuantidade = new javax.swing.JTextField();
        lblValor = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jtfTotal = new javax.swing.JTextField();
        jtfDescricaoProduto = new javax.swing.JTextField();
        jbtnNovoProduto = new javax.swing.JButton();
        jLayerCliente = new javax.swing.JLayeredPane();
        jLabel1 = new javax.swing.JLabel();
        jtfIDCliente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtfINomeCliente = new javax.swing.JTextField();
        jbtnNovoCliente = new javax.swing.JButton();
        jLayerTable = new javax.swing.JLayeredPane();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableProdutos = new javax.swing.JTable();
        btnSalvar = new javax.swing.JButton();
        jbtnEditar = new javax.swing.JButton();
        jbtnExcluir = new javax.swing.JButton();
        jtfTotalNota = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();

        menuProdutos.setBorder(null);
        menuProdutos.setFocusable(false);

        jspProdutos.setBorder(null);
        jspProdutos.setAutoscrolls(true);

        jspProdutos.setViewportView(jltProdutos);

        javax.swing.GroupLayout panelPesquisaProdutosLayout = new javax.swing.GroupLayout(panelPesquisaProdutos);
        panelPesquisaProdutos.setLayout(panelPesquisaProdutosLayout);
        panelPesquisaProdutosLayout.setHorizontalGroup(
            panelPesquisaProdutosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
            .addGroup(panelPesquisaProdutosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jspProdutos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE))
        );
        panelPesquisaProdutosLayout.setVerticalGroup(
            panelPesquisaProdutosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 93, Short.MAX_VALUE)
            .addGroup(panelPesquisaProdutosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jspProdutos, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
        );

        panelPesquisaClientes.setFocusable(false);

        jspClientes.setFocusable(false);

        jspClientes.setViewportView(jltClientes);

        javax.swing.GroupLayout panelPesquisaClientesLayout = new javax.swing.GroupLayout(panelPesquisaClientes);
        panelPesquisaClientes.setLayout(panelPesquisaClientesLayout);
        panelPesquisaClientesLayout.setHorizontalGroup(
            panelPesquisaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 269, Short.MAX_VALUE)
            .addGroup(panelPesquisaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jspClientes, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
        );
        panelPesquisaClientesLayout.setVerticalGroup(
            panelPesquisaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
            .addGroup(panelPesquisaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jspClientes, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
        );

        menuClientes.setFocusable(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastrando nota fiscal");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusTraversalPolicyProvider(true);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLayerProduto.setBorder(javax.swing.BorderFactory.createTitledBorder("Produto"));

        jbtnCancelar.setLabel("Cancelar");
        jbtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelarActionPerformed(evt);
            }
        });

        jbtnAdicionar.setText("Adicionar");
        jbtnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAdicionarActionPerformed(evt);
            }
        });

        jtfIdProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfIdProdutoActionPerformed(evt);
            }
        });

        lblID.setText("ID");

        lblDescricao.setText("Descrição");

        lblValor1.setText("Quantidade");

        jtfValor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfValorActionPerformed(evt);
            }
        });

        jtfQuantidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfQuantidadeActionPerformed(evt);
            }
        });
        jtfQuantidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfQuantidadeKeyReleased(evt);
            }
        });

        lblValor.setText("Valor (R$)");

        lblTotal.setText("Total (R$)");

        jtfTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfTotalActionPerformed(evt);
            }
        });

        jtfDescricaoProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtfDescricaoProdutoMouseClicked(evt);
            }
        });
        jtfDescricaoProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDescricaoProdutoActionPerformed(evt);
            }
        });
        jtfDescricaoProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfDescricaoProdutoKeyReleased(evt);
            }
        });

        jbtnNovoProduto.setText("Novo Produto");
        jbtnNovoProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNovoProdutoActionPerformed(evt);
            }
        });

        jLayerProduto.setLayer(jbtnCancelar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerProduto.setLayer(jbtnAdicionar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerProduto.setLayer(jtfIdProduto, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerProduto.setLayer(lblID, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerProduto.setLayer(lblDescricao, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerProduto.setLayer(lblValor1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerProduto.setLayer(jtfValor, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerProduto.setLayer(jtfQuantidade, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerProduto.setLayer(lblValor, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerProduto.setLayer(lblTotal, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerProduto.setLayer(jtfTotal, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerProduto.setLayer(jtfDescricaoProduto, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerProduto.setLayer(jbtnNovoProduto, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayerProdutoLayout = new javax.swing.GroupLayout(jLayerProduto);
        jLayerProduto.setLayout(jLayerProdutoLayout);
        jLayerProdutoLayout.setHorizontalGroup(
            jLayerProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayerProdutoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayerProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayerProdutoLayout.createSequentialGroup()
                        .addComponent(lblValor1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfQuantidade, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(lblValor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfValor, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTotal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jLayerProdutoLayout.createSequentialGroup()
                        .addComponent(lblID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfIdProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblDescricao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfDescricaoProduto))
                    .addGroup(jLayerProdutoLayout.createSequentialGroup()
                        .addComponent(jbtnNovoProduto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtnAdicionar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnCancelar)))
                .addContainerGap())
        );
        jLayerProdutoLayout.setVerticalGroup(
            jLayerProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayerProdutoLayout.createSequentialGroup()
                .addGroup(jLayerProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfIdProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblID)
                    .addComponent(lblDescricao)
                    .addComponent(jtfDescricaoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jLayerProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblValor)
                    .addComponent(jtfValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotal)
                    .addComponent(jtfTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblValor1)
                    .addComponent(jtfQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jLayerProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnAdicionar)
                    .addComponent(jbtnCancelar)
                    .addComponent(jbtnNovoProduto))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayerCliente.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        jLabel1.setText("ID");

        jLabel2.setText("Nome");

        jtfINomeCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtfINomeClienteMouseClicked(evt);
            }
        });
        jtfINomeCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfINomeClienteActionPerformed(evt);
            }
        });
        jtfINomeCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfINomeClienteKeyReleased(evt);
            }
        });

        jbtnNovoCliente.setText("+");
        jbtnNovoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNovoClienteActionPerformed(evt);
            }
        });

        jLayerCliente.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerCliente.setLayer(jtfIDCliente, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerCliente.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerCliente.setLayer(jtfINomeCliente, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerCliente.setLayer(jbtnNovoCliente, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayerClienteLayout = new javax.swing.GroupLayout(jLayerCliente);
        jLayerCliente.setLayout(jLayerClienteLayout);
        jLayerClienteLayout.setHorizontalGroup(
            jLayerClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayerClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfIDCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfINomeCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnNovoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jLayerClienteLayout.setVerticalGroup(
            jLayerClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayerClienteLayout.createSequentialGroup()
                .addGroup(jLayerClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfIDCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jtfINomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnNovoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayerTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jLayerTableFocusLost(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Lista de itens da nota");

        tableProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableProdutos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tableProdutosFocusLost(evt);
            }
        });
        tableProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tableProdutosMouseExited(evt);
            }
        });
        jScrollPane1.setViewportView(tableProdutos);

        btnSalvar.setText("Lançar nota");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        jbtnEditar.setText("Editar");
        jbtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditarActionPerformed(evt);
            }
        });

        jbtnExcluir.setText("Excluir");
        jbtnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExcluirActionPerformed(evt);
            }
        });

        jtfTotalNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfTotalNotaActionPerformed(evt);
            }
        });

        jLabel4.setText("Total:");

        jLayerTable.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerTable.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerTable.setLayer(btnSalvar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerTable.setLayer(jbtnEditar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerTable.setLayer(jbtnExcluir, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerTable.setLayer(jtfTotalNota, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerTable.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayerTableLayout = new javax.swing.GroupLayout(jLayerTable);
        jLayerTable.setLayout(jLayerTableLayout);
        jLayerTableLayout.setHorizontalGroup(
            jLayerTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayerTableLayout.createSequentialGroup()
                .addGroup(jLayerTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayerTableLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayerTableLayout.createSequentialGroup()
                        .addComponent(jbtnExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnEditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalvar))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayerTableLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfTotalNota, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jLayerTableLayout.setVerticalGroup(
            jLayerTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayerTableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jLayerTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfTotalNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jLayerTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar)
                    .addComponent(jbtnEditar)
                    .addComponent(jbtnExcluir))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayerCliente)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLayerTable))
                    .addComponent(jLayerProduto))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLayerCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLayerProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayerTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (jtfIDCliente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente antes de lançar a nota fiscal.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tablemodel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um item na nota fiscal.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Você realmente deseja lançar esta nota fiscal?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            salvarNotaFiscal();

        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void salvarNotaFiscal() {
        NotaFiscal nota = new NotaFiscal();
        nota.setDataEmissao(DataUtil.dataAtual());
        nota.setCliente(clienteSelecionado);
        nota.setListaItens(tablemodel.getlist());

        try ( Connection con = ConnectionsFactory.createConnetionToMySQL()) {
            NotaFiscalDAO dao = new NotaFiscalDAO(con);
            dao.create(nota);

            notifyObservers(nota);// avisando os observers

            JOptionPane.showMessageDialog(this, "Nota fiscal lançada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparTodosCampos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar com o banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar nota fiscal: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparTodosCampos() {
        limparCamposProduto();
        jtfIDCliente.setText("");
        jtfINomeCliente.setText("");
        tablemodel.removeAll();
    }

    private void jbtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelarActionPerformed
        limparCamposProduto();
    }//GEN-LAST:event_jbtnCancelarActionPerformed

    private void jbtnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAdicionarActionPerformed
        if (this.type == OperationType.SAVE) { //SALVA no banco  
            if (produtoSelecionado != null && !jtfQuantidade.getText().isEmpty()) {
                ItemNota itemNota = new ItemNota();
                itemNota.setProduto(produtoSelecionado);
                itemNota.setQuantidade(Integer.parseInt(jtfQuantidade.getText()));
                itemNota.setValorItem(produtoSelecionado.getValor());

                ItemNotaTableModel model = (ItemNotaTableModel) tableProdutos.getModel();
                model.add(itemNota);
                limparCamposProduto();
                atualizarTotalNota();

            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto e insira a quantidade.");
            }
        } else { //edita no banco
            if (rowIndexUpdate >= 0) {
                ItemNota itemNota = new ItemNota();
                itemNota.setProduto(produtoSelecionado);
                itemNota.setQuantidade(Integer.parseInt(jtfQuantidade.getText()));
                itemNota.setValorItem(produtoSelecionado.getValor());

                tablemodel.updateItemAt(rowIndexUpdate, itemNota);
                jbtnAdicionar.setText("Adicionar");
                limparCamposProduto();
                tableProdutos.clearSelection();
                atualizarTotalNota();
            }

        }
    }//GEN-LAST:event_jbtnAdicionarActionPerformed

    private void jtfIdProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfIdProdutoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfIdProdutoActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        tableProdutos.clearSelection();
        atualizarEstadoBotoes(true);
    }//GEN-LAST:event_formMouseClicked

    private void jtfValorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfValorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfValorActionPerformed

    private void jtfQuantidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfQuantidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfQuantidadeActionPerformed

    private void jtfTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfTotalActionPerformed

    private void jtfDescricaoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfDescricaoProdutoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfDescricaoProdutoActionPerformed

    private void jtfDescricaoProdutoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfDescricaoProdutoKeyReleased

    }//GEN-LAST:event_jtfDescricaoProdutoKeyReleased

    private void jtfQuantidadeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfQuantidadeKeyReleased
        try {
            int quantidade = !jtfQuantidade.getText().isEmpty() ? Integer.parseInt(jtfQuantidade.getText()) : 1;
            double valor = formatadorDecimal.parse(jtfValor.getText()).doubleValue();
            double total = quantidade * valor;
            jtfTotal.setText(formatadorDecimal.format(total));
        } catch (NumberFormatException | ParseException e) {
            jtfTotal.setText("");
            System.out.println("Erro ao converter valores: " + e.getMessage());
        }
    }//GEN-LAST:event_jtfQuantidadeKeyReleased

    private void jtfDescricaoProdutoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtfDescricaoProdutoMouseClicked
        if (evt.getClickCount() == 2) {  // Seleção com duplo clique
            buscarProdutos(jtfDescricaoProduto.getText().trim());
        }
    }//GEN-LAST:event_jtfDescricaoProdutoMouseClicked

    private void jtfINomeClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfINomeClienteKeyReleased
        buscarClientes(jtfDescricaoProduto.getText().trim());
    }//GEN-LAST:event_jtfINomeClienteKeyReleased

    private void jtfINomeClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtfINomeClienteMouseClicked
        if (evt.getClickCount() == 2) {  // Seleção com duplo clique
            buscarClientes(jtfINomeCliente.getText().trim());
        }
    }//GEN-LAST:event_jtfINomeClienteMouseClicked

    private void jbtnNovoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNovoProdutoActionPerformed
        ProdutoCRUDView view = new ProdutoCRUDView();
        view.setVisible(true);
    }//GEN-LAST:event_jbtnNovoProdutoActionPerformed

    private void jbtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditarActionPerformed
        if (itemSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela");
        } else {
            preencherCamposProduto(itemSelecionado);
            jbtnAdicionar.setText("Salvar alterações");
            this.type = OperationType.EDIT;
        }
        atualizarEstadoBotoes(true);

    }//GEN-LAST:event_jbtnEditarActionPerformed

    private void jbtnNovoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNovoClienteActionPerformed
        ClienteCRUDView view = new ClienteCRUDView();
        view.setVisible(true);
    }//GEN-LAST:event_jbtnNovoClienteActionPerformed

    private void jbtnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExcluirActionPerformed
        if (itemSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela");
        } else {
            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Tem certeza de que deseja excluir este item?",
                    "Confirmação de Exclusão",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                tablemodel.delete(itemSelecionado);
                atualizarTotalNota();
            }
        }
        atualizarEstadoBotoes(true);
    }//GEN-LAST:event_jbtnExcluirActionPerformed

    private void tableProdutosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tableProdutosFocusLost
    }//GEN-LAST:event_tableProdutosFocusLost

    private void tableProdutosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableProdutosMouseExited
    }//GEN-LAST:event_tableProdutosMouseExited

    private void jLayerTableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLayerTableFocusLost
    }//GEN-LAST:event_jLayerTableFocusLost

    private void jtfTotalNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfTotalNotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfTotalNotaActionPerformed

    private void jtfINomeClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfINomeClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfINomeClienteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NotaCreateView.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NotaCreateView.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NotaCreateView.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NotaCreateView.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NotaCreateView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalvar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLayeredPane jLayerCliente;
    private javax.swing.JLayeredPane jLayerProduto;
    private javax.swing.JLayeredPane jLayerTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnAdicionar;
    private javax.swing.JButton jbtnCancelar;
    private javax.swing.JButton jbtnEditar;
    private javax.swing.JButton jbtnExcluir;
    private javax.swing.JButton jbtnNovoCliente;
    private javax.swing.JButton jbtnNovoProduto;
    private javax.swing.JList<String> jltClientes;
    private javax.swing.JList<String> jltProdutos;
    private javax.swing.JScrollPane jspClientes;
    private javax.swing.JScrollPane jspProdutos;
    private javax.swing.JTextField jtfDescricaoProduto;
    private javax.swing.JTextField jtfIDCliente;
    private javax.swing.JTextField jtfINomeCliente;
    private javax.swing.JTextField jtfIdProduto;
    private javax.swing.JTextField jtfQuantidade;
    private javax.swing.JTextField jtfTotal;
    private javax.swing.JTextField jtfTotalNota;
    private javax.swing.JTextField jtfValor;
    private javax.swing.JLabel lblDescricao;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblValor;
    private javax.swing.JLabel lblValor1;
    private javax.swing.JPopupMenu menuClientes;
    private javax.swing.JPopupMenu menuProdutos;
    private javax.swing.JPanel panelPesquisaClientes;
    private javax.swing.JPanel panelPesquisaProdutos;
    private javax.swing.JTable tableProdutos;
    // End of variables declaration//GEN-END:variables
}
