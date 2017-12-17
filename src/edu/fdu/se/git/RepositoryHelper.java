package edu.fdu.se.git;


import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;


public class RepositoryHelper {

    public static Repository openJGitCookbookRepository() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = null;
		try {
			repository = builder.setGitDir(new File(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH)+"/.git")).readEnvironment() // scan
					.findGitDir() // scan up the file system tree
					.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return repository;
    }

//    public static Repository createNewRepository() throws IOException {
//        // prepare a new folder
//        File localPath = File.createTempFile("TestGitRepository", "");
//        if(!localPath.delete()) {
//            throw new IOException("Could not delete temporary file " + localPath);
//        }
//
//        // create the directory
//        Repository repository = FileRepositoryBuilder.create(new File(localPath, ".git"));
//        repository.create();
//
//        return repository;
//    }
}
