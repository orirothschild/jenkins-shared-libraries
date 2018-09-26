package TestData

static Map commonVariables(){ [
        JOB_NAME: "FAKE_Job_Name",
        BRANCH_NAME: "FAKE_Branch_Name",
        BUILD_URL: "FAKE_Build_Url",
        BUILD_ID: "1234"
    ]
}

//channel parameter is
static parametersChannelNullAllureNull(){[null, null]}//allure is false
static parametersChannelNullAllureEmpty(){[null, '']}//allure is false
static parametersChannelNullAllureWhitespace(){[null, ' ']}//allure is false
static parametersChannelNullAllureIncorrectSymbol(){[null, 'a']}//allure is false
static parametersChannelNullAllureNoExpression(){[null, 'no']}//allure is false
static parametersChannelNullAllureFalse(){[null, false]}//allure is false
static parametersChannelNullAllureYesExpression(){[null, 'y']}//allure is true
static parametersChannelNullAllureTrue(){[null, true]}//allure is true
static suiteParametersChannelNullAllureIsCorrect(){
    [
        parametersChannelNullAllureNull(),
        parametersChannelNullAllureEmpty(),
        parametersChannelNullAllureWhitespace(),
        parametersChannelNullAllureIncorrectSymbol(),
        parametersChannelNullAllureNoExpression(),
        parametersChannelNullAllureFalse(),
        parametersChannelNullAllureYesExpression(),
        parametersChannelNullAllureTrue()
    ]
}
static suiteParameterAllureFalseChannelNull(){
    [
        parametersChannelNullAllureNull(),
        parametersChannelNullAllureEmpty(),
        parametersChannelNullAllureWhitespace(),
        parametersChannelNullAllureIncorrectSymbol(),
        parametersChannelNullAllureNoExpression(),
        parametersChannelNullAllureFalse()
    ]
}
static suiteParameterAllureTrueChannelNull(){
    [
        parametersChannelNullAllureYesExpression(),
        parametersChannelNullAllureTrue()
    ]
}

//channel parameter is empty(default channel)
static parametersChannelEmptyAllureNull(){['', null]}//allure is true
static parametersChannelEmptyAllureEmpty(){['', '']}//allure is false
static parametersChannelEmptyAllureWhitespace(){['', ' ']}//allure is false
static parametersChannelEmptyAllureIncorrectSymbol(){['', 'a']}//allure is false
static parametersChannelEmptyAllureNoExpression(){['', 'no']}//allure is false
static parametersChannelEmptyAllureFalse(){['', false]}//allure is false
static parametersChannelEmptyAllureYesExpression(){['', 'y']}//allure is true
static parametersChannelEmptyAllureTrue(){['', true]}//allure is true
static suiteParameterChannelEmptyAllureIsCorrect(){
    [
        parametersChannelEmptyAllureNull(),
        parametersChannelEmptyAllureEmpty(),
        parametersChannelEmptyAllureWhitespace(),
        parametersChannelEmptyAllureIncorrectSymbol(),
        parametersChannelEmptyAllureNoExpression(),
        parametersChannelEmptyAllureFalse(),
        parametersChannelEmptyAllureYesExpression(),
        parametersChannelEmptyAllureTrue()
    ]
}
static suiteParameterAllureFalseChannelEmpty(){
    [
            parametersChannelEmptyAllureNull(),
            parametersChannelEmptyAllureEmpty(),
            parametersChannelEmptyAllureWhitespace(),
            parametersChannelEmptyAllureIncorrectSymbol(),
            parametersChannelEmptyAllureNoExpression(),
            parametersChannelEmptyAllureFalse()
    ]
}
static suiteParameterAllureTrueChannelEmpty(){
    [
            parametersChannelEmptyAllureYesExpression(),
            parametersChannelEmptyAllureTrue()
    ]
}

//channel parameter is whitespace(default channel)
static parametersChannelWhitespaceAllureNull(){[' ', null]}//allure is false
static parametersChannelWhitespaceAllureEmpty(){[' ', '']}//allure is false
static parametersChannelWhitespaceAllureWhitespace(){[' ', ' ']}//allure is false
static parametersChannelWhitespaceAllureIncorrectSymbol(){[' ', 'a']}//allure is false
static parametersChannelWhitespaceAllureNoExpression(){[' ', 'no']}//allure is false
static parametersChannelWhitespaceAllureFalse(){[' ', false]}//allure is false
static parametersChannelWhitespaceAllureYesExpression(){[' ', 'y']}//allure is true
static parametersChannelWhitespaceAllureTrue(){[' ', true]}//allure is true
static suiteParameterChannelWhitespaceAllureIsCorrect(){
    [
            parametersChannelWhitespaceAllureNull(),
            parametersChannelWhitespaceAllureEmpty(),
            parametersChannelWhitespaceAllureWhitespace(),
            parametersChannelWhitespaceAllureIncorrectSymbol(),
            parametersChannelWhitespaceAllureNoExpression(),
            parametersChannelWhitespaceAllureFalse(),
            parametersChannelWhitespaceAllureYesExpression(),
            parametersChannelWhitespaceAllureTrue()
    ]
}
static suiteParameterAllureFalseChannelWhitespace(){
    [
            parametersChannelWhitespaceAllureNull(),
            parametersChannelWhitespaceAllureEmpty(),
            parametersChannelWhitespaceAllureWhitespace(),
            parametersChannelWhitespaceAllureIncorrectSymbol(),
            parametersChannelWhitespaceAllureNoExpression(),
            parametersChannelWhitespaceAllureFalse()
    ]
}
static suiteParameterAllureTrueChannelWhitespace(){
    [
            parametersChannelWhitespaceAllureYesExpression(),
            parametersChannelWhitespaceAllureTrue()
    ]
}

//channel parameter is correct
static parametersChannelCorrectAllureNull(){['#channel', null]}//allure is false
static parametersChannelCorrectAllureEmpty(){['#channel', '']}//allure is false
static parametersChannelCorrectAllureWhitespace(){['#channel', ' ']}//allure is false
static parametersChannelCorrectAllureIncorrectSymbol(){['#channel', 'a']}//allure is false
static parametersChannelCorrectAllureNoExpression(){['#channel', 'no']}//allure is false
static parametersChannelCorrectAllureFalse(){['#channel', false]}//allure is false
static parametersChannelCorrectAllureYesExpression(){['#channel', 'y']}//allure is true
static parametersChannelCorrectAllureTrue(){['#channel', true]}//allure is true
static suiteParameterChannelCorrectAllureIsCorrect(){
    [
        parametersChannelCorrectAllureNull(),
        parametersChannelCorrectAllureEmpty(),
        parametersChannelCorrectAllureWhitespace(),
        parametersChannelCorrectAllureIncorrectSymbol(),
        parametersChannelCorrectAllureNoExpression(),
        parametersChannelCorrectAllureFalse(),
        parametersChannelCorrectAllureYesExpression(),
        parametersChannelCorrectAllureTrue()
    ]
}
static suiteParameterAllureFalseChannelCorrect(){
    [
            parametersChannelCorrectAllureNull(),
            parametersChannelCorrectAllureEmpty(),
            parametersChannelCorrectAllureWhitespace(),
            parametersChannelCorrectAllureIncorrectSymbol(),
            parametersChannelCorrectAllureNoExpression(),
            parametersChannelCorrectAllureFalse()
    ]
}
static suiteParameterAllureTrueChannelCorrect(){
    [
            parametersChannelCorrectAllureYesExpression(),
            parametersChannelCorrectAllureTrue()
    ]
}
