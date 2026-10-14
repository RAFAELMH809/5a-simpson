public class Logic {

    public static double densidad(
            double x, int gradosLibertad) {

        if (gradosLibertad <= 0) {
            throw new IllegalArgumentException(
                    "Grados de libertad no validos");
        }

        if (!Double.isFinite(x)) {
            throw new IllegalArgumentException(
                    "El valor x no es valido");
        }

        double v = gradosLibertad;

        double numerador =
                GammaFunction.calcular((v + 1.0) / 2.0);

        double denominador =
                Math.sqrt(v * Math.PI)
                * GammaFunction.calcular(v / 2.0);

        double exponente = -(v + 1.0) / 2.0;

        return (numerador / denominador)
                * Math.pow(1.0 + (x * x) / v, exponente);
    }

    public static double calcularProbabilidad(
            double x, int gradosLibertad) {

        return calcularProbabilidad(0.0, x, gradosLibertad, 10);
    }

    public static double calcularProbabilidad(
            double a, double b, int gradosLibertad, int segmentosIniciales) {

        if (!Double.isFinite(a) || !Double.isFinite(b) || b < a) {
            throw new IllegalArgumentException(
                    "Los limites deben ser finitos y b debe ser mayor o igual que a");
        }

        if (gradosLibertad <= 0) {
            throw new IllegalArgumentException(
                    "Grados de libertad no validos");
        }

        if (segmentosIniciales <= 0 || segmentosIniciales % 2 != 0) {
            throw new IllegalArgumentException(
                    "Los segmentos deben ser positivos y pares");
        }

        return SimpsonIntegration.integrarAdaptativo(
                t -> densidad(t, gradosLibertad),
                a, b, 0.00001, segmentosIniciales
        );
    }
}
