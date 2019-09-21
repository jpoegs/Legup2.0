package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import java.util.ArrayList;
import java.awt.*;

public class NoTentForTreeContradictionRule extends ContradictionRule
{

    public NoTentForTreeContradictionRule()
    {
        super("No Tent For Tree",
                "Each tree must link to a tent.",
                "edu/rpi/legup/images/treetent/contra_NoTentForTree.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        TreeTentBoard treeTentBoard = (TreeTentBoard) board;
        TreeTentCell cell = (TreeTentCell)puzzleElement;
        return(cell.getType() != TreeTentType.TREE || cell.isLinked() || checkTentforTree(treeTentBoard,cell.getLocation()))?"This cell does not contain a contradiction at this location.": null;

//        TreeTentBoard treeTentBoard = (TreeTentBoard) board;
//        TreeTentCell cell = (TreeTentCell)puzzleElement;
//        if(cell.getType() != TreeTentType.TREE) {
//            return "This cell does not contain a contradiction at this location.";
//        }
//        int adjTent = treeTentBoard.getAdjacent(cell, TreeTentType.TENT).size();
//        int adjUnknown = treeTentBoard.getAdjacent(cell, TreeTentType.UNKNOWN).size();
//        if(adjTent == 0 && adjUnknown == 0) {
//            return null;
//        } else {
//            return "This cell does not contain a contradiction at this location.";
//        }
    }

    public boolean checkTentforTree(TreeTentBoard board, Point loc){
        ArrayList<TreeTentCell> adjacent = new ArrayList<>();
        adjacent.add(board.getCell(loc.x+1,loc.y));
        adjacent.add(board.getCell(loc.x-1,loc.y));
        adjacent.add(board.getCell(loc.x,loc.y+1));
        adjacent.add(board.getCell(loc.x,loc.y-1));
        for(TreeTentCell cell: adjacent){
            if (cell != null && (cell.getType() == TreeTentType.UNKNOWN || (cell.getType() == TreeTentType.TENT && !cell.isLinked()))) {
                System.out.println("available");
                return true;
            }
        }
        return false;
    }
}