package main;

import com.google.gson.Gson;
import com.sun.deploy.net.BasicHttpRequest;
import models.SampleResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String args[]) throws IOException{
        Gson gson = new Gson();
        String httpResponse = fetchDataFromAPI();

        SampleResponse response = gson.fromJson(httpResponse, SampleResponse.class);

        Map<String, Integer> productQuantities = processProductQuantities(response);

        printTable(response, productQuantities);

    }

    private static void printTable(SampleResponse response, Map<String, Integer> productQuantities) {
        System.out.println("+-----------------+---------+------------+");
        System.out.println("| Product         |  Orders |      Total |");
        System.out.println("+-----------------+---------+------------+");

        for(int i=0;i<response.getProducts().size();i++){
            StringBuilder line = new StringBuilder();
            line.append("| \t");
            line.append(response.getProducts().get(i).getName());
            line.append("\t | \t");
            int quantity = productQuantities.getOrDefault(response.getProducts().get(i).getId(),0);
            line.append(quantity);
            line.append(" \t | \t");
            line.append((response.getProducts().get(i).getPrice()*quantity));
            System.out.println(line.toString());
            System.out.println("+--------------+---------+------------+");
        }
    }

    private  static Map<String,Integer> processProductQuantities(SampleResponse response){
        Map<String,Integer> productToQuantityMap = new HashMap<>();
        response.getOrders().forEach((order -> {
            order.getItems().forEach( item -> {
                productToQuantityMap.put(item.getProductId(),
                        item.getQuantity() + productToQuantityMap.getOrDefault(item.getProductId(), 0));
            });
        }));
        return productToQuantityMap;
    }

    private static String fetchDataFromAPI() throws IOException {
        URL url = new URL("https://www.tailorsoft.co/sample.json");
        URLConnection yc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine="";
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();
        return response.toString();
    }
}
