Task:

Upgrade your bank class from the example 01 keeping in mind that transfers run concurrently. In other words,
two transfers may have one account in common, so no money must be lost during the transactions - ie. the total sum
must not change. Use locks to protect your critical section from being accessed from multiple threads at the same time.