package com.autentia.tutoriales.puzzleSplitter;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws InvalidDimensionException, InvalidFirstPiece {
        int dim = Integer.parseInt(args[0]);
        int firstPieceRow = Integer.parseInt(args[1]);
        int firstPieceCol = Integer.parseInt(args[2]);

        System.out.println("Dimensions: " + dim + "x" + dim);
        System.out.println("First piece: " + firstPieceRow + "," + firstPieceCol);
        PuzzleSplitter splitter = new PuzzleSplitter(dim);
        splitter.split(firstPieceRow, firstPieceCol);
        System.out.println(splitter);
    }
}
