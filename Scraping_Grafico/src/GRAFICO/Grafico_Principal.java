/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GRAFICO;


import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;



//Importar Log
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 *
 * @author ManuReyesI
 */
public class Grafico_Principal extends javax.swing.JFrame {
    
    private static Logger log = Logger.getLogger(Grafico_Principal.class);
    
    //Nombre fichero
    public static String FICHERO = "CAMBIO_IP";
    
    
    //Hilo
    public static MiRunnable miRunnable = null;
    public static Thread hilo = null;
    
    /**
     * Creates new form Grafico_Principal
     */
    public Grafico_Principal() {
        initComponents();
        
        //Nombre Ventana
        this.setTitle("Actualizador DNS");
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        Texto_IP = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        Texto_IP.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Texto_IP.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jScrollPane1.setViewportView(Texto_IP);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/logo_dinahosting.jpg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 436, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(136, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
        //Icono
        Image icono = new ImageIcon(getClass().getResource("/IMG/icono.png")).getImage();
        setIconImage(icono);
        
        this.Texto_IP.setEditable(false);
        
        log.info("Iniciando Ventana");
        log.info("Comprobando archivo de configuración");

        boolean configuracion = false;

        log.info("Preparandose para leer Archivo de Objetos");
        try{

            File file = new File(FICHERO);

            //Comprobar si existe el Archivo
            if(file.exists()){
                //Leendo archivo de Objetos
                ObjectInputStream leerObjeto = new ObjectInputStream(new FileInputStream(FICHERO));

                MAIN.Scraping_Grafico.creden = new UTILES.Credenciales();

                MAIN.Scraping_Grafico.creden = (UTILES.Credenciales)leerObjeto.readObject();

                configuracion = true;

            }else{
                MAIN.Scraping_Grafico.creden = new UTILES.Credenciales();
            }

        }
        catch(IOException | ClassNotFoundException ex){
            log.error("Error al leer archivo en el fichero");
        }

        if(!configuracion){
            log.info("Lanzando ventana de Configuracion");
            Grafico_Con config = new Grafico_Con(this, true);
            config.setVisible(true);
        }
        else{
            log.info("Ejecutando Thread");
            ejecutar();
        }
        
    }//GEN-LAST:event_formWindowOpened

    public static void credenciales(String usuario, String contrasena, String dominio, String host, String ip){
        
        log.info("Introduciendo datos en Credenciales");
        
        MAIN.Scraping_Grafico.creden = new UTILES.Credenciales();
        MAIN.Scraping_Grafico.creden.setUSUARIO(usuario);
        MAIN.Scraping_Grafico.creden.setCONTRASENA(contrasena);
        MAIN.Scraping_Grafico.creden.setDOMINIO(dominio);
        MAIN.Scraping_Grafico.creden.setNOMBRE_HOST(host);
        MAIN.Scraping_Grafico.creden.setIP_ACTUAL(ip);
        
        ejecutar();
        
    }
    
    public static void ejecutar(){
        
        miRunnable = new MiRunnable();
        hilo = new Thread (miRunnable);
        hilo.start();
        
    }
    
    public static class MiRunnable implements Runnable{
        
        private boolean shouldStop = false;

        public void stop(){
            this.shouldStop = true;
        }
        
        public boolean shouldStop(){
            return this.shouldStop;
        }

        public void run (){
            
            while (!shouldStop()){

                log.info("Comprobar IP");

                String ip = "";

                try{

                    log.info("Recuperando IP");

                    URL whatismyip = new URL("http://checkip.amazonaws.com");

                    BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));     

                    ip = in.readLine(); 

                    log.info("IP: "+ip);
                    
                    Grafico_Principal.Texto_IP.setText(ip);

                }
                catch(IOException ex){
                    log.error("Error al leer archivo con IP");
                }
                catch(Exception ex){
                    log.error("Error al recuperar IP");
                }

                if(ip.compareTo(MAIN.Scraping_Grafico.creden.IP_ACTUAL) != 0){

                    Connection.Response response = null;

                    MAIN.Scraping_Grafico.creden.IP_NUEVA = ip;

                    try{

                        Map<String, String> cokies = new HashMap<String, String>();

                        log.info("Preparando Inicio de Sesion");

                        cokies = Jsoup.connect(MAIN.Scraping_Grafico.creden.URL+"/login").method(Connection.Method.POST)
                                .data("id", MAIN.Scraping_Grafico.creden.USUARIO)
                                .data("password", MAIN.Scraping_Grafico.creden.CONTRASENA)
                                .data("recordar", "0")
                                .execute().cookies();

                        log.info("Cokies: "+cokies.toString());

                        try{

                            response = Jsoup.connect(MAIN.Scraping_Grafico.creden.URL+"/dominio/procesar-formulario-zonas-modificar-ajax/_producto/"+MAIN.Scraping_Grafico.creden.DOMINIO).method(Connection.Method.POST)
                                .data("tipo_zona", MAIN.Scraping_Grafico.creden.TIPO_ZONA)
                                .data("host_ipv4", MAIN.Scraping_Grafico.creden.NOMBRE_HOST)
                                .data("ipv4", MAIN.Scraping_Grafico.creden.IP_NUEVA)
                                .data("destino_original", MAIN.Scraping_Grafico.creden.IP_ACTUAL)
                                .cookies(cokies)
                                .execute();

                        }
                        catch(Exception ex){}

                        log.info("IP actual: "+ MAIN.Scraping_Grafico.creden.IP_ACTUAL);

                        log.info("IP " + MAIN.Scraping_Grafico.creden.IP_NUEVA + " actualiza con exito para el Host " + MAIN.Scraping_Grafico.creden.NOMBRE_HOST);

                        MAIN.Scraping_Grafico.creden.IP_ACTUAL = MAIN.Scraping_Grafico.creden.IP_NUEVA;

                        log.info("IP actual: "+ MAIN.Scraping_Grafico.creden.IP_ACTUAL);
                        
                        Grafico_Principal.Texto_IP.setText(MAIN.Scraping_Grafico.creden.IP_ACTUAL);
                        
                    }
                    catch(Exception ex){
                        log.error("Error al modificar la IP");
                    }

                }
                
                //Guardar fichero configuracion
                try{

                    //Guardando en Fichero
                    log.info("Preparando para guarda objeto en Archivo");
                    ObjectOutputStream guardarFichero = new ObjectOutputStream(new FileOutputStream(FICHERO));
                    guardarFichero.writeObject(MAIN.Scraping_Grafico.creden);
                    guardarFichero.close();

                    log.info("Objeto guardado con exito en el archivo " +FICHERO);

                }
                catch(IOException ex){
                    log.error("Error al guardar Objeto en el fichero");
                }
                
                log.info("Parando ejecucion de hilo por 5 minutos");
                
                try {
                    Thread.sleep (300000);
                } catch (InterruptedException ex) {
                    log.error("Error al parar Hilo");
                }
            }
        }
    }
    
    public static void cerrarSinGuardar(){
        log.info("Preparandose para Cerrar");
        
        // Cerrar ventana
        System.exit(0);
        
    }
    
    @Override
    public void dispose() {
        
        log.info("Preparandose para cerrar Ventana Principal");
        
        //Guardar fichero configuracion
        try{

            //Guardando en Fichero
            log.info("Preparando para guarda objeto en Archivo");
            ObjectOutputStream guardarFichero = new ObjectOutputStream(new FileOutputStream(FICHERO));
            guardarFichero.writeObject(MAIN.Scraping_Grafico.creden);
            guardarFichero.close();

            log.info("Objeto guardado con exito en el archivo " +FICHERO);

        }
        catch(IOException ex){
            log.error("Error al guardar Objeto en el fichero");
        }
        
        //Parar hilo
        miRunnable.stop();
         
        // Cerrar ventana
        super.dispose();
        System.exit(0);
              
    }
    
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
            java.util.logging.Logger.getLogger(Grafico_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Grafico_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Grafico_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Grafico_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Grafico_Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextPane Texto_IP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
