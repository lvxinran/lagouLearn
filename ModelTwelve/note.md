Mysql数据库架构



存储引擎

Innodb引擎：

存储结构分为内存和磁盘两块：

内存分为|Buffer Pool查询缓存和Change Pool更新缓存 Log Buffer 日志缓存区

自适应hash索引：对bp进行优化。提高获取速度，监控表索引使用，寻找page数据

Buffer Pool ：由page页组成，存储数据和索引，访问表记录时会在page中进行缓存，减少磁盘io，提高效率

page管理：

page状态：free page 空闲；clean page被使用page，没有被修改过；dirty page：被使用配置，被修改过，和磁盘中不一致了

针对三种page类型，innodb通过3中链表结构来维护管理：

free list：空闲缓冲区，管理free page

flush list：表示需要刷新到磁盘的，管理dirty page，按修改时间排序

lru list：正在使用的缓冲区，管理clean page和dirty page，以midpoint为基点，前面的为new列表区，存储经常访问的数据，后面存放old列表区，存放较少数据

改进型lru算法维护：分为new和old两部分，添加元素并不是从表头插入，从中间midPoint插入

普通：末尾淘汰法

每当有新的page读到buffer pool时，判断是否有free ，有将其删除放入lru；没有的话使用lru淘汰链表尾页，分配给新页

ChangeBuffer ：写缓冲区，只适用于非唯一普通索引页；因为InnoDb要做唯一性校验，所以必须查询磁盘，然后讲记录放入BufferPool中，不会在ChangeBuffer操作。

LogBuffer：日志缓冲区，记录dml操作时产生的redo和undo日志，在空间满时会写入磁盘，未满时会有一些其他的写入策略。

innodb_flush_log_at_trx_commit参数控制日志刷新行为，默认为1：

0：每隔1s写日志文件和刷盘(写日志log buffer->os cache，刷盘os cache->磁盘文件)

1：事务提交，立刻写日志文件和刷盘，频繁io操作

2：事务提交，立刻写日志到os cache，每隔1s刷盘操作

磁盘

system Tablespace(系统表空间)：innodb data dictionary 数据字典，doublewrite buffer双写缓冲区，change buffer，undo logs

file-per-table tablespace(独立表空间)：默认存储表数据

undo tablespace(撤销表空间)

General tablespace(通用表空间)

temporary tablespace(临时表空间)

新版本(8.0)：

1. double write和数据字典 进行剥离
2. undo 日志进行合并
3. 临时表空间进行 global和session（用户）的区分

后台线程

io thread：wirte（4个），read（4个），insert buffer，log thread

read：读取，从磁盘到page页

write：写操作，将缓存脏页刷到磁盘

log：日志缓冲区 刷新到磁盘

insert buffer：将写缓冲内容更新到磁盘

purge thread（默认4个线程）：事务提交后，回收已经分配的undo页 show variables like innodb_purge_threads

page cleaner  thread：将脏数据刷新到磁盘，会去调用write thread，可以使redo log 循环

master thread：主线程，调度其他线程，将缓冲池中数据刷新到磁盘，保证数据一致性，包含：脏页刷新，undo回收等

内部两个主逻辑

每秒：刷新日志缓冲区到磁盘、合并写缓冲区数据（根据io压力）、刷新脏页到磁盘（根据脏页数量百分比）

每十秒：无条件执行每秒逻辑、删除无用undo日志