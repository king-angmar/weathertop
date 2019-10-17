//package xyz.wongs.weathertop.base.common;
//
//import lombok.extern.slf4j.Slf4j;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//import java.net.InetAddress;
//
//@Slf4j
//@Configuration
//public class ESClientBuilder {
//
//    private static Client client = null;
//
//    @Value("${es.host}")
//    public String host;
//    @Value("${es.port}")
//    public int port;
//    @Value("${es.scheme}")
//    public String scheme;
//
//    /**
//     * 单例模式
//     *
//     * @return
//     */
//    public static Client builder() {
//        if (client == null) {
//            synchronized (ESClientBuilder.class) {
//                if (client == null) {
//                    client = createClient();
//                }
//            }
//        }
//        return client;
//    }
//
//    /**
//     * 初始化
//     *
//     * @return
//     */
//    private static Client createClient() {
//        // 设置
//        Settings settings = Settings.settingsBuilder().put("client.transport.sniff", true)
//                .put("cluster.name", clusterName).build();
//        Client client;
//        try {
//            client = TransportClient.builder().settings(settings).build()
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), Integer.parseInt(port)));
//        } catch (Exception e) {
//            log.error("----------createClient---创建ES客户端失败", e);
//            throw new RuntimeException("创建ES客户端失败 请联系开发人员");
//        }
//        return client;
//    }
//}
