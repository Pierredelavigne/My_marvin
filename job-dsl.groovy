folder("/Tools") {
    description("Folder for miscellaneous tools.")
    displayName("Tools")
}
job("./Tools/clone-repository") {
    parameters {
        stringParam("GIT_REPOSITORY_URL", "", "Git URL of the repository to clone.")
    }
    scm {
        git {
            remote {
                url("$GIT_REPOSITORY_URL")
            }
        extensions {
            cleanBeforeCheckout()
            }
        }
    }
    steps {
        shell("echo Cloning repository from $GIT_REPOSITORY_URL...")
        shell("git clone $GIT_REPOSITORY_URL")
    }
    triggers {
        cron('') 
        scm('')
    }
}
job("/Tools/SEED") {
    parameters {
        stringParam("GITHUB_NAME", "", "GitHub repository owner/repo_name (e.g.: \"EpitechIT31000/chocolatine\")")
        stringParam("DISPLAY_NAME", "", "Display name for the job")
    }
    steps {
        jobDsl {
            script(readFileFromWorkspace("job_dsl.groovy"))
            removedJobsAction("IGNORE")
            removedViewsAction("IGNORE")
        }
    }
    triggers {
        cron('') 
        scm('')
    }
}
job("SEED-JOB") {
    displayName("$DISPLAY_NAME")
    githubProjectUrl("https://github.com/$GITHUB_NAME")
    scm {
        git {
            remote {
                url("https://github.com/$GITHUB_NAME")
            }
        extensions {
            cleanBeforeCheckout()
            }
        }
    }
    steps {
        shell("make fclean")
        shell("make")
        shell("make tests_run")
        shell("make clean")
    }
    triggers {
        scm('*/1 * * * *')
        cron('')
    }
}
