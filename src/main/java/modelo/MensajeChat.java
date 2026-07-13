package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MensajeChat implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idMensaje;
    private String texto;
    private Fanatico fanatico;
    private Partido partido;
    private LocalDateTime timestamp;
    private boolean enviado;

    public MensajeChat(int idMensaje, String texto, Fanatico fanatico, Partido partido) {
        this.idMensaje = idMensaje;
        setTexto(texto);
        this.fanatico = Objects.requireNonNull(fanatico, "El fanático es obligatorio.");
        this.partido = Objects.requireNonNull(partido, "El partido es obligatorio.");
        this.timestamp = LocalDateTime.now();
    }

    public void enviar() {
        if (enviado) {
            throw new IllegalStateException("El mensaje ya fue enviado.");
        }
        partido.agregarMensaje(this);
        enviado = true;
    }

    public String formatear() {
        String hora = timestamp.format(DateTimeFormatter.ofPattern("HH:mm"));
        return fanatico.getNombre() + " [" + hora + "]: " + texto;
    }

    public int getIdMensaje() {
        return idMensaje;
    }

    public String getTexto() {
        return texto;
    }

    public final void setTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío.");
        }
        if (texto.length() > 200) {
            throw new IllegalArgumentException("El mensaje no puede superar 200 caracteres.");
        }
        this.texto = texto.trim();
    }

    public Fanatico getFanatico() {
        return fanatico;
    }

    public Partido getPartido() {
        return partido;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isEnviado() {
        return enviado;
    }

    @Override
    public String toString() {
        return formatear();
    }
}
