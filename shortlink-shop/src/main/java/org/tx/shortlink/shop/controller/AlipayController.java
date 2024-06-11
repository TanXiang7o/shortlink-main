package org.tx.shortlink.shop.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tx.shortlink.shop.DTO.req.OrderStatusReqDTO;
import org.tx.shortlink.shop.DTO.resp.ProductOrderRespDTO;
import org.tx.shortlink.shop.common.enums.OrderStatusEnum;
import org.tx.shortlink.shop.service.IProductOrderService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/alipay")
public class AlipayController {
    @Autowired
    private IProductOrderService productOrderService;


    private static final String appId = "9021000137691976";

    private static final String notifyUrl = "http://gmn78x.natappfree.cc/alipay/notify";

    private static final String appPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDWi5qA04HZPlCXK03/pUdo4OIh40vc5AuVkcNnlNICG2XBOij7VFFT8AyYkW5bEWWXuXnry2L6rWdzunnEmOzn2whknXCxS2H8omIAhFpdBuzuS5LNPszhp6sdOv+86KNvD9XQM+Jo034ZiyqGqUxEkRoj85QNToK90ZIROUGMtDAT+E5N6VwhUtXicdaFErO5qyswJNvm6Jvb/WjWh2Vl+C3kje9VKpkX0TXtkxZPZH5C3BjvLEGxJd5e8ThTzZj9b4ILBNcYgWbzj2kWNXMys29Av6L33pRyfYPn3i9U65bAWrPVw+Hupq3JsX9TCt0gKAB8kCC3y6siQOOrUeZ3AgMBAAECggEBALdwZ+tMbNK14CxQv4Ec7UC0P0UgVgYJCgqZtgzDhwh8KnWbZ9z0zBDLx8lPV58P8eBXmvJMVVXmm/SsdV7uLmlTswpsPyWoQaDDqq2sfKLbwAtDnMJWttEeDkWQ44a/FrZoQLWjFyvv4PqwLe0zpd1ef45LvsPd5BWusYh+fxhwitaVJibHlOKIhj+bgwm7ETgRh3nPQmaJIzJ5vGLF+b5ehf0GurYPJz9y+W6sOb5A1iUKwxGQsxMncwiOOlYtbyMBYOcvIEDiOGphPIt9oQSiMgjaggG+dMUV6ShaJ/KA/qxqaqjQYEnge3qkKenJvke4vsJDhk/imvrY34BrSKkCgYEA+Fz4RJcwijyp5r/lbm7GyrjeXuSPy64kQeH6WgvmHgwO/Up79Z2adQynqOT5OQtCkDyRF1ZoqjqjpHez/3baaMjcqq6GtmhSJFaXjyfs2+UfWUlhEwGqh1RftPu3H/fzYvawdaRn5sa4SvECV3RYg/2cDdj7LanmgD7XWHb9LuMCgYEA3SRuZcoC/gG/Aq2/2bApOKH6+9tronQdaGttVSLWnne/PVT59azBIf1T4RjSLwzxB9CSUjeDjRDF99d4yHlt4DsA+MFHpBEPHUaqMxjNbwVOpkq3SODOfCUr+W1h416OGwKwhRK7Ppduzp0qnUTNIPws0/KArREBuxo3scjhCl0CgYAdnrjdNUrMsb9xjaFHMl7y/mlG1+tuVy5rzQ+WfaTMIJQJEUEHYexx2TiFpAk1DGq72P1U0zFYW/X4P25wITSbGpl2oanfR0HEo68fm5tb/k9mzqGNKr2zthRT+VE1ttSIEPOqlBjwJPMFvQiSdYW4+B4MaLxMDd83nEcCfe5kEQKBgFrTjLgn2UTEKNSL0kExdiy5cqEgxLtrX2y0uqn97hIV8qjZDvTVaFlyd5H7JNCISzK/emlkPZahrf2WNRh5ryQqd8Kzd54uF3wZuuvTowSXNG9ePpsjIguhJMeEInFFM65qU1/Eir0xayRXLtp7GJkUmcDPWAQ8+O4E3Z0aeTtNAoGAFiVq0jknbUamCCHz7QVwZhLSAOerzrFqKLTj0rlOoEnDVIkk4Dm+VEHQhQaAStpOHmRRtqPtDeEovn2zx+7szIAksTdCE9E0WQmxrTiBYd6CN+uzj6DYm15gzr6eWO8tFIR51bAVSY1MP8DbPu+jpDaIaP5InqJ1Xzm0uIr0h+w=";

    private static final String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk+AN0Nd5pGfVpUu6W6xe918Sc5zkdeUM0XH+MvOqHNKxiegdVIbnzOZJ4VpVkwvUxKzi8snLkCZDSlJ25nn0NXRFPCHZQPMJL3XcMLaf245S2hCR8aVjpFtiT6pieFfyJ7Rw82Xk7NhCty3KpdAKhWAa+YV0IVlk7c5Z2rO5w7+W0PB0i938MA1KSpWhGmek1ppJTNO12GhzvbDCv/gCh0U+Ou0hTaW6+2yEG2r3Agq+RUdp/F3utlsfEIw5ycwu4NN/ssjhLOj4pnZy0KtjHijsjJQRbmzG5UGJY80+4ipHLC0VgM40LG+8cNpsBNu5wo6xsJiggcYBIV0QmO501wIDAQAB";

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "json";

    private static final String CHARSET = "UTF-8";

    private static final String SIGN_TYPE = "RSA2";

    private static Long userId;
    @GetMapping("/pay")
    public void pay(Long userId,String tradeNo, HttpServletResponse httpServletResponse) throws IOException, AlipayApiException {
        //TODO 通过tradeNo查看订单是否超时或是支付失败 trade_closed,如果支付失败就更新状态为关闭
        AlipayController.userId = userId;
        ProductOrderRespDTO productOrderRespDTO = productOrderService.getByTradeNo(tradeNo, userId);
        //更新支付状态
        if (productOrderRespDTO.getState().equals(OrderStatusEnum.NEW.name())){
            OrderStatusReqDTO orderStatusReqDTO = new OrderStatusReqDTO();
            orderStatusReqDTO.setNewState(OrderStatusEnum.PAYING.name());
            orderStatusReqDTO.setOldState(OrderStatusEnum.NEW.name());
            orderStatusReqDTO.setTradeNo(tradeNo);
            orderStatusReqDTO.setUserId(userId);
            productOrderService.updateStatus(orderStatusReqDTO);
        }
        //构造支付请求
        DefaultAlipayClient defaultAlipayClient = new DefaultAlipayClient(getAlipayConfig());
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(tradeNo);
        model.setTotalAmount(String.valueOf(productOrderRespDTO.getPayAmount()));
        model.setTimeExpire(productOrderRespDTO.getGmtCreate().plusMinutes(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        model.setSubject(productOrderRespDTO.getProductTitle());
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        request.setBizModel(model);
//        request.setReturnUrl("http://gmn78x.natappfree.cc/product-order/list");
        String form = "";
        try {
            AlipayTradePagePayResponse response = defaultAlipayClient.pageExecute(request);
            form = response.getBody();
            System.out.println(form);
            if (response.isSuccess()){
                log.info("支付宝支付中");
            }else {
                String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                System.out.println("诊断链接：" + diagnosisUrl);
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        httpServletResponse.setContentType("text/html;charset="+CHARSET);
        httpServletResponse.getWriter().write(form);
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }

    @PostMapping("/notify")
    public void notify(HttpServletRequest request) throws AlipayApiException {
        if(request.getParameter("trade_status").equals("TRADE_SUCCESS")){
            log.info("====支付宝支付回调");
            HashMap<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()){
                params.put(name, request.getParameter(name));
            }
            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8");
            if (checkSignature){
                log.info("交易名称："+params.get("subject"));
                log.info("交易状态："+params.get("trade_status"));
                log.info("支付宝交易号："+params.get("trade_no"));
                log.info("付款金额："+params.get("total_amount"));
                log.info("商户订单号："+params.get("out_trade_no"));
                log.info("付款时间："+params.get("gmt_payment"));

                ProductOrderRespDTO productOrderRespDTO = productOrderService.getByTradeNo(params.get("out_trade_no"), userId);
                if (productOrderRespDTO.getState().equals(OrderStatusEnum.PAY_TIMEOUT.name())){
                    log.info("订单支付超时");
                    //TODO 更新状态为REFUND，走退款逻辑
                    return;
                }

                OrderStatusReqDTO orderStatusReqDTO = new OrderStatusReqDTO();
                orderStatusReqDTO.setNewState(OrderStatusEnum.PAY_SUCCESS.name());
                orderStatusReqDTO.setOldState(OrderStatusEnum.PAYING.name());
                orderStatusReqDTO.setTradeNo(params.get("out_trade_no"));
                orderStatusReqDTO.setUserId(userId);
                orderStatusReqDTO.setPayNo(params.get("trade_no"));
                orderStatusReqDTO.setPayTime(LocalDateTime.parse(params.get("gmt_payment")));
                productOrderService.updateStatus(orderStatusReqDTO);
                //TODO 发送消息，创建资源包
            }
        }
        //其它结果无异步通知
    }

    @GetMapping("/query")
    public void query(String tradeNo) throws AlipayApiException {
        DefaultAlipayClient defaultAlipayClient = new DefaultAlipayClient(getAlipayConfig());

        // 构造请求参数以调用接口
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();

        // 设置订单支付时传入的商户订单号
        model.setOutTradeNo(tradeNo);

        // 设置查询选项
        List<String> queryOptions = new ArrayList<String>();
        queryOptions.add("trade_settle_info");
        model.setQueryOptions(queryOptions);

        // 设置支付宝交易号
//        model.setTradeNo("2014112611001004680 073956707");

        request.setBizModel(model);
        AlipayTradeQueryResponse response = defaultAlipayClient.execute(request);
        System.out.println(response.getBody());

        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
            // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
            // System.out.println(diagnosisUrl);
        }
    }

    private static AlipayConfig getAlipayConfig() {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(GATEWAY_URL);
        alipayConfig.setAppId(appId);
        alipayConfig.setPrivateKey(appPrivateKey);
        alipayConfig.setFormat(FORMAT);
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
        alipayConfig.setCharset(CHARSET);
        alipayConfig.setSignType(SIGN_TYPE);
        return alipayConfig;
    }

    /**
     * TODO 处理支付结果
     *
     */
    @GetMapping("/result")
    public void result(){

    }
}
