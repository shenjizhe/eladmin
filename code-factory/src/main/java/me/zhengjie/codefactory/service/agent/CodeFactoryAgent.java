package me.zhengjie.codefactory.service.agent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import me.zhengjie.base.Const;
import me.zhengjie.base.Result;
import me.zhengjie.codefactory.domain.Config;
import me.zhengjie.codefactory.repository.ConfigRepository;
import me.zhengjie.codefactory.service.GitlabService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/20 18:07
 */
@Service
@RequiredArgsConstructor
public class CodeFactoryAgent {
    private final ConfigRepository configRepository;
    private String url;
    RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    private void init() {
        final Config byKey = configRepository.findByKey(Const.ConfigKey.CODE_FACTORY_URL);
        url = byKey.getValue();
    }

    private Object get(String url) {
        return request(url,RequestMethod.GET,null);
    }

    private Object post(String url,Object request) {
        return request(url,RequestMethod.POST,request);
    }

    private Object request(String url, RequestMethod method, Object request){
        ResponseEntity<Map> result = null;
        switch (method) {
            case GET:
                result = restTemplate.getForEntity(url, Map.class);
                break;
            case POST:
                result = restTemplate.postForEntity(url, request,Map.class);
                break;
            case DELETE:
                restTemplate.delete(url);
                break;
            case PUT:
                restTemplate.put(url,request);
                break;
            default:
                return null;
        }
        final Object data = result.getBody().get("data");
        return data;
    }

    public String generate(Long id) {
        final String requestUrl = url + "code-factory/generate/" + id + "?outputType=memory";
        return post(requestUrl, null).toString();
    }

    public CodeOutput output(String uuid) {
        final String requestUrl = url + "/code-factory/output/" + uuid;
        final Object o =get(requestUrl);
        final JSONObject jo = (JSONObject)JSON.toJSON(o);
        final CodeOutput codeOutput = jo.toJavaObject(CodeOutput.class);
        return codeOutput;
    }

    public CodeOutputItem outputItem(String uuid) {
        final String requestUrl = url + "/code-factory/output-item/" + uuid;
        final Object o =get(requestUrl);
        final JSONObject jo = (JSONObject)JSON.toJSON(o);
        final CodeOutputItem item = jo.toJavaObject(CodeOutputItem.class);
        return item;
    }

    public static void main(String[] args) {
//        final CodeFactoryAgent agent = new CodeFactoryAgent(null);
//        agent.url = "http://localhost:8071/";
//        final String generate = agent.generate(12L);
//        final CodeOutput output = agent.output(generate);
//
//        final List<String> items = output.getItems();
//        for (String uuid : items) {
//            CodeOutputItem item = agent.outputItem(uuid);
//            final Result master = gitlabService.pushCode(12L, item.getFullPath(), "master");
//            logger.info(item.getCode());
//        }
    }
}
