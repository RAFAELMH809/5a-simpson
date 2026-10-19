import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void testGammaCuatro() {
        assertEquals(6.0, GammaFunction.calcular(4.0), 0.00001);
    }

    @Test
    void testGammaCuatroMedios() {
        assertEquals(11.6317283965,
                GammaFunction.calcular(4.5), 0.00001);
    }

    @Test
    void testGammaInvalidaFraccion() {
        assertThrows(IllegalArgumentException.class,
                () -> GammaFunction.calcular(0.75));
    }

    @Test
    void testGammaInvalidaNoFinito() {
        assertThrows(IllegalArgumentException.class,
                () -> GammaFunction.calcular(Double.POSITIVE_INFINITY));
    }

    @Test
    void testGammaInvalidaCero() {
        assertThrows(IllegalArgumentException.class,
                () -> GammaFunction.calcular(0.0));
    }

    @Test
    void testCasoOriginal() {
        assertEquals(0.35006,
                Logic.calcularProbabilidad(1.1, 9), 0.00001);
    }

    @Test
    void testCasoAmpliadoUno() {
        assertEquals(0.35006,
                Logic.calcularProbabilidad(0, 1.1, 9, 10), 0.00001);
    }

    @Test
    void testCasoAmpliadoDos() {
        assertEquals(0.36757,
                Logic.calcularProbabilidad(0, 1.1812, 10, 10), 0.00001);
    }

    @Test
    void testCasoAmpliadoTres() {
        assertEquals(0.49500,
                Logic.calcularProbabilidad(0, 2.750, 30, 10), 0.00001);
    }

    @Test
    void testIntervaloPersonalizado() {
        assertEquals(0.65656,
                Logic.calcularProbabilidad(-1.0, 1.0, 9, 10), 0.00001);
    }

    @Test
    void testLogicaDofInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> Logic.calcularProbabilidad(1.1, 0));
    }

    @Test
    void testLogicaXInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> Logic.calcularProbabilidad(-1.0, 9));
    }

    @Test
    void testLogicaIntervaloInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> Logic.calcularProbabilidad(2, 1, 9, 10));
    }

    @Test
    void testDensidadEnCero() {
        assertEquals(1.0 / Math.PI, Logic.densidad(0.0, 1), 0.00001);
    }

    @Test
    void testDensidadRechazaParametrosInvalidos() {
        assertTrue(Logic.densidad(1.0, 9) > 0);
        assertThrows(IllegalArgumentException.class,
                () -> Logic.densidad(0.0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> Logic.densidad(Double.POSITIVE_INFINITY, 9));
    }

    @Test
    void testLogicaRechazaLimitesYSegmentosInvalidos() {
        assertThrows(IllegalArgumentException.class,
                () -> Logic.calcularProbabilidad(
                        Double.NaN, 1.0, 9, 10));
        assertThrows(IllegalArgumentException.class,
                () -> Logic.calcularProbabilidad(
                        0.0, Double.POSITIVE_INFINITY, 9, 10));
        assertThrows(IllegalArgumentException.class,
                () -> Logic.calcularProbabilidad(0.0, 1.0, 9, 0));
    }

    @Test
    void testIntegracionLineal() {
        assertEquals(16.0,
                SimpsonIntegration.integrar(x -> 2 * x, 0, 4, 4),
                0.00001);
    }

    @Test
    void testIntegracionParametrosInvalidos() {
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrar(x -> x, 0, 4, 3));
    }

    @Test
    void testIntegracionRechazaFuncionSegmentosYLimitesInvalidos() {
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrar(null, 0, 1, 2));
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrar(x -> x, 0, 1, 0));
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrar(
                        x -> x, Double.NaN, 1, 2));
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrar(
                        x -> x, 0, Double.POSITIVE_INFINITY, 2));
    }

    @Test
    void testIntegracionIntervaloInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrar(x -> x, 4, 0, 2));
    }

    @Test
    void testIntegracionAdaptativa() {
        assertEquals(16.0,
                SimpsonIntegration.integrarAdaptativo(
                        x -> 2 * x, 0, 4, 0.00001), 0.00001);
    }

    @Test
    void testIntegracionAdaptativaSegmentosIniciales() {
        assertEquals(16.0,
                SimpsonIntegration.integrarAdaptativo(
                        x -> 2 * x, 0, 4, 0.00001, 4), 0.00001);
    }

    @Test
    void testIntegracionAdaptativaToleranciaInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrarAdaptativo(
                        x -> 2 * x, 0, 4, 0));
    }

    @Test
    void testAdaptativaRechazaToleranciaYSegmentosNoValidos() {
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrarAdaptativo(
                        x -> x, 0, 1, Double.NaN));
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrarAdaptativo(
                        x -> x, 0, 1, 0.00001, 0));
    }

    @Test
    void testIntegracionAdaptativaSegmentosInvalidos() {
        assertThrows(IllegalArgumentException.class,
                () -> SimpsonIntegration.integrarAdaptativo(
                        x -> 2 * x, 0, 4, 0.00001, 3));
    }

    @Test
    void testIntegracionAdaptativaResultadoNoFinito() {
        assertThrows(ArithmeticException.class,
                () -> SimpsonIntegration.integrarAdaptativo(
                        x -> Double.NaN, 0, 1, 0.00001));
    }

    @Test
    void testIntegracionAdaptativaNoAlcanzaTolerancia() {
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
    void testAppModalidadOriginal(@TempDir Path directorio)
            throws IOException {
        Path archivo = directorio.resolve("resultados.txt");
        App.main(new String[] {"1.1", "9", "--output=" + archivo});
        assertTrue(Files.readString(archivo).contains(
                "a=0.0000 | b=1.1000 | dof=9 | n=10"));
    }

    @Test
    void testAppModalidadAmpliada(@TempDir Path directorio)
            throws IOException {
        Path archivo = directorio.resolve("resultados.txt");
        App.main(new String[] {
                "0", "1.1812", "10", "10", "--output=" + archivo
        });
        assertTrue(Files.readString(archivo).contains("Calculado=0.36757"));
    }

    @Test
    void testAppIntervaloPersonalizado(@TempDir Path directorio)
            throws IOException {
        Path archivo = directorio.resolve("resultados.txt");
        App.main(new String[] {
                "1", "2", "12", "12", "--output=" + archivo
        });
        assertTrue(Files.readString(archivo).contains(
                "a=1.0000 | b=2.0000 | dof=12 | n=12"));
    }

    @Test
    void testAppIncluyeEsperadoNoDisponible(@TempDir Path directorio)
            throws IOException {
        Path archivo = directorio.resolve("resultados.txt");
        App.main(new String[] {"1.1", "9", "--output=" + archivo});
        assertTrue(Files.readString(archivo).contains("Esperado=N/A"));
    }

    @Test
    void testAppRechazaCantidadDeArgumentos() {
        assertThrows(IllegalArgumentException.class,
                () -> App.main(new String[] {"1.0", "9", "2.0"}));
    }

    @Test
    void testAppRechazaTiposInvalidos() {
        assertThrows(IllegalArgumentException.class,
                () -> App.main(new String[] {"0", "1", "9", "impar"}));
    }

    @Test
    void testAppRechazaRestriccionesInvalidas() {
        assertThrows(IllegalArgumentException.class,
                () -> App.main(new String[] {"0", "1", "9", "5"}));
    }

    @Test
    void testAppRechazaSalidaVacia() {
        assertThrows(IllegalArgumentException.class,
                () -> App.main(new String[] {"1.0", "9", "--output="}));
    }

    @Test
    void testAppManejaErrorDeSalida(@TempDir Path directorio) {
        assertDoesNotThrow(
                () -> App.main(new String[] {
                        "1.0", "9", "--output=" + directorio
                }));
    }

    @Test
    void testConstructoresPublicos() {
        assertNotNull(new App());
        assertNotNull(new Logic());
        assertNotNull(new SimpsonIntegration());
        assertNotNull(new GammaFunction());
        assertNotNull(new Output());
    }

}
