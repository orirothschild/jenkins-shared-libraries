package TestData

static Map commonVariables(){ [
        JOB_NAME: "Job_Name"
    ]
}

static buildStateMap(){ ['SUCCESS': 'SUCCESSFUL', 'FAILURE': 'FAILED', 'UNSTABLE': 'FAILED'] }

static List<Object[]> suite_DefinedBuildStatus_CustomBitbucketStatuses(){
    TestDataCombination.join(definedBuildStatuses(), customBitbucketStatuses())
}

static List<Object[]> suite_UndefinedBuildStatus_CustomBitbucketStatuses(){
    TestDataCombination.join(undefinedBuildStatuses(), customBitbucketStatuses())
}

static List<Object[]> suite_DefinedBuildStatus_DefaultBitbucketStatuses(){
    TestDataCombination.join(definedBuildStatuses(), defaultBitbucketStatuses())
}

static List<Object[]> suite_UndefinedBuildStatus_DefaultBitbucketStatuses(){
    TestDataCombination.join(undefinedBuildStatuses(), defaultBitbucketStatuses())
}

static definedBuildStatuses(){
    ['SUCCESS', 'UNSTABLE', 'FAILURE']
}

static undefinedBuildStatuses(){
    ['UNDEFINED', '', ' ', null, 'ABORTED']
}

static customBitbucketStatuses(){
    ['custom', 'INPROGRESS']
}

static defaultBitbucketStatuses(){
    [null, '', ' ']
}