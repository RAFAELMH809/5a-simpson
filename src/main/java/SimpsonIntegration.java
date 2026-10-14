import java.util.function.DoubleUnaryOperator;

public class SimpsonIntegration {

    public static double integrar(
            DoubleUnaryOperator funcion,
            double a,
            double b,
            int segmentos) {

        if (funcion == null) {
            throw new IllegalArgumentException("Funcion nula");
        }

        if (segmentos <= 0 || segmentos % 2 != 0) {
            throw new IllegalArgumentException(
                    "Los segmentos deben ser positivos y pares");
        }

        if (!Double.isFinite(a) || !Double.isFinite(b)
                || b < a) {
            throw new IllegalArgumentException(
                    "Intervalo no valido");
        }

        double ancho = (b - a) / segmentos;

        double suma = funcion.applyAsDouble(a)
                + funcion.applyAsDouble(b);

        for (int i = 1; i < segmentos; i++) {
            double x = a + i * ancho;
            int coeficiente = (i % 2 == 0) ? 2 : 4;

            suma += coeficiente
                    * funcion.applyAsDouble(x);
        }

        return (ancho / 3.0) * suma;
    }

    public static double integrarAdaptativo(
            DoubleUnaryOperator funcion,
            double a,
            double b,
            double tolerancia) {

        return integrarAdaptativo(funcion, a, b, tolerancia, 10);
    }

    public static double integrarAdaptativo(
            DoubleUnaryOperator funcion,
            double a,
            double b,
            double tolerancia,
            int segmentosIniciales) {

        if (!Double.isFinite(tolerancia)
                || tolerancia <= 0) {
            throw new IllegalArgumentException(
                    "Tolerancia no valida");
        }

        if (segmentosIniciales <= 0 || segmentosIniciales % 2 != 0) {
            throw new IllegalArgumentException(
                    "Los segmentos deben ser positivos y pares");
        }

        int segmentos = segmentosIniciales;
        double anterior = integrar(
                funcion, a, b, segmentos);

        while (true) {

            if (segmentos > 500000) {
                throw new IllegalStateException(
                        "No se alcanzo la tolerancia");
            }

            segmentos *= 2;

            double actual = integrar(
                    funcion, a, b, segmentos);

            if (!Double.isFinite(actual)) {
                throw new ArithmeticException(
                        "Resultado no finito");
            }

            if (Math.abs(actual - anterior) < tolerancia) {
                return actual;
            }

            anterior = actual;
        }
    }
}
