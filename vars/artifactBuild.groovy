
def call(Map params){

    def repoFilePath=null
    def imageNameParam=null

    if (params != null){
        repoFilePath = params.repofile
        imageNameParam = params.imageName
    }

    def repoFilePathLocal = './Dockerfile'
    if (repoFilePath?.trim()){
        repoFilePathLocal = repoFilePath
    }

    String imageNameLocal
    if (!imageNameParam?.trim()){
        imageNameLocal = "${imageName()}"
    }else{
        imageNameLocal = imageNameParam
    }
    if ("${env.ARTIFACTORY_SERVER}" == 'null') {
        error 'Variable ARTIFACTORY_SERVER is not defined'
    }

    def dockerImageName = "${env.ARTIFACTORY_SERVER}/${imageNameLocal}"

    rtServer (
        id: "jenkins-artifactory-server",
        url: 'https://repository.securedtouch.com/artifactory',
        credentialsId: 'JFROG_ADMIN_CREDS',
        bypassProxy: true,
        timeout: 300
    )

    rtMavenDeployer (
        id: "MAVEN_DEPLOYER",
        serverId: "jenkins-artifactory-server",
        releaseRepo: "maven-releases-local",
        snapshotRepo: "maven-snapshots-local"
    )

    rtMavenResolver (
        id: "MAVEN_RESOLVER",
        serverId: "jenkins-artifactory-server",
        releaseRepo: "maven-releases",
        snapshotRepo: "maven-snapshots"
    )
    rtMavenRun (
        tool: 'jenkins_maven_3.3.9', // Tool name from Jenkins configuration
        pom: 'pom.xml',
        goals: ' clean versions:set -DnewVersion=2.2.${BUILD_NUMBER} versions:commit -V -B'
        
    )
    
    // sh "docker build . -f \"${repoFilePathLocal}\" -t \"${dockerImageName}:${BRANCH_NAME}-${BUILD_ID}\" -t \"${dockerImageName}:${imageTag()}\""
}

def call(String repoFilePath=null, String imageNameParam=null){

    call repofile: repoFilePath, imageName: imageNameParam

}
