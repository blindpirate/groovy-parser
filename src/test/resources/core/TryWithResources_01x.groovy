import java.io.*


class Resource implements Closeable {
    int resourceId;
    static closedResourceIds = [];
    static exMsg = "failed to close";

    public Resource(int resourceId) {
        this.resourceId = resourceId;
    }

    public void close() {
        if (3 == resourceId) throw new IOException(exMsg);

        closedResourceIds << resourceId
    }
}

// test case 1
def a = 1;
try (Resource r1 = new Resource(1)) {
    a = 2;
}
assert Resource.closedResourceIds == [1]
assert 2 == a

// test case 2
Resource.closedResourceIds = []
final exMsg = "resource not found";
try {
    // try { ... } should throw the IOException, while the resource should be closed
    try (Resource r1 = new Resource(2)) {
        throw new FileNotFoundException(exMsg)
    }
} catch(FileNotFoundException e) {
    assert exMsg == e.getMessage()
}
assert Resource.closedResourceIds == [2]

// test case 3
Resource.closedResourceIds = []
a = 1;
try {
    try (Resource r1 = new Resource(3)) {
        a = 2;
    }
} catch (IOException e) {
    assert Resource.exMsg == e.getMessage()
}
assert 2 == a;
assert Resource.closedResourceIds == []

// test case 4
Resource.closedResourceIds = []
try {
    // try { ... } should throw the IOException, while the resource should be closed
    try (Resource r1 = new Resource(3)) {
        throw new FileNotFoundException(exMsg)
    }
} catch(FileNotFoundException e) {
    assert exMsg == e.getMessage()

    def suppressedExceptions = e.getSuppressed();
    assert suppressedExceptions.length == 1
    assert suppressedExceptions[0] instanceof IOException
    assert suppressedExceptions[0].getMessage() == Resource.exMsg
}
assert Resource.closedResourceIds == []


// test case 5
Resource.closedResourceIds = []
a = 1;
try (Resource r1 = new Resource(5);
Resource r2 = new Resource(6);) {
    a = 2;
}
assert Resource.closedResourceIds == [6, 5]
assert 2 == a

// test case 6
Resource.closedResourceIds = []
a = 1;
try (Resource r1 = new Resource(5);
Resource r2 = new Resource(6);
Resource r3 = new Resource(7);) {
    a = 2;
}
assert Resource.closedResourceIds == [7, 6, 5]
assert 2 == a


// test case 7
Resource.closedResourceIds = []
try (Resource r1 = new Resource(7)) {
    throw new FileNotFoundException(exMsg)
} catch(FileNotFoundException e) {
    assert exMsg == e.getMessage()
}
assert Resource.closedResourceIds == [7]

// test case 8
Resource.closedResourceIds = []
try (Resource r1 = new Resource(7);
Resource r2 = new Resource(8)) {
    throw new FileNotFoundException(exMsg)
} catch(FileNotFoundException e) {
    assert exMsg == e.getMessage()
}
assert Resource.closedResourceIds == [8, 7]


// test case 9
Resource.closedResourceIds = []
a = 1;
try (Resource r1 = new Resource(3)) {
    a = 2;
} catch (IOException e) {
    assert Resource.exMsg == e.getMessage()
}
assert 2 == a;
assert Resource.closedResourceIds == []


// test case 10
Resource.closedResourceIds = []
a = 1;
try (Resource r1 = new Resource(3);
Resource r2 = new Resource(4)) {
    a = 2;
} catch (IOException e) {
    assert Resource.exMsg == e.getMessage()
}
assert 2 == a;
assert Resource.closedResourceIds == [4]

// test case 11
Resource.closedResourceIds = []
a = 1;
try (Resource r0 = new Resource(2);
Resource r1 = new Resource(3);
Resource r2 = new Resource(4)) {
    a = 2;
} catch (IOException e) {
    assert Resource.exMsg == e.getMessage()
}
assert 2 == a;
assert Resource.closedResourceIds == [4, 2]


// test case 12
Resource.closedResourceIds = []
try (Resource r1 = new Resource(3);
Resource r2 = new Resource(4)) {
    throw new FileNotFoundException(exMsg)
} catch(FileNotFoundException e) {
    assert exMsg == e.getMessage()

    def suppressedExceptions = e.getSuppressed();
    assert suppressedExceptions.length == 1
    assert suppressedExceptions[0] instanceof IOException
    assert suppressedExceptions[0].getMessage() == Resource.exMsg
}
assert Resource.closedResourceIds == [4]

// test case 13
Resource.closedResourceIds = []
try (Resource r0 = new Resource(2);
Resource r1 = new Resource(3);
Resource r2 = new Resource(4)) {
    throw new FileNotFoundException(exMsg)
} catch(FileNotFoundException e) {
    assert exMsg == e.getMessage()

    def suppressedExceptions = e.getSuppressed();
    assert suppressedExceptions.length == 1
    assert suppressedExceptions[0] instanceof IOException
    assert suppressedExceptions[0].getMessage() == Resource.exMsg
}
assert Resource.closedResourceIds == [4, 2]

// test case 14
Resource.closedResourceIds = []
a = 1;
try (Resource r1 = new Resource(1)) {
    a += 2;
    try (Resource r2 = new Resource(2);Resource r4 = new Resource(4)) {
        a += 3;
        try (Resource r5 = new Resource(5);Resource r6 = new Resource(6);Resource r7 = new Resource(7)) {
            a += 4;
            try {
                try (Resource r3 = new Resource(3)) {
                    a += 5;
                }
            } catch (IOException e) {
                assert Resource.exMsg == e.getMessage()
            }
        }
    } catch(Exception e) {
        // ignored
    } finally {
        a += 10
    }
}
assert Resource.closedResourceIds == [7, 6, 5, 4, 2, 1]
assert 25 == a


