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

        if (!Double.isFinite(x) || x < 0) {
            throw new IllegalArgumentException(
                    "x debe ser no negativo y finito");
        }

        if (gradosLibertad <= 0) {
            throw new IllegalArgumentException(
                    "Grados de libertad no validos");
        }

        return SimpsonIntegration.integrarAdaptativo(
                t -> densidad(t, gradosLibertad),
                0.0,
                x,
                0.00001
        );
    }
}
