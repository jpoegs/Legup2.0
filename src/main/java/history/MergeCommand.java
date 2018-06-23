package history;

import app.GameBoardFacade;
import model.Puzzle;
import model.gameboard.Board;
import model.rules.MergeRule;
import model.tree.Tree;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import ui.treeview.TreeElementView;
import ui.treeview.TreeNodeView;
import ui.treeview.TreeTransitionView;
import ui.treeview.TreeView;

import java.util.ArrayList;

public class MergeCommand extends PuzzleCommand
{

    private ArrayList<TreeElementView> selectedViews;

    public MergeCommand(ArrayList<TreeElementView> selectedViews)
    {
        this.selectedViews = (ArrayList<TreeElementView>)selectedViews.clone();
    }

    /**
     * Executes an command
     */
    @Override
    public void execute()
    {
        super.execute();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        Tree tree = GameBoardFacade.getInstance().getTree();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        System.err.println("Attempting Merge...");

        ArrayList<TreeNode> mergingNodes = new ArrayList<>();
        ArrayList<Board> mergingBoards = new ArrayList<>();
        for(TreeElementView view : selectedViews)
        {
            TreeNode node = ((TreeNodeView)view).getTreeElement();
            mergingNodes.add(node);
            mergingBoards.add(node.getBoard());
        }

        TreeNode lca = tree.getLowestCommonAncestor(mergingNodes);
        Board lcaBoard = lca.getBoard();

        Board mergedBoard = lcaBoard.mergedBoard(lcaBoard, mergingBoards);

        TreeNode mergedNode = new TreeNode(mergedBoard.copy());
        TreeNodeView mergedView = new TreeNodeView(mergedNode);

        for(int i = 0; i < selectedViews.size(); i++)
        {
            TreeElementView elementView = selectedViews.get(i);
            TreeNodeView nodeView = (TreeNodeView)elementView;

            TreeTransition transition = new TreeTransition(nodeView.getTreeElement(), mergedBoard);
            transition.setRule(new MergeRule());
            nodeView.getTreeElement().addChild(transition);
            transition.setChildNode(mergedNode);
            mergedNode.addParent(transition);

            TreeTransitionView transitionView = new TreeTransitionView(transition, nodeView);
            nodeView.addChildrenView(transitionView);
            transitionView.setChildView(mergedView);
            mergedView.addParentView(transitionView);
        }
        treeView.repaint();
    }

    /**
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
        return true;
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     * otherwise null if command can be executed
     */
    @Override
    public String getExecutionError()
    {
        return null;
    }
}