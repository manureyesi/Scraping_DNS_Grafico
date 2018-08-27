/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MAIN;

import GRAFICO.Grafico_Principal;
import javax.swing.JFrame;



//Importar Log
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

//Importar Jsoup
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

/**
 *
 * @author ManuReyesI
 */
public class Scraping_Grafico extends JFrame{

    /**
     * @param args the command line arguments
     */
    
    private static Logger log = Logger.getLogger(Scraping_Grafico.class);
    
    public static GRAFICO.Grafico_Principal principal = new Grafico_Principal();
    
    public static String FICHERO = "CAMBIO_IP";
    
    public static void main(String[] args) {
        
        // Carga el archivo de configuracion de log4J
        PropertyConfigurator.configure("log4j.properties");
        
        log.info(".............. INICIANDO ..............");
        log.info("Iniciando Grafico.");
        
        principal.setVisible(true);
    }
    
}
