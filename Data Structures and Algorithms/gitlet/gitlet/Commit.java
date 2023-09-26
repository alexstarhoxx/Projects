package gitlet;

import java.io.Serializable;
import java.util.HashMap;

import static gitlet.HelperMethods.*;

/** Represents a gitlet commit object.
 *
 *  @author Alex Ho
 */
public class Commit implements Serializable {
    /** The message of this Commit. */
    private String message;
    /** The timestamp of this Commit */
    private String timeStamp;
    /** The blobs of this Commit, mapping file names to its corresponding blob */
    private HashMap<String, String> blobs;
    /** The first parent of this Commit, which should be the head of active branch
     *  when merging, indicating by using SHA-1 hash code */
    private String first;
    /** The second parent of this Commit, which should be the head of the given branch
     *  given in the command line when merging, indicating by using SHA-1 hash code */
    private String second;

    /** Initialize a commit */
    public Commit() {
        message = "initial commit";
        timeStamp = date(0);
        blobs = new HashMap<>();
        first = null;
        second = null;
    }

    /** Create a new commit object with only one parent */
    public Commit(String m, String t, HashMap<String, String> b, String p) {
        message = m;
        timeStamp = t;
        blobs = b;
        first = p;
    }

    /** Create a new commit object with two parents */
    public Commit(String m, String t, HashMap<String, String> b, String p1, String p2) {
        message = m;
        timeStamp = t;
        blobs = b;
        first = p1;
        second = p2;
    }

    /** Copy this commit with only the first parent is copied */
    public Commit copyCommit() {
        return new Commit(message, timeStamp, blobs, first);
    }

    /** Update instances variables(info) of this commit */
    public void updateCommit(String m, String t, HashMap<String, String> b, String p1, String p2) {
        this.message = m;
        this.timeStamp = t;
        this.blobs = b;
        this.first = p1;
        this.second = p2;
    }

    /** Get message of this commit */
    public String getMessage() {
        return this.message;
    }

    /** Get timeStamp of this commit */
    public String getTimeStamp() {
        return this.timeStamp;
    }

    /** Get blobs of this commit */
    public HashMap<String, String> getBlobs() {
        return this.blobs;
    }

    /** Get the id of a blob of a file named F in this commit.
     *  If there is no tracked file named F in this commit, return null
     */
    public String getBlobIdOf(String f) {
        HashMap<String, String> b = getBlobs();
        if (!b.containsKey(f)) {
            return null;
        }
        return b.get(f);
    }


    /** Get SHA-1 hash code of this commit's first parent */
    public String getFirst() {
        return this.first;
    }

    /** Get this commit's first parent */
    public Commit getFirstCommit() {
        return getCommit(getFirst());
    }

    /** Get SHA-1 hash code of this commit's first parent */
    public String getSecond() {
        return this.second;
    }

    /** Get this commit's second parent */
    public Commit getSecondCommit() {
        return getCommit(getSecond());
    }

    /** Return if the commit is the initial one */
    public boolean isInitialCommit() {
        return first == null && second == null;
    }

    /** Check if there is a tracked file named F in the commit */
    public boolean hasFile(String f) {
        return this.getBlobs().containsKey(f);
    }
}
