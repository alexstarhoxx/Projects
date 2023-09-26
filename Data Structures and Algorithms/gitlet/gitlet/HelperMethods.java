package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.Comparator;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class HelperMethods {

    /* SAVE LOADED OBJECTS INTO FILES IN .GITLET DIRECTORY */

    /** Save commit object C to .gitlet/commits/ directory */
    public static void saveCommit(Commit c) {
        String sha = getSha(c);
        File fir2 = join(COMMITS_DIR, first2Sha(sha));
        File rest38 = join(fir2, rest38Sha(sha));
        if (!fir2.exists()) {
            fir2.mkdir();
        }
        createNewFile(rest38);
        writeObject(rest38, c);
    }

    /** Save blob B to .gitlet/blobs/ directory */
    public static void saveBlob(String b) {
        String sha = getSha(b);
        File fir2 = join(BLOBS_DIR, first2Sha(sha));
        File rest38 = join(fir2, rest38Sha(sha));
        if (!fir2.exists()) {
            fir2.mkdir();
        }
        createNewFile(rest38);
        writeContents(rest38, b);
    }

    /** Save pointer P to .gitlet/pointers/ */
    public static void savePointer(Pointer p) {
        File f = join(POINTERS_DIR, p.name());
        writeObject(f, p);
    }

    /** Save addition file to .gitlet/staging area/ */
    public static void saveAddFile(HashMap<String, String> add) {
        writeObject(ADDITION_FILE, add);
    }

    /** Save removal file to .gitlet/staging area/ */
    public static void saveRmFile(HashMap<String, String> rm) {
        writeObject(REMOVAL_FILE, rm);
    }

    /* GET(READ, LOAD) EXISTED FILE FROM .GITLET DIRECTORY */

    /** Get sha-1 hash of the Head commit that HEAD pointer is pointing to */
    public static String getHeadSha() {
        Pointer active = getActivePointer();
        return active.pointingTo();
    }

    /** Get head commit object according to what HEAD pointer's pointing to */
    public static Commit getHeadCommit() {
        String sha = getHeadSha();
        return getCommit(sha);
    }

    /** Get commit with the given sha hash. If sha is null, return null */
    public static Commit getCommit(String sha) {
        if (sha == null) {
            return null;
        }
        File commit = join(COMMITS_DIR, first2Sha(sha), rest38Sha(sha));
        return readObject(commit, Commit.class);
    }

    /** Get content of a blob with given sha hash. If sha is null, return blank */
    public static String getContentOfBlob(String sha) {
        if (sha == null) {
            return "";
        }
        File blob = join(BLOBS_DIR, first2Sha(sha), rest38Sha(sha));
        return readContentsAsString(blob);
    }

    /** Get object of addition file in staging area directory */
    public static HashMap<String, String> getAddFile() {
        File add = join(ADDITION_FILE);
        return readObject(add, HashMap.class);
    }

    /** Get object of removal file in staging area directory */
    public static HashMap<String, String> getRmFile() {
        File rm = join(REMOVAL_FILE);
        return readObject(rm, HashMap.class);
    }

    /** Get object of a POINTER*/
    public static Pointer getPointer(File pointer) {
        return readObject(pointer, Pointer.class);
    }


    /** Get object of current active pointer(HEAD) */
    public static Pointer getActivePointer() {
        File activePointer = join(POINTERS_DIR, getHEAD());
        return readObject(activePointer, Pointer.class);
    }

    /** Get name of active pointer in HEAD pointer file */
    public static String getHEAD() {
        return readContentsAsString(HEAD_FILE);
    }


    /* STAGE OR UNSTAGE(WRITE OR ERASE) FILES TO ADDITION OR REMOVAL FILE */

    /** Stage a pair of file named NAME and its blob to
     *  addition file of staging area directory.
     */
    public static void stageAdd(String name, String blob) {
        HashMap<String, String> add = getAddFile();
        add.put(name, getSha(blob));
        saveAddFile(add);
    }

    /** Unstage the file named NAME in addition file.
     *  If NAME does not exist, nothing will change.
     */
    public static void unstageAdd(String name) {
        HashMap<String, String> add = getAddFile();
        add.remove(name);
        saveAddFile(add);
    }

    /** Stage the file named NAME and its corresponding sha-1 id into removal file */
    public static void stageRm(String name, String id) {
        File f = join(CWD, name);
        HashMap<String, String> rm = getRmFile();
        rm.put(name, id);
        saveRmFile(rm);
    }

    /** Stage the file named NAME into removal file */
    public static void stageRm(String name) {
        File f = join(CWD, name);
        HashMap<String, String> rm = getRmFile();
        rm.put(name, getSha(f));
        saveRmFile(rm);
    }

    /** Stage the FILE into removal file */
    public static void stageRm(File file) {
        HashMap<String, String> rm = getRmFile();
        rm.put(file.getName(), getSha(file));
        saveRmFile(rm);
    }

    /** Unstage the file named NAME in removal file.
     *  If NAME does not exist, nothing will change.
     */
    public static void unstageRm(String name) {
        HashMap<String, String> rm = getRmFile();
        rm.remove(name);
        saveRmFile(rm);
    }

    /** Unstage the FILE in removal file.
     *  If FILE does not exist, nothing will change.
     */
    public static void unstageRm(File file) {
        HashMap<String, String> rm = getRmFile();
        rm.remove(file.getName());
        saveRmFile(rm);
    }

    /** Unstage All files and its corresponding sha-1 hash in addition file */
    public static void unstagedAllAdd() {
        HashMap<String, String> add = getAddFile();
        add = new HashMap<>();
        saveAddFile(add);
    }

    /** Unstage All files and its corresponding sha-1 hash in removal file */
    public static void unstagedAllRm() {
        HashMap<String, String> rm = getRmFile();
        rm = new HashMap<>();
        saveRmFile(rm);
    }

    /** Clear the staging area */
    public static void clearStagingArea() {
        unstagedAllAdd();
        unstagedAllRm();
    }


    /* CHECK IF THE FILE IS IN STAGING AREA OR NOT */

    /** Check if the file named NAME is staged for addition */
    public static boolean isStagedAdd(String name) {
        HashMap<String, String> addFile = getAddFile();
        return addFile.containsKey(name);
    }

    /** Check if the file named NAME is staged for removal */
    public static boolean isStagedRm(String name) {
        HashMap<String, String> rmFile = getRmFile();
        return rmFile.containsKey(name);
    }


    /* CHECK IF A FILE IS TRACKED OR UNTRACKED IN A COMMIT */

    /** Check if the file named NAME is tracked by the head commit(current commit) */
    public static boolean isTracked(String name) {
        return isTrackedBy(getHeadSha(), name);
    }

    /** Check if the file named NAME is tracked by a commit with the given ID */
    public static boolean isTrackedBy(String id, String name) {
        Commit c = getCommit(id);
        HashMap<String, String> blobs = c.getBlobs();
        return blobs.containsKey(name);
    }

    /** Check if the file named NAME is untracked by the head commit(current commit) */
    public static boolean isUntracked(String name) {
        return isUntrackedBy(getHeadSha(), name);
    }

    /** Check if the file named NAME is untracked by a commit with the given ID.
     *  This does not mean the opposite of the isTracked method. When the file
     *  is neither staged for addition nor tracked in commit with ID, the file
     *  can be called untracked. Also, if the file is staged for removal but re-created
     *  without Gitlet knowledge, this file can be called untracked one too.
     */
    public static boolean isUntrackedBy(String id, String name) {
        File f = join(CWD, name);
        return !isStagedAdd(name) && !isTrackedBy(id, name)
                || isStagedRm(name) && isExist(f) && isTrackedBy(id, name);
    }


    /* FILES MANIPULATION */

    /** Pass in a file object and create a new file */
    public static void createNewFile(File f) {
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Check if FILE exists */
    public static boolean isExist(File f) {
        return f.exists();
    }

    /** Get ALL plain files in commits or blobs directory,
     *  File DIR should neither be COMMITS_DIR nor BLOBS_DIR.
     */
    public static List<String> allPlainFilesIn(File dir) {
        String[] subDir = dir.list();
        List<String> all = new ArrayList<>();
        for (int i = 0; i < subDir.length; i += 1) {
            String first2 = subDir[i];
            File d = join(dir, first2);
            for (String rest38 : plainFilenamesIn(d)) {
                all.add(first2 + rest38);
            }
        }
        all.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return all;
    }

    /** Overwrite file named F with blob with ID */
    public static void overwrite(String f, String id) {
        File file = join(CWD, f);
        createNewFile(file);
        writeContents(file, getContentOfBlob(id));
    }

    /** Overwrite FILE with CONTENT */
    public static void overwrite(File file, String content) {
        createNewFile(file);
        writeContents(file, content);
    }


    /* MERGE RELATED HELPER METHODS */

    /** Given the SPLIT POINT and the current or given commit C,
     *  check if a file named F is modified from the split point
     */
    public static boolean isModified(Commit splitPoint, Commit c, String f) {
        return !splitPoint.getBlobIdOf(f).equals(c.getBlobIdOf(f));
    }

    /** Given the CURRENT commit and GIVEN commit, check if
     *  file named F has the same blob in these commits
     */
    public static boolean hasSameBlob(Commit current, Commit given, String f) {
        String currId = current.getBlobIdOf(f);
        String givenId = given.getBlobIdOf(f);
        if (currId == null && givenId == null) {
            return true;
        } else if (currId == null || givenId == null) {
            return false;
        }

        return currId.equals(givenId);
    }

    /** Given the SPLIT POINT and the current or given commit C,
     *  check if a file named F is added in C from the SPLIT POINT
     */
    public static boolean isAdded(Commit sp, Commit c, String f) {
        return !sp.hasFile(f) && c.hasFile(f);
    }

    /** Given the SPLIT POINT and the current or given commit C,
     *  check if a file named F is removed from the SPLIT POINT
     */
    public static boolean isRemoved(Commit sp, Commit c, String f) {
        return sp.hasFile(f) && !c.hasFile(f);
    }


    /* SHA-1 RELATED HELPER METHODS AND OTHER METHODS  */

    /** Pass in a serializable object and return its sha-1 hash
     *  If the object is null, return null
     */
    public static String getSha(Serializable o) {
        if (o == null) {
            return null;
        }
        byte[] b = serialize(o);
        return sha1(b);
    }

    /** Get the first two characters of sha-1 hash */
    public static String first2Sha(String sha) {
        String fir2 = "";
        for (int i = 0; i < 2; i++) {
            fir2 = fir2 + sha.charAt(i);
        }
        return fir2;
    }

    /** Get the rest of 38 characters of sha-1 hash */
    public static String rest38Sha(String sha) {
        String rest38 = "";
        for (int i = 2; i < 40; i++) {
            rest38 = rest38 + sha.charAt(i);
        }
        return rest38;
    }

    /** Get the last 4 consecutive characters of sha-1 hash */
    public static String restSha(String sha) {
        String rest = "";
        for (int i = 2; i < sha.length(); i++) {
            rest = rest + sha.charAt(i);
        }
        return rest;
    }

    /**
     * If i is 0, return initial date, else return current date
     */
    public static String date(int i) {
        Date d;
        if (i == 0) {
            d = new Date(i);
        } else {
            d = new Date();
        }
        SimpleDateFormat ft = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy +0800");
        return ft.format(d);
    }

    /** Print our error message and exit */
    public static void error(String msg) {
        message(msg);
        System.exit(0);
    }
}
