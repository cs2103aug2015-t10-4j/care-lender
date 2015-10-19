package carelender.view;

import carelender.controller.Controller;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserInterfaceController implements Initializable {
    @FXML
    private TextArea outputText;
    @FXML
    private TextField inputText;

    @FXML
    private SplitPane canvasSplitPane;
    @FXML
    private StackPane canvasHolderTop;
    @FXML
    private StackPane canvasHolderBottom;

    private ArrayList<String> messageList;
    ResizableCanvas canvas1;
    ResizableCanvas canvas2;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert outputText != null : "fx:id=\"outputText\" was not injected: check your FXML file 'simple.fxml'.";
        assert inputText != null : "fx:id=\"inputText\" was not injected: check your FXML file 'simple.fxml'.";
        assert canvasHolderBottom != null : "fx:id=\"canvasHolderBottom\" was not injected: check your FXML file 'simple.fxml'.";
        assert canvasHolderTop != null : "fx:id=\"canvasHolderTop\" was not injected: check your FXML file 'simple.fxml'.";
        assert canvasSplitPane != null : "fx:id=\"canvasSplitPane\" was not injected: check your FXML file 'simple.fxml'.";
        // initialize your logic here: all @FXML variables will have been injected

        //Create canvases using code
        canvas1 = new ResizableCanvas();
        canvasHolderTop.getChildren().add(canvas1);

        // Bind canvas size to stack pane size.
        canvas1.widthProperty().bind(
                canvasHolderTop.widthProperty());
        canvas1.heightProperty().bind(
                canvasHolderTop.heightProperty());

        canvas2 = new ResizableCanvas();
        canvasHolderBottom.getChildren().add(canvas2);

        // Bind canvas size to stack pane size.
        canvas2.widthProperty().bind(
                canvasHolderBottom.widthProperty());
        canvas2.heightProperty().bind(
                canvasHolderBottom.heightProperty());

        Controller.initUserInterfaceController(this);

        Controller.printWelcomeMessage();
        final EventHandler<KeyEvent> keyEventHandler =
                new EventHandler<KeyEvent>() {
                    public void handle(final KeyEvent keyEvent) {
                        if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                            switch ( keyEvent.getCode() ) {
                                case ENTER:
                                    String text = inputText.getText();
                                    inputText.setText("");
                                    Controller.processUserInput(text);
                                    break;
                                case UP:
                                    Controller.processUpPress();
                                    break;
                                case DOWN:
                                    Controller.processDownPress();
                                    break;
                                default:
                                    Controller.processIncompleteInput(inputText.getText());
                                    break;
                            }

                        }
                    }
                };
        inputText.setOnKeyPressed( keyEventHandler );
    }

    public void setCanvas1Renderer ( CanvasRenderer renderer ) {
        canvas1.setRenderer(renderer);
    }
    public void setCanvas2Renderer ( CanvasRenderer renderer ) {
        canvas2.setRenderer(renderer);
    }
    public void setMessageList( ArrayList<String> messageList ) {
    	this.messageList = messageList;
    }

    public void clearMessageLog() {
        messageList.clear();
        refreshOutputField();
    }
    public void displayMessage( String message ) {
        messageList.add(message);
        refreshOutputField();
    }

    public void setUserInput ( String inputText ) {
        this.inputText.setText(inputText);
    }
    public void refreshOutputField() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messageList.size(); i++ ) {
            stringBuilder.append(messageList.get(i));
            stringBuilder.append("\n");
        }
        outputText.setText(stringBuilder.toString());
        outputText.setScrollTop(Double.MAX_VALUE);
    }

}