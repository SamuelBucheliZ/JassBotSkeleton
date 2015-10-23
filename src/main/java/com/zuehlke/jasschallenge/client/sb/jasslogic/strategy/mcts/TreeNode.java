package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.mcts;

import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.common.PointsCounter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Function;

public abstract class TreeNode<T> {
    private static final Logger logger = LogManager.getLogger(TreeNode.class);

    private static double EXPLORATION_FACTOR = 1/Math.sqrt(2);
    private static double EPSILON = 1e-6;

    private static Random rand = new Random();

    public static void search(TreeNode<?> root) {
        TreeNode<?> current = root;

        // Select
        while (!current.isTerminal()) {
            if (!current.isFullyExpanded()) {
                current = current.expand();
                break;
            } else {
                current = current.bestChild(EXPLORATION_FACTOR);
            }
        }

        // Rollout
        PointsCounter evaluation = current.rollOut();

        // Backpropagate
        while (!Objects.isNull(current)) { // remember to traverse the tree all the way up to the root, including the root!
            current.update(evaluation);
            current = current.getParent();
        }
    }

    private WeakReference<TreeNode<?>> parent;
    private Set<T> untriedMoves;
    private Map<T, TreeNode<?>> children;
    private int numberOfVisits;
    private int numberOfWins;
    private int points;

    TreeNode(TreeNode<?> parent, Collection<T> untriedMoves) {
        this.parent = new WeakReference<>(parent);
        this.untriedMoves = new HashSet<>();
        this.untriedMoves.addAll(untriedMoves);
        this.children = new HashMap<>();
        this.numberOfVisits = 0;
        this.points = 0;
    }

    public abstract PointsCounter rollOut();

    protected abstract TreeNode<?> createChild(T move);

    public abstract boolean isTerminal();

    public TreeNode<?> bestChild(double explorationFactor) {
        int totalNumberOfVisits = this.getNumberOfVisits();
        double totalVisitFactor = totalNumberOfVisits == 0 ? Double.POSITIVE_INFINITY : 2*Math.log(totalNumberOfVisits); // avoid troubles with Math.log(0) and give unvisited nodes very high priority
        double pointsPerGame = PointsCounter.POINTS_PER_GAME; // remember that double division is required in exploitation, else the result will be int
        Function<TreeNode<?>, Double> getUCT =
                node ->  node.getPoints() / (node.getNumberOfVisits() * pointsPerGame) // exploitation -> game theoretic value of node, normalized to [0,1]
                        + explorationFactor * Math.sqrt(totalVisitFactor / node.getNumberOfVisits()) // exploration -> is bigger if node has not been visited often
                        + rand.nextDouble() * EPSILON; // randomly break ties
        Comparator<TreeNode<?>> compareByValue =
                (first, second) -> getUCT.apply(first).compareTo(getUCT.apply(second));
        TreeNode<?> selected = children.values().stream().max(compareByValue).get();
        return selected;
    }

    public TreeNode<?> expand() {
        T move = selectRandomUntriedMove();
        return this.expandWith(move);
    }

    public T selectRandomUntriedMove() {
        ArrayList<T> moves = new ArrayList<>(untriedMoves);
        int index = rand.nextInt(moves.size());
        T move = moves.get(index);
        this.untriedMoves.remove(move);
        return move;
    }

    public T selectBestMove() {
        // -----
        /*double totalNumberOfVisits = this.getNumberOfVisits();
        double totalVisitFactor = totalNumberOfVisits == 0 ? Double.POSITIVE_INFINITY : Math.log(totalNumberOfVisits);
        double pointsPerGame = PointsCounter.POINTS_PER_GAME; // enabling double promotion, else exploitation will be int
        Function<CardNode, Double> getExploitation =
                node -> node.getPoints() / (node.getNumberOfVisits() * pointsPerGame);
        Function<CardNode, Double> getExploration =
                node -> Math.sqrt(totalVisitFactor / node.getNumberOfVisits());
        Function<Map.Entry<T, CardNode>, String> prettyPrinter = e -> String.format("%s->%d/%d/%4.2f/%4.2f", e.getKey(), e.getValue().getPoints(), e.getValue().getNumberOfVisits(), getExploitation.apply(e.getValue()), getExploration.apply(e.getValue()));
        logger.info("Move rating: {}.", this.children.entrySet().stream().map(prettyPrinter).collect(Collectors.toList()));*/
        // ---

        final int ignoreExploration = 0;
        TreeNode<?> child = bestChild(ignoreExploration);
        T move = children.entrySet().stream().filter(e -> e.getValue() == child).findAny().get().getKey();
        return move;
    }

    public void update(PointsCounter evaluation) {
        increaseNumberOfVisits();
        addPoints(evaluation.getOurPoints());
        if (evaluation.weWin()) {
            increaseNumberOfWins();
        }
    }

    private TreeNode<?> expandWith(T move) {
        TreeNode<?> child = createChild(move);
        addChild(move, child);
        return child;
    }

    public boolean isFullyExpanded() {
        return untriedMoves.isEmpty();
    }

    public void makeRoot() {
        parent.clear();
    }

    public TreeNode<?> getParent() {
        return parent.get();
    }

    public void increaseNumberOfVisits() {
        this.numberOfVisits++;
    }

    public int getNumberOfVisits() {
        return numberOfVisits;
    }

    public void increaseNumberOfWins() {
        this.numberOfWins++;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }

    public void addChild(T move, TreeNode<?> child) {
        children.put(move, child);
    }

    public TreeNode<?> getOrCreateChild(T move) {
        if (this.children.containsKey(move)) {
            return this.children.get(move);
        } else {
            return this.expandWith(move);
        }
    }


}
