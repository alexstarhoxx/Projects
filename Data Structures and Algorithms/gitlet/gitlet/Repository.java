package gitlet;

import java.io.File;
import java.util.List;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Set;
import static gitlet.Utils.*;
import static gitlet.HelperMethods.*;

/**
 *  Represents a gitlet repository which manipulates all
 *  files and directories in .gitlet and current working
 *  directory to complete all the commands.
 *  @author Alex Ho
 */
public class Repository {

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /**
     * The commits directory in .gitlet/
     */
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    /**
     * The blobs directory in .gitlet/
     */
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    /**
     * The pointers directory in .gitlet/
     */
    public static final File POINTERS_DIR = join(GITLET_DIR, "pointers");
    /**
     * The HEAD pointer file in .gitlet/pointers/
     */
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");
    /**
     * The staging area directory in .gitlet/
     */
    public static final File STAGING_AREA_DIR = join(GITLET_DIR, "staging area");
    /**
     * The staged for addition file in .gitlet/staging area/
     */
    public static final File ADDITION_FILE = join(STAGING_AREA_DIR, "addition");
    /**
     * The staged for removal file in .gitlet/staging area/
     */
    public static final File REMOVAL_FILE = join(STAGING_AREA_DIR, "removal");


    /* INITIALIZE REPO */

    /**
     * Initialize .gitlet repo when init command passes in.
     * First, set up all necessary directories in .gitlet.
     * Then, set up an initial commit and master pointer pointing to this commit.
     * Let the HEAD pointer points to where master pointer points to.
     * If .gitlet has already exists, print error message.
     */
    public static void initGitlet() {
        if (GITLET_DIR.exists()) {
            error("A Gitlet version-control system already exists in the current directory.");
        }

        setupPersistence();

        Commit init = new Commit();
        String sha = getSha(init);
        saveCommit(init);

        Pointer master = new Pointer(sha);
        savePointer(master);
        master.updateHEAD();
    }

    /**
     * Does require filesystem operations to allow for persistence
     * The structure of a gitlet repository is as follows:
     * .gitlet/ -- top level folder for all persistent data in your project2 folder
     * - HEAD -- file containing name of the active pointer
     * - commits/ -- folder containing all commits objects created.
     * - blobs/ -- folder containing all blobs objects created.
     * - pointers/ -- folder containing all pointers pointing to commit tree
     * - staging area/ -- folder containing stage for additional file and stage for removal file
     * - addition -- file containing information of files staged for addition
     * - removal -- file containing information of files staged for removal
     */
    private static void setupPersistence() {
        GITLET_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
        POINTERS_DIR.mkdir();
        createNewFile(HEAD_FILE);
        STAGING_AREA_DIR.mkdir();
        createNewFile(ADDITION_FILE);
        createNewFile(REMOVAL_FILE);
        initAddFile();
        initRmFile();
    }

    /**
     * Create an empty HashMap for addition file and save it
     */
    private static void initAddFile() {
        File addFile = join(ADDITION_FILE);
        HashMap<String, String> add = new HashMap<>();
        writeObject(addFile, add);
    }

    /**
     * Create an empty HashMap for removal file and save it
     */
    private static void initRmFile() {
        File addFile = join(REMOVAL_FILE);
        HashMap<String, String> rm = new HashMap<>();
        writeObject(addFile, rm);
    }


    /* ADD FILES INTO STAGED FOR ADDITION */

    /**
     * Add a copy of the file named NAME as it currently
     * exists to addition file of staging area directory.
     * This is also called staging file for addition.
     * If the file does not exist, print the error message.
     * If the current working version of the file is identical
     * to the version in the current commit, do not stage it to be
     * added, and remove it from the staging area if it's already exist.
     * The file will no longer be staged for removal, if it was at the
     * time of this command.
     */
    public static void addFile(String name) {
        File file = join(CWD, name);

        if (!file.exists()) {
            error("File does not exist.");
        }

        String blob = readContentsAsString(file);
        if (isTracked(name) && isIdenticalCommitBlob(name) || isStagedRm(name)) {
            unstageAdd(name);
            unstageRm(name);
            return;
        }
        saveBlob(blob);

        stageAdd(name, blob);
    }

    /**
     * Check if the current working version of the file
     * named NAME is identical to the version in the current commit.
     */
    private static boolean isIdenticalCommitBlob(String name) {
        File file = join(CWD, name);
        String blob = readContentsAsString(file);
        Commit currentCommit = getHeadCommit();
        HashMap<String, String> blobs = currentCommit.getBlobs();
        String blobSHAInCommit = blobs.get(name);
        String blobInCommit = getContentOfBlob(blobSHAInCommit);
        return blob.equals(blobInCommit);
    }


    /* CREATE A NEW COMMIT, SAVE A SNAPCHAT OF TRACTED FILES */

    /**
     * Save a snapshot of tracked files in the current commit
     * and staging area so that they can be restored at a later time,
     * create a new commit.
     * Files tracked in the current commit may be untracked in the
     * new commit as a result being staged for removal.
     * If newMessage is blank, print error message and exit.
     */
    public static void createNewCommit(String newMessage) {
        createNewCommit(newMessage, null);
    }

    private static void createNewCommit(String newMessage, Commit p2) {
        if (newMessage.isBlank()) {
            error("Please enter a commit message.");
        }

        String currentTime = date(1);

        Commit pre = getHeadCommit();
        Commit current = pre.copyCommit();

        HashMap<String, String> oldBlobs = current.getBlobs();
        HashMap<String, String> newBlobs = updateBlobs(oldBlobs);
        current.updateCommit(newMessage, currentTime, newBlobs, getSha(pre), getSha(p2));
        saveCommit(current);

        Pointer active = getActivePointer();
        active.updateSHA(getSha(current));
        savePointer(active);
    }

    /**
     * Create new blobs, add all key-pairs from old blobs and then
     * add all key-pairs got from addition file to it.
     * Remove all key-pairs of removal file from the new blobs.
     * Reset addition file and removal file at last.
     * If no files have been staged, print error message and exit.
     */
    private static HashMap<String, String> updateBlobs(HashMap<String, String> oldBlobs) {
        HashMap<String, String> newBlobs = new HashMap<>();
        HashMap<String, String> addFile = getAddFile();
        HashMap<String, String> rmFile = getRmFile();
        if (addFile.isEmpty() && rmFile.isEmpty()) {
            error("No changes added to the commit.");
        }

        for (String name : oldBlobs.keySet()) {
            newBlobs.put(name, oldBlobs.get(name));
        }

        for (String name : addFile.keySet()) {
            newBlobs.put(name, addFile.get(name));
        }
        initAddFile(); // Reset addition file as an empty HashMap

        for (String name : rmFile.keySet()) {
            newBlobs.remove(name);
        }
        initRmFile(); // Reset removal file as an empty HashMap

        return newBlobs;
    }


    /* REMOVE FILE */

    /**
     * Unstage the file if it is currently staged for addition.
     * If the file is tracked in the current commit, stage it
     * for removal and remove the file from the working directory
     * if the user has not already done so.
     * If the file is neither staged for addition nor tracked by
     * the current commit, print error message.
     */
    public static void rmFile(String name) {
        File file = join(CWD, name);

        if (isStagedAdd(name)) {
            unstageAdd(name);
            return;
        } else if (isTracked(name)) {
            stageRm(file);
            restrictedDelete(file);
            return;
        }

        error("No reason to remove the file.");
    }


    /* LOG OF COMMIT TREE */

    /**
     * Starting at the current head commit, display information
     * about each commit backwards along the commit tree until the
     * initial commit, following the first parent commit links,
     * ignoring any second parents found in merge commits.
     * For very node in the tree, the information it should display is:
     * - the commit id
     * - the time the commit was made
     * - the commit message
     * There is a === before each commit and an empty line after it.
     */
    public static void log() {
        Commit head = getHeadCommit();

        while (head != null) {
            displayInfo(head);
            head = head.getFirstCommit();
        }
    }

    /**
     * Display information of a node C
     */
    private static void displayInfo(Commit c) {
        String sha = getSha(c);
        String date = c.getTimeStamp();
        String message = c.getMessage();
        message("===\n" + "commit " + sha + "\n" + "Date: " + date + "\n" + message + "\n");
    }

    /**
     * Like log, except displays info about all commits ever made.
     */
    public static void globalLog() {
        List<String> listOfFile = allPlainFilesIn(COMMITS_DIR);
        if (listOfFile != null) {
            for (String sha : listOfFile) {
                Commit c = getCommit(sha);
                displayInfo(c);
            }
        }
    }


    /**
     * Print out the ids of all commits that have the given
     * commit message, one per line. If there are multiple such
     * commits, it prints the ids out on separate lines.
     * If no such commit exists, print error message and exit.
     */
    public static void find(String message) {
        List<String> listOfFile = allPlainFilesIn(COMMITS_DIR);
        boolean exist = false;
        if (listOfFile != null) {
            for (String sha : listOfFile) {
                Commit c = getCommit(sha);
                if (c.getMessage().equals(message)) {
                    exist = true;
                    System.out.println(sha);
                }
            }
        }

        if (!exist) {
            error("Found no commit with that message.");
        }
    }


    /* THE STATUS OF CURRENT COMMIT TREE */

    /**
     * Displays what branches currently exist, and marks the current branch with a *.
     * Also displays what files have been staged for addition or removal,
     * modifications not staged for commit and untracked files.
     * There is an empty line between sections and the entire status ends in an empty
     * line as well. Entries of each section should be listed in lexicographic order,
     * using the Java string-comparison order.
     */
    public static void status() {
        String headPointer = readContentsAsString(HEAD_FILE);
        List<String> listOfPointers = plainFilenamesIn(POINTERS_DIR);
        message("=== Branches ===");
        printBranches(headPointer, listOfPointers);
        System.out.println();

        HashMap<String, String> addFile = getAddFile();
        Set<String> addedFiles = addFile.keySet();
        message("=== Staged Files ===");
        printStagingArea(addedFiles);
        System.out.println();

        HashMap<String, String> rmFile = getRmFile();
        Set<String> removedFiles = rmFile.keySet();
        message("=== Removed Files ===");
        printStagingArea(removedFiles);
        System.out.println();

        message("=== Modifications Not Staged For Commit ===");
        printMNSFC();
        System.out.println();

        message("=== Untracked Files ===");
        printUntracked();
        System.out.println();
    }

    /**
     * Print out Branches section
     */
    private static void printBranches(String h, List<String> lst) {
        if (lst != null) {
            for (String p : lst) {
                if (p.equals(h)) {
                    p = "*" + p;
                }
                message(p);
            }
        }
    }

    /**
     * Print out Staged File section and Removed File section
     */
    private static void printStagingArea(Set<String> files) {
        TreeSet<String> sorted = new TreeSet<>((f1, f2) -> f1.compareTo(f2));
        sorted.addAll(files);
        if (sorted != null) {
            for (String f : sorted) {
                message(f);
            }
        }
    }

    /**
     * Print out Modifications Not Staged For Commit(MNSFC) section
     */
    private static void printMNSFC() {
        Commit headCommit = getHeadCommit();
        HashMap<String, String> addFile = getAddFile();
        HashMap<String, String> blobs = headCommit.getBlobs();
        List<String> listOfFiles = plainFilenamesIn(CWD);

        if (listOfFiles != null) {
            for (String name : listOfFiles) {
                if (isTracked(name) && !isIdenticalCommitBlob(name)
                        && !isStagedAdd(name) && !isStagedRm(name)) {
                    message(name + " (modified)");
                } else if (isStagedAdd(name) && !isIdenticalAddBlob(name)) {
                    message(name + " (modified)");
                } else {
                    blobs.remove(name);
                }
            }

            for (String name : addFile.keySet()) {
                File f = join(CWD, name);
                if (!isExist(f)) {
                    message(name + " (deleted)");
                }
            }

            if (!blobs.isEmpty()) {
                for (String name : blobs.keySet()) {
                    File f = join(CWD, name);
                    // Why isStageAdd(name)? Because users may track the
                    // file first, then change it, add it, then delete it.
                    if (!isStagedRm(name) && !isExist(f) && !isStagedAdd(name)) {
                        message(name + " (deleted)");
                    }
                }
            }
        }
    }

    /**
     * Check if content of file named NAME is identical to content of file in addition file
     */
    private static boolean isIdenticalAddBlob(String name) {
        HashMap<String, String> addFile = getAddFile();
        File f = join(CWD, name);
        String content = readContentsAsString(f);
        String blobSHA = addFile.get(name);
        String blob = getContentOfBlob(blobSHA);
        return content.equals(blob);
    }

    /**
     * Print out Untracked Files section
     */
    private static void printUntracked() {
        List<String> listOfFiles = plainFilenamesIn(CWD);

        if (listOfFiles != null) {
            for (String name : listOfFiles) {
                if (isUntracked(name)) {
                    message(name);
                }
            }
        }
    }


    /* CHECKOUT FILES OR A BRANCH */

    /**
     * Takes the version of the file named NAME as ite exists in the head commit
     * and puts it in the working directory, overwriting the version of the file
     * that's already there if there is one. The new version of the file is not staged.
     * If the file does not exist in the head commit, print the error message.
     */
    public static void checkoutHeadFile(String name) {
        checkoutFile(getHeadSha(), name);
    }

    /**
     * Takes the version of the file named NAME as it exists in the commit with the given id,
     * and puts it in the working directory, overwriting the version of the file that's
     * already there if there is one. The new version of the file is not staged.
     * If no commit with the given id exists, print error message.
     * If the file does not exist in the given commit, print error message.
     */
    public static void checkoutFile(String id, String name) {
        if (id.length() < 40) {
            id = findCompleteId(id);
        }

        File commit = join(COMMITS_DIR, first2Sha(id), rest38Sha(id));
        if (!isExist(commit)) {
            error("No commit with that id exists.");
        }

        if (!isTrackedBy(id, name)) {
            error("File does not exist in that commit.");
        }

        File f = join(CWD, name);
        HashMap<String, String> headBlobs = getCommit(id).getBlobs();
        String blobSHA = headBlobs.get(name);
        String content = getContentOfBlob(blobSHA);
        writeContents(f, content);
    }

    /**
     * Given a shortened sha-1 hash, find its complete one
     */
    private static String findCompleteId(String id) {
        String complete = "";
        String first2 = first2Sha(id);
        File subDir = join(COMMITS_DIR, first2);
        if (!subDir.exists()) {
            error("No commit with that id exists.");
        }

        complete += first2;
        List<String> lst = plainFilenamesIn(subDir);
        for (String rest38 : lst) {
            if (rest38.contains(restSha(id))) {
                complete += rest38;
            }
        }

        if (complete.length() != 40) {
            error("No commit with that id exists.");
        }
        return complete;
    }

    /**
     * Takes all files in the commit at the head of the given branch named BRANCH,
     * and puts them in the working directory, overwriting the versions of the files
     * that are already there if they exist. Also, any files tracked in the current
     * branch but not presented in the checked-out branch are deleted.
     *
     * Additionally, the given branch will now be considered the current branch(HEAD).
     * The staging area is cleared unless the checked-out branch is the current branch.
     *
     * If no branch with that name exists or if that branch is the current branch,
     * print error message and exit. If a working file is untracked in the current
     * branch, and may be overwritten by this command, also print error message and exit.
     */
    public static void checkoutBranch(String branch) {
        File b = join(POINTERS_DIR, branch);
        String activeBranch = getHEAD();
        if (!b.exists()) {
            error("No such branch exists.");
        } else if (activeBranch.equals(branch)) {
            error("No need to checkout the current branch.");
        }
        Pointer branchPointer = getPointer(b);
        String newId = branchPointer.pointingTo();
        changeCwd(getHeadSha(), newId);
        clearStagingArea();
        branchPointer.updateHEAD();
    }


    /* CREATE A NEW BRANCH */

    /**
     * Create a new branch with the given name BRANCH, and points it at the current
     * head commit. A branch is nothing more than a name for a pointer pointing
     * to a commit node.
     * <p>
     * This command does NOT immediately switch to the newly created branch.
     * Before branch being called, code should be running with a default branch
     * called "master".
     */
    public static void createNewBranch(String branch) {
        File b = join(POINTERS_DIR, branch);
        if (isExist(b)) {
            error("A branch with that name already exists.");
        }

        Commit headCommit = getHeadCommit();
        Pointer newBranch = new Pointer(branch, getSha(headCommit));
        savePointer(newBranch);
    }


    /* DELETE A BRANCH */

    /**
     * Delete the branch with the given name BRANCH. This only means to delete
     * the pointer associated with the branch. It does not mean to delete all
     * commits that were created under the branch.
     */
    public static void deleteBranch(String branch) {
        File b = join(POINTERS_DIR, branch);
        String activeBranch = getHEAD();
        if (!b.exists()) {
            error("A branch with that name does not exist.");
        } else if (activeBranch.equals(branch)) {
            error("Cannot remove the current branch.");
        }

        b.delete();
    }


    /* RESET COMMAND */

    /**
     * Check out all the files tracked by the given commit id. Remove tracked files
     * that are not present in that commit. Also move the current branch's head to
     * that commit node. The staging area is cleared.
     * If no commit with the given id exists, print error message.
     * If there is an untracked file in the current branch, and it may be overwritten
     * by this command, print error message.
     */
    public static void reset(String id) {
        if (id.length() < 40) {
            id = findCompleteId(id);
        }

        File f = join(COMMITS_DIR, first2Sha(id), rest38Sha(id));
        if (!f.exists()) {
            error("No commit with that id exists.");
        }
        Pointer activePointer = getActivePointer();
        changeCwd(activePointer.pointingTo(), id);
        activePointer.updateSHA(id);
        savePointer(activePointer);
        clearStagingArea();
    }

    /**
     * Check out all the files tracked by the new commit NEWID from files
     * tracked by the old commit ID. Remove tracked files that are not present
     * in that new commit. If there is a file untracked in the old commit,
     * but it is tracked in the new commit, print error message.
     */
    private static void changeCwd(String id, String newId) {
        List<String> listOfFile = plainFilenamesIn(CWD);
        if (!listOfFile.isEmpty()) {
            for (String name : listOfFile) {
                if (isUntrackedBy(id, name) && isTrackedBy(newId, name)) {
                    error("There is an untracked file in the way; "
                            + "delete it, or add and commit it first.");
                } else if (isTrackedBy(id, name)) {
                    File f = join(CWD, name);
                    f.delete();
                }
            }
        }

        HashMap<String, String> blobs = getCommit(newId).getBlobs();
        for (String name : blobs.keySet()) {
            String blobSha = blobs.get(name);
            File f = join(CWD, name);
            writeContents(f, getContentOfBlob(blobSha));
        }
    }


    /* MERGE A BRANCH WITH THE ACTIVE ONE */

    public static void merge(String g) {
        File other = join(POINTERS_DIR, g);
        if (!getAddFile().isEmpty() || !getRmFile().isEmpty()) {
            error("You have uncommitted changes.");
        } else if (!other.exists()) {
            error("A branch with that name does not exist.");
        } else if (g.equals(getHEAD())) {
            error("Cannot merge a branch with itself.");
        }

        String c = getHEAD();
        Commit split = findSplitPoint(c, g);
        String splitId = getSha(split);
        Commit curr = getHeadCommit();
        String givenId = getPointer(join(POINTERS_DIR, g)).pointingTo();
        Commit given = getCommit(givenId);
        boolean check = false;
        boolean conflict = false;

        if (splitId.equals(givenId)) {
            error("Given branch is an ancestor of the current branch.");
        } else if (splitId.equals(getHeadSha())) {
            checkoutBranch(g);
            error("Current branch fast-forwarded.");
        }

        TreeSet<String> files = getFilesSetOf(split, curr, given);
        for (String f : files) {
            File file = join(CWD, f);
            if (file.exists()
                    && (isUntrackedBy(getHeadSha(), f)
                    && split.hasFile(f) && given.hasFile(f) && !isModified(split, given, f)
                    || !split.hasFile(f) && !curr.hasFile(f) && given.hasFile(f))) {
                error("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
            }
        }

        for (String f : files) {
            if (split.hasFile(f)) {
                if (curr.hasFile(f) && given.hasFile(f)) {
                    addModifiedFilesToMerge(split, given, curr, f);
                    addModifiedFilesToMerge(split, curr, given, f);
                    if (isModified(split, curr, f) && isModified(split, given, f)) {
                        check = modifiedSame(curr, given, f);
                    }
                } else {
                    rmFilesToMerge(split, curr, given, f);
                    rmFilesToMerge(split, given, curr, f);
                    if (isRmAndModified(split, given, curr, f)
                            || isRmAndModified(split, curr, given, f)) {
                        check = modifiedSame(curr, given, f);
                    }
                }
            }

            if (!split.hasFile(f)) {
                addNewFilesToMerge(curr, given, f);
                addNewFilesToMerge(given, curr, f);
                if (curr.hasFile(f) && given.hasFile(f)) {
                    check = modifiedSame(curr, given, f);
                }
            }

            if (check) {
                conflict = true;
            }
        }

        String message = String.format("Merged %s into %s.", g, c);
        createNewCommit(message, given);

        if (conflict) {
            message("Encountered a merge conflict.");
        }
        System.exit(0);
    }

    /** Given two branches CURRENT and OTHER, find out the latest ancestor of them */
    private static Commit findSplitPoint(String current, String other) {
        String c = getPointer(join(POINTERS_DIR, current)).pointingTo();
        String o = getPointer(join(POINTERS_DIR, other)).pointingTo();
        SplitPointFinder finder = new SplitPointFinder(c, o);
        return finder.findSplitPoint();
    }

    /** Filter all files appearing in split point, current commit and given commit */
    private static TreeSet<String> getFilesSetOf(Commit splitPoint, Commit current, Commit other) {
        TreeSet<String> files = new TreeSet<>();
        filter(files, splitPoint);
        filter(files, current);
        filter(files, other);
        return files;
    }

    /** Add names of all appearing files in commit C in a TreeSet F and return F */
    private static TreeSet<String> filter(TreeSet<String> f, Commit c) {
        HashMap<String, String> sp = c.getBlobs();
        if (!sp.isEmpty()) {
            for (String file : sp.keySet()) {
                if (!f.contains(file)) {
                    f.add(file);
                }
            }
        }
        return f;
    }

    /** If the file named F is modified in commit MODIFIED but not in commit NOTMODIFIED
     *  (These two commits should neither be current commit nor given commit) from the SPLIT point,
     *  stage the modified version of F to addition
     */
    private static void addModifiedFilesToMerge(Commit split, Commit modified,
                                                Commit notModified, String f) {
        if (isModified(split, modified, f) && !isModified(split, notModified, f)) {
            String id = modified.getBlobIdOf(f);
            overwrite(f, id);
            stageAdd(f, getContentOfBlob(id));
        }
    }

    /** If the file named F is a new file for HASNEWFILES commit but not a new file
     *  for NOTHASNEWFILES commit(These two commits should be neither current commit
     *  nor given commit) from the SPLIT point, stage F to addition
     */
    private static void addNewFilesToMerge(Commit hasNewFiles, Commit notHasNewFiles, String f) {
        if (hasNewFiles.hasFile(f) && !notHasNewFiles.hasFile(f)) {
            String id = hasNewFiles.getBlobIdOf(f);
            overwrite(f, id);
            stageAdd(f, getContentOfBlob(id));
        }
    }

    /** If the file named F is not modified in UNMODIFIED commit but removed in REMOVED commit
     *  (These two commits should be neither current commit nor given commit), stage F to removal
     */
    private static void rmFilesToMerge(Commit split, Commit unModified, Commit removed, String f) {
        if (!isModified(split, unModified, f) && isRemoved(split, removed, f)) {
            File file = join(CWD, f);
            stageRm(file);
            restrictedDelete(file);
        }
    }

    /** If the file named F is modified in MODIFIED commit but removed in REMOVED commit,
     *  (These two commits should be neither current commit nor given commit), return true
     */
    private static boolean isRmAndModified(Commit split, Commit modified,
                                           Commit removed, String f) {
        if (isModified(split, modified, f) && isRemoved(split, removed, f)) {
            return true;
        }
        return false;
    }

    /**
     * Check if the file named F is modified in the same way from split point
     * to the commit CURR or GIVEN. If it is not modified in the same way, replace
     * content of F as conflict information, stage it for addition and return true.
     * Otherwise, do nothing and return false
     */
    private static boolean modifiedSame(Commit curr, Commit given, String f) {
        File file = join(CWD, f);
        String currCont = getContentOfBlob(curr.getBlobIdOf(f));
        String givenCont = getContentOfBlob(given.getBlobIdOf(f));

        String newContent = String.format("<<<<<<< HEAD\n%s=======\n%s>>>>>>>\n",
                currCont, givenCont);
        if (!hasSameBlob(curr, given, f)) {
            overwrite(file, newContent);
            addFile(f);
            return true;
        }
        return false;
    }
}
