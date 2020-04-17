.. Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

###########################
Use the enhanced client to map items in |DDB|
###########################

.. meta::
   :description: How to use the DynamoDB enhanced client to map client-side items to records in DynamoDB tables.
   :keywords: AWS for Java SDK code examples, enhanced client, object mapper, get items from DynamoDB tables


The |DDBlong| enhanced client is a high-level library that is part of the |sdk-java| version 2 (v2). It offers a straightforward way to map client-side classes to |DDB| tables. You define the relationships between tables and their corresponding model classes in your code. Then you can intuitively perform various create, read, update, or delete (CRUD) operations on tables or items in |DDB|.

The |sdk-java| v2 includes a set of annotations that you can use with a Java bean to quickly generate a :aws-java-class:`TableSchema <enhanced/dynamodb/TableSchema>` for mapping your classes to tables. Alternatively, if you declare each :aws-java-class:`TableSchema <enhanced/dynamodb/TableSchema>` explicitly, you don't need to include annotations in your classes.

To work with items in a |DDB| table using the enhanced client, first create a 
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



.. _dynamodb-enhanced-mapper-beantable:

Create a table using the enhanced client
===================================

To easily create a
:aws-java-class:`TableSchema <enhanced/dynamodb/TableSchema>` using the enhanced client, start by creating a
Java data class that includes a default public constructor and standardized names of getters and setters for
each property in the class. Include a class-level annotation to indicate it is a :classname:`DynamoDbBean` and, at a minimum,
include a :classname:`DynamoDbPartitionKey` annotation on the getter or setter for the primary key of the table record.

Once this data class has been defined, call :aws-java-class:`TableSchema <enhanced/dynamodb/TableSchema>`'s :methodname:`fromBean()`
with that data class to create the table schema.

See the code snippet below for an example of how to do this.


**Imports**

.. literalinclude:: dynamodb.java2.mapping.putitem.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.mapping.putitem.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-iam:`complete example <EnhancedPutItem.java>` on GitHub.



.. _dynamodb-enhanced-mapper-getitem:

Retrieve (get) an item from a table
===================================

To get an item from a |DDB| table, create a 
:aws-java-class:`DynamoDbTable <enhanced/dynamodb/DynamoDbTable>` object and call :methodname:`getItem()` with a 
:aws-java-class:`GetItemEnhancedRequest <enhanced/dynamodb/model/GetItemEnhancedRequest>` object
to get the actual item.
This gives you access to key-value pairs associated with the item.

For example, the following code snippet demonstrates one way to use the enhanced client to get information from an item in a |DDB| table.


**Imports**

.. literalinclude:: dynamodb.java2.mapping.getitem.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.mapping.getitem.main.txt
   :dedent: 7
   :language: java

See the :sdk-examples-java-iam:`complete example <EnhancedGetItem.java>` on GitHub.


.. _dynamodb-enhanced-mapper-batchitems:

Batch create (put) and delete items
===================================

You can batch a series of put requests
(:aws-java-class:`PutItemEnhancedRequest <enhanced/dynamodb/model/PutItemEnhancedRequest>`) and delete requests
(:aws-java-class:`DeleteItemEnhancedRequest <enhanced/dynamodb/model/DeleteItemEnhancedRequest>`)
to one or more |DDB| tables, and then send all of the changes in a single request.

In the following code snippet, a
:aws-java-class:`DynamoDbTable <enhanced/dynamodb/DynamoDbTable>` object is created, two items are queued up to be added to the table, and then the items are written to the table in a single call.
Include multiple entries of :methodname:`addDeleteItem()` and :methodname:`addPutItem()`
(:aws-java-class:`part of WriteBatch.Builder <enhanced/dynamodb/model/WriteBatch.Builder>`)
in each batch, as needed. To queue up changes to a different table, add another instance of :methodname:`WriteBatch.builder()` and provide a corresponding :aws-java-class:`DynamoDbTable <enhanced/dynamodb/DynamoDbTable>` object in :methodname:`mappedTableResource()`.

**Imports**

.. literalinclude:: dynamodb.java2.mapping.batchitems.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.mapping.batchitems.main.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-iam:`complete example <EnhancedBatchWriteItems.java>` on GitHub.


.. _dynamodb-enhanced-mapper-queryfilter:

Use a filtered query to get items from a table
===================================

You can get items from a table based on filterable queries, and then perform operations (for example, return item values) on one or more of the items in the query results.

In the following code snippet, you build a filter by first defining the value or values you're searching for as an 
:aws-java-class:`AttributeValue <services/dynamodb/model/AttributeValue>` object. Then you put this into a :classname:`HashMap` and build an 
:aws-java-class:`Expression <enhanced/dynamodb/Expression>` from the classname:`HashMap`. Build a 
:aws-java-class:`QueryConditional <enhanced/dynamodb/model/QueryConditional>` object to specify the primary key to match against in the query, and then execute the query on your 
:aws-java-class:`DynamoDbTable <enhanced/dynamodb/DynamoDbTable>` object.

.. note:: The :aws-java-class:`QueryConditional <enhanced/dynamodb/model/QueryConditional>` interface has several methods you can use to build your queries, including common conditional statements like greater than, less than, and in between.


**Imports**

.. literalinclude:: dynamodb.java2.mapping.queryfilter.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.mapping.queryfilter.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-iam:`complete example <EnhancedQueryRecordsWithFilter.java>` on GitHub.


.. _dynamodb-enhanced-mapper-scan:

Retrieve (get) all items from a table
===================================

When you want to get all of the records in a given |DDB| table, use the :methodname:`scan()` method of your
:aws-java-class:`DynamoDbTable <enhanced/dynamodb/DynamoDbTable>` object and the :methodname:`items()` method
to create a set of results against which you can execute various item operations. For example, the following code
snippet prints out the ID value of each item in the **Record** table.

**Imports**

.. literalinclude:: dynamodb.java2.mapping.scan.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.mapping.scan.main.txt
   :dedent: 4
   :language: java

See the :sdk-examples-java-iam:`complete example <EnhancedScanRecords.java>` on GitHub.


For more information, see :ddb-dg:`Working with items in DynamoDB <WorkingWithItems>` in the |ddb-dg|.
