
def call(Object build = null){

    return build == null? currentBuild.currentResult == 'SUCCESS': build.currentResult == 'SUCCESS'

}
