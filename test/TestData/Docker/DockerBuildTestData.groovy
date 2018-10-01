package TestData.Docker

import TestData.TestDataCombination

static Map commonVariables(){ [
        JOB_NAME: 'Job_Name',
        BRANCH_NAME: 'master',
        BUILD_ID: '1',
        DOCKER_REGISTRY: 'registry.com'
    ]
}

static Map commonVariablesWithoutDockerRegistry(){ [
        JOB_NAME: 'Job_Name',
        BRANCH_NAME: 'master',
        BUILD_ID: '1'
    ]
}

static suite_CustomDockerFilePathsWithDefaultImageNames(){
    TestDataCombination.join(customDockerFilePaths(), DockerTestData.defaultImageNames())
}
static suite_CustomDockerFilePathsWithCustomImageNames(){
    TestDataCombination.join(customDockerFilePaths(), DockerTestData.customImageNames())
}

static suite_DefaultDockerFilePathsWithDefaultImageNames(){
    TestDataCombination.join(defaultDockerFilePaths(), DockerTestData.defaultImageNames())
}

static suite_DefaultDockerFilePathsWithCustomImageNames(){
    TestDataCombination.join(defaultDockerFilePaths(), DockerTestData.customImageNames())
}

static customDockerFilePaths(){
    ['some_path', './some_path', '/home/some_path', '.', '\\\\']
}

static defaultDockerFilePaths(){
    [null, '', ' ']
}