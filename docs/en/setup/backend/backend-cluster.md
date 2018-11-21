# Cluster Management
In many product environments, backend need to support high throughputs and provide HA to keep robustness,
so you should need cluster management always in product env.
 
Backend provides several ways to do cluster management. Choose the one you need/want.

- [Zookeeper coordinator](#zookeeper-coordinator). Use Zookeeper to let backend detects and communicates
with each other.
- [Kubernetes](#kubernetes). When backend cluster are deployed inside kubernetes, you could choose this
by using k8s native APIs to manage cluster.


## Zookeeper coordinator
Zookeeper is a very common and wide used cluster coordinator. Set the **cluster** module's implementor
to **zookeeper** in the yml to active. 

Required Zookeeper version, 3.5+

Zookeeper version is required above 3.5 by Curator 4.0 lib, but actually Curator 4.0 is compatible with ZooKeeper 3.4.x.
To make ZooKeeper 3.4.x works, please replace the ZooKeeper 3.5+ library jar in `oap-libs` folder with ZooKeeper 3.4.x library.

```yaml
cluster:
  zookeeper:
    hostPort: localhost:2181
    # Retry Policy
    baseSleepTimeMs: 1000 # initial amount of time to wait between retries
    maxRetries: 3 # max number of times to retry
```

- `hostPort` is the list of zookeeper servers. Format is `IP1:PORT1,IP2:PORT2,...,IPn:PORTn`
- `hostPort`, `baseSleepTimeMs` and `maxRetries` are settings of Zookeeper curator client.


## Kubernetes
Require backend cluster are deployed inside kubernetes, guides are in [Deploy in kubernetes](backend-k8s.md).
Set implementor to `kubernetes`.

```yaml
cluster:
  kubernetes:
    watchTimeoutSeconds: 60
    namespace: default
    labelSelector: app=collector,release=skywalking
    uidEnvName: SKYWALKING_COLLECTOR_UID
```
