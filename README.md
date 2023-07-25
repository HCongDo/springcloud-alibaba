# springcloud-alibaba
springcloud-alibaba模块清单：

  gateway :
                            功能： 路由转发、限流熔断降级、权限验证
                            实现： gateway + sentinel + authentication模块
                            说明： 利于网关作为请求总入口的特点，将系统权限验证、限流熔断降级、跨域等需求在此实现，但需注意各业务系统之间也存在相互调用的场景，网关只做第一层限流，业务系统需进行二层限流实现

  authentication : 
                            功能： 鉴权模块
                            实现： spring security + oauth2.0 + JWT
                            说明： 负责用户认证、令牌发放与验证功能，供各个其他模块调用

  seata-client-standards : 
                            功能： 分布式鉴权标准客户端实现Demo
                            实现： seata + mybatis plus
                            说明： seata与mybatis都需要代理数据源，网上有出现过两者集成的时候mybatis plus会出现分页插件、自动填充等功能的失效。但此模块标准模式实现不会出现此情况

  seata-datasource-proxy :
                            功能： 分布式鉴权标准客户端重写数据源实现
                            实现： seata + mybatis plus
                            说明： 上面标准模式有描述过 seata 与 mybatis plus 集成某些场景会出现的插件失效问题，此模块重写了两者的datasource代理可解决此问题

  common：
                            功能： 公共模块
                            实现： 无特殊技术栈
                            说明： 主要包括工具类、统一返回值、fegin接口

  order:               
                            功能： 订单模块
                            实现： seata + senteinel + skywalking +  feign + mybatis plus
                            说明： 网关下第一层服务，主要做分布式事务、限流、追踪链、服务间相互调用、mybatis plus 相关功能测试demo
