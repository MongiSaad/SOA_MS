package org.INSA.missionManagementMS.controleur;

import java.sql.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import entity.Mission;

@RestController
@RequestMapping("/missions")
public class Controller {
	
	@Value("${bdd.url}")
    private String bddUrl;

    @Value("${bdd.username}")
    private String bddLogin;

    @Value("${bdd.password}")
    private String bddMdp;
	
    // Insérer une mission
    @PostMapping("/add")
    public Mission addMission(@RequestBody Mission missionRequest) {
        System.out.println("Début de la méthode addMission");

        try (Connection connexion = DriverManager.getConnection(bddUrl, bddLogin, bddMdp)) {
            System.out.println("Connexion à la base de données réussie");

            int userId = 0;
            int type = -2;

            if (missionRequest.getBenevoleId() != null) {
                userId = missionRequest.getBenevoleId();
                type = 1;  // Type Bénévole
            } else if (missionRequest.getClientId() != null) {
                userId = missionRequest.getClientId();
                type = -1;  // Type Client
            } else if (missionRequest.getValideurId() != null) {
                userId = missionRequest.getValideurId();
                type = 0;  // Type Valideur
            }

            String titre = missionRequest.getTitre();

            missionRequest.setStatus("waiting");

            String column = "";
            if (type == -1) {
                column = "client_id";
            } else if (type == 0) {
                column = "valideur_id";
            } else if (type == 1) {
                column = "benevole_id";
            } else {
                System.out.println("Type d'utilisateur inconnu : " + type);
                return null;
            }

            String req = "INSERT INTO mission (titre, " + column + ", status) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connexion.prepareStatement(req)) {
                statement.setString(1, titre);
                statement.setInt(2, userId);
                statement.setString(3, missionRequest.getStatus());
                statement.executeUpdate();
                System.out.println("Mission insérée avec succès dans la base de données");
            }

            System.out.println("Mission créée : " + missionRequest);
            return missionRequest;

        } catch (SQLException e) {
            System.out.println("Erreur SQL dans addMission : " + e.getMessage());
            return null;  
        } catch (Exception e) {
            System.out.println("Erreur générale dans addMission : " + e.getMessage());
            return null;
        }
    }
    
    @PostMapping("/statusupdate")
    public Mission updateMissionStatus(@RequestBody Mission missionRequest) {
        try (Connection connexion = DriverManager.getConnection(bddUrl, bddLogin, bddMdp)) {
            System.out.println("Connexion à la base de données réussie");

            int missionId = missionRequest.getId();
            String status = missionRequest.getStatus();
            String motif = missionRequest.getMotif();

            String req = "UPDATE mission SET status = ?, motif = ? WHERE id = ?";
            try (PreparedStatement statement = connexion.prepareStatement(req)) {
                statement.setString(1, status);
                statement.setString(2, motif); 
                statement.setInt(3, missionId);

                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Mission mise à jour avec succès. Nouveau statut : " + status);
                    missionRequest.setStatus(status);  
                    missionRequest.setMotif(motif);    
                    return missionRequest;             
                } else {
                    System.out.println("Mission non trouvée pour l'ID : " + missionId);
                    return null;  
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL dans la mise à jour de la mission : " + e.getMessage());
            return null;  
        } catch (Exception e) {
            System.out.println("Erreur générale dans la mise à jour de la mission : " + e.getMessage());
            return null;  
        }
    }
    
    @PostMapping("/assignUserToMission")
    public Mission assignUserToMission(@RequestBody Mission missionRequest) {
        try (Connection connexion = DriverManager.getConnection(bddUrl, bddLogin, bddMdp)) {
            System.out.println("Connexion à la base de données réussie");

            int missionId = missionRequest.getId();
            Integer clientId = missionRequest.getClientId();
            Integer benevoleId = missionRequest.getBenevoleId();

            String columnToUpdate = null;
            Integer userIdToUpdate = null;

            if (clientId != null) {
                columnToUpdate = "client_id";
                userIdToUpdate = clientId;
            } else if (benevoleId != null) {
                columnToUpdate = "benevole_id";
                userIdToUpdate = benevoleId;
            } else {
                System.out.println("Aucun ID utilisateur fourni dans la requête.");
                return null;
            }

            String updateMissionQuery = "UPDATE mission SET " + columnToUpdate + " = ?, status = 'Accepted' WHERE id = ?";

            try (PreparedStatement updateStatement = connexion.prepareStatement(updateMissionQuery)) {
                updateStatement.setInt(1, userIdToUpdate);
                updateStatement.setInt(2, missionId);

                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Mission mise à jour avec succès : Mission ID = " + missionId);
                    missionRequest.setStatus("Accepted");

                    if ("client_id".equals(columnToUpdate)) {
                        missionRequest.setClientId(clientId);
                    } else {
                        missionRequest.setBenevoleId(benevoleId);
                    }

                    return missionRequest;
                } else {
                    System.out.println("La mission n'a pas pu être mise à jour. Vérifiez les conditions.");
                    return null;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL dans l'assignation de l'utilisateur à la mission : " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Erreur générale dans l'assignation de l'utilisateur à la mission : " + e.getMessage());
            return null;
        }
    }
}