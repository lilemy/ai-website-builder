# 小新智能网站生成器（后端）

## application-dev.yml 文件
```yaml
spring:
  web:
    resources:
      static-locations: classpath:/static/
  # mysql
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/ai_website_builder_dev
    username: root
    password: xxx
  data:
    # Redis 配置
    redis:
      # Redis数据库索引（默认为0）
      database: 0
      # Redis 服务器地址
      host: localhost
      # Redis 服务器连接端口
      port: 6379
      # Redis 服务器连接密码
      password: xxx
      # 连接超时时间
      timeout: 10s
      lettuce:
        pool:
          # 连接池最大连接数
          max-active: 200
          # 连接池最大阻塞等待时间（使用负值表示没有限制）
          max-wait: -1ms
          # 连接池中的最大空闲连接
          max-idle: 10
          # 连接池中的最小空闲连接
          min-idle: 0
# AI
langchain4j:
  open-ai:
    chat-model:
      base-url: https://api.deepseek.com
      api-key: your_api_key
      model-name: deepseek-chat
      max-tokens: 8192
      # 严格开启 Json 模式
      strict-json-schema: true
      response-format: json_object
      log-requests: true
      log-responses: true
      timeout: 600s
    streaming-chat-model:
      base-url: https://api.deepseek.com
      api-key: your_api_key
      model-name: deepseek-chat
      max-tokens: 8192
      log-requests: true
      log-responses: true
      timeout: 600s
# springdoc-openapi
springdoc:
  group-configs:
    - group: 'default'
      packages-to-scan: com.lilemy.aiwebsitebuilder.controller
# knife4j
knife4j:
  enable: true
  setting:
    language: zh_cn
# Sa-Token 配置 (文档: https://sa-token.cc)
sa-token:
  # token 名称（同时也是 cookie 名称）
  token-name: token_dev
  # token 有效期（单位：秒） 默认30天，-1 代表永久有效
  timeout: 2592000
  # token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认-1 代表不限制，永不冻结
  active-timeout: -1
  # 是否允许同一账号多地同时登录 （为 true 时允许一起登录, 为 false 时新登录挤掉旧登录）
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个 token （为 true 时所有登录共用一个 token, 为 false 时每次登录新建一个 token）
  is-share: false
  # token 风格（默认可取值：uuid、simple-uuid、random-32、random-64、random-128、tik）
  token-style: uuid
  # 是否输出操作日志
  is-log: true
```