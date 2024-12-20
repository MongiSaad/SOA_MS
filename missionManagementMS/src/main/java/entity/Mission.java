package entity;

public class Mission {

	private int id;
	private String titre;
	private Integer demandeurId;
	private Integer accepteurId;
	private Integer valideurId;
	private String status;
	private String motif;
	
	public Mission(Integer userId, String titre, String statut, int type) {
        this.titre = titre;
        this.status = statut;
        this.motif = null;
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
	
	public Integer getDemandeurId() {
		return demandeurId;
	}

	public void setDemandeurId(Integer demandeurId) {
		this.demandeurId = demandeurId;
	}

	public Integer getAccepteurId() {
		return accepteurId;
	}

	public void setAccepteurId(Integer accepteurId) {
		this.accepteurId = accepteurId;
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
	
	public String getMotif() {
	    return motif;
	}
	
	public void setMotif(String motif) {
	    this.motif = motif;
	}

}
