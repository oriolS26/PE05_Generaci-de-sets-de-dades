package com.example;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import java.util.*;

public class App {
    public Map<String, List<String>> datasets = new HashMap<>();
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        App program = new App();
        program.inici();
        // Opció 1: deixar que el client llegeixi la clau de GEMINI_API_KEY / GOOGLE_API_KEY
        // Client client = new Client();

        // Opció 2: passar explícitament l'API key (també la llegeixo de l'entorn)
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Error: falta la variable d'entorn GEMINI_API_KEY");
            System.err.println("Si us plau, exporta GEMINI_API_KEY amb la teva API key de Gemini.");
            return;
        }

        Client client = Client.builder()
                .apiKey(apiKey)  // usem l'API key del Gemini Developer API
                .build();

        // Prompt a enviar al model
        String prompt = "Donem un conjunt de dades que es puguin importar en format llista i que continguin mamifers.";

        try {
            // "gemini-2.5-flash" és un dels models recomanats per text :contentReference[oaicite:4]{index=4}
            GenerateContentResponse response =
                    client.models.generateContent("gemini-2.5-flash", prompt, null);

            System.out.println("=== Resposta de Gemini ===");
            System.out.println(response.text());
        } catch (Exception e) {
            System.err.println("S'ha produït un error cridant a Gemini:");
            e.printStackTrace();
        }
    }

     public void inici() {
        String opcio;
        do {
            System.out.println("\n--- Generador de Sets de Dades ---");
            System.out.println("1. Generar un nou set de dades");
            System.out.println("2. Visualitzar un o tots els sets de dades");
            System.out.println("3. Esborrar un o tots els sets de dades");
            System.out.println("4. Sortir");
            System.out.print("Tria una opció: ");
            opcio = sc.nextLine();

            switch (opcio) {
                case "1":
                generarSet();
                case "2":
                visualitzarSets();
                case "3":
                esborrarSets();
                case "4":
                System.out.println("Tancant el programa. Fins aviat!");
                default:
                System.out.println("Opció no vàlida");
            }
        } while (!opcio.equals("4"));
    }
}
