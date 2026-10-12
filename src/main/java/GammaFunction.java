public class GammaFunction {

    public static double calcular(double valor) {

        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException(
                    "El valor debe ser positivo y finito");
        }

        if (valor == 1.0) {
            return 1.0;
        }

        if (valor == 0.5) {
            return Math.sqrt(Math.PI);
        }

        if (valor < 1.0) {
            throw new IllegalArgumentException(
                    "Solo se admiten enteros y semienteros");
        }

        return (valor - 1.0)
                * calcular(valor - 1.0);
    }
}
