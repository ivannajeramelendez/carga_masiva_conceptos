package com.convertidor.AplicarPago;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONObject;

public class App 
{
	public static int ID_CLIENTE=0;
	public static int ID_PEDIDO=1;
    public static void main( String[] args )
    {
    	try {
        	File f = new File("CLIENTES.xlsx");
            InputStream inp = new FileInputStream(f);
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(0); 
            int iRow=1;
            Row row = sheet.getRow(iRow); //En qué fila empezar ya dependerá también de si tenemos, por ejemplo, el título de cada columna en la primera fila
            while(row!=null) 
            {
            	 Cell fechaTransaccionCell = row.getCell(ID_PEDIDO);  
                 Cell idClienteCell = row.getCell(ID_CLIENTE); 
                  
                 int fechaTransaccionVal = (int) fechaTransaccionCell.getNumericCellValue();
                 int idClienteVal = (int) idClienteCell.getNumericCellValue();
                 System.out.println( fechaTransaccionVal+"\t|\t"+idClienteVal   +"\t|\t");
                 
                 String body ="{\r\n"
         				+ "\"IDCliente\":"+idClienteVal+",\r\n"
         				+ "\"IDClub\":2,\r\n"  //CAMBIAR EL ID DEL CLUB
         				+ "\"Cantidad\":1,\r\n"
         				+ "\"IDProductoServicio\":"+fechaTransaccionVal+",\r\n"
         				+ "\"Observaciones\":\'PRUEBA DE CARGA MASIVA',\r\n"
         				+ "\"DescuentoPorciento\":0,\r\n"
         				+ "\"FechaInicio\":\"2022-11-09 00:00:00\",\r\n" //CAMBIAR FECHA DE INICIO
         				+ "\"FechaFin\":\"2022-11-09 00:00:00\",\r\n" //CAMBIAR FECHA FIN
         				+ "\"CobroProporcional\":0,\r\n"
         				+ "\"Token\":\"77D5BDD4-1FEE-4A47-86A0-1E7D19EE1C74\"\r\n"
         				+ "}";
                 
                 String resp = connectApi("http://192.168.20.44/ServiciosClubAlpha/api/OrdenDeVenta/Registra",body);
     		   	
                System.out.println(resp);
                iRow++;  
                row = sheet.getRow(iRow);
            }
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
	public static String connectApi(String endpoint,String body) {
		HttpRequest request1 = HttpRequest.newBuilder().uri(
				 URI.create(endpoint))
				 .header("Content-Type", "application/json")
				 .POST(BodyPublishers.ofString(body)).build();
		CompletableFuture<String> client = HttpClient.newHttpClient().sendAsync(request1, BodyHandlers.ofString())
				.thenApply(HttpResponse::body);
		String json = "";
		try {
			json = String.valueOf(client.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return json;
	}
}
            
           
