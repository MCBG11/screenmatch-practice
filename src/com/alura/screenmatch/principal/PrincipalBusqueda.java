package com.alura.screenmatch.principal;

import com.alura.screenmatch.excepcion.ErrorEnConversionDeDuracionExcepcion;
import com.alura.screenmatch.modelos.Titulo;
import com.alura.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrincipalBusqueda {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner reading = new Scanner(System.in);
        List<Titulo> tituloslist = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create(); // new librari, insted of using nwe Gson we used new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create(); for the record TituloOmdb

        while (true) {
            System.out.println("Write the movie title");
            var searching = reading.nextLine();
            if (searching.equalsIgnoreCase("salir")) {
                break;
            }

            String path = "https://www.omdbapi.com/?t=" +
                    searching.replace(" ", "+") +
                    "&apikey=149657dc";

            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(path))
                        .build();

                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());

                String json = response.body();
                System.out.println(json);//to look for lilo & stitch you should write lilo+%26+stitch




                TituloOmdb miTituloOmdb = gson.fromJson(json, TituloOmdb.class);
                System.out.println(miTituloOmdb);// its needed to get a toString in Titulo class, but not for print miTituloOmdb

                //after create TituloOmdb to send JSON to Titulo class we try to send TituloOmdb as an objet to Title, but a new constructor needs to be created,
                // cause the first one needs nombre and fechaDeLanzamiento as parameters, so:

                Titulo miTitulo = new Titulo(miTituloOmdb);
                System.out.println("Título ya convertido: " + miTitulo);

                //FileWriter escritura = new FileWriter("peliculas.txt");
                //escritura.write(miTitulo.toString());
                //escritura.close();

                tituloslist.add(miTitulo);
            } catch (NumberFormatException e) {
                System.out.println("Ocurrió un error");
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error en la URI, verifique la dirección");
                System.out.println(e.getMessage());
            } catch (
                    ErrorEnConversionDeDuracionExcepcion e) {// clase madre de todas la excepciones, no es específica ante el tipo de error
                System.out.println(e.getMessage());
            }
        }
        System.out.println(tituloslist);

        FileWriter escritura = new FileWriter("tituloslist.json");
        escritura.write(gson.toJson(tituloslist));
        escritura.close();//siempre

        System.out.println("Finalizó la ejecución del programa ");
    }
}
