package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

	@FXML
	private Label usernameLabel;

	@FXML
	private Label keyLabel;

	@FXML
	private Label errorLabel;

	@FXML
	private Button launchButton;

	@FXML
	private Button makeButton;

	@FXML
	private ListView<String> appList;

	ArrayList<File> apps;

	@FXML
	void launch(ActionEvent event) {
		errorLabel.setVisible(false);

		// Get currently selected item
		File launchFile = null;
		try {
			launchFile = apps.get(appList.getSelectionModel().getSelectedIndex());
		} catch (ArrayIndexOutOfBoundsException e) {
			errorLabel.setText("Error: Please select an app to launch.");
			errorLabel.setVisible(true);
			return;
		}

		// Check if an item was selected
		if (!launchFile.exists()) {
			errorLabel.setText("Error: File selected does not exist.");
			errorLabel.setVisible(true);
			return;
		}

		// DEBUG
		System.out.println("Selected file: " + launchFile.getName());

		// Decrypt
		File outputFile = new File(launchFile.getName().replace(".locked", ".jar"));

		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(cipher.DECRYPT_MODE, KeyManager.getKey());

			FileInputStream inputStream = new FileInputStream(launchFile);
			byte[] inputBytes = new byte[(int) launchFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void make(ActionEvent event) {
		try {
			Pane pane = (Pane) FXMLLoader.load(Main.class.getResource("Make.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Builder");
			stage.setScene(new Scene(pane));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		usernameLabel.setText("Welcome: " + KeyManager.getUsername());
		keyLabel.setText("Private Key: " + KeyManager.getKeyString());

		// Auto detect applications in the dir: ./apps/
		File folder = new File("./apps/");
		File[] files = folder.listFiles();
		apps = new ArrayList<File>();

		// Add all .locked files
		for (File file : files) {
			if (file.isFile() && file.getName().endsWith(".locked")) {
				System.out.println("Added: " + file.getName());
				apps.add(file);
			}
		}

		// ObservableList<String> items = FXCollections.observableArrayList("Single",
		// "Double", "Suite", "Family App");

		// Add the files to the list
		String[] appNames = new String[apps.size()];
		for (int i = 0; i < apps.size(); i++) {
			appNames[i] = apps.get(i).getName().replace(".locked", "");
		}
		ObservableList<String> items = FXCollections.observableArrayList(appNames);
		appList.setItems(items);

	}
}
