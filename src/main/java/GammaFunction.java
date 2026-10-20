public class GammaFunction {

    public static double calcular(double valor) {
        validarValor(valor);

        if (esSemientero(valor)) {
            int indice = (int) (valor - 0.5);
            return calcularGammaSemientero(indice);
        }

        int entero = (int) valor;
        return calcularGammaEntero(entero);
    }

    private static double calcularGammaEntero(int entero) {
        return calcularFactorial(entero );
    }

    private static double calcularGammaSemientero(int indice) {
        double coeficiente = 1.0;
        for (int factor = 1; factor <= indice; factor++) {
            coeficiente *= (2.0 * factor - 1.0) / 2.0;
        }

        return coeficiente * Math.sqrt(Math.PI);
    }

    private static double calcularFactorial(int numero) {
        double resultado = 1.0;
        for (int factor = 2; factor <= numero; factor++) {
            resultado *= factor;
        }
        return resultado;
    }

    private static void validarValor(double valor) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException(
                    "El valor debe ser positivo y finito");
        }

        if (!esEnteroOSemientero(valor)) {
            throw new IllegalArgumentException(
                    "Solo se admiten enteros y semienteros");
        }
    }

    private static boolean esEnteroOSemientero(double valor) {
        return valor % 0.5 == 0.0;
    }

    private static boolean esSemientero(double valor) {
        return valor % 1.0 == 0.5;
    }
}
