package me.zhengjie.morpheme.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/20 18:07
 */
@Service
@RequiredArgsConstructor
public class Agent {
    private String url;
    RestTemplate restTemplate = new RestTemplate();

    private Object get(String url) {
        return restTemplate.getForObject(url,String.class );
    }

    public String transfer(String word) {
        final String requestUrl = "http://apii.dict.cn/mini.php?q=" + word;
        return get(requestUrl).toString();
    }

    public static void main(String[] args) {
        Agent agent = new Agent();
        String time = agent.transfer("time");

        String test = time;


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

