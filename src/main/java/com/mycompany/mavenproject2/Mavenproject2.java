package com.mycompany.mavenproject2;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Leonyela - Tienda de Flores 🌷 VERSIÓN BONITA DECORADA
 * Fondo con campo de tulipanes dibujados, tarjetas blancas translúcidas,
 * header degradado, botones redondeados.
 */
public class Mavenproject2 {

    private JFrame frame;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre, txtCantidad, txtPrecio, txtMonto;
    private JLabel lblPreview, lblTotal, lblContador;
    private ImageIcon imagenActual = null;
    private final Random rnd = new Random();
    private final DecimalFormat fmt = new DecimalFormat("#,##0.00");

    private final String[] floresRandom = {"Rosa", "Girasol", "Tulipán", "Lavanda", "Orquídea", "Margarita", "Clavel", "Lirio", "Peonía", "Hortensia"};
    private final String[] emojis = {"🌹", "🌻", "🌷", "💜", "🌺", "🌼", "🌸", "💐", "🌹", "💠"};

    // Paleta bonita
    private final Color ROSA = new Color(255, 111, 165);
    private final Color LILA = new Color(179, 136, 255);
    private final Color CREMA = new Color(255, 248, 240);
    private final Color VERDE_HOJA = new Color(85, 170, 95);

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new Mavenproject2().crearVentana());
    }

    public void crearVentana() {
        frame = new JFrame("Leonyela - Tienda de Flores 🌷");
        frame.setSize(860, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== HEADER DEGRADADO ROSA->LILA =====
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, ROSA, getWidth(), 0, LILA);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // florecitas decorativas del header
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
                g2.setColor(new Color(255,255,255,120));
                g2.drawString("🌷 🌸 🌷 🌸 🌷", getWidth() - 230, 38);
            }
        };
        header.setPreferredSize(new Dimension(0, 92));
        header.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 20));

        JLabel logo = new JLabel("🌷 Leonyela");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        logo.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Tienda de Flores  •  Frescura, color y amor en cada pétalo ✿");
        sub.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        sub.setForeground(new Color(255, 255, 255, 240));
        JPanel txtH = new JPanel(new GridLayout(2, 1, 0, 0));
        txtH.setOpaque(false);
        txtH.add(logo); txtH.add(sub);

        JLabel lado = new JLabel("🌸🌷🌹", SwingConstants.RIGHT);
        lado.setFont(new Font("Segoe UI", Font.PLAIN, 30));

        header.add(txtH, BorderLayout.CENTER);
        header.add(lado, BorderLayout.EAST);
        frame.add(header, BorderLayout.NORTH);

        // ===== FONDO CON TULIPANES =====
        FondoTulipanes fondo = new FondoTulipanes();
        fondo.setLayout(new BoxLayout(fondo, BoxLayout.Y_AXIS));
        fondo.setBorder(BorderFactory.createEmptyBorder(14, 18, 8, 18));

        // ----- TARJETA TABLA -----
        Tarjeta cardTabla = new Tarjeta();
        cardTabla.setLayout(new BoxLayout(cardTabla, BoxLayout.Y_AXIS));
        JLabel tReg = titulo("🌷 Registro de flores");
        cardTabla.add(tReg);
        cardTabla.add(Box.createVerticalStrut(6));

        String[] cols = {"Imagen", "Producto", "Cantidad", "Precio", "Monto"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override public Class<?> getColumnClass(int c) { return c == 0 ? ImageIcon.class : String.class; }
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(50);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setSelectionBackground(new Color(255, 200, 220));
        tabla.setSelectionForeground(new Color(80, 20, 50));

        JTableHeader th = tabla.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(new Color(255, 111, 165));
        th.setForeground(Color.WHITE);
        th.setPreferredSize(new Dimension(0, 34));
        ((DefaultTableCellRenderer) th.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? Color.WHITE : new Color(255, 235, 244));
                setHorizontalAlignment(col >= 2 ? SwingConstants.CENTER : SwingConstants.LEFT);
                setFont(new Font("Segoe UI", col == 1 ? Font.BOLD : Font.PLAIN, 13));
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
        tabla.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                JLabel l = new JLabel();
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setOpaque(true);
                l.setBackground(sel ? t.getSelectionBackground() : (row % 2 == 0 ? Color.WHITE : new Color(255, 235, 244)));
                if (v instanceof ImageIcon) { l.setIcon((ImageIcon) v); l.setText(""); }
                else { l.setIcon(null); l.setText(v == null ? "" : v.toString()); l.setFont(new Font("Segoe UI", Font.PLAIN, 24)); }
                return l;
            }
        });
        tabla.getColumnModel().getColumn(0).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(190);

        modelo.addRow(new Object[]{iconoTexto("🎀", new Color(255, 215, 235)), "Lavanda", "80", "42.65", "3,412.00"});
        modelo.addRow(new Object[]{iconoTexto("🌺", new Color(255, 205, 205)), "Orquídea", "61", "88.28", "5,385.08"});

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(0, 240));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(255, 170, 200), 2, true));
        scroll.getViewport().setBackground(Color.WHITE);
        cardTabla.add(scroll);

        // Barra total bonita
        JPanel barraTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 2));
        barraTotal.setOpaque(false);
        lblContador = new JLabel("🌸 2 productos");
        lblContador.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblContador.setForeground(new Color(150, 40, 90));
        lblTotal = new JLabel("💰 Total inventario: S/ 8,797.08");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotal.setForeground(new Color(150, 40, 90));
        lblTotal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 150, 190), 1, true),
                BorderFactory.createEmptyBorder(3, 12, 3, 12)));
        lblTotal.setOpaque(true);
        lblTotal.setBackground(new Color(255, 240, 246));
        barraTotal.add(lblContador);
        barraTotal.add(lblTotal);
        cardTabla.add(Box.createVerticalStrut(6));
        cardTabla.add(barraTotal);

        fondo.add(cardTabla);
        fondo.add(Box.createVerticalStrut(12));

        // ----- TARJETA FORMULARIO -----
        Tarjeta cardForm = new Tarjeta();
        cardForm.setLayout(new BoxLayout(cardForm, BoxLayout.Y_AXIS));
        cardForm.add(titulo("🌸 Datos de la flor"));
        cardForm.add(Box.createVerticalStrut(8));

        JPanel filaCampos = new JPanel(new GridLayout(2, 4, 12, 4));
        filaCampos.setOpaque(false);
        filaCampos.add(etiqueta("🌷 Nombre de la Flor"));
        filaCampos.add(etiqueta("🔢 Cantidad"));
        filaCampos.add(etiqueta("💲 Precio"));
        filaCampos.add(etiqueta("🧾 Monto (automático)"));

        txtNombre = campo("Ej. Tulipán rosado");
        txtCantidad = campo("Ej. 50");
        txtPrecio = campo("Ej. 35.50");
        txtMonto = campo("");
        txtMonto.setEditable(false);
        txtMonto.setBackground(new Color(245, 240, 255));
        txtMonto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtMonto.setForeground(new Color(120, 30, 90));
        filaCampos.add(txtNombre); filaCampos.add(txtCantidad); filaCampos.add(txtPrecio); filaCampos.add(txtMonto);
        cardForm.add(filaCampos);

        DocumentListener auto = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calcular(); }
            public void removeUpdate(DocumentEvent e) { calcular(); }
            public void changedUpdate(DocumentEvent e) { calcular(); }
        };
        txtCantidad.getDocument().addDocumentListener(auto);
        txtPrecio.getDocument().addDocumentListener(auto);

        // Fila imagen
        JPanel filaImg = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        filaImg.setOpaque(false);
        JLabel lblImgTxt = new JLabel("🖼️ Imagen del producto:");
        lblImgTxt.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPreview = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblPreview.setPreferredSize(new Dimension(84, 66));
        lblPreview.setOpaque(true);
        lblPreview.setBackground(Color.WHITE);
        lblPreview.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblPreview.setForeground(Color.GRAY);
        lblPreview.setBorder(new DashedBorder(new Color(200, 150, 200)));

        BotonRedondo btnSel = new BotonRedondo("📷 Seleccionar Imagen", new Color(52, 152, 219));
        btnSel.addActionListener(ev -> seleccionarImagen());
        BotonRedondo btnQuit = new BotonRedondo("🗑️ Quitar Imagen", new Color(150, 150, 160));
        btnQuit.addActionListener(ev -> {
            imagenActual = null;
            lblPreview.setIcon(null);
            lblPreview.setText("Sin imagen");
        });
        filaImg.add(lblImgTxt); filaImg.add(lblPreview); filaImg.add(btnSel); filaImg.add(btnQuit);
        cardForm.add(filaImg);

        // Botones CRUD
        JPanel filaBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        filaBtns.setOpaque(false);
        BotonRedondo btnCrear = new BotonRedondo("🌱 Crear", new Color(46, 204, 113));
        BotonRedondo btnEliminar = new BotonRedondo("❌ Eliminar", new Color(231, 76, 60));
        BotonRedondo btnActualizar = new BotonRedondo("🔄 Actualizar", new Color(52, 152, 219));
        BotonRedondo btnAleatorio = new BotonRedondo("🎲 Producto Aleatorio", new Color(243, 156, 18));
        btnCrear.addActionListener(ev -> crear());
        btnEliminar.addActionListener(ev -> eliminar());
        btnActualizar.addActionListener(ev -> actualizar());
        btnAleatorio.addActionListener(ev -> aleatorio());
        filaBtns.add(btnCrear); filaBtns.add(btnEliminar); filaBtns.add(btnActualizar); filaBtns.add(btnAleatorio);
        cardForm.add(filaBtns);

        fondo.add(cardForm);

        // Footer
        JLabel footer = new JLabel("🌷 Leonyela • Hecho con amor • ¡Gracias por tu compra! 🌷", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footer.setForeground(new Color(130, 60, 100));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        fondo.add(Box.createVerticalStrut(8));
        fondo.add(footer);

        frame.add(fondo, BorderLayout.CENTER);

        tabla.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int f = tabla.getSelectedRow();
                txtNombre.setText(modelo.getValueAt(f, 1).toString());
                txtCantidad.setText(modelo.getValueAt(f, 2).toString());
                txtPrecio.setText(modelo.getValueAt(f, 3).toString());
                txtMonto.setText(modelo.getValueAt(f, 4).toString());
                Object img = modelo.getValueAt(f, 0);
                if (img instanceof ImageIcon) {
                    imagenActual = (ImageIcon) img;
                    lblPreview.setText("");
                    lblPreview.setIcon(new ImageIcon(((ImageIcon) img).getImage().getScaledInstance(84, 66, Image.SCALE_SMOOTH)));
                } else {
                    imagenActual = null;
                    lblPreview.setIcon(null);
                    lblPreview.setText(img == null ? "Sin imagen" : img.toString());
                }
            }
        });

        actualizarTotales();
        frame.setVisible(true);
    }

    private JLabel titulo(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setForeground(new Color(160, 40, 100));
        return l;
    }

    private JLabel etiqueta(String t) {
        JLabel l = new JLabel(t, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(90, 50, 80));
        return l;
    }

    private JTextField campo(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 170, 200), 1, true),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)));
        tf.setBackground(Color.WHITE);
        tf.setToolTipText(placeholder);
        return tf;
    }

    private void calcular() {
        try {
            double cant = Double.parseDouble(txtCantidad.getText().trim());
            double pre = Double.parseDouble(txtPrecio.getText().trim());
            txtMonto.setText(fmt.format(cant * pre));
        } catch (Exception e) { txtMonto.setText(""); }
    }

    private void seleccionarImagen() {
        JFileChooser ch = new JFileChooser();
        ch.setDialogTitle("🌷 Seleccionar imagen del producto");
        if (ch.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File f = ch.getSelectedFile();
            ImageIcon ic = new ImageIcon(f.getAbsolutePath());
            imagenActual = new ImageIcon(ic.getImage().getScaledInstance(50, 45, Image.SCALE_SMOOTH));
            lblPreview.setText("");
            lblPreview.setIcon(new ImageIcon(ic.getImage().getScaledInstance(84, 66, Image.SCALE_SMOOTH)));
        }
    }

    private void crear() {
        String nom = txtNombre.getText().trim();
        if (nom.isEmpty() || txtCantidad.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "🌸 Completa Nombre, Cantidad y Precio.", "Faltan datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double cant = Double.parseDouble(txtCantidad.getText().trim());
            double pre = Double.parseDouble(txtPrecio.getText().trim());
            String cantTxt = (cant == (int) cant) ? String.valueOf((int) cant) : txtCantidad.getText().trim();
            Object img = (imagenActual != null) ? imagenActual : iconoTexto("🌸", new Color(255, 225, 240));
            modelo.addRow(new Object[]{img, nom, cantTxt, txtPrecio.getText().trim(), fmt.format(cant * pre)});
            limpiar();
            actualizarTotales();
            JOptionPane.showMessageDialog(frame, "🌷 ¡Flor \"" + nom + "\" agregada con amor!", "Creado", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Cantidad y Precio deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int f = tabla.getSelectedRow();
        if (f < 0) {
            JOptionPane.showMessageDialog(frame, "Selecciona una fila para eliminar 🌸", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modelo.removeRow(f);
        limpiar();
        actualizarTotales();
    }

    private void actualizar() {
        int f = tabla.getSelectedRow();
        if (f < 0) {
            JOptionPane.showMessageDialog(frame, "Selecciona una fila para actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double cant = Double.parseDouble(txtCantidad.getText().trim());
            double pre = Double.parseDouble(txtPrecio.getText().trim());
            String cantTxt = (cant == (int) cant) ? String.valueOf((int) cant) : txtCantidad.getText().trim();
            modelo.setValueAt((imagenActual != null) ? imagenActual : modelo.getValueAt(f, 0), f, 0);
            modelo.setValueAt(txtNombre.getText().trim(), f, 1);
            modelo.setValueAt(cantTxt, f, 2);
            modelo.setValueAt(txtPrecio.getText().trim(), f, 3);
            modelo.setValueAt(fmt.format(cant * pre), f, 4);
            actualizarTotales();
            JOptionPane.showMessageDialog(frame, "🌷 Producto actualizado", "OK", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Revisa Cantidad y Precio.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aleatorio() {
        int i = rnd.nextInt(floresRandom.length);
        txtNombre.setText(floresRandom[i]);
        int cant = 10 + rnd.nextInt(91);
        double pre = Math.round((20 + rnd.nextDouble() * 80) * 100.0) / 100.0;
        txtCantidad.setText(String.valueOf(cant));
        txtPrecio.setText(String.valueOf(pre));
        Color[] colores = {new Color(255, 215, 235), new Color(255, 240, 200), new Color(220, 235, 255), new Color(225, 255, 225)};
        ImageIcon ic = iconoTexto(emojis[i], colores[rnd.nextInt(colores.length)]);
        imagenActual = ic;
        lblPreview.setText("");
        lblPreview.setIcon(new ImageIcon(ic.getImage().getScaledInstance(84, 66, Image.SCALE_SMOOTH)));
        calcular();
    }

    private void limpiar() {
        txtNombre.setText(""); txtCantidad.setText(""); txtPrecio.setText(""); txtMonto.setText("");
        imagenActual = null;
        lblPreview.setIcon(null); lblPreview.setText("Sin imagen");
        tabla.clearSelection();
    }

    private void actualizarTotales() {
        double total = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            try {
                String m = modelo.getValueAt(i, 4).toString().replace(",", "");
                total += Double.parseDouble(m);
            } catch (Exception ignored) {}
        }
        lblContador.setText("🌸 " + modelo.getRowCount() + " productos");
        lblTotal.setText("💰 Total inventario: S/ " + fmt.format(total));
    }

    private ImageIcon iconoTexto(String emoji, Color fondo) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(50, 45, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(fondo);
        g.fillRoundRect(0, 0, 50, 45, 12, 12);
        g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        FontMetrics fm = g.getFontMetrics();
        int x = (50 - fm.stringWidth(emoji)) / 2;
        int y = (45 - fm.getHeight()) / 2 + fm.getAscent();
        g.setColor(Color.BLACK);
        g.drawString(emoji, x, y);
        g.dispose();
        return new ImageIcon(img);
    }

    // ===== TARJETA BLANCA REDONDEADA =====
    static class Tarjeta extends JPanel {
        Tarjeta() { setOpaque(false); setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16)); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 6, 22, 22);
            g2.setColor(new Color(255, 255, 255, 235));
            g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 22, 22);
            g2.setColor(new Color(255, 170, 200));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 22, 22);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ===== BOTÓN REDONDEADO CON HOVER =====
    static class BotonRedondo extends JButton {
        private final Color base;
        private boolean hover = false;
        BotonRedondo(String t, Color b) {
            super(t);
            base = b;
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(150, 36));
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(java.awt.event.MouseEvent e) { hover = false; repaint(); }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = hover ? base.brighter() : base;
            GradientPaint gp = new GradientPaint(0, 0, c.brighter(), 0, getHeight(), c.darker());
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(255, 255, 255, 90));
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() / 2, 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ===== BORDE PUNTEADO PARA PREVIEW =====
    static class DashedBorder extends AbstractBorder {
        private final Color c;
        DashedBorder(Color c) { this.c = c; }
        @Override public void paintBorder(Component comp, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(c);
            g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5, 4}, 0));
            g2.drawRoundRect(x + 1, y + 1, w - 3, h - 3, 10, 10);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(4, 4, 4, 4); }
    }

    // ===== FONDO CON CAMPO DE TULIPANES DIBUJADOS =====
    class FondoTulipanes extends JPanel {
        FondoTulipanes() { setOpaque(true); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int W = getWidth(), H = getHeight();

            // Cielo crema -> rosa clarito
            GradientPaint cielo = new GradientPaint(0, 0, new Color(255, 252, 245), 0, H, new Color(255, 225, 235));
            g2.setPaint(cielo);
            g2.fillRect(0, 0, W, H);

            // Sol suave arriba derecha
            g2.setColor(new Color(255, 235, 150, 160));
            g2.fillOval(W - 130, 10, 90, 90);
            g2.setColor(new Color(255, 245, 190, 120));
            g2.fillOval(W - 145, -5, 120, 120);

            // Nubecitas
            g2.setColor(new Color(255, 255, 255, 170));
            nube(g2, 60, 22, 1.0f);
            nube(g2, W / 2, 8, 0.7f);
            nube(g2, W - 260, 55, 0.85f);

            // Pétalos flotando
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
            g2.setColor(new Color(255, 150, 190, 130));
            long t = System.currentTimeMillis() / 700;
            for (int i = 0; i < 8; i++) {
                int px = (int) ((i * 137 + t * 13) % (W + 40)) - 20;
                int py = 40 + (i * 53) % 200;
                g2.drawString(i % 2 == 0 ? "🌸" : "🌺", px, py);
            }

            // Pasto abajo
            g2.setPaint(new GradientPaint(0, H - 130, new Color(190, 235, 170, 0), 0, H, new Color(140, 210, 140, 220)));
            g2.fillRect(0, H - 130, W, 130);
            g2.setColor(new Color(85, 170, 95, 200));
            g2.fillRect(0, H - 26, W, 26);

            // Hilera de tulipanes abajo (borde inferior)
            Color[] petalos = {new Color(255, 90, 140), new Color(255, 160, 40), new Color(235, 80, 80),
                    new Color(190, 110, 255), new Color(255, 120, 170), new Color(255, 220, 60)};
            int n = Math.max(8, W / 78);
            for (int i = 0; i < n; i++) {
                int x = 20 + i * (W / Math.max(1, n)) + (i % 2) * 10;
                int yBase = H - 8 - (i % 3) * 8;
                int size = 30 + (i % 3) * 7;
                dibujarTulipan(g2, x, yBase, size, petalos[i % petalos.length]);
            }
            // Tulipanes laterales arriba para enmarcar
            for (int i = 0; i < 3; i++) {
                dibujarTulipan(g2, 8, 120 + i * 90, 26, petalos[(i + 2) % petalos.length]);
                dibujarTulipan(g2, W - 34, 120 + i * 90, 26, petalos[(i + 4) % petalos.length]);
            }
            g2.dispose();
        }

        private void nube(Graphics2D g2, int x, int y, float s) {
            Ellipse2D e1 = new Ellipse2D.Float(x, y, 60 * s, 26 * s);
            Ellipse2D e2 = new Ellipse2D.Float(x + 20 * s, y - 10 * s, 50 * s, 28 * s);
            Ellipse2D e3 = new Ellipse2D.Float(x + 45 * s, y, 55 * s, 24 * s);
            g2.fill(e1); g2.fill(e2); g2.fill(e3);
        }

        private void dibujarTulipan(Graphics2D g2, int x, int yBase, int size, Color petalo) {
            int h = size + 26;
            // tallo
            g2.setColor(VERDE_HOJA);
            g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x, yBase, x, yBase - h);
            // hojitas
            g2.fill(new Ellipse2D.Float(x - 16, yBase - h / 2 - 4, 16, 8));
            g2.fill(new Ellipse2D.Float(x + 1, yBase - h / 2 + 4, 16, 8));
            // flor: 3 pétalos
            int w = size, hh = size;
            int topY = yBase - h - hh + 10;
            g2.setColor(petalo.darker());
            g2.fill(new Ellipse2D.Float(x - w / 2, topY, w, hh)); // fondo
            g2.setColor(petalo);
            g2.fill(new Ellipse2D.Float(x - w / 2 + 3, topY - 4, w - 6, hh)); // central alto
            g2.setColor(petalo.brighter());
            g2.fill(new Ellipse2D.Float(x - w / 4, topY + 2, w / 2, hh - 6)); // brillo
            // puntitas
            g2.setColor(petalo.darker());
            int[] xs = {x - w / 2 + 3, x - w / 4, x, x + w / 4, x + w / 2 - 3};
            for (int px : xs) {
                Polygon tri = new Polygon(new int[]{px - 5, px + 5, px}, new int[]{topY + 2, topY + 2, topY - 8}, 3);
                g2.fill(tri);
            }
        }
    }
}
