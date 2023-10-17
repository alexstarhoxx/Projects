# Gitlet Design Document

This is a project originated from Project 2 of CS61B, University of California, Berkeley.

The specification can be seen from [this linked](https://sp21.datastructur.es/materials/proj/proj2/proj2).

**Name**: Alex Ho

## Classes and Data Structures
### Main
This is the entry point to gitlet program. It takes in arguments from the command line
and based on the commands(the elements of the `args` array) call the corresponding
command in `Repository` which will actually execute the logic of the command.

It also validates if there is any command passed in, and validates the arguments based 
on the command to ensure that enough arguments are passed in and arguments are corresponding
to different commands' requirement.

#### Fields
This class has no fields and hence no associated state. It simply validates arguments
and defers the execution to `Repository` class.


### Repository
This is where the main logic of our program will live. The file will be responsible for
setting up persistence if there is no `.gitlet` folder. Also, The file will handle all 
of the logic of actual gitlet commands by reading/writing from/to the correct file, error 
checking for each command.

There are also some private methods written below each command. These private methods
are only used for its specific method for implementing a command above them, not to be 
used repeatedly in other command methods.

#### Fields
1. `public static final File CWD = new File(System.getProperty("user.dir"));` The current
working directory. It is useful for the other `File` objects we need to use.
2. `public static final File GITLET_DIR = join(CWD, ".gitlet");` The `.gitlet` directory.
This will be initialized when the user use `init` command. This is also where all states
of the `Repository` will be stored, including blobs, commits, staging areas and pointers.
3. `public static final File COMMITS_DIR = join(GITLET_DIR, "commits");` The `commits`
directory where stores all of commits objects. All commit objects will be serialized to
or deserialized from the files in this directory. The first 2-digit of a commit SHA-1 id
will be the name of a subdirectory and the rest 38-digit will be the name of the commit 
file stored in this subdirectory. Other commits with the same first 2-digit should be stored 
in the same subdirectory.
4. `public static final File BLOBS_DIR = join(GITLET_DIR, "blobs")` The `blobs` directory
where we will store all blob objects. All objects will be serialized to or deserialized 
from the files of this directory. The way of saving files is the same as commit objects in
`commits` directory.
5. `public static final File POINTERS_DIR = join(GITLET_DIR, "pointers")` The `pointers`
directory where we will store all pointer objects in the files named by their pointer's name.
All pointer objects will also be serialized to or deserialized from these files. The default
pointer will be `master` as the repository is initialized.
6. `public static final File HEAD_FILE = join(GITLET_DIR, "HEAD")` The `HEAD` file represents
the Head pointer in gitlet. It stores the name of the active current pointer. The name of the 
pointer should be any name from `pointers` directory.
7. `public static final File STAGING_AREA_DIR = join(GITLET_DIR, "staging area")` The `staging area`
directory where there are perpetually only two files named `addition` and `removal` respectively.
8. `public static final File ADDITION_FILE = join(STAGING_AREA_DIR, "addition")` The `addition` file
where stores files staged for addition.
9. `public static final File REMOVAL_FILE = join(STAGING_AREA_DIR, "removal")` The `removal` file
where stores files staged for removal.

Since all of them are modified by `public`, other classes in the package can use these fields.

### Commit
This represents a gitlet commit object.
This class should be implements `serializable`
for it may be serialized to the `COMMIT_DIR`
which is within the `.gitlet`.

#### Fields
1. `private String message` The message saved in this commit
2. `private String timeStamp` The timestamp of this commit when
the user commits
3. `private HashMap<String, String> blobs` The blobs saved in this
commit, mapping files name to its corresponding blob id
4. `private String first` The first parent of this commit, which should be the head of active branch
when creating a merge commit, indicating by using SHA-1 hash code
5. `private String second` The second parent of this commit, which should be the head of the given branch
given in the command line when creating a merge commit, indicating by using SHA-1 hash code


### Pointer
This represents a gitlet pointer object. It should implement 
`serializable` for it may be saved to `POINTER_DIR` in `.gitlet`

#### Fields
1. `private String name` The name of this pointer
2. `private String commitSHA` The commit that this pointer is pointing to


### SplitPointFinder
This class represents an object whose duty is to find the split point of two summit commits 
of two branches: current branch and given branch(if any).

This class will only be instantiated when implementing `merge` command. There are some
useful methods that will find the latest split point corresponding to the respective 
`String` SHA-1 of two summit commits given to it.

#### Fields
1. `private String current`  The SHA-1 hash of a summit commit that the active branch is pointing to
2. `private String given` The SHA-1 hash of a summit commit that the other branch is pointing to
3. `private TreeMap<String, Integer> marked` Map of each commit's id to its distance to
the commit that the active branch is pointing to
4. `private TreeMap<String, Integer> splitPoints` Map of each split point's id to its distance
to the commit that the active branch is pointing to


### HelperMethods
This class contains manifold static helper methods for:
* Save objects to directories(including `commit`, `blobs`, `pointers`, `addition`, `removal`) 
within `.gitlet`
* Read string of contents or objects from directories within `.gitlet`
* Stage or unstage files to `addition` or `removal`
* Check if a file is staged or unstaged to `addition` or `removal`
* Manipulation for a single file
* All helper methods only for implementing the merge command
* SHA-1 related methods, date and error methods

#### Fields
This class has no fields and hence no associated state. It is just a list of static helper
methods to use in other classes.


### Utils
This class contains helpful utility methods to read/write objects or `String` contents
from/to files, as well as reporting errors when they occur**.

#### Fields
Only some `private` fields to aid in the magic.


## Algorithms

## Persistence
### Directory Structure
The directory structure looks like this:
```
CWD                      <=== Whatever the current working directory is.
`-- .gitlet              <=== All persistant data is stored within here.
    |-- HEAD             <=== Store the String name of the current active pointer in this file.
    |-- blobs            <=== All different blobs of files are stored in this directory.
    |   `-- ab           <=== All blobs with a common previous 2-digit SHA-1 hash id will be stored in a directory.
    |       `-- f3783sd7f82ji23kj2nksld21aaf9772dbb037   <=== A single blob object is stored in a file.
    |-- commits          <=== All commits objects are stored in this directory.
    |   `-- fc           <=== All commits with a common previous 2-digit SHA-1 hash id will be stored in a directory.
    |       `-- f75ef4d63c3062994b1a81921aaf9772dbb037   <=== The initial commit stored in a file.
    |-- pointers         <=== All pointers objects are stored in this directory
    |   `-- master       <=== The default pointer object is stored in this file.
    |   `-- pointer 1    <=== A single pointer object is stored in a file.
    `-- staging area     <=== The area of staged for addition and staged for removal are stored in this directory.
        |-- addition     <=== The map of staged for addition is stored in this file.
        `-- removal      <=== The map of staged for removal is stored in this file.
```
#### The Way of Saving Blobs Objects and Commits Objects in their Directory
Within the directory of `blobs` or `commits`, the first 2-digit of an object's
(whether blobs or commits) SHA-1 id will be the name of a subdirectory and the rest 
38-digit will be the name of the file stored in this subdirectory. Others with the same
first 2-digit should be stored in the same subdirectory but file with different name. 
All objects will be serialized to or deserialized from the files in their corresponding 
subdirectory.

#### Interpretation of Basic Structure
When type `init`, the static method `initGitlet()` in `Repository` will set up all persistence.
It will do the following things:
1. Create `.gitlet` folder if it does not already exist.
2. Create `HEAD` file.
3. Create an empty directory named `blobs`.
4. Create a directory named `commits` and save the initial commit object into it.
5. Create a directory named `pointers`, create a file named `master` and save the initial
commit's SHA-1 hash id into it.
6. Create a directory named `staging area` and create two files named `addition`
and `removal` respectively. Each of the files contain an emtpy hash map.

### Useful Helper Methods for Persistence
There are several very useful methods set up in `HelperMethods` file in order to 
* Create or initialize required directories and files on `.gitlet` directory. 
* Help serialize/deserialize the contents of files or the objects from/to `.gitlet` directory and its sub-directories.

#### Initialization
This section includes all the methods for initialization when `init` command is called. These methods will also be used to re-initalize some files for some commands. All these files are on `Repository.java` file.
1. `private static void initAddFile()` - Create a file called `addition` on `.staging area`, create an empty hash map and save it to that file.
2. `private static void initRmFile()` - Create a file called `removal` on `.staging area`, create an empty hash map and save it to that file.


#### Staging/Unstaging Files and its Blob Id
1. `public static void unstageAdd(String name)` - It will first deserialize the hash map on
`.staging area/addition`, remove the key-value pair where key is the file `name`, and then serialize
that hash map back to that `addition` file.
2. `public static void unstageRm(String name)` - It is almost the same logic as `unstageAdd`
method, except that it deserialize the hash map on `.staging area/removal` and serialize it back
to this file.
3. `public static void stageAdd(String name, String blob)` - It will first deserialize the hash map on `.staging area/addition`, then add a pair of the given name of a file to its correponding blob id to this hash map. Finally it serialize that updated map back to `addition` file.
4. `public static void stageRm(String name, String id)` - It is almose the same logic as `stageAdd` method, except that it remove the pair of the given name of a file to its blob id from the hash map.

#### Get Objects/Contents from Files of `.gitlet`
In this section, all the methods are used to deserialize, or get the objects (including commits, pointers, blobs and hash maps for addition and removal repectively) and contents (including contents from HEAD and master files) from different files of `.gitlet`

1. `public static Commit getHeadCommit()` - Deserialize the commit object that HEAD pointer pointing to from `commits` directory.
2. `public static HashMap<String, String> getAddFile()` - Deserialize the hash map noting file names to be staged.
3. `public static HashMap<String, String> getRmFile()` - Almost same as `getAddFile`, except the hash map is for noting
file names to be staged for removal, and deserialize it from `.staging area/removal` file.
4. `public static Pointer getActivePointer()` - Deserialize the HEAD pointer object from `pointers` directory.


#### Save Objects/Contents to Files of `.gitlet`
In this section, all the methods are used to serialize, or save the objects and contents to different files of `.gitlet`

1. `public static void savePointer()` - Deserialize a pointer object to `pointer` directory.


### Add Files into Staged for Addition
When the `add [a file's name]`command is used, we will do one of two things:
1. If that file has already tracked by the commit the active pointer pointing to, and
it is staged for removal or it has the same content as tracked commit's blob, we will
use `HelperMethods.unstageRm` and `HelperMethods.unstageAdd` respectively in order to
remove the key-value pair of this file from hash maps on `.staging area/removal` and
`.staging area/addition` files.
2. Otherwise, firstly the corresponding blob of this file will be saved to `blobs` 
directory by using `HelperMethods.saveBlob`. Then, the file name and its corresponding
blob's SHA-1 hash id will be saved in `.staging area/addition` file by using `HelperMethods.stageAdd`.

The `HelperMethods` class will handle the serialization and deserialization of blob objects
and change of `addition` and `removal` files. There are three methods that are useful for these:
1. `public static void unstageAdd(String name)`
2. `public static void unstageRm(String name)`
3. `public static void stageAdd(String name, String blob)`

### Creat a New Commit
When the `commit [message]` is used, it will:
1. Use `HelperMethods.getHeadCommit` to get the commit the HEAD pointer pointing to from `commits`
directory.
2. After getting `blobs` of the head commit, use `HelperMethods.getAddFile` and `HelperMethods.getRmFile`
to get hash map from `.staging area/addition` and `.staging area/removal` respectively. 
3. After updating the `blobs` of the head commit in accordance with addition and removal hash maps, use
`Repository.initAddFile` and `Repository.initRmFile` to reset the two hash maps as empty and finally save
them back to files respectively.
4. Updating all information of a new commit, it will be saved to `commits` directory by using `HelperMethods.saveCommit`.
5. Finally, use `HelperMethods.getActivePointer` to get the current pointer object and update the commit it
is pointing to. Then use `HelperMethods.savePointer` to save this object back to `pointers` directory.

The `HelperMethods` class has several useful methods for deserializing contents of files in `.gitlet` directory and
serializing objects or string of contents to files in that directory. There are 6 methods are used for these:
1. `public static Commit getHeadCommit()`
2. `public static HashMap<String, String> getAddFile()`
3. `public static HashMap<String, String> getRmFile()`
4. `public static Pointer getActivePointer()`
5. `public static void savePointer()`
6. `public static void initAddFile()`
7. `public static void initRmFile()`

### Remove a file from Current Commit or Addition area
When the `rm [a file name]` is used, it will:
1. If the file has already staged for addition, we will use `HelperMethod.unstageAdd` to remove file name and its blob id from the hash map of `.staging area/addition`.
2. If the file has already tracked by the current commit, we will use `HelperMethod.stageRm` to add file and its blob id into the hash map on `.staging area/removal`. Then, use `Utils.restrictedDelete` to delete this file from the working directory.

The `HelperMethods` class has ? useful methods for deserializing objects of files in `.gitlet` directory, serializing objects and deleting the file in that directory:
1. `public static void unstageAdd(String name)`
2. `public static void stageRm(String name, String id)`
