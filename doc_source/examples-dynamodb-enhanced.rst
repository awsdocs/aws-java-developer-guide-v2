.. Copyright 2010-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.

###########################
Use the Enhanced Client to work with items in |DDB|
###########################

.. meta::
   :description: How to use the |DDB| Enhanced Client to retrieve (get) items in DynamoDB tables.
   :keywords: AWS for Java SDK code examples, Enhanced Client, get items from DynamoDB tables


The Amazon DynamoDB enhanced client is a high-level library that is part of the AWS SDK for Java v2. It offers a straightforward way to map client-side classes to DynamoDB tables. You define the relationships between tables and their corresponding object instances in your code, and then you can intuitively perform various create, read, update, or delete (CRUD) operations on tables or items in DynamoDB.
The SDK for Java v2 includes a set of annotations that you can use along with a :aws-java-class:`DynamoDbBean <services/enhanced/dynamodb/mapper/annotations/DynamoDbBean>` to create a :aws-java-class:`TableSchema <services/enhanced/dynamodb/TableScheman>` for mapping your classes to tables. Alternatively, if you declare each TableSchema explicitly, you do not need to include annotations in your classes.

To work with items in a |DDB| table using the Enhanced Client, first create a 
:aws-java-class:`DynamoDbEnhancedClient <services/enhanced/dynamodb/DynamoDbEnhancedClient>` from
an existing :aws-java-class:`DynamoDbClient <services/dynamodb/DynamoDbClient>` object.

IMPORTANT: Before you can use any of the other examples, you first need to run the :sdk-examples-java-enhanced-dynamodb:`CreateTable.java <CreateTable.java>` example. This will create a DynamoDB table called "Record" that the remaining examples will use.


.. _dynamodb-enhanced-getitem:

Retrieve (get) an item from a table
===================================

To get an item from a DynamoDB table, you can retrieve the table as a 
:aws-java-class:`DynamoDbTable <services/enhanced/dynamodb/DynamoDbTable>` object and use a 
:aws-java-class:`GetItemEnhancedRequest <services/enhanced/dynamodb/model/GetItemEnhancedRequest>` object
to get the actual item.
This will give you access to key value pairs associated with the item.

For example, the code snippet below demonstrates one way to use the Enhanced Client to get information from an item in a DynamoDB table.


**Imports**

.. literalinclude:: dynamodb.java2.enhanced_getitem.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.enhanced_getitem.main.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-dynamodb:`complete example <DynamoDBMappingGetItem.java>` on GitHub.


.. _dynamodb-enhanced-batchitems:

Batch create (put) and delete items
===================================

You can batch together a series of put requests
(:aws-java-class:`PutItemEnhancedRequest <services/enhanced/dynamodb/model/PutItemEnhancedRequest>`) and delete requests
(:aws-java-class:`DeleteItemEnhancedRequest <services/enhanced/dynamodb/model/DeleteItemEnhancedRequest>`)
to one or more DynamoDB tables, and then send all of the changes in a single request.

In this code snippet, a
:aws-java-class:`DynamoDbTable <services/enhanced/dynamodb/DynamoDbTable>` is created, two items are queued up to be added to the table, and then the items are written to the table in a single call.
Include multiple entries of :methodname:`addDeleteItem()` and :methodname:`addPutItem()`
(:aws-java-class:`part of WriteBatch.Builder <services/enhanced/dynamodb/model/WriteBatch>`)
in each batch as needed. To queue up changes to a different table, add another instance of :methodname:`WriteBatch.builder()` and provide a corresponding :aws-java-class:`DynamoDbTable <services/enhanced/dynamodb/DynamoDbTable>` object in :methodname:`mappedTableResource()`.

**Imports**

.. literalinclude:: dynamodb.java2.enhanced_batchitems.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.enhanced_batchitems.main.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-dynamodb:`complete example <BatchWriteItems.java>` on GitHub.


.. _dynamodb-enhanced-queryfilter:

Use a filtered query to get items from a table
===================================

You can get items from a table based on filterable queries, and then perform operations (e.g. return item values) on one or more of the items in the query results.

TODO

**Imports**

.. literalinclude:: dynamodb.java2.enhanced_queryfilter.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.enhanced_queryfilter.main.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-dynamodb:`complete example <QueryRecordsWithFilter.java>` on GitHub.


.. _dynamodb-enhanced-scan:

Retrieve (get) all items from a table
===================================

TODO

**Imports**

.. literalinclude:: dynamodb.java2.enhanced_scan.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.enhanced_scan.main.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-dynamodb:`complete example <ScanRecords.java>` on GitHub.


More info
=========

* :ddb-dg:`Guidelines for Working with items <GuidelinesForitems>` in the |ddb-dg|
* :ddb-dg:`Working with items in DynamoDB <WorkingWithitems>` in the |ddb-dg|
