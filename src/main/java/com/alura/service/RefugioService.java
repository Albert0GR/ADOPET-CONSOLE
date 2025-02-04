package com.alura.service;

import com.alura.client.ClientHttpConfiguration;
import com.alura.domain.Refugio;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RefugioService {

    private ClientHttpConfiguration client;

    public RefugioService(ClientHttpConfiguration client) {
        this.client = client;
    }

    public void listarRefugios() throws IOException, InterruptedException {
        String uri = "http://localhost:8080/refugios";
        HttpResponse<String> response = client.dispararRequestGet(uri);
        String responseBody = response.body();
        //JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
        ObjectMapper objectMapper = new ObjectMapper().enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION);
        Refugio[] refugios = objectMapper.readValue(responseBody,Refugio[].class);
        //lista de refugios
        List<Refugio> refugioList = Arrays.stream(refugios).toList();
        if (refugioList.isEmpty()){
            System.out.println("No hay refugios registrados");
        } else {
            System.out.println("Refugios registrados:");
            for (Refugio refugio : refugioList) {
                //JsonObject jsonObject = element.getAsJsonObject(); ya no se ocupa
                //long id = jsonObject.get("id").getAsLong();
                long id = refugio.getId();
                String nombre = refugio.getNombre();
                System.out.println(id + " - " + nombre);
            }
        }

    }

    public void registrarRefugio() throws IOException, InterruptedException {
        System.out.println("Escriba el nombre del refugio:");
        String nombre = new Scanner(System.in).nextLine();
        System.out.println("Escriba el teléfono del refugio:");
        String telefono = new Scanner(System.in).nextLine();
        System.out.println("Escriba el email del refugio:");
        String email = new Scanner(System.in).nextLine();

        Refugio refugio = new Refugio(nombre, telefono, email);


        String uri = "http://localhost:8080/refugios";
        HttpResponse<String> response = client.dispararRequestPost(uri, refugio);
        int statusCode = response.statusCode();
        String responseBody = response.body();
        if (statusCode == 200) {
            System.out.println("Refugio registrado exitosamente!");
            System.out.println(responseBody);
        } else if (statusCode == 400 || statusCode == 500) {
            System.out.println("Error al registrar el refugio:");
            System.out.println(responseBody);
        }
    }
}