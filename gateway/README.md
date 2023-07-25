此模块功能：  权限验证、路由转发、限流熔断、跨域处理

权限验证
        设计： 利用网关作为服务总入口，子服务便不需再做权限校验
        核心类：JwtTokenFilter
        具体实现： 调用 authentication 鉴权模块进行权限验证

限流熔断
        设计： 利用网关做第一层限流熔断，子服务中因存在服务之间调用的场景需要，子服务也需要做二层限流熔断。
        核心类：GlobalPostFilter、GatewaySentinelExceptionConfig
        具体实现： GlobalPostFilter-路由转发的后置处理器，因子服务返回值都经其自身的全局异常处理器处理过，sentinel无法识别是否异常，需在此类中进行返回值的统一、异常结果转化
                  GatewaySentinelExceptionConfig-sentinel功能全局异常处理器

权限验证
        设计： 网关作为服务总入口，与页面打交道避免不了跨域问题
        核心类：CorsConfig
        具体实现： 配置后端允许跨域

