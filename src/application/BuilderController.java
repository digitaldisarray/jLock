package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.contra.obfuscator.Application;

public class BuilderController {

	File chosen;
	HWID hwid = new HWID();
	KeyGenerator keygen = new KeyGenerator();

	@FXML
	private Button browseButton;

	@FXML
	private Label errorLabel;

	@FXML
	private Label statusLabel;

	@FXML
	private TextField jarPath;

	@FXML
	private Button buildButton;

	@FXML
	private TextField username;

	@FXML
	private TextField password;

	@FXML
	void browse(ActionEvent event) {
		// Get the builder file

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Java Executable", "jar");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			chosen = chooser.getSelectedFile();
		} else {
			return;
		}

		jarPath.setText(chosen.getPath());

	}

	@FXML
	void build(ActionEvent event) {
		errorLabel.setVisible(false);

		if (chosen.equals(null)) {
			errorLabel.setVisible(true);
			errorLabel.setText("Error: No jar chosen.");
		}

		if (!chosen.exists()) {
			errorLabel.setVisible(true);
			errorLabel.setText("Error: Selected jar does not exist.");
		}

		// Obfuscate
		String[] args = { chosen.getPath(), "class-name" };
		statusLabel.setText("Status: Running");
		Application.main(args);
		statusLabel.setText("Status: Complete");
		
		File obfuscated = new File(chosen.getName().replace(".", "-new."));
		File outputFile = new File("./apps/" + obfuscated.getName().replace("-new.jar", ".locked"));
		
		// Encrypt
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");

			SecretKey encKey = keygen.generateKey(username.getText(), password.getText(), hwid.getHWID());
			
			cipher.init(Cipher.ENCRYPT_MODE, encKey);

			FileInputStream inputStream = new FileInputStream(obfuscated);
			byte[] inputBytes = new byte[(int) chosen.length()];
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
}
