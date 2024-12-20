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
	
    //Insérer une mission par un demandeur (client ou bénévole)
    @PostMapping("/addByDemandeur")
    public boolean addMissionByDemandeur(@RequestBody Mission missionRequest) {
        System.out.println("Début de la méthode addMissionByDemandeur");

        try (Connection connexion = DriverManager.getConnection(bddUrl, bddLogin, bddMdp)) {
            
            missionRequest.setStatus("Waiting to be processed");

            String req = "INSERT INTO mission (titre, demandeurId, status) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connexion.prepareStatement(req)) {
                statement.setString(1, missionRequest.getTitre());
                statement.setInt(2, missionRequest.getDemandeurId());
                statement.setString(3, missionRequest.getStatus());
                statement.executeUpdate();
                System.out.println("Mission insérée avec succès dans la base de données par un demandeur");
            }

            System.out.println("Mission créée : " + missionRequest);
            return true;

        } catch (SQLException e) {
            System.out.println("Erreur SQL dans addMissionByDemandeur : " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Erreur générale dans addMissionByDemandeur : " + e.getMessage());
            return false;
        }
    }

    //Insérer une mission par un valideur
    @PostMapping("/addByValideur")
    public boolean addMissionByValideur(@RequestBody Mission missionRequest) {
        System.out.println("Début de la méthode addMissionByValideur");

        try (Connection connexion = DriverManager.getConnection(bddUrl, bddLogin, bddMdp)) {

            missionRequest.setStatus("Accepted");

            String req = "INSERT INTO mission (titre, demandeurId, valideurId, status) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connexion.prepareStatement(req)) {
                statement.setString(1, missionRequest.getTitre());
                statement.setInt(2, missionRequest.getDemandeurId());
                statement.setInt(3, missionRequest.getValideurId());
                statement.setString(4, missionRequest.getStatus());
                statement.executeUpdate();
                System.out.println("Mission insérée avec succès dans la base de données par un valideur");
            }

            System.out.println("Mission créée : " + missionRequest);
            return true;

        } catch (SQLException e) {
            System.out.println("Erreur SQL dans addMissionByValideur : " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Erreur générale dans addMissionByValideur : " + e.getMessage());
            return false;
        }
    }
    
    //Mettre à jour le status d'une mission par un valideur
    @PutMapping("/statusupdate")
    public boolean updateMissionStatus(@RequestParam int missionId, @RequestParam int valideurId, @RequestParam String status, @RequestParam String motif) {
        try (Connection connexion = DriverManager.getConnection(bddUrl, bddLogin, bddMdp)) {
            System.out.println("Connexion à la base de données réussie");

            String req = "UPDATE mission SET status = ?, motif = ?, valideurId = ? WHERE id = ?";
            
            try (PreparedStatement statement = connexion.prepareStatement(req)) {
                statement.setString(1, status);        
                statement.setString(2, motif);         
                statement.setInt(3, valideurId);       
                statement.setInt(4, missionId);        

                int rowsUpdated = statement.executeUpdate();  

                if (rowsUpdated > 0) {
                    System.out.println("Mission mise à jour avec succès. Nouveau statut : " + status);
                    return true;
                } else {
                    System.out.println("Mission non trouvée pour l'ID : " + missionId);
                    return false; 
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL dans la mise à jour de la mission : " + e.getMessage());
            return false;  
        } catch (Exception e) {
            System.out.println("Erreur générale dans la mise à jour de la mission : " + e.getMessage());
            return false;  
        }
    }
    
    //Méthode pour s'associer à une mission 
    @PutMapping("/assignUserToMission")
    public boolean assignUserToMission(@RequestParam int missionId, @RequestParam int userId) {
        try (Connection connexion = DriverManager.getConnection(bddUrl, bddLogin, bddMdp)) {
            String updateMissionQuery = "UPDATE mission SET accepteurId = ?, status = 'Accepted' WHERE id = ?";

            try (PreparedStatement updateStatement = connexion.prepareStatement(updateMissionQuery)) {
                updateStatement.setInt(1, userId);
                updateStatement.setInt(2, missionId);

                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Mission mise à jour avec succès : Mission ID = " + missionId);
                    return true;
                } else {
                    System.out.println("La mission n'a pas pu être mise à jour. Vérifiez les conditions.");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL dans l'assignation de l'utilisateur à la mission : " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Erreur générale dans l'assignation de l'utilisateur à la mission : " + e.getMessage());
            return false;
        }
    }

}
