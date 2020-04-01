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


To work with items in a |DDB| table using the Enhanced Client, first create a 
:aws-java-class:`DynamoDbEnhancedClient <services/enhanced/dynamodb/DynamoDbEnhancedClient>` from
an existing :aws-java-class:`DynamoDbClient <services/dynamodb/DynamoDbClient>` object.


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

.. literalinclude:: dynamodb.java2.enhanced.getitem.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.enhanced.getitem.main.txt
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
:aws-java-class:`DynamoDbTable <services/enhanced/dynamodb/DynamoDbTable>` is created, two items are queued up in separate batches, and then the items are batch-written in a single call.
Include multiple entries of :methodname:`addDeleteItem()` and :methodname:`addPutItem()`
(:aws-java-class:`part of WriteBatch.Builder <services/enhanced/dynamodb/model/WriteBatch>`)
in each batch as needed. To queue up changes to a multiple tables, specify a different 
:aws-java-class:`DynamoDbTable <services/enhanced/dynamodb/DynamoDbTable>` object in :methodname:`mappedTableResource()` and a different class in :methodname:`build()` for each.

**Imports**

.. literalinclude:: dynamodb.java2.enhanced.batchitems.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.enhanced.batchitems.main.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-dynamodb:`complete example <BatchWriteItems.java>` on GitHub.

.. _dynamodb-enhanced-batchitems:

Batch create (put) and delete items
===================================

You can batch together a series of put requests
(:aws-java-class:`PutItemEnhancedRequest <services/enhanced/dynamodb/model/PutItemEnhancedRequest>`) and delete requests
(:aws-java-class:`DeleteItemEnhancedRequest <services/enhanced/dynamodb/model/DeleteItemEnhancedRequest>`)
to one or more DynamoDB tables, and then send all of the changes in a single request.

In this code snippet, a
:aws-java-class:`DynamoDbTable <services/enhanced/dynamodb/DynamoDbTable>` is created, two items are queued up in separate batches, and then the items are batch-written in a single call.
Include multiple entries of :methodname:`addDeleteItem()` and :methodname:`addPutItem()`
(:aws-java-class:`part of WriteBatch.Builder <services/enhanced/dynamodb/model/WriteBatch>`)
in each batch as needed. To queue up changes to a multiple tables, specify a different 
:aws-java-class:`DynamoDbTable <services/enhanced/dynamodb/DynamoDbTable>` object in :methodname:`mappedTableResource()` and a different class in :methodname:`build()` for each.

**Imports**

.. literalinclude:: dynamodb.java2.enhanced.batchitems.import.txt
   :language: java

**Code**

.. literalinclude:: dynamodb.java2.enhanced.batchitems.main.txt
   :dedent: 8
   :language: java

See the :sdk-examples-java-dynamodb:`complete example <BatchWriteItems.java>` on GitHub.


More info
=========

* :ddb-dg:`Guidelines for Working with items <GuidelinesForitems>` in the |ddb-dg|
* :ddb-dg:`Working with items in DynamoDB <WorkingWithitems>` in the |ddb-dg|
