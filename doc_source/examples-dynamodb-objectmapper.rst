.. Copyright 2010-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

###########################
Use the Object Mapper to work with items in |DDB|
###########################

.. meta::
   :description: How to use the |DDB| Object Mapper to retrieve (get) items in DynamoDB tables.
   :keywords: AWS for Java SDK code examples, Enhanced Client, Object Mapper, get items from DynamoDB tables


The Amazon DynamoDB Object Mapper is a high-level library that is part of the AWS SDK for Java v2. It offers a straightforward way to map client-side classes to DynamoDB tables. You define the relationships between tables and their corresponding object instances in your code, and then you can intuitively perform various create, read, update, or delete (CRUD) operations on tables or items in DynamoDB.

The SDK for Java v2 includes a set of annotations that you can use along with a :aws-java-class:`DynamoDbBean <enhanced/dynamodb/mapper/annotations/DynamoDbBean>` to quickly generate a :aws-java-class:`TableSchema <enhanced/dynamodb/TableSchema>` for mapping your classes to tables. Alternatively, if you declare each TableSchema explicitly, you do not need to include annotations in your classes.

To work with items in a |DDB| table using the Object Mapper, first create a 
:aws-java-class:`DynamoDbEnhancedClient <enhanced/dynamodb/DynamoDbEnhancedClient>` from
an existing :aws-java-class:`DynamoDbClient <services/dynamodb/DynamoDbClient>` object.

.. code-block:: java

    Region region = Region.US_EAST_1;
    DynamoDbClient ddb = DynamoDbClient.builder()
            .region(region)
            .build();

    DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build();

    createDynamoDBTable(enhancedClient);


.. _dynamodb-enhanced-mapper-before:

Before you use these examples
===================================

.. note:: Before you can use the examples in this topic, you first need to run the
   :sdk-examples-java-dynamodb:`CreateTable.java <CreateTable.java>` example. This
   will create a DynamoDB table called "Record" which the remaining examples use.


.. _dynamodb-enhanced-mapper-getitem:

Retrieve (get) an item from a table
===================================

To get an item from a DynamoDB table, you can retrieve the table as a 
:aws-java-class:`DynamoDbTable <enhanced/dynamodb/DynamoDbTable>` object and use a 
:aws-java-class:`GetItemEnhancedRequest <enhanced/dynamodb/model/GetItemEnhancedRequest>` object
to get the actual item.
This gives you access to key value pairs associated with the item.

For example, the code snippet below demonstrates one way to use the Object Mapper to get information from an item in a DynamoDB table.


**Imports**

.. literalinclude:: dynamodb.java2.enhanced_getitem.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.enhanced_getitem.main.txt
   :dedent: 7
   :language: java

See the :sdk-examples-java-dynamodb-enhanced:`complete example <DynamoDBMappingGetItem.java>` on GitHub.


.. _dynamodb-enhanced-mapper-batchitems:

Batch create (put) and delete items
===================================

You can batch together a series of put requests
(:aws-java-class:`PutItemEnhancedRequest <enhanced/dynamodb/model/PutItemEnhancedRequest>`) and delete requests
(:aws-java-class:`DeleteItemEnhancedRequest <enhanced/dynamodb/model/DeleteItemEnhancedRequest>`)
to one or more DynamoDB tables, and then send all of the changes in a single request.

In this code snippet, a
:aws-java-class:`DynamoDbTable <enhanced/dynamodb/DynamoDbTable>` is created, two items are queued up to be added to the table, and then the items are written to the table in a single call.
Include multiple entries of :methodname:`addDeleteItem()` and :methodname:`addPutItem()`
(:aws-java-class:`part of WriteBatch.Builder <enhanced/dynamodb/model/WriteBatch.Builder>`)
in each batch as needed. To queue up changes to a different table, add another instance of :methodname:`WriteBatch.builder()` and provide a corresponding :aws-java-class:`DynamoDbTable <enhanced/dynamodb/DynamoDbTable>` object in :methodname:`mappedTableResource()`.

**Imports**

.. literalinclude:: dynamodb.java2.enhanced_batchitems.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.enhanced_batchitems.main.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-dynamodb-enhanced:`complete example <BatchWriteItems.java>` on GitHub.


.. _dynamodb-enhanced-mapper-queryfilter:

Use a filtered query to get items from a table
===================================

You can get items from a table based on filterable queries, and then perform operations (e.g. return item values) on one or more of the items in the query results.

In the following code snippet, a filter is built by first defining the value or values you are searching for as an 
:aws-java-class:`AttributeValue <services/dynamodb/model/AttributeValue>` object. Then you put this into a :classname:`HashMap` and build an 
:aws-java-class:`Expression <enhanced/dynamodb/Expression>` from the classname:`HashMap`. Build a 
:aws-java-class:`QueryConditional <enhanced/dynamodb/model/QueryConditional>` to specify the primary key to match against in the query, and then execute the query on your 
:aws-java-class:`DynamoDbTable <enhanced/dynamodb/DynamoDbTable>`.


**Imports**

.. literalinclude:: dynamodb.java2.enhanced_queryfilter.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.enhanced_queryfilter.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-dynamodb-enhanced:`complete example <QueryRecordsWithFilter.java>` on GitHub.


.. _dynamodb-enhanced-mapper-scan:

Retrieve (get) all items from a table
===================================

When you want to get all of the records in a given DynamoDB table, the :methodname:`scan()` method of your
:aws-java-class:`DynamoDbTable <enhanced/dynamodb/DynamoDbTable>` object along with the :methodname:`items()` method
to create a set of results against which you can execute various item operations. For example, the following code
snippet prints out the ID values of each items in the "Record" table.

**Imports**

.. literalinclude:: dynamodb.java2.enhanced_scan.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.enhanced_scan.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-dynamodb-enhanced:`complete example <ScanRecords.java>` on GitHub.


More info
=========

* :ddb-dg:`Working with items in DynamoDB <WorkingWithItems>` in the |ddb-dg|
