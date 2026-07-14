package vista;

import java.awt.Color;

/**
 * Paleta de colores centralizada para toda la interfaz de FidESPN.
 * Inspirada en el césped (verdes), el balón y el uniforme árbitro (blanco/negro).
 */
public final class PaletaColores {

    private PaletaColores() {
    }

    /** Verde césped — acciones primarias, botones principales, resaltados */
    public static final Color VERDE_CESPED = new Color(0x1A, 0xB8, 0x00);

    /** Verde medio — hover, elementos secundarios, badges */
    public static final Color VERDE_MEDIO = new Color(0x29, 0x85, 0x1B);

    /** Verde oscuro — encabezados, texto sobre fondo claro, bordes de énfasis */
    public static final Color VERDE_OSCURO = new Color(0x27, 0x52, 0x21);

    /** Blanco — fondos de tarjetas y contenido */
    public static final Color BLANCO = Color.WHITE;

    /** Negro casi puro — texto principal, encabezados de alto contraste */
    public static final Color NEGRO = new Color(0x0A, 0x0A, 0x0A);

    /** Gris claro para fondos generales de ventana */
    public static final Color FONDO_VENTANA = new Color(0xF5, 0xF7, 0xF5);

    /** Gris para texto secundario / descripciones */
    public static final Color TEXTO_SECUNDARIO = new Color(0x5F, 0x69, 0x64);

    /** Gris muy claro para bordes sutiles de tarjetas */
    public static final Color BORDE_TARJETA = new Color(0xD7, 0xE0, 0xD8);

    /** Rojo para mensajes de error (tarjeta roja, guiño futbolero) */
    public static final Color ROJO_ERROR = new Color(0xC0, 0x2B, 0x2B);
}
