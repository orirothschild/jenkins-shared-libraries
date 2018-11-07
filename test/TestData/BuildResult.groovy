package TestData

class BuildResult {

	private Map properties = [:]

	void setProperty( String propertyName, Object v){
		switch(propertyName){
			case 'result':
				properties['currentResult'] = v; break
			case 'currentResult':
				throw new IllegalArgumentException("Property currentResult is readonly")
			default:
				properties[propertyName] = v
		}
	}
	Object getProperty(String propertyName){
		switch(propertyName){
			case 'result':
				return null
			default:
				properties[propertyName]
		}
	}

}
