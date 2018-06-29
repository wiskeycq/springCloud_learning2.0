eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
spring:
  application:
    name: api-gateway
  cloud:
    config:
      discovery:
        enabled: true
        service-id: CONFIG
      profile: dev
zuul:
  routes:
    myProductA:
      path: /myProduct5/**
      serviceId: product
      sensitiveHeaders: 
    myUserA:
      path: /myUser/**
      serviceId: user
      #排除敏感头
      sensitiveHeaders:
    #简洁写法【不按默认的路由规则，自定义路由】
    #product: /myProduct/**
  #排除某些路由
  ignored-patterns:
    - /**/product/listForOrder
    