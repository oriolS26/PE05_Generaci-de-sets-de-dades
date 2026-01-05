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

    public void generarSet() {

        System.out.print("Nom del set: ");
        String nom = sc.nextLine();

        System.out.print("Tipus de dada (1-Enters, 2-Decimals, 3-Text): ");
        String tipusOp = sc.nextLine();

        String tipus = tipusOp.equals("1") ? "números enters"
                : tipusOp.equals("2") ? "números decimals"
                : "text";

        System.out.print("Quantitat: ");
        int quantitat = Integer.parseInt(sc.nextLine());

        System.out.print("Descripció de les dades: ");
        String desc = sc.nextLine();

        // Construcció del prompt

        String prompt = "Genera una llista Java amb " + quantitat + " elements de tipus " + tipus
                + ". Dades: " + desc + ". Retorna només la llista.";

        try {
            String apiKey = System.getenv("GEMINI_API_KEY");
            Client client = Client.builder().apiKey(apiKey).build();
            GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", prompt, null);
            String text = response.text().trim();

            if (!text.startsWith("[") || !text.endsWith("]")) {
                System.out.println("La resposta no és una llista vàlida.");
                return;
            }

            text = text.substring(1, text.length() - 1);
            List<String> llista = text.isEmpty() ? new ArrayList<>() : Arrays.asList(text.split("\\s*,\\s*"));

            datasets.put(nom, llista);
            System.out.println("Set \"" + nom + "\" guardat correctament!");

        } catch (Exception e) {
            System.out.println("Error cridant a Gemini.");
        }
    }

    public void visualitzarSets() {
        if (datasets.isEmpty()) {
            System.out.println("No hi ha sets disponibles.");
            return;
        }

        System.out.println("1. Visualitzar un set");
        System.out.println("2. Visualitzar tots");
        System.out.print("Opció: ");
        String op = sc.nextLine();

        if (op.equals("1")) {
            System.out.println("Sets disponibles:");
            datasets.keySet().forEach(n -> System.out.println("- " + n));
            System.out.print("Quin vols visualitzar?: ");
            String nom = sc.nextLine();
            List<String> llista = datasets.get(nom);
            if (llista != null) System.out.println(nom + ": " + llista);
            else System.out.println("Aquest set no existeix.");
        } else if (op.equals("2")) {
            datasets.forEach((nom, llista) -> System.out.println(nom + ": " + llista));
        } else {
            System.out.println("Opció no vàlida.");
        }
    }
    
        public void esborrarSets() {
        if (datasets.isEmpty()) {
            System.out.println("No hi ha sets a esborrar.");
            return;
        }

        System.out.println("1. Esborrar un set");
        System.out.println("2. Esborrar tots");
        System.out.print("Opció: ");
        String op = sc.nextLine();

        if (op.equals("1")) {
            System.out.println("Sets disponibles:");
            datasets.keySet().forEach(n -> System.out.println("- " + n));
            System.out.print("Quin vols esborrar?: ");
            String nom = sc.nextLine();
            if (datasets.remove(nom) != null) System.out.println("Set eliminat correctament.");
            else System.out.println("Aquest set no existeix.");
        } else if (op.equals("2")) {
            datasets.clear();
            System.out.println("Tots els sets han estat eliminats.");
        } else {
            System.out.println("Opció no vàlida.");
        }
    }
}
