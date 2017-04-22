/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import errordetection.DataBits;
import errordetection.Parity;
import errordetection.Hamming;
import errordetection.Crc;
import errordetection.CodeBase;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Kamil
 */
public class ParityServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            //generowanie ciągu bitów
            Integer bitAmount = Integer.parseInt(request.getParameter("bitAmount"));
            DataBits inputBits = new DataBits();
            inputBits.generate(bitAmount);
            String generatedBits = inputBits.toString();

            //kodowanie bitów
            CodeBase transmitter;
            String title = "";
            switch (request.getParameter("typeOf")) {
                case "parity":
                {
                    title = "Bit parzystości";
                    Parity parityTransmitter = new Parity();
                    transmitter = (CodeBase) parityTransmitter;
                    break;
                }
                case "hamming":
                {
                    title = "Kodowanie Hamminga";
                    Hamming hammingTransmitter = new Hamming();
                    transmitter = (CodeBase) hammingTransmitter;
                    break;
                }
                case "crc-atm":
                {
                    title = "Kodowanie CRC ATM";
                    Crc crcTransmitter = new Crc();
                    crcTransmitter.setKey(Crc.ATM);
                    transmitter = (CodeBase) crcTransmitter;
                    break;
                }
                case "crc-12":
                {
                    title = "Kodowanie CRC-12";
                    Crc crcTransmitter = new Crc();
                    crcTransmitter.setKey(Crc.CRC12);
                    transmitter = (CodeBase) crcTransmitter;
                    break;
                }
                case "crc-16":
                {
                    title = "Kodowanie CRC-16";
                    Crc crcTransmitter = new Crc();
                    crcTransmitter.setKey(Crc.CRC16);
                    transmitter = (CodeBase) crcTransmitter;
                    break;
                }
                case "crc-16R":
                {
                    title = "Kodowanie CRC-16 Reverse";
                    Crc crcTransmitter = new Crc();
                    crcTransmitter.setKey(Crc.CRC16_REVERSE);
                    transmitter = (CodeBase) crcTransmitter;
                    break;
                }
                case "crc-sdlc":
                {
                    title = "Kodowanie CRC-SDLC";
                    Crc crcTransmitter = new Crc();
                    crcTransmitter.setKey(Crc.SDLC);
                    transmitter = (CodeBase) crcTransmitter;
                    break;
                }
                case "crc-sdlcR":
                {
                    title = "Kodowanie CRC-SDLC Reverse";
                    Crc crcTransmitter = new Crc();
                    crcTransmitter.setKey(Crc.SDLC_REVERSE);
                    transmitter = (CodeBase) crcTransmitter;
                    break;
                }
                case "crc-32":
                {
                    title = "Kodowanie CRC-32";
                    Crc crcTransmitter = new Crc();
                    crcTransmitter.setKey(Crc.CRC32);
                    transmitter = (CodeBase) crcTransmitter;
                    break;
                }
                default:
                    {
                        //ten default to przez to, ze IDE na mnie drze ryja, ze jestem debil bo go nie dalem, pozniej cos wymysle
                        
                        Parity parityTransmitter = new Parity();
                        transmitter = (CodeBase) parityTransmitter;
                        break;
                    }
            }
            transmitter.setData(generatedBits);
            transmitter.encode();
            transmitter.setCode(transmitter.codeToString());
            String encodeBits = transmitter.codeToString();

            //zakłócanie bitów
            Integer errorAmount = Integer.parseInt(request.getParameter("errorAmount"));
            transmitter.interfere(errorAmount);
            String errorBits = transmitter.codeToString();

            //kodowanie po korekcji
            transmitter.setCode(errorBits);
            transmitter.fix();
            //naprawione
            String correctedData = transmitter.codeToString();
            transmitter.decode();
            //odkodowane
            String decodeData = transmitter.dataToString();
            int errors = countErrors(encodeBits, errorBits);
            //ilosc bitów
            String dataBits = Integer.toString(transmitter.getDataBitsNumber());
            //bity kontrolne
            String controlBits = Integer.toString(transmitter.getControlBitsNumber());
            int detected = transmitter.getDetectedErrorsNumber();
            //wykryte
            String detectedErrors = Integer.toString(detected);
            //naprawione
            String fixesErrors = Integer.toString(transmitter.getFixedErrorsNumber());
            //niewykryte
            String notDetectedErrors = Integer.toString(errors - detected);
            System.out.println(errors);
            System.out.println(detected);
            System.out.println(errors-detected);
            Map values = new LinkedHashMap() {};
            values.put( "Liczba bitów", errorAmount);
            values.put( "Liczba zakłóceń", bitAmount);
            values.put( "Wygenerowane", generatedBits);
            values.put( "Zakodowane", encodeBits);
            values.put( "Zakłócone", errorBits);
            values.put( "Zakodowane po korekcji", correctedData);
            values.put( "Odkodowane", decodeData);
            values.put( "Ilość bitów", dataBits);
            values.put( "Błędy wykryte", detectedErrors);
            values.put( "Błędy naprawione", fixesErrors);
            values.put( "Bity nadmiarowe", controlBits);
            values.put( "Bity niewykryte", notDetectedErrors);
            
            
            
           
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BitGenarator</title>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1>"+title+"</h1>");
            printTable(out, (HashMap) values);
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private int countErrors(String input, String output) {
        if (input.length() != output.length()) {
            return -1;
        } else {
            int errors = 0;
            int l = input.length();
            for (int i = 0; i < l; i++) {
                if (input.charAt(i) != output.charAt(i)) {
                    errors++;
                }
            }
            return errors;
        }
    }
  	
    private void printTable(PrintWriter out, HashMap values )
    {
        out.println("<table border=1>");
        for(Object key : values.keySet())
        {
            out.println("<tr><td>"+key+"</td><td>"+values.get(key)+"</td></tr>");
        }
        out.println("</table>");
    }
}
