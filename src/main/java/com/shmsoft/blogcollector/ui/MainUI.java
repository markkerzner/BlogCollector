package com.shmsoft.blogcollector.ui;

import com.shmsoft.blogcollector.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mark
 */
public class MainUI extends javax.swing.JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MainUI.class);
    /**
     * Creates new form MainUI
     */
    public MainUI() {
        logger.info("Starting BlogCollector Main UI, Version: {}", Settings.VERSION);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainMenu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        optionsMenuItem = new javax.swing.JMenuItem();
        downloadMenu = new javax.swing.JMenu();
        startDownloadMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        About = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Blog post collector");

        fileMenu.setText("File");

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        mainMenu.add(fileMenu);

        editMenu.setText("Edit");

        optionsMenuItem.setText("Options");
        optionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(optionsMenuItem);

        mainMenu.add(editMenu);

        downloadMenu.setText("Download");

        startDownloadMenuItem.setText("Start");
        startDownloadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startDownloadMenuItemActionPerformed(evt);
            }
        });
        downloadMenu.add(startDownloadMenuItem);

        mainMenu.add(downloadMenu);

        helpMenu.setText("Help");

        About.setText("About");
        About.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutActionPerformed(evt);
            }
        });
        helpMenu.add(About);

        mainMenu.add(helpMenu);

        setJMenuBar(mainMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 688, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 423, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        exitApp();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void AboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutActionPerformed
        showAboutDialog();
    }//GEN-LAST:event_AboutActionPerformed

    private void optionsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsMenuItemActionPerformed
        showOptionsDialog();
    }//GEN-LAST:event_optionsMenuItemActionPerformed

    private void startDownloadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startDownloadMenuItemActionPerformed
        showDownloadDialog();
    }//GEN-LAST:event_startDownloadMenuItemActionPerformed

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
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        // connect to the database

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem About;
    private javax.swing.JMenu downloadMenu;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JMenuItem optionsMenuItem;
    private javax.swing.JMenuItem startDownloadMenuItem;
    // End of variables declaration//GEN-END:variables

    private void exitApp() {        
        dispose();        
        System.exit(0);
    }

    private void showAboutDialog() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AboutDialog dialog = new AboutDialog(new javax.swing.JFrame(), true);
                dialog.setVisible(true);
            }
        });
    }


    private void showDownloadDialog() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                DownloadDialog dialog = new DownloadDialog(new javax.swing.JFrame(), true);
                dialog.setVisible(true);
            }
        });
    }

    private void showOptionsDialog() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                OptionsDialog dialog = new OptionsDialog(new javax.swing.JFrame(), true);
                dialog.setVisible(true);
            }
        });
    }

}
