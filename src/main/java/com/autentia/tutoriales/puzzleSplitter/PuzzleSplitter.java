package com.autentia.tutoriales.puzzleSplitter;

import java.text.DecimalFormat;

public class PuzzleSplitter {

    private int dim;
    private int pieceNumber;
    private int[][] matrix;

    public PuzzleSplitter(int dim) throws InvalidDimensionException {
        if ((dim % 4) == 0) {
            this.dim = dim;
            pieceNumber = 1;
            matrix = new int[dim][dim];
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    matrix[i][j] = 0;
                }
            }
        } else {
            throw new InvalidDimensionException("Dimensions must be multiples of 4.");
        }
    }

    private void splitPosition(int row, int col) {
        matrix[row][col] = pieceNumber;
    }

    private boolean isSplittedPosition(int row, int col) {
        return matrix[row][col] != 0;
    }

    private void splitFirstPiece(int row, int col) {
        splitPosition(row, col);
        pieceNumber++;
    }

    private boolean isValidFirstPiece(int row, int col) {
        return (row >= 0 && row < dim) && (col >= 0 && col < dim);
    }

    private boolean isBaseCase(int start, int end) {
        return (end - start) == 1;
    }

    private boolean hasOneSplitted(int rowStart, int colStart, int rowEnd, int colEnd) {
        for (int i = rowStart; i <= rowEnd; i++) {
            for (int j = colStart; j <= colEnd; j++) {
                if (isSplittedPosition(i, j))
                    return true;
            }
        }
        return false;
    }

    private int getSplittedQuadrant(int rowStart, int colStart, int rowEnd, int colEnd) {
        final int middleRow = rowStart + (rowEnd - rowStart) / 2;
        final int middleCol = colStart + (colEnd - colStart) / 2;

        if (hasOneSplitted(rowStart, colStart, middleRow, middleCol))
            return 1;
        else if (hasOneSplitted(rowStart, middleCol + 1, middleRow, colEnd))
            return 2;
        else if (hasOneSplitted(middleRow + 1, colStart, rowEnd, middleCol))
            return 3;
        else
            return 4;
    }

    private void SplitQuadrant(int quadrant, int rowStart, int colStart, int rowEnd, int colEnd) {
        final int middleRow = rowStart + (rowEnd - rowStart) / 2;
        final int middleCol = colStart + (colEnd - colStart) / 2;

        switch (quadrant) {
            case 1:
                splitAux(rowStart, colStart, middleRow, middleCol);
                break;
            case 2:
                splitAux(rowStart, middleCol + 1, middleRow, colEnd);
                break;
            case 3:
                splitAux(middleRow + 1, colStart, rowEnd, middleCol);
                break;
            case 4:
                splitAux(middleRow + 1, middleCol + 1, rowEnd, colEnd);
                break;
        }
    }

    public void split(int firstPieceRow, int firstPieceColumn) throws InvalidFirstPiece {
        if (!isValidFirstPiece(firstPieceRow, firstPieceColumn))
            throw new InvalidFirstPiece("The piece must be in the matrix.");
        splitFirstPiece(firstPieceRow, firstPieceColumn);
        splitAux(0, 0, dim - 1, dim - 1);
    }

    private void splitAux(int rowStart, int colStart, int rowEnd, int colEnd) {
        if (isBaseCase(rowStart, rowEnd)) {
            splitBaseCase(rowStart, colStart, rowEnd, colEnd);
        } else {
            // First we look for a quadrant which has one position splitted to split the
            // whole quadrant.
            final int splittedQuadrant = getSplittedQuadrant(rowStart, colStart, rowEnd, colEnd);
            SplitQuadrant((splittedQuadrant), rowStart, colStart, rowEnd, colEnd);
            // Now we set a L piece at the corner of this quadrant.
            setCornerPiece(rowStart, colStart, rowEnd, colEnd, splittedQuadrant);
            // Finally, whe split the rest of the quadrants.
            splitRemainingQuadrants(rowStart, colStart, rowEnd, colEnd, splittedQuadrant);
        }
    }

    private void splitRemainingQuadrants(int rowStart, int colStart, int rowEnd, int colEnd, int splittedQuadrant) {
        if (splittedQuadrant != 1)
            SplitQuadrant(1, rowStart, colStart, rowEnd, colEnd);
        if (splittedQuadrant != 2)
            SplitQuadrant(2, rowStart, colStart, rowEnd, colEnd);
        if (splittedQuadrant != 3)
            SplitQuadrant(3, rowStart, colStart, rowEnd, colEnd);
        if (splittedQuadrant != 4)
            SplitQuadrant(4, rowStart, colStart, rowEnd, colEnd);
    }

    private void setCornerPiece(int rowStart, int colStart, int rowEnd, int colEnd, int splittedQuadrant) {
        final int middleRow = rowStart + (rowEnd - rowStart) / 2;
        final int middleCol = colStart + (colEnd - colStart) / 2;

        if (splittedQuadrant != 1)
            splitPosition(middleRow, middleCol);
        if (splittedQuadrant != 2)
            splitPosition(middleRow, middleCol + 1);
        if (splittedQuadrant != 3)
            splitPosition(middleRow + 1, middleCol);
        if (splittedQuadrant != 4)
            splitPosition(middleRow + 1, middleCol + 1);
        pieceNumber++;
    }

    private void splitBaseCase(int rowStart, int colStart, int rowEnd, int colEnd) {
        for (int i = rowStart; i <= rowEnd; i++) {
            for (int j = colStart; j <= colEnd; j++) {
                if (!isSplittedPosition(i, j))
                    splitPosition(i, j);
            }
        }
        pieceNumber++;
    }

    @Override
    public String toString() {
        String strMatrix = "";
        DecimalFormat formater = new DecimalFormat("00");
        for (int i = 0; i < dim; i++) {
            strMatrix += "[";
            for (int j = 0; j < dim; j++) {
                strMatrix += formater.format(matrix[i][j]);
            }
            strMatrix += "]\n";
        }
        return strMatrix;
    }

}