import java.io.IOException;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

public class App {

    public static void main(String[] args) {
        Caso caso = obtenerCaso(args);
        String archivoSalida = obtenerArchivoSalida(args);

        StringBuilder reporte = new StringBuilder();

        reporte.append("ACTIVIDAD 5A - SIMPSON RULE\n");
        reporte.append("Distribucion t de Student\n\n");

        double resultado = Logic.calcularProbabilidad(
                caso.a(), caso.b(), caso.gradosLibertad(),
                caso.segmentosIniciales());
        String esperado = "N/A";
        String linea = String.format(
                Locale.US,
                "a=%.4f | b=%.4f | dof=%d | n=%d"
                        + " | Esperado=%s | Calculado=%.5f%n",
                caso.a(), caso.b(), caso.gradosLibertad(),
                caso.segmentosIniciales(), esperado, resultado);

        System.out.print(linea);
        reporte.append(linea);

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

    private static Caso obtenerCaso(String[] args) {
        List<String> valores = new ArrayList<>();
        for (String argumento : args) {
            if (!argumento.startsWith("--output=")) {
                valores.add(argumento);
            }
        }

        if (valores.size() != 2 && valores.size() != 4) {
            throw new IllegalArgumentException(
                    "Debes proporcionar 2 parametros (x dof) o 4 "
                            + "(a b dof n)");
        }

        try {
            if (valores.size() == 2) {
                double x = Double.parseDouble(valores.get(0));
                int grados = Integer.parseInt(valores.get(1));
                return new Caso(0.0, x, grados, 10);
            }

            double a = Double.parseDouble(valores.get(0));
            double b = Double.parseDouble(valores.get(1));
            int grados = Integer.parseInt(valores.get(2));
            int segmentos = Integer.parseInt(valores.get(3));
            return new Caso(a, b, grados, segmentos);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Los parametros deben tener tipos numericos validos", e);
        }
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

    private record Caso(
            double a,
            double b,
            int gradosLibertad,
            int segmentosIniciales) {
    }
}
