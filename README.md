# 多数据源的说明

1.将发布模式调整为：MultipleDB;
则会启动 PrimaryMapperConfig、OrderMapperConfig 这两个配置  
这种模式适合通过分包的模式来管理多数据源，确定了dao层使用的数据，不管Service  
层中怎么换数据源，Dao 都不会改变。Service 通过注解来声明事务，并需要指定事务管理器。  
默认会使用Primary指定的事务管理器。

这种模式适合：非主从模式的数据源之前的切换。   

2.将发布模式调整为：DynamicDS  

