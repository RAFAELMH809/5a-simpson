import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpsonRuleTest {

    @Test
    void testGammaUno() {
        assertEquals(1.0, GammaFunction.calcular(1.0), 0.00001);
    }

    @Test
    void testGammaMedio() {
        assertEquals(Math.sqrt(Math.PI),
                GammaFunction.calcular(0.5), 0.00001);
    }

    @Test
    void testGammaCinco() {
        assertEquals(24.0, GammaFunction.calcular(5.0), 0.00001);
    }

    @Test
    void testGammaInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> GammaFunction.calcular(-1.0));
        assertThrows(IllegalArgumentException.class,
                () -> GammaFunction.calcular(Double.NaN));
        assertThrows(IllegalArgumentException.class,
                () -> GammaFunction.calcular(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class,
                () -> GammaFunction.calcular(0.75));
    }

    @Test
    void testCasosObligatorios() {
        assertEquals(0.35006,
                Logic.calcularProbabilidad(1.1, 9), 0.00001);
        assertEquals(0.36757,
                Logic.calcularProbabilidad(1.1812, 10), 0.00001);
        assertEquals(0.49500,
                Logic.calcularProbabilidad(2.750, 30), 0.00001);
    }

    @Test
    void testProbabilidadCero() {
        assertEquals(0.0, Logic.calcularProbabilidad(0, 9), 0.00001);
    }

    @Test
    void testLogicaInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> Logic.calcularProbabilidad(1.1, 0));
        assertThrows(IllegalArgumentException.class,
                () -> Logic.calcularProbabilidad(-1.0, 9));
        assertThrows(IllegalArgumentException.class,
                () -> Logic.calcularProbabilidad(Double.NaN, 9));
        assertThrows(IllegalArgumentException.class,
                () -> Logic.calcularProbabilidad(1.0, -1));
        assertEquals(1.0 / Math.PI, Logic.densidad(0.0, 1), 0.00001);
        assertTrue(Logic.densidad(1.0, 9) > 0);
        assertThrows(IllegalArgumentException.class,
                () -> Logic.densidad(0.0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> Logic.densidad(Double.POSITIVE_INFINITY, 9));
    }

    @Test
    void testFuncionLineal() {
        assertEquals(16.0,
                SimpsonIntegration.integrar(x -> 2 * x, 0, 4, 4),
                0.00001);
    }

    @Test
    void testFuncionConstante() {
        assertEquals(20.0,
                SimpsonIntegration.integrar(x -> 5, 0, 4, 4),
                0.00001);
    }

    @Test
    void testIntegracionInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrar(x -> 2 * x, 0, 4, 3));
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrar(x -> 2 * x, 0, 4, 0));
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrar(null, 0, 4, 2));
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrar(x -> x, 4, 0, 2));
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrar(
                        x -> x, Double.NaN, 1, 2));
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrar(
                        x -> x, 0, Double.POSITIVE_INFINITY, 2));
    }

    @Test
    void testIntegracionAdaptativa() {
        assertEquals(16.0,
                SimpsonIntegration.integrarAdaptativo(
                        x -> 2 * x, 0, 4, 0.00001), 0.00001);
    }

    @Test
    void testIntegracionAdaptativaInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrarAdaptativo(
                        x -> 2 * x, 0, 4, 0));
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrarAdaptativo(
                        x -> 2 * x, 0, 4, Double.NaN));
        assertThrows(ArithmeticException.class,
                () -> SimpsonIntegration.integrarAdaptativo(
                        x -> Double.NaN, 0, 1, 0.00001));
        double[] llamadas = {0};
        assertThrows(IllegalStateException.class,
                () -> SimpsonIntegration.integrarAdaptativo(
                        x -> llamadas[0]++ % 2, 0, 1, 1e-300));
    }

    @Test
    void testGuardar(@TempDir Path directorio) throws IOException {
        Path archivo = directorio.resolve("resultado.txt");
        Output.guardar(archivo.toString(), "contenido");
        assertEquals("contenido", Files.readString(archivo));
    }

    @Test
    void testGuardarEnRutaInvalida(@TempDir Path directorio) {
        assertThrows(IOException.class,
                () -> Output.guardar(directorio.toString(), "contenido"));
    }

    @Test
    void testAppUsaCasosPredeterminados(@TempDir Path directorio)
            throws IOException {
        Path archivo = directorio.resolve("resultados.txt");
        App.main(new String[] {"--output=" + archivo});
        assertTrue(Files.readString(archivo).contains("Calculado=0.35006"));
    }

    @Test
    void testAppAceptaValoresVariables(@TempDir Path directorio)
            throws IOException {
        Path archivo = directorio.resolve("resultados.txt");
        App.main(new String[] {
                "0.5", "8", "2.0", "12", "--output=" + archivo
        });
        String contenido = Files.readString(archivo);
        assertTrue(contenido.contains("x=0.5000 | dof=8"));
        assertTrue(contenido.contains("x=2.0000 | dof=12"));
        assertTrue(contenido.contains("Esperado=N/A"));
    }

    @Test
    void testAppValidaArgumentos() {
        assertThrows(IllegalArgumentException.class,
                () -> App.main(new String[] {"1.0"}));
        assertThrows(IllegalArgumentException.class,
                () -> App.main(new String[] {"abc", "9"}));
        assertThrows(IllegalArgumentException.class,
                () -> App.main(new String[] {"1.0", "9", "--output="}));
        assertThrows(IllegalArgumentException.class,
                () -> App.main(new String[] {"-1.0", "9"}));
    }

    @Test
    void testAppManejaErrorDeSalida(@TempDir Path directorio) {
        App.main(new String[] {"1.0", "9", "--output=" + directorio});
    }

    @Test
    void testInstanciaClasesPrincipales() {
        assertTrue(new App() != null);
        assertTrue(new Logic() != null);
        assertTrue(new SimpsonIntegration() != null);
        assertTrue(new GammaFunction() != null);
        assertTrue(new Output() != null);
    }
}
