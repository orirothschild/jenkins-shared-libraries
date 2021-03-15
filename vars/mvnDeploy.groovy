
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
        credentialsId: 'JFROG_READER_CREDS',
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
    
    withMaven(globalMavenSettingsConfig: 'fa167938-9e8a-48ec-b5fd-601102327f0a', maven: 'jenkins_maven_3.3.9') {
        sh 'mvn clean deploy -X -V -B'
    }   
}

def call(String repoFilePath=null, String imageNameParam=null){

    call repofile: repoFilePath, imageName: imageNameParam

}
