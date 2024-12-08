package entity;

public class Mission {

	private int id;
	private String titre;
	private Integer benevoleId;
	private Integer clientId;
	private Integer valideurId;
	private String status;
	private String avisDemandeur;
	private String avisBenevole;
	private String motif;
	
	public Mission(Integer userId, String titre, String statut, int type) {
        this.titre = titre;
        this.status = statut;
        this.avisDemandeur = null;
        this.avisBenevole = null;
        this.motif = null;

        // Détermine quel ID assigner en fonction du type d'utilisateur
        switch (type) {
            case -1: // Client
                this.clientId = userId;
                this.benevoleId = null;
                this.valideurId = null;
                break;
            case 0: // Valideur
                this.valideurId = userId;
                this.clientId = null;
                this.benevoleId = null;
                break;
            case 1: // Bénévole
                this.benevoleId = userId;
                this.valideurId = null;
                this.clientId = null;
                break;
            default:
                throw new IllegalArgumentException("Type d'utilisateur invalide");
        }
    }
	
	public int getId() {
	    return id;
	}
	
	public void setId(int id) {
	    this.id = id;
	}
	
	public String getTitre() {
	    return titre;
	}
	
	public void setTitre(String titre) {
	    this.titre = titre;
	}
	
	public Integer getBenevoleId() {
	    return benevoleId;
	}
	
	public void setBenevoleId(Integer benevoleId) {
	    this.benevoleId = benevoleId;
	}
	
	public Integer getClientId() {
	    return clientId;
	}
	
	public void setClientId(Integer clientId) {
	    this.clientId = clientId;
	}
	
	public Integer getValideurId() {
	    return valideurId;
	}
	
	public void setValideurId(Integer valideurId) {
	    this.valideurId = valideurId;
	}
	
	public String getStatus() {
	    return status;
	}
	
	public void setStatus(String status) {
	    this.status = status;
	}
	
	public String getAvisDemandeur() {
	    return avisDemandeur;
	}
	
	public void setAvisDemandeur(String avisDemandeur) {
	    this.avisDemandeur = avisDemandeur;
	}
	
	public String getAvisBenevole() {
	    return avisBenevole;
	}
	
	public void setAvisBenevole(String avisBenevole) {
	    this.avisBenevole = avisBenevole;
	}
	
	public String getMotif() {
	    return motif;
	}
	
	public void setMotif(String motif) {
	    this.motif = motif;
	}

}
