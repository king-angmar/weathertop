## 6.1. JWT整合

### 6.1.1. JWT概念

Json web token (JWT), 是为在网络应用环境间传递声明而执行的一种基于JSON的开放标准（(RFC 7519).定义了一种简洁的，自包含的方法用于通信双方之间以JSON对象的形式安全的传递信息。因为数字签名的存在，这些信息是可信的，JWT可以使用HMAC算法或者是RSA的公私秘钥对进行签名。

![JWT原理](doc/image/jwt/1.png)

### 6.1.2. 应用场景

身份认证在这种场景下，一旦用户完成了登陆，在接下来的每个请求中包含JWT，可以用来验证用户身份以及对路由，服务和资源的访问权限进行验证。由于它的开销非常小，可以轻松的在不同域名的系统中传递，所有目前在单点登录（SSO）中比较广泛的使用了该技术。 信息交换在通信的双方之间使用JWT对数据进行编码是一种非常安全的方式，由于它的信息是经过签名的，可以确保发送者发送的信息是没有经过伪造的。

### 6.1.3. 特点

- 简洁(Compact): 可以通过URL，POST参数或者在HTTP header发送，因为数据量小，传输速度也很快
- 自包含(Self-contained)：负载中包含了所有用户所需要的信息，避免了多次查询数据库
- 因为Token是以JSON加密的形式保存在客户端的，所以JWT是跨语言的，原则上任何web形式都支持。
- 不需要在服务端保存会话信息，特别适用于分布式微服务。

### 6.1.4. 结构

WT是由三段信息构成的，将这三段信息文本用.连接一起就构成了JWT字符串。

~~~
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ
~~~

JWT包含了三部分：
- Header 头部(标题包含了令牌的元数据，并且包含签名和/或加密算法的类型)
- Payload 负载 (类似于飞机上承载的物品)
- Signature 签名/签证

#### 6.1.4.1. Header

JWT的头部承载两部分信息：token类型和采用的加密算法

~~~
{ 
  "alg": "HS256",
   "typ": "JWT"
} 
~~~
声明类型:这里是jwt
声明加密的算法:通常直接使用 HMAC SHA256

#### 6.1.4.2. Payload

载荷就是存放有效信息的地方。
有效信息包含三个部分:
- 标准中注册的声明
- 公共的声明
- 私有的声明

#### 6.1.4.3. Signature

jwt的第三部分是一个签证信息，这个签证信息由三部分组成：
header (base64后的)
payload (base64后的)
secret
这个部分需要base64加密后的header和base64加密后的payload使用.连接组成的字符串，然后通过header中声明的加密方式进行加盐secret组合加密，然后就构成了jwt的第三部分。
密钥secret是保存在服务端的，服务端会根据这个密钥进行生成token和进行验证，所以需要保护好。

### 6.1.5. 环境集成

#### 6.1.5.1. 加入依赖包

~~~
    <dependency>
        <groupId>com.auth0</groupId>
        <artifactId>java-jwt</artifactId>
        <version>${JWT.VERSION}</version>
    </dependency>
~~~

#### 6.1.5.2. 自定义一个实体类

使用lombok简化实体类的编写

~~~
@Data
public class User {
    private Long userId;
    private String username;
    private int age;
    private int sex;


    public static User getAuther(){
        User auther = new User();
        auther.setUserId(1l);
        auther.setUsername("帅大叔");
        auther.setAge(24);
        auther.setSex(1);
        return auther;
    }
}
~~~

#### 6.1.5.3. 生成Token方法

~~~
public String createToken(Algorithm algorithm,Object data){
    String[] audience  = {"app","web"};
    return JWT.create()
            .withIssuer("rstyro")   //发布者
            .withSubject("test")    //主题
            .withAudience(audience)     //观众，相当于接受者
            .withIssuedAt(new Date())   // 生成签名的时间
            .withExpiresAt(DateUtils.offset(new Date(),2, Calendar.HOUR_OF_DAY))    // 生成签名的有效期,分钟
            .withClaim("data", JSON.toJSONString(data)) //存数据
            .withNotBefore(new Date())  //生效时间
            .withJWTId(UUID.randomUUID().toString())    //编号
            .sign(algorithm);
}
~~~

#### 6.1.5.4. 验证token返回数据

~~~
public Response getDataByToken(JwtDto jwtdto) throws Exception{
    Algorithm algorithm =null;
    DecodedJWT verify =null;
    if(StringUtils.isEmpty(jwtdto.getAlg())){
        throw new WeathertopRuntimeException(ResponseCode.ALGORITHM_CAN_NOT_NULL);
    }
    if("rs256".equalsIgnoreCase(jwtdto.getAlg())){
        algorithm = Algorithm.RSA256(CreateSecrteKey.getRSA256Key().getPublicKey(), null);
    }else {
        algorithm = Algorithm.HMAC256(SECRET);
    }
    JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer("rstyro")
            .build();
    try {
        verify = verifier.verify(jwtdto.getToken());
    }catch (TokenExpiredException ex){
        throw new WeathertopRuntimeException(ResponseCode.TOKEN_EXPIRED);
    }catch (JWTVerificationException ex){
        throw new JWTVerificationException(ResponseCode.SIGN_VERIFI_ERROR.getMsg());
    }
    String dataString = verify.getClaim("data").asString();

    Response response = new Response();
    response.setData(JSON.parseObject(dataString,User.class));
    return response;
}
~~~

### 6.1.6. 基于Jwt登陆用户认证实战



## 6.2. springboot-generator自动生成代码




## 6.3. 消息Kafka

### 6.3.1. 应用集成

加入依赖包

~~~
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
~~~

#### 6.3.1.1. 生产者

- application.yml

~~~
spring:
  kafka:
    bootstrap-servers: 192.168.147.129:9092
    producer:
      retries: 0
      batch-size: 16384
      buffer-memory: 33554432
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      properties:
        linger.ms: 1
~~~

- 发送消息

~~~
@RequestMapping("/kafka")
@RestController
public class TestKafkaProducerController {

    private Logger LOG = LoggerFactory.getLogger(TestKafkaProducerController.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping("/send")
    public Response send(){
        String topic = "wongs";
        User user = User.getAuther();
        Response response = new Response();
        response.setData(user);
        String data = JSONObject.toJSON(user).toString();
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, data);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                LOG.error("kafka sendMessage error, ex = {}, topic = {}, data = {}", ex, topic, data);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                LOG.info("kafka sendMessage success topic = {}, data = {}",topic, data);
            }
        });
        return response;
    }
}
~~~

#### 6.3.1.2. 消费者

- application.yml

~~~
spring:
  kafka:
    bootstrap-servers: 192.168.147.129:9092
    consumer:
      group-id: test-consumer-group
      enable-auto-commit: true
      auto-commit-interval: 1000ms
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      properties:
        session.timeout.ms: 15000
~~~

- 监听

~~~
@Slf4j
@Component
public class TestConsumer {

    @KafkaListener(topics = {"wongs"})
    public void listen (ConsumerRecord<?, ?> record) throws Exception {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("----------------- record =" + record);
            log.info("------------------ message =" + message);
        }
    }
}

~~~

#### 6.3.1.3. 演示

![MQ生产端](doc/image/kafka/04-kafka-server.png)

![MQ消费端](doc/image/kafka/03-kafka-client.png)