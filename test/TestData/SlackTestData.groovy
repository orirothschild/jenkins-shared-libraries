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
    suite_AllureIsFalse_ChannelIs(null)
}
static List<Object[]> suite_ChannelIsNull_AllureIsTrue(){
    suite_AllureIsTrue_ChannelIs(null)
}

//channel empty with combination of allure
static List<Object[]> suite_ChannelIsEmpty_AllureIsCorrect(){
    suite_ChannelIsEmpty_AllureIsFalse() +  suite_ChannelIsEmpty_AllureIsTrue()
}
static List<Object[]> suite_ChannelIsEmpty_AllureIsFalse(){
    suite_AllureIsFalse_ChannelIs('')
}
static List<Object[]> suite_ChannelIsEmpty_AllureIsTrue(){
    suite_AllureIsTrue_ChannelIs('')
}

//channel whitespace with combination of allure
static List<Object[]> suite_ChannelIsWhitespace_AllureIsAny(){
    suite_ChannelIsWhitespace_AllureIsFalse() +  suite_ChannelIsWhitespace_AllureIsTrue()
}
static List<Object[]> suite_ChannelIsWhitespace_AllureIsFalse(){
    suite_AllureIsFalse_ChannelIs(' ')
}
static List<Object[]> suite_ChannelIsWhitespace_AllureIsTrue(){
    suite_AllureIsTrue_ChannelIs(' ')
}

//channel #channel with combination of allure
static List<Object[]> suite_ChannelIsDefined_AllureIsAny(){
    suite_ChannelIsDefined_AllureIsFalse() +  suite_ChannelIsDefined_AllureIsTrue()
}
static List<Object[]> suite_ChannelIsDefined_AllureIsFalse(){
    suite_AllureIsFalse_ChannelIs('#channel')
}
static List<Object[]> suite_ChannelIsDefined_AllureIsTrue(){
    suite_AllureIsTrue_ChannelIs('#channel')
}

private static List<Object[]> suite_AllureIsFalse_ChannelIs(channel){
    def result = []
    falseAllure().each {
        result << [channel, it] as Object[]
    }
    result
}
private static List<Object[]> suite_AllureIsTrue_ChannelIs(channel){
    def result = []
    trueAllure().each {
        result << [channel, it] as Object[]
    }
    result
}

private static falseAllure(){
    [null, '', ' ', 'a', 'no', 0, '0', false]
}
private static trueAllure(){
    ['y', true, 'true', 1, '1']
}

