package modelo;

import java.util.Objects;

public class SituacionRelevante extends EventoPartido {

    private static final long serialVersionUID = 1L;

    private String descripcion;
    private CategoriaSituacion categoria;
    private boolean registrada;

    public SituacionRelevante(int idEvento, Partido partido, int minuto,
                              Corresponsal corresponsal, String descripcion,
                              CategoriaSituacion categoria) {
        super(idEvento, partido, minuto, corresponsal, TipoEvento.SITUACION_RELEVANTE);
        setDescripcion(descripcion);
        this.categoria = Objects.requireNonNull(categoria, "La categoría es obligatoria.");
    }

    @Override
    public void registrar() {
        if (registrada) {
            throw new IllegalStateException("La situación ya fue registrada.");
        }
        partido.agregarEvento(this);
        registrada = true;
    }

    @Override
    public String obtenerDescripcion() {
        return categoria + ": " + descripcion + " (min. " + minuto + ")";
    }

    public final void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }
        if (descripcion.length() > 280) {
            throw new IllegalArgumentException("La descripción no puede superar 280 caracteres.");
        }
        this.descripcion = descripcion.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public CategoriaSituacion getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaSituacion categoria) {
        this.categoria = Objects.requireNonNull(categoria);
    }
}
