This repo's purpose is to provide an example to illustrate how to recreate a bug
found in tracking event processors when running integration tests.

Steps to reproduce:
1. Create the DB container (for your local docker cache)
   ```sh
   cd db
   docker build --no-cache --tag example-axon/example-bug-db:SNAPSHOT .
   ```
2. Run the BookStateProjectionTest (a run configuration is available if using IntelliJ Idea).
   You will get the following exception:
   ```
       2020-08-25T21:01:54,608 INFO  [EventProcessor[book-projection]-1] o.a.e.TrackingEventProcessor$WorkerLauncher: An error occurred while attempting to claim a token for segment: 0. Will retry later...
       java.lang.IllegalArgumentException: The type identifier of the serialized object
        at org.axonframework.common.Assert.isTrue(Assert.java:56)
        at org.axonframework.common.Assert.notNull(Assert.java:80)
        at org.axonframework.serialization.SimpleSerializedObject.<init>(SimpleSerializedObject.java:47)
        at org.axonframework.eventhandling.tokenstore.AbstractTokenEntry.getSerializedToken(AbstractTokenEntry.java:111)
        at org.axonframework.eventhandling.tokenstore.AbstractTokenEntry.getToken(AbstractTokenEntry.java:121)
        at org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore.fetchToken(JpaTokenStore.java:202)
        at org.axonframework.eventhandling.TrackingEventProcessor$WorkerLauncher.lambda$run$2(TrackingEventProcessor.java:1392)
        at org.axonframework.common.transaction.TransactionManager.executeInTransaction(TransactionManager.java:47)
        at org.axonframework.eventhandling.TrackingEventProcessor$WorkerLauncher.run(TrackingEventProcessor.java:1391)
        at java.base/java.lang.Thread.run(Thread.java:834)
   ```