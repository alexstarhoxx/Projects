package gitlet;

import static gitlet.Utils.*;
import static gitlet.Repository.*;
import static gitlet.HelperMethods.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Alex Ho
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        haveInput(args);
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                validateArgs(args, 1);
                initGitlet();
                break;
            case "add":
                validateArgs(args, 2);
                addFile(args[1]);
                break;
            case "commit":
                validateArgs(args, 2);
                createNewCommit(args[1]);
                break;
            case "rm":
                validateArgs(args, 2);
                rmFile(args[1]);
                break;
            case "log":
                validateArgs(args, 1);
                log();
                break;
            case "global-log":
                validateArgs(args, 1);
                globalLog();
                break;
            case "find":
                validateArgs(args, 2);
                find(args[1]);
                break;
            case "status":
                validateArgs(args, 1);
                status();
                break;
            case "checkout":
                if (args.length == 2) {
                    checkoutBranch(args[1]);
                } else if (args.length == 3 && validateSlashSlash(args[1])) {
                    checkoutHeadFile(args[2]);
                } else if (args.length == 4 && validateSlashSlash(args[2])) {
                    checkoutFile(args[1], args[3]);
                }
                break;
            case "branch":
                validateArgs(args, 2);
                createNewBranch(args[1]);
                break;
            case "rm-branch":
                validateArgs(args, 2);
                deleteBranch(args[1]);
                break;
            case "reset":
                validateArgs(args, 2);
                reset(args[1]);
                break;
            case "merge":
                validateArgs(args, 2);
                merge(args[1]);
                break;
            default:
                message("No command with that name exists.");
                System.exit(0);
        }
    }

    /** Check if there is an input as for ARGUS */
    private static void haveInput(String[] args) {
        if (args.length == 0) {
            error("Please enter a command.");
        }
    }

    /** Check if the inputs are the valid */
    private static void validateArgs(String[] args, int n) {
        if (!args[0].equals("init") && !GITLET_DIR.exists()) {
            error("Not in an initialized Gitlet directory.");
        } else if (args.length != n) {
            error("Incorrect operands.");
        }
    }

    /** Check if one of the input arguments is valid slash-slash */
    private static boolean validateSlashSlash(String s) {
        if (!s.equals("--")) {
            error("Incorrect operands.");
        }
        return true;
    }
}
