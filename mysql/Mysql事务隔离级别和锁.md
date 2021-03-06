
## 一.事务的四种隔离级别：
1.未提交读(Read uncommitted)：允许脏读，也可能读到其他会话中未提交事务修改的数据。  
2.提交读(Read committed): 只能读取到已提交的数据。  
3.可重复读(Repeated Read): 在同一个事务内的查询都是事务开始时刻一致的。InnoDB引擎默认隔离级别。在SQL标准中，该级别消除了不可重复读，但存在幻读。  
4.可串行化(Serializable)：完全串行化的读，每次读都需要获取表级共享锁，读写相互都会堵塞。  

## 二.事务的隔离级别和锁
锁的介绍
### 1.RC 如何加锁
1.快照读：普通select,不加锁。

2.除了在外键约束检查(foreign-key constraint checking)以及重复键检查(duplicate-key checking)时会封锁区间，其他场景都只使用记录锁。此时，其他事务的插入依然可以执行，就可能导致，读取到幻影记录。

### 2.RR如何加锁
RR级别下，Mysql的读和事务的读的区别。
1.快照读(snapshot read):读取历史数据的方式,不加锁。  
普通的select,如：select * from table;

2.当前读(current read):读取数据库当前版本数据的方式，加锁的select/插入/更新/删除都属于当期读操作，处理的是当期版本数据，需要加锁。  
select * from table where ? lock in share mode;  
select * from table where ? for update;  
insert;  
update ;  
delete;  

①在唯一索引上使用唯一的查询条件，使用记录锁(record lock,行锁)，而不封锁记录之间的间隔，即不使用间隔锁(gap lock)和临建锁(next-key lock)  
②范围条件查询，使用间隔锁和临建锁，锁住索引之间的间隔，避免范围间插入记录，防止幻读。

示例：
（非唯一索引+范围当前读）
（非唯一索引+等值当前读）
（主键索引+范围当前读）


RC级别，行锁：使用索引作为条件查询时，会给索引字段加行锁。如果使用非索引字段，则会给整张表的所有数据行的加行，MySQL Server过滤条件，发现不满足后，会调用unlock_row方法，把不满足条件的记录释放锁 (违背了二段锁协议的约束)。

RR级别，间隙锁：如果查询条件使用的是没有索引的字段（即使没有匹配到任何数据）,会给全表加入gap锁。同时，它不能像上文中行锁一样经过MySQL Server过滤自动解除不满足条件的锁，因为没有索引，则这些字段也就没有排序，也就没有区间。除非该事务提交，否则其它事务无法插入任何数据。
### 3.RR和RC快照读的区别
1.RC下，快照读总是能读到最新的行数据快照，当然，必须是已提交事务写入的。  
2.RR下，某个事务首次read记录的时间为T，未来不会读取到T时间之后已提交事务写入的记录。  

## 三.InnoDB 死锁
