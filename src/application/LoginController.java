package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginController {

	// --- START Declarations ---
	HWID hwidUtil = new HWID();
	KeyGenerator keygen = new KeyGenerator();

	@FXML
	private Button loginButton;

	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Label errorLabel;
	// --- END Declarations ---

	@FXML
	void login(ActionEvent event) {
		// Make error label invisible if it isn't already
		errorLabel.setVisible(false);

		// Get the users hwid
		String hwid = "";
		try {
			hwid = hwidUtil.getHWID();
		} catch (Exception e) {
			errorLabel.setVisible(true);
			errorLabel.setText("Error: Could not generate HWID.");
			return;
		}

		String username = usernameField.getText();
		String password = passwordField.getText();

		// Check that text was entered
		if (username.equals("") || password.equals("")) {
			errorLabel.setVisible(true);
			errorLabel.setText("Error: Username/Password not present.");
			return;
		}

		// Double check hwid is present
		if (hwid.equals("")) {
			errorLabel.setVisible(true);
			errorLabel.setText("Error: Could not generate HWID.");
			return;
		}

		// ========= If we are at this point we have a username, password, and hwid =========

		// Set the username
		KeyManager.setUsername(username);
		
		System.out.println("hwid: " + hwid + "\nusername:" + username + "\npassword: " + password);

		// Generate and set the private key
		KeyManager.setKey(keygen.generateKey(username, password, hwid));

		// Open up the dashboard and close login window 
		Stage login = (Stage) loginButton.getScene().getWindow();
		login.close();
		
        try {
        	Pane pane = (Pane) FXMLLoader.load(Main.class.getResource("Dashboard.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

}
