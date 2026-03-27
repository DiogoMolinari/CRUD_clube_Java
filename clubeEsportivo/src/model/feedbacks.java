package src.model;

public class feedbacks {
    private int idFeed, idReclamante;
    private String tipoFeedback, textoFeedback;

    public feedbacks(int idFeed, String tipoFeedback, String textoFeedback, int idReclamante) {
        this.idFeed = idFeed;
        this.tipoFeedback = tipoFeedback;
        this.textoFeedback = textoFeedback;
        this.idReclamante = idReclamante;
    }

    public int getIdFeed() { return idFeed; }
    public void setIdFeed(int idFeed) { this.idFeed = idFeed; }

    public String getTipoFeedback() { return tipoFeedback; }
    public void setTipoFeedback(String tipoFeedback) { this.tipoFeedback = tipoFeedback; }

    public String getTextoFeedback() { return textoFeedback; }
    public void setTextoFeedback(String textoFeedback) { this.textoFeedback = textoFeedback; }

    public int getIdReclamante() { return idReclamante; }
    public void setIdReclamante(int idReclamante) { this.idReclamante = idReclamante; }
}
