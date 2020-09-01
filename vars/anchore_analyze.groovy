
def call(String imageName){
    writeFile file: 'anchore_images', text: imageName
    anchore name: 'anchore_images'
}
