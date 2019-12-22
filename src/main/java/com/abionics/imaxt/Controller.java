package com.abionics.imaxt;

import com.abionics.imaxt.core.ChannelsSpace;
import com.abionics.imaxt.core.Imcryptor;
import com.abionics.imaxt.core.coder.CoderException;
import com.abionics.imaxt.core.crypto.CryptoException;
import com.abionics.imaxt.core.decoder.DecoderException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public class Controller {
    static final private String DIRECTORY = "data/";

    @FXML private TextArea textArea;
    @FXML private TextField passwordTextField;
    @FXML private ComboBox<String> channelsSpaceComboBox;
    @FXML private CheckBox isUseFileCheckBox;
    @FXML private Button chooseFileButton;
    @FXML private Label filePathLabel;
    @FXML private Button chooseImageButton;
    @FXML private Label imagePathLabel;
    @FXML private CheckBox isCreateFileCheckBox;
    private Window window;

    private Imcryptor imcryptor;
    private File file;
    private File image;


    @FXML private void initialize() {
        if (new File(DIRECTORY).mkdir()) {
            System.out.println("Created data directory");
        }
        String[] spaces = Arrays.stream(ChannelsSpace.values()).map(Enum::name).toArray(String[]::new);
        channelsSpaceComboBox.getItems().addAll(spaces);
        channelsSpaceComboBox.setValue(spaces[0]);
        Platform.runLater(() -> {
            window = textArea.getScene().getWindow();
            imcryptor = new Imcryptor(window, DIRECTORY, isCreateFileCheckBox.isSelected());
        });
        chooseFileButton.setOnAction(actionEvent -> {
            var chooser = createFileChooser();
            file = chooser.showOpenDialog(window);
            if (file == null) {
                filePathLabel.setText("...");
                return;
            }
            filePathLabel.setText(file.getName());
            isUseFileCheckBox.setSelected(true);
        });
        chooseImageButton.setOnAction(actionEvent -> {
            var chooser = createFileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
            image = chooser.showOpenDialog(window);
            if (image == null) {
                imagePathLabel.setText("...");
                return;
            }
            imagePathLabel.setText(image.getName());
        });
        isCreateFileCheckBox.setOnAction(actionEvent -> imcryptor.isCreateFile = isCreateFileCheckBox.isSelected());
    }

    @NotNull
    private FileChooser createFileChooser() {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(DIRECTORY));
        return chooser;
    }

    @FXML private void code() {
        if (file == null && isUseFileCheckBox.isSelected()) {
            showError("Choose file or switch to text mode");
            return;
        }
        try {
            String text = textArea.getText();
            String password = passwordTextField.getText();
            var space = ChannelsSpace.valueOf(channelsSpaceComboBox.getValue());
            if (isUseFileCheckBox.isSelected()) {
                imcryptor.code(file, password, space);
            } else {
                imcryptor.code(text, password, space);
            }
        } catch (CoderException e) {
            showError(e.getMessage());
            System.out.println(e.getPlace());
            e.printStackTrace();
        } catch (IOException | GeneralSecurityException e) {
            showError(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML private void decode() {
        if (image == null) {
            showError("Choose image to decode");
            return;
        }
        try {
            String password = passwordTextField.getText();
            var space = ChannelsSpace.valueOf(channelsSpaceComboBox.getValue());
            String text = imcryptor.decode(image, password, space);
            textArea.setText(text);
        } catch (CryptoException e) {
            showError(e.getMessage());
        } catch (DecoderException e) {
            showError(e.getMessage());
            System.out.println(e.getPlace());
            e.printStackTrace();
        } catch (IOException | GeneralSecurityException e) {
            showError(e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Imaxt Error");
        alert.setHeaderText(text);
        //alert.setContentText(text);
        alert.showAndWait();
    }
}
