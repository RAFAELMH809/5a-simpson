import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class App {

    public static void main(String[] args) {
        List<Caso> casos = obtenerCasos(args);
        String archivoSalida = obtenerArchivoSalida(args);

        StringBuilder reporte = new StringBuilder();

        reporte.append("ACTIVIDAD 5A - SIMPSON RULE\n");
        reporte.append("Distribucion t de Student\n\n");

        for (Caso caso : casos) {
            double resultado = Logic.calcularProbabilidad(
                    caso.x(), caso.gradosLibertad());
            String esperado = caso.esperado() == null
                    ? "N/A" : String.format(Locale.US, "%.5f",
                    caso.esperado());
            String linea = String.format(
                    Locale.US,
                    "x=%.4f | dof=%d | Esperado=%s"
                            + " | Calculado=%.5f%n",
                    caso.x(), caso.gradosLibertad(), esperado, resultado);

            System.out.print(linea);
            reporte.append(linea);
        }

        try {
            Output.guardar(
                    archivoSalida,
                    reporte.toString());

            System.out.println(
                    "\nArchivo " + archivoSalida + " generado");

        } catch (IOException e) {
            System.err.println(
                    "Error al guardar: " + e.getMessage());
        }
    }

    private static List<Caso> obtenerCasos(String[] args) {
        List<String> valores = new ArrayList<>();
        for (String argumento : args) {
            if (!argumento.startsWith("--output=")) {
                valores.add(argumento);
            }
        }

        if (valores.isEmpty()) {
            return List.of(
                    new Caso(1.1, 9, 0.35006),
                    new Caso(1.1812, 10, 0.36757),
                    new Caso(2.750, 30, 0.49500));
        }

        if (valores.size() % 2 != 0) {
            throw new IllegalArgumentException(
                    "Las entradas deben venir en pares: x gradosLibertad");
        }

        List<Caso> casos = new ArrayList<>();
        for (int i = 0; i < valores.size(); i += 2) {
            try {
                double x = Double.parseDouble(valores.get(i));
                int grados = Integer.parseInt(valores.get(i + 1));
                casos.add(new Caso(x, grados, null));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Cada entrada debe ser numerica: x gradosLibertad", e);
            }
        }
        return casos;
    }

    private static String obtenerArchivoSalida(String[] args) {
        String archivo = "resultados.txt";
        for (String argumento : args) {
            if (argumento.startsWith("--output=")) {
                archivo = argumento.substring("--output=".length());
                if (archivo.isBlank()) {
                    throw new IllegalArgumentException(
                            "La ruta de salida no puede estar vacia");
                }
            }
        }
        return archivo;
    }

    private record Caso(double x, int gradosLibertad, Double esperado) {
    }
}
