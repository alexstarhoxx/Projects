package gitlet;

import java.io.Serializable;

import static gitlet.Utils.*;
import static gitlet.Repository.*;

/**
 *  Represents a pointer object pointing to commit.
 *  @author Alex Ho
 */
public class Pointer implements Serializable {
    /** Name of a pointer */
    private String name;
    /** The commit it is pointing to */
    private String commitSHA;

    /** Initialize a pointer when it initials a gitlet repo */
    public Pointer(String initSHA) {
        name = "master";
        commitSHA = initSHA;
    }

    /** Create a new pointer */
    public Pointer(String n, String sha) {
        name = n;
        commitSHA = sha;
    }

    /**
     * Update HEAD file(pointer) so that the content
     * of HEAD file be name of the active pointer.
     * The content of HEAD file should be overwritten.
     */
    public void updateHEAD() {
        writeContents(HEAD_FILE, this.name);
    }

    /** Update commitSHA instance variable of this pointer */
    public void updateSHA(String newSHA) {
        this.commitSHA = newSHA;
    }

    /** Return name of the pointer */
    public String name() {
        return name;
    }

    /** Return SHA-1 hash of commit that it is pointing to */
    public String pointingTo() {
        return commitSHA;
    }
}
