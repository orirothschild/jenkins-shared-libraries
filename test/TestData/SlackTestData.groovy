package TestData

static Map commonVariables(){ [
        JOB_NAME: "FAKE_Job_Name",
        BRANCH_NAME: "FAKE_Branch_Name",
        BUILD_URL: "FAKE_Build_Url",
        BUILD_ID: "1234"
    ]
}
//channel null with combination of allure
static List<Object[]> suite_ChannelIsNull_AllureIsAny(){
    suite_AllureIsFalse_ChannelIsNull() +  suite_ChannelIsNull_AllureIsTrue()
}
static List<Object[]> suite_AllureIsFalse_ChannelIsNull(){
    TestDataCombination.join(null, falseAllure())
}
static List<Object[]> suite_ChannelIsNull_AllureIsTrue(){
    TestDataCombination.join(null, trueAllure())
}

//channel empty with combination of allure
static List<Object[]> suite_ChannelIsEmpty_AllureIsCorrect(){
    suite_ChannelIsEmpty_AllureIsFalse() +  suite_ChannelIsEmpty_AllureIsTrue()
}
static List<Object[]> suite_ChannelIsEmpty_AllureIsFalse(){
    TestDataCombination.join('', falseAllure())
}
static List<Object[]> suite_ChannelIsEmpty_AllureIsTrue(){
    TestDataCombination.join('', trueAllure())
}

//channel whitespace with combination of allure
static List<Object[]> suite_ChannelIsWhitespace_AllureIsAny(){
    suite_ChannelIsWhitespace_AllureIsFalse() +  suite_ChannelIsWhitespace_AllureIsTrue()
}
static List<Object[]> suite_ChannelIsWhitespace_AllureIsFalse(){
    TestDataCombination.join(' ', falseAllure())
}
static List<Object[]> suite_ChannelIsWhitespace_AllureIsTrue(){
    TestDataCombination.join(' ', trueAllure())
}

//channel #channel with combination of allure
static List<Object[]> suite_ChannelIsDefined_AllureIsAny(){
    suite_ChannelIsDefined_AllureIsFalse() +  suite_ChannelIsDefined_AllureIsTrue()
}
static List<Object[]> suite_ChannelIsDefined_AllureIsFalse(){
    TestDataCombination.join('#channel', falseAllure())
}
static List<Object[]> suite_ChannelIsDefined_AllureIsTrue(){
    TestDataCombination.join('#channel', trueAllure())
}

private static falseAllure(){
    [null, '', ' ', 'a', 'no', 0, '0', false]
}
private static trueAllure(){
    ['y', true, 'true', 1, '1']
}

