package TestData.Docker

static Map commonVariables(){ [
        JOB_NAME: 'Job_Name/master',
        BRANCH_NAME: 'master',
        BUILD_ID: '1',
        DOCKER_REGISTRY: 'registry.com'
    ]
}

static Map commonVariablesWithoutDockerRegistry(){ [
        JOB_NAME: 'Job_Name/master',
        BRANCH_NAME: 'master',
        BUILD_ID: '1'
    ]
}