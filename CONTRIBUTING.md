## Contributing to Super Mario Paint
Most features will likely be rebased into main. Ideally the features are implemented as direct offshoot branches from the *main* branch.

### Git Proficiency
It is requested that people are familiar with Git to a certain degree to contribute, but I understand that not everyone is -- for more information here is a useful little site that will help with Git operations: https://learngitbranching.js.org/

#### Common Git Errors to Avoid
There are a few quirks of Git that will very quickly mess up your Git history if you're not careful
- *git pull*
    - Ideally you are working on your own branches most of the time so this problem is completely sidestepped
    - Sometimes however you want to update the *main* branch in some way    
    - *git pull* actually does a *git fetch* and then a *git merge*
    - This can quickly create extra commits that may be unwanted
    - Please make sure to avoid the *merge remote-tracking branch* problem with *git pull* by not using *git pull* all the time
    - A possible solution to this is to use *git pull --rebase* instead, which will replay all new commits on top of the *main*
    - You might be able to *git fetch* a repository and check out various commits and then rebase things manually if you're into doing that.
    - Sometimes the easiest solution is to re-clone the repository and put your changes on top of it.
    
- *git push*
    - When put in conjunction with *git pull* it can quickly mess up the repository history.

### Features
The general requested flow is:
- Fork the repository (this is necessary, unless you were granted write permissions for the repository)
- Branch off of *main* and implement your feature
    - Make sure to test it before proceeding to the next step
- Make a pull request that encapsulates the feature
- The pull request will be reviewed and tested to ensure functionality
- If it passes the above functionality tests it will be rebased and merged into *main*
- Once the feature has been merged you can delete the branch
	- If you want to make another feature it's recommended you branch off of the most updated *main* again


### Bugfixes
Bugfixes will generally follow the same above process.


### IDE Operations
Currently we use the Eclipse IDE to compile and run and release the code. There is a VSCode build setup being worked on.



