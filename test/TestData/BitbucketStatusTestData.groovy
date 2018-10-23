package TestData

static Map commonVariables(){
    [
        JOB_NAME: "Job_Name",
        BUILD_ID: 2
    ]
}
static Map commonVariablesForFirstBuild(){
    [
        JOB_NAME: "Job_Name",
        BUILD_ID: 1
    ]
}

static buildStateMap(){ ['SUCCESS': 'SUCCESSFUL', 'FAILURE': 'FAILED', 'UNSTABLE': 'FAILED'] }

static List<Object[]> suite_DefinedBuildStatus_ValidBitbucketStatuses(){
    TestDataCombination.join(definedBuildStatuses(), validBitbucketStatuses())
}

static List<Object[]> suite_UndefinedBuildStatus_ValidBitbucketStatuses(){
    TestDataCombination.join(undefinedBuildStatuses(), validBitbucketStatuses())
}

static List<Object[]> suite_DefinedBuildStatus_InvalidBitbucketStatuses(){
    TestDataCombination.join(definedBuildStatuses(), invalidBitbucketStatuses())
}

static List<Object[]> suite_UndefinedBuildStatus_InvalidBitbucketStatuses(){
    TestDataCombination.join(undefinedBuildStatuses(), invalidBitbucketStatuses())
}

static definedBuildStatuses(){
    ['SUCCESS', 'UNSTABLE', 'FAILURE']
}

static undefinedBuildStatuses(){
    ['UNDEFINED', '', ' ', null, 'ABORTED']
}

static validBitbucketStatuses(){
    ['INPROGRESS', 'SUCCESSFUL', 'FAILED']
}

static invalidBitbucketStatuses(){
    ['', ' ', 'custom']
}