
def call(String imageName){
    writeFile file: 'anchore_images', text: imageName
    try {
        anchore name: 'anchore_images'
    } catch(Exception e) {
        echo "${e}"
    }
}
