package TestData

static List<Object[]> join(value1, value2){
    def collection1 = getCollection(value1)
    def collection2 = getCollection(value2)
    def result = []
    collection1.each { coll1 ->
        collection2.each { coll2 ->
            result << [coll1, coll2] as Object[]
        }
    }

    result
}

private static getCollection(obj){
    if (obj instanceof Collection){
        return obj
    }
    else return [obj]
}