package org.INSA.userManagerMS.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.INSA.userManagerMS.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class Controller {
    
	@Value("${bdd.url}")
    private String bddUrl;

    @Value("${bdd.username}")
    private String bddLogin;

    @Value("${bdd.password}")
    private String bddMdp;

    // Ajouter un utilisateur
    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        try (Connection connexion = DriverManager.getConnection(bddUrl, bddLogin, bddMdp)) {
            String req = "INSERT INTO user (username, type, mdp) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connexion.prepareStatement(req)) {
                statement.setString(1, user.getUsername());
                statement.setInt(2, user.getType());
                statement.setString(3, user.getMdp());
                statement.executeUpdate();
                return "Utilisateur ajouté avec succès";
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {  // Code d'erreur pour contrainte unique
                return "Nom d'utilisateur déjà existant";
            } else {
                return "Erreur ajout utilisateur: " + e.getMessage();
            }
        }
    }
    
    //Supprimer un utilisateur 
    @PostMapping("/remove/{username}")
    public String removeUser(@PathVariable String username) {
        try (Connection connexion = DriverManager.getConnection(bddUrl, bddLogin, bddMdp)) {
            String req = "DELETE FROM user WHERE username = ?";
            try (PreparedStatement statement = connexion.prepareStatement(req)) {
                statement.setString(1, username);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    return "Utilisateur supprimé avec succès";
                } else {
                    return "Aucun utilisateur trouvé avec ce nom";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erreur lors de la suppression de l'utilisateur: " + e.getMessage();
        }
    }
    
    //update un utilisateur 
    @PutMapping("/update")
    public String updateUser(@RequestBody User user, @RequestParam String oldUsername) {
        try (Connection connexion = DriverManager.getConnection(bddUrl, bddLogin, bddMdp)) {
            String req = "UPDATE user SET username = ?, mdp = ? WHERE username = ?";
            try (PreparedStatement statement = connexion.prepareStatement(req)) {
                statement.setString(1, user.getUsername());  
                statement.setString(2, user.getMdp());       
                statement.setString(3, oldUsername);         
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    return "Utilisateur mis à jour avec succès";
                } else {
                    return "Aucun utilisateur trouvé avec ce nom";
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {  
                return "Nom d'utilisateur déjà existant";
            } else {
                return "Erreur mise à jour utilisateur: " + e.getMessage();
            }
        }
    }
}
