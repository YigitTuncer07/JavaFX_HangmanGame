import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Hangman extends Application {

    private static final Pane pane = new Pane();
    private final TextField textArea = new TextField();
    private static final String[] wordPool = {"car","bayern munich","turkey","blue whale","red lobster","house","money","hangman","coca cola","lean","eggs","water","fish","minecraft","capitalism","orange juice","turkish delight","dragon"};
    private static String word = chooseWord();
    private static char[] wordArray = stringToChar(word);
    private final Label displayLabel = new Label("Enter a letter or a word.");
    private final Label wordLabel = new Label(stringForLabel());
    private  final Label triedLetters = new Label("Tried Letters: ");
    private static int wrongAnswers = 0;
    private static final ArrayList<Shape> addedParts = new ArrayList<>();
    private static boolean isGameLost = false;
    private static boolean isGameWon = false;
    private static final ArrayList<Character> triedChars = new ArrayList<>();
    private int wins = 0;
    private int loses = 0;
    private final Label winLabel = new Label("W: " + wins);
    private final Label loseLabel = new Label("L: " + loses);
    
    private static final Circle headCircle = new Circle();
    private static final Line bodyLine = new Line();
    private static final Line rightArmLine = new Line();
    private static final Line leftArmLine = new Line();
    private static final Line rightLegLine = new Line();
    private static final Line leftLegLine = new Line();
    private static final Line leftEye = new Line();
    private static final Line rightEye = new Line();
    private static final Arc sadMouth = new Arc();
    private static final Arc happyMouth = new Arc();


    @Override
    public void start(Stage tryTextButtonPressed) {

        //Creates a new Pane
        pane.setPadding(new Insets(10, 10, 10, 10));

        //Actual game


        //Creates nodes to add to pane
        Button tryTextButton = new Button("Try Text");
        tryTextButton.setLayoutX(252);
        tryTextButton.setLayoutY(227);
        tryTextButton.setPrefWidth(80);
        pane.getChildren().add(tryTextButton);
        tryTextButton.setOnAction(event -> {
            if (isGameLost) {
                displayLabel.setText("Wrong! You Lost!");
            } else if (isGameWon) {
                displayLabel.setText("Correct! You Won!");
            } else {

                String input = textArea.getText();
                input = input.toLowerCase();

                if (input.length() == 1) {
                    char inputChar = input.charAt(0);
                    if (triedChars.contains(inputChar)) {
                        displayLabel.setText("You've already tried that letter!");
                        textArea.clear();
                    } else if (wordArrayHasChar(inputChar)) {
                        triedChars.add(inputChar);

                        ArrayList<Integer> list = getIndexesOfChar(inputChar);
                        displayLabel.setText("Correct!");
                        textArea.clear();
                        for (int index : list) {
                            index = index * 2;
                            wordLabel.setText(replaceChar(index, Character.toString(inputChar), wordLabel.getText()));
                        }
                        if (!wordLabel.getText().contains("_")) {
                            displayLabel.setText("Correct! You Won!");
                            wins++;
                            winLabel.setText("W: " + wins);
                            if (addedParts.size() >= 1){
                                addPart(10);
                            }
                            isGameWon = true;
                            textArea.setText("Start a new game!");
                            textArea.setEditable(false);
                        }
                    } else {
                        if (wrongAnswers == 8) {
                            displayLabel.setText("Wrong! You Lost!");
                            loses++;
                            loseLabel.setText("L: " + loses);
                            wordLabel.setText(addSpacesToString(word));
                            textArea.setText("Start a new game!");
                            textArea.setEditable(false);
                            isGameLost = true;
                            addPart(8);
                        } else {
                            displayLabel.setText("Wrong! Try again.");
                            addLetterToTriedLetters(inputChar);
                            triedChars.add(inputChar);
                            textArea.clear();
                            addPart(wrongAnswers);
                            wrongAnswers++;
                        }
                    }
                } else if (input.length() > 1) {
                    if (input.equals(word)) {
                        displayLabel.setText("Correct! You Won!");
                        wins++;
                        winLabel.setText("W: " + wins);
                        if (addedParts.size() >= 1){
                            addPart(10);
                        }
                        isGameWon = true;
                        textArea.setText("Start a new game!");
                        textArea.setEditable(false);
                        wordLabel.setText(addSpacesToString(word));
                    } else {
                        if (wrongAnswers == 8) {
                            displayLabel.setText("Wrong! You Lost!");
                            loses++;
                            loseLabel.setText("L: " + loses);
                            wordLabel.setText(addSpacesToString(word));
                            textArea.setText("Start a new game!");
                            textArea.setEditable(false);
                            isGameLost = true;
                            addPart(8);
                        } else {
                            displayLabel.setText("Wrong! Try again!");
                            textArea.clear();
                            addPart(wrongAnswers);
                            wrongAnswers++;
                        }
                    }
                } else {
                    displayLabel.setText("Enter a letter or word!");
                }
            }
        });

        Button clearTextButton = new Button("Clear Text");
        clearTextButton.setLayoutX(334);
        clearTextButton.setLayoutY(227);
        clearTextButton.setPrefWidth(80);
        pane.getChildren().add(clearTextButton);
        clearTextButton.setOnAction(event -> textArea.clear());

        Button newGameButton = new Button("New Game");
        newGameButton.setLayoutX(416);
        newGameButton.setLayoutY(227);
        newGameButton.setPrefWidth(80);
        pane.getChildren().add(newGameButton);
        newGameButton.setOnAction(event -> {
            triedLetters.setText("Tried Letters: ");
            wrongAnswers = 0;
            word = chooseWord();
            triedChars.clear();
            wordLabel.setText(stringForLabel());
            textArea.setEditable(true);
            textArea.clear();
            isGameLost = false;
            isGameWon = false;
            wordArray = stringToChar(word);
            for (Shape addedPart : addedParts) {
                pane.getChildren().remove(addedPart);
            }
            addedParts.clear();

        });
        tryTextButton.setDefaultButton(true);

        winLabel.setLayoutX(252);
        winLabel.setLayoutY(55);
        pane.getChildren().add(winLabel);

        loseLabel.setLayoutX(287);
        loseLabel.setLayoutY(55);
        pane.getChildren().add(loseLabel);

        triedLetters.setLayoutX(252);
        triedLetters.setLayoutY(80);
        pane.getChildren().add(triedLetters);

        wordLabel.setLayoutX(252);
        wordLabel.setLayoutY(10);
        wordLabel.setFont(new Font(20));
        pane.getChildren().add(wordLabel);

        displayLabel.setLayoutX(252);
        displayLabel.setLayoutY(205);
        pane.getChildren().add(displayLabel);

        textArea.setLayoutX(252);
        textArea.setLayoutY(256);
        textArea.setPrefSize(244, 42);
        textArea.setStyle("-fx-background-color: #EEEEEE;");
        textArea.setPromptText("Enter text here");
        pane.getChildren().add(textArea);

        Line SplitLine = new Line();
        SplitLine.setStartX(250);
        SplitLine.setStartY(0);
        SplitLine.setEndX(250);
        SplitLine.setEndY(300);
        pane.getChildren().add(SplitLine);

        Line bottomLine = new Line();
        bottomLine.setStartY(280);
        bottomLine.setStartX(20);
        bottomLine.setEndY(280);
        bottomLine.setEndX(230);
        bottomLine.setStroke(Color.GRAY);
        pane.getChildren().add(bottomLine);

        Line poleLine = new Line();
        poleLine.setStartY(280);
        poleLine.setStartX(200);
        poleLine.setEndY(30);
        poleLine.setEndX(200);
        poleLine.setStroke(Color.GRAY);
        pane.getChildren().add(poleLine);

        Line vertPoleLine = new Line();
        vertPoleLine.setStartY(35);
        vertPoleLine.setStartX(200);
        vertPoleLine.setEndY(35);
        vertPoleLine.setEndX(100);
        vertPoleLine.setStroke(Color.GRAY);
        pane.getChildren().add(vertPoleLine);

        Line stringLine = new Line();
        stringLine.setStartY(35);
        stringLine.setStartX(110);
        stringLine.setEndY(50);
        stringLine.setEndX(110);
        stringLine.setStroke(Color.GRAY);
        pane.getChildren().add(stringLine);

        Line poleLineThing = new Line();
        poleLineThing.setStartY(35);
        poleLineThing.setStartX(180);
        poleLineThing.setEndY(50);
        poleLineThing.setEndX(200);
        poleLineThing.setStroke(Color.GRAY);
        pane.getChildren().add(poleLineThing);

        //end of unchanging shapes

        headCircle.setCenterX(110);
        headCircle.setCenterY(70);
        headCircle.setRadius(20);
        headCircle.setFill(Color.TRANSPARENT);
        headCircle.setStroke(Color.GRAY);

        bodyLine.setStartY(90);
        bodyLine.setStartX(110);
        bodyLine.setEndY(190);
        bodyLine.setEndX(110);
        bodyLine.setStroke(Color.GRAY);

        rightArmLine.setStartY(110);
        rightArmLine.setStartX(110);
        rightArmLine.setEndY(150);
        rightArmLine.setEndX(140);
        rightArmLine.setStroke(Color.GRAY);

        leftArmLine.setStartY(110);
        leftArmLine.setStartX(110);
        leftArmLine.setEndY(150);
        leftArmLine.setEndX(80);
        leftArmLine.setStroke(Color.GRAY);

        rightLegLine.setStartY(190);
        rightLegLine.setStartX(110);
        rightLegLine.setEndY(250);
        rightLegLine.setEndX(130);
        rightLegLine.setStroke(Color.GRAY);

        leftLegLine.setStartY(190);
        leftLegLine.setStartX(110);
        leftLegLine.setEndY(250);
        leftLegLine.setEndX(90);
        leftLegLine.setStroke(Color.GRAY);

        leftEye.setStartY(62);
        leftEye.setStartX(102);
        leftEye.setEndY(70);
        leftEye.setEndX(102);
        leftEye.setStroke(Color.GRAY);

        rightEye.setStartY(62);
        rightEye.setStartX(118);
        rightEye.setEndY(70);
        rightEye.setEndX(118);
        rightEye.setStroke(Color.GRAY);

        sadMouth.setCenterX(110);
        sadMouth.setCenterY(82);
        sadMouth.setType(ArcType.OPEN);
        sadMouth.setStartAngle(0);
        sadMouth.setRadiusX(8);
        sadMouth.setRadiusY(8);
        sadMouth.setLength(180);
        sadMouth.setFill(Color.TRANSPARENT);
        sadMouth.setStroke(Color.GRAY);
        sadMouth.setStrokeWidth(1);

        happyMouth.setCenterX(110);
        happyMouth.setCenterY(76);
        happyMouth.setType(ArcType.OPEN);
        happyMouth.setStartAngle(0);
        happyMouth.setRadiusX(8);
        happyMouth.setRadiusY(8);
        happyMouth.setLength(-180);
        happyMouth.setFill(Color.TRANSPARENT);
        happyMouth.setStroke(Color.GRAY);
        happyMouth.setStrokeWidth(1);


        //Creates scene and adds pane to it
        Scene scene = new Scene(pane, 498, 300);
        pane.setStyle("-fx-background-color: #CCCCCC;");
        scene.getStylesheets().add("Hangman.css");


        //Sets up stage
        tryTextButtonPressed.setResizable(false);
        tryTextButtonPressed.setTitle("Hangman Beta 1.1v");
        tryTextButtonPressed.setScene(scene);
        tryTextButtonPressed.show();

    }

    public static void main(String[] args) {
        launch();
    }

    public static String chooseWord() {
        int index = (int) (Math.random() * wordPool.length);
        return wordPool[index];
    }

    public static char[] stringToChar(String word) {
        char[] array = new char[word.length()];
        for (int i = 0; i < word.length(); i++) {
            array[i] = word.charAt(i);
        }
        return array;
    }

    private static String stringForLabel() {
        int numOfSpaces = 0;
        for (int i = 0; i < word.length(); i++){
            if (word.charAt(i) == ' '){
                numOfSpaces++;
            }
        }
        int[] indexes = new int[numOfSpaces];
        for (int i = 0, k = 0; i < word.length(); i++){
            if (word.charAt(i) == ' '){
                indexes[k] = i * 2;
                k++;
            }
        }
        char[] array = new char[word.length() * 2 - 1];
        for (int i = 0; i < word.length() * 2 - 1; i++) {
            if (i % 2 == 0) {
                array[i] = '_';
            } else {
                array[i] = ' ';
            }
        }
        for (int index : indexes) {
            array[index] = ' ';
        }
        return String.valueOf(array);
    }

    private String replaceChar(int index, String replacement, String string) {
        if (index == 0) {
            return replacement + string.substring(1);

        } else if (index == this.wordLabel.getText().length() - 1) {
            return string.substring(0, wordLabel.getText().length() - 1) + replacement;

        }
        String s1 = string.substring(0, index);
        String s3 = string.substring(index + 1);
        return s1 + replacement + s3;

    }

    private static boolean wordArrayHasChar(char c) {
        for (char a : wordArray) {
            if (a == c) {
                return true;
            }
        }
        return false;
    }
    private void addLetterToTriedLetters(char c){
        if ((!triedChars.contains(c))){
            if (triedLetters.getText().length() == 15){
                triedLetters.setText(triedLetters.getText() + c);
            } else {
                triedLetters.setText(triedLetters.getText() + ", " + c);
            }
        }

    }

    private ArrayList<Integer> getIndexesOfChar(char c) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < wordArray.length; i++) {
            if (wordArray[i] == c) {
                list.add(i);
            }
        }
        return list;
    }

    private static String addSpacesToString(String string) {
        return string.replace("", " ").trim();

    }
    private static void addPart(int i) {
        switch (i) {
            case 0:
                pane.getChildren().add(headCircle);
                addedParts.add(headCircle);
                break;
            case 1:
                pane.getChildren().add(bodyLine);
                addedParts.add(bodyLine);
                break;
            case 2:
                pane.getChildren().add(rightArmLine);
                addedParts.add(rightArmLine);
                break;
            case 3:
                pane.getChildren().add(leftArmLine);
                addedParts.add(leftArmLine);
                break;
            case 4:
                pane.getChildren().add(rightLegLine);
                addedParts.add(rightLegLine);
                break;
            case 5:
                pane.getChildren().add(leftLegLine);
                addedParts.add(leftLegLine);
                break;
            case 6:
                pane.getChildren().add(leftEye);
                addedParts.add(leftEye);
                break;
            case 7:
                pane.getChildren().add(rightEye);
                addedParts.add(rightEye);
                break;
            case 8:
                pane.getChildren().add(sadMouth);
                addedParts.add(sadMouth);
                break;
            case 10:
                int size = addedParts.size();
                if (size <= 6){
                    pane.getChildren().addAll(rightEye,leftEye,happyMouth);
                    addedParts.add(rightEye);
                    addedParts.add(leftEye);
                    addedParts.add(happyMouth);
                } else if (size == 7) {
                    pane.getChildren().addAll(rightEye,happyMouth);
                    addedParts.add(rightEye);
                    addedParts.add(happyMouth);
                } else if (size == 8){
                    pane.getChildren().add(happyMouth);
                    addedParts.add(happyMouth);
                }
                break;
        }
    }
}
