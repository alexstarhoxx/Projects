package gitlet;

import java.util.TreeMap;
import java.util.Map;
import java.util.Map.Entry;
import static gitlet.HelperMethods.*;

/**
 * Do DFS on the commit tree in order to
 * find the split point for merge command.
 *
 * @author Alex Ho
 */
public class SplitPointFinder {
    /**
     * Map of marked commits' id to its distance to a commit
     * that the active branch is pointing to
     */
    private TreeMap<String, Integer> marked;
    /**
     * Map of split points' id with its distance to a commit
     * that the active branch is pointing to
     */
    private TreeMap<String, Integer> splitPoints;
    /** The SHA-1 hash of commit that the current(active) branch is pointing to */
    private String current;
    /** The SHA-1 hash of commit that the other(given) branch is pointing to */
    private String other;

    public SplitPointFinder(String c, String o) {
        marked = new TreeMap<>();
        splitPoints = new TreeMap<>();
        current = c;
        other = o;
    }

    /** Find out split point of current branch and given branch */
    public Commit findSplitPoint() {
        depthFirstSearch(current);
        meetSplitPoints(other);
        return getLatestSplitPointOf(splitPoints);
    }

    /** Do DFS on current branch, store marked commits
     *  and its distance to ID in MARKED.
     */
    private void depthFirstSearch(String id) {
        dfs(id, 0);
    }

    private void dfs(String id, int dist) {
        marked.put(id, dist);
        Commit c = getCommit(id);
        if (c.isInitialCommit()) {
            return;
        }

        String[] parents = new String[]{c.getFirst(), c.getSecond()};
        for (String p : parents) {
            if (p != null && !marked.containsKey(p)) {
                dfs(p, dist + 1);
            }
        }
    }

    /**
     * Do DFS on other branch, if there is a marked commit,
     *  add it into the list of all split points
     */
    private void meetSplitPoints(String id) {
        Commit o = getCommit(id);
        if (marked.containsKey(id)) {
            splitPoints.put(id, marked.get(id));
            return;
        }

        String[] parents = new String[]{o.getFirst(), o.getSecond()};
        for (String p : parents) {
            if (p != null && !marked.containsKey(p)) {
                meetSplitPoints(p);
            } else if (p != null && marked.containsKey(p)) {
                splitPoints.put(p, marked.get(p));
            }
        }
    }

    /** Get the latest split points by comparing their timestamp */
    private String getLatestSplitPointIDOf(TreeMap<String, Integer> split) {
        Entry pair = sortedByValue(split);
        String id = (String) pair.getKey();
        return id;
    }

    /** Get the latest split points by comparing their timestamp */
    private Commit getLatestSplitPointOf(TreeMap<String, Integer> split) {
        String id = getLatestSplitPointIDOf(split);
        return getCommit(id);
    }

    private static Entry sortedByValue(TreeMap<String, Integer> t) {
        return t.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue())
                .findFirst()
                .get();
    }
}
